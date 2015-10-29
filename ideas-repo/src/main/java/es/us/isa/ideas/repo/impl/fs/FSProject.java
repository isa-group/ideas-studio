package es.us.isa.ideas.repo.impl.fs;

import java.io.File;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.Project;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.operation.Listable;

public class FSProject extends Project {

	public FSProject(String name, String workspace, String owner) {
		super(name, workspace, owner);
	}

	@Override
	protected boolean moveImpl(Listable dest, boolean copy) {
		boolean result = false;
		try {
			File p1 = new File(IdeasRepo.get().getObjectFullUri(this));
			File p2 = new File(IdeasRepo.get().getObjectFullUri(dest));
			FileUtils.copyDirectory(p1, p2);
			if (!copy) {
				FileUtils.deleteDirectory(p1);
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
			File proy = new File(IdeasRepo.get().getObjectFullUri(this));
			if (proy.exists()) {
				result = true;
				FileUtils.deleteDirectory(proy);
			} else {
				System.out.println("Project: " + proy.getAbsolutePath()
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
			File proy = new File(IdeasRepo.get().getObjectFullUri(this));
			if (!proy.exists()) {
				if (!proy.mkdir()) {
					System.out.println("Failed creating project: "
							+ proy.getAbsolutePath());
				} else {
					result = true;
				}
			} else {
				System.out.println("Project already exists.");
			}
		} catch (ObjectClassNotValidException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected FSNode listImpl() {

		return null;

	}

}
