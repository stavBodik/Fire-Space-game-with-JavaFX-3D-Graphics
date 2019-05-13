package GameApplication;
import java.util.Iterator;
import java.util.LinkedHashSet;
import Entities.FireEvent;
import Entities.Game;
import Entities.Player;
import Game3D.ObjectsContainer;
import GameApplication.Constants.OBJECT_NAMES;
import javafx.scene.Group;

/**
 * This class used to manage application model domain , 
 * Stores data of game entities and have access to game server data base . 
 * @author Stanislav Bodik
 */

public class ModelManager {

	public  ObjectsContainer objectsContainer;
	private Game currentGame;
	private Player currentPlayingPlayer;
	private GameClient gameClient;
	private LinkedHashSet<FireEvent> currentGameEvents = new LinkedHashSet<FireEvent>();

	
 	public void init(ControlManager controlManagerInstance){
		//loads objects from files and using as objects information container (vertexes/texture/materials)
		objectsContainer = new ObjectsContainer();
		
		// game client used to communicate with server
		gameClient = new GameClient(controlManagerInstance);
	}
	
 	public void addEvent(FireEvent event){
 		currentGameEvents.add(event);
 	}
 	
 	public void registerCannonFireEventsInDB(){
 		Iterator<FireEvent> it = currentGameEvents.iterator();

 		while(it.hasNext()){
 			registerEvent(it.next());
 		}
 	}
 
 	public void restCannonFireEvents(){
 		currentGameEvents.clear();
 	}
 
 	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}
	
	public Player getCurrentPlayingPlayer() {
		return currentPlayingPlayer;
	}
	
	// returns group of mesh views which is loaded from object files
	public Group getObjectMeshViewGroup(OBJECT_NAMES objectName){
		return objectsContainer.getObjectMeshViewGroup(objectName);
	}
	
	public void registerNewPlayer(Player player){
		gameClient.registerNewPlayer(player);
	}

	public void getPlayerFromDB(String email){
		gameClient.getPlayerFromDB(email);
	}
	
	public void setCurrentPlayingPlayer(Player currentPlayingPlayer) {
		this.currentPlayingPlayer = currentPlayingPlayer;
	}
	
	public void checkUserAuthenticationOnDB(Player player){
		gameClient.loginAuthenticationDB(player);
	}
	
	public void connectToServer(){
		gameClient.connectToServer();
	}
	
	public void disconnectFromServer(){
		gameClient.disconnectFromServer();
	}

	public void addNewGame(Game game){
		gameClient.addNewGame(game);
		registerCannonFireEventsInDB();
	}
 
	public void registerEvent(FireEvent event){
		gameClient.addNewEvent(event);
	}
}
