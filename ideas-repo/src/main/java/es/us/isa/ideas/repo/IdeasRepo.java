package es.us.isa.ideas.repo;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;

/**
 * The Singleton Class RepoLab.
 */
public class IdeasRepo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3374144457119714166L;

	/** The separator. */
	public static String SEPARATOR = "/";

	/** The repo lab. */
	private static IdeasRepo ideasRepo = null;

	/** The base path. */
	private static String basePathToConfigFile = "";

	/** The repo impl. */
	private String repoImpl = "";

	private String repoPackage = "";

	/** The repo base uri. */
	private String repoBaseUri = "";

	/** The user handler. */
	private static AuthenticationManagerDelegate authManagerDelegate;

	/**
	 * Instantiates a new repo lab.
	 */
	public IdeasRepo() {
		super();

		Properties prop = new Properties();
		InputStream input = null;

		try {
			// Lo lee de {tomcat]/bin/, porque lo inicio desde ahí
			String propFile = "RepoLab.properties";

			if (basePathToConfigFile == null || "".equals(basePathToConfigFile))
				propFile = SEPARATOR + propFile;
			else
				propFile = basePathToConfigFile + SEPARATOR + propFile;

			input = IdeasRepo.class.getResourceAsStream(propFile);

			if (input != null) {
				prop.load(input);
				System.out.println("Leyendo propiedades... : "
						+ prop.getProperty("repo_base_uri"));
				this.setRepoBaseUri(prop.getProperty("repo_base_uri"));
				this.repoImpl = prop.getProperty("user_repo_impl");
				this.repoPackage = prop.getProperty("user_repo_impl_package");
			} else {
				System.out.println("Fallo en input repo");
				this.setRepoBaseUri("./ideas-repo/");
				this.repoImpl = "FSRepo";
				this.repoPackage = "es.us.isa.ideas.utils.repolab.impl.fs.";
			}
			createRepo();
		} catch (IOException ex) {
			System.out.println("No existe: " + basePathToConfigFile
					+ "RepoLab.properties\n");
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void init(AuthenticationManagerDelegate handler) {
		authManagerDelegate = handler;
	}

	private boolean createRepo() {
		boolean res = false;
		try {
			java.io.File repo = new java.io.File(this.getRepoBaseUri());
			res = repo.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Gets the singleton instance.
	 *
	 * @return the repo lab
	 */
	public static IdeasRepo get() {
		if (ideasRepo == null)
			ideasRepo = new IdeasRepo();
		return ideasRepo;
	}

	public static IdeasRepo get(String repoBasePath) {
		IdeasRepo repo = get();
		repo.setRepoBaseUri(repoBasePath);
		return repo;
	}

	/**
	 * Gets the repo base uri.
	 *
	 * @return the repo base uri
	 */
	public String getRepoBaseUri() {
		return repoBaseUri;
	}

	/**
	 * Sets the repo base uri.
	 *
	 * @param repoBaseUri
	 *            the new repo base uri
	 */
	protected void setRepoBaseUri(String repoBaseUri) {
		if (!repoBaseUri.endsWith(SEPARATOR)) {
			repoBaseUri += SEPARATOR;
		}
		this.repoBaseUri = repoBaseUri;
	}

	/**
	 * Sets the path to config file.
	 *
	 * @param path
	 *            the new path to config file
	 */
	public static void setPathToConfigFile(String path) {
		if (path.endsWith(SEPARATOR))
			basePathToConfigFile = path;
		else
			basePathToConfigFile = path + SEPARATOR;
	}

	/**
	 * Gets the user repo.
	 *
	 * @param <T>
	 *            the generic type
	 * @return the user repo
	 */
	@SuppressWarnings("unchecked")
	public <T extends Repo> T getRepo() {
		T res = null;
		Class<? extends Repo> c;
		try {
			String repoClassName = this.repoPackage + this.repoImpl;
			c = (Class<? extends Repo>) Class.forName(repoClassName);
			Constructor<?> ctor = c.getConstructor();
			res = (T) ctor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Gets the auth manager delegate.
	 *
	 * @return the auth manager delegate
	 */
	public static AuthenticationManagerDelegate getAuthManagerDelegate() {
		return authManagerDelegate;
	}

	/**
	 * Sets the auth manager delegate.
	 *
	 * @param authManagerDelegate
	 *            the new auth manager delegate
	 */
	public static void setAuthManagerDelegate(
			AuthenticationManagerDelegate authManagerDelegate) {
		IdeasRepo.authManagerDelegate = authManagerDelegate;
	}

	// TODO Pasar este metodo a FSRepo

	public String getObjectFullUri(Object o)
			throws ObjectClassNotValidException {
		String fullPath = getRepoBaseUri() + SEPARATOR;

		if (o instanceof Workspace) {
			Workspace w = (Workspace) o;
			fullPath += w.getOwner() + SEPARATOR + w.getName() + SEPARATOR;
		} else if (o instanceof Project) {
			Project p = (Project) o;
			fullPath += p.getOwner() + SEPARATOR + p.getWorkspace() + SEPARATOR
					+ p.getName() + SEPARATOR;
		} else if (o instanceof Directory) {
			Directory d = (Directory) o;
			fullPath += d.getOwner() + SEPARATOR + d.getWorkspace() + SEPARATOR
					+ d.getProject() + SEPARATOR + d.getName() + SEPARATOR;
		} else if (o instanceof File) {
			File f = (File) o;
			fullPath += f.getOwner() + SEPARATOR + f.getWorkspace() + SEPARATOR
					+ f.getProject() + SEPARATOR + f.getName();
		} else {
			throw new ObjectClassNotValidException(
					"Object must be an instance of Workspace, Project, Directory or File.");
		}

		try {
			fullPath = java.net.URLDecoder.decode(fullPath, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(IdeasRepo.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return fullPath;
	}

}
