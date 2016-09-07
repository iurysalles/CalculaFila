<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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
	<script type="text/javascript" src="<c:url value='/assets/js/MeuChart.js'/>"></script>
    
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
	  
	  .test{
	      min-width: 50px; 
	  }
	  
	.estatistica{
	  font-size: 20px;
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
  	
  	#icone{
  		cursor: pointer;
  	}
    
  </style>
</head>

  <body>
<div class="container-fluid">
   <form  method="POST" action="gravarResultado" id="dadosForm">	
		<input type="hidden" name="inputTipoChegada" value="${inputGravar.inputTipoChegada}">
		<input type="hidden" name="taxaMediaChegada" value="${inputGravar.taxaMediaChegada}">
		<input type="hidden" name="taxaChegadaDeterministico" value="${inputGravar.taxaChegadaDeterministico}">
		<input type="hidden" name="parametroDeFormaChegadaErlang" value="${inputGravar.parametroDeFormaChegadaErlang}">
		<input type="hidden" name="taxaChegadaErlang" value="${inputGravar.taxaChegadaErlang}">
		<input type="hidden" name="inputTipoAtendimento" value="${inputGravar.inputTipoAtendimento}">
		<input type="hidden" name="taxaMediaAtendimento" value="${inputGravar.taxaMediaAtendimento}">
		<input type="hidden" name="taxaAtendimentoDeterministico" value="${inputGravar.taxaAtendimentoDeterministico}">
		<input type="hidden" name="parametroDeFormaAtendimentoErlang" value="${inputGravar.parametroDeFormaAtendimentoErlang}">
		<input type="hidden" name="taxaAtendimentoErlang" value="${inputGravar.taxaAtendimentoErlang}">
		<input type="hidden" name="tamanhoPopulacao" value="${inputGravar.tamanhoPopulacao}">
		<input type="hidden" name="inputTipoFila" value="${inputGravar.inputTipoFila}">
		<input type="hidden" name="servidores" value="${inputGravar.servidores}">
		<input type="hidden" name="tempo" value="${inputGravar.tempo}">
		
		
		<input type="hidden" name="tamanhoMedioClientesSistema" value="${tamanhoMedioClientesSistema.media}">
		<input type="hidden" name="varianciaTamanhoMedioClientesSistema" value="${tamanhoMedioClientesSistema.variancia}">
		
		<input type="hidden" name="tamanhoMedioFila" value="${tamanhoMedioFila.media}">
		<input type="hidden" name="varianciatamanhoMedioFila" value="${tamanhoMedioFila.variancia}">
		
		<input type="hidden" name="tempoMedioSistema" value="${tempoMedioSistema.media}">
		<input type="hidden" name="varianciatempoMedioSistema" value="${tempoMedioSistema.variancia}">
		
		<input type="hidden" name="tempoMedioEspera" value="${tempoMedioEspera.media}">
		<input type="hidden" name="varianciatempoMedioEspera" value="${tempoMedioEspera.variancia}">

		<c:if  test="${distribuicaoChegada !=  null}">
			<c:forEach items="${distribuicaoChegada}" var="distribuicao">
			
				<input type="hidden" value="${distribuicao.probabilidadeAcumulada}" name ="valor_tempo_chegada_${distribuicao.tempo}">	
				
			</c:forEach>
		</c:if>
		
		<c:if  test="${distribuicaoAtendimento !=  null}">
			<c:forEach items="${distribuicaoAtendimento}" var="distribuicao">
			
				<input type="hidden" value="${distribuicao.probabilidadeAcumulada}" name ="valor_tempo_atendimento_${distribuicao.tempo}">	
				
			</c:forEach>
		</c:if>


			<!-- Escrevendo valores Pessoas X Tempo		 -->	
		<c:if test="${filaInstante != null}">
			<c:forEach items="${filaInstante}" var="fila">
				
				<input type="hidden" class="grafico" id="tempo_${fila.tempo}" value="${fila.tamanho}" name ="tempo_${fila.tempo}">
		
			</c:forEach>
		</c:if>
		
		<!-- Escrevendo valores waitingTime X Tempo		 -->	
		<c:if test="${waitingTime != null}">
			<c:forEach items="${waitingTime}" var="wt">
				
				<input type="hidden" class="graficowt" id="tempowt_${wt.tempo}" value="${wt.mediaTempoEspera}">
		
			</c:forEach>
		</c:if>
		
		<!-- Escrevendo valores serverUtilization X Tempo		 -->
		<c:if test="${serverUtilization != null}">
			<c:forEach items="${serverUtilization}" var="su">
				
				<input type="hidden" class="graficosu" id="temposu_${su.tempo}" value="${su.percentual}">
		
			</c:forEach>
		</c:if>
		
		
		<h1>Resultado Simulação</h1>
		<!-- Gráfico Pessoas X Tempo		 -->	
		<div style="width: 100%;">
			<h3 class="text-center bg-primary">Tempo : Fila</h3>
			<canvas id="graph" ></canvas>
		</div>
		
		<br><br><br>
		
		<!-- Gráfico  Waiting Time X Tempo		 -->	
		<div style="width: 100%;">
			<h3 class="text-center bg-primary">Tempo : Tempo de Espera</h3>
			<canvas id="graphwt" ></canvas>
		</div>
		
		<br><br><br>
		
		<!-- Gráfico  server Utilization X Tempo		 -->	
		<div style="width: 100%;">
			<h3 class="text-center bg-primary">Tempo : Ocupação dos Atendentes</h3>
			<canvas id="graphsu" ></canvas>
		</div>
		
	
 	

		<div class="container">
		
				<div class="row reverse">
					 <h2>Resultados:</h2>
					<div class="table-responsive">
					
						
						<table class="table table-striped">
							<tr>
								<th class="text-center">#</th>
								<th class="text-center">Média</th>
								<th class="text-center">Variância</th>
								<th class="text-center">Desvio Padrão</th>
							</tr>
							<tr>
								<td class="text-center"><span data-toggle="tooltip" title="Média de clientes no sistema" class="test btn btn-default text-center"><b>L</b></span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioClientesSistema.mediaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioClientesSistema.varianciaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioClientesSistema.desvioPadraoFormatado}</span></td>
							</tr>
							
							<tr>
								<td class="text-center"><span data-toggle="tooltip" title="Média de clientes da fila" class="test btn btn-default"><b>Lq</b></span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioFila.mediaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioFila.varianciaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tamanhoMedioFila.desvioPadraoFormatado}</span></td>
							</tr>
							
							<tr>
								<td class="text-center"><span data-toggle="tooltip" title="Tempo Médio do cliente no sistema" class="test btn btn-default"><b>W</b></span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioSistema.mediaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioSistema.varianciaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioSistema.desvioPadraoFormatado}</span></td>
							</tr>
							
							<tr>
								<td class="text-center"><span data-toggle="tooltip" title="Tempo Médio de Espera do cliente" class="test btn btn-default" data-placement="top"><b>Wq</b></span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioEspera.mediaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioEspera.varianciaFormatado}</span></td>
								<td class="text-center"><span class="estatistica">${tempoMedioEspera.desvioPadraoFormatado}</span></td>
							</tr>
						</table>	
				</div>
			
					<h2>Log Simulação Fila</h2>
					<%int i=0; %>
					<c:if test="${mensagem != null}">
				
						<c:if test="${mensagemAviso != null}">
				
							<c:forEach items="${mensagemAviso}" var="ocorrencia">
								
								<input type="hidden" value="${ocorrencia.posicao}" name ="cliente_posicao_<%=i%>">
								<input type="hidden" value="${ocorrencia.horaChegada}" name ="cliente_hora_chegada_<%=i%>">
								<input type="hidden" value="${ocorrencia.tempoAtendimento}" name ="cliente_tempo_atendimento_<%=i%>">
								<input type="hidden" value="${ocorrencia.horaSaida}" name ="cliente_hora_saida_<%=i%>">
								<input type="hidden" value="${ocorrencia.atendente}" name ="cliente_atendente_<%=i%>">
								<% i++;%>
				
								<p class="bg-info"><b>Cliente: ${ocorrencia.posicao}</b>
								<p>Hora Chegada: ${ocorrencia.horaChegada}
							 	<p>Hora Atendimento: ${ocorrencia.horaAtendimento} 
								<p>Tempo Atendimento: ${ocorrencia.tempoAtendimento} 
								<p>Hora Saida: ${ocorrencia.horaSaida} 
								<p>Tempo Espera: ${ocorrencia.tempoEspera} 
								<p>Tempo Fila: ${ocorrencia.tempoFila} 
								<p>Atendente: ${ocorrencia.atendente}
								<p>Taxa Atendimento: ${ocorrencia.taxaAtendimento}
							
							</c:forEach>
						</c:if>
					</c:if>
					
					
					<div id="apps-bar-wrapper">
						<a id="apps-bar-collapse" style="height: 44px;">
							<span id="icone" class="glyphicon glyphicon-chevron-right" ></span>
						</a>
						<div id="apps-bar">
							<div class="row reverse">
								<div class="span9 apps-bar-controls text-right">
									<c:if test="${consulta == null}">
										<button id="send_btn" class="wait btn btn-primary " type="submit" name="gravarResultado" value="gravarResultado">Gravar Resultado</button>
									</c:if>
									<c:if test="${consulta != null}">
										<a class="wait btn btn-primary" href="simulacoesAnteriores">Simulações Anteriores</a>
									</c:if>
									<a class="btn btn-primary" href="carregar">Nova Simulação</a>
								</div>
								
								
							</div>
						</div>
					</div>

				
					
				</div>
			
		</div>
			
	
	</form>
