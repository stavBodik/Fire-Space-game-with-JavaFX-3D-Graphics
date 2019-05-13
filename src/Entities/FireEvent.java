package Entities;

import java.io.Serializable;
import java.util.Date;

/**
 * This class used to hold game fire event,
 * when ever user fires from canon information of firing saved : time,date,hit.
 * @author Stanislav Bodik
 */

public class FireEvent implements Serializable{
	private static final long serialVersionUID = 1L;
	private Player player;
	private boolean isHidted;
	private Date date;
	private Game game;
	
	public FireEvent(Player player, boolean isHidted, Date date,Game game) {
		super();
		this.player = player;
		this.isHidted = isHidted;
		this.date = date;
		this.game=game;
	}

	public Game getGame() {
		return game;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isHidted() {
		return isHidted;
	}

	public void setHidted(boolean isHidted) {
		this.isHidted = isHidted;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result + (isHidted ? 1231 : 1237);
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FireEvent other = (FireEvent) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (isHidted != other.isHidted)
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
}
