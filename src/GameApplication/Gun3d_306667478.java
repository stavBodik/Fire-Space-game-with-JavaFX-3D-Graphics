package GameApplication;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Client application Main class used to initiate Application Manager 
 * @author Stanislav Bodik
 */

public class Gun3d_306667478 extends Application {
		 
	@Override
	 public void start(Stage stage) throws Exception {			
		new ApplicationManager().initApplication();
	 }
	
	 public static void main(String[] args) {
        launch(args);
     }
}
