package Application2D_UI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class used to show information to player while playing game : name,time left,current score. 
 * @author Stanislav Bodik
 */

public class BottomInfoPanel extends VBox{

	private Label playerNameLB,gameTimeLeftLB,scoreLB;
	
	public BottomInfoPanel(int panelWidth) {
		setPrefWidth(panelWidth);
		setSpacing(10);
		
		GridPane gp = new GridPane();
		Background c = new Background(new BackgroundFill(Color.web("#00000030"), CornerRadii.EMPTY, Insets.EMPTY));
		gp.setBackground(c);
		designColumns(gp);
		addComponents(gp);

		StackPane gameTimeLeftLBContainer = new StackPane();
		gameTimeLeftLB = new Label("00:00:00");
		gameTimeLeftLB.setTextFill(Color.WHITE);
		gameTimeLeftLB.setFont(new Font("Arial", 30));
		gameTimeLeftLBContainer.setPrefWidth(panelWidth);
		gameTimeLeftLBContainer.getChildren().add(gameTimeLeftLB);
		
		getChildren().add(gameTimeLeftLBContainer);
		getChildren().add(gp);

	}
	
	private void addComponents(GridPane gp){
		
		
		StackPane playerNameLBContainer = new StackPane();
		playerNameLB = new Label("Stav Bodik");
		playerNameLB.setTextFill(Color.WHITE);
		playerNameLBContainer.getChildren().add(playerNameLB);
		gp.add(playerNameLBContainer, 0, 0);
		
		
		StackPane scoreLBContainer = new StackPane();
		scoreLB = new Label("Score : 100");
		scoreLB.setTextFill(Color.WHITE);
		scoreLBContainer.getChildren().add(scoreLB);
		gp.add(scoreLBContainer, 2, 0);
		
	}
	
	public void setScore(int score){
		scoreLB.setText("Score : " + Integer.toString(score));
	}
	
	public void setPlayerName(String name){
		playerNameLB.setText(name);
	}

	public void setTime(String time){
		gameTimeLeftLB.setText(time);
	}
	
  	private void designColumns(GridPane gp){
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(100/3);
		column1.setFillWidth(true);

		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(100/3);
		column2.setFillWidth(true);
		
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(100/3);
		column3.setFillWidth(true);
		
		gp.getColumnConstraints().add(column1);
		gp.getColumnConstraints().add(column2);
		gp.getColumnConstraints().add(column3);

	}

  	public void showTime(){
  		gameTimeLeftLB.setOpacity(1);
  	}
	
  	public void hideScore(){
  		scoreLB.setOpacity(0);
  	}
  	
  	public void showScore(){
  		scoreLB.setOpacity(1);
  	}
  	
   	public void hideTime(){
  		gameTimeLeftLB.setOpacity(0);

  	}
}
