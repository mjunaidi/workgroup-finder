<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="assets/ico/favicon.png">
  <title>Workgroup Finder</title>
  
  <!-- Bootstrap core CSS -->
  <link href="bootstrap/css/bootstrap.cerulean.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="css/offcanvas.css" rel="stylesheet">

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
  <![endif]-->
</head>

<body ng-controller="MainCtrl" ng-init="init()">
  <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Workgroup Finder</a>
      </div>
      <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
          <li class="active"><a href="#home">Home</a></li>
          <li><a data-toggle="modal" data-target="#aboutModal" href="">About</a></li>
        </ul>
        <!-- right navbar links -->
        <ul class="nav navbar-nav navbar-right">
          <li class="dropdown">
            <c:choose>
              <c:when test="${username != null}">
                <a href="" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span> <b>${username}</b> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <%-- <li><a href="${pageContext.request.contextPath}/fileUpload">Data Manager</a></li> --%>
                  <%-- <li><a href="#fileUpload">Data Manager</a></li> --%>
                  <li><a href="#manage">Data Manager</a></li>
                  <li><a href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
                </ul>
              </c:when>
              <c:otherwise>
                <a href="" class="dropdown-toggle" data-toggle="dropdown">Login <b class="caret"></b></a>
                <div class="dropdown-menu" ng-include="'${pageContext.request.contextPath}/login'"></div>
              </c:otherwise>
            </c:choose>
          </li>
        </ul>
      </div>
      <!-- /.nav-collapse -->
    </div>
    <!-- /.container -->
  </div>
  <!-- /.navbar -->

  <div class="container">

    <div class="row row-offcanvas row-offcanvas-right">
      <div class="col-xs-12 col-sm-12">
        <div class="row">
          <div class="col-12 col-sm-12 col-lg-12" ng-view></div>
          <!--/span-->
        </div>
        <!--/row-->
      </div>
      <!--/span-->
    </div>
    <!--/row-->

  </div>
  <!--/.container-->

  <div ng-include="'content/aboutModal.html'"></div>

  <!-- Bootstrap core JavaScript
    ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <!-- <script src="assets/js/jquery.js"></script> -->
  <script src="angularjs/jquery-1.10.2.min.js"></script>
  <script src="bootstrap/js/bootstrap.min.js"></script>
  <script src="js/offcanvas.js"></script>

  <!-- Angular JS -->
  <script src="angularjs/underscore-min.js"></script>
  <script src="angularjs/angular.min.js"></script>
  <script src="angularjs/app.js"></script>
  <script src="angularjs/directives.js"></script>
  <script src="angularjs/navigation.js"></script>
  <script src="angularjs/services.js"></script>
  <script src="angularjs/workgroup.js"></script>
  <script src="angularjs/files.js"></script>
  <script src="angularjs/MainCtrl.js"></script>
</body>
</html>
