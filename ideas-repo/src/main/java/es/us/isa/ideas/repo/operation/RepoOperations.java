package es.us.isa.ideas.repo.operation;

import es.us.isa.ideas.repo.Node;
import es.us.isa.ideas.repo.exception.AuthenticationException;

/**
 * The Interface RepoOperations.
 */
public interface RepoOperations extends Operation {

	/**
	 * Creates the.
	 *
	 * @param c the c
	 * @return true, if successful
	 * @throws AuthenticationException 
	 */
	public boolean create(Creatable c) throws AuthenticationException;
	
	/**
	 * Delete.
	 *
	 * @param d the d
	 * @return true, if successful
	 * @throws AuthenticationException 
	 */
	public boolean delete(Deletable d) throws AuthenticationException;
	
	/**
	 * Move.
	 *
	 * @param m the m
	 * @return true, if successful
	 * @throws AuthenticationException 
	 */
	public boolean move(Movable m, Listable dest, boolean copy) throws AuthenticationException;
	
	/**
	 * List.
	 *
	 * @param l the l
	 * @return the list
	 * @throws AuthenticationException 
	 */
	public Node list(Listable l) throws AuthenticationException;
	
	/**
	 * Read as string.
	 *
	 * @param r the r
	 * @return the string
	 * @throws AuthenticationException 
	 */
	public String readAsString(Readable r) throws AuthenticationException;
	
	/**
	 * Read as bytes.
	 *
	 * @param r the r
	 * @return the byte[]
	 * @throws AuthenticationException 
	 */
	public byte[] readAsBytes(Readable r) throws AuthenticationException;
	
	/**
	 * Write.
	 *
	 * @param w the w
	 * @return true, if successful
	 * @throws AuthenticationException 
	 */
	public boolean write(Writeable w, String content) throws AuthenticationException;
        
        public boolean write(Writeable w, byte[] content) throws AuthenticationException;
	
}
