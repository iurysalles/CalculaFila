package br.com.spring.controllers;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.spring.dominio.Cliente;
import br.com.spring.dominio.Distribuicao;
import br.com.spring.dominio.Estatistica;
import br.com.spring.dominio.Fila;
import br.com.spring.dominio.InputSimular;
import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.ServerUtilization;
import br.com.spring.dominio.Simulacao;
import br.com.spring.dominio.WaitingTime;
import br.com.spring.servicos.ServicosDeCalculoComportamentoFila;
import br.com.spring.servicos.ServicosDeDados;
import br.com.spring.util.Constantes;
import br.com.spring.util.Util;

@Controller
public class HomeController {
	DecimalFormat fmt = new DecimalFormat("0.0000");
	
	@Autowired
	private ServicosDeCalculoComportamentoFila servicos;
	@Autowired
	private ServicosDeDados servicosDados;


	@RequestMapping(value = "/carregar", method = RequestMethod.GET)
	public ModelAndView carregar(Model model) {
		
		// TODO começar o sistema
		return new ModelAndView("carga", "command", new InputSimular());
	}
	
	@RequestMapping(value = "/verSimulacao", method = RequestMethod.GET)
	public String verSimulacao(Model model, Simulacao simForm) throws Exception {
		Simulacao simulacao = servicosDados.getSimulacao(simForm.getIdSimulacao());
		
		Collection<Cliente> clientesFinal = null;
		Collection<Fila> filaPorTempo = null;
		Collection<WaitingTime> tempoEspera = null;
		Collection<ServerUtilization> serverUtilization = null;
		Estatistica tempoMedioEspera = null;
		Estatistica tempoMedioSistema = null;
		Estatistica tamanhoMedioFila = null;
		Estatistica tamanhoMedioClientesSistema = null;
		
		clientesFinal = toCollectionCliente(simulacao.getClientes());
		filaPorTempo = servicos.calculaClientesNaFilaPorTempo(clientesFinal);
		tempoEspera = servicos.calculaMediaTempoEsperaPorHoraChegada(clientesFinal);
		serverUtilization = servicos.serverUtilization(clientesFinal, simulacao.getNoServidores());
		
		for(int i = 0; i<simulacao.getEstatisticas().length; i++){
			if(simulacao.getEstatisticas()[i].getCodigoEstatistica() == Constantes.TIPO_MEDIA_L)
				tamanhoMedioClientesSistema = simulacao.getEstatisticas()[i];
			if(simulacao.getEstatisticas()[i].getCodigoEstatistica() == Constantes.TIPO_MEDIA_LQ)
				tamanhoMedioFila = simulacao.getEstatisticas()[i];
			if(simulacao.getEstatisticas()[i].getCodigoEstatistica() == Constantes.TIPO_MEDIA_W)
				tempoMedioSistema = simulacao.getEstatisticas()[i];
			if(simulacao.getEstatisticas()[i].getCodigoEstatistica() == Constantes.TIPO_MEDIA_WQ)
				tempoMedioEspera = simulacao.getEstatisticas()[i];
				
		}

		model.addAttribute("mensagemAviso",clientesFinal); 
		model.addAttribute("mensagem","mensagem"); 
		model.addAttribute("waitingTime",tempoEspera); 
		model.addAttribute("filaInstante", filaPorTempo);
		model.addAttribute("serverUtilization", serverUtilization);
		
		model.addAttribute("tamanhoMedioClientesSistema", tamanhoMedioClientesSistema);
		model.addAttribute("tamanhoMedioFila", tamanhoMedioFila);
		
		model.addAttribute("tempoMedioSistema",tempoMedioSistema); 
		model.addAttribute("tempoMedioEspera",tempoMedioEspera); 
		
		model.addAttribute("consulta","consulta");
		
		return "resultadoCarga";
	}
	
