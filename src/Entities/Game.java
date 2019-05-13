package Entities;
import java.io.Serializable;
import java.util.Date;

import GameApplication.Constants.GAME_TYPE;

/**
 * This class used to store current game information .
 * @author Stanislav Bodik
 */

public class Game implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date startTime;
	private int score;
	private GAME_TYPE gameType;
	private Player player;
	
	public Game(Date startTime, GAME_TYPE gameType, Player player) {
		super();
		this.startTime = startTime;
		this.gameType = gameType;
		this.player = player;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public GAME_TYPE getGameType() {
		return gameType;
	}
	
	public void setGameType(GAME_TYPE gameType) {
		this.gameType = gameType;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
