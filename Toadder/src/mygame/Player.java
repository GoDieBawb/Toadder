/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class Player {
    
    private Node  model;
    private int   currentLane;
    private int   currentNode;
    private int   score;
    
    public Player(Node model) {
        this.model  = model;
        currentLane = 0;
        currentNode = 5;
    }
    
    public void move(String dir) {
        
        switch(dir)
        {
            case "Up":
                if (currentLane != 6) currentLane++;
                break;
            case "Down":
                if (currentLane != 0) currentLane--;
                break;
            case "Left":
                if (currentNode != 10) currentNode++;
                break;
            case "Right":
                if (currentNode != 0) currentNode--;
                break;
        }
        
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setCurrentLane(int currentLane) {
        this.currentLane = currentLane;
    }
    
    public int getCurrentLane() {
        return currentLane;
    }
    
    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }
    
    public int getCurrentNode() {
        return currentNode;
    }
    
    public Node getModel() {
        return model;
    }
    
    public void die() {
        score       = 0;
        currentNode = 5;
        currentLane = 0;
    }
    
}
