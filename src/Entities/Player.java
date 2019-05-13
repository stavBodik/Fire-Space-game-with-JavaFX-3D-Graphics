package Entities;

import java.io.Serializable;

/**
 * This class used to store current playing player information.
 * @author Stanislav Bodik
 */

public class Player implements Serializable{
	private static final long serialVersionUID = 1L;
	private String email;
	private String name;
	private String password; // hashed with MD5 .

	@Override
	public String toString() {
		return "Player [email=" + email + ", name=" + name + ", password=" + password + "]";
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Player(String email, String name,String password) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
