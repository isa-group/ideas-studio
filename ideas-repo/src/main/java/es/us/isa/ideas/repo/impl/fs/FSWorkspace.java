package es.us.isa.ideas.repo.impl.fs;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.Workspace;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.operation.Listable;

public class FSWorkspace extends Workspace {

	private static final Logger LOGGER = Logger.getLogger(FSWorkspace.class
			.getName());

	public FSWorkspace(String name, String owner) {
		super(name, owner);
	}

	@Override
	protected boolean moveImpl(Listable dest, boolean copy) {
		boolean result = false;
		try {
			File w1 = new File(IdeasRepo.get().getObjectFullUri(this));
			File w2 = new File(IdeasRepo.get().getObjectFullUri(dest));
			FileUtils.copyDirectory(w1, w2);
			if (!copy) {
				FileUtils.deleteDirectory(w1);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean deleteImpl() {
		boolean result = false;
		try {
			File ws = new File(IdeasRepo.get().getObjectFullUri(this));
			if (ws.exists()) {
				FileUtils.deleteDirectory(ws);
				result = true;
			} else {
				LOGGER.log(Level.INFO, "Workspace: " + ws.getAbsolutePath()
						+ " does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean persistImpl() {
		boolean result = false;
		try {
			checkOwnerFolder();
			// System.out.println("1###### " +
			// IdeasRepo.get().getObjectFullUri(this));
			// System.out.println("2###### " + (new
			// File(IdeasRepo.get().getObjectFullUri(this))).toURI());
			// System.out.println("3###### " + (new
			// File(IdeasRepo.get().getObjectFullUri(this))).toURI().getPath());
			File ws = new File(IdeasRepo.get().getObjectFullUri(this));
			if (!ws.exists()) {
				if (!ws.mkdir()) {
					LOGGER.log(
							Level.INFO,
							"Failed creating workspace: "
									+ ws.getAbsolutePath());
				} else {
					result = true;
				}
			} else {
				LOGGER.log(Level.INFO, "workspace already exists.");
			}
		} catch (ObjectClassNotValidException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void checkOwnerFolder() {
		File ownerFolder = new File(IdeasRepo.get().getRepoBaseUri()
				+ IdeasRepo.SEPARATOR + getOwner());
		if (!ownerFolder.exists()) {
			ownerFolder.mkdir();
		}
	}

	@Override
	protected FSNode listImpl() {
		FSNode parentNode = null;
		File parentDir;
		try {
			parentDir = new File(IdeasRepo.get().getObjectFullUri(this));
			if (parentDir.exists()) {
				parentNode = FSNodeBuilder.getFiles(parentDir, null);
			} else {
				LOGGER.log(Level.INFO, "Workspace not found.");
				// throw new ResourceNotFoundException();
			}
		} catch (ObjectClassNotValidException e) {
			e.printStackTrace();
		}
		return parentNode;
	}

}
