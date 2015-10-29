package es.us.isa.ideas.repo;

import es.us.isa.ideas.repo.AuthenticationManagerDelegate.AuthOpType;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSNode;
import es.us.isa.ideas.repo.operation.Creatable;
import es.us.isa.ideas.repo.operation.Deletable;
import es.us.isa.ideas.repo.operation.Listable;
import es.us.isa.ideas.repo.operation.Movable;

/**
 * The Class Directory.
 */
public abstract class Directory extends RepoElement implements Creatable,
		Deletable, Movable, Listable {

	/** The project. */
	private String workspace, project;

	/**
	 * Instantiates a new directory.
	 *
	 * @param name
	 *            the name
	 * @param workspace
	 *            the workspace
	 * @param project
	 *            the project
	 * @param owner
	 *            the owner
	 */
	public Directory(String name, String workspace, String project, String owner) {
		super(name, owner);
		this.workspace = workspace;
		this.project = project;
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

	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 *
	 * @param project
	 *            the new project
	 */
	public void setProject(String project) {
		this.project = project;
	}

	@Override
	public boolean move(Listable dest, boolean copy)
			throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, project, getName(), AuthOpType.WRITE)) {
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
				getOwner(), workspace, project, getName(), AuthOpType.WRITE)) {
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
				getOwner(), workspace, project, getName(), AuthOpType.WRITE)) {
			return persistImpl();
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	/**
	 * list impl.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean persistImpl();

	@Override
	public FSNode list() throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), workspace, project, getName(), AuthOpType.READ)) {
			return listImpl();
		} else {
			AuthenticationException e = new AuthenticationException();
			throw e;
		}
	}

	/**
	 * List impl.
	 *
	 * @return the list
	 */
	protected abstract FSNode listImpl();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result
				+ ((workspace == null) ? 0 : workspace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Directory other = (Directory) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (workspace == null) {
			if (other.workspace != null)
				return false;
		} else if (!workspace.equals(other.workspace))
			return false;
		return true;
	}

}