</div> 
	
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
	
	

	$( ".wait" ).click(function() {
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
    $('[data-toggle="tooltip"]').tooltip(); 
});
//INÍCIO -- Script gráfico Pessoas X tempo
var myDataLabels = new Array();
var myData = new Array();

$(".grafico").each(function( index ) {
	tempo = $(this).attr('id').substring(6);
	myDataLabels.push([String(tempo)]);
	myData.push([parseInt($(this).val())]);
	
});

var barChartData = {
	labels : myDataLabels,
	datasets : [
		
		{
			fillColor : "rgba(151,187,205,0.5)",
			strokeColor : "rgba(151,187,205,0.8)",
			highlightFill : "rgba(151,187,205,0.75)",
			highlightStroke : "rgba(151,187,205,1)",
			data : myData
		}
	]

}
//FIM -- Script gráfico Pessoas X tempo

//INÍCIO -- Script gráfico waitingTime X Tempo
var waitingTimeDataLabels = new Array();
var waitingTimeData = new Array();

$(".graficowt").each(function( index ) {
	wttempo = $(this).attr('id').substring(8);
	waitingTimeDataLabels.push([String(wttempo)]);
	waitingTimeData.push([parseInt($(this).val())]);
});

var barChartDataWt = {
		labels : waitingTimeDataLabels,
		datasets : [
			
			{
				fillColor : "rgba(151,187,205,0.5)",
				strokeColor : "rgba(151,187,205,0.8)",
				highlightFill : "rgba(151,187,205,0.75)",
				highlightStroke : "rgba(151,187,205,1)",
				data : waitingTimeData
			}
		]

}
//FIM -- Script gráfico waitingTime X Tempo

