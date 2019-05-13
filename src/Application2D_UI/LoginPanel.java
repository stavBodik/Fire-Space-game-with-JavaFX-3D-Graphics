package Application2D_UI;

import GameApplication.Constants;
import GameApplication.Constants.BUTTON_IDS;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class used to show main application panel which is the login
 * page includes text fields and buttons.
 * @author Stanislav Bodik
 */

public class LoginPanel extends StackPane{
	
	private int fieldTypeText=0;
	private int fieldTypePassword=1;
	private TextFieldText emailTFT,passwordTFT;
	private Label messageLabel;
	MyImageButton regiserBTN,loginBTN;
	
	public LoginPanel(int panelWidth,int PanelHeight) {

	
		setPrefWidth(panelWidth);
		setPrefHeight(PanelHeight);
		Background c = new Background(new BackgroundFill(Color.web("#00000070"), CornerRadii.EMPTY, Insets.EMPTY));
		setBackground(c);
		
		VBox v = new VBox();
		c = new Background(new BackgroundFill(Color.web("#ff000070"), CornerRadii.EMPTY, Insets.EMPTY));
		v.setAlignment(Pos.TOP_CENTER);
		v.setPadding(new Insets(PanelHeight*0.2, 0, 0, 0));
		v.setMaxWidth(panelWidth*0.3);
		v.setSpacing(PanelHeight*0.02);
		
		messageLabel = new Label();
		v.getChildren().add(messageLabel);
		
		VBox gridsContainer = new VBox();
		c = new Background(new BackgroundFill(Color.web("#ffffff70"), CornerRadii.EMPTY, Insets.EMPTY));
		gridsContainer.setBackground(c);
		emailTFT = new TextFieldText("Email : ",fieldTypeText);
		gridsContainer.getChildren().add(emailTFT);
		passwordTFT = new TextFieldText("Password : ",fieldTypePassword);
		gridsContainer.getChildren().add(passwordTFT);
		gridsContainer.setSpacing(PanelHeight*0.02);
		gridsContainer.setPadding(new Insets(5,5,5,5));
		v.getChildren().add(gridsContainer);
		
		
		HBox buttonsPanel = new HBox();
		loginBTN = new MyImageButton(BUTTON_IDS.LOGIN_BTN,Constants.LOGIN_BTN_IMG, 174, 105, panelWidth*0.15, PanelHeight*0.08);
		buttonsPanel.getChildren().add(loginBTN);
		
		regiserBTN = new MyImageButton(BUTTON_IDS.REGISTER_SMALL_BTN,Constants.REGISTER_SMALL_BTN_IMG, 174, 105, panelWidth*0.15, PanelHeight*0.08);
		buttonsPanel.getChildren().add(regiserBTN);

		
	    StackPane.setAlignment(v, Pos.BOTTOM_CENTER);
		v.getChildren().add(buttonsPanel);

		getChildren().add(v);

	
		
	
	}
	
	public void setEventHandler(EventHandler<MouseEvent> mouseEventHandler){
		regiserBTN.setOnMousePressed(mouseEventHandler);
		loginBTN.setOnMousePressed(mouseEventHandler);
	}
	
	private class TextFieldText extends GridPane{
		
		private Control textControl;
		public TextFieldText(String text,int type){
			
			
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setPercentWidth(35);
			column1.setFillWidth(true);

			ColumnConstraints column2 = new ColumnConstraints();
			column2.setPercentWidth(65);
			column2.setFillWidth(true);
			
			getColumnConstraints().add(column1);
			getColumnConstraints().add(column2);
			
			Label textLB = new Label(text);
			textLB.setTextFill(Color.WHITE);
			
			if(type==fieldTypeText){
				textControl = new TextField();
			}else{
				textControl = new PasswordField();
			}
			
			Background c = new Background(new BackgroundFill(Color.web("#ff000000"), CornerRadii.EMPTY, Insets.EMPTY));
			textControl.setBackground(c);
			textControl	.setStyle("-fx-text-inner-color: white;");
			textControl.setStyle("-fx-background-color: rgba(255,255,255, 0.5)");

			add(textLB, 0, 0);
			add(textControl, 1, 0);
			
			
		}
		
		public Control getTextControl() {
			return textControl;
		}
	}

	public String getEmail(){
		return((TextField)emailTFT.getTextControl()).getText();
	}
	
	public void showErrorMessage(String message){
		messageLabel.setTextFill(Color.RED);
		messageLabel.setText(message);
	}
	
	public String getPassword(){
		return((PasswordField)passwordTFT.getTextControl()).getText();
	}
}
