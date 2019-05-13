package Application2D_UI;

import GameApplication.Constants;
import GameApplication.Constants.BUTTON_IDS;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class used to show 2 buttons options for playing game modes : 
 * 1. normal player which have 3 modes : easy,medium,hard.
 * 2. training mode.
 * @author Stanislav Bodik
 */

public class StartGamePanel extends StackPane{

	private MyImageButton playBTN,trainingBTN;
	
	public StartGamePanel(int panelWidth,int PanelHeight) {

	
		setPrefWidth(panelWidth);
		setPrefHeight(PanelHeight);
		Background c = new Background(new BackgroundFill(Color.web("#00000070"), CornerRadii.EMPTY, Insets.EMPTY));
		setBackground(c);
		
		VBox v = new VBox();
		v.setAlignment(Pos.TOP_CENTER);
		v.setPadding(new Insets(PanelHeight*0.2, 0, 0, 0));
		v.setMaxWidth(panelWidth/2);
		v.setSpacing(PanelHeight*0.1);
		
		playBTN = new MyImageButton(BUTTON_IDS.PLAY_BTN,Constants.PLAY_BTN_IMG, 278, 105, panelWidth*0.2, PanelHeight*0.08);
		v.getChildren().add(playBTN);
		
		trainingBTN = new MyImageButton(BUTTON_IDS.TRAINING_BTN,Constants.TRAINING_BTN_IMG, 278, 105,panelWidth*0.2, PanelHeight*0.08);
		v.getChildren().add(trainingBTN);
		
	    StackPane.setAlignment(v, Pos.BOTTOM_CENTER);

		getChildren().add(v);

	}
	
	public void setEventHandler(EventHandler<MouseEvent> mouseEventHandler){
		playBTN.setOnMousePressed(mouseEventHandler);
		trainingBTN.setOnMousePressed(mouseEventHandler);

	}
}
