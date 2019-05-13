package Application2D_UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class used to show game over message when time runs out.
 * @author Stanislav Bodik
 */

public class GameOverPanel extends StackPane{
	
	private Label messageLabel;

	public GameOverPanel(int panelWidth,int PanelHeight) {

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
		messageLabel.setTextFill(Color.WHITE);

		v.getChildren().add(messageLabel);
				
		
	    StackPane.setAlignment(v, Pos.BOTTOM_CENTER);

		getChildren().add(v);
	}

	public void showMessage(String message){
		messageLabel.setText(message);
	}
		
}
