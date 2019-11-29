/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
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
    
    private final ArrayList<Lane>    lanes;
    private final ArrayList<Integer> goals;
    private final Node               sceneNode;
    private final Node               guiNode;
    private final Player             player;
    private final AssetManager       assetManager;
    private final BitmapText         titleText;
    private final BitmapText         scoreText;
    private final BitmapFont         font;
    
    private Material redMat;
    private boolean  titleVisible;
    
    public GameManager(Node guiNode, AssetManager assetManager, Node rootNode,Player player) {
        this.assetManager = assetManager;
        this.player       = player;
        this.guiNode      = guiNode;
        sceneNode         = new Node();
        lanes             = new ArrayList<>();
        goals             = new ArrayList<>();
        font              = assetManager.loadFont("Interface/Fonts/Default.fnt");
        titleText         = new BitmapText(font, false);
        scoreText         = new BitmapText(font, false);
        titleVisible      = false;
        rootNode.attachChild(sceneNode);
        showTitle();
    }
    
    private void showTitle() {
        titleText.setColor(ColorRGBA.White);
        titleText.setText("Toadder");
        titleText.setSize(Display.getHeight()/10);
        titleText.setLocalTranslation(300, titleText.getLineHeight(), 0);
        titleText.setLocalTranslation(Display.getWidth()/2-titleText.getLineWidth()/1.5f, Display.getHeight()/3, 0);
        
        scoreText.setText("Press Space");
        scoreText.setSize(Display.getHeight()/20);
        scoreText.setLocalTranslation(Display.getWidth()/2-scoreText.getLineWidth()/1.5f, Display.getHeight()/5f, 0);
        
        guiNode.attachChild(scoreText);
        guiNode.attachChild(titleText);
        titleVisible = true;
    }
    
    private void reset() {
        
        for (int i = 0; i < lanes.get(lanes.size()-1).getNodes().size(); i++) {
            Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            m.setColor("Color", ColorRGBA.White);
            lanes.get(lanes.size()-1).getNodes().get(i).setMaterial(m);
        }
        
        goals.clear();
        
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
        
        Lane currentLane = lanes.get(player.getCurrentLane());
        
        if (player.getCurrentLane() == lanes.size()-1) {
            
            if (goals.contains(player.getCurrentNode())) {
                player.setCurrentLane(player.getCurrentLane()-1);
                return;
            }
            
            else {
                System.out.println("Adding: " + player.getCurrentNode());
                currentLane.getNodes().get(player.getCurrentNode()).setMaterial(redMat);
                goals.add(player.getCurrentNode());
                player.setCurrentNode(5);
                player.setCurrentLane(0);
                player.setScore(player.getScore()+1);
                if (goals.size()==11) {
                    reset();
                    increaseDifficulty();
                }
            }
            
        }
        
        Spatial currentNode = currentLane.getNodes().get(player.getCurrentNode());
        player.getModel().setLocalTranslation(currentNode.getWorldTranslation());
        scoreText.setText("Score: " + player.getScore());
        
    }
    
    private void increaseDifficulty() {
        for (int i = 0; i < lanes.size(); i++) {
            Lane currentLane = lanes.get(i);
            currentLane.setSpeed(currentLane.getSpeed()+currentLane.getSpeed()/10);
        }
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
    
    public boolean getTitleVisibile() {
        return titleVisible;
    }
    
    public void hideTitle() {
        titleVisible = false;
        titleText.removeFromParent();
        scoreText.setSize(Display.getHeight()/30);
        scoreText.setText("Score: " + player.getScore());
        scoreText.setLocalTranslation(Display.getWidth()*.9f - scoreText.getLineWidth(), Display.getHeight()*.9f, 0);
    }
    
    private void checkCollision() {
        
        Spatial playerModel = player.getModel();
        
        for (int i = 0; i < lanes.get(player.getCurrentLane()).getEntities().size(); i++) {
            
            Spatial entityModel = lanes.get(player.getCurrentLane()).getEntities().get(i).getModel();
            
            CollisionResults results = new CollisionResults();
            playerModel.collideWith(entityModel.getWorldBound(), results);
            
            if (results.size() > 0) {
                player.die();
                reset();
                updatePlayer();
                showTitle();
            }
            
        }
        
    }
    
    public void update(float tpf) {
        
        if (titleVisible) {
            return;
        }
        
        updatePlayer();
        updateLanes(tpf);
        checkCollision();
        
    }
    
}