	@RequestMapping(value = "/gravarResultado", method = RequestMethod.POST)
	public String gravarResultado(Model model, InputSimular inputSimular, HttpServletRequest request) throws SQLException {
		try{
			Simulacao simulacao = transformaEmSimulacao(inputSimular, request);
			servicosDados.gravaSimulacao(simulacao);
			Collection<Simulacao> simulacoes =servicosDados.listaSimulacoes();
			model.addAttribute("simulacoes",simulacoes); 
			return "simulacoesAnteriores";
		}
		catch (NumberFormatException e) {
				e.printStackTrace();
				return "erro";
		}catch (NullPointerException e) {
			e.printStackTrace();
			return "erro";
		}
	
		
	}
	
	@RequestMapping(value = "/simulacoesAnteriores", method = RequestMethod.GET)
	public String simulacoesAnteriores(Model model, HttpServletRequest request) throws SQLException {
		Collection<Simulacao> simulacoes =servicosDados.listaSimulacoes();
		model.addAttribute("simulacoes",simulacoes); 
		
		return "simulacoesAnteriores";
	}
	
	
	@RequestMapping(value = "/planilhaDistribuicao", method = RequestMethod.GET)
	public ModelAndView export(Model model, HttpServletRequest request) throws SQLException {
		
		String idSimulacao = request.getParameter("idSimulacao");
		String tipoDistribuicao = request.getParameter("tipoDistribuicao");
		
		List<Parametro> parametros = (List<Parametro>) servicosDados.getDistribuicao(idSimulacao, tipoDistribuicao);
		
		return new ModelAndView("ExcelHelperView", "parametros", parametros);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Model model, HttpServletRequest request) throws SQLException {	
		String idSimulacao = request.getParameter("idSimulacao");
		servicosDados.deleteSimulacao(Integer.valueOf(idSimulacao));
		Collection<Simulacao> simulacoes =servicosDados.listaSimulacoes();
		model.addAttribute("simulacoes",simulacoes); 
		return "simulacoesAnteriores";
	}
	   
	
	/**
	 * Método responsável por calcular a simulação
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public String salvar(@ModelAttribute("planilhaCarga")  InputSimular inputSimular,Model model) throws Exception {
		Collection<String> resultadoChegada = new ArrayList<String>();
		Collection<String> resultadoTempoAtendimento = new ArrayList<String>();
		Collection<Cliente> clientesFinal = null;
		Collection<Fila> filaPorTempo = null;
		Collection<Fila> clientesSistemaPorTempo = null;
		Collection<WaitingTime> tempoEspera = null;
		Collection<ServerUtilization> serverUtilization = null;
		Collection<Distribuicao> distribuicaoChegada = null;
		Collection<Distribuicao> distribuicaoAtendimento = null;
		Estatistica tempoMedioEspera;
		Estatistica tempoMedioSistema;
		Estatistica tamanhoMedioFila;
		Estatistica tamanhoMedioClientesSistema;
		Collection<Cliente> clientes;
			try {
				
				// Calculando chegada
				if (inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL) {
					
					distribuicaoChegada = servicos.calculaDistribuicaoExponencial(inputSimular.getTaxaMediaChegada());
					
				}else
				
				if (inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO) {
					
					distribuicaoChegada = servicos.criaDistribuicaoDeterministico(1.0/inputSimular.getTaxaChegadaDeterministico());
					
				}else
					
				if (inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K) {
					
					distribuicaoChegada = servicos.calculaDistribuicaoErlangk(inputSimular.getTaxaChegadaErlang(),inputSimular.getParametroDeFormaChegadaErlang());
					
				}else
	
				if (inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_GERAL) {
					
					distribuicaoChegada = servicos.planilhaToDistribuicao(inputSimular.getPlanilhaCargaChegada());
					model.addAttribute("distribuicaoChegada",distribuicaoChegada); 
				}	

			
				clientes = servicos.criaFilaChegada(distribuicaoChegada, inputSimular.getTempo(), inputSimular.getTamanhoPopulacao());
				
				//Calculando Atendimento
				if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL){
					
					distribuicaoAtendimento = servicos.calculaDistribuicaoExponencial(inputSimular.getTaxaMediaAtendimento());
					
				}else
					
				if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO){
					
					distribuicaoAtendimento = servicos.criaDistribuicaoDeterministico(1.0/inputSimular.getTaxaAtendimentoDeterministico());
					
				}else 
					
				if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K){
				
					distribuicaoAtendimento = servicos.calculaDistribuicaoErlangk(inputSimular.getTaxaAtendimentoErlang(), inputSimular.getParametroDeFormaAtendimentoErlang());
					
				}else 
	
				if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_GERAL){
					
					distribuicaoAtendimento = servicos.planilhaToDistribuicao(inputSimular.getPlanilhaCargaAtendimento());
					model.addAttribute("distribuicaoAtendimento",distribuicaoAtendimento); 
				}
				
				resultadoTempoAtendimento = servicos.simulaResultadoDistribuicao(distribuicaoAtendimento,  clientes.size());
				
				
				//Calculando Resultado Final (Chegada X Atendimento X servidores)
				if(inputSimular.getInputTipoFila() == Constantes.TIPO_FILA_FIRST_COME_FIRST_SERVED){
					
					clientesFinal = servicos.calculaAtendimentoFila(clientes, resultadoTempoAtendimento, inputSimular.getServidores());
					
				}else if(inputSimular.getInputTipoFila() == Constantes.TIPO_FILA_LAST_COME_FIRST_SERVED){
					
					clientesFinal = servicos.calculaAtendimentoFilaLastComeFirstServed(clientes, resultadoTempoAtendimento, inputSimular.getServidores());
					
				}else if(inputSimular.getInputTipoFila() == Constantes.TIPO_FILA_RANDOM){
					
					clientesFinal = servicos.calculaAtendimentoFilaRandom(clientes, resultadoTempoAtendimento, inputSimular.getServidores());
					
				}
				
				filaPorTempo = servicos.calculaClientesNaFilaPorTempo(clientes);
				clientesSistemaPorTempo = servicos.calculaClientesNoSistemaPorTempo(clientes);
				
				tempoEspera = servicos.calculaMediaTempoEsperaPorHoraChegada(clientes);
			
				serverUtilization = servicos.serverUtilization(clientes, inputSimular.getServidores());
				
				tamanhoMedioClientesSistema = servicos.calculaEstatisticaTamanhoFila(clientesSistemaPorTempo);
				tamanhoMedioFila = servicos.calculaEstatisticaTamanhoFila(filaPorTempo);
				
				tempoMedioSistema = servicos.calculaEstatisticaTempoSistema(clientes);
				tempoMedioEspera = servicos.calculaEstatisticaTempoEspera(clientes);
			 
				
			} catch (Exception e) {
				
				if(resultadoChegada.iterator().hasNext() && !Util.isNumeroTry(resultadoChegada.iterator().next()))
					model.addAttribute("avisoResultadoChegada",resultadoChegada); 
				
				model.addAttribute("avisoResultadoTempoAtendimento",resultadoTempoAtendimento); 
				e.printStackTrace();
				return "resultadoErro";
			}
			if(clientes != null && !clientes.isEmpty()){
				model.addAttribute("inputGravar",inputSimular); 
				model.addAttribute("mensagemAviso",clientesFinal); 
				model.addAttribute("waitingTime",tempoEspera); 
				model.addAttribute("filaInstante", filaPorTempo);
				model.addAttribute("serverUtilization", serverUtilization);
				
				model.addAttribute("tamanhoMedioClientesSistema", tamanhoMedioClientesSistema);
				model.addAttribute("tamanhoMedioFila", tamanhoMedioFila);
				
				model.addAttribute("tempoMedioSistema",tempoMedioSistema); 
				model.addAttribute("tempoMedioEspera",tempoMedioEspera); 
				model.addAttribute("consulta",null);
				
			}

			model.addAttribute("mensagem","sucesso");


		return "resultadoCarga";
	}
	
	
	
	private Simulacao transformaEmSimulacao(InputSimular inputSimular, HttpServletRequest request) throws NumberFormatException, NullPointerException{
		Simulacao simulacao = new Simulacao();
		Estatistica[] estatisticas = new Estatistica[4];
		Parametro[] parametrosChegada = null;
		Parametro[] parametrosAtendimento = null;
		Parametro parametro;
		
		
		double mediaClientesSistema           = Double.valueOf(request.getParameter("tamanhoMedioClientesSistema"));
		double varianciaTamanhoMedioClientesSistema  = Double.valueOf(request.getParameter("varianciaTamanhoMedioClientesSistema"));
		double mediaFila                      = Double.valueOf(request.getParameter("tamanhoMedioFila"));
		double varianciatamanhoMedioFila             = Double.valueOf(request.getParameter("varianciatamanhoMedioFila"));
		double mediaTempoSistema                     = Double.valueOf(request.getParameter("tempoMedioSistema"));
		double varianciatempoMedioSistema            = Double.valueOf(request.getParameter("varianciatempoMedioSistema"));
		double mediaTempoEspera                      = Double.valueOf(request.getParameter("tempoMedioEspera"));
		double varianciatempoMedioEspera             = Double.valueOf(request.getParameter("varianciatempoMedioEspera"));
		
		
		
		Estatistica tamanhoMedioClientesSistema = new Estatistica(Constantes.TIPO_MEDIA_L, mediaClientesSistema, varianciaTamanhoMedioClientesSistema);
		Estatistica tamanhoMedioFila = new Estatistica(Constantes.TIPO_MEDIA_LQ, mediaFila, varianciatamanhoMedioFila);
		Estatistica tempoMedioSistema = new Estatistica(Constantes.TIPO_MEDIA_W, mediaTempoSistema, varianciatempoMedioSistema);
		Estatistica tempoMedioEspera = new Estatistica(Constantes.TIPO_MEDIA_WQ, mediaTempoEspera, varianciatempoMedioEspera);
		estatisticas[0] = tamanhoMedioClientesSistema;
		estatisticas[1] = tamanhoMedioFila;
		estatisticas[2] = tempoMedioSistema;
		estatisticas[3] = tempoMedioEspera;
		 
	    
		if(inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL){
			parametrosChegada = new Parametro[1];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_EXPONENCIAL_LAMBDA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaMediaChegada());
			parametrosChegada[0] = parametro;
		}else if(inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO){
			parametrosChegada = new Parametro[1];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_DETERMINISTICO_LAMBDA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaChegadaDeterministico());
			parametrosChegada[0] = parametro;
		}else if(inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K){
			parametrosChegada = new Parametro[2];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_ERLANG_K_THETA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaChegadaErlang());
			parametrosChegada[0] = parametro;
			
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_ERLANG_K_FORMA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getParametroDeFormaChegadaErlang());
			parametrosChegada[1] = parametro;
		}else if(inputSimular.getInputTipoChegada() == Constantes.TIPO_DISTRIBUICAO_GERAL){
			parametrosChegada =  getDistribuicaoGeral(request,Constantes.NOME_PARAMETRO_CHEGADA);
		}

		if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL){
			parametrosAtendimento = new Parametro[1];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_EXPONENCIAL_LAMBDA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaMediaAtendimento());
			parametrosAtendimento[0] = parametro;
		}else if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO){
			parametrosAtendimento = new Parametro[1];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_DETERMINISTICO_LAMBDA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaAtendimentoDeterministico());
			parametrosAtendimento[0] = parametro;
		}else if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K){
			parametrosAtendimento = new Parametro[2];
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_ERLANG_K_THETA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getTaxaAtendimentoErlang());
			parametrosAtendimento[0] = parametro;
			
			parametro = new Parametro(Constantes.TIPO_PARAMETRO_ERLANG_K_FORMA, Constantes.TIPO_DISTRIBUICAO_CHEGADA, inputSimular.getParametroDeFormaAtendimentoErlang());
			parametrosAtendimento[1] = parametro;
		}else if(inputSimular.getInputTipoAtendimento() == Constantes.TIPO_DISTRIBUICAO_GERAL){
			parametrosAtendimento = getDistribuicaoGeral(request,Constantes.NOME_PARAMETRO_ATENDIMENTO);
		}
		
		simulacao.setClientes(getClientes(request));
		
		simulacao.setIdTipoDistribuicaoChegada(inputSimular.getInputTipoChegada());
		simulacao.setIdTipoDistribuicaoAtendimento(inputSimular.getInputTipoAtendimento());
		simulacao.setTipoDisciplinaFila(inputSimular.getInputTipoFila());
		simulacao.setNoPopulacao(inputSimular.getTamanhoPopulacao());
		simulacao.setNoServidores(inputSimular.getServidores());
		simulacao.setTempoTotal(inputSimular.getTempo());
		simulacao.setEstatisticas(estatisticas);
		simulacao.setParametrosChegada(parametrosChegada);
		simulacao.setParametrosAtendimento(parametrosAtendimento);
		
		
		return simulacao;
	}
	
	
	private Parametro[] getDistribuicaoGeral(HttpServletRequest request, String inicialNomeParametro){
		ArrayList<Distribuicao> distribuicaoGeral = new ArrayList<Distribuicao>();
		Distribuicao distribuicao;
		Parametro[] parametros;
		Parametro parametro;
		ArrayList<String> parameterNamesChegada = new ArrayList<String>();
		Enumeration<String> enumeration = request.getParameterNames();
		String tipoDistribuicao;
	    while (enumeration.hasMoreElements()) {
	        String parameterName = (String) enumeration.nextElement();
	        if(parameterName.startsWith(inicialNomeParametro)){
	        	parameterNamesChegada.add(parameterName);
	        	distribuicao = new Distribuicao();
	        	String valor;
	        	distribuicao.setProbabilidadeAcumulada(Double.valueOf(request.getParameter(parameterName)));
	        	valor = parameterName.substring(inicialNomeParametro.length());
	        	distribuicao.setOcorrencias((Double.valueOf(valor).intValue()));
	        	distribuicao.setTempo(Double.valueOf(valor));
	        	distribuicaoGeral.add(distribuicao);
	        }
	    }
	    
	    int tamanhoDistribuicao = distribuicaoGeral.size();
	    
	    parametros = new Parametro[tamanhoDistribuicao];
	    
	    if(Constantes.NOME_PARAMETRO_CHEGADA.equals(inicialNomeParametro)){
	    	tipoDistribuicao = Constantes.TIPO_DISTRIBUICAO_CHEGADA;
	    }else{
	    	tipoDistribuicao = Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO;
	    }
	    for(int i=0; i< tamanhoDistribuicao; i++){
	    	parametro = new Parametro(Constantes.TIPO_PARAMETRO_GERAL,tipoDistribuicao, distribuicaoGeral.get(i).getProbabilidadeAcumulada(),distribuicaoGeral.get(i).getTempo());
	    	parametros[i]= parametro;
	    }
		
		return parametros;
	}
	
	
	private Cliente[] getClientes(HttpServletRequest request) throws NumberFormatException{
		ArrayList<Cliente> clientesChegada = new ArrayList<Cliente>();
		Cliente[] clientes;
		Cliente cliente;
		
		
		Enumeration<String> enumeration = request.getParameterNames();
	    while (enumeration.hasMoreElements()) {
	        String parameterName = (String) enumeration.nextElement();
	        if(parameterName.startsWith("cliente_posicao_")){
	        	
		        	String id = parameterName.substring("cliente_posicao_".length());
		        	cliente = new Cliente(Integer.valueOf(request.getParameter(parameterName)), Double.valueOf(request.getParameter("cliente_hora_chegada_"+id)));
		        	cliente.setId(Integer.valueOf(id));
		        	cliente.setAtendente(Integer.valueOf(request.getParameter("cliente_atendente_"+id)));
		        	cliente.setHoraSaida(Double.valueOf(request.getParameter("cliente_hora_saida_"+id)));
		        	cliente.setTempoAtendimento(Double.valueOf(request.getParameter("cliente_tempo_atendimento_"+id)));	
		        	clientesChegada.add(cliente);
	        	
	        }
	    }
	    
	    int tamanhoPopulacao = clientesChegada.size();
	    
	    clientes = new Cliente[tamanhoPopulacao];
	    
	   
	    for(int i=0; i< tamanhoPopulacao; i++){
	    	cliente = clientesChegada.get(i);
	    	clientes[i]= cliente;
	    }
		
		return clientes;
	}
	
	private Collection<Cliente> toCollectionCliente(Cliente[] lista){
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

	    for(int i=0; i< lista.length; i++){
	    	clientes.add(lista[i]);
	    }
		
	    return clientes;
	}

}
