package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface Movable.
 */
public interface Movable extends Operation {

	/**
	 * Move.
	 *
	 * @return true, if successful
	 */
	public boolean move(Listable dest, boolean copy) throws AuthenticationException;
	
}
