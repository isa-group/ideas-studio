package es.us.isa.ideas.app.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import es.us.isa.ideas.app.security.LoginService;
import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.util.FileMetadata;
import es.us.isa.ideas.repo.AuthenticationManagerDelegate;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.exception.BadUriException;
import es.us.isa.ideas.repo.exception.ObjectClassNotValidException;
import es.us.isa.ideas.repo.impl.fs.FSFacade;
import es.us.isa.ideas.repo.impl.fs.FSWorkspace;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(FileController.class
			.getName());

	private static IdeasRepo ideasRepo = null;

	// Cambiar por LoginService.getPrincipal().getUsername()
	public static void initRepoLab() {
		if (FileController.ideasRepo == null) {
			IdeasRepo.init(new AuthenticationManagerDelegate() {

				@Override
				public boolean operationAllowed(String authenticatedUser,
						String Owner, String workspace, String project,
						String fileOrDirectoryUri, AuthOpType operationType) {
					return true;
				}

				@Override
				public String getAuthenticatedUserId() {
					// return LoginService.getPrincipal().getUsername();
					if (SecurityContextHolder.getContext().getAuthentication() == null
							|| !(SecurityContextHolder.getContext()
									.getAuthentication().getPrincipal() instanceof UserAccount)) {
						return "";
					} else {
						return LoginService.getPrincipal().getUsername();
					}
				}
			});

			ideasRepo = IdeasRepo.get();
		}
	}

	@RequestMapping(value = "/getWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkspace(
			@RequestParam("workspaceName") String workaspaceName) {
		LOGGER.log(Level.INFO, "Reading workspace: " + workaspaceName);
		initRepoLab();
		String wsJson = "";
		try {
			wsJson = FSFacade.getWorkspaceTree(workaspaceName, LoginService
					.getPrincipal().getUsername());
		} catch (AuthenticationException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return wsJson;
	}

	@RequestMapping(value = "/getWorkspaces", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkspaces() {
		initRepoLab();
		String ws = "";
		try {
			ws = FSFacade.getWorkspaces(LoginService.getPrincipal()
					.getUsername());
		} catch (AuthenticationException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return ws;
	}

	@RequestMapping(value = "/getFileContent", method = RequestMethod.GET)
	@ResponseBody
	public String getFileContent(@RequestParam("fileUri") String fileUri) {
		initRepoLab();
		String fileContent = "";
		try {
			fileContent = FSFacade.getFileContent(fileUri, LoginService
					.getPrincipal().getUsername());
		} catch (BadUriException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (AuthenticationException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return fileContent;
	}

	@RequestMapping(value = "/createWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public boolean createWorkspace(
			@RequestParam("workspaceName") String workspaceName) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.createWorkspace(workspaceName, LoginService
					.getPrincipal().getUsername());

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/createFile", method = RequestMethod.GET)
	@ResponseBody
	public boolean createFile(@RequestParam("fileUri") String fileUri) {
		initRepoLab();

		boolean res = false;
		try {
			res = FSFacade.createFile(fileUri, LoginService.getPrincipal()
					.getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/createDirectory", method = RequestMethod.GET)
	@ResponseBody
	// return bool? string?
	public String createDirectory(
			@RequestParam("directoryUri") String directoryUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.createDirectory(directoryUri, LoginService
					.getPrincipal().getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error creating directory: "
					+ directoryUri);
		}
		return String.valueOf(res);
	}

	@RequestMapping(value = "/createProject", method = RequestMethod.GET)
	@ResponseBody
	public String createProject(@RequestParam("projectUri") String projectUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.createProject(projectUri, LoginService
					.getPrincipal().getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error creating project: "
					+ projectUri);
		}
		return String.valueOf(res);
	}

	@RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteFile(@RequestParam("fileUri") String fileUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.deleteFile(fileUri, LoginService.getPrincipal()
					.getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/deleteDirectory", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteDirectory(@RequestParam("directoryUri") String dirUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.deleteDirectory(dirUri, LoginService.getPrincipal()
					.getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/deleteProject", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteProject(@RequestParam("projectUri") String projectUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.deleteProject(projectUri, LoginService
					.getPrincipal().getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/deleteWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteWorkspace(
			@RequestParam("workspaceUri") String workspaceUri) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.deleteWorkspace(workspaceUri, LoginService
					.getPrincipal().getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/setFileContent", method = RequestMethod.POST)
	@ResponseBody
	public boolean setFileContent(@RequestParam("fileUri") String fileUri,
			@RequestParam("fileContent") String fileContent) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.setFileContent(fileUri, LoginService.getPrincipal()
					.getUsername(), fileContent);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/moveFile", method = RequestMethod.GET)
	@ResponseBody
	public boolean moveFile(@RequestParam("fileUri") String fileUri,
			@RequestParam("destUri") String destUri,
			@RequestParam("copy") boolean copy) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.moveFile(fileUri, LoginService.getPrincipal()
					.getUsername(), destUri, copy);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/moveDirectory", method = RequestMethod.GET)
	@ResponseBody
	public boolean moveDirectory(@RequestParam("directoryUri") String dirUri,
			@RequestParam("destUri") String destUri,
			@RequestParam("copy") boolean copy) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.moveDirectory(dirUri, LoginService.getPrincipal()
					.getUsername(), destUri, copy);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/renameFile", method = RequestMethod.GET)
	@ResponseBody
	public boolean renameFile(@RequestParam("fileUri") String fileUri,
			@RequestParam("newName") String newName) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.renameFile(fileUri, LoginService.getPrincipal()
					.getUsername(), newName);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/renameDirectory", method = RequestMethod.GET)
	@ResponseBody
	public boolean renameDirectory(@RequestParam("directoryUri") String dirUri,
			@RequestParam("newName") String newName) {
		initRepoLab();
		boolean res = false;
		try {
			res = FSFacade.renameDirectory(dirUri, LoginService.getPrincipal()
					.getUsername(), newName);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/saveSelectedWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public boolean saveSelectedWorkspace(
			@RequestParam("workspaceName") String workspaceName) {
		initRepoLab();
		LOGGER.log(Level.INFO, "Persisting selected workspace:  "
				+ workspaceName + ", username: "
				+ LoginService.getPrincipal().getUsername());
		boolean res = true;
		try {
			FSFacade.saveSelectedWorkspace(workspaceName, LoginService
					.getPrincipal().getUsername());
		} catch (Exception e) {
			res = false;
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/getSelectedWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public String getSelectedWorkspace() {
		initRepoLab();
		String res = "";
		try {
			res = FSFacade.getSelectedWorkspace(LoginService.getPrincipal()
					.getUsername());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/cloneSelectedWorkspaceToDemo", method = RequestMethod.GET)
	@ResponseBody
	public String cloneSelectedWorkspaceToDemo(
			@RequestParam("workspaceName") String wsName) {
		initRepoLab();
		String res = "";

		String username = LoginService.getPrincipal().getUsername();
		FSWorkspace userWS = new FSWorkspace(wsName, username);

		FSWorkspace demoWS = new FSWorkspace(wsName, "DemoMaster");

		boolean demoExists = true;
		try {
			demoExists = FSFacade.getWorkspaces("DemoMaster").contains(
					"\"" + wsName + "\"");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Error while checking if DemoMaster contains the workspace \""
							+ wsName + "\"");
			demoExists = false;
		}
		if (!demoExists) {
			try {
				IdeasRepo.get().getRepo().move(userWS, demoWS, true);
			} catch (AuthenticationException e) {
				LOGGER.log(Level.SEVERE,
						"Error creating workspace for DemoMaster from "
								+ username);
				res = "[ERROR] Error creating WS for DemoMaster from "
						+ username;
			}
		} else {
			LOGGER.log(Level.WARNING, "The workspace \"" + wsName
					+ "\" already exists");
			res = "[INFO] The ws already exists.";
		}

		return res;
	}

	@RequestMapping(value = "/importDemoWorkspace", method = RequestMethod.GET)
	@ResponseBody
	public String importDemoWorkspace(
			@RequestParam("demoWorkspaceName") String demoWorkspaceName,
			@RequestParam("targetWorkspaceName") String targetWorkspaceName) {
		String response = "";

		String username = LoginService.getPrincipal().getUsername();

		FSWorkspace demoWS = new FSWorkspace(demoWorkspaceName, "DemoMaster");
		FSWorkspace newWS = new FSWorkspace(targetWorkspaceName, username);

		FileController.initRepoLab();

		boolean demoExists = true;

		// TODO: Cambiar comprobacion una vez este refactorizado. No se deberia
		// trabajar a nivel de cadenas, sino de objetos serializables (para
		// devolver JSON)
		try {
			demoExists = FSFacade.getWorkspaces("DemoMaster").contains(
					"\"" + demoWorkspaceName + "\"");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			demoExists = false;
		}

		if (demoExists) {

			try {
				IdeasRepo.get().getRepo().delete(newWS);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Could not remove workspace before importing.");
			}

			try {
				IdeasRepo.get().getRepo().move(demoWS, newWS, true);
				response = "Demo workspace created with name: "
						+ targetWorkspaceName;
			} catch (AuthenticationException e) {
				LOGGER.log(Level.SEVERE, "Error creating demo workspace for "
						+ username);
				response = e.getMessage();
			}

		} else {
			LOGGER.log(Level.WARNING, "There is no demo named \""
					+ demoWorkspaceName + "\"");
			response = "There is no Demo named " + demoWorkspaceName;
		}

		return response;

	}

	@RequestMapping(value = "/upload/**", method = RequestMethod.POST)
	public @ResponseBody Collection<FileMetadata> upload(
			MultipartHttpServletRequest request, HttpServletResponse response) {
		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		String pathUrl = request.getRequestURI().substring(
				request.getRequestURI().indexOf("file/upload/") + 12);
		MultipartFile mpf = null;
		FileMetadata fileMeta = null;
		String fileUri = null;
		List<FileMetadata> files = new ArrayList<FileMetadata>();
		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());
			LOGGER.log(Level.INFO, "File " + mpf.getOriginalFilename()
					+ " uploaded successfully");

			// 2.3 create new fileMeta
			fileMeta = new FileMetadata();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());
				fileUri = getSelectedWorkspace() + "/" + pathUrl + "/"
						+ fileMeta.getFileName();
				FSFacade.createFile(fileUri, LoginService.getPrincipal()
						.getUsername());
				FSFacade.setFileContent(fileUri, LoginService.getPrincipal()
						.getUsername(), mpf.getBytes());
				files.add(fileMeta);

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			} catch (BadUriException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage());
			} catch (AuthenticationException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage());
			}

		}

		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;

	}

	@RequestMapping(value = "/uploadAndExtract/**", method = RequestMethod.POST)
	public ModelAndView uploadAndExtract(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		Iterator<String> itr = request.getFileNames();
		// String pathUrl = request.getRequestURI().substring(
		// request.getRequestURI().indexOf("uploadAndExtract/")
		// + "uploadAndExtract/".length());
		MultipartFile mpf = null;
		// FileMetadata fileMeta = null;
		// String fileUri = null;
		// List<FileMetadata> files = new ArrayList<FileMetadata>();
		while (itr.hasNext()) {

			mpf = request.getFile(itr.next());

			try {
				if (mpf.getOriginalFilename().endsWith(".zip")
						|| mpf.getOriginalFilename().endsWith(".gz"))
					extractInWorkspace(getSelectedWorkspace(), mpf);
				else
					saveFile(getSelectedWorkspace(), "defaultProject", mpf);

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			} catch (BadUriException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage());
			} catch (AuthenticationException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage());
			}
		}
		return new ModelAndView("app/editor");

	}

	public void extractInWorkspace(String workspace, MultipartFile mpf)
			throws BadUriException, AuthenticationException, IOException {
		int extensionIndex = mpf.getOriginalFilename().lastIndexOf('.');
		if (extensionIndex == -1)
			extensionIndex = mpf.getOriginalFilename().length() - 1;
		String folderUri = workspace + "/"
				+ mpf.getOriginalFilename().substring(0, extensionIndex);
		File temp = File.createTempFile("uploadExtraction", "zipFiles");
		try {
			mpf.transferTo(temp);
			FSFacade.extractIn(folderUri, LoginService.getPrincipal()
					.getUsername(), temp);
			temp.delete();
		} catch (ObjectClassNotValidException ex) {
			Logger.getLogger(FileController.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void saveFile(String workspace, String path, MultipartFile mpf)
			throws BadUriException, AuthenticationException, IOException {
		String fileUri = getSelectedWorkspace() + "/";
		if (!"".equals(path) && path != null)
			fileUri += path + "/";
		fileUri += mpf.getOriginalFilename();
		FSFacade.createFile(fileUri, LoginService.getPrincipal().getUsername());
		FSFacade.setFileContent(fileUri, LoginService.getPrincipal()
				.getUsername(), mpf.getBytes());
	}

	@RequestMapping(value = "/get/**", method = RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String pathUrl = request.getRequestURI().substring(
				request.getRequestURI().indexOf("file/get/") + 9);
		String fileUri = java.net.URLDecoder.decode(pathUrl, "UTF-8");
		try {
			response.addHeader("Content-Type",
					URLConnection.guessContentTypeFromName(pathUrl));
			// response.setContentType(URLConnection.guessContentTypeFromName(pathUrl));
			// response.setHeader("Content-disposition",
			// "attachment; filename=\"" + getFileName(pathUrl) + "\"");
			FileCopyUtils.copy(FSFacade.getFileContentAsBytes(fileUri,
					LoginService.getPrincipal().getUsername()), response
					.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (BadUriException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		} catch (AuthenticationException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	@RequestMapping(value = "/getAsZip/**", method = RequestMethod.GET)
	public void getAsZip(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String pathUrl = request.getRequestURI().substring(
				request.getRequestURI().indexOf("file/getAsZip")
						+ "file/getAszip/".length());
		String fileUri = java.net.URLDecoder.decode(pathUrl, "UTF-8");
		try {
			response.addHeader("Content-Type", "application/zip");
			if (pathUrl.contains("/"))
				FSFacade.saveDirectoryContentAsZip(fileUri, LoginService
						.getPrincipal().getUsername(), response
						.getOutputStream());
			else
				FSFacade.saveWorkspaceContentAsZip(fileUri, LoginService
						.getPrincipal().getUsername(), response
						.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (BadUriException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		} catch (AuthenticationException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	public String getFileName(String path) {
		return path.contains("/") ? path.substring(path.indexOf("/") + 1)
				: path;
	}

}
