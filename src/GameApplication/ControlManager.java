package GameApplication;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.validator.routines.EmailValidator;

import Application2D_UI.LoginPanel;
import Application2D_UI.MyImageButton;
import Application2D_UI.RegisterPanel;
import Entities.FireEvent;
import Entities.Game;
import Entities.Player;
import Game3D.Asteroid;
import Game3D.Bullet;
import Game3D.Cannon;
import Game3D.MyPoint3D;
import Game3D.RayFromCameraTo3DWorld;
import GameApplication.Constants.GAME_TYPE;
import GameApplication.Constants.OBJECT_NAMES;
import GameApplication.Constants.PANEL_TYPE;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * This class used to control application model and view.
 * Holds events from view check/work/calculate and update back view and model.
 * @author Stanislav Bodik
 */

public class ControlManager {

	private  RayFromCameraTo3DWorld cameraRayToWorld;
	private  Cannon cannon;
	// current views panel in application
	private  PANEL_TYPE currentViewingPanel = PANEL_TYPE.LOGIN;
	private  Timer gameCountDownTimer;
	private   ModelManager   modelManager;
	private   ViewManager    viewManager;
	
	public ControlManager(ModelManager modelManager,ViewManager viewManager){
		this.modelManager=modelManager;
		this.viewManager=viewManager;
	}
	
	// Initiate application controls (mouse events,objects,animations,connections to server )
   	public void init(){
		cameraRayToWorld = new RayFromCameraTo3DWorld(viewManager.getGame3dSubScene().getCamera(), viewManager.getGame3dSubScene().getSubSceneWidth(), viewManager.getGame3dSubScene().getSubSceneHeigh());
		cannon = loadCannonToView();
		viewManager.setCannon(cannon);
		viewManager.startAsteroidsGnerationThread(GAME_TYPE.TRAINING);
		viewManager.getGame3dSubScene().startRotateCannonAnimation();
		viewManager.registerEvents(buttonsClickListener, scene3DMouseClickHandler, scene3DMouseMoveHandler);
		modelManager.connectToServer();
		setCloseOperation(viewManager.getStage());
		
 	}
	
   	// control mouse move events from view on 3D scene
 	public  final EventHandler<MouseEvent> scene3DMouseMoveHandler = new EventHandler<MouseEvent>(){
 		
		public void handle(MouseEvent e) {
			
			
			// in case there is animations prevent mouse events
			if(viewManager.isAnimatingCannon()){
				return;
			}
			
			//update ray pointing from camera to 3d world depends on mouse in 2d
			cameraRayToWorld.updateRay(e.getSceneX(), e.getSceneY());
						
			//check intersection with sky box
			MyPoint3D rayIntersectionPoint = viewManager.getGame3dSubScene().getSkyBox().checkIntersectionWithSkyBox(cameraRayToWorld);

			if(rayIntersectionPoint==null)return;
									
        	// calculate angel between intersection point and cannon
	   		double canonGunAngelYRad = Math.atan2(rayIntersectionPoint.y-cannon.getCannonHeigh(),rayIntersectionPoint.z);
	   		double canonGunAngelYDegree = canonGunAngelYRad*180/Math.PI;
	   		
        	double mouseClickAngelXZ = Math.atan2(rayIntersectionPoint.z,rayIntersectionPoint.x)*180/Math.PI;
        	double cannonRotationXZ=cannon.getStartRotateAngel()-mouseClickAngelXZ;
        	
        	// update canon view rotations 
    	   	viewManager.getGame3dSubScene().rotateCannonXZ(cannonRotationXZ);

        	if(canonGunAngelYDegree<20){
        		viewManager.getGame3dSubScene().rotateCannonY(canonGunAngelYDegree);
        	}
        	
        	viewManager.getGame3dSubScene().translateMouseMovePoint(rayIntersectionPoint);

		
        	//update cannon model rotations 
        	//calculate angel between gun preparation and cannon connector points
        	double angelPrepConnectorRad = Math.atan2(-(cannon.getGunPreparationStartPosition().y-cannon.getCannonConnectorHeight()),-cannon.getGunPreparationStartPosition().z);
    		double cannonGunAngelYRad = (canonGunAngelYDegree*Math.PI/180);
    		
        	float cannonGunPreparationNewPosX =(float)(1*Math.cos(cannonGunAngelYRad-angelPrepConnectorRad)*Math.cos(mouseClickAngelXZ*Math.PI/180));
        	float cannonGunPreparationNewPosY =(float)(cannon.getCannonConnectorHeight()+1*Math.sin(cannonGunAngelYRad-angelPrepConnectorRad));
        	float cannonGunPreparationNewPosZ = (float)(1*Math.cos(-cannonGunAngelYRad-angelPrepConnectorRad)*Math.sin(mouseClickAngelXZ*Math.PI/180));
        	
        	cannon.setGunPreparationPosition(new MyPoint3D(cannonGunPreparationNewPosX,cannonGunPreparationNewPosY,cannonGunPreparationNewPosZ));
        	cannon.setGunAngelXZ((float)cannonRotationXZ);
        	cannon.setGunAngelY((float)canonGunAngelYDegree);
		}
	};
	
