package Game3D;
import java.util.ArrayList;
import GameApplication.Constants;
import GameApplication.Constants.GAME_TYPE;
import GameApplication.ModelManager;
import GameApplication.ViewManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * This class is the main Game 3D sub scene, used to show and manage
 * 3D World objects,animations etc . 
 * @author Stanislav Bodik
 */

public class Game3DSubSceneHolder {

	private SubScene subScene;
	private Group subSceneGroup;
	private Cannon cannon; 
	private double subSceneWidth,SubSceneHeigh;
	private MeshView surfaceMeshView;
	private Sphere mouseMovePoint; // small sphere where the mouse pointing at 3D World
	private Rotate cannonGunRotateXZ,cannonGunRotateY,cannonBaseConnectorRotateXZ;
	private ArrayList<Asteroid> paintedAsteroids = new ArrayList<>();
	private PerspectiveCamera camera;
	private Skybox skyBox;
	private Timeline cannonRotateAnimation;
	private Rotate cannonRotation;
	private boolean isGameRest; // used to stop asteroids generation thread when game rests
	private Thread asteroidsGenerationThread;
	private Translate cameraTranslate;
	private ModelManager modelManagerInstance;
	private boolean isAnimatingCannon; // used to stop mouse events while animating cannon
	
   	public Game3DSubSceneHolder(ModelManager modelManagerInstance,double subSceneWidth,double SubSceneHeigh){
		this.subSceneWidth=subSceneWidth;
		this.SubSceneHeigh=SubSceneHeigh;
		this.modelManagerInstance = modelManagerInstance;
		
		createGame3DScene();
	}
   	
   	public void registerEvents(EventHandler<MouseEvent> mouseClick3DFireCannon,EventHandler<MouseEvent> mouseMove3DScene){
   		subScene.setOnMouseMoved(mouseMove3DScene);
   		subScene.setOnMousePressed(mouseClick3DFireCannon);
   	}
   	
   	public  void setCannon(Cannon cannon){
   		this.cannon=cannon;
   		subSceneGroup.getChildren().addAll(cannon.getGunMeshGroups());
   	}
	
