package es.us.isa.ideas.repo.impl.fs;

import java.io.File;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.Directory;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.operation.Listable;

public class FSDirectory extends Directory {

	public FSDirectory(String name, String workspace, String project,
			String owner) {
		super(name, workspace, project, owner);
	}

	@Override
	protected boolean moveImpl(Listable dest, boolean copy) {
		boolean result = false;
		try {
			File d1 = new File(IdeasRepo.get().getObjectFullUri(this));
			File d2 = new File(IdeasRepo.get().getObjectFullUri(dest));
			// Comprobar que el destino no esté dentro del origen, o hay bucle
			FileUtils.copyDirectory(d1, d2);
			if (!copy) {
				FileUtils.deleteDirectory(d1);
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
			File dir = new File(IdeasRepo.get().getObjectFullUri(this));
			if (dir.exists()) {
				FileUtils.deleteDirectory(dir);
				result = true;
			} else {
				System.out.println("Directory: " + dir.getAbsolutePath()
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
			File dir = new File(IdeasRepo.get().getObjectFullUri(this));
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					System.out.println("Failed creating directory: "
							+ dir.getAbsolutePath());
				} else {
					result = true;
				}
			} else {
				System.out.println("Directory already exists.");
			}
		} catch (ObjectClassNotValidException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * No puede ser así. Hay que crear un objeto intermedio para rellenar la
	 * lista, si se rellena de string no se sabe si es un directorio o un
	 * fichero.
	 */

	@Override
	protected FSNode listImpl() {
		// List<Node> filesList = new ArrayList<Node>();
		FSNode parentNode = null;
		// return filesList;
		return parentNode;
	}

}
