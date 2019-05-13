package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.commons.codec.digest.DigestUtils;

import Entities.FireEvent;
import Entities.Game;
import Entities.Player;
import GameApplication.Constants;
import GameApplication.Constants.SERVER_FUNCTION_ID;
/**
 * This class used for communication with client to game Database
 * Holds Data access object which used JDBC drivers for communication 
 * with MySQL Data base . 
 * @author Stanislav Bodik
 */
public class GameServer {
	
	private DAO dataBaseAccessObject;
	
	public GameServer(DAO dataBaseAccessObject ) {
		
		this.dataBaseAccessObject = dataBaseAccessObject;
		
		// instantiation of clients connection thread and waits for requests from client
		initClientConnectionsThread();		
	}
	
	//Infinite thread listening for new client connections
	public void initClientConnectionsThread(){
		
		new Thread(() -> {
			try {
				
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);

				while(true){
					Socket clientSocket = serverSocket.accept();
					InetAddress clientInetAddress = clientSocket.getInetAddress();
					System.out.println("Client : " + clientInetAddress + " connected");
					initReciveThreadForClient(clientSocket);
				}

			}
			catch(IOException ex) {
				System.err.println(ex);
			}
		}).start();
		
	}
	
	// thread for receiving request from client while client is alive
	public void initReciveThreadForClient(Socket clientSocket){
		
		new Thread(() -> {
			try {
				
				ObjectInputStream inputStreamFromClient = new ObjectInputStream(clientSocket.getInputStream());
				ObjectOutputStream outputStreamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
				while (true) {
					SERVER_FUNCTION_ID functionID = (SERVER_FUNCTION_ID) inputStreamFromClient.readObject();
					Object object = inputStreamFromClient.readObject();
					runFunctionOnServer(outputStreamToClient,functionID,object);
					
					if(functionID==SERVER_FUNCTION_ID.closeConnection){
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	// used to run different functions on server by type requests from client
	public void runFunctionOnServer(ObjectOutputStream outputStreamToClient, SERVER_FUNCTION_ID serverFunctionID,Object object) throws IOException{
		
		switch (serverFunctionID) {
		case getPlayerFromDB:
			Player player = getPlayerFromDB((String)object);
			outputStreamToClient.writeObject(SERVER_FUNCTION_ID.getPlayerFromDB);
			outputStreamToClient.writeObject(player);
			break;
		case addNewGame:
			addNewGame((Game)object);
			break;
		case isUserExistInDB:
			outputStreamToClient.writeObject(SERVER_FUNCTION_ID.isUserExistInDB);
			if(isUserExistInDB(object.toString())){
				outputStreamToClient.writeObject(true);
			}else{
				outputStreamToClient.writeObject(false);
			}
			break;
		case loginAuthenticationDB:
			outputStreamToClient.writeObject(SERVER_FUNCTION_ID.loginAuthenticationDB);
			if(loginAuthenticationDB((Player)object)){
				outputStreamToClient.writeObject(true);
			}else{
				outputStreamToClient.writeObject(false);
			}
			break;
		case registerNewPlayer:
			outputStreamToClient.writeObject(SERVER_FUNCTION_ID.registerNewPlayer);
			if(registerNewPlayer((Player)object)){
				outputStreamToClient.writeObject((Player)object);
			}else{
				outputStreamToClient.writeObject(null);
			}
			break;
		case closeConnection:
			outputStreamToClient.writeObject(SERVER_FUNCTION_ID.closeConnection);
			outputStreamToClient.writeObject(null);
			break;
		case addNewEvent:
			addNewEvent((FireEvent)object);
			break;
		default:
			break;
		}
	}
	
	public boolean addNewEvent(FireEvent event){
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat microDate = new SimpleDateFormat("ss.SSS");
		String evenTime= sdfDate.format(event.getDate().getTime());
		String microTime = microDate.format(event.getDate().getTime());
		String gameStartTime = sdfDate.format(event.getGame().getStartTime());
		String query = "INSERT INTO hitevent (playeremail,gameid,hit,time,microtime) VALUES ('"+event.getPlayer().getEmail()+"','"+gameStartTime+"','"+(event.isHidted() ? 1:0)+"','"+evenTime+"','"+microTime+"')";
		return dataBaseAccessObject.update(query)>0 ? true:false;
	}
 
	public boolean addNewGame(Game game){
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String gameStartDate= sdfDate.format(game.getStartTime().getTime());
		String query = "INSERT INTO game (id,gametype,score,playeremail) VALUES ('"+gameStartDate+"','"+game.getGameType()+"','"+game.getScore()+"','"+game.getPlayer().getEmail()+"')";
		return dataBaseAccessObject.update(query)>0 ? true:false;

	}
	
	public boolean isUserExistInDB(String email){
		String query = "select * from player where playeremail = '"+email+"'";
		ResultSet res = dataBaseAccessObject.executeQuery(query);
		try {
			return res.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean registerNewPlayer(Player player){
		String passwordHashed = encodeAsMD5(player.getPassword());
		String query = "INSERT INTO player (playeremail,name,password) VALUES ('"+player.getEmail()+"','"+player.getName()+"','"+passwordHashed+"')";
		
		if(isUserExistInDB(player.getEmail())){
			return false;
		}else{
			dataBaseAccessObject.update(query);
			return true;
		}
	}
	
	public Player getPlayerFromDB(String email){
		String query = "select * from player where playeremail = '"+email+"'";
		ResultSet res = dataBaseAccessObject.executeQuery(query);
		
		try {
			res.next();
			return new Player(res.getString(1), res.getString(2),res.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean loginAuthenticationDB(Player player){
		
		String passwordHashed = encodeAsMD5(player.getPassword());
		
		if(isUserExistInDB(player.getEmail())){
			String query = "select * from player where playeremail = '"+player.getEmail()+"'";
			ResultSet res = dataBaseAccessObject.executeQuery(query);
			String passwordDB = null;
			try {
				res.next();
				 passwordDB = res.getString(3);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(passwordDB!=null && passwordHashed.equals(passwordDB)){
				return true;
			}
		}
		return false;
	}

	public String encodeAsMD5(String password) {
		   return DigestUtils.md5Hex(password);
	}
}
