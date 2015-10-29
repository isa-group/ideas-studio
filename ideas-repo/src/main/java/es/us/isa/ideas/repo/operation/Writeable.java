package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface Writeable.
 */
public interface Writeable extends Operation {

	/**
	 * Write.
	 *
	 * @return true, if successful
	 */
	public boolean write( String content ) throws AuthenticationException;
	public boolean write( byte[] content ) throws AuthenticationException;
}
