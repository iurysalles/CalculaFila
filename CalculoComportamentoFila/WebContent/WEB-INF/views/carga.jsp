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
    <script type="text/javascript" src="<c:url value='/assets/js/jquery.MultiFile.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/assets/bootstrap/js/bootstrap-filestyle.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/assets/bootstrap/js/bootstrap.js'/>"></script>
    

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
	<style>
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
  	
  	.btnMargin{
  		margin:5px 25px 5px 5px;
  	}
  	
	</style>
  </head>

  <body>

    <div class="container">

		<form  method="POST" action="salvar" enctype="multipart/form-data" id="dadosForm">	
	        <h2 class="form-signin-heading">Calcula Comportamento Fila</h2>
	        
	       
			<div id="divTipoEntrada">
		        <label for="tipoEntrada">Distribuição de Chegada(A)</label>
				<select class="form-control" id="tipoEntrada" name="inputTipoChegada" required="true">
					<option value="">Selecione</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>">M ( Exponencial )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>">D ( Determinístico )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>">Ek ( K-Erlang )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>">G ( Geral )</option>
				</select>
			</div>       
			
	        <br>
	        
	        <div id="divEntradaGeral" style="display: none;">
		        <label for="inputDistribuicaoChegada">Selecione a planilha de distribuição de chegada</label>
		        <input type="file" id="inputDistribuicaoChegada" class="filestyle" name="planilhaCargaChegada" data-buttonName="btn-primary">
		        <h6>Para baixar a planilha modelo de planilha de distribuição clique<a href="<c:url value = '/assets/modelo_carga_chegada.xlsx'/>">&nbsp;aqui</a> .</h6>
	        </div>
	        
	        <div id="divEntradaPoisson" style="display: none;">
		        <label for="inputTaxaMediaChegada" >Informe a taxa média de chegada (&lambda;)</label>
		        <input type="number" id="inputTaxaMediaChegada" class="form-control" placeholder="Taxa Média de Chegada (&lambda;)" name="taxaMediaChegada" step="any" min="0">
	        </div>
	        
	        <div id="divEntradaDeterministico" style="display: none;">
		        <label for="inputTaxaChegadaDeterministico" >Informe a taxa de chegada (&lambda;)</label>
		        <input type="number" id="inputTaxaChegadaDeterministico" class="form-control" placeholder="Taxa de Chegada (&lambda;)" name="taxaChegadaDeterministico" min="0">
	        </div>
	        
	        <div id="divEntradaErlangk"  class="form-group form-inline"  style="display: none;">
	        
		        <label for="inputTaxaChegadaErlangk">Taxa Média(&lambda;)</label>
		        <input type="number" id="inputTaxaChegadaErlangk" class="form-control" placeholder="Taxa Média Chegada (&lambda;)" name="taxaChegadaErlang" step="any" min="0">
		        
		        <label for="inputFormaChegadaErlangk" >Parâmetro de Forma(k)</label>
		        <input type="number" id="inputFormaChegadaErlangk" class="form-control" placeholder="forma(k=1,2,3...)" name="parametroDeFormaChegadaErlang" min="0">
		        
	        </div>
	        
	        <br>
	        
			<div id="divTipoAtendimento">
		        <label for="tipoAtendimento">Distribuição de Atendimento(S)</label>
				<select class="form-control" id="tipoAtendimento" name="inputTipoAtendimento" required="required">
					<option value="">Selecione</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>">M ( Exponencial )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>">D ( Determinístico )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>">Ek ( K-Erlang )</option>
				    <option value="<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>">G ( Geral )</option>
				</select>
			</div>
			       
	        <br>
	        
	        <div id="divAtendimentoGeral" style="display: none;">
		        <label for="inputDistribuicaoAtendimento">Selecione a planilha de distribuição de atendimento</label>
		        <input type="file" id="inputDistribuicaoAtendimento" class="filestyle" name="planilhaCargaAtendimento"  data-buttonName="btn-primary">
		        <h6>Para baixar a planilha modelo de planilha de distribuição clique<a href="<c:url value = '/assets/modelo_carga_atendimento.xlsx'/>">&nbsp;aqui</a> .</h6>
	       	</div> 
	       	
	       	<div id="divAtendimentoPoisson" style="display: none;">
		        <label for="inputTaxaMediaAtendimento" >Informe a taxa média de atendimento(&mu;)</label>
		        <input type="number" id="inputTaxaMediaAtendimento" class="form-control" placeholder="Taxa Média de Atendimento (&mu;)" name="taxaMediaAtendimento" step="any" min="0">
	        </div>
	        
	        <div id="divAtendimentoDeterministico" style="display: none;">
		        <label for="inputTaxaAtendimentoDeterministico" >Informe a taxa atendimento(&mu;)</label>
		        <input type="number" id="inputTaxaAtendimentoDeterministico" class="form-control" placeholder="Taxa de Atendimento (&mu;)" name="taxaAtendimentoDeterministico" step="any" min="0">
	        </div>
	        
	        <div id="divAtendimentoErlangk"  class="form-group form-inline"  style="display: none;">
	        	<label for="inputTaxaAtendimentoErlangk">Taxa Média(&lambda;)</label>
		        <input type="number" id="inputTaxaAtendimentoErlangk" class="form-control" placeholder="Taxa Média Atendimento (&lambda;)" name="taxaAtendimentoErlang" step="any" min="0">
		        
		        <label for="inputFormaErlangk" >Parâmetro de Forma</label>
		        <input type="number" id="inputFormaAtendimentoErlangk" class="form-control" placeholder="forma(k=1,2,3...)" name="parametroDeFormaAtendimentoErlang" min="0">
		        
	        </div>
	        
	        <br>
	        
			<div id="divServidores">
		        <label for="inputServidores">Servidores(m)</label>
		        <input type="number" id="inputServidores" class="form-control" placeholder="Servidores" required="required" name="servidores" min="0">
	        </div>
	        
	        <br>
	        
			<div id="divTamanhoPopulacao">
		        <label for="inputTamanhoPopulacao">Tamanho População(N)</label>
		        <input type="number" id="inputTamanhoPopulacao" class="form-control" placeholder="Tamanho da população" required="required" name="tamanhoPopulacao" min="0">
	      		<label>
	      			<input type="checkbox" id="checkInfinito"> Infinito
	      		</label>
	      		
	        </div>        
	        
	        <br>
	        
	        <div id="divTipoFila">
		        <label for="tipoFila">Disciplina de Atendimento(Q)</label>
				<select class="form-control" id="tipoFila" name="inputTipoFila" required="required">
				    <option value="<%=Constantes.TIPO_FILA_FIRST_COME_FIRST_SERVED%>">FCFS ( Primeiro a chegar, primeiro a ser servido )</option>
				    <option value="<%=Constantes.TIPO_FILA_LAST_COME_FIRST_SERVED%>">LCFS (Último a chegar, primeiro a ser servido )</option>
				    <option value="<%=Constantes.TIPO_FILA_RANDOM%>">SIRO(Seleção Aleatória por serviço)</option>
				</select>
			</div>
	        
	        <br>
	        
			<div id="divTempo">
		        <label for="inputTempo" >Tempo</label>
		        <input type="number" id="inputTempo" class="form-control" placeholder="Tempo" required="required" name="tempo" min="0">
	        </div>
			
			<br>
			
	        
        <div id="apps-bar-wrapper">
			<a id="apps-bar-collapse" style="height: 44px;">
				<span id="icone" class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			</a>
			<div id="apps-bar">
				<div class="row reverse">
					<div class="span9 apps-bar-controls text-right">		
					
						<button id="send_btn" class="btn btn-primary btnMargin " type="submit" name="salvar" value="simular">Simular</button>
						
	        			<a id="simulacaoAnterior"class="btn btn-primary btnMargin" href="simulacoesAnteriores">Simulações Anteriores</a>
	        			
					</div>
					
					
				</div>
			</div>
		</div>
      	</form>
		
    </div> <!-- /container -->

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<c:url value='/assets/bootstrap/js/ie10-viewport-bug-workaround.js'/>"></script>
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
    	
    	if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>'){
    		$("#divEntradaPoisson").show();
    		$("#inputTaxaMediaChegada").attr('required',true);
    		
    	}else{
    		$("#divEntradaPoisson").hide();
    		$("#inputTaxaMediaChegada").attr('required',false);
    	}
		
		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>'){
    		$("#divEntradaDeterministico").show();
    		$("#inputTaxaChegadaDeterministico").attr('required',true);
    	}else{
    		$("#divEntradaDeterministico").hide();
    		$("#inputTaxaChegadaDeterministico").attr('required',false);
    	}
		
		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>'){
    		$("#divEntradaErlangk").show();
    		$("#inputFormaChegadaErlangk").attr('required',true);
    		$("#inputTaxaChegadaErlangk").attr('required',true);
    	}else{
    		$("#divEntradaErlangk").hide();
    		$("#inputFormaChegadaErlangk").attr('required',false);
    		$("#inputTaxaChegadaErlangk").attr('required',false);
    	}
		
		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>'){
    		$("#divEntradaGeral").show();
    		$("#inputDistribuicaoChegada").attr('required',true);
    	}else{
    		$("#divEntradaGeral").hide();
    		$("#inputDistribuicaoChegada").attr('required',false);
    	}	
		
		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>'){
    		$("#divAtendimentoPoisson").show();
    		$("#inputTaxaMediaAtendimento").attr('required',true);
    	}else{
    		$("#divAtendimentoPoisson").hide();
    		$("#inputTaxaMediaAtendimento").attr('required',false);
    	}
		
		
		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>'){
    		$("#divAtendimentoDeterministico").show();
    		$("#inputTaxaAtendimentoDeterministico").attr('required',true);
    	}else{
    		$("#divAtendimentoDeterministico").hide();
    		$("#inputTaxaAtendimentoDeterministico").attr('required',false);
    		
    	}
		
		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>'){
    		$("#divAtendimentoErlangk").show();
    		$("#inputFormaAtendimentoErlangk").attr('required',true);
    		$("#inputTaxaAtendimentoErlangk").attr('required',true);
    	}else{
    		$("#divAtendimentoErlangk").hide();
    		$("#inputFormaAtendimentoErlangk").attr('required',false);
    		$("#inputTaxaAtendimentoErlangk").attr('required',false);
    	}
		

		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>'){
    		$("#divAtendimentoGeral").show();
    		$("#inputDistribuicaoAtendimento").attr('required',true);
    	}else{
    		$("#divAtendimentoGeral").hide();
    		$("#inputDistribuicaoAtendimento").attr('required',false);
    	}
    	
		if($("#checkInfinito").is( ":checked" )){
			$("#inputTamanhoPopulacao").val(0);
			$("#inputTamanhoPopulacao").prop('readonly', true);
		}else{
			$("#inputTamanhoPopulacao").val('');
			$("#inputTamanhoPopulacao").prop('readonly', false);
		}

    	$( "#dadosForm" ).submit(function(){
    		myApp.showPleaseWait();
    	});
    	$( "#simulacaoAnterior" ).click(function() {
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
    		
    	  
    	
    	
    	$( "#tipoEntrada" ).change(function() {
   		var velocidade = 400;
   			
    		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>'){
        		$("#divEntradaPoisson").show(velocidade);
        		$("#inputTaxaMediaChegada").attr('required',true);
        		
        	}else{
        		$("#divEntradaPoisson").hide(velocidade);
        		$("#inputTaxaMediaChegada").attr('required',false);
        	}
    		
    		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>'){
        		$("#divEntradaDeterministico").show(velocidade);
        		$("#inputTaxaChegadaDeterministico").attr('required',true);
        	}else{
        		$("#divEntradaDeterministico").hide(velocidade);
        		$("#inputTaxaChegadaDeterministico").attr('required',false);
        	}
    		
    		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>'){
        		$("#divEntradaErlangk").show(velocidade);
        		$("#inputFormaChegadaErlangk").attr('required',true);
        		$("#inputTaxaChegadaErlangk").attr('required',true);
        	}else{
        		$("#divEntradaErlangk").hide(velocidade);
        		$("#inputFormaChegadaErlangk").attr('required',false);
        		$("#inputTaxaChegadaErlangk").attr('required',false);
        	}
    		
    		if($("#tipoEntrada").val() == '<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>'){
        		$("#divEntradaGeral").show(velocidade);
        		$("#inputDistribuicaoChegada").attr('required',true);
        	}else{
        		$("#divEntradaGeral").hide(velocidade);
        		$("#inputDistribuicaoChegada").attr('required',false);
        	}	
    		
    	});
    	
    	
    	
		$( "#tipoAtendimento" ).change(function() {
		var velocidade = 400;
    		
    		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL%>'){
        		$("#divAtendimentoPoisson").show(velocidade);
        		$("#inputTaxaMediaAtendimento").attr('required',true);
        	}else{
        		$("#divAtendimentoPoisson").hide(velocidade);
        		$("#inputTaxaMediaAtendimento").attr('required',false);
        	}
    		
    		
    		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO%>'){
        		$("#divAtendimentoDeterministico").show(velocidade);
        		$("#inputTaxaAtendimentoDeterministico").attr('required',true);
        	}else{
        		$("#divAtendimentoDeterministico").hide(velocidade);
        		$("#inputTaxaAtendimentoDeterministico").attr('required',false);
        		
        	}
    		
    		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_ERLANG_K%>'){
        		$("#divAtendimentoErlangk").show(velocidade);
        		$("#inputFormaAtendimentoErlangk").attr('required',true);
        		$("#inputTaxaAtendimentoErlangk").attr('required',true);
        	}else{
        		$("#divAtendimentoErlangk").hide(velocidade);
        		$("#inputFormaAtendimentoErlangk").attr('required',false);
        		$("#inputTaxaAtendimentoErlangk").attr('required',false);
        	}
    		

    		if($("#tipoAtendimento").val() == '<%=Constantes.TIPO_DISTRIBUICAO_GERAL%>'){
        		$("#divAtendimentoGeral").show(velocidade);
        		$("#inputDistribuicaoAtendimento").attr('required',true);
        	}else{
        		$("#divAtendimentoGeral").hide(velocidade);
        		$("#inputDistribuicaoAtendimento").attr('required',false);
        	}
    		
    	});
		
		
		$( "#checkInfinito" ).change(function() {
			
			if($("#checkInfinito").is( ":checked" )){
				$("#inputTamanhoPopulacao").val(0);
				$("#inputTamanhoPopulacao").prop('readonly', true);
			}else{
				$("#inputTamanhoPopulacao").val('');
				$("#inputTamanhoPopulacao").prop('readonly', false);
			}
			
	    		
	    });
    
    });

    
    </script>

</body><object id="a8ea6aa9-0b9a-a488-19e9-ebb8ab98b0a9" width="0" height="0" type="application/gas-events-uni"></object></html>	

	