//INÍCIO -- Script gráfico Server Utilization X Tempo
var serverUtilizationDataLabels = new Array();
var serverUtilizationData = new Array();

$(".graficosu").each(function( index ) {
	sutempo = $(this).attr('id').substring(8);
	serverUtilizationDataLabels.push([String(sutempo)]);
	serverUtilizationData.push([parseInt($(this).val())]);
});

var barChartDataSu = {
		labels : serverUtilizationDataLabels,
		datasets : [
			
			{
				fillColor : "rgba(151,187,205,0.5)",
				strokeColor : "rgba(151,187,205,0.8)",
				highlightFill : "rgba(151,187,205,0.75)",
				highlightStroke : "rgba(151,187,205,1)",
				data : serverUtilizationData
			}
		]

}
//FIM -- Script gráfico Server Utilization X Tempo

window.onload = function(){
	
	var ctx = document.getElementById("graph").getContext("2d");
	window.myBar = new Chart(ctx).Bar(barChartData, {
		responsive : true,
		showXLabels: 50
	});
	
	var ctxwt = document.getElementById("graphwt").getContext("2d");
	window.myBar = new Chart(ctxwt).Bar(barChartDataWt, {
		responsive : true,
		showXLabels: 50
	});
	
	var ctxsu = document.getElementById("graphsu").getContext("2d");
	window.myBar = new Chart(ctxsu).Bar(barChartDataSu, {
		responsive : true,
		showXLabels: 50
	});
}



</script>


		
			

	
