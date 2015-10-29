package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface Readable.
 */
public interface Readable extends Operation {

	/**
	 * Read as string.
	 *
	 * @return the string
	 */
	public String readAsString() throws AuthenticationException;
	
	/**
	 * Read as bytes.
	 *
	 * @return the byte[]
	 */
	public byte[] readAsBytes() throws AuthenticationException;
	
}
