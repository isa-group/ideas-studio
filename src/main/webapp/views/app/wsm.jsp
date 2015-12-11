
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="css/wsm.css" media="screen" />

<script>
    jQuery(function() {
        AppPresenter.setCurrentSection("wsm");
            $("#versions").hide();         
    });
</script>

<div id="wsmMain" class="container-fluid">

    <div class="wrap margin-bottom-2 margin-top-4" style="margin:2%">
        <h3>Tags</h3>
        <div id="tags" >
        <jstl:forEach items="${tags}" var="tag">
            <a href="/app/wsm?filter=${tag.name}" id="tags_${tag.id}" class="tagSearch btn collapse-margin-bottom-0">${tag.name}</a>       
        </jstl:forEach> 
        </div>
        <div class="float-right margin-right-1">
            <input id="tagFilter" style="transition:width .02s ease;width:172px;" class="transparent" placeholder="Filter..." width="0">
        </div>
   </div>
         <jstl:if test="${!empty(workspaces)}">

   <div style="margin:2%">
       <h3>My workspaces</h3>
       <hr>
        <jstl:forEach items="${workspaces}" var="workspace">	
	<article class="col-5 card">
          <section>
            <div class="wrap flex-top-justify margin-bottom-1">
              <div class="float-left">
                <h4 class="ellipsize">
                  <a href="${workspace.name}" class="openWS"><span>${workspace.name}</span></a>
                </h4>
                <h6></h6>
              </div>
                <div class="float-right spaced-2">
                    <small class="lighten">
                        <span>Updated: </span>
                        <span>${workspace.lastMod}</span>
                        <span></span>
                      </small> 
                </div>
            </div>
            <p class="fixed-height-3">${workspace.description}</p>
            <div class="fixed-height-2 spaced-1">
                <jstl:forEach items="${workspace.workspaceTags}" var="wstag">
                    <a href="/app/wsm?filter=${wstag.name}" id="tags_${wstag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${wstag.name}</a>       
                </jstl:forEach> 
            </div>
            <p class="flex-center-justify">
              
              
                </p>
             <p class="flex-center-right">
                <a href="${workspace.name}" id="openWS" class="openWS btn btn-success  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Open workspace"><span class="glyphicon glyphicon-folder-open"></span></a>
              <jstl:url value="app/wsm/workspaces/${workspace.name}/edit" var="edit_url"/>
              <a href="${edit_url}" id="editWS" class="btn btn-primary  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Edit workspace"><span class="glyphicon glyphicon-pencil"></span></a>
              <a href="${workspace.name}" id="downloadWS" class="download-ws btn btn-info  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Download zip"><span class="glyphicon glyphicon-download"></span></a>
              <a href="${workspace.name}" id="publishWS" class="publish-demo btn btn-info  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Publish demo"><span class="glyphicon glyphicon-cloud-upload"></span></a>
              <a href="${workspace.name}" id="deleteWS" class="delete-ws btn btn-danger  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Delete workspace"><span class="glyphicon glyphicon-trash"></span></a>
            </p>
          </section>
          <footer class="text-small flex-center-justify">
            <span><span> Downloads: </span><strong>${workspace.downloads}</strong></span>
            <span><span> Launches: </span><strong>${workspace.launches}</strong></span>
            <span><span> Version: </span><strong>${workspace.wsVersion}</strong></span>
          </footer>
        </article>
        </jstl:forEach>                
    </div> 
             
         </jstl:if>
    
     <jstl:if test="${!empty(demos)}">
    <div id="myDemos" style="margin:2%">
        <h3>My demos</h3>
        <hr>
        <jstl:forEach items="${demos}" var="demo">	
	<article class="col-5 clickable card">
          <section>
            <div class="wrap flex-top-justify margin-bottom-1">
              <div class="float-left">
                <h4 class="ellipsize">
                  <a href="#"><span>${demo.name}</span></a>
                </h4>
                <h6></h6>
              </div>
              <div class="float-right spaced-2"><small class="lighten">
                <span>Updated: </span>
                <span>${demo.lastMod}</span>
                <span></span>
              </small>
              </div>
            </div>
            <p class="fixed-height-3">${demo.description}</p>
            <div class="fixed-height-2 spaced-1">
                    <jstl:forEach items="${demo.workspaceTags}" var="demotag">
                        <a href="/app/wsm?filter=${demotag.name}" id="tags_${demotag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${demotag.name}</a>       
                    </jstl:forEach> 
                </div>
            <p class="flex-center-left">
              
            </p>
             <p class="flex-center-right">
              <jstl:url value="demo/${demo.name}" var="demo_url"/>
              <a href="${demo_url}" id="viewDemo" target="_blank" class="btn btn-success solid  nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="View demo"><span class="glyphicon glyphicon-eye-open"></span></a>
              <a href="${demo.name}" id="updateDemo" class="updateDemo btn btn-primary solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Update demo"><span class="glyphicon glyphicon-refresh"></span></a>            
              <a href="${demo.name}" id="deleteDemo" class="delete-demo btn btn-danger  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Depublish demo"><span class="glyphicon glyphicon-eject"></span></a>
            </p>
          </section>
          <footer class="text-small flex-center-justify">
            <span><span> Downloads: </span><strong>${demo.downloads}</strong></span>
            <span><span> Launches: </span><strong>${demo.launches}</strong></span>
            <span><span> Version: </span><strong>${demo.wsVersion}</strong></span>
          </footer>
        </article>
        </jstl:forEach>                
    </div>
    </jstl:if>
    <div id="publicDemos" style="margin:2%">
        <h3>Public Demos</h3>
        <hr>
        <jstl:forEach items="${otherdemos}" var="otherdemo">	
	<article class="col-5 clickable card">
          <section>
            <div class="wrap flex-top-justify margin-bottom-1">
              <div class="float-left">
                <h4 class="ellipsize">
                  <a href="#"><span>${otherdemo.name}</span></a>
                </h4>
                <h6></h6>
              </div>
                <div class="float-right spaced-2">
                    <small class="lighten">
                <span>Updated: </span>
                <span>${otherdemo.lastMod}</span>
                <span></span>
              </small>
                </div>
            </div>
            <p class="fixed-height-3">${otherdemo.description}</p>
            <div class=" fixed-height-2 spaced-1">
                    <jstl:forEach items="${otherdemo.workspaceTags}" var="otherdemotag">
                        <a href="/app/wsm?filter=${otherdemotag.name}" id="tags_${otherdemotag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${otherdemotag.name}</a>       
                    </jstl:forEach> 
                </div>
            <p class="flex-center-left">
              
            </p>
             <p class="flex-center-right">
                <jstl:url value="demo/${otherdemo.name}" var="otherdemo_url"/>
                <a href="${otherdemo_url}" id="viewDemo" target="_blank" class="btn btn-success solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="View demo" ><span class="glyphicon glyphicon-eye-open"></span></a>
                <a href="${otherdemo.name}" id="cloneDemo" class="importDemo btn btn-primary solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Clone demo"><span class="glyphicon glyphicon-cloud-download"></span></a>
            </p>
          </section>
          <footer class="text-small flex-center-justify">
            <span><span> Downloads: </span><strong>${otherdemo.downloads}</strong></span>
            <span><span> Launches: </span><strong>${otherdemo.launches}</strong></span>
            <span><span> Version: </span><strong>${otherdemo.wsVersion}</strong></span>
          </footer>
        </article>
        </jstl:forEach>                
    </div>    
</div>

<script src='js/wsm.js' type="text/javascript"></script>
 