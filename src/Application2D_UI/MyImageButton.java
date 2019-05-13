package Application2D_UI;

import GameApplication.Constants.BUTTON_IDS;
import GameApplication.ViewManager;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * This class used to create button with background image.
 * @author Stanislav Bodik
 */

public class MyImageButton extends ImageView{

	private DropShadow shadowEffect = new DropShadow();
	private BUTTON_IDS btn_ID;
	
	public MyImageButton(BUTTON_IDS btn_ID,String imageSrcName,int imgWidth,int imgHeight,double buttonWidth,double buttonHeight) {

		this.btn_ID =btn_ID;
		Image img = new Image(ViewManager.class.getClass().getResource("/Application2D_UI/resources/"+imageSrcName).toExternalForm(),imgWidth,imgHeight,true,true);
		setImage(img);
		setFitWidth(buttonWidth);
		setFitHeight(buttonHeight);
		shadowEffect.setColor(Color.WHITE);
		
		
		addEventHandler(MouseEvent.MOUSE_ENTERED,
		        new EventHandler<MouseEvent>() {
	          @Override
	          public void handle(MouseEvent e) {
	        	  setEffect(shadowEffect);
	          }
	    });
		
		addEventHandler(MouseEvent.MOUSE_EXITED,
		        new EventHandler<MouseEvent>() {
	          @Override
	          public void handle(MouseEvent e) {
	        	  setEffect(null);
	          }
	    });
		
	
	}
	
	public BUTTON_IDS getBtn_ID() {
		return btn_ID;
	}
}
