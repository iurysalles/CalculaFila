/*
 * 
 * Functions gerais tanto para tela de associação como para a nova associação
 * 
 */

	var idsUsuarios=new Array();
	var idsPerguntas=new Array();

	function salvarAssociacao(){
		$('input[name=idsUsuarios]').val(idsUsuarios);
		$('input[name=idsPerguntas]').val(idsPerguntas);
		$('#pesquisa').submit();
	}
	
	function buscarAjax(){
	$('.usuariosListados').remove();
			var nome =$("#nomeCompleto").val();
			$.getJSON( "pesquisarUsuario?nomeCompleto="+nome +"&idsUsuarios="+ idsUsuarios, function( data ) {
			var items = [];
			$.each( data, function( key, val ) {
				items.push( "<li id='" + key + "'>" + val + "</li>" );
				var row = '<tr class="usuariosListados usu_'+key+'"><td>'+key+'</td><td>'+val+'</td> <td><a class="btn btn-prime ico-left-small" href="javascript:setaUsuario('+'\''+key+'\''+','+'\''+operacao+'\''+')"> Selecionar</a></td></tr>';
				$('#search-result').append(row);
			});
		});
	}
	
	function buscarAjaxPergunta(){
		$('.perguntasListadas').remove();
			var nome =$("#nomePergunta").val();
			var idPesquisa = $('#sequencial').val();
			var action = "pesquisarPergunta?nomePergunta="+nome +"&idsPerguntas="+ idsPerguntas+"&idPesquisa="+idPesquisa;
			$.getJSON(action , function( data ) {
		    var items = [];
		    $.each( data, function( key, val ) {
		    	items.push( "<li id='" + key + "'>" + val + "</li>" );
		    	var name = Object.keys(val)[0];
		    	var descricao = val[name];
		    	var row = '<tr class="perguntasListadas per_'+key+'"><td>'+name+'</td><td>'+descricao+'</td> <td><a class="btn btn-prime ico-left-small" href="javascript:setaPergunta('+key+','+'\''+name+'\''+','+'\''+operacao+'\''+')"> Selecionar</a></td></tr>';
		    	$('#search-result-pergunta').append(row);
		    
		    });
		});
	}
	
	function setaUsuario(usuario, operacao){
 	  var row = '<tr><td><span name="usuariosSelecionado">'+usuario+'</span></td>';
	  if(operacao=="editar"){
		  row+='</tr>';
	  }else{
		  row+='<td><a href="#" onclick="excluirUsuario(\''+usuario+'\');">X</a></td></tr>';
	  }
      $('#usuarios').append(row);
      $('.usu_'+usuario).remove();
	  var index = idsUsuarios.indexOf(usuario);
	  if (index > -1) {
	     return;
	  };      
	  idsUsuarios.push(usuario);
	}
	
	function setaPergunta(id, name, operacao){
	  var row = '<tr id="per_'+id+'" class="perguntas"><td><span name="perguntasSelecionada">'+name+'</span></td>';
	  if(operacao=="editar"){
		  row+='</tr>';
	  }else{
		  row+='<td><a href="#" onclick="excluirPergunta('+id+');">X</a></td></tr>';
	  }
      $('#perguntas').append(row);
      $('.per_'+id).remove();
	  var index = idsPerguntas.indexOf(id);
	  if (index > -1) {
	     return;
	  };      
      idsPerguntas.push(id);
	}
	
	function excluirUsuario(usuario){
		var index = idsUsuarios.indexOf(usuario);
		if (index > -1) {
		    idsUsuarios.splice(index, 1);
		};
		$("span:contains('"+usuario+"')").parent().parent().remove();		
	}
	
	function excluirPergunta(id){
		var index = idsPerguntas.indexOf(id);
		if (index > -1) {
		    idsPerguntas.splice(index, 1);
		};
		$('#per_'+id+'.perguntas').remove();
	}
	
	function addUserSelected(){
	
		idsUsuarios = [];
		$('span[name=usuariosSelecionado]').each(function(){
		    idsUsuarios.push(this.innerHTML);
		});
		
	}
	
	function addPerguntaSelected(){
	
		idsPerguntas = [];
		$('span[name=perguntasSelecionada]').each(function(){
		    idsPerguntas.push(this.innerHTML);
		});
		
	}
	
