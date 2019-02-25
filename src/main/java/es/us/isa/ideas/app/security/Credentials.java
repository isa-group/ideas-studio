package es.us.isa.ideas.app.security;

import javax.validation.constraints.Size;

public class Credentials {

	// Constructors -----------------------------------------------------------

	public Credentials() {
		super();
	}

	// Attributes -------------------------------------------------------------

	private String username;
	private String password;

	@Size(min = 5, max = 32)
	public String getUsername() {
		return username;
	}

	public void setJ_username(String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
