package es.us.isa.ideas.repo;

/**
 * The Interface UserHandler.
 */
public interface AuthenticationManagerDelegate {
	
	public enum AuthOpType {
		READ, WRITE, READWRITE;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getAuthenticatedUserId();
	
	/**
	 * Operation allowed.
	 *
	 * @param authenticatedUser the user
	 * @param Owner
	 * @param workspace the workspace
	 * @param project the project
	 * @param fileOrDirectoryUri the file or directory uri
	 * @param operationType the operation type
	 * @return true, if operation is allowed
	 */
	public boolean operationAllowed(String authenticatedUser, String Owner, String workspace, String project, String fileOrDirectoryUri, AuthOpType operationType);
	
}
