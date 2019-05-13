package GameApplication;

/**
 * Application manager root which initiate and holds instance of MVC model managers.
 * @author Stanislav Bodik
 */

public class ApplicationManager {
	//MVC managers
	private ModelManager   modelManager;
	private ViewManager    viewManager;
	private ControlManager controlManager;

	public void initApplication(){
		modelManager = new ModelManager();		
		viewManager = new ViewManager();
		controlManager = new ControlManager(modelManager,viewManager);
		
		modelManager.init(controlManager);
		viewManager.Init(modelManager);
		controlManager.init();
	}
}
