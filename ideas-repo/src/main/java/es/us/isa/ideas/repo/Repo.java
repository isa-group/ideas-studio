package es.us.isa.ideas.repo;

import java.io.Serializable;

import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSNode;
import es.us.isa.ideas.repo.operation.Creatable;
import es.us.isa.ideas.repo.operation.Deletable;
import es.us.isa.ideas.repo.operation.Listable;
import es.us.isa.ideas.repo.operation.Movable;
import es.us.isa.ideas.repo.operation.Readable;
import es.us.isa.ideas.repo.operation.RepoOperations;
import es.us.isa.ideas.repo.operation.Writeable;

/**
 * The Class UserRepo.
 */
public abstract class Repo implements Serializable, RepoOperations {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7863233554392956747L;

	/**
	 * Gets the repo uri.
	 *
	 * @return the repo uri
	 */
	public abstract String getRepoUri();
	
	@Override
	public boolean create(Creatable c) throws AuthenticationException {
		return c.persist();
	}

	@Override
	public boolean delete(Deletable d) throws AuthenticationException {
		return d.delete();
	}

	@Override
	public boolean move(Movable m, Listable dest, boolean copy) throws AuthenticationException {
		return m.move( dest, copy );
	}

	@Override
	public FSNode list(Listable l) throws AuthenticationException {
		return l.list();
	}

	@Override
	public String readAsString(Readable r) throws AuthenticationException {
		return r.readAsString();
	}

	@Override
	public byte[] readAsBytes(Readable r) throws AuthenticationException {
		return r.readAsBytes();
	}

	@Override
	public boolean write(Writeable w, String content) throws AuthenticationException {
		return w.write( content );
	}
	
        @Override
	public boolean write(Writeable w, byte[] content) throws AuthenticationException {
		return w.write( content );
	}
    
	
}
