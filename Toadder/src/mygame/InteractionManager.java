/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Bob
 */
public class InteractionManager implements ActionListener {
    
    private final InputManager inputManager;
    private final Player       player;
    
    public InteractionManager(Player player, InputManager inputManager){
        this.inputManager = inputManager;
        this.player       = player;
        setUpKeys();
    }
    
    //Sets up key listeners for the action listener
    private void setUpKeys(){
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
    }    
    
    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
    
        if (!isPressed) player.move(binding);
        
    }    
    
}
