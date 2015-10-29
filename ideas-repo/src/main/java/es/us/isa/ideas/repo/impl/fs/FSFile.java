package es.us.isa.ideas.repo.impl.fs;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.File;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.operation.Listable;

public class FSFile extends File {

	public FSFile(String name, String workspace, String project, String owner) {
		super(name, workspace, project, owner);
	}

	@Override
	protected boolean writeImpl(String content) {
		boolean res = false;
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			if (file.exists()) {
				FileUtils.writeStringToFile(file, content);
				res = true;
			} else {
				System.out.println("Cannot write to file: "
						+ file.getAbsolutePath() + " it does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public boolean write(byte[] content) throws AuthenticationException {
		boolean res = false;
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			if (file.exists()) {
				FileUtils.writeByteArrayToFile(file, content);
				res = true;
			} else {
				System.out.println("Cannot write to file: "
						+ file.getAbsolutePath() + " it does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected boolean moveImpl(Listable dest, boolean copy) {
		boolean res = false;
		try {
			java.io.File f1 = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			java.io.File f2 = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(dest));
			FileUtils.copyFileToDirectory(f1, f2);
			if (!copy) {
				if (!f1.delete()) {
					System.out.println("Cannot delete file: "
							+ f1.getAbsolutePath());
				} else {

				}
			}
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected boolean deleteImpl() {
		boolean result = false;
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			result = file.delete();

		} catch (ObjectClassNotValidException e) {
			System.out.println("Failed getting full path.");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean persistImpl() {
		boolean result = false;
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			try {
				result = file.createNewFile();
			} catch (IOException e) {
				System.out.println("Failed creating new file: "
						+ file.getAbsolutePath());
				e.printStackTrace();
			}
		} catch (ObjectClassNotValidException e) {
			System.out.println("Failed getting full path.");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected String readAsStringImpl() {
		String content = "";
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			content = FileUtils.readFileToString(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	protected byte[] readAsBytesImpl() {
		byte[] content = null;
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			content = FileUtils.readFileToByteArray(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}
