<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="br.com.spring.util.Constantes" %>	
<!DOCTYPE html>
<!-- saved from url=(0040)http://getbootstrap.com/examples/signin/ -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

	<script type="text/javascript" src="<c:url value='/assets/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap/js/bootstrap.js'/>"></script>
    
    <link rel="icon" href="<c:url value='/assets/bootstrap/fonts/virtual-queue.ico'/>">
 	<link href="<c:url value='/assets/bootstrap/css/bootstrap.min.css'/>" rel="stylesheet">
 	<!-- Custom styles for this template -->
    <link href="<c:url value='/assets/bootstrap/css/signin.css'/>" rel="stylesheet">

    <title>Calcula Comportamento Fila</title>

    


    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="<c:url value='/assets/bootstrap/js/ie-emulation-modes-warning.js'/>"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
<style>

.test + .tooltip > .tooltip-inner {
	      background-color: #0099FF; 
	      color: #FFFFFF; 
	      border: 1px solid #66FFFF; 
	      padding: 12px;
	      font-size: 18px;
	  }
	  
	#apps-bar{
		  width: 100%;
		  min-height: 40px;
		  position: fixed;
		  bottom: 0;
		  right: 0;
		  z-index: 99999995;
		  background: rgba(236,234,231,0.9);
		  border-top: 1px solid #b2b2b2;
		  box-shadow: 0 0 8px rgba(50,50,50,0.3);
		  -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#e5eceae7,endColorstr=#e5eceae7,GradientType=0)";
		  zoom: 1;
		  padding: 0 20px 0 4px;	  
	 }
	 #apps-bar-collapse{
		position: fixed;
	  	right: 0;
		bottom: 0;
		z-index: 99999997;
		border-left: 1px solid #fff;
		text-align: center;
		color: #fff;
		width: 25px;
		min-height: 40px;
		background: #cececd;
		display: table;
	}
	
	#apps-bar-collapse > span{
		display: table-cell;
	  	vertical-align: middle;
  	}
  	
  	.btn-primary{
  		margin:5px 25px 5px 5px;
  	}
  	
</style>
</head>

