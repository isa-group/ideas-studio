package es.us.isa.ideas.repo;

import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.impl.fs.FSFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public abstract class RepoElement {
	private static final Logger LOG = Logger.getLogger(RepoElement.class
			.getName());

	private String name, owner;

	public RepoElement(String name, String owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean rename(String newName) {
		boolean res = false;
		try {
			java.io.File f = new java.io.File(IdeasRepo.get().getObjectFullUri(
					this));
			String uri = new String(IdeasRepo.get().getObjectFullUri(this));
			uri = uri.replace(System.getProperty("file.separator"), "/");
			String[] splittedUri = uri.split("/");
			String destUri = uri.replace("/"
					+ splittedUri[splittedUri.length - 1], "");
			java.io.File newFile = new java.io.File(destUri + "/" + newName);
			res = f.renameTo(newFile);
		} catch (ObjectClassNotValidException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepoElement other = (RepoElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	public void saveAsZip(OutputStream os) {
		ZipOutputStream zos = new ZipOutputStream(os);
		try {
			java.io.File file = new java.io.File(IdeasRepo.get()
					.getObjectFullUri(this));
			LOG.info("saving as zip file: '" + file.getAbsolutePath() + "'.");
			if (file.exists()) {
				if (file.isDirectory()) {
					saveDirectoryAsZip(zos, file);
				} else {
					safeFileAsZip(zos, file, file.getName());
				}
			}
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ObjectClassNotValidException ex) {
			Logger.getLogger(FSFile.class.getName())
					.log(Level.SEVERE, null, ex);
		}
	}

	private void safeFileAsZip(ZipOutputStream zos, java.io.File file,
			String name) throws IOException {
		ZipEntry ze = new ZipEntry(name);
		zos.putNextEntry(ze);
		FileUtils.copyFile(file, zos);
		zos.closeEntry();
	}

	private void saveDirectoryAsZip(ZipOutputStream zos, File file)
			throws IOException {
		List<String> files = new ArrayList<String>();
		generateFileList(file, files, file.getAbsolutePath());
		for (String entry : files) {
			safeFileAsZip(zos, new File(file.getAbsolutePath() + "/" + entry),
					entry);
		}
	}

	private void generateFileList(File node, List<String> list,
			String originalFile) {
		if (node.isFile()) {
			list.add(node.getAbsoluteFile().toString()
					.substring(originalFile.length() + 1));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), list, originalFile);
			}
		}

	}
}
