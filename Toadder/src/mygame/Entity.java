/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bob
 */
public class Entity {
    
    private Node    model;
    private Lane    lane;
    private String  type;
    
    public Entity(AssetManager assetManager, String type) {
        model           = new Node();
        Box      pbox  = new Box(2f,1.5f,1.35f);
        Geometry pgeom = new Geometry("Box", pbox);
        Material pMat  = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        pMat.setColor("Color", ColorRGBA.Pink);
        pgeom.setMaterial(pMat);
        model.attachChild(pgeom);
    }
    
    public Node getModel() {
        return model;
    }
    
}
