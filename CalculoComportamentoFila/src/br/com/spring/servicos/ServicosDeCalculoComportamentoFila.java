package br.com.spring.servicos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import umontreal.iro.lecuyer.probdist.ErlangDist;
import umontreal.iro.lecuyer.probdist.ExponentialDist;
import br.com.spring.dominio.Cliente;
import br.com.spring.dominio.Distribuicao;
import br.com.spring.dominio.Estatistica;
import br.com.spring.dominio.Fila;
import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.ServerUtilization;
import br.com.spring.dominio.WaitingTime;
import br.com.spring.util.Constantes;
import br.com.spring.util.Util;




/**
 * Implementação dos serviços de Meta
 * 
 * @author danieltc
 * 
 */
@Service
@Transactional
public class ServicosDeCalculoComportamentoFila {

	public double calculaOqueEuQuero(){
		double sum = 0;
		double temp = 0;
		double constante = (Math.pow(10,12)/fatorial(12)) * (12/(12-10));
		for(int i=0;i<12;i++){
			temp = Math.pow(10,i)/ fatorial(i) + constante;
			sum = sum + temp;
			System.out.println("i:"+i+" valor:"+temp);
		}
		System.out.println("Soma:"+sum);
		System.out.println("Sum-1: "+ Math.pow(sum,-1));
		return sum;
	}
	
	 public static int fatorial(int num) {


	        /**
	         * Este é o caso base, se o número passado por parametro for 0 ou 1,
	         * ele retorna o resultado 1 e finaliza o método.
	         */
	        if (num <= 1) {

	            return 1;

	        } else {

	            /**
	             * chama o método fatorial novamente, mas dessa vez enviando como
	             * parametro (n - 1).
	             */
	            
	            return fatorial(num - 1) * num;

	        }

	    }
	
	/**
	 * Processa a planilha
	 * 
	 * @return Retorna uma colecao de Distribuicao relativos a distribuicao representada na planilha  {@link Collection<Distribuicao>}
	 * @throws ExcecaoDeServico
	 */

	@Transactional
	public Collection<Distribuicao> planilhaToDistribuicao(MultipartFile[] planilhas) throws Exception {
		Collection<Distribuicao> distribuicao = new ArrayList<Distribuicao>();
		for (MultipartFile planilha : planilhas) {




			ByteArrayInputStream is;

			try {
				is = new ByteArrayInputStream(planilha.getBytes());


				Workbook workbook;

				workbook = new XSSFWorkbook(is);

				int quantidadePlanilhas = workbook.getNumberOfSheets();

				if (quantidadePlanilhas > 0) {


					Sheet sheet = workbook.getSheetAt(0);
					sheet.getRow(0).getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					//Removendo cabecalho
					sheet.removeRow(sheet.getRow(0));

				
					Iterator<Row> rowsIdentificador = sheet.rowIterator();
					
					
					Distribuicao anterior =  null;
					while (rowsIdentificador.hasNext()) {
						
						XSSFRow row= (XSSFRow) rowsIdentificador.next();
						try{
							Distribuicao proximo = rowToDistribuicao(row);
							
							if(anterior != null && anterior.getTempo() <= proximo.getTempo() && anterior.getProbabilidadeAcumulada() <= proximo.getProbabilidadeAcumulada()){
								anterior = rowToDistribuicao(row);
								proximo.setProbabilidadeAcumulada(proximo.getProbabilidadeAcumulada()/100.0);
								distribuicao.add(proximo);
							}
							else if(anterior == null){
								proximo.setProbabilidadeAcumulada(proximo.getProbabilidadeAcumulada()/100.0);
								distribuicao.add(proximo);
								anterior = rowToDistribuicao(row);
							}
								
							else{
								//avisos.add("Planilha inválida, reveja os valores.");
								distribuicao = null;
								return distribuicao;
							}

						}catch (Exception e) {

							//

							continue;
						}
					}
					
					if(anterior.getProbabilidadeAcumulada() != 100){
						//avisos.add("Planilha não está completa. Preencha até 100%");
						distribuicao = null;
						return distribuicao;
					}
					

				}
			} catch (Exception e) {

				//Log.error(this.getClass().toString(), "importarPlanilhas", e.getMessage(), e);
			}


		}
		return distribuicao;

	}
	
