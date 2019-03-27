<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<ideas:portal-layout>
    <section id="home" name="home"></section>
    <div id="headerwrap" class="headerwrap">
        <div class="container">
            <div class="row centered">
                <div class="col-lg-12">
                    <h1>Welcome To <b>EXEMPLAR</b></h1>
                    <h3>EXpEriments Management PLAtfoRm</h3>
                    <br>
                </div>

                <div class="col-lg-2">
                    <h5>Organize your research</h5>
                    <p>Use your personal space to create workspaces 
                        and projects, you can control
                        access and upload contents such as pdfs, raw data, figures, and scripts.</p>
                    <img class="hidden-xs hidden-sm hidden-md" src="img/arrow1.png">
                </div>
                <div class="col-lg-8">
                    <img class="img-responsive" src="img/app-bg.png" alt="">
                </div>
                <div class="col-lg-2">
                    <br>
                    <img class="hidden-xs hidden-sm hidden-md" src="img/arrow2.png">
                    <h5>Formalize, automate, replicate</h5>
                    <p>By formailizing your experiment, you can benefit our powerfull automation
                        toolset: threats to validity detection, missing information alerts, consistency checkings...</p>
                </div>
            </div>
        </div> <!--/ .container -->
    </div><!--/ #headerwrap -->


    <section id="desc" name="desc"></section>
    <!-- INTRO WRAP -->
    <div id="intro">
        <div class="container">
            <div class="row centered">
                <h1 class="centered">Why EXEMPLAR?</h1>
                <br>
                <h2 class="text-info">â€œThe use of precise, repeatable experiments is the hallmark of a mature scientific or engineering disciplineâ€</h2>                                
                <span class="cite">
                    "On the relationship between the object-oriented paradigm and software reuse: An empirical investigation" 
                    Lewis, J.A., Henry, S.M., Kafura, D.G., Schulman, R.S.
                    Technical report, Blacksburg, VA, USA (1992)
                </span>
                <h2 class="text-info">"Replication is at the heart of the experimental paradigm and is considered to be the cornerstone of scientific knowledge"</h2>
                <span class="cite">
                    "Understanding replication of experiments in software engineering: A classification". 
                    Omar S. GÃ³mez, Natalia Juristo, Sira Vegas. 
                    Information and Software Technology. Volume 56, Issue 8, August 2014, Pages 1033â€“1048
                </span>
                <br>                                
                <h3 class="text-warning">however... may authors note that</h3>
                <h2 class="text-danger">"Verifying results found in the literature is in practice almost impossible"</h2>                                
                <h2 class="text-danger">"The details presented in a typical paper are insufficient to ensure that one would implement the same algorithm"</h2>
                <span class="cite">
                    Eiben, A., Jelasity, M.: A critical note on experimental research methodology in EC. Computational Intelligence, Proceedings of the World on Congress on 1 (2002) 582â€“587</span>
                <h2 class="text-danger right">"Most Software Engieering experiments results have not been reproduced"</h2>
                <span class="cite">Natalia Juristo, Omar S. GÃ³mez: Replication of Software Engineering Experiments, chapter of Empirical Software Engineering and Verification. Lecture Notes in Computer Science Volume 7007, 2012, pp 60-88</cite>
                    <h2 class="text-danger">"Not only are experiments rarely replicated, they are rarely even replicable in a meaningful way.</h2>
                    <span class="cite"> Ian P. Gent: The recomputation manifesto. Available online at http://www.recomputation.org/papers/Manifesto1_9479.pdf</cite>				
                        <h1 class="text-success">Our approach:</h1>
                        <br>
                        <br>
                        <div class="col-lg-4">
                            <img src="img/intro01-exemplar.png" alt="">
                            <h3>An repository of experiments in the Cloud</h3>
                            <p>With exemplar you can store, share and monitor accseso to your experimental information, scripts and data.</p>
                        </div>
                        <div class="col-lg-4">
                            <img src="img/intro02-exemplar.png" alt="">
                            <h3>Powerfull online edition capabilities</h3>
                            <p>EXEMPLAR integrates a SEDL editor which enables the formalization of your experiments.</p>
                        </div>
                        <div class="col-lg-4">
                            <img src="img/intro03-exemplar.png" alt="">
                            <h3>Automation</h3>
                            <p>EXEMPLAR can replicate your statistical analyses. EXEMPLAR helps you by detecting common validity threats, and pointing out improvements and missing information.</p>
                        </div>
                        </div>
                        <br>
                        <hr>
                        </div> <!--/ .container -->
                        </div><!--/ #introwrap -->

                        <!-- FEATURES WRAP -->
                        <div id="features">
                            <div class="container">
                                <div class="row">
                                    <h3>Features of exemplar</h3>
                                    <br>
                                    <div class="col-lg-6 centered">
                                        <img class="centered" src="img/clinic-doctor-health-hospital.jpg" alt="">
                                    </div>
                                    <div class="col-lg-6">

                                        <!-- ACCORDION -->
                                        <div class="accordion ac" id="accordion2">
                                            <div class="accordion-group">
                                                <div class="accordion-heading">
                                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
                                                        Currently the best to ensure replicability is by creating and sharing a lab-pack.
                                                    </a>
                                                </div><!-- /accordion-heading -->
                                                <div id="collapseOne" class="accordion-body collapse in">
                                                    <div class="accordion-inner">
                                                        <p></p>
                                                    </div><!-- /accordion-inner -->
                                                </div><!-- /collapse -->
                                            </div><!-- /accordion-group -->
                                            <br>

                                            <div class="accordion-group">
                                                <div class="accordion-heading">
                                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                                                        Automate your analyses, enhance your papers.
                                                    </a>
                                                </div>
                                                <div id="collapseTwo" class="accordion-body collapse">
                                                    <div class="accordion-inner">
                                                        <p>.</p>
                                                    </div><!-- /accordion-inner -->
                                                </div><!-- /collapse -->
                                            </div><!-- /accordion-group -->
                                            <br>

                                            <div class="accordion-group">
                                                <div class="accordion-heading">
                                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">
                                                        Awesome Support
                                                    </a>
                                                </div>
                                                <div id="collapseThree" class="accordion-body collapse">
                                                    <div class="accordion-inner">
                                                        <p>.</p>
                                                    </div><!-- /accordion-inner -->
                                                </div><!-- /collapse -->
                                            </div><!-- /accordion-group -->
                                            <br>

                                            <div class="accordion-group">
                                                <div class="accordion-heading">
                                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseFour">
                                                        Responsive Design
                                                    </a>
                                                </div>
                                                <div id="collapseFour" class="accordion-body collapse">
                                                    <div class="accordion-inner">
                                                        <p>.</p>
                                                    </div><!-- /accordion-inner -->
                                                </div><!-- /collapse -->
                                            </div><!-- /accordion-group -->
                                            <br>			
                                        </div><!-- Accordion -->
                                    </div>
                                </div>
                            </div><!--/ .container -->
                        </div><!--/ #features -->


    <section id="showcase" name="showcase"></section>
                        <div id="showcase">
                            <div class="container">
                                <div class="row">
                                    <h1 class="centered">News</h1>
                                    <br>
                                    <div class="col-lg-8 col-lg-offset-2">
                                        <div id="carousel-example-generic" class="carousel slide">
                                            <!-- Indicators -->
                                            <ol class="carousel-indicators">
                                                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                                                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                                            </ol>

                                            <!-- Wrapper for slides -->
                                            <div class="carousel-inner headerwrap">
                                                <div class="item active">
                                                    <div class="well-sm">
                                                        <h5>Charting module v.1</h5>
                                                        <p>EXEMPLAR supports the creation of histograms and boxplots</p>
                                                        <a href="https://exemplar.us.es/demo/ChartsDemoWorkspace">TRY IT NOW!</a>
                                                    </div>
                                                </div>
                                                <div class="item">
                                                    <div class="well-sm">
                                                        <h5>R module v.1</h5>
                                                        <p>EXEMPLAR supports the authoring and execution of R scripts</p>
                                                        <a href="https://exemplar.us.es/demo/Rdemo">TRY IT NOW!</a>
                                                    </div>
                                                </div>-
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <br>
                                <br>	
                            </div><!-- /container -->
                        </div>


                        <section id="useit" name="useit"></section>
                        <div id="footerwrap">					
                            <div class="row centered">
                                <h1><a href="app/editor">Start using EXEMPLAR now!</a></h1>		
                            </div>
                        </div>
</ideas:portal-layout>