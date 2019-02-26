<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<ideas:app-template>
    <div id="wsmMain" class="container-fluid">
        <div class="wrap margin-bottom-2 margin-top-4">

            <div class="float-right margin-right-1">
                <input style="transition:width .02s ease;width:172px;" class="transparent" placeholder="Filter..." width="0">
            </div>
        </div>
        <div style="margin:5%">
            <jstl:forEach items="${workspaces}" var="workspace">	
                <article class="col-5 clickable card">
                    <section>
                        <div class="wrap flex-top-justify margin-bottom-1">
                            <div class="float-left">
                                <h4 class="ellipsize">
                                    <a href="#"><span>${workspace.name}</span></a>
                                </h4>
                                <h6></h6>
                            </div>
                            <div class="float-right spaced-2"></div>
                        </div>
                        <p class="fixed-height-3">${workspace.description}</p>
                        <p class="flex-center-justify">
                            <small class="lighten">
                                <span>Updated: </span>
                                <span>${workspace.lastMod}</span>
                                <span></span>
                            </small>
                            <a href="#" id="openWS" class="button solid success nounderline collapse-margin-bottom-0">Open</a>
                            <a href="workspace/edit"> TEST</a>
                        <jstl:choose>
                            <jstl:when test="true">
                                <jstl:url value="wsm/workspaces/${workspace.name}/edit" var="edit_url"/>
                                <a href="#" id="editWS" class="button solid nounderline collapse-margin-bottom-0">Edit</a>
                            </jstl:when>    
                            <jstl:otherwise>
                                <a href="#" id="cloneWS" class="button solid nounderline collapse-margin-bottom-0">Clone</a>
                            </jstl:otherwise>
                        </jstl:choose>

                        </p>
                    </section>
                    <footer class="text-small flex-center-justify">
                        <span><span> Downloads: </span><strong>${workspace.downloads}</strong></span>
                        <span><span> Launches: </span><strong>${workspace.launches}</strong></span>
                        <span><span> Version: </span><strong>${workspace.version}</strong></span>
                    </footer>
                </article>
            </jstl:forEach>                
        </div>
    </div>
</ideas:app-template>