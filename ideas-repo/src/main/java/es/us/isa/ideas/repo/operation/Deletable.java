package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface Deletion.
 */
public interface Deletable extends Operation {

	/**
	 * Delete.
	 *
	 * @return true, if successful
	 */
	public boolean delete() throws AuthenticationException;

	
}
