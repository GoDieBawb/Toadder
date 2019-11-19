/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class GameManager {
    
    private final ArrayList<Lane>   lanes;
    private final Node              sceneNode;
    private final Player            player;
    private final AssetManager      assetManager;
    
    private Material redMat;
    
    public GameManager(AssetManager assetManager, Node rootNode,Player player) {
        this.assetManager = assetManager;
        this.player       = player;
        sceneNode         = new Node();
        lanes             = new ArrayList<>();
        rootNode.attachChild(sceneNode);
    }
    
    public void initScene() {
        
        redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.setColor("Color", ColorRGBA.Red);
        
        int x = 0;
        
        while (x <= 6) {
            
            String            type = "Road";
            if (x==0 || x==6) type = "Grass";
            if (x==3)         type = "Water";
            Lane l = new Lane(type, assetManager);
            if (x==1||x==4)l.setIsLeft(false);
            lanes.add(l);
            
            sceneNode.attachChild(l.getModel());
            l.getModel().setLocalTranslation(0, 0, Lane.HEIGHT*x*2);
            
            x++;
            
        }
        
    }
    
    public ArrayList<Lane> getLanes() {
        return lanes;
    }
    
    private void updatePlayer() {
        Lane currentLane    = lanes.get(player.getCurrentLane());
        Spatial currentNode = currentLane.getNodes().get(player.getCurrentNode());
        player.getModel().setLocalTranslation(currentNode.getWorldTranslation());
        currentNode.setMaterial(redMat);
    }
    
    private void updateLanes(float tpf) {
        
        for (int i = 1; i < lanes.size()-1; i++) {
            
            ArrayList<Entity> removes = new ArrayList<>();
            
            Lane l  = lanes.get(i);
            int dir = -1;
            
            if (l.isLeft()) dir = 1;
            
            if (l.getEntities().size() < 5) {
                l.addEntity(assetManager);
            }
            
            for (int j = 0; j < l.getEntities().size(); j++) {
                
                Entity e = l.getEntities().get(j);
                e.getModel().move(l.getSpeed()*dir*tpf, 0, 0);
                
                if (!l.isLeft()) {
                    
                    if (e.getModel().getWorldTranslation().x  < -Lane.WIDTH)
                        removes.add(e);
                    
                }
                
                else {
                    
                    if (e.getModel().getWorldTranslation().x  > Lane.WIDTH)
                        removes.add(e);
                    
                }
                
            }
            
            for(Entity e : removes) {
                e.getModel().removeFromParent();
                l.getEntities().remove(e);
            }
            
        }
        
    }
    
    public void update(float tpf) {
        updatePlayer();
        updateLanes(tpf);
    }
    
}