 	public  void startAsteroidsGnerationThread(GAME_TYPE gameType){
		
		isGameRest=false;
		asteroidsGenerationThread = new Thread(new Runnable() {
			public void run() {
				
				// asteroids generated size and speed depends on game difficulty
				Range asteroidsSpeed = null;
				Range asteroidsSize = null;
				Range generationTime = null; // interval time between generation new asteroid
				int asteroidGenerationStartRaduis;
				
				switch (gameType) {
				case EASY:
					generationTime= new Range(500, 1000);
					asteroidsSize = new Range(1f, 2); 
					asteroidsSpeed = new Range(35000, 40000);
					asteroidGenerationStartRaduis=35;
					break;
				case MEDIUM:
					generationTime= new Range(1000, 2000);
					asteroidsSize = new Range(0.6f, 1.5f); 
					asteroidsSpeed = new Range(20000, 30000);
					asteroidGenerationStartRaduis=56;
					break;
				case HARD:
					generationTime= new Range(1500, 4000);
					asteroidsSize = new Range(0.2f, 0.8f); 
					asteroidsSpeed = new Range(10000, 10000);
					asteroidGenerationStartRaduis=70;
					break;
				
				default:
					generationTime= new Range(500, 4000);
					asteroidsSize = new Range(0.2f, 2); 
					asteroidsSpeed = new Range(1000, 20000);
					asteroidGenerationStartRaduis=70;
					break;
				}
				
				// start generation
				while(true){
					//generate random asteroid
			    	AsteroidRandomGenerator asRandom= new AsteroidRandomGenerator(modelManagerInstance,asteroidGenerationStartRaduis,asteroidsSize,asteroidsSpeed);

			    	Asteroid asteroid = asRandom.getNextRandomAstroid();

			        Platform.runLater(new Runnable() {
			            @Override public void run() {
			            	 //add it to view
			            	drawMeshGroup(asteroid.getAsteroidMeshViewGroup());
			            	addAsteroid(asteroid);
			            }
			        });
			        //asteroid animations movements and rotations
			        Transition asteroidTranslationAnimation;
			        Timeline asteroidRotateAnimation = new Timeline();
			        // asteroid translation animation
					 asteroidTranslationAnimation = new Transition() {
						float translationTime=asteroid.getSpeed();

								    			
						float translationDistance=asteroid.getStartPosition().distance(asteroid.getTargetPosition());
						float dx=asteroid.getTargetPosition().x-asteroid.getStartPosition().x;
						float dy=asteroid.getTargetPosition().y-asteroid.getStartPosition().y;
						float dz=asteroid.getTargetPosition().z-asteroid.getStartPosition().z;
						
						float dxDirection = dx/translationDistance;
						float dyDirection = dy/translationDistance;
						float dzDirection = dz/translationDistance;

			   			{
			   				setCycleDuration(new Duration(translationTime));
			   				this.setInterpolator(Interpolator.LINEAR);
			   				this.setOnFinished(e -> {	   					
			   					removeMeshGroup(asteroid.getAsteroidMeshViewGroup());
			   					removeAsteroid(asteroid);
			   			        asteroidRotateAnimation.stop();
			   				});
			   			}
						@Override
						protected void interpolate(double frac) {		
							MyPoint3D newAsteroidPosition = new MyPoint3D(asteroid.getStartPosition().x+(float)(translationDistance*frac*dxDirection),asteroid.getStartPosition().y+(float)(translationDistance*frac*dyDirection),asteroid.getStartPosition().z+(float)(translationDistance*frac*dzDirection));
							asteroid.setPosition(newAsteroidPosition);
							asteroid.setDistanceFromTarged(asteroid.getCurrentPosition().distance(asteroid.getTargetPosition()));
						}
					};
					
			        asteroidTranslationAnimation.playFromStart();
			        
			   	    // asteroid rotation animation
			        Rotate asteroidRotation = new Rotate();
			        asteroidRotation.setAxis(new Point3D(Utils3D.generateRandomFloat(0, 1),Utils3D.generateRandomFloat(0, 1),Utils3D.generateRandomFloat(0, 1)));

			        final KeyValue kv0 = new KeyValue(asteroidRotation.angleProperty(), 0, Interpolator.LINEAR);
			        final KeyValue kv1 = new KeyValue(asteroidRotation.angleProperty(), 360, Interpolator.LINEAR);
			        final KeyFrame kf0 = new KeyFrame(Duration.millis(0), kv0);
			        final KeyFrame kf1 = new KeyFrame(Duration.millis(4000*Utils3D.generateRandomFloat(0.2f, 1)), kv1); // min speed, max duration

			        asteroidRotateAnimation.setCycleCount(Timeline.INDEFINITE);        
			        asteroidRotateAnimation.getKeyFrames().setAll(kf0, kf1);
			        asteroid.setRotation(asteroidRotation);
			        asteroidRotateAnimation.playFromStart();
			        
					//random delay between generations
			        try {
						Thread.sleep((long) Utils3D.generateRandomFloat(generationTime.getMin(), generationTime.getMax()));
					} catch (InterruptedException e) {
						if(isGameRest){
							break;
						}
					}
				}
				
			}
		}); asteroidsGenerationThread.start();
		
	}
	
	public void drawMeshGroup(Group p){
		subSceneGroup.getChildren().add(p);
	}
	
