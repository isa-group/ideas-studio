
package es.us.isa.ideas.app.services.deployer;


/**
 *
 * @author japarejo
 */
/*
@Path("/deploymentContext")
public interface DeploymentContextResource {

    @HEAD
    @Path(value = "/{contextId}")
    boolean existsDeploymentContex(@PathParam(value = "contextId") String contextId);

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    List<DeploymentContext> getDeploymentContexs();
    
    
    @GET
    @Path(value = "/{contextId}/{path}")
    Response getDeployedResource(@PathParam(value = "contextId") String contextId, @PathParam(value = "path") String path);

    @GET
    @Path(value = "/{contextId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    DeploymentContext getDeploymentContex(@PathParam(value = "contextId") String contextId);
    
    
    @POST    
    Response createDeploymentContext(@Context UriInfo uriInfo);
    
    @POST
    @Path(value = "/initialContent")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    Response createDeploymentContex(MultipartFormDataInput input);
    
    @PUT
    @Path(value = "/{contextId}")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    Response deploy(@PathParam(value = "contextId") String contextId,MultipartFormDataInput input);        
    
    @DELETE
    @Path(value = "/{contextId}")
    Response deleteDeploymentContext(@PathParam(value = "contextId") String contextId);

    

    
    
   
    
}
*/