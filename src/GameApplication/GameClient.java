package GameApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Entities.FireEvent;
import Entities.Game;
import Entities.Player;
//import GameApplication.Constants.SERVER_FUNCTION;
import GameApplication.Constants.SERVER_FUNCTION_ID;

/**
 * This class used for communication of application client with server
 * by TCP protocol .
 * @author Stanislav Bodik
 */

public class GameClient {

	public  ObjectOutputStream outputStreamToServer;
	public  ObjectInputStream inputStreamFromServer;
	private Socket clientSocket;
	private ControlManager controlManagerInstance;
	
	public GameClient(ControlManager controlManagerInstance) {
		this.controlManagerInstance=controlManagerInstance;
	}
	
	private void installReciverFromServer(){
		
		new Thread(() -> {

			while (!clientSocket.isClosed()) {
				try {
					SERVER_FUNCTION_ID functionID = (SERVER_FUNCTION_ID) inputStreamFromServer.readObject();
					Object object = inputStreamFromServer.readObject();
					if(functionID == SERVER_FUNCTION_ID.closeConnection){
						break;
					}
					onReciveFromServer(functionID,object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}
	
	private void onReciveFromServer(SERVER_FUNCTION_ID serverFunctionID,Object object){
		
		
		switch (serverFunctionID) {
		case getPlayerFromDB:
			Player player = (Player)object;
			controlManagerInstance.onUserAuthenticated(player);
			break;
		case loginAuthenticationDB:
			controlManagerInstance.onUserAuthentication((boolean)object);
			break;
		case registerNewPlayer:
			if(object==null){
				controlManagerInstance.onRegiserNewPlayer(null);
			}else{
				controlManagerInstance.onRegiserNewPlayer((Player)object);
			}
			break;
		default:
			break;
		}
	}

 	public void connectToServer(){
		
		new Thread(() -> {

			try {
	    		clientSocket = new Socket(Constants.SERVER_ADRESS, Constants.SERVER_PORT);
	    		outputStreamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    		inputStreamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    		installReciverFromServer();
			} catch (IOException e) {
				controlManagerInstance.onFaildToConnectServer(e.getMessage());
			}
		
		}).start();
	}
 	
	public void getPlayerFromDB(String email){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.getPlayerFromDB);
			outputStreamToServer.writeObject(email);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addNewGame(Game game){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.addNewGame);
			outputStreamToServer.writeObject(game);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loginAuthenticationDB(Player player){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.loginAuthenticationDB);
			outputStreamToServer.writeObject(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerNewPlayer(Player player){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.registerNewPlayer);
			outputStreamToServer.writeObject(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void isUserExistInDB(String email){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.isUserExistInDB);
			outputStreamToServer.writeObject(new String(email));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addNewEvent(FireEvent event){
	
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.addNewEvent);
			outputStreamToServer.writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
 	public void disconnectFromServer(){
		try {
			outputStreamToServer.writeObject(SERVER_FUNCTION_ID.closeConnection);
			outputStreamToServer.writeObject(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