	@Transactional
	public void expExcel(Collection<Parametro> lista) {
		 
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet firstSheet = workbook.createSheet("Aba1");
		 
		FileOutputStream fos = null;
		 
		try {
			fos = new FileOutputStream(new File("distribuicao"));
			 

			int i = 0;
			HSSFRow row = firstSheet.createRow(i);
			row.createCell(0).setCellValue("K");
			row.createCell(1).setCellValue("Probabilidade Acumulada"); 
			for (Parametro cd : lista) {
				i++;
				row = firstSheet.createRow(i);
				row.createCell(0).setCellValue(cd.getValorTempo());
				row.createCell(1).setCellValue(cd.getValorParametro());
			
			} // fim do for
			 
			workbook.write(fos);
		 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao exportar arquivo");
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // fim do metodo exp
	
	
	
	/**
	 * Simula ,à partir de uma distribuição e tempo, seu resultado de atendimento
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	@Transactional
	public Collection<String> simulaResultadoDistribuicao(Collection<Distribuicao> distribuicao, int repeticoes) throws Exception {
		Collection<String> resultado = new ArrayList<String>();
		double st =0.0;
		for(int i = 0; i<repeticoes; i++){
			
			st = geraOcorrenciasRandomAtendimento(distribuicao);

			resultado.add(String.valueOf(st));
			
		}

		return resultado;
	}
	
	/**
	 * Cria distribuicao deterministico Atendimento
	 * 
	 * @return Retorna uma colecao de distribuicoes  {@link Collection<Distribuicao>}
	 * @throws ExcecaoDeServico
	 */
	public Collection<Distribuicao> criaDistribuicaoDeterministico(double taxa) throws Exception {
		Collection<Distribuicao> distribuicao = new ArrayList<Distribuicao>();
		Distribuicao dist = new Distribuicao();
		dist.setTempo(taxa);
		dist.setProbabilidadeAcumulada(1.0);
		distribuicao.add(dist);
		return distribuicao;
		
	}
	

	/**
	 * Calcula distribuicao exponencial
	 * 
	 * @return Retorna uma colecao de distribuicoes  {@link Collection<Distribuicao>}
	 * @throws ExcecaoDeServico
	 */
	public Collection<Distribuicao> calculaDistribuicaoExponencial(double lambda) throws Exception {
		Collection<Distribuicao> distribuicao = new ArrayList<Distribuicao>();
		Distribuicao dist = null;
		ExponentialDist exponencial = new ExponentialDist(lambda);
		
		double x=0;

		while(exponencial.cdf(x) < 1){
			dist = new Distribuicao();
			dist.setTempo(Double.valueOf(x));
			dist.setProbabilidadeAcumulada(exponencial.cdf(x));
			distribuicao.add(dist);
			
			x=x+0.001;
			
			
		}
		
		distribuicao.add(new Distribuicao(x,exponencial.cdf(x)));
		
		return distribuicao;
		
	}
	
	
	
	/**
	 * Calcula distribuicao de Erlang k
	 * 
	 * @return Retorna uma colecao de distribuicoes  {@link Collection<Distribuicao>}
	 * @throws ExcecaoDeServico
	 */
	public Collection<Distribuicao> calculaDistribuicaoErlangk(double lambda, Integer k) throws Exception {
		Collection<Distribuicao> distribuicao = new ArrayList<Distribuicao>();
		Distribuicao dist = null;
		double x=0;
		ErlangDist erlang = new ErlangDist(k, lambda);
		while(erlang.cdf(x) < 1){
			dist = new Distribuicao();
			dist.setTempo(x);
			dist.setProbabilidadeAcumulada(erlang.cdf(x));
			distribuicao.add(dist);
			x=x+0.001;
		}
				
		return distribuicao;
	}
	

	
	/**
	 * Cria Fila
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	@Transactional
	public Collection<Cliente> criaFilaChegada(Collection<Distribuicao> distribuicaoChegada, int tempo, int tamanhoPopulacao) throws Exception {
		
		
		double now = 0.0;
		int posicao = 0;
		Cliente cliente = null;
		double temp = 0.0;
		Collection<Cliente> clientes = new ArrayList<Cliente>();
		if(tamanhoPopulacao == 0){
			tamanhoPopulacao = Integer.MAX_VALUE;
		}
		
		
			
		while(now < tempo){
			if(posicao < tamanhoPopulacao){
				temp = geraOcorrenciasRandomAtendimento(distribuicaoChegada);
				//temp = 1.0/temp;
				//temp = -1.0/lambda * Math.log(Math.random());   
				//temp = -Math.log(1.0 - Math.random()) / lambda;

				now = now + temp;
				cliente = new Cliente(posicao, now);
				posicao++;
				clientes.add(cliente);
			}else
				now = tempo;
		}
		
		return clientes;
	}
	
	/**
	 * Seta tempo de atendimento e tempo de espera Fila
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	public Collection<Cliente> calculaAtendimentoFila(Collection<Cliente> clientes,Collection<String> distribuicaoTempo, Integer atendentes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		
		double tempo = 0;
		double temp = 0;
		Cliente cliente = null;
		
		Iterator<String> iteratorDistribuicaoTempo = distribuicaoTempo.iterator();
		
		Double[] tempoAtendentes = new Double[atendentes];
		
		boolean pula = false;
		
		if(fila.size() > 0 && fila.get(0).getHoraChegada() > 0)
			tempo = fila.get(0).getHoraChegada();
		for(int j =0;j<clientes.size(); j++ ){

				for(int i=0; i<atendentes; i++){
					pula = false;
					
					//Se tem atendente livre
					if( (tempoAtendentes[i] == null || tempoAtendentes[i] == 0)){
						
						
						for(int n=0;n<atendentes;n++){
							if((j+n < fila.size()) && !fila.get(j+n).isAtendido()){
								cliente = fila.get(j+n);
																
								double tempoAtendimento = Double.valueOf(iteratorDistribuicaoTempo.next());
								cliente.setTempoAtendimento(tempoAtendimento);
								cliente.setHoraSaida(tempo+tempoAtendimento);
								cliente.setAtendente(i);
								cliente.setAtendido(true);
								tempoAtendentes[i] = tempoAtendimento;
								j = j+n;
								break;
							}
									
						}
						
						if(j+1 < fila.size() && fila.get(j+1).getHoraChegada() > tempo){
							
							temp = fila.get(j+1).getHoraChegada() - tempo;
							tempo = fila.get(j+1).getHoraChegada();
							
							
							for(int h=0; h<atendentes; h++){
								
								if(tempoAtendentes[h]  != null && tempoAtendentes[h] > 0){
									if(tempoAtendentes[h] > temp)
										tempoAtendentes[h] = tempoAtendentes[h] - temp;
									else
										tempoAtendentes[h] = 0.0;
								}
									
							}
							
							
							pula = true;
						}
						
						
					}
					
				}
				//Todos atendentes ocupados
				if(!pula){
					
					double menorTempo = retornaMenorTempo(tempoAtendentes);
					
					for(int i=0; i<atendentes; i++){
					
						if(tempoAtendentes[i]  != null && tempoAtendentes[i] > 0){
							if(tempoAtendentes[i] > 1 && menorTempo > 1)
								tempoAtendentes[i]--;
							else
								tempoAtendentes[i] = tempoAtendentes[i] - menorTempo;
						}
					}
					if(menorTempo > 1)
						tempo++;
					else
						tempo = tempo + menorTempo;
				}
				
				
				while(!temAtendenteLivre(tempoAtendentes)){
					
					double menorTempo = retornaMenorTempo(tempoAtendentes);
					
					for(int i=0; i<atendentes; i++){
						if(tempoAtendentes[i]  != null && tempoAtendentes[i] > 0){
							if(tempoAtendentes[i] > 1 && menorTempo > 1)
								tempoAtendentes[i] --;
							else
								tempoAtendentes[i] = tempoAtendentes[i] - menorTempo;
						}
					}
					if(menorTempo > 1)
						tempo++;
					else
						tempo = tempo + menorTempo;
				}
				
				

		}
		
		return clientes;
	}
	

	/**
	 * Seta tempo de atendimento e tempo de espera Fila
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	public Collection<Cliente> calculaAtendimentoFilaLastComeFirstServed(Collection<Cliente> clientes,Collection<String> distribuicaoTempo, Integer atendentes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		
		double tempo = 0;
		double temp = 0;
		Cliente cliente = null;
		
		Iterator<String> iteratorDistribuicaoTempo = distribuicaoTempo.iterator();
		
		Double[] tempoAtendentes = new Double[atendentes];
		
		boolean pula = false;
		
		if(fila.size() > 0){
			tempo = fila.get(0).getHoraChegada();
			fila.get(0).setTempoAtendimento(Double.valueOf(iteratorDistribuicaoTempo.next()));
			fila.get(0).setHoraSaida(tempo+fila.get(0).getTempoAtendimento());
			fila.get(0).setAtendido(true);
			fila.get(0).setAtendente(0);
			tempoAtendentes[0] = fila.get(0).getTempoAtendimento();
		}
			
		
		while(existeClienteSemAtendimento(clientes)){
		
			if(temAtendenteLivre(tempoAtendentes)){
			
				for(int i=0; i<atendentes; i++){

					if( (tempoAtendentes[i] == null || tempoAtendentes[i] == 0)){
						
						for(int j = clientes.size()-1; j >= 0;j--){	
							
								if(!fila.get(j).isAtendido() && fila.get(j).getHoraChegada() <= tempo){
									cliente = fila.get(j);						
									double tempoAtendimento = Double.valueOf(iteratorDistribuicaoTempo.next());
	
									cliente.setTempoAtendimento(tempoAtendimento);
									cliente.setHoraSaida(tempo+tempoAtendimento);
									cliente.setAtendente(i);
									cliente.setAtendido(true);
									tempoAtendentes[i] = tempoAtendimento;
									pula = true;
									break;
								}
								
						
						}
					}
				}
				
				if(!pula){
					if(retornaProximoCliente(clientes) != null)
						tempo = retornaProximoCliente(clientes).getHoraChegada();
					else
						tempo = retornaUltimoASair(clientes).getHoraSaida();
					
				}
				pula = false;
			}else{
				temp = retornaMenorTempo(tempoAtendentes);
				tempo = tempo + temp;
				for(int i=0; i<atendentes; i++){
					tempoAtendentes[i] = tempoAtendentes[i]-temp;
				}
			}

		}
		
		
		return clientes;
	}

	
	/**
	 * Seta tempo de atendimento e tempo de espera Fila para um atendimento random
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	public Collection<Cliente> calculaAtendimentoFilaRandom(Collection<Cliente> clientes,Collection<String> distribuicaoTempo, Integer atendentes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		ArrayList<Integer> indicesNaFila;
		double tempo = 0;
		double temp = 0;
		Cliente cliente = null;
		
		Iterator<String> iteratorDistribuicaoTempo = distribuicaoTempo.iterator();
		
		Double[] tempoAtendentes = new Double[atendentes];
		
		if(fila.size() > 0){
			tempo = fila.get(0).getHoraChegada();
			fila.get(0).setTempoAtendimento(Double.valueOf(iteratorDistribuicaoTempo.next()));
			fila.get(0).setHoraSaida(tempo+fila.get(0).getTempoAtendimento());
			fila.get(0).setAtendido(true);
			fila.get(0).setAtendente(0);
			tempoAtendentes[0] = fila.get(0).getTempoAtendimento();
		}
			
		
		while(existeClienteSemAtendimento(clientes)){
		
			if(temAtendenteLivre(tempoAtendentes)){
			
				for(int i=0; i<atendentes; i++){
					
					if( (tempoAtendentes[i] == null || tempoAtendentes[i] == 0)){
						indicesNaFila = new ArrayList<Integer>();
						for(int j = 0; j < clientes.size();j++){		
								if(!fila.get(j).isAtendido() && fila.get(j).getHoraChegada() <= tempo){
									indicesNaFila.add(j);
								}
						}
						
						if(indicesNaFila != null && indicesNaFila.size() > 0){
							Integer sorteado = sorteiaNumero(indicesNaFila);
							cliente = fila.get(sorteado);						
							double tempoAtendimento = Double.valueOf(iteratorDistribuicaoTempo.next());
							cliente.setTempoAtendimento(tempoAtendimento);
							cliente.setHoraSaida(tempo+tempoAtendimento);
							cliente.setAtendente(i);
							cliente.setAtendido(true);
							tempoAtendentes[i] = tempoAtendimento;
						}else{
							if(retornaProximoCliente(clientes) != null)
								tempo = retornaProximoCliente(clientes).getHoraChegada();
							else
								tempo = retornaUltimoASair(clientes).getHoraSaida();
						}
						
					}
				}
		
			}else{
				temp = retornaMenorTempo(tempoAtendentes);
				tempo = tempo + temp;
				for(int i=0; i<atendentes; i++){
					tempoAtendentes[i] = tempoAtendentes[i]-temp;
				}
			}

		}

		return clientes;
	}


	
	/**
	 * Seta tempo de atendimento e tempo de espera Fila
	 * 
	 * @return Retorna uma colecao de avisos relativos a erros na carga  {@link Collection<String>}
	 * @throws ExcecaoDeServico
	 */

	public Collection<Fila> calculaClientesNaFilaPorTempo(Collection<Cliente> clientes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		double tempoTotal = Math.round(retornaUltimoASair(clientes).getHoraSaida());
		List<Fila> clientesNaFilaPorTempo = new ArrayList<Fila>();
		List<Cliente> temporaria = new ArrayList<Cliente>();
		Fila filaInstante;

		
		for (int i = 0; i<tempoTotal; i++){
			temporaria = new ArrayList<Cliente>();
			for(int j = 0; j< fila.size();j++){
				
				if(fila.get(j).getHoraAtendimento() > i && fila.get(j).getHoraChegada() <= i){
					
					temporaria.add(fila.get(j));
					
				}
				if(fila.get(j).getHoraChegada() > i){

					break;
				}
			}
			
			filaInstante = new Fila();
			filaInstante.setFila(temporaria);
			filaInstante.setTempo((long)i);
			
			clientesNaFilaPorTempo.add(filaInstante);
		}
		

		return clientesNaFilaPorTempo;
	}
	
	public Collection<Fila> calculaClientesNoSistemaPorTempo(Collection<Cliente> clientes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		double tempoTotal = Math.round(retornaUltimoASair(clientes).getHoraSaida());
		List<Fila> clientesNoSistemaPorTempo = new ArrayList<Fila>();
		List<Cliente> temporaria = new ArrayList<Cliente>();
		Fila filaInstante;

		
		for (int i = 0; i<tempoTotal; i++){
			temporaria = new ArrayList<Cliente>();
			for(int j = 0; j< fila.size();j++){
				
				if(fila.get(j).getHoraSaida()> i && fila.get(j).getHoraChegada() <= i){
					
					temporaria.add(fila.get(j));
					
				}
				if(fila.get(j).getHoraChegada() > i){

					break;
				}
			}
			
			filaInstante = new Fila();
			filaInstante.setFila(temporaria);
			filaInstante.setTempo((long)i);
			
			clientesNoSistemaPorTempo.add(filaInstante);
		}
		

		return clientesNoSistemaPorTempo;
	}
	
	public Collection<WaitingTime> calculaMediaTempoEsperaPorHoraChegada(Collection<Cliente> clientes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		double tempoTotal = retornaUltimoAChegar(clientes).getHoraChegada();
		List<WaitingTime> list = new ArrayList<WaitingTime>();
		List<Cliente> temporaria = new ArrayList<Cliente>();
		WaitingTime wait;
		int k =0;
		double sum = 0;
		double media = 0;
		
		for (int i = 0; i<tempoTotal; i++){
			//temporaria = new ArrayList<Cliente>();
			for(int j = k; j< fila.size();j++){
				
				if(fila.get(j).getHoraChegada() <= i && fila.get(j).getHoraChegada() > (i-1)){
					temporaria.add(fila.get(j));
					k = j;
				}else if(fila.get(j).getHoraChegada() > i)
					break;
			}
			sum = 0;
			for(int h =0; h<temporaria.size();h++){
				sum = sum + temporaria.get(h).getTempoEspera();
			}
			if(sum != 0)
				media = sum/temporaria.size();
			if(media>=1){
			}else 
				media = 0;
			
			wait = new WaitingTime();
			wait.setMediaTempoEspera(media);
			wait.setTempo(Double.valueOf(i));
			temporaria = new ArrayList<Cliente>();
			list.add(wait);
		}
		

		return list;
	}
		
	/**
	 * Método que calcula a utilização do servidor durante o tempo
	 * @param clientes
	 * @param atendentes
	 * @throws Exception 
	 */		
	public Collection<ServerUtilization> serverUtilization(Collection<Cliente> clientes, Integer atendentes) throws Exception {
		
		ArrayList<Cliente> fila = (ArrayList<Cliente>) clientes;
		double tempoTotal = Math.round(retornaUltimoASair(clientes).getHoraSaida());
		List<ServerUtilization> list = new ArrayList<ServerUtilization>();
		List<Cliente> temporaria = new ArrayList<Cliente>();
		ServerUtilization server;
		Double percentualUtilizacao = 0.0;
		int servidoresUtilizados = 0;
		
		boolean[] utilizacaoServidores = new boolean[atendentes];
		
		
		for (int i = 0; i<tempoTotal; i++){

			for(int j = 0; j< fila.size();j++){
				
				if(fila.get(j).getHoraAtendimento() <= i && fila.get(j).getHoraSaida() > i){
					temporaria.add(fila.get(j));
				}
			}

			for(int h =0; h<temporaria.size();h++){
				for(int l = 0; l < atendentes; l++){
					if(temporaria.get(h).getAtendente() == l){
						utilizacaoServidores[l] = true;
					}
				}
			}
			
			for(int l = 0; l < atendentes; l++){
				if(utilizacaoServidores[l]){
					servidoresUtilizados++;
				}
			}
			
			percentualUtilizacao =  ((double)servidoresUtilizados/atendentes);
			
			server = new ServerUtilization();
			server.setPercentual(percentualUtilizacao*100);
			server.setTempo((long)i);
			temporaria = new ArrayList<Cliente>();
			list.add(server);
			
			for(int l = 0; l < atendentes; l++){
				utilizacaoServidores[l]=false;
			}
			servidoresUtilizados = 0;
		}
		

		return list;
	}
	
	

	
/**
 * Método que calcula media e variancia de tempo de espera por cliente
 * @param row
 * @param clientes
 * @throws Exception 
 */	
	public Estatistica calculaEstatisticaTempoEspera(Collection<Cliente> clientes) throws Exception {	
		int indiceVetor=-1;
		double totalEspera=0;
		
		Estatistica estatisticaTempoEspera = new Estatistica();
		Iterator<Cliente> iterator = clientes.iterator();
		double[] tempoEsperaCliente = new double[clientes.size()];
		
		while (iterator.hasNext()) {
			
			Cliente cli = iterator.next();
			
			tempoEsperaCliente[++indiceVetor] = cli.getTempoEspera();
			
			totalEspera = totalEspera + cli.getTempoEspera();
			
		}
		
		estatisticaTempoEspera.setMedia((totalEspera/(double)(indiceVetor+1)));
		estatisticaTempoEspera.setVariancia(Util.calculaVarianciaGlobal(tempoEsperaCliente,estatisticaTempoEspera.getMedia()));	
		
		return estatisticaTempoEspera;
	}

	
/**
 * Método que calcula media e variancia de tempo no sistema
 * @param clientes
 * @throws Exception 
 */	
	public Estatistica calculaEstatisticaTempoSistema(Collection<Cliente> clientes) throws Exception {
		int indiceVetor=-1;
		double tempoSistemaClienteX =0;
		double totalTempoSistema=0;
		double[] tempoClienteSistema = new double[clientes.size()];
		Estatistica estatisticaTempoSistema = new Estatistica();
		Iterator<Cliente> iterator = clientes.iterator();

		while (iterator.hasNext()) {
			
			Cliente cli = iterator.next();
			tempoSistemaClienteX =  (cli.getHoraSaida()-cli.getHoraChegada());
			tempoClienteSistema[++indiceVetor] = tempoSistemaClienteX;
			
			totalTempoSistema = totalTempoSistema + tempoSistemaClienteX;
			
		}
		
		estatisticaTempoSistema.setMedia((totalTempoSistema/(double)(indiceVetor+1)));
		estatisticaTempoSistema.setVariancia(Util.calculaVarianciaGlobal(tempoClienteSistema,estatisticaTempoSistema.getMedia()));	
		
		return estatisticaTempoSistema;
	}
/**
 * Método que calcula tamanho médio da fila/clientes no sistema e variancia
 * @param Collection<Fila> fila
 * @throws Exception 
 */	
	public Estatistica calculaEstatisticaTamanhoFila(Collection<Fila> fila) throws Exception {
		int indiceVetor=-1;

		double totalClientes=0;
		double[] totalClientesFila = new double[fila.size()];
		Estatistica estatisticaTamanhoFila = new Estatistica();
		Iterator<Fila> iterator = fila.iterator();

		
		while (iterator.hasNext()) {
			
			Fila fil = iterator.next();

			totalClientesFila[++indiceVetor] = fil.getTamanho();
			
			totalClientes = totalClientes + fil.getTamanho();
			
		}
		estatisticaTamanhoFila.setMedia((totalClientes/(double)(indiceVetor+1)));
		estatisticaTamanhoFila.setVariancia(Util.calculaVarianciaGlobal(totalClientesFila,estatisticaTamanhoFila.getMedia()));	
		
		return estatisticaTamanhoFila;
	}

	
/**
 * Método que calcula media de tempo no sistema
   @param Collection<Fila> fila
   @param Collection<ServerUtilization> serverUtilization
   @param int quantidadeServidores
 * @throws Exception 
 */	
/*	public Estatistica calculaEstatisticaClientesSistema(Collection<Fila> fila,Collection<ServerUtilization> serverUtilization, int quantidadeServidores) throws Exception {
		int indiceVetor=-1;
		
		double clientesSistemaTempoX =0;
		
		double totalClientes=0;
		double[] totalClientesSistema = new double[fila.size()];
		Estatistica estatisticaClientesSistema = new Estatistica();
		
        Iterator<Fila> filaIterator = fila.iterator();
		Iterator<ServerUtilization> serverUtilizationIterator = serverUtilization.iterator();
		
		while (filaIterator.hasNext()) {
			if(serverUtilizationIterator.hasNext()){
				ServerUtilization servUtil = serverUtilizationIterator.next();
				Fila fil = filaIterator.next();
				
				if(fil.getTempo().equals(servUtil.getTempo())){
					
					
					clientesSistemaTempoX = ((servUtil.getPercentual()/100)*quantidadeServidores) + fil.getTamanho();
					totalClientesSistema[++indiceVetor] = clientesSistemaTempoX;
					totalClientes = totalClientes + clientesSistemaTempoX;
				}
				
			}else
				break;

		}
		estatisticaClientesSistema.setMedia((totalClientes/(double)(indiceVetor+1)));
		estatisticaClientesSistema.setVariancia(Util.calculaVarianciaGlobal(totalClientesSistema,estatisticaClientesSistema.getMedia()));	
		
		return estatisticaClientesSistema;
	}*/
/**
 * Método que converte uma linha da planilha em uma entidade Meta
 * @param row
 * @param Ocorrencias
 * @param ProbabilidadeAcumulada
 * @return  @link Meta
 * @throws ExcecaoDeServico 
 */
	private Distribuicao rowToDistribuicao(Row row) throws Exception{
		Distribuicao distribuicao = new Distribuicao();
		
		distribuicao.setOcorrencias((int) row.getCell(Constantes.COLUNA_OCORRENCIAS).getNumericCellValue());
		distribuicao.setTempo((double) row.getCell(Constantes.COLUNA_OCORRENCIAS).getNumericCellValue());
		distribuicao.setProbabilidadeAcumulada((double) row.getCell(Constantes.COLUNAS_PROBABILIDADE).getNumericCellValue());
		
		return distribuicao;

	}
	
	private int geraOcorrenciasRandom(Collection<Distribuicao> distribuicaoChegada){
		double random = Math.random();
		ArrayList<Distribuicao> distribuicao = (ArrayList<Distribuicao>) distribuicaoChegada;
		
		int tamanho = distribuicao.size();
		tamanho --;
		for (int i = tamanho; i>=0;i--) {
			if(random > distribuicao.get(i).getProbabilidadeAcumulada()) 
				return distribuicao.get(i+1).getOcorrencias();
	    }
		
		return distribuicao.get(0).getOcorrencias();
	}
	
	private double geraOcorrenciasRandomAtendimento(Collection<Distribuicao> distribuicaoChegada){
		double random = Math.random();
		ArrayList<Distribuicao> distribuicao = (ArrayList<Distribuicao>) distribuicaoChegada;
		if(distribuicao.get(0).getTempo() == 0){
			distribuicao.remove(0);
		}
		int tamanho = distribuicao.size();
		tamanho --;
		for (int i = tamanho; i>=0;i--) {
			if(random > distribuicao.get(i).getProbabilidadeAcumulada()) 
				return distribuicao.get(i+1).getTempo();
	    }
		
		return distribuicao.get(0).getTempo();
	}
	
	
	private boolean temAtendenteLivre(Double[] atendentes){
		boolean retorno = false;
		
		for(int i = 0; i < atendentes.length; i++){
			if(atendentes[i] == null || atendentes[i] == 0)
				return true;
		}
		
		return retorno;
	}
	
	private double retornaMenorTempo(Double[] atendentes){
		double menorTempo = Double.POSITIVE_INFINITY;
		
		for(int i=0; i< atendentes.length ; i++){
			if(atendentes[i] < menorTempo && atendentes[i] != null){
				menorTempo = atendentes[i];
			}
		}
		
		
		return menorTempo;
	}
	
	private boolean existeClienteSemAtendimento(Collection<Cliente> clientes){
		boolean retorno = false;
		ArrayList<Cliente> todosClientes = (ArrayList<Cliente>) clientes;
		
		for(int i=0; i<todosClientes.size(); i++){
			if(!todosClientes.get(i).isAtendido()){
				retorno = true;
				break;
			}
		}

		return retorno;
	}
	
	private Cliente retornaProximoCliente(Collection<Cliente> clientes){
		Cliente retorno = null;
		ArrayList<Cliente> todosClientes = (ArrayList<Cliente>) clientes;
		
		for(int i=0;i<todosClientes.size();i++){
			if(!todosClientes.get(i).isAtendido()){
				retorno = todosClientes.get(i);
				break;
			}	
		}

		return retorno;
	}
	
	private Cliente retornaUltimoASair(Collection<Cliente> clientes){
		int indiceCliente=0;
		double maiorTempo=0;
		ArrayList<Cliente> todosClientes = (ArrayList<Cliente>) clientes;
		
		for(int i=0;i<todosClientes.size();i++){
			if(todosClientes.get(i).isAtendido()){
				if(todosClientes.get(i).getHoraSaida() > maiorTempo){
					maiorTempo = todosClientes.get(i).getHoraSaida();
					indiceCliente = i;
				}
			}	
		}

		return todosClientes.get(indiceCliente);
	}
	
	private Cliente retornaUltimoAChegar(Collection<Cliente> clientes){
		int indiceCliente=0;
		double maiorTempo=0;
		ArrayList<Cliente> todosClientes = (ArrayList<Cliente>) clientes;
		
		for(int i=0;i<todosClientes.size();i++){
			if(todosClientes.get(i).getHoraChegada() > maiorTempo){
				maiorTempo = todosClientes.get(i).getHoraChegada();
				indiceCliente = i;
			}
		}

		return todosClientes.get(indiceCliente);
	}
	
	private Integer sorteiaNumero(ArrayList<Integer> indices){
		Collection<Distribuicao> distribuicao = new  ArrayList<Distribuicao>();
		Distribuicao probabilidadeAcumuladaX;
		double soma = 0;
		double distribuicaoIndividual = 1.0/(double)indices.size();
		
		for(int x=0;x<indices.size();x++){
			soma+=distribuicaoIndividual;
			probabilidadeAcumuladaX = new Distribuicao(indices.get(x), soma);
			distribuicao.add(probabilidadeAcumuladaX);
		}

		return geraOcorrenciasRandom(distribuicao);
	}
	
	

}


