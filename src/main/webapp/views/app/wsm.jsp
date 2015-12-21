
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="css/wsm.css" media="screen" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<script>
    jQuery(function() {
        AppPresenter.setCurrentSection("wsm");
            $("#versions").hide();         
    });
</script>

<div id="wsmMain">
   
    <div class="wsm_content_area">
         
        <h2 class="title-wsm">Workspaces</h2>
        <div class="divider"></div>
        <div class="isotope_section wsm-menu-members" id="workspaceFilters">
            <ul>
                <li class="active_prot_menu"><a class="filterLink" href="#" data-filter-ws="">All</a></li>
                <li class=""><a class="filterLink" href="#" data-filter-ws=".workspace">Workspaces</a></li>
                <li class=""><a class="filterLink" href="#" data-filter-ws=".demoworkspace">Demos</a></li>
                <li class=""><a class="filterLink" href="#" data-filter-ws=".publicdemo">Public Demos</a></li>
            </ul>
        </div>
        <div id="tagFilters" class="isotope_section button-group"> 
            
            <ul>
                <h3>Tags:</h3>
            <jstl:forEach items="${tags}" var="tag">
                <li class=""><a class="button tagFilterLink" data-filter-tag=".tagged_${tag.name}" >${tag.name}</a></li>
            </jstl:forEach> 
            </ul> 
        </div>
        <div class="wsm_content">
            <div class="row" id="wsmWorkspaces">
                <div class="timelineContainer">
                </div>
                <div class="col-xs-12 cards">
                    <jstl:forEach items="${workspaces}" var="workspace">
                                                               
                    <!--begin_ws-->
                    <div class="col-md-4 col-sm-6 wholeCard workspace <jstl:forEach items="${workspace.workspaceTags}" var="tag"> tagged_${tag.name} </jstl:forEach>" type="workspace">
                        <div class="card radius shadowDepth1">
                            <div class="card__meta">
                                
                                <img class="card__meta-logo" src="./img/ideas/default_screenshot.png" alt="screenshot" title="${workspace.name}">
                                <div class="card__meta-content">
                                    <time class="date">${workspace.lastMod}</time>
                                    <p class="card__meta-content-title">${workspace.name}</p>
                                    <p class="card__meta-content-subtitle">${workspace.description}</p>
                                   
                                    <p class="card__content-text">
                                         <jstl:forEach items="${workspace.workspaceTags}" var="wstag">
                                            <a href="#" id="tags_${wstag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${wstag.name}</a>       
                                        </jstl:forEach>
                                    </p>
                                </div>
                            </div>
                            
                            <div class="card__content card__padding">
                                <article class="actions_bar">    
                                    <div class="actions_bar_title flex-center-left">Actions:</div>
                                    <div class="actions_bar_buttons flex-center-right">                                    
                                        <a href="${workspace.name}" id="openWS" class="openWS btn btn-success  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Open workspace"><span class="glyphicon glyphicon-folder-open"></span></a>
                                      <jstl:url value="app/wsm/workspaces/${workspace.name}/edit" var="edit_url"/>
                                      <a href="${edit_url}" id="editWS" class="btn btn-primary  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Edit workspace"><span class="glyphicon glyphicon-pencil"></span></a>
                                      <a href="${workspace.name}" id="downloadWS" class="download-ws btn btn-info  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Download zip"><span class="glyphicon glyphicon-download"></span></a>
                                      <a href="${workspace.name}" id="publishWS" class="publish-demo btn btn-info  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Publish demo"><span class="glyphicon glyphicon-cloud-upload"></span></a>
                                      <a href="${workspace.name}" id="deleteWS" class="delete-ws btn btn-danger  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Delete workspace"><span class="glyphicon glyphicon-trash"></span></a>
                                    </div>
                                </article>
                            </div>
                            <div class="card__action">
                                <div class="card__author">
                                    <div class="card__author-content">
                                       <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">get_app</i>
                                                <p>${workspace.downloads}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">launch</i>
                                                <p>${workspace.launches}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">label</i>
                                                <p>${workspace.wsVersion}</p>
                                            </div>
                                        </a>
                                    </div>
