<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
	
<!DOCTYPE html>
<!-- saved from url=(0040)http://getbootstrap.com/examples/signin/ -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <script type="text/javascript" src="<c:url value='/assets/js/jquery.MultiFile.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap/js/bootstrap-filestyle.min.js'/>"></script>
    
    
    <link rel="icon" href="<c:url value='/assets/bootstrap/fonts/virtual-queue.ico'/>">
 	<link href="<c:url value='/assets/bootstrap/css/bootstrap.min.css'/>" rel="stylesheet">
	
	
    <title>Simula Comportamento Fila</title>

    <!-- Custom styles for this template -->
    <link href="<c:url value='/assets/bootstrap/css/signin.css'/>" rel="stylesheet">


    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="<c:url value='/assets/bootstrap/js/ie-emulation-modes-warning.js'/>"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
   
  </head>
<body>
	<h1 class="text-center">Calcula comportamento fila </h1>
	<h6 class="text-center">Versão 2.0.0</h6>
	<p class="text-center"><a class="btn btn-primary btn-lg" href="carregar"> Clique aqui para iniciar!</a>
	<c:redirect url="carregar"/> 
</body>
</html>