/*
 * 
 * Functions específicas da tela de associação
 * 
 */
	
	function pesquisar(){
		var nome =$("#nomePesquisa").val();
		var paramPesquisa = $('input[name=paramPesquisa]:radio:checked').val();
		var action;
		
		if(paramPesquisa != 'usuario'){
			action = "pesquisarPergunta?nomePergunta=";
		}else{
			action = "pesquisarUsuario?nomeCompleto=";
		}
		
		$.getJSON( action+nome, function( data ) {
	  var items = [];
	  	$('.usuariosListados').remove();
		$('.perguntasListadas').remove();
	  $.each( data, function( key, val ) {
	    items.push( "<li id='" + key + "'>" + val + "</li>" );
	    var row;
	    if(paramPesquisa == 'usuario'){
	    	row = '<tr class="usuariosListados usu_'+key+'"><td>'+key+'</td><td>'+val+'</td> <td><a class="btn btn-prime ico-left-small" href="javascript:setaUsuarioPesquisa('+"'"+key+"'"+')"> Selecionar</a></td></tr>';
	    }else{
		    var name = Object.keys(val)[0];
		    var descricao = val[name];		    
	    	row = '<tr class="perguntasListadas per_'+key+'"><td>'+name+'</td><td>'+descricao+'</td> <td><a class="btn btn-prime ico-left-small" href="javascript:setaPerguntaPesquisa('+"'"+key+"'"+','+'\''+name+'\''+')"> Selecionar</a></td></tr>';
	    }
        $('#search-result-pesquisa').append(row);
	    
	  });	
		});
	
	}
	
	function setaUsuarioPesquisa(usuario){
	  $('#modalPergunta').show();
	  $('#modalUsuario').hide();
	  $('#salvarAssociacao').show();	  	
	  limparPesquisa();	
	  $('#include_modal_pesquisar .modal-btn-close').click();
	  var index = idsUsuarios.indexOf(usuario);
	  if (index > -1) {
	     return;
	  };    
	  var row = '<tr><td><span name="usuariosSelecionado">'+usuario+'</span></td></tr>';
	  $('#usuarios').append(row);
	  idsUsuarios.push(usuario);
	  var action = "obterPerguntasPorUsuarios?idsUsuarios="+idsUsuarios;
	
	  $.getJSON( action, function( data ) {
	      $.each( data, function( key, val ) {
	      	  var index = idsPerguntas.indexOf(key);
			  if (index > -1) {
			     return;
			  };    
			 idsPerguntas.push(key);
			 var name = Object.keys(val)[0];
			 var row = '<tr id="per_'+key+'" class="perguntas"><td><span name="perguntasSelecionada">'+name+'</span></td></tr>';
		 $('#perguntas').append(row);
	     });	
	 });
	}
	
	function setaPerguntaPesquisa(id, name){
	  $('#modalUsuario').show();
	  $('#modalPergunta').hide();
	  $('#salvarAssociacao').show();		  
	  limparPesquisa();	
	  $('#include_modal_pesquisar .modal-btn-close').click();
	  var index = idsPerguntas.indexOf(id);
	  if (index > -1) {
	     return;
	  };    
	  var row = '<tr><td><span name="perguntasSelecionada">'+name+'</span></td></tr>';
	  $('#perguntas').append(row);
	  idsPerguntas.push(id);
	  var action = "obterUsuariosPorPerguntas?idsPerguntas="+idsPerguntas;
	
	  $.getJSON( action, function( data ) {
	      $.each( data, function( key, val ) {
	      	  var index = idsUsuarios.indexOf(key);
			  if (index > -1) {
			     return;
			  };    
			 idsUsuarios.push(key);
			 var row = '<tr id="usu_'+key+'" class="usuarios"><td><span name="usuariosSelecionado">'+key+'</span></td></tr>';
		 $('#usuarios').append(row);
	     });	
	 });	
	  
	}
	
	 function limparPesquisa(){
	    idsUsuarios=new Array();
	    idsPerguntas=new Array();
	 	$('span[name=perguntasSelecionada]').parent().parent().remove();
	 	$('span[name=usuariosSelecionado]').parent().parent().remove();
	 } 
	
	 function novaAssociacao(){
		$('#pesquisa').get(0).setAttribute('action','selecionar');
	$('#pesquisa').submit();	 
	 }
	 
     function closeModal(path) {
         jQuery.noConflict();
         (function ($) {
             $('#'+path+'').modal('show');
         }
         )(jQuery);

     }
     
 /*
  * 
  * Functions para salvar resposta 
  * 
  */
     
     function salvarEnviarResposta(param){
    	 var path = $('#contextPath').val();
    	 var json = createJSONRespostas();
         $.ajax({
             url: path + "/resposta/atualizarRespostas",
             type: 'GET',
             dataType: 'json',
             data: "jsonRespostasPerguntas="+json+"&param="+param,
             beforeSend: function() {
             },
             success: function(data, textStatus, xhr) {
            	 $("#mensagem").html('<div class="span12"><p class="msg msg-success close"><span>'+data+'</span></p></div>');
             },
             error: function(xhr, textStatus, errorThrown) {
                 console.log("erro!");
             }
         });	
    	 
     }
     
     function createJSONRespostas(){
    	     	 
    	 jsonObj = [];    	 
    	 $('.perguntas').each(function(){
    		
    		 var id = $(this).attr('id');
    		 var resposta = $('#resp_'+id).val();
    		 if(resposta==undefined || resposta.trim() == ""){
    			 return;
    		 }
        	 item = {};
        	 item ['id'] = id;
        	 item ['resposta'] = resposta;
        	 
        	 jsonObj.push(item);
        	 
    	 });
    	 
    	 var jsonRespostas = JSON.stringify(jsonObj);
    	 return jsonRespostas;
    	 
     }
	