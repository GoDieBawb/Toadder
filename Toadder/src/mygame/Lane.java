/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class Lane {
    
    public static float WIDTH  = Display.getWidth()/25;
    public static float HEIGHT = Display.getHeight()/70;
    private static int  LANE = 0;
    
    private final Node               model;
    private final String             type;
    private final ArrayList<Spatial> nodes;
    private final ArrayList<Entity>  entities; 
    
    private float   speed;
    private boolean isLeft;
    
    private int gap;
    
    public Lane(String type, AssetManager a) {
        LANE++;
        isLeft     = true;
        speed      = randomInt(50,90)/10;
        model      = new Node();
        nodes      = new ArrayList<>();
        entities   = new ArrayList<>();
        gap        = randomInt(1,3);
        this.type  = type;
        
        Box      mbox  = new Box(1,1,1);
        Geometry mgeom = new Geometry("Box", mbox);
        
        Box      obox  = new Box(1,1,1);
        Geometry ogeom = new Geometry("Box", obox);
        
        int x = -5;
        
        while (x < 6) {
            Box      nbox  = new Box(1,1,1);
            Geometry ngeom = new Geometry("Box", nbox);   
            Material nMat  = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
            model.attachChild(ngeom);
            ngeom.setMaterial(nMat);
            ngeom.setLocalTranslation(x*5, 0, 0);
            nodes.add(ngeom);
            if (LANE!=7)
                ngeom.setCullHint(Spatial.CullHint.Always);
            x++;
        }
        
        model.attachChild(mgeom);
        model.attachChild(ogeom);
        
        
        mgeom.setLocalTranslation(0,.1f,0);
        
        mgeom.setLocalScale(WIDTH-HEIGHT/20, .2f, HEIGHT-HEIGHT/20);
        ogeom.setLocalScale(WIDTH, .2f, HEIGHT);
        
        Material mMat = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
        Material oMat = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
        
        
        switch (type) {
            case "Grass":
                mMat.setColor("Color", ColorRGBA.Green);
                oMat.setColor("Color", ColorRGBA.Yellow);
                break;
            case "Water":
                mMat.setColor("Color", ColorRGBA.Blue);
                oMat.setColor("Color", ColorRGBA.Orange);
                break;
            default:
                mMat.setColor("Color", ColorRGBA.Gray);
                oMat.setColor("Color", ColorRGBA.White);
                break;
        }
        
        mgeom.setMaterial(mMat);
        ogeom.setMaterial(oMat);
        
        
    }
    
    public void addEntity(AssetManager a) {
        
        
        boolean  create         = false;
        
        int         firstNode   = 10;
        if (isLeft) firstNode   = 0;
        
        int         gapNode     = firstNode-gap;
        if (isLeft) gapNode     = firstNode+gap;
        
        Vector3f firstNodeSpot  = nodes.get(firstNode).getWorldTranslation();
        Vector3f gapNodeSpot    = nodes.get(gapNode).getWorldTranslation();
        
        if (entities.isEmpty()) {
            firstNodeSpot  = nodes.get(randomInt(0,9)).getWorldTranslation();
            create         = true;
        }
        
        else {
            
            Vector3f prevEntSpot   = entities.get(entities.size()-1).getModel().getWorldTranslation();

            if (isLeft) {

                if (prevEntSpot.x >gapNodeSpot.x) {
                    create = true;
                }

            }

            else {
                
                if (prevEntSpot.x < gapNodeSpot.x) {
                    create = true;
                }

            }
        
        }
        
        if (create) {
            gap = randomInt(1,4);
            Entity e = new Entity(a,"Car");
            model.getParent().attachChild(e.getModel());
            entities.add(e);
            e.getModel().setLocalTranslation(firstNodeSpot.addLocal(0, 0, 0));        
        }
        
    }
    
    public Node getModel() {
        return model;
    }
    
    public ArrayList<Spatial> getNodes() {
        return nodes;
    }
    
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    
    public boolean isLeft() {
        return isLeft;
    }
    
    public void setIsLeft(boolean val) {
        isLeft = val;
    }    
    
    public float getSpeed() {
        return speed;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    private static final Random R = new Random();
    
    public static int randomInt(int min, int max) {
        return R.nextInt((max - min) + 1) + min;
    }          
    
}
