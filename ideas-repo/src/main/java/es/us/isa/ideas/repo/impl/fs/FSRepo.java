package es.us.isa.ideas.repo.impl.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.Repo;
import es.us.isa.ideas.repo.RepoElement;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.operation.Creatable;
import es.us.isa.ideas.repo.operation.Listable;

/**
 * The Class FSRepo.
 */
public class FSRepo extends Repo {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1951012080099899922L;

	/** The separator. */
	protected String SEPARATOR = System.getProperty("file.separator");

	/**
	 * Instantiates a new fS repo.
	 * 
	 * @param baseUserUri
	 *            the base user uri
	 */
	public FSRepo() {
		super();
	}

	public boolean create(Creatable c, Listable parent)
			throws AuthenticationException {
		if (c instanceof RepoElement && parent instanceof RepoElement) {
			RepoElement rc = (RepoElement) c;
			RepoElement rp = (RepoElement) parent;
			rc.setName(rp.getName() + SEPARATOR + rc.getName());
		}
		return create(c);
	}

	@Override
	public String getRepoUri() {
		String res = IdeasRepo.get().getRepoBaseUri();
		if (!res.endsWith(SEPARATOR))
			res += SEPARATOR;

		return res;
	}

	public String getWorkspaces(String owner) {
		File f = new File(getRepoUri() + owner);
		String[] wsList = f.list();
		List<String> workspaces = new ArrayList<String>();
		if (wsList != null) {
			for (String wsName : wsList) {
				if (!wsName.startsWith(".")) {
					String ws = "{\"name\": \"" + wsName + "\"}";
					workspaces.add(ws);
				}
			}
		}
		return workspaces.toString();
	}

	public void saveSelectedWorkspace(String wsName, String owner)
			throws IOException {
		String PREF_FILE_NAME = ".history";
		String prefPath = IdeasRepo.get().getRepoBaseUri()
				+ IdeasRepo.SEPARATOR + owner + IdeasRepo.SEPARATOR
				+ PREF_FILE_NAME;
		File prefFile = new File(prefPath);
		if (!prefFile.exists()) {
			prefFile.createNewFile();
		}
		Date now = new Date();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder sb = new StringBuilder();
		sb.append(FileUtils.readFileToString(prefFile) + "\n");
		sb.append(dft.format(now));
		sb.append("\t" + wsName);

		FileUtils.writeStringToFile(prefFile, sb.toString());
	}

	public String getSelectedWorkspace(String owner) throws IOException {
		String PREF_FILE_NAME = ".history";
		String prefPath = IdeasRepo.get().getRepoBaseUri() /*
															 * +
															 * IdeasRepo.SEPARATOR
															 */
				+ owner + IdeasRepo.SEPARATOR + PREF_FILE_NAME;
		File prefFile = new File(prefPath);
		if (!prefFile.exists()) {
			prefFile.createNewFile();
		}

		String lastLine = "";
		String sCurrentLine = "";
		InputStream is = new FileInputStream(prefFile);
		Reader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		while ((sCurrentLine = br.readLine()) != null) {
			lastLine = sCurrentLine;
		}
		br.close();

		String[] aux = lastLine.split("\t");
		String ws = aux[1];

		/*
		 * TODO Temp. properties
		 * 
		 * String d = aux[0]; SimpleDateFormat dft = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); try { Date date =
		 * dft.parse(d); } catch (ParseException e) { e.printStackTrace(); }
		 */

		return ws;
	}

}
