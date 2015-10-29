package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSNode;

/**
 * The Interface Listable.
 */
public interface Listable extends Operation {

	/**
	 * List.
	 *
	 * @return the list
	 */
	public FSNode list() throws AuthenticationException;
	
}