<!--                                    <div class="card__author-content">
                                        <div class="card__author-content_item">
                                            <a target="_blank" title="Author" href="#"><i class="material-icons">perm_identity</i></a>
                                        </div>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--end_ws-->
                    </jstl:forEach>
                    <jstl:forEach items="${demos}" var="demo">	
                    <!--begin_ws-->
                    <div class="col-md-4 col-sm-6 wholeCard demoworkspace <jstl:forEach items="${demo.workspaceTags}" var="demotag"> tagged_${demotag.name} </jstl:forEach>" type="demoworkspace">
                        <div class="card radius shadowDepth1">
                            <div class="card__meta">
                                <img class="card__meta-logo" src="./img/ideas/default_screenshot.png" alt="screenshot" title="${demo.name}">
                                <div class="card__meta-content">
                                    <time class="date">${demo.lastMod}</time>
                                    <p class="card__meta-content-title">${demo.name}</p>
                                    <p class="card__meta-content-subtitle">${demo.description}</p>
                                    
                                    <p class="card__content-text">
                                        <jstl:forEach items="${demo.workspaceTags}" var="demotag">
                                            <a href="#" id="tags_${demotag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${demotag.name}</a>       
                                        </jstl:forEach> 
                                    </p>
                                </div>
                            </div>                    
                            <div class="card__content card__padding">
                                <article class="actions_bar">    
                                    <div class="actions_bar_title flex-center-left">Actions:</div>
                                    <div class="actions_bar_buttons flex-center-right">  
                                        <jstl:url value="demo/${demo.name}" var="demo_url"/>
                                        <a href="${demo_url}" id="viewDemo" target="_blank" class="btn btn-success solid  nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="View demo"><span class="glyphicon glyphicon-eye-open"></span></a>
                                        <a href="${demo.name}" id="updateDemo" class="updateDemo btn btn-primary solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Update demo"><span class="glyphicon glyphicon-refresh"></span></a>            
                                        <a href="${demo.name}" id="deleteDemo" class="delete-demo btn btn-danger  collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Depublish demo"><span class="glyphicon glyphicon-eject"></span></a>
                                    </div>
                                </article>
                            </div>
                            <div class="card__action">
                                <div class="card__author">
                                    <div class="card__author-content">
                                       <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">get_app</i>
                                                <p>${demo.downloads}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">launch</i>
                                                <p>${demo.launches}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">label</i>
                                                <p>${demo.wsVersion}</p>
                                            </div>
                                        </a>
                                    </div>
<!--                                    <div class="card__author-content">
                                        <div class="card__author-content_item">
                                            <a target="_blank" title="Author" href="#"><i class="material-icons">perm_identity</i></a>
                                        </div>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--end_ws-->
                    </jstl:forEach>
                    <jstl:forEach items="${otherdemos}" var="otherdemo">	
                    <!--begin_ws-->
                    <div class="col-md-4 col-sm-6 wholeCard publicdemo <jstl:forEach items="${otherdemo.workspaceTags}" var="publictag"> tagged_${publictag.name} </jstl:forEach>" type="publicdemo">
                        <div class="card radius shadowDepth1">
                            <div class="card__meta">
                                
                                <img class="card__meta-logo" src="./img/ideas/default_screenshot.png" alt="${otherdemo.name} screenshot" title="${otherdemo.name}">
                                <div class="card__meta-content">
                                    <time class="date">${otherdemo.lastMod}</time>
                                    <p class="card__meta-content-title">${otherdemo.name}</p>
                                    <p class="card__meta-content-subtitle">${otherdemo.description}</p>
                                    
                                    <p class="card__content-text">
                                        <jstl:forEach items="${otherdemo.workspaceTags}" var="otherdemotag">
                                            <a href="#" id="tags_${otherdemotag.id}" class="tagSearch btn-xs btn btn-info collapse-margin-bottom-0">${otherdemotag.name}</a>       
                                        </jstl:forEach> 
                                    </p>
                                </div>
                            </div>
                            
                           <div class="card__content card__padding">
                                <article class="actions_bar">    
                                    <div class="actions_bar_title flex-center-left">Actions:</div>
                                    <div class="actions_bar_buttons flex-center-right">  
                                        <jstl:url value="demo/${otherdemo.name}" var="otherdemo_url"/>
                                        <a href="${otherdemo_url}" id="viewDemo" target="_blank" class="btn btn-success solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="View demo" ><span class="glyphicon glyphicon-eye-open"></span></a>
                                        <a href="${otherdemo.name}" id="cloneDemo" class="importDemo btn btn-primary solid nounderline collapse-margin-bottom-0 margin-right-1" data-toggle="tooltip" data-placement="bottom" title="Clone demo"><span class="glyphicon glyphicon-import"></span></a>
                                    </div>
                                </article>
                            </div>
                            <div class="card__action">
                                <div class="card__author">
                                    <div class="card__author-content">
                                       <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">get_app</i>
                                                <p>${workspace.downloads}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">launch</i>
                                                <p>${workspace.launches}</p>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="card__author-content">
                                        <a class="tooltips">
                                            <div class="card__author-content_item">
                                                <i class="material-icons">label</i>
                                                <p>${workspace.wsVersion}</p>
                                            </div>
                                        </a>
                                    </div>
<!--                                    <div class="card__author-content">
                                        <div class="card__author-content_item">
                                            <a target="_blank" title="Author" href="#"><i class="material-icons">perm_identity</i></a>
                                        </div>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--end_ws-->
                    </jstl:forEach>
                </div>
            </div>
        </div>
    </div>
                   
    </div>    
</div>

<script src='js/wsm.js' type="text/javascript"></script>
 