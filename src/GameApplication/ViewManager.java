package GameApplication;

import Application2D_UI.BottomInfoPanel;
import Application2D_UI.GameDiffuculyChoosePanel;
import Application2D_UI.GameOverPanel;
import Application2D_UI.LoginPanel;
import Application2D_UI.MyImageButton;
import Application2D_UI.RegisterPanel;
import Application2D_UI.StartGamePanel;
import Entities.Player;
import Game3D.Cannon;
import Game3D.Game3DSubSceneHolder;
import GameApplication.Constants.BUTTON_IDS;
import GameApplication.Constants.GAME_TYPE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class used to manage and create application user interface
 * @author Stanislav Bodik
 */

public class ViewManager {

	private Point mainSceneScreenDimentions = new Point();
	// Sub scene which holds 3D game components
	private Game3DSubSceneHolder game3dSubScene;
	//information panel at bottom of 3d scene(player name,level,time left,score)
	private BottomInfoPanel bottomInfoPanel;
	
	// panels
	private StartGamePanel startGamePanel;
	private GameDiffuculyChoosePanel gameDifficultyPanel;
	private RegisterPanel registerPanel;
	private LoginPanel loginPanel;
	private GameOverPanel gameOverPanel;
	private Stage stage;
	private AnchorPane mainApplicationPanel;
	
	private VBox backToStartGameBTContainer;
	private MyImageButton backToStartGamePanelBT;
		
	//initiate application scenes and views
 	public void Init(ModelManager modelManagerInstance){
		 		
	 	this.stage = new Stage();
	 	
		mainSceneScreenDimentions.setX(getScreenSize().getWidth()*0.5);
		mainSceneScreenDimentions.setY(getScreenSize().getHeight()*0.8);
		
		//Initiate mainScene
		mainApplicationPanel = new AnchorPane();
		Scene mainScene = new Scene(mainApplicationPanel,mainSceneScreenDimentions.getX(),mainSceneScreenDimentions.getY());
		mainScene.setFill(Constants.MAIN_SCENE_BACKGROUND_COLOR);
		//Add 3d sub scene to main scene
		game3dSubScene = new Game3DSubSceneHolder(modelManagerInstance,mainSceneScreenDimentions.getX(), mainSceneScreenDimentions.getY());
		mainApplicationPanel.getChildren().add(game3dSubScene.getSubScene());

		//add information panel at bottom (player name,level,time left,score)
		bottomInfoPanel = new BottomInfoPanel((int) mainSceneScreenDimentions.getX());
		AnchorPane.setBottomAnchor(bottomInfoPanel, 20.0);
		
		
		//start game panel
		startGamePanel = new StartGamePanel((int) mainSceneScreenDimentions.getX(), (int) mainSceneScreenDimentions.getY());
		
		//choose game difficulty level panel
		gameDifficultyPanel = new GameDiffuculyChoosePanel((int) mainSceneScreenDimentions.getX(), (int) mainSceneScreenDimentions.getY());
		
		//Register Panel
		registerPanel = new RegisterPanel((int) mainSceneScreenDimentions.getX(), (int) mainSceneScreenDimentions.getY());
		
		
		//Login Panel
		loginPanel = new LoginPanel((int) mainSceneScreenDimentions.getX(), (int) mainSceneScreenDimentions.getY());
		mainApplicationPanel.getChildren().add(loginPanel);

		//GameOver Panel
		gameOverPanel = new GameOverPanel((int) mainSceneScreenDimentions.getX(), (int) mainSceneScreenDimentions.getY());
				
		//back to start game button
		backToStartGameBTContainer = new VBox();
		backToStartGameBTContainer.setPadding(new Insets(mainSceneScreenDimentions.getY()*0.03,0,0,0));
		backToStartGamePanelBT = new MyImageButton(BUTTON_IDS.BACK_TO_START_GAME_BTN, Constants.BACK_TO_START_GAME_BTN_IMG, 174, 105, mainSceneScreenDimentions.getX()*0.12, mainSceneScreenDimentions.getY()*0.07);
		backToStartGameBTContainer.getChildren().add(backToStartGamePanelBT);
		AnchorPane.setRightAnchor(backToStartGameBTContainer,mainSceneScreenDimentions.getY()*0.03);
						
		Image appIcon = new Image(ViewManager.class.getResource("resources/"+Constants.APP_ICON).toExternalForm(),225,225,true,true);
        stage.getIcons().add(appIcon);
		stage.setTitle("Fire Space");
		stage.setAlwaysOnTop(true);
		stage.setScene(mainScene);
		stage.show();
	}
	
 	public Stage getStage() {
		return stage;
	}
 	
 	
 	public void stopAnimationThreads() {
		game3dSubScene.stopAnimationThreads();
	}
 	
 	public void registerEvents(EventHandler<MouseEvent> mouseClick2DButtons,EventHandler<MouseEvent> mouseClick3DFireCannon,EventHandler<MouseEvent> mouseMove3DScene){
		game3dSubScene.registerEvents(mouseClick3DFireCannon, mouseMove3DScene);
 		backToStartGamePanelBT.setOnMousePressed(mouseClick2DButtons);
		gameDifficultyPanel.setEventHandler(mouseClick2DButtons);
		loginPanel.setEventHandler(mouseClick2DButtons);
		registerPanel.setEventHandler(mouseClick2DButtons);
		startGamePanel.setEventHandler(mouseClick2DButtons);
 	}
 	
