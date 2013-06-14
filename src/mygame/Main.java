package mygame;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
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
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.rmi.transport.Target;

// Raditya Danang - 1401077453
// Albert - 141076381

public class Main extends SimpleApplication 
implements PhysicsCollisionListener
{
    
    //Deklarasi
    static Main app;
    AudioNode hurt1, hurt2, sound;
    Button easy, medium, hard, exit;
    Boolean isRunning = false, isEasy = false, isMedium = false, isHard = false, pulang = false;
    Material mat_terrain;
    Material mat_terrain1;
    TerrainQuad terrain;
    TerrainQuad terrain1;
    float kecepatan = 0.4f, kecrudal = 3f;
    BitmapText notif;
    Spatial hero, landscape, rudal, platform1, platform2, platform3, platform4, platform5, platform6, 
            platform7, platform8, platform9, platform10, platform11, platform12, platform13, FinishPlatform;
    CharacterControl herocontrol;
    BulletAppState bulletAppState = new BulletAppState();
    RigidBodyControl terrain_control1, rudalc, terrain_control2, platfrm1, platfrm2, platfrm3, platfrm4,
            platfrm5, platfrm6, platfrm7, platfrm8, platfrm9, platfrm10, platfrm11, platfrm12, platfrm13, FinishPlat;
    String notiftext = "";
    AnimControl animctrl;
    AnimChannel animchn1, animchn2, animchn3;
    Vector3f walkDirection = new Vector3f();
    boolean left = false,
            right = false,
            forward = false,
            backward = false;
    

    public static void main(String[] args) {
        app = new Main();
        app.showSettings = false;
        app.settings = new AppSettings(true);
        app.settings.setResolution(800, 600);
        app.settings.setVSync(false);
        app.settings.setSamples(0);
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
        //herocontrol.setPhysicsLocation(new Vector3f(-370, 20, -380));
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
        //platform1.setMaterial(matplat);
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
        //platform2.setMaterial(matplat);
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
        //platform3.setMaterial(matplat);
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
        //platform4.setMaterial(matplat);
        platform4.scale(5f);
        platform4.setLocalTranslation(-200, 5, -300);
        platform4.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm4 = new RigidBodyControl(0);
        platform4.addControl(platfrm4);
        
        rootNode.attachChild(platform4);
        bulletAppState.getPhysicsSpace().add(platfrm4);
        /*## END-OF-PLATFORM-4 ##*/
        
        /*## PLATFORM-5 ##*/
        //Setting Spatial
        platform5 = assetManager.loadModel("Scenes/Platform3/Platform3.mesh.xml");
        //platform4.setMaterial(matplat);
        platform5.scale(3f);
        platform5.setLocalTranslation(-150, 1, -280);
        platform5.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm5 = new RigidBodyControl(0);
        platform5.addControl(platfrm5);
        
        rootNode.attachChild(platform5);
        bulletAppState.getPhysicsSpace().add(platfrm5);
        /*## END-OF-PLATFORM-5 ##*/
        
         /*## PLATFORM-6 ##*/
        //Setting Spatial
        platform6 = assetManager.loadModel("Scenes/Platform3/Platform3.mesh.xml");
        //platform4.setMaterial(matplat);
        platform6.scale(5f);
        platform6.setLocalTranslation(-120, 7, -240);
        platform6.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm6 = new RigidBodyControl(0);
        platform6.addControl(platfrm6);
        
        rootNode.attachChild(platform6);
        bulletAppState.getPhysicsSpace().add(platfrm6);
        /*## END-OF-PLATFORM-6 ##*/
        
        /*## PLATFORM-7 ##*/
        //Setting Spatial
        platform7 = assetManager.loadModel("Scenes/Platform2/Platform2.mesh.xml");
        //platform3.setMaterial(matplat);
        platform7.scale(5f);
        platform7.setLocalTranslation(-80, 17, -180);
        platform7.setLocalRotation(new Quaternion(0, 7, 0, 3));
        
        //Physic
        platfrm7 = new RigidBodyControl(0);
        platform7.addControl(platfrm7);
        
        rootNode.attachChild(platform7);
        bulletAppState.getPhysicsSpace().add(platfrm7);
        /*## END-OF-PLATFORM-7 ##*/
        
        /*## PLATFORM-8 ##*/
        //Setting Spatial
        platform8 = assetManager.loadModel("Scenes/Platform2/Platform2.mesh.xml");
        //platform3.setMaterial(matplat);
        platform8.scale(5f);
        platform8.setLocalTranslation(-50, 26, -150);
        platform8.setLocalRotation(new Quaternion(0, 7, 0, 3));
        
        //Physic
        platfrm8 = new RigidBodyControl(0);
        platform8.addControl(platfrm8);
        
        rootNode.attachChild(platform8);
        bulletAppState.getPhysicsSpace().add(platfrm8);
        /*## END-OF-PLATFORM-8 ##*/
        
        /*## PLATFORM-9 ##*/
        //Setting Spatial
        platform9 = assetManager.loadModel("Scenes/Platform2/Platform2.mesh.xml");
        //platform3.setMaterial(matplat);
        platform9.scale(5f);
        platform9.setLocalTranslation(-15, 35, -120);
        platform9.setLocalRotation(new Quaternion(0, 7, 0, 3));
        
        //Physic
        platfrm9 = new RigidBodyControl(0);
        platform9.addControl(platfrm9);
        
        rootNode.attachChild(platform9);
        bulletAppState.getPhysicsSpace().add(platfrm9);
        /*## END-OF-PLATFORM-9 ##*/
        
        /*## PLATFORM-10 ##*/
        //Setting Spatial
        platform10 = assetManager.loadModel("Scenes/Platform1/Platform1.mesh.xml");
        //platform1.setMaterial(matplat);
        platform10.scale(8f);
        platform10.setLocalTranslation(25, 5, -50);
        
        //Physic
        platfrm10 = new RigidBodyControl(0);
        platform10.addControl(platfrm10);
        
        rootNode.attachChild(platform10);
        bulletAppState.getPhysicsSpace().add(platfrm10);
        /*## END-OF-PLATFORM-10 ##*/
        
        /*## PLATFORM-11 ##*/
        //Setting Spatial
        platform11 = assetManager.loadModel("Scenes/Platform3/Platform3.mesh.xml");
        //platform4.setMaterial(matplat);
        platform11.scale(5f);
        platform11.setLocalTranslation(60, 10, -10);
        //platform11.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm11 = new RigidBodyControl(0);
        platform11.addControl(platfrm11);
        
        rootNode.attachChild(platform11);
        bulletAppState.getPhysicsSpace().add(platfrm11);
        /*## END-OF-PLATFORM-11 ##*/
        
        /*## PLATFORM-12 ##*/
        //Setting Spatial
        platform12 = assetManager.loadModel("Scenes/Platform4/Platform4.mesh.xml");
        //platform4.setMaterial(matplat);
        platform12.scale(5f);
        platform12.setLocalTranslation(100, 10, 10);
        //platform12.setLocalRotation(new Quaternion(0, 10, 0, 10));
        
        //Physic
        platfrm12 = new RigidBodyControl(0);
        platform12.addControl(platfrm12);
        platfrm12.setKinematic(true);
        
        rootNode.attachChild(platform12);
        bulletAppState.getPhysicsSpace().add(platfrm12);
        /*## END-OF-PLATFORM-12 ##*/
        
        /*## PLATFORM-13 ##*/
        //Setting Spatial
        platform13 = assetManager.loadModel("Scenes/Platform4/Platform4.mesh.xml");
        //platform4.setMaterial(matplat);
        platform13.scale(5f);
        platform13.setLocalTranslation(330, 10, 240);
        //platform13.setLocalRotation(new Quaternion(0, 90, 0, -40));
        
        //Physic
        platfrm13 = new RigidBodyControl(0);
        platform13.addControl(platfrm13);
        platfrm13.setKinematic(true);
        
        rootNode.attachChild(platform13);
        bulletAppState.getPhysicsSpace().add(platfrm13);
        /*## END-OF-PLATFORM-13 ##*/
        
        /*## FINISH-PLATFORM ##*/
        //Setting Spatial
        FinishPlatform = assetManager.loadModel("Scenes/FinishPlatform/FinishPlatform.mesh.xml");
        //platform1.setMaterial(matplat);
        FinishPlatform.scale(8f);
        FinishPlatform.setLocalTranslation(350, 100, 270);
        FinishPlatform.setLocalRotation(new Quaternion(0, 90, 0, -40));
        
        //Physic
        FinishPlat = new RigidBodyControl(0);
        FinishPlatform.addControl(FinishPlat);
        
        rootNode.attachChild(FinishPlatform);
        bulletAppState.getPhysicsSpace().add(FinishPlat);
        /*## END-OF-FINISH-PLATFORM ##*/
        
    }
    
    void initRudal()
    {
        stateManager.attach(bulletAppState);
        
        Texture rudal_tex = assetManager.loadTexture("Textures/texrudal.jpg");
        //Material rudal_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        rudalc = new RigidBodyControl(0);
        //rudal.setLocalTranslation(-370, 5, -380);
        //rudal_mat.setTexture("ColorMap", rudal_tex); 
        
        rudal = assetManager.loadModel("Scenes/Rudal/Rudal.mesh.xml");
        //rudal_mat.setColor("Color", ColorRGBA.Blue);
        //rudal.setMaterial(rudal_mat);
        rudal.addControl(rudalc);                     
        rudalc.setKinematic(true);  
        
        rudal.setLocalScale(4f);
        rudal.setLocalTranslation(-390, 5, -380);
        //rudalc.setPhysicsLocation(new Vector3f(-390, 5, -380));
        
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
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(actionListener, 
                "Left", "Right", "Forward", "Backward", "Jump", "Sprint", "Click");
    }
     
    void initGUI2D()
    {
        guiFont = assetManager.loadFont("Interface/Fonts/8BITWONDER.fnt");
        notif = new BitmapText(guiFont);
        notif.setText(notiftext);
        notif.setColor(ColorRGBA.White);
        notif.setLocalTranslation(settings.getWidth()/3.5f, settings.getHeight()/2, 0);
        guiNode.attachChild(notif);
    }
    
    void initMenu()
    {
        /*
        Picture background = new Picture("Background");
        background.setImage(assetManager, "Menu/bg.png", true);
        background.setWidth(settings.getWidth());
        background.setHeight(settings.getHeight());
        background.setPosition(0, 0);
        */
        
        flyCam.setEnabled(false);
        //guiNode.attachChild(background);
        inputManager.setCursorVisible(true);
        
        
        initButton();
    }
    
    void initButton()
    {
        easy = new Button(assetManager, "easy", "easybtn.png");
        easy.setHoverImage("easybtnh.png");
        easy.setWidth(150);
        easy.setHeight(80);
        easy.setPosition(20, 5);
        
        medium = new Button(assetManager, "medium", "mediumbtn.png");
        medium.setHoverImage("mediumbtnh.png");
        medium.setWidth(150);
        medium.setHeight(80);
        medium.setPosition(240, 5);
        
        hard = new Button(assetManager, "hard", "hardbtn.png");
        hard.setHoverImage("hardbtnh.png");
        hard.setWidth(150);
        hard.setHeight(80);
        hard.setPosition(450, 5);
        
        exit = new Button(assetManager, "exit", "exitbtn.png");
        exit.setHoverImage("exitbtnh.png");
        exit.setWidth(150);
        exit.setHeight(72);
        exit.setPosition(640, 8);
        
        guiNode.attachChild(easy);
        guiNode.attachChild(medium);
        guiNode.attachChild(hard);
        guiNode.attachChild(exit);
    }
    
       
    
     
    @Override
    public void simpleInitApp() {
        
        
        //Warna Background Viewport
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
        initGUI2D();
        flyCam.setEnabled(false); 
        ChaseCamera chaseCam = new ChaseCamera(cam, hero, inputManager);
        //chaseCam.setTrailingEnabled(true);
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        //chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(10));
        //chaseCam.setLookAtOffset(Vector3f.UNIT_X.mult(20));
        
        sound = new AudioNode(assetManager, "Sounds/lavasound.ogg");
        sound.setVolume(30f);
        sound.setLooping(true);
        sound.play(); 
        
        hurt1 = new AudioNode(assetManager, "Sounds/hurt1.wav");
        hurt1.setVolume(0.15f);
        hurt1.setLooping(false);
        
        hurt2 = new AudioNode(assetManager, "Sounds/hurt2.wav");
        hurt2.setVolume(0.5f);
        hurt2.setLooping(false);
        
        
        //MENU GAGAL
        //initMenu();
        
    }
    
    ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf)
        {
            //if(name.equals("Click") && !isPressed && !isRunning)
            //{
                    
                if(name.equals("Jump"))
                {
                    herocontrol.jump();
                    //animchn3.setAnim("JumpStart");                
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
                
                /* GAGAL MASUKIN MENU
                if(easy.isFocused)
                    {
                        guiNode.detachAllChildren();
                        flyCam.setEnabled(true);
                        inputManager.setCursorVisible(false);
                        kecrudal = 3f;


                        isRunning = true;

                                    //Warna Background Viewport
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
                        initGUI2D();
                        flyCam.setEnabled(false); 
                        ChaseCamera chaseCam = new ChaseCamera(cam, hero, inputManager);
                        //chaseCam.setTrailingEnabled(true);

                        //bulletAppState.getPhysicsSpace().addCollisionListener(this);
                        //chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(10));
                        //chaseCam.setLookAtOffset(Vector3f.UNIT_X.mult(20));

                        sound = new AudioNode(assetManager, "Sounds/lavasound.ogg");
                        sound.setVolume(30f);
                        sound.setLooping(true);
                        sound.play(); 

                        hurt1 = new AudioNode(assetManager, "Sounds/hurt1.wav");
                        hurt1.setVolume(0.15f);
                        hurt1.setLooping(false);

                        hurt2 = new AudioNode(assetManager, "Sounds/hurt2.wav");
                        hurt2.setVolume(0.5f);
                        hurt2.setLooping(false);

                    }
                    else if(medium.isFocused)
                    {
                        guiNode.detachAllChildren();
                        flyCam.setEnabled(true);
                        inputManager.setCursorVisible(false);
                        kecrudal = 7f;

                        //initApp();
                        isRunning = true;
                    }
                    else if(hard.isFocused)
                    {
                        guiNode.detachAllChildren();
                        flyCam.setEnabled(true);
                        inputManager.setCursorVisible(false);
                        kecrudal = 15f;

                        //initApp();
                        isRunning = true;
                    }

                    if(exit.isFocused)
                    {
                        app.stop();
                    }
            */
            
                
        }
    };

    AnimEventListener animEventListener = new AnimEventListener() {

        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
        {
            /*
            if(animName.equals("JumpStart"))
            {
                animchn1.setAnim("IdleTop");
                animchn2.setAnim("IdleBase");
            }*/
            
        }
        public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
        {
            
        }
    };
    
    @Override
    public void simpleUpdate(float tpf) {
        
        //if(isRunning)
        //{
            //TODO: add update code 
            /*for(String nama : hero.getControl(AnimControl.class).getAnimationNames())
                System.out.println(nama);*/
            hero.getControl(AnimControl.class).getAnimationNames();
            walkDirection.zero();
            Vector3f camDir = cam.getDirection().clone().multLocal(kecepatan);
            Vector3f camLeft = cam.getLeft().clone().multLocal(kecepatan);
            camDir.y = 0;
            camLeft.y = 0;        
            float airTime = 0;

            if (!herocontrol.onGround()) 
                {
                   airTime = airTime + tpf;
                   if(airTime > 0.3f)
                    animchn3.setAnim("JumpLoop", 1f);
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
            }

            float x, y, z, x1, y1, z1;
            x = herocontrol.getPhysicsLocation().x;
            y = herocontrol.getPhysicsLocation().y;
            z = herocontrol.getPhysicsLocation().z; 

            x1 = rudalc.getPhysicsLocation().x;
            y1 = rudalc.getPhysicsLocation().y;
            z1 = rudalc.getPhysicsLocation().z;
            float moveX=0, moveY=0, moveZ=0;

            //Kondisi Rudal Supaya Ngejar Hero (thanks kk aslab udah dibantuin :D )
            if (x1 > x)
                moveX=-1;
            else if (x1<x)
                moveX=+1;
            if (y1 > y)
                moveY=-1;
            else if (y1<y)
                moveY=+1;
            if (z1 > z)
                moveZ=-1;
            else if (z1<z)
                moveZ=+1;


            //rudalc.setPhysicsLocation(new Vector3f(x*tpf*0.005f, y*tpf*0.005f, z*tpf*0.005f));
            rudal.move(moveX*tpf*kecrudal, moveY*tpf*kecrudal, moveZ*tpf*kecrudal);
            rudal.lookAt(hero.getLocalTranslation(), hero.getLocalTranslation());
            //rudal.setLocalTranslation(x1, y1, z1);
            //System.out.println("x: " + rudal.getLocalTranslation().x);

            float px1, py1, pz1, px2, py2, pz2, px3, py3, pz3;
            float movePX1 = 0, movePY1 = 0, movePZ1 = 0;
            px1 = platfrm12.getPhysicsLocation().x;
            py1 = platfrm12.getPhysicsLocation().y;
            pz1 = platfrm12.getPhysicsLocation().z;

            px2 = platfrm13.getPhysicsLocation().x;
            py2 = platfrm13.getPhysicsLocation().y;
            pz2 = platfrm13.getPhysicsLocation().z;

            px3 = platfrm11.getPhysicsLocation().x;
            py3 = platfrm11.getPhysicsLocation().y;
            pz3 = platfrm11.getPhysicsLocation().z;

            if(pulang == false)
            {
                if(px1 < px2 - 20)
                {
                        movePX1=+1;
                }

                if(pz1 < pz2 - 20)
                {
                        movePZ1=+1;
                }
                
                if(px1 >= px2 - 20 || pz1 >= pz2 - 20)
                {
                    pulang = true;
                }
            }
            
            if(pulang == true)
            {
                if(px1 > px3 + 10)
                {     
                        movePX1=-1;   
                }


                if(pz1 > pz3 + 10)
                {     
                        movePZ1=-1;   
                }
                
                if(px1 <= px3 + 10 || pz1 <= pz3 + 10)
                {
                    pulang = false;
                }
            }


            platform12.move(movePX1*tpf*20, 0, movePZ1*tpf*20);
            System.out.println("x :"+px1);
            System.out.println("xpt13 :"+px2);
        }
    /*
        else
        {
            easy.onMouseEnter(inputManager);
            easy.onMouseLeave();
            
            medium.onMouseEnter(inputManager);
            medium.onMouseLeave();
            
            hard.onMouseEnter(inputManager);
            hard.onMouseLeave();
            
            exit.onMouseEnter(inputManager);
            exit.onMouseLeave();
        }*/

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
            //herocontrol.setPhysicsLocation(new Vector3f(-370, 20, -380));
            rudal.setLocalTranslation(-390, 5, -380);
            guiNode.detachChild(notif);
            hurt2.play();
            notiftext = "MATI KARENA LAVA";
            initGUI2D();
        }
        else if(
                    (
                    event.getNodeA().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeB().getName().equals("Rudal-ogremesh")
                    )
                    ||
                    (
                    event.getNodeB().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeA().getName().equals("Rudal-ogremesh")
                    )
                )
        {
            
            herocontrol.setPhysicsLocation(new Vector3f(-370, 20, -380));
            rudal.setLocalTranslation(-390, 5, -380);
            hurt1.play();
            notiftext = "MATI KARENA RUDAL";
            guiNode.detachChild(notif);
            initGUI2D();
        }
        
        else if(
                    (
                    event.getNodeA().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeB().getName().equals("Platform1-ogremesh")
                    )
                    ||
                    (
                    event.getNodeB().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeA().getName().equals("Platform1-ogremesh")
                    )
                )
        {
            
            notiftext = "";
            guiNode.detachChild(notif);
        }
        else if(
                    (
                    event.getNodeA().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeB().getName().equals("Platform2-ogremesh")
                    )
                    ||
                    (
                    event.getNodeB().getName().equals("Sinbad-ogremesh") 
                    && 
                    event.getNodeA().getName().equals("Platform2-ogremesh")
                    )
                )
        {
            //guiNode.detachChild(notif);
            //notiftext = "HINT PRESS SHIFT TO SPRINT";
            //initGUI2D();
        }
        else
        {
            guiNode.detachChild(notif);
        }
        
    }

    
}
