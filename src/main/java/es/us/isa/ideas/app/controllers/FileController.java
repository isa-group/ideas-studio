package es.us.isa.ideas.app.controllers;

import es.us.isa.ideas.app.entities.Workspace;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import es.us.isa.ideas.repo.AuthenticationManagerDelegate;
import es.us.isa.ideas.repo.IdeasRepo;
import es.us.isa.ideas.app.security.LoginService;
import es.us.isa.ideas.app.security.UserAccount;
import es.us.isa.ideas.app.services.WorkspaceService;
import es.us.isa.ideas.app.util.FileMetadata;
import es.us.isa.ideas.repo.exception.AuthenticationException;
import es.us.isa.ideas.repo.impl.fs.FSFacade;
import es.us.isa.ideas.repo.impl.fs.FSWorkspace;
import es.us.isa.ideas.utilities.AppResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/files")
public class FileController extends AbstractController {

    @Autowired
    WorkspaceService workspaceService;
    
    private static final String DEMO_MASTER="DemoMaster";
    
    private static final String FILE_TYPE_PROJECT="project";
    private static final String FILE_TYPE_DIR="directory";
    private static final String FILE_TYPE_FILE="file";
    private static final String FILE_TYPE_WS="ws";

    private static IdeasRepo repoLab = null;
    
    private static final Logger logger = Logger.getLogger(FileController.class.getName());