 	public void setCannon(Cannon cannon){
 		game3dSubScene.setCannon(cannon);
 	}
 	
  	public GameOverPanel getGameOverPanel() {
		return gameOverPanel;
	}
 	
 	public void onGameOver(String message){
 		hideBackButton();
 		getGameOverPanel().showMessage(message);
		showGameOverPanel();
		showBackButton();
 	}
  	
 	public void showBackButton(){
		mainApplicationPanel.getChildren().add(backToStartGameBTContainer);
 	}
 	
 	public void hideBackButton(){
		mainApplicationPanel.getChildren().remove(backToStartGameBTContainer);
 	}
 	
 	public void hideStartGamePanel(){
		mainApplicationPanel.getChildren().remove(startGamePanel);
	}
 	
 	public void showGameOverPanel(){
		mainApplicationPanel.getChildren().add(gameOverPanel);
	}
 	
 	public void hideGameOverPanel(){
		mainApplicationPanel.getChildren().remove(gameOverPanel);
	}
 	
 	public void showStartGamePanel(){
		mainApplicationPanel.getChildren().add(startGamePanel);
	}
 	
 	public void hideDifficultyGamePanel(){
		mainApplicationPanel.getChildren().remove(gameDifficultyPanel);
	}
 	
 	public void showDifficultyGamePanel(){
		mainApplicationPanel.getChildren().add(gameDifficultyPanel);
	}
 	
 	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
 	
 	public void hideRegisterGamePanel(){
		mainApplicationPanel.getChildren().remove(registerPanel);
	}
 	
 	public void showRegisterGamePanel(){
		mainApplicationPanel.getChildren().add(registerPanel);
	}
 	
 	public void hideLoginGamePanel(){
		mainApplicationPanel.getChildren().remove(loginPanel);
	}
 	
 	public void showLoginGamePanel(){
		mainApplicationPanel.getChildren().add(loginPanel);
	}
 	
 	public void showChooseGameDifficultyPanel(){
 		hideBackButton();
 		hideStartGamePanel();
 		showDifficultyGamePanel();
 		showBackButton();
 	}
	
	public boolean isAnimatingCannon() {
		return game3dSubScene.isAnimatingCannon();
	}
	
	public void onBackToStartGamePanel(){
		game3dSubScene.startRotateCannonAnimation();
		game3dSubScene.restAsteroidsGeneration();
		game3dSubScene.startAsteroidsGnerationThread(GAME_TYPE.TRAINING);
		game3dSubScene.animateCamera(-1);
		hideInfoBottomPanel();
		hideBackButton();
		hideGameOverPanel();
		showStartGamePanel();
		showBackButton();
	}
	
	public void onGameStart(GAME_TYPE gameType,Player player){

		game3dSubScene.setAnimatingCannon(true);
		game3dSubScene.stopCanonRotateAnimation();
		game3dSubScene.restAsteroidsGeneration();
		game3dSubScene.animateCamera(1);
		startAsteroidsGnerationThread(gameType);		
		
		switch (gameType) {
		
		case TRAINING:
			showInfoBottomPanel(GAME_TYPE.TRAINING);
			hideStartGamePanel();
			break;
		default:
			showInfoBottomPanel(GAME_TYPE.EASY);
			hideDifficultyGamePanel();
			break;

		}
		
		bottomInfoPanel.setPlayerName(player.getName());
		bottomInfoPanel.setScore(0);
	}
 
	public void showInfoBottomPanel(GAME_TYPE ptType){
		
		switch (ptType) {
		case TRAINING:
			bottomInfoPanel.hideTime();
			bottomInfoPanel.hideScore();
			break;

		default:
			bottomInfoPanel.showTime();
			bottomInfoPanel.showScore();
			break;
		}
		
		mainApplicationPanel.getChildren().add(bottomInfoPanel);
	}
	
	public void hideInfoBottomPanel(){
		mainApplicationPanel.getChildren().remove(bottomInfoPanel);
	}
	
	private Rectangle2D getScreenSize(){
		return Screen.getPrimary().getVisualBounds();
	 }
	
	public Game3DSubSceneHolder getGame3dSubScene() {
		return game3dSubScene;
	}
	
	public void startAsteroidsGnerationThread(GAME_TYPE gameType){
		game3dSubScene.startAsteroidsGnerationThread(gameType);
	}
	
	public void restAsteroids(){
		game3dSubScene.restAsteroidsGeneration();
	}
 	
	public void setPlayerName(String playerName){
		bottomInfoPanel.setPlayerName(playerName);
	}
	
	public void setScore(int score){
		bottomInfoPanel.setScore(score);
	}
	
	public void setTime(String time){
		bottomInfoPanel.setTime(time);
	}
 
	public RegisterPanel getRegisterPanel() {
		return registerPanel;
	}
	
}
