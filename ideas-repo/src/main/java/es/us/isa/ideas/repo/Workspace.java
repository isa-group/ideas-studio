package es.us.isa.ideas.repo;

import es.us.isa.ideas.repo.AuthenticationManagerDelegate.AuthOpType;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSNode;
import es.us.isa.ideas.repo.operation.Creatable;
import es.us.isa.ideas.repo.operation.Deletable;
import es.us.isa.ideas.repo.operation.Listable;
import es.us.isa.ideas.repo.operation.Movable;

/**
 * The Class Workspace.
 */
public abstract class Workspace extends RepoElement implements Creatable,
		Deletable, Movable, Listable {

	/**
	 * Instantiates a new workspace.
	 *
	 * @param name
	 *            the name
	 * @param owner
	 *            the owner
	 */
	public Workspace(String name, String owner) {
		super(name, owner);
	}

	@Override
	public boolean move(Listable dest, boolean copy)
			throws AuthenticationException {
		if (IdeasRepo.getAuthManagerDelegate().operationAllowed(
				IdeasRepo.getAuthManagerDelegate().getAuthenticatedUserId(),
				getOwner(), getName(), null, null, AuthOpType.WRITE)) {
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
				getOwner(), getName(), null, null, AuthOpType.WRITE)) {
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
				getOwner(), getName(), null, null, AuthOpType.WRITE)) {
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
				getOwner(), getName(), null, null, AuthOpType.READ)) {
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

}
