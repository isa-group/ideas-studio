package es.us.isa.ideas.repo.impl.fs;

import static org.apache.commons.io.FilenameUtils.getExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.IdeasRepo;

public class FSNodeBuilder {

	private static String SEDL_EXTENSION = ".sedl";

	public static FSNode getFiles(final File parentDir, FSNode parentNode) {
		return getFiles(parentDir, parentNode, parentDir.getName());
	}

	public static FSNode getFiles(final File parentDir, FSNode parentNode,
			String path) {
		Map<File, FSNode> subDirs = new HashMap<File, FSNode>();
		List<File> subFiles = new ArrayList<File>();
		if (parentNode == null) {
			parentNode = new FSNode();
			parentNode.setTitle(parentDir.getName());
			parentNode.setFolder(true);
			parentNode.setIcon(FSNodeIcon.WORKSPACE);

		}
		try {
			File[] subFileArr = parentDir.listFiles();

			for (int i = 0; i < subFileArr.length; i++) {
				File s = subFileArr[i];
				if (s.isDirectory()) {
					FSNode subDirNode = new FSNode();
					subDirNode.setFolder(true);
					subDirNode.setTitle(s.getName());
					subDirNode.setKeyPath(constructFileKeyPath(s, path));
					parentNode.getChildren().add(subDirNode);
					if (parentNode.getIcon().equals(FSNodeIcon.WORKSPACE)) {
						subDirNode.setIcon(FSNodeIcon.PROJECT);
					} else {
						subDirNode.setIcon(FSNodeIcon.FOLDER);
					}
					subDirs.put(s, subDirNode);
				} else if (!s.getName().startsWith(".")) {
					subFiles.add(subFileArr[i]);
					FSNode subNode = new FSNode();
					if (s.getName().endsWith(SEDL_EXTENSION)) {
						subNode.setIcon(FSNodeIcon.SEDL_FILE);
					} else {
						subNode.setIcon(FSNodeIcon.iconFor(getExtension(s
								.getName())));
					}
					subNode.setTitle(s.getName());
					subNode.setFolder(false);
					subNode.setKeyPath(constructFileKeyPath(s, path));
					parentNode.getChildren().add(subNode);
				} else {
					if (s.getName().equals(".description")
							|| s.getName().equals(".description.txt")) {
						parentNode.setDescription(FileUtils.readFileToString(s)
								.replace("\n", "\\n"));
					}
				}

			}
			if (!subDirs.isEmpty()) {

				for (File subDir : subDirs.keySet()) {
					getFiles(subDir, subDirs.get(subDir),
							path + "/" + subDir.getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentNode;
	}

	public static String constructFileKeyPath(final File f, String path) {
		String res;
		String baseUri = IdeasRepo.get().getRepoBaseUri();
		if (baseUri.startsWith("."))
			baseUri = baseUri.substring(1);

		String absPath = f.getAbsolutePath();
		absPath = absPath.replace(System.getProperty("file.separator"), "/");
		if (absPath.contains(baseUri)) {
			String aux = absPath.substring(absPath.indexOf(baseUri)
					+ baseUri.length());

			res = aux.replace(aux.split("/")[0], "");
			res = res.replaceFirst("/[^/]+?/", "");

		} else {
			String aux = absPath.substring(absPath.indexOf(path));
			res = aux.replace(aux.split("/")[0], "");
			res = res.replaceFirst("/[^/]+?/", "");
		}

		return res;
	}

}
