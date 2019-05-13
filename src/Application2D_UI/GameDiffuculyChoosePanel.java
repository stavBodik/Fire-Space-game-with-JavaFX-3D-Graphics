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
 * This class used to show 3 buttons options for playing game easy,medium,hard. 
 * @author Stanislav Bodik
 */

public class GameDiffuculyChoosePanel extends StackPane{

	MyImageButton easyBTN,meduimBTN,hardBTN;
	
	public GameDiffuculyChoosePanel(int panelWidth,int PanelHeight) {

	
		setPrefWidth(panelWidth);
		setPrefHeight(PanelHeight);
		Background c = new Background(new BackgroundFill(Color.web("#00000070"), CornerRadii.EMPTY, Insets.EMPTY));
		setBackground(c);
		
		VBox v = new VBox();
		v.setAlignment(Pos.TOP_CENTER);
		v.setPadding(new Insets(PanelHeight*0.2, 0, 0, 0));
		v.setMaxWidth(panelWidth/2);
		v.setSpacing(PanelHeight*0.1);
		
		easyBTN = new MyImageButton(BUTTON_IDS.EASY_BTN,Constants.EASY_BTN_IMG, 278, 105, panelWidth*0.2, PanelHeight*0.08);
		v.getChildren().add(easyBTN);
		
		 meduimBTN = new MyImageButton(BUTTON_IDS.MEDUIM_BTN,Constants.MEDUIM_BTN_IMG, 278, 105,panelWidth*0.2, PanelHeight*0.08);
		v.getChildren().add(meduimBTN);
		
		hardBTN = new MyImageButton(BUTTON_IDS.HARD_BTN,Constants.HARD_BTN_IMG, 278, 105, panelWidth*0.2, PanelHeight*0.08);
		v.getChildren().add(hardBTN);
		
		
	    StackPane.setAlignment(v, Pos.BOTTOM_CENTER);

		getChildren().add(v);
	
	}
	
	public void setEventHandler(EventHandler<MouseEvent> mouseEventHandler){
		easyBTN.setOnMousePressed(mouseEventHandler);
		meduimBTN.setOnMousePressed(mouseEventHandler);
		hardBTN.setOnMousePressed(mouseEventHandler);
	}
}
