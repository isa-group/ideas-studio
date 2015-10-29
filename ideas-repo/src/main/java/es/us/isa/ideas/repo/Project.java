package es.us.isa.ideas.repo;

import es.us.isa.ideas.repo.AuthenticationManagerDelegate.AuthOpType;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSNode;
import es.us.isa.ideas.repo.operation.Creatable;
import es.us.isa.ideas.repo.operation.Deletable;
import es.us.isa.ideas.repo.operation.Listable;
import es.us.isa.ideas.repo.operation.Movable;

/**
 * The Class Project.
 */
public abstract class Project extends RepoElement implements Creatable,
		Deletable, Movable, Listable {

	/** The owner. */
	private String workspace;

	/**
	 * Instantiates a new project.
	 *
	 * @param name
	 *            the name
	 * @param workspace
	 *            the workspace
	 * @param owner
	 *            the owner
	 */
	public Project(String name, String workspace, String owner) {
		super(name, owner);
		this.workspace = workspace;
	}

	/**
	 * Gets the workspace.
	 *
	 * @return the workspace
	 */
	public String getWorkspace() {
		return workspace;
	}

	/**
	 * Sets the workspace.
	 *
	 * @param workspace
	 *            the new workspace
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	@Override
	public boolean move(Listable dest, boolean copy)
			throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, getName(), null, AuthOpType.WRITE)) {
			return moveImpl(dest, copy);
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	/**
	 * Move impl.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean moveImpl(Listable dest, boolean copy);

	@Override
	public boolean delete() throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, getName(), null, AuthOpType.WRITE)) {
			return deleteImpl();
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	/**
	 * Delete impl.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean deleteImpl();

	@Override
	public boolean persist() throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, getName(), null, AuthOpType.WRITE)) {
			return persistImpl();
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	/**
	 * Persist impl.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean persistImpl();

	@Override
	public FSNode list() throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, getName(), null, AuthOpType.READ)) {
			return listImpl();
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	protected abstract FSNode listImpl();

}