    public static void initRepoLab() {
        if (FileController.repoLab == null) {
            IdeasRepo.init(new AuthenticationManagerDelegate() {

                @Override
                public boolean operationAllowed(String authenticatedUser, String Owner,
                        String workspace, String project, String fileOrDirectoryUri,
                        AuthOpType operationType) {
                    return true;
                }

                @Override
                public String getAuthenticatedUserId() {
                    if (SecurityContextHolder.getContext().getAuthentication() == null || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAccount)) {
                        return "";
                    } else {
                        return LoginService.getPrincipal().getUsername();
                    }
                }
            });

            repoLab = IdeasRepo.get();
        }
    }

    /* Files C-UD */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public boolean createFile(@RequestParam("fileUri") String fileUri,
                              @RequestParam("fileType") String fileType) {
        initRepoLab();
        
        boolean res = Boolean.FALSE;
        boolean success = Boolean.TRUE;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            if (fileType.equalsIgnoreCase(FILE_TYPE_PROJECT)) { //Project
                res = FSFacade.createProject(fileUri, username);
            } 
            else if (fileType.equalsIgnoreCase(FILE_TYPE_DIR)) { //Directory
                res = FSFacade.createDirectory(fileUri, username);
            } 
            else { //FILE
                res = FSFacade.createFile(fileUri, username);
            }
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating " + fileType + ": " + fileUri, e);
            success = Boolean.FALSE;
        }
        if (success) {
            workspaceService.updateTime(getSelectedWorkspace(), username);
        }
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public boolean renameFile(@RequestParam("fileUri") String fileUri,
                              @RequestParam("fileType") String fileType,
                              @RequestParam("newName") String newName) {
        initRepoLab();
        
        boolean res = Boolean.FALSE;
        boolean success = Boolean.TRUE;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            if (fileType.equalsIgnoreCase(FILE_TYPE_DIR)) { //Directory
                res = FSFacade.renameDirectory(fileUri, username, newName);
            } 
            else { //FILE
                res = FSFacade.renameFile(fileUri, username, newName);
            }
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating " + fileType + ": " + fileUri, e);
            success = Boolean.FALSE;
        }
        if (success) {
            workspaceService.updateTime(getSelectedWorkspace(), username);
        }
        return res;
    }

    @RequestMapping(value = "/move", method = RequestMethod.PUT)
    @ResponseBody
    public boolean moveFile(@RequestParam("fileUri") String fileUri,
                            @RequestParam("fileType") String fileType,
                            @RequestParam("destUri") String destUri,
                            @RequestParam("copy") boolean copy) {
        initRepoLab();
        
        boolean res = Boolean.FALSE;
        boolean success = Boolean.TRUE;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            if (fileType.equalsIgnoreCase(FILE_TYPE_DIR)) { //Directory
                res = FSFacade.moveDirectory(fileUri, username, destUri, copy);
            } 
            else { //FILE
                res = FSFacade.moveFile(fileUri, username, destUri, copy);
            }
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error moving " + fileType + ": " + fileUri, e);
            success = Boolean.FALSE;
        }
        if (success) {
            workspaceService.updateTime(getSelectedWorkspace(), username);
        }
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteFile(@RequestParam("fileUri") String fileUri,
                              @RequestParam("fileType") String fileType) {
        initRepoLab();
        
        boolean res = Boolean.FALSE;
        boolean success = Boolean.TRUE;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            if (fileType.equalsIgnoreCase(FILE_TYPE_PROJECT)) { //Project
                res = FSFacade.deleteProject(fileUri, username);
            } 
            else if (fileType.equalsIgnoreCase(FILE_TYPE_DIR)) { //Directory
                res = FSFacade.deleteDirectory(fileUri, username);
            } 
            else { //FILE
                res = FSFacade.deleteFile(fileUri, username);
            }
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting " + fileType + ": " + fileUri, e);
            success = Boolean.FALSE;
        }
        if (success) {
            workspaceService.updateTime(getSelectedWorkspace(), username);
        }
        return res;
    }

    /* Content */
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    @ResponseBody
    public String getFileContent(@RequestParam("fileUri") String fileUri) {
        
        initRepoLab();
        
        String fileContent = "";
            
        try {
            fileContent = FSFacade.getFileContent(fileUri, LoginService.getPrincipal().getUsername());
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, null , e);
        }
        return fileContent;
    }

    @RequestMapping(value = "/content", method = RequestMethod.POST)
    @ResponseBody
    public boolean setFileContent(@RequestParam("fileUri") String fileUri,
                                  @RequestParam("fileContent") String fileContent) {
        initRepoLab();
        
        boolean res = Boolean.FALSE;
        boolean success = Boolean.TRUE;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            res = FSFacade.setFileContent(fileUri, username, fileContent);
        } 
        catch (Exception e) {
            success = Boolean.FALSE;
            logger.log(Level.SEVERE, null, e);
        }
        if (success) {
            workspaceService.updateTime(getSelectedWorkspace(), username);
        }
        return res;
    }

    /* Demo (¿¿move to --> DemoController /demo/{name}/import??) */
    @RequestMapping(value = "/importDemo", method = RequestMethod.GET)
    @ResponseBody
    public AppResponse importDemoWorkspace(
            @RequestParam("demoWorkspaceName") String demoWorkspaceName,
            @RequestParam("targetWorkspaceName") String targetWorkspaceName) {
        
        AppResponse response = new AppResponse();

        String username = LoginService.getPrincipal().getUsername();

        FSWorkspace demoWS = new FSWorkspace(demoWorkspaceName, DEMO_MASTER);
        FSWorkspace newWS = new FSWorkspace(targetWorkspaceName, username);

        Workspace demo = workspaceService.findByNameAndOwner(targetWorkspaceName, username);

        if (demo != null) {
            int downloads = demo.getDownloads()+ 1;
            demo.setDownloads(downloads);
        }

        initRepoLab();

        boolean demoExists = true;

        // TODO: Cambiar comprobacion una vez este refactorizado. No se deberia trabajar a nivel de cadenas, sino de objetos serializables (para devolver JSON)	
        try {
            demoExists = FSFacade.getWorkspaces(DEMO_MASTER).contains("\"" + demoWorkspaceName + "\"");
        } catch (Exception e) {
            logger.log(Level.SEVERE, null , e);
            demoExists = false;
        }

        if (demoExists) {

            try {
                IdeasRepo.get().getRepo().delete(newWS);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Could not remove workspace before importing.", e);
            }

            try {
                IdeasRepo.get().getRepo().move(demoWS, newWS, true);
                response.setStatus(AppResponse.Status.OK);
                response.setMessage("Demo workspace created with name: " + targetWorkspaceName);
                Workspace ws = workspaceService.findByNameAndOwner(demoWorkspaceName, DEMO_MASTER);
                String originID = "";
                if(ws!=null && ws.getOrigin()!=null)
                    originID=String.valueOf(ws.getOrigin().getId());
                workspaceService.createWorkspace(targetWorkspaceName, username, originID);

            } catch (AuthenticationException e) {
                logger.log(Level.SEVERE, "### Error creating demo WS for " + username, e);
                response.setStatus(AppResponse.Status.ERROR);
                response.setMessage(e.getMessage());
            }

        } else {
            logger.log(Level.SEVERE, "Demo {0} do not exists!", demoWorkspaceName);
            System.out.println();
            response.setStatus(AppResponse.Status.OK_PROBLEMS);
            response.setMessage("There is no Demo named " + demoWorkspaceName);
        }

        return response;

    }

    /* CRUD Workspaces in RepoLab */
    @RequestMapping(value = "/workspaces", method = RequestMethod.GET)
    @ResponseBody
    public String getWorkspacesString() {
        initRepoLab();
        String ws = "";
        try {
            ws = FSFacade.getWorkspaces(LoginService.getPrincipal().getUsername());
        } catch (AuthenticationException e) {
           logger.log(Level.SEVERE, null, e);
        }
        return ws;
    }

    @RequestMapping(value = "/workspaces", method = RequestMethod.POST)
    @ResponseBody
    public boolean createWorkspace(@RequestParam("workspaceName") String workspaceName,
                                   @RequestParam("description") String description,
                                   @RequestParam("tags") String tags) {
        initRepoLab();
        boolean res = false;
        boolean success = true;
        String username = LoginService.getPrincipal().getUsername();
        try {
            res = FSFacade.createWorkspace(workspaceName, username);

        } catch (Exception e) {
            success = false;
            logger.log(Level.SEVERE, null, e);
        }
        if (success) {
            workspaceService.createWorkspaceWithTags(workspaceName, description, username, null,tags);
        }
        return res;
    }

    @RequestMapping(value = "/workspaces", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateWorkspace( @RequestParam("workspaceName") String workspaceName,
                                    @RequestParam("newName") String newName,
                                    @RequestParam("newDescription") String newDescription){
        initRepoLab();
        boolean success = true;
        String username = LoginService.getPrincipal().getUsername();
        
        String workspace = getSelectedWorkspace();
        
        FSWorkspace oldWS = new FSWorkspace(workspaceName, username);
        FSWorkspace newWS = new FSWorkspace(newName, username);
        
        boolean nameExists = true;
        
        if(!(workspace.equals(newName))){
            try {
                nameExists = FSFacade.getWorkspaces(username).contains("\"" + nameExists + "\"");
            } catch (Exception e) {
                logger.log(Level.SEVERE, null , e);
                nameExists = false;
            }
            if (!nameExists){
                try {
                    IdeasRepo.get().getRepo().move(oldWS, newWS, true);
                    IdeasRepo.get().getRepo().delete(oldWS); 
                    
                } catch (AuthenticationException ex) {
                    success=false;
                    Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(success){
                    Workspace ws = workspaceService.findByNameAndOwner(workspaceName, username);
                    ws.setDescription(newDescription);
                    ws.setName(newName);    
                    workspaceService.save(ws);
                    try {
                        FSFacade.saveSelectedWorkspace(newName, username);
                    } catch (IOException ex) {
                        Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            else{
                logger.log(Level.SEVERE, "Workspace with name {0} already exists!", newName);
            }
        }
        return success;
    }

    @RequestMapping(value = "/workspaces",method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteWorkspace(@RequestParam("workspaceName") String workspaceName) {
        
        initRepoLab();
        
        boolean success = true;
        
        String username = LoginService.getPrincipal().getUsername();
        
        try {
            FSFacade.deleteWorkspace(workspaceName, username);
        } 
        catch (Exception e) {
            success = false;
            logger.log(Level.SEVERE, null, e);
        }
        if (success) {
            workspaceService.delete(workspaceName, username);
        }
        return success;
    }

    @RequestMapping(value = "/workspaces/selected", method = RequestMethod.GET)
    @ResponseBody
    public String getSelectedWorkspace() {
        
        initRepoLab();
        
        String res = "";
        
        try {
            res = FSFacade.getSelectedWorkspace(LoginService.getPrincipal().getUsername());
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return res;
    }
    
    @RequestMapping(value = "/workspaces/selected", method = RequestMethod.POST)
    @ResponseBody
    public boolean setSelectedWorkspace(@RequestParam("workspaceName") String workspaceName) {
        
        initRepoLab();
        logger.log(Level.INFO, "Persisting selected workspace:  "
                        + workspaceName + ", username: "
                        + LoginService.getPrincipal().getUsername());
        boolean res = true;
        try {
                FSFacade.saveSelectedWorkspace(workspaceName, LoginService
                                .getPrincipal().getUsername());
        } catch (Exception e) {
                res = false;
                logger.log(Level.SEVERE, e.getMessage());
        }
        return res;
      
    }

    /* Upload */
    @RequestMapping(value = "/upload/**", method = RequestMethod.POST)
    public @ResponseBody
    Collection<FileMetadata> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        //1. build an iterator
        Iterator<String> itr = request.getFileNames();
        String pathUrl = request.getRequestURI().substring(request.getRequestURI().indexOf("files/upload/") + 12);
        MultipartFile mpf = null;
        FileMetadata fileMeta = null;
        String fileUri = null;
        List<FileMetadata> files = new ArrayList<FileMetadata>();
        String username = LoginService.getPrincipal().getUsername();

        //2. get each file
        while (itr.hasNext()) {

            //2.1 get next MultipartFile
            mpf = request.getFile(itr.next());
            System.out.println(mpf.getOriginalFilename() + " uploaded!");

            //2.3 create new fileMeta
            fileMeta = new FileMetadata();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
            fileMeta.setFileType(mpf.getContentType());

            try {
                fileMeta.setBytes(mpf.getBytes());
                fileUri = getSelectedWorkspace() + "/" + pathUrl + "/" + fileMeta.getFileName();
                FSFacade.createFile(fileUri, username);
                FSFacade.setFileContent(fileUri, username, mpf.getBytes());
                files.add(fileMeta);
                workspaceService.updateTime(getSelectedWorkspace(), username);

            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }
        }

        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        return files;

    }

    @RequestMapping(value = "/uploadAndExtract/**", method = RequestMethod.POST)
    public ModelAndView uploadAndExtract(MultipartHttpServletRequest request, HttpServletResponse response) {
        
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;
        
        while (itr.hasNext()) {

            mpf = request.getFile(itr.next());

            try {
                if (mpf.getOriginalFilename().endsWith(".zip") || mpf.getOriginalFilename().endsWith(".gz")) {
                    extractInWorkspace(getSelectedWorkspace(), mpf);
                } else {
                    saveFile(getSelectedWorkspace(), "defaultProject", mpf);
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
        return new ModelAndView("app/editor");

    }

    public void extractInWorkspace(String workspace, MultipartFile mpf){
        
        String username = LoginService.getPrincipal().getUsername();
        
        int extensionIndex = mpf.getOriginalFilename().lastIndexOf('.');
        
        if (extensionIndex == -1) {
            extensionIndex = mpf.getOriginalFilename().length() - 1;
        }
        
        String folderUri = workspace + "/" + mpf.getOriginalFilename().substring(0, extensionIndex);
        
        try {
            File temp = File.createTempFile("uploadExtraction", "zipFiles");
            mpf.transferTo(temp);
            FSFacade.extractInNew(folderUri, LoginService.getPrincipal().getUsername(), temp);
            temp.delete();
            workspaceService.updateTime(workspace, username);
        } 
        catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void saveFile(String workspace, String path, MultipartFile mpf) {
       
        String username = LoginService.getPrincipal().getUsername();
        String fileUri = getSelectedWorkspace() + "/";
        
        if (!"".equals(path) && path != null) {
            fileUri += path + "/";
        }
        
        fileUri += mpf.getOriginalFilename();
        try{
            FSFacade.createFile(fileUri, username);
            FSFacade.setFileContent(fileUri, username, mpf.getBytes());
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        
        workspaceService.updateTime(workspace, username);
    }

    @RequestMapping(value = "/get/**", method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response) {
        
        String username = LoginService.getPrincipal().getUsername();
        String pathUrl = request.getRequestURI().substring(request.getRequestURI().indexOf("files/get/") + 9);
        
        try {
            String fileUri = java.net.URLDecoder.decode(pathUrl, "UTF-8");
            response.addHeader("Content-Type", URLConnection.guessContentTypeFromName(pathUrl));
            FileCopyUtils.copy(FSFacade.getFileContentAsBytes(fileUri, username), response.getOutputStream());
            response.flushBuffer();
        } 
        catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public String getFileName(String path) {
        return path.contains("/") ? path.substring(path.indexOf("/") + 1) : path;
    }

    @RequestMapping(value = "/getAsZip/**", method = RequestMethod.GET)
    public void getAsZip(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException 
    {
        String pathUrl = request.getRequestURI().substring(request.getRequestURI().indexOf("files/getAsZip") + "files/getAszip/".length());
        String fileUri = java.net.URLDecoder.decode(pathUrl, "UTF-8");

        boolean success = Boolean.FALSE;

        String username = LoginService.getPrincipal().getUsername();

        try {
            response.addHeader("Content-Type", "application/zip");
            if (pathUrl.contains("/")) {
                FSFacade.saveDirectoryContentAsZip(fileUri, username, response.getOutputStream());
            } else {
                FSFacade.saveWorkspaceContentAsZip(fileUri, username, response.getOutputStream());
                success = Boolean.TRUE;
            }
            response.flushBuffer();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        if (success) {
            workspaceService.updateDownloads(fileUri, username);
        }
    }

}
