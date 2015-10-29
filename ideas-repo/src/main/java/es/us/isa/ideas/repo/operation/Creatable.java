package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface Creation.
 */
public interface Creatable extends Operation {
	
	/**
	 * Persist.
	 *
	 * @return true, if successful
	 */
	public boolean persist() throws AuthenticationException;

}