<body>
	
	<div class="container-fluid">
		<h2 class="form-signin-heading">Simulações Anteriores</h2>
	
		<div class="table-responsive">
			<table class="table">
				<thead>
			        <tr>		
			        			<th>#</th>
			        			<th>#</th>
					            <th><span class="test" data-toggle="tooltip" title="Distribuição de Chegada">A</span></th>
					            <th><span class="test" data-toggle="tooltip" title="Distribuição Atendimento">B</span></th>
					            <th><span class="test" data-toggle="tooltip" title="Disciplina de Atendimento">D</span></th>
					            <th><span class="test" data-toggle="tooltip" title="Numero Servidores">m</span></th>
					            <th><span class="test" data-toggle="tooltip" title="Tamanho População">n</span></th>
					            <th><span class="test" data-toggle="tooltip" title="Tempo">t</span></th>
					            <th><span class="glyphicon glyphicon-trash" data-toggle="tooltip" title="Excluir"></span></th>
					</tr>
				</thead>
			    <tbody>
			    	<c:if test="${simulacoes != null}">
						<c:forEach items="${simulacoes}" var="simulacao">
					        <tr >
					            <td data-toggle="collapse" data-target=".row${simulacao.idSimulacao}"><i class="glyphicon glyphicon-plus btn btn-default"></i></td>
					            <td ><a href="verSimulacao?idSimulacao=${simulacao.idSimulacao}" class="btn btn-default consultarSimulacao"><b>Visualizar</b></a></td>
								<td><span class="test" data-toggle="tooltip" title="${simulacao.nomeDistribuicaoChegada}">${simulacao.siglaDistribuicaoChegada}</span></td>
								<td><span class="test" data-toggle="tooltip" title="${simulacao.nomeDistribuicaoAtendimento}">${simulacao.siglaDistribuicaoAtendimento}</span></td>
								<td><span class="test" data-toggle="tooltip" title="${simulacao.nomeTipoFila}">${simulacao.siglaTipoFila}</span></td>
								<td>${simulacao.noServidores}</td>
								<td>${simulacao.noPopulacao}</td>
								<td>${simulacao.tempoTotal}</td>
								<td><a class="glyphicon glyphicon-trash consultarSimulacao" href="delete?idSimulacao=${simulacao.idSimulacao}"></a></td>
					        </tr>
					        
					        <tr class="collapse row${simulacao.idSimulacao}">
					        	<td></td>
					        	<td></td>
					            <td>
					            	<c:choose>
									    <c:when test="${simulacao.idTipoDistribuicaoChegada == '1' || simulacao.idTipoDistribuicaoChegada == '2' || simulacao.idTipoDistribuicaoChegada == '3'}">
									        <table class="table-responsive">
									       		<tr>
											        <th class="text-left" >Parametro</th>
											        <th class="text-right" >Valor</th>
									        	</tr>
									        <c:forEach var="i" begin="0" end="${fn:length(simulacao.parametrosChegada)}">
				            					<tr>
							            			<td class="text-left" >${simulacao.parametrosChegada[i].nomeParametro}</td>
							            			<td class="text-right" >${simulacao.parametrosChegada[i].valorParametro}</td>
							            		</tr>
											</c:forEach>
											</table>
									    </c:when>
									    
									    <c:otherwise>
									       <a href="planilhaDistribuicao?idSimulacao=${simulacao.idSimulacao}&tipoDistribuicao=C">planilha</a>
									    </c:otherwise>
									</c:choose>
					            </td>
					            <td>
					            	<c:choose>
									    <c:when test="${simulacao.idTipoDistribuicaoAtendimento == '1' || simulacao.idTipoDistribuicaoAtendimento == '2' || simulacao.idTipoDistribuicaoAtendimento == '3'}">
									        <table class="table-responsive">
									        	<tr>
											        <th>Parametro</th>
											        <th>Valor</th>
									        	</tr>
									        <c:forEach var="i" begin="0" end="${fn:length(simulacao.parametrosAtendimento)}">
				            					<tr>
							            			<td class="text-left" >${simulacao.parametrosAtendimento[i].nomeParametro}</td>
							            			<td class="text-right" >${simulacao.parametrosAtendimento[i].valorParametro}</td>
							            		</tr>
											</c:forEach>
											</table>
									    </c:when>
									    
									    <c:otherwise>
									       <a href="planilhaDistribuicao?idSimulacao=${simulacao.idSimulacao}&tipoDistribuicao=A">planilha</a>
									    </c:otherwise>
									</c:choose>
					            </td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
					        </tr>
						</c:forEach>
					</c:if>
			    </tbody>
			</table>
		</div>
		<div id="apps-bar-wrapper">
			<a id="apps-bar-collapse" style="height: 44px;">
				<span id="icone" class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			</a>
			<div id="apps-bar">
				<div class="row reverse">
					<div class="span9 apps-bar-controls text-right">		
						<a class="btn btn-primary" href="carregar">Nova Simulação</a>
					</div>
					
					
				</div>
			</div>
		</div>
	</div> 	
</body>
<script>
var myApp;
myApp = myApp || (function () {
    var pleaseWaitDiv = $('<div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false"><div class="modal-header"><h1>Processing...</h1></div><div class="modal-body"><div class="progress progress-striped active"><div class="bar" style="width: 100%;"></div></div></div></div>');
    return {
        showPleaseWait: function() {
            pleaseWaitDiv.modal();
        },
        hidePleaseWait: function () {
            pleaseWaitDiv.modal('hide');
        },

    };
})();



$(document).ready(function(){
	
	$( ".consultarSimulacao" ).click(function() {
		myApp.showPleaseWait();
	});
	
	$( "#apps-bar-collapse" ).click(function() {

		if( $("#apps-bar").is(':visible')){
			$('#apps-bar').hide(600);
			setTimeout("$( \"#icone\" ).removeClass( \"glyphicon glyphicon-chevron-right\" ).addClass( \"glyphicon glyphicon-chevron-left\" );", 600);
			
		}else{
		   $('#apps-bar').show(600);
		   setTimeout("$( \"#icone\" ).removeClass( \"glyphicon glyphicon-chevron-left\" ).addClass( \"glyphicon glyphicon-chevron-right\" );", 600);
		   
		}
			
		
	});
	
	$('.collapse').on('show.bs.collapse', function () {
	    $('.collapse.in').collapse('hide');
	});
	
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>


		
			

	
