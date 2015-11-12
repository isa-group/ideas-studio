<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>

<div id="appFooter">
	<div id="version">v${project.version}</div> 
	<span id="appFooterCopyright"> 
		<spring:message code="app.footer.copyright" />
	</span> 
	<span id="isaLogoSmall"> 
		<a href="http://www.isa.us.es/" target="_blank"> 
			<img src="img/ideas/isaLogoSmall.png" alt="ISA">
		</a>
	</span>
</div>

<div id="versions">
	<img id="vClose" alt="" src="img/ideas/basic-icon-x.png">
	<div id="vers-info">
		<%
			String file = getServletContext().getRealPath("/")
					+ "/CHANGELOG.txt";
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(file));
				while ((sCurrentLine = br.readLine()) != null) {
					out.println(sCurrentLine + "<br>");
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
		%>
	</div>

</div>