   	// control mouse click events from view on 3D scene
	public  final EventHandler<MouseEvent> scene3DMouseClickHandler = new EventHandler<MouseEvent>()

	{

		public void handle(MouseEvent e) {

			// in case there is animations prevent mouse events
			if(viewManager.isAnimatingCannon()){
				return;
			}

			Cannon cannon = viewManager.getGame3dSubScene().getCannon();
			Bullet bullet = new Bullet(modelManager.getObjectMeshViewGroup(OBJECT_NAMES.BULLET),cannon.getGunPreparationPosition(), 0.1f, cannon.getGunAngelXZ(), cannon.getGunAngelY());
			viewManager.getGame3dSubScene().drawMeshGroup(bullet.getBulletMeshGroup());
			Game currentGame = modelManager.getCurrentGame();

			// start bullet animation
			Transition t = new Transition() {
				int translationTime=2000;
				{
					setCycleDuration(new Duration(translationTime));
					this.setInterpolator(Interpolator.LINEAR);
					this.setOnFinished(e -> {
						viewManager.getGame3dSubScene().removeMeshGroup(bullet.getBulletMeshGroup());
						if(!bullet.isHitAsteroid()){
							modelManager.addEvent(new FireEvent(modelManager.getCurrentPlayingPlayer(), false, new Date(),currentGame));
						}
					});
				}
				
				@Override
				protected void interpolate(double frac) {
					
					float translationDistance=Constants.WORLD_SIZE;
					MyPoint3D bulletFlyDirection = new MyPoint3D(Math.cos((bullet.getStartBulletRotateAngel()-bullet.getRotationXZAngelDeg())*Math.PI/180),Math.sin(bullet.getRotationYAngelDeg()*Math.PI/180),Math.sin((bullet.getStartBulletRotateAngel()-bullet.getRotationXZAngelDeg())*Math.PI/180));
					MyPoint3D newBulletPosition = new MyPoint3D((float)(bullet.getStartPosition().x+translationDistance*frac*bulletFlyDirection.x),(float)(bullet.getStartPosition().y+translationDistance*frac*bulletFlyDirection.y) ,(float)(bullet.getStartPosition().z+translationDistance*frac*bulletFlyDirection.z));
					bullet.setPosition(newBulletPosition);
					bullet.setFlyDirection(bulletFlyDirection);
					ArrayList<Asteroid> intersectedAsteroids = viewManager.getGame3dSubScene().checkIntersectionBulletWithAsteroid(bullet);
					
					if(intersectedAsteroids.size()>0){
						for(int i=0; i<intersectedAsteroids.size(); i++){
							int newScore = calculateScore(currentGame.getScore(), intersectedAsteroids.get(i).getDistanceFromTarged(), intersectedAsteroids.get(i).getSpeed(), currentGame.getGameType());
							// update model
							currentGame.setScore(newScore);
							modelManager.addEvent(new FireEvent(modelManager.getCurrentPlayingPlayer(), true, new Date(),currentGame));
							bullet.setHitAsteroid(true);
							//update view
							viewManager.setScore(newScore);
						
						}
					}
				}
			};

			t.playFromStart();
		}
	};
		
