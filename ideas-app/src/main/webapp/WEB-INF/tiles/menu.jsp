<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div id="menu">
	<security:authorize access="hasRole('ADMIN')">
		<div id="navigationBar" class="navbar">
			<div class="navbar-inner">
				<a class="brand" href="admin/dashboard">Dashboard</a>
				<ul class="nav" role="menu">
					<li><a href="researcher/list">Researchers</a></li>
					<li><a href="experiment/list">Experimentos</a></li>
					<li><a href="executor/manage">Execution Engine</a>
					<li>
				</ul>
				<form class="navbar-search pull-left">
					<input type="text" class="search-query" placeholder="Search">
				</form>
			</div>
		</div>
	</security:authorize>

	<security:authorize access="hasRole('RESEARCHER')">
		<div id="navigationBar" class="navbar">
			<div class="navbar-inner">
				<a class="brand" href="researcher/dashboard">Dashboard</a>
				<ul class="nav" role="menu">
					<!--                     <li><a href="researcher/list">Researchers</a></li> -->
					<!--                     <li><a href="experiment/list">Experimentos</a></li>             -->
					<!--                     <li><a href="executor/manage">Execution Engine</a><li>             -->
					<li><a href="app/editor">Editor</a></li>
				</ul>
				<form class="navbar-search pull-left">
					<input type="text" class="search-query" placeholder="Search">
				</form>
			</div>
		</div>
	</security:authorize>




</div>
