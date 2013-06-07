package mygame;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.rmi.transport.Target;

// Raditya Danang - 1401077453
// Albert - 

public class Main extends SimpleApplication 
implements PhysicsCollisionListener
{
    
    
    //Deklarasi
    Material mat_terrain;
    Material mat_terrain1;
    TerrainQuad terrain;
    TerrainQuad terrain1;
    float kecepatan = 0.4f;
    Spatial hero, landscape, rudal, platform1, platform2, platform3, platform4;
    CharacterControl herocontrol;
    BulletAppState bulletAppState = new BulletAppState();
    RigidBodyControl terrain_control1, rudalc, terrain_control2, platfrm1, platfrm2, platfrm3, platfrm4;
    String notiftext = "";
    AnimControl animctrl;
    AnimChannel animchn1, animchn2, animchn3;
    Vector3f walkDirection = new Vector3f();
    boolean left = false,
            right = false,
            forward = false,
            backward = false;
    

    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.settings = new AppSettings(true);
        app.settings.setResolution(800, 600);
        app.start();
    }
    
    void initHero()
    {
        stateManager.attach(bulletAppState);
        
        //Bikin Ambient Light Biar Heronya Keliatan
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
        
        //Load Hero
        hero = assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        //hero.setLocalScale(5f);
        hero.setLocalTranslation(-370, 20, -380);
        rootNode.attachChild(hero);
        
        //Hero Control
        CapsuleCollisionShape capsuleCollisionShape = new CapsuleCollisionShape(3, 4); //3,4
        herocontrol = new CharacterControl(capsuleCollisionShape, 0.05f);
        hero.addControl(herocontrol);
        herocontrol.setJumpSpeed(25);
        bulletAppState.getPhysicsSpace().add(herocontrol);
        
        //Hero Animation
        animctrl = hero.getControl(AnimControl.class);
        animctrl.addListener(animEventListener);
        animchn1 = animctrl.createChannel();
        animchn2 = animctrl.createChannel();
        animchn3 = animctrl.createChannel();
        
        animchn1.setAnim("IdleTop");
        animchn2.setAnim("IdleBase");
        //animchn3.setAnim("JumpLoop");
        
        for(String nama : animctrl.getAnimationNames())
            System.out.println(nama);
        //animchn3.setAnim("Dance");
        
    }
    
    void initPlatforms()
    {
        stateManager.attach(bulletAppState);
        //Material Setting
        Material matplat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture plattex = assetManager.loadTexture("Textures/metalfloor.jpg");
        matplat.setTexture("ColorMap", plattex);
        
        /*## PLATFORM-1 ##*/
        //Setting Spatial
        platform1 = assetManager.loadModel("Scenes/Platform1/Platform1.mesh.xml");
        platform1.setMaterial(matplat);
        platform1.scale(5f);
        platform1.setLocalTranslation(-370, -3, -380);
        
        //Physic
        platfrm1 = new RigidBodyControl(0);
        platform1.addControl(platfrm1);
        
        rootNode.attachChild(platform1);
        bulletAppState.getPhysicsSpace().add(platfrm1);
        /*## END-OF-PLATFORM-1 ##*/
        
        /*## PLATFORM-2 ##*/
        //Setting Spatial
        platform2 = assetManager.loadModel("Scenes/Platform2/Platform2.mesh.xml");
        platform2.setMaterial(matplat);
        platform2.scale(5f);
        platform2.setLocalTranslation(-330, -3, -360);
        platform2.setLocalRotation(new Quaternion(5, 0, 1, 0));
        
        //Physic
        platfrm2 = new RigidBodyControl(0);
        platform2.addControl(platfrm2);
        
        rootNode.attachChild(platform2);
        bulletAppState.getPhysicsSpace().add(platfrm2);
        /*## END-OF-PLATFORM-2 ##*/
        
        /*## PLATFORM-3 ##*/
        //Setting Spatial
        platform3 = assetManager.loadModel("Scenes/Platform2/Platform2.mesh.xml");
        platform3.setMaterial(matplat);
        platform3.scale(5f);
        platform3.setLocalTranslation(-270, -3, -340);
        platform3.setLocalRotation(new Quaternion(7, 0, 1, 0));
        
        //Physic
        platfrm3 = new RigidBodyControl(0);
        platform3.addControl(platfrm3);
        
        rootNode.attachChild(platform3);
        bulletAppState.getPhysicsSpace().add(platfrm3);
        /*## END-OF-PLATFORM-3 ##*/
        
        /*## PLATFORM-4 ##*/
        //Setting Spatial
        platform4 = assetManager.loadModel("Scenes/Platform3/Platform3.mesh.xml");
        platform4.setMaterial(matplat);
        platform4.scale(5f);
        platform4.setLocalTranslation(-200, 5, -300);
        platform4.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm4 = new RigidBodyControl(0);
        platform4.addControl(platfrm4);
        
        rootNode.attachChild(platform4);
        bulletAppState.getPhysicsSpace().add(platfrm4);
        /*## END-OF-PLATFORM-4 ##*/
    }
    
    void initRudal()
    {
        stateManager.attach(bulletAppState);
        
        Texture rudal_tex = assetManager.loadTexture("Textures/texrudal.jpg");
        Material rudal_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        rudalc = new RigidBodyControl(0);
        //rudal.setLocalTranslation(-370, 5, -380);
        //rudal_mat.setTexture("ColorMap", rudal_tex); 
        
        rudal = assetManager.loadModel("Scenes/Rudal/Rudal.mesh.xml");
        rudal_mat.setColor("Color", ColorRGBA.Blue);
        rudal.setMaterial(rudal_mat);
        rudal.addControl(rudalc);                     
        rudalc.setKinematic(true);                              
        bulletAppState.getPhysicsSpace().add(rudalc);
        rootNode.attachChild(rudal);
    }
    void initTiren()
    {
        stateManager.attach(bulletAppState);
                       
        mat_terrain1 = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        mat_terrain1.setTexture("Alpha", assetManager.loadTexture("Textures/alphamaped.jpg"));
        
        
        //Texture Untuk Terrain
        Texture road = assetManager.loadTexture("Textures/soiltex.jpg");
        Texture lava = assetManager.loadTexture("Textures/lavatex2.jpg");
        
        //Texture untuk alpha
        //---LAVA
        lava.setWrap(Texture.WrapMode.Repeat);
        mat_terrain1.setTexture("Tex1", lava);
        mat_terrain1.setFloat("Tex1Scale", 32f);
        
        //---ROAD
        road.setWrap(Texture.WrapMode.Repeat);
        mat_terrain1.setTexture("Tex2", road);
        mat_terrain1.setFloat("Tex2Scale", 32f);
       
        //HeightMap Terrain 2
        AbstractHeightMap heightMap1 = null;
        Texture heightMapImage1 = assetManager.loadTexture("Textures/heightmaped.jpg");
        heightMap1 = new ImageBasedHeightMap(heightMapImage1.getImage());
        heightMap1.load();
        
        //Terrain Setting
        terrain = new TerrainQuad("Terrain", 65, 513, heightMap1.getHeightMap());
        terrain.setLocalTranslation(0, -10, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        terrain.setMaterial(mat_terrain1);
        
        
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(control);
        
        
        rootNode.attachChild(terrain);
        
        
        //Physic
           
        com.jme3.bullet.collision.shapes.CollisionShape a = CollisionShapeFactory.createMeshShape((Node)terrain);
        terrain_control1 = new RigidBodyControl(a, 0);
        terrain.addControl(terrain_control1);

        
 
        
        bulletAppState.getPhysicsSpace().add(terrain_control1);
        //bulletAppState.getPhysicsSpace().add(terrain_control2);
    }
    
    void initKeys()
    {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        
        inputManager.addListener(actionListener, 
                "Left", "Right", "Forward", "Backward", "Jump", "Sprint");
    }
     
    void initGUI2D()
    {
        guiFont = assetManager.loadFont("Interface/Fonts/8BITWONDER.fnt");
        BitmapText notif = new BitmapText(guiFont);
        notif.setText(notiftext);
        notif.setColor(ColorRGBA.White);
        notif.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2, 0);
        
        guiNode.attachChild(notif);
    }

    @Override
    public void simpleInitApp() {
        
        //Warnar Background Viewport
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        //Camera Move Speed
        flyCam.setMoveSpeed(1000f);
        
        //Panggil Fungsi Terrain
        initTiren();
        
        initPlatforms();
        //Panggil Fungsi Animasi
        //initAnim();
        //Panggil Fungsi Hero
        initHero();
        
        initRudal();
        //Panggil Fungsi Mapping Tombol
        initKeys();
  
        flyCam.setEnabled(false);
        ChaseCamera chaseCam = new ChaseCamera(cam, hero, inputManager);
        //chaseCam.setTrailingEnabled(true);
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        //chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(10));
        //chaseCam.setLookAtOffset(Vector3f.UNIT_X.mult(20));
        /*
        AudioNode sound = new AudioNode(assetManager, "Sound/Environment/Nature.ogg");
        sound.setVolume(30f);
        sound.setLooping(true);
        sound.play();*/
        
       
                
    }
    
    ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf)
        {
            if(name.equals("Jump"))
            {
                herocontrol.jump();
                //animchn3.setAnim("JumpLoop");                
            }
            else if(name.equals("Left") || name.equals("Right") || name.equals("Forward") || name.equals("Backward"))
            {
                if(animchn1.getAnimationName().equals("RunTop") == false && isPressed)
                {
                    animchn1.setAnim("RunTop");
                    animchn2.setAnim("RunBase");
                }
                else if(animchn1.getAnimationName().equals("IdleTop") == false && !isPressed)
                {
                    animchn1.setAnim("IdleTop");
                    animchn2.setAnim("IdleBase");
                }
 
                if(name.equals("Left"))
                {
                    
                    left = isPressed;
                    hero.lookAt(new Vector3f(-1, 0, 0), Vector3f.UNIT_X);
                }
                else if(name.equals("Right"))
                {
                    
                    right = isPressed;
                    hero.lookAt(new Vector3f(1, 0, 0), Vector3f.UNIT_X);
                }
                else if(name.equals("Forward"))
                {
                    
                    forward = isPressed;
                    hero.lookAt(new Vector3f(0, 0, 1), Vector3f.UNIT_Z);
                }
                else if(name.equals("Backward"))
                { 
                    
                    backward = isPressed;
                    hero.lookAt(new Vector3f(0, 0, -1), Vector3f.UNIT_Z);
                }
            }
            else if(name.equals("Sprint") && isPressed)
            {
                kecepatan = 1f;
            }
            else if(name.equals("Sprint") && !isPressed)
            {
                kecepatan = 0.4f;
            }
            
        }
    };

    AnimEventListener animEventListener = new AnimEventListener() {

        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
        {
            if(animName.equals("JumpLoop"))
            {
                animchn1.setAnim("IdleTop");
                animchn2.setAnim("IdleBase");
            }
        }

        public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
        {
            
        }
    };
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        initGUI2D();
        walkDirection.zero();
        Vector3f camDir = cam.getDirection().clone().multLocal(kecepatan);
        Vector3f camLeft = cam.getLeft().clone().multLocal(kecepatan);
        camDir.y = 0;
        camLeft.y = 0;        
        float airTime = 0;
        
        if (!herocontrol.onGround()) 
            {
               airTime = airTime + tpf;
            } 
        else 
            {
                airTime = 0;
            }
        
        if(left) walkDirection.addLocal(camLeft); // cam.getLeft()
        if(right) walkDirection.addLocal(camLeft.negate());
        if(forward) walkDirection.addLocal(camDir); // cam.getDirection()
        if(backward) walkDirection.addLocal(camDir.negate());
        herocontrol.setWalkDirection(walkDirection);
        //cam.setLocation(herocontrol.getPhysicsLocation());
        
        //Third Person
        if(walkDirection.length() > 0)
        {
            herocontrol.setViewDirection(walkDirection);
            //notiftext = "";
        }
        
        float x, y, z;
        x = hero.getLocalTranslation().x;
        y = hero.getLocalTranslation().y;
        z = hero.getLocalTranslation().z;
        //rudalc.setPhysicsLocation(new Vector3f(x+1f*tpf, y+1f*tpf, z+1f*tpf));
        //rudal.move(x*tpf, y*tpf, z*tpf);
        //rudal.move(1*tpf, 0, 0);
        //rudal.lookAt(hero.getLocalTranslation(), hero.getLocalTranslation());
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public void collision(PhysicsCollisionEvent event) {
        fpsText.setText(event.getNodeA().getName() + 
                " VS "+ event.getNodeB().getName());
        if(
                (
                event.getNodeA().getName().equals("Sinbad-ogremesh") 
                && 
                event.getNodeB().getName().equals("Terrain")
                )
                ||
                (
                event.getNodeB().getName().equals("Sinbad-ogremesh") 
                && 
                event.getNodeA().getName().equals("Terrain")
                )
           )
        {
            herocontrol.setPhysicsLocation(new Vector3f(-370, 20, -380));
            notiftext = "MATI KARENA LAVA";
        }
        else
        {
            notiftext = "";
            initGUI2D();
        }
    }
}