	private void createGame3DScene() {
		 
		// create world surface 
	 	Mesh surfaceMesh = new Mesh(Constants.WORLD_SIZE,Constants.WORLD_SIZE);
	 	surfaceMeshView = new MeshView(surfaceMesh);
	 	surfaceMeshView.setOpacity(0);
	 	surfaceMeshView.setCullFace(CullFace.NONE);
	 	surfaceMeshView.getTransforms().add(new Rotate(-90, 1, 0, 0, Rotate.X_AXIS));

 	 	//Moon Sphere
 	 	int sphereRaduis=15;
 	 	Sphere moonSphere = new Sphere(sphereRaduis);
 	 	PhongMaterial phongMaterial = new PhongMaterial();
 	 	phongMaterial.setSpecularColor(Color.WHITE);
 	    phongMaterial.setDiffuseMap(new Image(Game3DSubSceneHolder.class.getResource("resources/"+Constants.MOON_TEXTURE).toExternalForm(),4000,2000,true,true));
 	    phongMaterial.setBumpMap(new Image(Game3DSubSceneHolder.class.getResource("resources/"+Constants.MOON_TEXTURE1).toExternalForm(),4000,2000,true,true));
 	    phongMaterial.setSpecularMap(new Image(Game3DSubSceneHolder.class.getResource("resources/"+Constants.MOON_TEXTURE1).toExternalForm(),4000,2000,true,true));
 	    phongMaterial.setDiffuseColor(Color.GRAY);
 	    moonSphere.setRotate(90);
 	    moonSphere.setMaterial(phongMaterial);
 	 	moonSphere.setTranslateY(sphereRaduis);
 	 	
 	 
        // Create and position camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setFieldOfView(90);
        camera.setVerticalFieldOfView(false);
        cameraTranslate = new Translate(Constants.cameraEyePositionNormal.x,Constants.cameraEyePositionNormal.y,Constants.cameraEyePositionNormal.z);
        camera.getTransforms().addAll (cameraTranslate);
        
        // Skynox
        skyBox = new Skybox(Constants.top,Constants.bottom,Constants.left,Constants.right,Constants.front,Constants.back,Constants.WORLD_SIZE);
        
        // SubScene's lights
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateZ(-3);
        pointLight.setTranslateY(-3);
        pointLight.setTranslateX(3);

        AmbientLight ambLight = new AmbientLight(Color.color(0.75,0.7,0.9));
        
        mouseMovePoint = new Sphere(0.05);
        mouseMovePoint.setTranslateX(0);
        mouseMovePoint.setTranslateZ(0);
        mouseMovePoint.setTranslateY(0);
        mouseMovePoint.setMaterial(new PhongMaterial(Color.RED));
                
        // Build the sub scene Graph
		subSceneGroup = new Group(moonSphere,skyBox,surfaceMeshView,pointLight,ambLight);	       
		subSceneGroup.getChildren().add(mouseMovePoint);
		subSceneGroup.setAutoSizeChildren(false);
		
        // create a SubScene       
        subScene = new SubScene(subSceneGroup,subSceneWidth,SubSceneHeigh,true,SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        
	}
	
	public Skybox getSkyBox() {
		return skyBox;
	}
	
	public boolean isAnimatingCannon() {
		return isAnimatingCannon;
	}
	
	public void setAnimatingCannon(boolean isAnimatingCannon) {
		this.isAnimatingCannon = isAnimatingCannon;
	}
	
	public void stopAnimationThreads(){
		isGameRest=true;
		asteroidsGenerationThread.interrupt();
	}
	// Called when ever game rests clear the scene from asteroids
	public void restAsteroidsGeneration(){
		//clears asteroids generation thread
		isGameRest=true;
		asteroidsGenerationThread.interrupt();
		
		for(int i=0; i<paintedAsteroids.size(); i++){
			subSceneGroup.getChildren().remove(paintedAsteroids.get(i).getAsteroidMeshViewGroup());
		}
		paintedAsteroids.clear();
		
	}
	
 	public double getSubSceneHeigh() {
		return SubSceneHeigh;
	}
	
	public double getSubSceneWidth() {
		return subSceneWidth;
	}
	
	public PerspectiveCamera getCamera() {
		return camera;
	}
	
	// checks intersection of mouse click in 2D with asteroid in 3D world.
	public MyPoint3D checkIntersectionMouseWithAsteroid(RayFromCameraTo3DWorld ray){
		for(int i=0; i<paintedAsteroids.size(); i++){
			MyPoint3D intersectionPointWithAsteroid = paintedAsteroids.get(i).checkIntersectionWithMouse(ray);
			if(intersectionPointWithAsteroid!=null){
				return intersectionPointWithAsteroid;
			}
		}

		return null;
	}
	
	// checks intersection of flying bullet with asteroid in 3D world.
	public ArrayList<Asteroid> checkIntersectionBulletWithAsteroid(Bullet bullet){
		
		ArrayList<Asteroid> intersectedAsteroids = new ArrayList<Asteroid>();
		
		for(int i=0; i<paintedAsteroids.size(); i++){
		
			boolean isIntersected = false;
			
			try{
			 isIntersected = paintedAsteroids.get(i).checkIntersectionWithBullet(bullet);
			}catch(NullPointerException e){
				System.err.println("cheking intersection with deleted asteroid");
			}
			
			if(isIntersected){
				intersectedAsteroids.add(paintedAsteroids.get(i));
			}
		}
		
		if(intersectedAsteroids.size()>0){
			for(int i=0; i<intersectedAsteroids.size(); i++){
				subSceneGroup.getChildren().remove(intersectedAsteroids.get(i).getAsteroidMeshViewGroup());
			}
			paintedAsteroids.removeAll(intersectedAsteroids);
		}

		return intersectedAsteroids;
	}
	
	public void initCannonRotations(){
				
		cannon.getCannontGun().getTransforms().clear();				

		cannonGunRotateXZ = new Rotate();
		cannonGunRotateY = new Rotate();
		cannonBaseConnectorRotateXZ = new Rotate();
		
		cannon.getCannontGun().getTransforms().add(cannonGunRotateXZ);
   		cannon.getCannontGun().getTransforms().add(cannonGunRotateY);
   		cannon.getCannonBaseConnector().getTransforms().add(cannonBaseConnectorRotateXZ);
   		
   		cannonBaseConnectorRotateXZ.setAxis(new javafx.geometry.Point3D(0,1,0));
		cannonGunRotateY.setAxis(new javafx.geometry.Point3D(1,0,0));
		cannonGunRotateXZ.setAxis(new javafx.geometry.Point3D(0,1,0));

   		cannonGunRotateY.setPivotX(0);
   		cannonGunRotateY.setPivotY(cannon.getCannonConnectorHeight());
   		cannonGunRotateY.setPivotZ(0);
   		cannonGunRotateXZ.setAngle(cannon.getStartRotateAngel()-90);
	}
	
	public void removeMeshGroup(Group group){
		subSceneGroup.getChildren().remove(group);
	}
	
	public SubScene getSubScene() {
		return subScene;
	}
	
	public void translateMouseMovePoint(MyPoint3D p ){
		mouseMovePoint.setTranslateX(p.x);
    	mouseMovePoint.setTranslateY(p.y);
    	mouseMovePoint.setTranslateZ(p.z);
	}
	
	public void rotateCannonXZ(double cannonRotationAngelXZ){	
		cannonBaseConnectorRotateXZ.setAngle(cannonRotationAngelXZ);
		cannonGunRotateXZ.setAngle(cannonRotationAngelXZ);
	}
	
	public void rotateCannonY(double cannonGunRotationAngelY){
			cannonGunRotateY.setAngle(cannonGunRotationAngelY);
	}
	
 	public void removeAsteroid(Asteroid asteroid){
		paintedAsteroids.remove(asteroid);
	}
	
	public void addAsteroid(Asteroid asteroid){
		paintedAsteroids.add(asteroid);
	}
 
	public Cannon getCannon() {
		return cannon;
	}
	
	public void stopCanonRotateAnimation(){
		cannonRotateAnimation.stop();
		cannonRotateAnimation.getKeyFrames().clear();
		
		final KeyValue kv0 = new KeyValue(cannonRotation.angleProperty(), cannonRotation.getAngle(), Interpolator.LINEAR);
        final KeyValue kv1 = new KeyValue(cannonRotation.angleProperty(), cannon.getStartRotateAngel()-90, Interpolator.LINEAR);
        final KeyFrame kf0 = new KeyFrame(Duration.millis(0), kv0);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(500), kv1); // min speed, max duration

        cannonRotateAnimation.setCycleCount(1);        
        cannonRotateAnimation.getKeyFrames().setAll(kf0, kf1);
        cannon.getCannontGun().getTransforms().setAll(cannonRotation);
        cannonRotateAnimation.playFromStart();
        
		cannonRotateAnimation.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				isAnimatingCannon=false;
				initCannonRotations();				
			}
		});
	}
	
	public void startRotateCannonAnimation(){
		
        cannonRotateAnimation = new Timeline();
		cannonRotation = new Rotate();
		cannonRotation.setAxis(new Point3D(0, 1, 0));
        final KeyValue kv0 = new KeyValue(cannonRotation.angleProperty(), 0, Interpolator.LINEAR);
        final KeyValue kv1 = new KeyValue(cannonRotation.angleProperty(), 360, Interpolator.LINEAR);
        final KeyFrame kf0 = new KeyFrame(Duration.millis(0), kv0);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(8000), kv1); // min speed, max duration

        cannonRotation.setPivotX(0);
        cannonRotation.setPivotY(cannon.getCannonConnectorHeight());
        cannonRotation.setPivotZ(0);

        cannonRotateAnimation.setCycleCount(Timeline.INDEFINITE);        
        cannonRotateAnimation.getKeyFrames().setAll(kf0, kf1);
        cannon.getCannontGun().getTransforms().setAll(cannonRotation);
        cannonRotateAnimation.playFromStart();
        
        
	}

	public void animateCamera(int direction){
		
		 // asteroid translation animation
		Transition cameraTranslationAnimation = new Transition() {
					 			
			float translationDistance=Constants.cameraEyePositionNormal.distance(Constants.cameraEyePositionPlay);
			float dz = Constants.cameraEyePositionPlay.z-Constants.cameraEyePositionNormal.z;
			float dzDirection = dz/translationDistance;

  			{
  				dzDirection*=direction;
  				setCycleDuration(new Duration(1500));
  				this.setInterpolator(Interpolator.LINEAR);
  				this.setOnFinished(e -> {	   					
  					
  				});
  			}
			@Override
			protected void interpolate(double frac) {
				if(direction>0){
					cameraTranslate.setZ(Constants.cameraEyePositionNormal.z+(float)(translationDistance*frac*dzDirection));
				}else{
					cameraTranslate.setZ(Constants.cameraEyePositionPlay.z+(float)(translationDistance*frac*dzDirection));
				}
			}
		};
		cameraTranslationAnimation.playFromStart();
	}
}
