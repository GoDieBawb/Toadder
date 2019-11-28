package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    GameManager gm;
    Player p;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        this.setDisplayStatView(false);
        this.setDisplayFps(false);
        flyCam.setEnabled(false);
        
        Node     pn    = new Node();
        Box      pbox  = new Box(1.5f,1.5f,1.5f);
        Geometry pgeom = new Geometry("Box", pbox);
        Material pMat  = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        p              = new Player(pn);
        
        pMat.setColor("Color", ColorRGBA.Brown);
        pn.attachChild(pgeom);
        pgeom.setMaterial(pMat);
        
        rootNode.attachChild(pn);
        
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0,15,0));
        cam.lookAtDirection(new Vector3f(0,0,0), new Vector3f(0,1,0));
        gm = new GameManager(guiNode, assetManager, rootNode, p);
        new InteractionManager(p, gm, inputManager);
        gm.initScene();
    }

    @Override
    public void simpleUpdate(float tpf) {
        gm.update(tpf);
        Vector3f laneCenter = gm.getLanes().get(p.getCurrentLane()).getNodes().get(4).getWorldTranslation();
        cam.setLocation(laneCenter.add(0,50f,-50f));
        cam.lookAt(laneCenter, new Vector3f(0,1,0));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