	private void setCloseOperation(Stage stage){
		stage.setOnCloseRequest(
		new EventHandler<WindowEvent>()
		{ public void handle(WindowEvent event)
		{ 
			System.out.println("Client closed");
			viewManager.stopAnimationThreads();
	 		modelManager.disconnectFromServer();
		}
		});
	}
	
   	// control mouse click events from view on 2D buttons.
	public final EventHandler<MouseEvent> buttonsClickListener = new EventHandler<MouseEvent>()
	{
		public void handle(MouseEvent e) {

			MyImageButton buttonClicked = (MyImageButton)e.getSource();
		
			switch (buttonClicked.getBtn_ID()) {
				case PLAY_BTN:
					viewManager.showChooseGameDifficultyPanel();
					currentViewingPanel=PANEL_TYPE.CHOOSE_DIFFICULTY;
				break;
		
				case TRAINING_BTN:
					startGame(GAME_TYPE.TRAINING,modelManager.getCurrentPlayingPlayer());
					currentViewingPanel=PANEL_TYPE.PLAYING;
				break;
				case BACK_TO_START_GAME_BTN:
					
					switch (currentViewingPanel) {
					case REGISTER:
						viewManager.hideRegisterGamePanel();
						viewManager.hideBackButton();
						viewManager.showLoginGamePanel();
						currentViewingPanel=PANEL_TYPE.LOGIN;
					break;

					case PLAYING:
						viewManager.onBackToStartGamePanel();
						if(gameCountDownTimer!=null){
							gameCountDownTimer.cancel();
							gameCountDownTimer=null;
						}
						currentViewingPanel=PANEL_TYPE.START_GAME;
					break;
					case START_GAME:
						viewManager.hideStartGamePanel();
						viewManager.hideBackButton();
						viewManager.showLoginGamePanel();
						currentViewingPanel=PANEL_TYPE.LOGIN;
					break;
					
					case CHOOSE_DIFFICULTY:
						viewManager.hideDifficultyGamePanel();
						viewManager.showStartGamePanel();
						currentViewingPanel=PANEL_TYPE.START_GAME;

					break;
					
					default:
					break;
					}
					
			break;
			case EASY_BTN:
				startGame(GAME_TYPE.EASY,modelManager.getCurrentPlayingPlayer());
				currentViewingPanel = PANEL_TYPE.PLAYING;
			break;
			case HARD_BTN:
				startGame(GAME_TYPE.HARD,modelManager.getCurrentPlayingPlayer());
				currentViewingPanel = PANEL_TYPE.PLAYING;
			break;
			case MEDUIM_BTN:
				startGame(GAME_TYPE.MEDIUM,modelManager.getCurrentPlayingPlayer());
				currentViewingPanel = PANEL_TYPE.PLAYING;
			break;
			case LOGIN_BTN:
				LoginPanel loginPangel = viewManager.getLoginPanel();
				String email = loginPangel.getEmail();
				String password = loginPangel.getPassword();
				modelManager.checkUserAuthenticationOnDB(new Player(email, null, password));
			break;
			case REGISTER_BTN:
				onRegisterNewUser();
			break;
			case REGISTER_SMALL_BTN:
				viewManager.hideLoginGamePanel();
				viewManager.showRegisterGamePanel();
				viewManager.showBackButton();
				currentViewingPanel=PANEL_TYPE.REGISTER;
			break;
			default:
			break;
			
			}
		}
	};
	
	// called from server when new player registered,
	// if register success server sends new player else sends null pointer.
	public void onRegiserNewPlayer(Player newRegisteredPlayer){
		
		RegisterPanel registarPanelInstance = viewManager.getRegisterPanel();
		
		if(newRegisteredPlayer==null){
			Platform.runLater(new Runnable() {
	            @Override public void run() {
	    			registarPanelInstance.showErrorMessage("User already exist");
	        		}
	        });
		}
		else{
			Platform.runLater(new Runnable() {
	            @Override public void run() {
					registarPanelInstance.showMessage("Register Successfully !");
	        		}
	        });
		}
	}
	
