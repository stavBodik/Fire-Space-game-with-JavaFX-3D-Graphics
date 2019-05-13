package Server;

import java.sql.ResultSet;

import Entities.Player;
import GameApplication.ApplicationManager;
import GameApplication.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * Server application Main class used to run Game server and
 * Enable viewing/editing application Database. 
 * @author Stanislav Bodik
 */

public class Gun3d_306667478 extends Application {
		 
	private DAO dataBaseAccessObject;
	@SuppressWarnings("rawtypes")
	private TableView<ObservableList> tableView = new TableView<>();
	private GameServer gameServer;
	@Override
	 public void start(Stage stage) throws Exception {	
		// load JDBC drivers and communicate with DB
		dataBaseAccessObject = new DAO(Constants.DB_ADDRESS, Constants.DB_NAME, Constants.DB_USER, Constants.DB_PASSWORD);
			
		// manage requests from client more info inside
		gameServer = new GameServer(dataBaseAccessObject);
		
		// small application with UI in server side for viewing and editing DB tables
		// also option for run multiply clients
		createServerUI(stage);		
	}

	 public void runNewClient(){
		 new Thread(() -> {
				Platform.runLater(new Runnable() {
		            @Override public void run() {
						new ApplicationManager().initApplication();
		        		}
		        });
			}).start();
	 }
	 
