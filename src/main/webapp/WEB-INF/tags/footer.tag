<%@tag description="Pie de las páginas de ideas" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<jsp:useBean id="date" class="java.util.Date" />

<hr />
<footer id="footer" class="row-fluid">
    <p class="pull-right">
        <a href="#top">
            <spring:message code="master.page.back-top" />
        </a>
    </p>
    <div id="Authors"  class="span12 center centercontents">
        <spring:message code="master.page.authors" />
        <ul>
            <li>
                <a href="http://www.isa.us.es/members/joseantonio.parejo">
                    Jos&eacute; Antonio Parejo Maestre
                </a>
            </li>            
            <li>
                <a href="http://www.isa.us.es/members/antonio.ruiz">
                    Antonio Ruiz Cort&eacute;s
                </a>
            </li>            
        </ul>
    </div>
    <div id="Acnowledgements" class="span12 center centercontents">
        <div id="AcnowledgementsLogos">
            <ul>
                <li><a href="http://www.us.es"><img src="./img/USLogo.png" alt="Universidad de Sevilla"/></a></li>
                <li><a href="http://www.juntadeandalucia.es/index.html"><img src="./img/juntadeandalucia.jpg" alt="Junta de Andaluc&iacute;a"/></a></li>
                <li><a href="http://www.mcinn.es"><img src="./img/mcin.png" alt="Ministerio de Ciencia e Innovaci&oacute;n"/></a></li>                
            </ul>
        </div>
        <spring:message code="master.page.support" /><br/>    
    </div>
    <div id="techInfo" class="row-fluid">
        <div id="PoweredBy" class="span4 pull-left">
            <!--Powered by:
            <ul>
                <li>
                    <img src="./img/logoApache.png" alt="Apache"> 
                </li>
                <li>
                    <img src="./img/glassfish.gif" alt="GlassFish"> 
                </li>
            </ul>-->
        </div>
        <div id="StandardsMeeting" class="span3 pull-right">
            <ul>
                <li>
                    <a href="http://jigsaw.w3.org/css-validator/check/referer">
                        <img style="border:0;width:88px;height:31px"
                             src="http://jigsaw.w3.org/css-validator/images/vcss"
                             alt="Valid CSS!" />
                    </a>
                </li>
                <li><a href="http://validator.w3.org/check?uri=referer"><img
                            src="http://www.w3.org/Icons/valid-html401"
                            alt="Valid HTML 4.01 Transitional" height="31" width="88">
                    </a>
                </li>
            </ul>
        </div>
    </div>
</footer>