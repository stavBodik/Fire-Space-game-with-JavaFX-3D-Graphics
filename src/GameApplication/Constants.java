package GameApplication;
import java.io.Serializable;

import Game3D.MyPoint3D;
import Game3D.Skybox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Application constants.
 * @author Stanislav Bodik
 */
public class Constants {
	
	//-------------------3D------------------//
	//Sky box textures
    public static Image 
    top = new Image(Skybox.class.getResource("resources/top.png").toExternalForm()),
    bottom = new Image(Skybox.class.getResource("resources//bottom.png").toExternalForm()),
    left = new Image(Skybox.class.getResource("resources//left.png").toExternalForm()),
    right = new Image(Skybox.class.getResource("resources//right.png").toExternalForm()),
    front = new Image(Skybox.class.getResource("resources//front.png").toExternalForm()),
    back = new Image(Skybox.class.getResource("resources//back.png").toExternalForm());
    
	// Main 3D world Scene UI properties
	public static final Color MAIN_SCENE_BACKGROUND_COLOR = Color.color(1, 1, 1, 1.0);
	public static final float WORLD_SIZE=50;
	
	//cameraPosition
	public static final MyPoint3D cameraEyePositionPlay = new MyPoint3D(0, -2,-5);
	public static final MyPoint3D cameraEyePositionNormal = new MyPoint3D(0, -2,-10);

	//objects
	public static final String[] objects_files = {"gun TBS 001C.obj","projektil OBJ.obj","asteroid OBJ.obj"};
	public static enum OBJECT_NAMES { CANNON_BASE, CANNON_BASE_CONNECTOR, CANNON_GUN, BULLET,ASTEROID }
	
	//Moon textures
	public static final String MOON_TEXTURE = "MoonColorMap.png";
	public static final String MOON_TEXTURE1 = "MoonReliefMap.png";
	
	//Bullet textures
	public static final String BULLET_TEXTURE = "REFMAP.GIF";

	//Asteroid textures
	public static final String ASTROID_TEXTURE = "astroid_tex.jpg";

	//-------------------2D------------------//
	
	//application icon
	public static final String APP_ICON = "icon.jpg";

	
	//button images
	public static final String PLAY_BTN_IMG = "play.png";
	public static final String TRAINING_BTN_IMG = "training.png";
	public static final String BACK_TO_START_GAME_BTN_IMG = "back.png";
	public static final String EASY_BTN_IMG = "easy.png";
	public static final String MEDUIM_BTN_IMG = "meduim.png";
	public static final String HARD_BTN_IMG = "hard.png";
	public static final String REGISTER_BTN_IMG = "register.png";
	public static final String REGISTER_SMALL_BTN_IMG = "registersmall.png";
	public static final String LOGIN_BTN_IMG = "login.png";

	//button IDS
	public static enum BUTTON_IDS {LOGIN_BTN,REGISTER_SMALL_BTN,REGISTER_BTN,PLAY_BTN,TRAINING_BTN,BACK_TO_START_GAME_BTN,EASY_BTN,HARD_BTN,MEDUIM_BTN}
	
	//viewing panel type 
	public static enum PANEL_TYPE {PLAYING,CHOOSE_DIFFICULTY,START_GAME,REGISTER,LOGIN}
	
	// game types
	public static enum GAME_TYPE {EASY,MEDIUM,HARD,TRAINING}
	
	// time for playing game (2 minutes)
	public static final int PLAY_TIME_MILLISEC= 30000;

	
	//-------------------DB------------------//
	public static final String DB_ADDRESS = "127.0.0.1";
	public static final String DB_NAME = "firespace";
	public static final String DB_USER = "scott";
	public static final String DB_PASSWORD = "tiger";


	//-------------------SERVER------------------//
	public static final String SERVER_ADRESS = "127.0.0.1";
	public static final int    SERVER_PORT = 9999;

	//-------------------GAME------------------//
	public static final double SCORE_FACTOR=10000;

	
	//-------------------CLIENT_SERVER_OPERATIONS------------------//
	public static enum SERVER_FUNCTION_ID implements Serializable {addNewEvent,closeConnection,getPlayerFromDB,addNewGame,loginAuthenticationDB,registerNewPlayer,isUserExistInDB}

}
