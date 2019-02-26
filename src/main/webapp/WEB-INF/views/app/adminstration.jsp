<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%@taglib prefix="ideas" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ideas:app-template>
    <script>
        jQuery(function () {

            AppPresenter.setCurrentSection("administration");
        });
    </script>
    <div>ADMINISTRATION</div>
</ideas:app-template>