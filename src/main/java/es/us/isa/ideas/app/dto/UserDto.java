package es.us.isa.ideas.app.dto;

import java.io.Serializable;
import java.util.Collection;

import es.us.isa.ideas.app.entities.Actor;
import es.us.isa.ideas.app.security.Authority;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 3018817178568296075L;
	private String name;
	private String email;
	private String username;

	private Collection<Authority> authorities;

	public UserDto() {
		super();
	}

	public UserDto(Actor actor) {
		super();
		name = actor.getName();
		email = actor.getEmail();
		username = actor.getUserAccount().getUsername();
		authorities = actor.getUserAccount().getAuthorities();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