	// called from model when server sends OK message for login authentication
 	public void onUserAuthenticated(Player player){
		
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	modelManager.setCurrentPlayingPlayer(player);
        		viewManager.hideLoginGamePanel();
        		viewManager.showStartGamePanel();
        		viewManager.showBackButton();
        		currentViewingPanel=PANEL_TYPE.START_GAME;
        		}
        });
	}
	
	// called from model when server checks if user Authenticated for login
	public void onUserAuthentication(boolean isAuthendicated){
		LoginPanel loginPangel = viewManager.getLoginPanel();
		if(isAuthendicated){
			String email = loginPangel.getEmail();
			modelManager.getPlayerFromDB(email);
		}else{
			Platform.runLater(new Runnable() {
	            @Override public void run() {
	    			loginPangel.showErrorMessage("Wrong login information");
	            }
	        });
		}
	}
	
	// called when register button pressed in register panel
	// checks fields validity and updates model.
 	private void onRegisterNewUser(){
		RegisterPanel registarPanelInstance = viewManager.getRegisterPanel();
		
		String email = registarPanelInstance.getEmail();
		String name = registarPanelInstance.getName();
		String password = registarPanelInstance.getPassword();
		
		if(email.isEmpty() || name.isEmpty() || password.isEmpty()){
			registarPanelInstance.showErrorMessage("Error : Empty fields");
		}else if(!EmailValidator.getInstance().isValid(email)){
			registarPanelInstance.showErrorMessage("Please input valid email");
		}else{
			modelManager.registerNewPlayer(new Player(email, name, password));
		}
	}
	
 	private void startGame(GAME_TYPE gameType,Player player){	
		viewManager.onGameStart(gameType,player);
		modelManager.setCurrentGame(new Game(new Date(), gameType,player));
		modelManager.restCannonFireEvents();
		if(gameType!=GAME_TYPE.TRAINING){
			initGameCountDownTimer();
		}
	}
	
 	private void onGameOver(){
 		Player currentPlayer = modelManager.getCurrentPlayingPlayer();
 		Game   currentGame = modelManager.getCurrentGame();
 		String messageToPlayer = "Good job "+currentPlayer.getName() + "\n Your score is : " + currentGame.getScore();
 		viewManager.onGameOver(messageToPlayer);
		modelManager.addNewGame(currentGame);
	}

 	public void onFaildToConnectServer(String message){
 		Platform.runLater(new Runnable() {
            @Override public void run() {
				viewManager.getLoginPanel().showErrorMessage("Unable to connect server \n"+message); 
            }
        });
 	}
	
 	public void onCloseOperation(){
 		
 	}
 	
  	private void initGameCountDownTimer(){
		
		gameCountDownTimer= new Timer();
		gameCountDownTimer.schedule(new TimerTask() {
			int elapsedTime = Constants.PLAY_TIME_MILLISEC;
			@Override
			public void run() {
				
				if(elapsedTime==0){
					gameCountDownTimer.cancel();
					Platform.runLater(new Runnable() {
			            @Override public void run() {
							onGameOver();
			            }
			        });
				}
				
				elapsedTime-=1;
				
				String timeAsString = String.format("%02d:%02d:%02d", 
						TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60,
						TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60,
						((elapsedTime / 10) % 100)
						
					);
				// update ui
				Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	viewManager.setTime(timeAsString);
		            }
		        });
			}
		}, 0, 1); 
		
	}
	
	private int calculateScore(int currentScore,float asteroidDistance,float asteroedSpeed,GAME_TYPE gameDifficulty){
		
		int newScore = 0;
		switch (gameDifficulty) {
		case EASY:
			newScore = (int) (currentScore + (( asteroidDistance + asteroedSpeed)/Constants.SCORE_FACTOR)); 
			break;
		case HARD:
			newScore = (int) (currentScore + ( asteroidDistance + asteroedSpeed)/Constants.SCORE_FACTOR)*3; 
			break;
		case MEDIUM:
			newScore = (int) (currentScore + ( asteroidDistance + asteroedSpeed)/Constants.SCORE_FACTOR)*2; 
			break;
		default:
			break;
		}
		return newScore;
	}
	
	private Cannon loadCannonToView(){
		return new Cannon(modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_BASE), modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_BASE_CONNECTOR), modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_GUN));
	}
}
	
	