	 public void createServerUI(Stage stage){
		 
		 BorderPane mainPanel = new BorderPane();
		 
		 VBox buttonsHolderVB = new VBox();
		 buttonsHolderVB.setSpacing(10);
		 
		 Button editPlayersBT = new Button("Edit players");
		 Button query1Bt = new Button("Query 1");
		 Button query2Bt = new Button("Query 2");
		 Button query3Bt = new Button("Query 3");
		 Button query4Bt = new Button("Query 4");
		 Button newClient = new Button("New Client");

		 
		 HBox buttonsRow1HB = new HBox();
		 buttonsRow1HB.getChildren().add(editPlayersBT);
		 buttonsRow1HB.getChildren().add(query1Bt);
		 buttonsRow1HB.getChildren().add(query2Bt);
		 buttonsRow1HB.getChildren().add(query3Bt);
		 buttonsRow1HB.getChildren().add(query4Bt);
		 buttonsRow1HB.getChildren().add(newClient);

		 buttonsHolderVB.getChildren().addAll(buttonsRow1HB);
		 
		 Button addPlayerBT = new Button("Add Player");
		 Button deletePlayerBT = new Button("Delete Player");
		 Button commitBT = new Button("Commit Changes");

		 HBox buttonsRow2HB = new HBox();
		 buttonsRow2HB.getChildren().add(addPlayerBT);
		 buttonsRow2HB.getChildren().add(deletePlayerBT);
		 buttonsRow2HB.getChildren().add(commitBT);
		 buttonsRow2HB.setVisible(false);
		 buttonsHolderVB.getChildren().addAll(buttonsRow2HB);

		 mainPanel.setTop(buttonsHolderVB);
		 mainPanel.setCenter(tableView);
		 
		 tableView.setEditable(true);
		 
		 
		 Scene scene = new Scene(mainPanel,getScreenSize().getWidth()*0.7 , getScreenSize().getHeight()*0.6);
		 stage.setTitle("Fire Space server"); // Set the stage title
		 stage.setScene(scene); // Place the scene in the stage
		 stage.setAlwaysOnTop(true);
		 stage.show();
		 
		 setOnCloseOperation(stage);
		 
		 
		 deletePlayerBT.setOnMouseClicked(new EventHandler<Event>() {
			 @Override
			 public void handle(Event event) {
				 if(tableView.getItems().size()>0){
					 @SuppressWarnings("rawtypes")
					ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();				
					 TablePosition<?, ?> tablePosition = selectedCells.get(0);    	  
					 tableView.getItems().remove(tablePosition.getRow());
				 }
			 }
		 });
		 
		 commitBT.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				//clear table
				dataBaseAccessObject.update("truncate player");
				for(int i=0; i<tableView.getItems().size(); i++){		
					Player p = new Player(tableView.getItems().get(i).get(0).toString(), tableView.getItems().get(i).get(1).toString(), tableView.getItems().get(i).get(2).toString());		
					replaceAddPlayerInfo(p);
				}
				
			}
		});
		 
		 addPlayerBT.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
		         ObservableList<String> row = FXCollections.observableArrayList();
		         row.add("email");
		         row.add("name");
		         row.add("password");
		         tableView.getItems().add(row);
			}
		});
		 
		 editPlayersBT.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				 buttonsRow2HB.setVisible(true);
				 String query = "select * from player";
				 ResultSet res = dataBaseAccessObject.executeQuery(query);
				 populateTableView(res);
			}
		});
		 
		 //-----------------------QUERYS---------------//
		 query1Bt.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					 buttonsRow2HB.setVisible(false);
					 String query = "select distinct * from (player inner join (game inner join hitevent using (playeremail))using(playeremail)) order by player.name,game.id,hitevent.time;";
					 ResultSet res = dataBaseAccessObject.executeQuery(query);
					 populateTableView(res);
				}
			});
		 
		 query2Bt.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					 buttonsRow2HB.setVisible(false);
					 String query = "select distinct * from (player inner join (game inner join hitevent using (playeremail))using(playeremail)) order by player.name,game.score desc,hitevent.time;";
					 ResultSet res = dataBaseAccessObject.executeQuery(query);
					 populateTableView(res);
				}
			});
		 
		 query3Bt.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					 buttonsRow2HB.setVisible(false);
					 String query = "select distinct * from player inner join game using(playeremail) order by game.score desc;";
					 ResultSet res = dataBaseAccessObject.executeQuery(query);
					 populateTableView(res);
				}
			});
		 	 
		 query4Bt.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					 buttonsRow2HB.setVisible(false);
					 String query =  " SELECT name,AVG(s.score) "+
							 		 " FROM   (select * from player inner join(select * from game order by game.score desc)m using (playeremail)) s "+
							 		 " WHERE "+ 
							         "("+
							            " SELECT  COUNT(*) "+ 
							            " FROM    (select * from player inner join(select * from game order by game.score desc)m using (playeremail)) f"+
							            " WHERE f.playeremail = s.playeremail AND "+ 
							                  " f.score >= s.score " +
							         " ) <= 3 and playeremail in (SELECT playeremail FROM game GROUP BY playeremail HAVING COUNT(*) >= 3) group by s.playeremail";
					 ResultSet res = dataBaseAccessObject.executeQuery(query);
					 populateTableView(res);
				}
			});
		 
		 
		 newClient.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				runNewClient();
			}
		});
		 
		 Image appIcon = new Image(Gun3d_306667478.class.getResource("/GameApplication/resources/"+Constants.APP_ICON).toExternalForm(),225,225,true,true);
	     stage.getIcons().add(appIcon);
		 
	 }
	
	 public boolean replaceAddPlayerInfo(Player player){
		 	player.setPassword(gameServer.encodeAsMD5(player.getPassword()));
			String query = "REPLACE INTO player (playeremail,name,password) VALUES ('"+player.getEmail()+"','"+player.getName()+"','"+player.getPassword()+"')";
			return dataBaseAccessObject.update(query)>0 ? true:false;
	}
	 
	 private Rectangle2D getScreenSize(){
			return Screen.getPrimary().getVisualBounds();
		}
	 
	 private void setOnCloseOperation(Stage stage){
		 stage.setOnCloseRequest(
				 new EventHandler<WindowEvent>()
				 { public void handle(WindowEvent event)
				 { 
					 Platform.exit();
					 System.exit(0);
				 }
				 });
	 }
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 private void populateTableView(ResultSet rs)
	  { 
		        
		  try{
			  
		  ObservableList<ObservableList>  data = FXCollections.observableArrayList();

		  tableView.getColumns().clear();
		  
		  /**********************************
	       * TABLE COLUMN ADDED DYNAMICALLY *
	       **********************************/
	      for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
	          //We are using non property style for making dynamic table
	          final int j = i;                
	          TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
	          
	          Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
	              @Override
	              public TableCell call(TableColumn p) {
	                  return new EditingCell();
	              }
	          };
	          
	          
	          col.setCellFactory(cellFactory);

	          col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
	              public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
	                 
	            	  if(param.getValue().get(j)!=null){
	            	  return new SimpleStringProperty(param.getValue().get(j).toString());                        
	            	  }
	            	  else {return new SimpleStringProperty("");
	            	  }
	              }                    
	          });

	          
	          col.setOnEditCommit(new EventHandler<CellEditEvent<String, String>>() {
	              @Override
	              public void handle(CellEditEvent<String, String> t) { 
	            	  System.out.println("New edit : " + t.getNewValue());
	            	  tableView.getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());	            	   
	              }
	          });
	          
	          tableView.getColumns().addAll(col); 
	         // System.out.println("Column ["+i+"] ");
	      }

	      /********************************
	       * Data added to ObservableList *
	       ********************************/
	      while(rs.next()){
	          //Iterate Row
	          ObservableList<String> row = FXCollections.observableArrayList();
	          for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	              //Iterate Column
	              row.add(rs.getString(i));
	          }
	         // System.out.println("Row [1] added "+row );
	          data.add(row);

	      }

	      //FINALLY ADDED TO TableView
	      tableView.setItems(data);
	    	
	    } 
	    catch (Exception e)
	    { 
	      e.printStackTrace();
	      System.out.println("Error on Building Data");
	    }
	  }
	  
	 public static void main(String[] args) {
        launch(args);
     }

	

}

