package es.us.isa.ideas.repo.impl.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.exception.BadUriException;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.operation.Listable;

public class FSFacade {

	private static String SEPARATOR = System.getProperty("file.separator");

	public static boolean createFile(String uri, String owner)
			throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(uri, owner);
		return IdeasRepo.get().getRepo().create(fsFile);
	}

	public static FSFile getFileFromUri(String uri, String owner)
			throws BadUriException {
		FSFile fsFile = null;
		String[] uriArray = splitURI(uri);
		if (uriArray.length < 3) {
			throw new BadUriException("Bad uri: " + uri);
		}
		String workspace = uriArray[0];
		String project = uriArray[1];
		String nameUri = "";
		for (int i = 2; i < uriArray.length; i++) {
			nameUri += uriArray[i];
			if (i < uriArray.length - 1) {
				nameUri += SEPARATOR;
			}
		}
		fsFile = new FSFile(nameUri, workspace, project, owner);
		return fsFile;
	}

	private static Listable getListableFromUri(String uri, String owner)
			throws BadUriException {
		Listable l = null;

		String[] uriArray = splitURI(uri);
		if (uriArray.length == 2) {
			l = getProjectFromUri(uri, owner);
		} else if (uriArray.length > 2) {
			l = getDirectoryFromUri(uri, owner);
		} else {
			throw new BadUriException();
		}
		return l;
	}

	private static FSProject getProjectFromUri(String uri, String owner)
			throws BadUriException {
		FSProject fsProject = null;
		String uriArray[] = splitURI(uri);
		if (uri.length() < 2) {
			throw new BadUriException(
					"Bad uri, it should contains at 1 separator.");
		}
		String ws = uriArray[0];
		String projectName = uriArray[1];
		fsProject = new FSProject(projectName, ws, owner);
		return fsProject;
	}

	private static FSDirectory getDirectoryFromUri(String uri, String owner)
			throws BadUriException {
		FSDirectory fsDir = null;
		String uriArray[] = splitURI(uri);
		if (uriArray.length < 3) {
			throw new BadUriException();
		}
		String ws = uriArray[0];
		String pr = uriArray[1];
		String dirName = "";
		for (int i = 2; i < uriArray.length; i++) {
			dirName += uriArray[i];
			if (i < uriArray.length) {
				dirName += SEPARATOR;
			}
		}
		fsDir = new FSDirectory(dirName, ws, pr, owner);
		return fsDir;
	}

	public static boolean createWorkspace(String wsName, String owner)
			throws AuthenticationException {
		FSWorkspace ws = new FSWorkspace(wsName, owner);
		return IdeasRepo.get().getRepo().create(ws);
	}

	public static String getWorkspaceTree(String wsName, String owner)
			throws AuthenticationException {
		FSWorkspace ws = new FSWorkspace(wsName, owner);
		FSNode wsNode = IdeasRepo.get().getRepo().list(ws);
		String resp = "[";
		for (int i = 0; i < wsNode.getChildren().size(); i++) {
			if (i != 0) {
				resp += ",";
			}
			FSNode fsChild = (FSNode) wsNode.getChildren().get(i);
			resp += fsChild.toString();
		}
		resp += "]";
		return resp;
	}

	public static String getProjectTree(String wsName, String owner,
			String project) throws AuthenticationException {
		FSWorkspace ws = new FSWorkspace(wsName, owner);
		FSNode wsNode = IdeasRepo.get().getRepo().list(ws);
		String result = "";
		for (int i = 0; i < wsNode.getChildren().size(); i++) {
			FSNode fsChild = (FSNode) wsNode.getChildren().get(i);
			if (fsChild.getTitle().equals(project))
				result = fsChild.toString();
		}
		return result;
	}

	public static String getWorkspaces(String owner)
			throws AuthenticationException {
		FSRepo fsRepo = (FSRepo) IdeasRepo.get().getRepo();
		return fsRepo.getWorkspaces(owner);
	}

	public static String getFileContent(String fileUri, String owner)
			throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		return IdeasRepo.get().getRepo().readAsString(fsFile);
	}

	public static byte[] getFileContentAsBytes(String fileUri, String owner)
			throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		return IdeasRepo.get().getRepo().readAsBytes(fsFile);
	}

	public static boolean setFileContent(String fileUri, String owner,
			String fileContent) throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		return IdeasRepo.get().getRepo().write(fsFile, fileContent);
	}

	public static boolean setFileContent(String fileUri, String owner,
			byte[] fileContent) throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		return IdeasRepo.get().getRepo().write(fsFile, fileContent);
	}

	public static boolean createProject(String projectUri, String owner)
			throws BadUriException, AuthenticationException {
		FSProject fsProject = getProjectFromUri(projectUri, owner);
		return IdeasRepo.get().getRepo().create(fsProject);
	}

	public static boolean createDirectory(String dirUri, String owner)
			throws AuthenticationException, BadUriException {
		FSDirectory fsDir = getDirectoryFromUri(dirUri, owner);
		return IdeasRepo.get().getRepo().create(fsDir);
	}

	public static boolean deleteFile(String fileUri, String owner)
			throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		return IdeasRepo.get().getRepo().delete(fsFile);
	}

	public static boolean deleteDirectory(String dirUri, String owner)
			throws AuthenticationException, BadUriException {
		FSDirectory fsDir = getDirectoryFromUri(dirUri, owner);
		return IdeasRepo.get().getRepo().delete(fsDir);
	}

	public static boolean deleteProject(String projUri, String owner)
			throws AuthenticationException, BadUriException {
		FSProject fsProj = getProjectFromUri(projUri, owner);
		return IdeasRepo.get().getRepo().delete(fsProj);
	}

	public static boolean deleteWorkspace(String dirWs, String owner)
			throws AuthenticationException, BadUriException {
		FSWorkspace fsWs = new FSWorkspace(dirWs, owner);
		return IdeasRepo.get().getRepo().delete(fsWs);
	}

	public static boolean moveFile(String fileUri, String owner,
			String destUri, boolean copy) throws BadUriException,
			AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, owner);
		Listable dest = getListableFromUri(destUri, owner);
		return IdeasRepo.get().getRepo().move(fsFile, dest, copy);
	}

	public static boolean moveDirectory(String dirUri, String owner,
			String destUri, boolean copy) throws BadUriException,
			AuthenticationException {
		FSDirectory fsDir = getDirectoryFromUri(dirUri, owner);
		Listable dest = getListableFromUri(destUri, owner);
		return IdeasRepo.get().getRepo().move(fsDir, dest, copy);
	}

	public static boolean renameFile(String fileUri, String owner,
			String newName) throws BadUriException, AuthenticationException {
		boolean res = false;
		try {
			FSFile fsFile = getFileFromUri(fileUri, owner);
			res = fsFile.rename(newName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String uri = new String(fileUri);
		// String[] splittedUri = uri.split("/");
		// String destUri = uri.replace("/" +
		// splittedUri[splittedUri.length-1],"");
		// Listable dest = getListableFromUri(destUri, owner);
		// fsFile.setName(newName);

		// return IdeasRepo.get().getRepo().move(fsFile, dest, false);
		return res;
	}

	public static boolean renameDirectory(String dirUri, String owner,
			String newName) throws BadUriException, AuthenticationException {
		boolean res = false;
		try {
			FSDirectory fsDir = getDirectoryFromUri(dirUri, owner);
			res = fsDir.rename(newName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// FSDirectory fsDir = getDirectoryFromUri(dirUri, owner);
		// String uri = new String(dirUri);
		// String[] splittedUri = uri.split("/");
		// String destUri = uri.replace("/" +
		// splittedUri[splittedUri.length-1],"");
		// Listable dest = getListableFromUri(destUri, owner);
		// fsDir.setName(newName);
		// return IdeasRepo.get().getRepo().move(fsDir, dest, false);
		return res;
	}

	public static void saveSelectedWorkspace(String workspaceName, String owner)
			throws IOException {
		if (IdeasRepo.get().getRepo() instanceof FSRepo) {
			FSRepo fsRep = (FSRepo) IdeasRepo.get().getRepo();
			fsRep.saveSelectedWorkspace(workspaceName, owner);
		}
	}

	public static String getSelectedWorkspace(String owner) throws IOException {
		String ret = "";
		if (IdeasRepo.get().getRepo() instanceof FSRepo) {
			FSRepo fsRep = (FSRepo) IdeasRepo.get().getRepo();
			ret = fsRep.getSelectedWorkspace(owner);
		}
		return ret;
	}

	private static String[] splitURI(String uri) {
		String splitter = "[/";
		if ("\\".equals(SEPARATOR))
			splitter += SEPARATOR + SEPARATOR + "]";
		else
			splitter += SEPARATOR + "]";
		String[] uriArray = uri.split(splitter);
		return uriArray;
	}

	public static void saveFileContentAsZip(String fileUri, String username,
			OutputStream os) throws BadUriException, AuthenticationException {
		FSFile fsFile = getFileFromUri(fileUri, username);
		fsFile.saveAsZip(os);
	}

	public static void saveProjectContentAsZip(String projectUri,
			String username, OutputStream os) throws BadUriException,
			AuthenticationException {
		FSProject fsProject = getProjectFromUri(projectUri, username);
		fsProject.saveAsZip(os);
	}

	public static void saveDirectoryContentAsZip(String directoriUri,
			String username, OutputStream os) throws BadUriException,
			AuthenticationException {
		FSDirectory fsDirectory = getDirectoryFromUri(directoriUri, username);
		fsDirectory.saveAsZip(os);
	}

	public static void saveWorkspaceContentAsZip(String wsName, String owner,
			OutputStream os) throws BadUriException, AuthenticationException {
		FSWorkspace ws = new FSWorkspace(wsName, owner);
		ws.saveAsZip(os);
	}

	public static void extractIn(String folderUri, String username, File f)
			throws BadUriException, ObjectClassNotValidException, IOException {
		FSProject fsProject = getProjectFromUri(folderUri, username);

		File folder = new File(IdeasRepo.get().getObjectFullUri(fsProject));
		if (!folder.exists()) {
			folder.mkdir();
		}

		try {
			ZipFile zipFile = new ZipFile(f);
			zipFile.extractAll(folder.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static void copyProjectInto(String workspace, String project,
			String owner, String dest) {
		FSProject pr = new FSProject(project, workspace, owner);

		try {
			String fullPathToProject = IdeasRepo.get().getObjectFullUri(pr);
			File p = new File(fullPathToProject);

			File destination = new File(dest);
			FileUtils.copyDirectory(p, destination);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TODO: up to now this is oriented to be used at R-Module, is not generic
	// yet, but will.
	public static void copytoProject(String workspace, String project,
			String owner, String source) {
		FSProject pr = new FSProject(project, workspace, owner);
		try {
			String fullPathToProject = IdeasRepo.get().getObjectFullUri(pr);
			File p = new File(fullPathToProject);
			File src = new File(source);

			Path output = p.toPath();
			Path tempOutput = src.toPath();
			output = output.resolve("R-OutputFolder");
			tempOutput = tempOutput.resolve("R-OutputFolder");
			File o = output.toFile();
			File to = tempOutput.toFile();
			if (to.exists()) {

				FileUtils.copyDirectory(to, o);
			} else {

				output = p.toPath();
				Calendar c = Calendar.getInstance();
				output = output.resolve("RUN-" + c.get(Calendar.YEAR) + "-"
						+ c.get(Calendar.MONTH) + 1 + "-"
						+ c.get(Calendar.DAY_OF_WEEK));
				Files.createDirectories(output);
				o = output.toFile();
				/* FileUtils.copyDirectory(src, o); */
				FileUtils.copyDirectory(to, o);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void extractInNew(String folderUri, String username, File f)
			throws BadUriException, ObjectClassNotValidException, IOException {

		String workpace = splitURI(folderUri)[0];
		// String zipName = splitURI(folderUri)[1];

		ZipInputStream is = null;
		OutputStream os = null;

		try {
			ZipFile zipFile = new ZipFile(f);

			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();

			for (FileHeader fh : fileHeaderList) {

				// String project = splitURI(fh.getFileName())[0];

				String filePath = workpace
						+ "/"
						+ fh.getFileName().substring(0,
								fh.getFileName().lastIndexOf('.'));

				FSProject fsProject = getProjectFromUri(filePath, username);

				File folder = new File(IdeasRepo.get().getObjectFullUri(
						fsProject));
				if (!folder.exists()) {
					folder.mkdirs();
				}

				String fileName = fh.getFileName().substring(
						fh.getFileName().indexOf('/'),
						fh.getFileName().length());

				File outputFile = new File(folder.getAbsolutePath() + fileName);

				File parentDir = outputFile.getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}

				// Get the InputStream from the ZipFile
				is = zipFile.getInputStream(fh);
				// Initialize the output stream
				os = new FileOutputStream(outputFile);

				IOUtils.copy(is, os);
				os.close();
				is.close();

				os = null;
				is = null;

				// To restore File attributes
				UnzipUtil.applyFileAttributes(fh, outputFile);
				System.out.println("Extracted: " + fh.getFileName());

			}

		} catch (ZipException e) {
			e.printStackTrace();
		}

	}

}
