package br.com.spring.dados.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import br.com.spring.dados.ISimulacao;
import br.com.spring.dominio.Cliente;
import br.com.spring.dominio.Estatistica;
import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.Simulacao;
import br.com.spring.util.Constantes;

public class SimulacaoDadosMysql implements ISimulacao{
		
	
	@Override
	public void gravaSimulacao(final Simulacao simulacao) throws SQLException{
			final int idAtual = getIdUltimaSimulacao()+1;
			
			JdbcTemplate jdbcTemplate = getJdbcTemplate();

	        String sqlInsert = "insert into simulacao(ID_SIMULACAO, ID_TIPO_DISTRIBUICAO_CHEGADA, ID_TIPO_DISTRIBUICAO_ATENDIMENTO, ID_TIPO_DISCIPLINA_FILA, NO_POPULACAO, NO_TEMPO_TOTAL, NO_SERVIDORES)"
	        +"values(?,?,?,?,?,?,?)";
	        
	        String sqlsInsertEstatistica = "insert into estatistica_simulacao(ID_SIMULACAO, ID_ESTATISTICA, VALOR_MEDIA, VALOR_VARIANCIA)"
	        +"values(?,?,?,?);";
	       
	        String sqlInsertsParametros ="insert into parametros_simulacao(ID_PARAMETRO, ID_SIMULACAO, VALOR_PARAMETRO, DISTRIBUICAO_TIPO)"
	        +"values(?,?,?,?)";
	                
	        String sqlInsertClientes ="insert into resultado_cliente(ID_SIMULACAO,NO_CLIENTE,NO_POSICAO,ATENDENTE,TEMPO_ATENDIMENTO,HORA_CHEGADA,HORA_SAIDA)"
	        +"values(?,?,?,?,?,?,?)";
	        
	        jdbcTemplate.update(sqlInsert, idAtual, simulacao.getIdTipoDistribuicaoChegada(),simulacao.getIdTipoDistribuicaoAtendimento(),simulacao.getTipoDisciplinaFila(),simulacao.getNoPopulacao(), simulacao.getTempoTotal(),simulacao.getNoServidores());
	        
	        jdbcTemplate.batchUpdate(sqlsInsertEstatistica, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                	Estatistica estatistica = simulacao.getEstatisticas()[i];
                    int idSimulacao = idAtual;
                    int idEstatistica = estatistica.getCodigoEstatistica();
                    
                    double media =  estatistica.getMedia();
                    double variancia = estatistica.getVariancia();

                    ps.setInt(1, idSimulacao);
                    ps.setInt(2, idEstatistica);
                    ps.setDouble(3, media);
                    ps.setDouble(4, variancia);
                }

				@Override
				public int getBatchSize() {
                    return simulacao.getEstatisticas().length;
				}

            
            });
	        
	        jdbcTemplate.batchUpdate(sqlInsertClientes, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                	Cliente cliente = simulacao.getClientes()[i];

                	int idSimulacao = idAtual;
                    int noCliente = cliente.getId();
                    int posicao = cliente.getPosicao();
                    int atendente = cliente.getAtendente();
                    double tempoAtendimento = cliente.getTempoAtendimento();
                    double horaChegada = cliente.getHoraChegada();
                    double horaSaida = cliente.getHoraSaida();
           
                    ps.setInt(1, idSimulacao);
                    ps.setInt(2, noCliente);
                    ps.setInt(3, posicao);
                    ps.setInt(4, atendente);
                    ps.setDouble(5, tempoAtendimento);
                    ps.setDouble(6, horaChegada);
                    ps.setDouble(7, horaSaida);
                }
                
				@Override
				public int getBatchSize() {
                    return simulacao.getClientes().length;
				}
				
           });
	        
	        if(simulacao.getIdTipoDistribuicaoChegada() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_EXPONENCIAL_LAMBDA, idAtual, simulacao.getParametrosChegada()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_CHEGADA);
	      
	        }else if(simulacao.getIdTipoDistribuicaoChegada() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_DETERMINISTICO_LAMBDA, idAtual, simulacao.getParametrosChegada()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_CHEGADA);
	      
	        }else if(simulacao.getIdTipoDistribuicaoChegada() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_ERLANG_K_THETA, idAtual, simulacao.getParametrosChegada()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_CHEGADA);
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_ERLANG_K_FORMA, idAtual, simulacao.getParametrosChegada()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_CHEGADA);
	       
	        }else if(simulacao.getIdTipoDistribuicaoChegada() == Constantes.TIPO_DISTRIBUICAO_GERAL){
	        	
	        	String temp = sqlInsertsParametros;
	        	
	        	sqlInsertsParametros ="insert into parametros_simulacao(ID_PARAMETRO, ID_SIMULACAO, VALOR_TEMPO, VALOR_PARAMETRO, DISTRIBUICAO_TIPO)"
	        	        +"values(?,?,?,?,?)";
	        	 jdbcTemplate.batchUpdate(sqlInsertsParametros, new BatchPreparedStatementSetter() {
	                 @Override
	                 public void setValues(PreparedStatement ps, int i) throws SQLException {
	                 	Parametro parametro = simulacao.getParametrosChegada()[i];
	                     int idSimulacao = idAtual;
	                     int idParametro = parametro.getIdTipoParametro();
	                     
	                     double tempo =  parametro.getValorTempo();
	                     double probabilidadeAcumulada = parametro.getValorParametro();
	              
	                     ps.setInt(1, idParametro);
	                     ps.setInt(2, idSimulacao);
	                     ps.setDouble(3, tempo);
	                     ps.setDouble(4, probabilidadeAcumulada);
	                     ps.setString(5, Constantes.TIPO_DISTRIBUICAO_CHEGADA);
	                 }
	                 
	 				@Override
	 				public int getBatchSize() {
	                     return simulacao.getParametrosChegada().length;
	 				}
	 				
	            });
	        	
	        	sqlInsertsParametros = temp;
	        }
	        
	        
	        if(simulacao.getIdTipoDistribuicaoAtendimento() == Constantes.TIPO_DISTRIBUICAO_EXPONENCIAL){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_EXPONENCIAL_LAMBDA, idAtual, simulacao.getParametrosAtendimento()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO);
	      
	        }else if(simulacao.getIdTipoDistribuicaoAtendimento() == Constantes.TIPO_DISTRIBUICAO_DETERMINISTICO){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_DETERMINISTICO_LAMBDA, idAtual, simulacao.getParametrosAtendimento()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO);
	      
	        }else if(simulacao.getIdTipoDistribuicaoAtendimento() == Constantes.TIPO_DISTRIBUICAO_ERLANG_K){
	        	
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_ERLANG_K_THETA, idAtual, simulacao.getParametrosAtendimento()[0].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO);
		        jdbcTemplate.update(sqlInsertsParametros, Constantes.TIPO_PARAMETRO_ERLANG_K_FORMA, idAtual, simulacao.getParametrosAtendimento()[1].getValorParametro(),Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO);
	       
	        }else if(simulacao.getIdTipoDistribuicaoAtendimento() == Constantes.TIPO_DISTRIBUICAO_GERAL){
	        	String temp = sqlInsertsParametros;
	        	sqlInsertsParametros ="insert into parametros_simulacao(ID_PARAMETRO, ID_SIMULACAO, VALOR_TEMPO, VALOR_PARAMETRO, DISTRIBUICAO_TIPO)"
	        	        +"values(?,?,?,?,?)";
	        	 jdbcTemplate.batchUpdate(sqlInsertsParametros, new BatchPreparedStatementSetter() {
	                 @Override
	                 public void setValues(PreparedStatement ps, int i) throws SQLException {
	                 	Parametro parametro = simulacao.getParametrosAtendimento()[i];
	                     int idSimulacao = idAtual;
	                     int idParametro = parametro.getIdTipoParametro();
	                     
	                     double tempo =  parametro.getValorTempo();
	                     double probabilidadeAcumulada = parametro.getValorParametro();
	                     
	                     ps.setInt(1, idParametro);
	                     ps.setInt(2, idSimulacao);
	                     ps.setDouble(3, tempo);
	                     ps.setDouble(4, probabilidadeAcumulada);
	                     ps.setString(5, Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO);
	                 }
	
	 				@Override
	 				public int getBatchSize() {
	                     return simulacao.getParametrosAtendimento().length;
	 				}

	            });
	        	 
	            sqlInsertsParametros = temp;
	        }
		
	}
    
	@Override
	public int getIdUltimaSimulacao() throws SQLException{
		
			JdbcTemplate jdbcTemplate = getJdbcTemplate();

	        String sqlSelect = "select COALESCE(max(id_simulacao),0) as id_simulacao from simulacao";
	        
	        return jdbcTemplate.queryForInt(sqlSelect);
	}
	
	@Override
	public void deleteSimulacao(int idSimulacao) throws SQLException{
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		String deleteEstatistica = "delete from estatistica_simulacao where id_simulacao = ?";
		String deleteParametros = "delete from parametros_simulacao where id_simulacao = ?";
		String deleteClientes = "delete from resultado_cliente where id_simulacao = ?";
		String deleteSimulacao = "delete from simulacao where id_simulacao = ?";
		
        jdbcTemplate.update(deleteEstatistica, idSimulacao);
        jdbcTemplate.update(deleteParametros, idSimulacao);
        jdbcTemplate.update(deleteClientes, idSimulacao);
        jdbcTemplate.update(deleteSimulacao, idSimulacao);
	}
	@Override
	public Collection<Parametro> getDistribuicao(String idSimulacao, String tipoDistribuicao) throws SQLException {
		
			JdbcTemplate jdbcTemplate = getJdbcTemplate();

	        String sqlSelect = 
	        		"SELECT                                                              "
	        				 +"    paramsim.ID_PARAMETRO,                                         "
	        				 +"    SIGLA_PARAMETRO,                                               "
	        				 +"    NOME_PARAMETRO,                                                "
	        				 +"    ID_SIMULACAO,                                                  "
	        				 +"    VALOR_PARAMETRO,                                               "
	        				 +"    VALOR_TEMPO,                                                   "
	        				 +"    DISTRIBUICAO_TIPO                                              "
	        				 +"FROM                                                               "
	        				 +"    parametros_simulacao paramsim                                  "
	        				 +"        INNER JOIN                                                 "
	        				 +"    parametros param ON paramsim.ID_PARAMETRO = param.ID_PARAMETRO "
	        				 +"where DISTRIBUICAO_TIPO =? and ID_SIMULACAO = ?                    ";
	        
	        String nomesParametro[] = new String[] {tipoDistribuicao,idSimulacao};
	        
        	List<Parametro> parametros = jdbcTemplate.query(sqlSelect,nomesParametro,new RowMapper<Parametro>(){
				@Override
				public Parametro mapRow(ResultSet rs, int arg1) throws SQLException {
					Parametro parametro = new Parametro();
					parametro.setIdTipoParametro(rs.getInt("ID_PARAMETRO"));
					parametro.setTipoDistribuicao(rs.getString("DISTRIBUICAO_TIPO"));
					parametro.setValorParametro(rs.getDouble("VALOR_PARAMETRO"));
					parametro.setValorTempo(rs.getDouble("VALOR_TEMPO"));
					parametro.setNomeParametro(rs.getString("NOME_PARAMETRO"));
					parametro.setSiglaParametro(rs.getString("SIGLA_PARAMETRO"));
					return parametro;
				}
        		
        	});
	        
	        return parametros;
	}
	
	
	@Override
	public Collection<Simulacao> listaSimulacoes() throws SQLException {
		final JdbcTemplate jdbcTemplate = getJdbcTemplate();

        String sqlSelectSimulacao = 
		"SELECT                                                                                                       "
		+"    s.id_simulacao,                                                                                         "
		+"    dchegada.ID_TIPO_DISTRIBUICAO AS ID_TIPO_DISTRIBUICAO_CHEGADA,                                          "
		+"    dchegada.SIGLA_DISTRIBUICAO AS SG_DISTRIBUICAO_CHEGADA,                                                 "
		+"    dchegada.TIPO_DISTRIBUICAO AS NM_DISTRIBUICAO_CHEGADA,                                                  "
		+"    datendimento.ID_TIPO_DISTRIBUICAO AS ID_TIPO_DISTRIBUICAO_ATENDIMENTO,                                  "
		+"    datendimento.SIGLA_DISTRIBUICAO AS SG_DISTRIBUICAO_ATENDIMENTO,                                         "
		+"    datendimento.TIPO_DISTRIBUICAO AS NM_DISTRIBUICAO_ATENDIMENTO,                                          "
		+"    tipoFila.ID_TIPO_DISCIPLINA_FILA,                                                                       "
		+"    tipoFila.SIGLA_TIPO_FILA,                                                                               "
		+"    tipoFila.NOME_TIPO_FILA,                                                                                "
		+"    s.no_populacao,                                                                                         "
		+"    no_tempo_total,                                                                                         "
		+"    no_servidores                                                                                           "
		+"FROM                                                                                                        "
		+"    simulacao s                                                                                             "
		+"        INNER JOIN                                                                                          "
		+"    tipo_distribuicao dchegada ON dchegada.ID_TIPO_DISTRIBUICAO = s.ID_TIPO_DISTRIBUICAO_CHEGADA            "
		+"        INNER JOIN                                                                                          "
		+"    tipo_distribuicao datendimento ON datendimento.ID_TIPO_DISTRIBUICAO = s.ID_TIPO_DISTRIBUICAO_ATENDIMENTO"
		+"        INNER JOIN                                                                                          "
		+"    tipo_disciplina_fila tipoFila ON tipoFila.ID_TIPO_DISCIPLINA_FILA = s.ID_TIPO_DISCIPLINA_FILA           "
		+"    order by  id_simulacao																				  ";
        
        final String sqlBuscaParametrosSimulacao = 
		 "SELECT                                                              "
		 +"    paramsim.ID_PARAMETRO,                                         "
		 +"    SIGLA_PARAMETRO,                                               "
		 +"    NOME_PARAMETRO,                                                "
		 +"    ID_SIMULACAO,                                                  "
		 +"    VALOR_PARAMETRO,                                               "
		 +"    VALOR_TEMPO,                                                   "
		 +"    DISTRIBUICAO_TIPO                                              "
		 +"FROM                                                               "
		 +"    parametros_simulacao paramsim                                  "
		 +"        INNER JOIN                                                 "
		 +"    parametros param ON paramsim.ID_PARAMETRO = param.ID_PARAMETRO "
		 +"where DISTRIBUICAO_TIPO =? and ID_SIMULACAO = ?                    ";
        
        List<Simulacao> listSimulacao = jdbcTemplate.query(sqlSelectSimulacao, new RowMapper<Simulacao>() {
       	 
            public Simulacao mapRow(ResultSet result, int rowNum) throws SQLException {
            	Simulacao simulacao = new Simulacao();
            	simulacao.setIdSimulacao(result.getInt("id_simulacao"));
            	
            	simulacao.setIdTipoDistribuicaoChegada(result.getInt("ID_TIPO_DISTRIBUICAO_CHEGADA"));
            	simulacao.setSiglaDistribuicaoChegada(result.getString("SG_DISTRIBUICAO_CHEGADA"));
            	simulacao.setNomeDistribuicaoChegada(result.getString("NM_DISTRIBUICAO_CHEGADA"));
            	
     
            	String nomeParametrosChegada[] = new String[] {Constantes.TIPO_DISTRIBUICAO_CHEGADA,String.valueOf(simulacao.getIdSimulacao())};
            	List<Parametro> parametrosChegada = jdbcTemplate.query(sqlBuscaParametrosSimulacao,nomeParametrosChegada,new RowMapper<Parametro>(){
					@Override
					public Parametro mapRow(ResultSet rs, int arg1) throws SQLException {
					Parametro parametro = new Parametro();
						parametro.setIdTipoParametro(rs.getInt("ID_PARAMETRO"));
						parametro.setTipoDistribuicao(rs.getString("DISTRIBUICAO_TIPO"));
						parametro.setValorParametro(rs.getDouble("VALOR_PARAMETRO"));
						parametro.setValorTempo(rs.getDouble("VALOR_TEMPO"));
						parametro.setNomeParametro(rs.getString("NOME_PARAMETRO"));
						parametro.setSiglaParametro(rs.getString("SIGLA_PARAMETRO"));
						return parametro;
					}
            		
            	});
            	String nomeParametrosAtendimento[] = new String[] {Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO,String.valueOf(simulacao.getIdSimulacao())};
            	List<Parametro> parametrosAtendimento = jdbcTemplate.query(sqlBuscaParametrosSimulacao,nomeParametrosAtendimento,new RowMapper<Parametro>(){
					@Override
					public Parametro mapRow(ResultSet rs, int arg1) throws SQLException {
					Parametro parametro = new Parametro();
						parametro.setIdTipoParametro(rs.getInt("ID_PARAMETRO"));
						parametro.setTipoDistribuicao(rs.getString("DISTRIBUICAO_TIPO"));
						parametro.setValorParametro(rs.getDouble("VALOR_PARAMETRO"));
						parametro.setValorTempo(rs.getDouble("VALOR_TEMPO"));
						parametro.setNomeParametro(rs.getString("NOME_PARAMETRO"));
						parametro.setSiglaParametro(rs.getString("SIGLA_PARAMETRO"));
						return parametro;
					}
            		
            	});
            	simulacao.setParametrosChegada(toVectorParametro(parametrosChegada));
            	simulacao.setParametrosAtendimento(toVectorParametro(parametrosAtendimento));
            	simulacao.setIdTipoDistribuicaoAtendimento(result.getInt("ID_TIPO_DISTRIBUICAO_ATENDIMENTO"));
            	simulacao.setSiglaDistribuicaoAtendimento(result.getString("SG_DISTRIBUICAO_ATENDIMENTO"));
            	simulacao.setNomeDistribuicaoAtendimento(result.getString("NM_DISTRIBUICAO_ATENDIMENTO"));
               
            	simulacao.setSiglaTipoFila(result.getString("SIGLA_TIPO_FILA"));
                simulacao.setNomeTipoFila(result.getString("NOME_TIPO_FILA"));
                simulacao.setNoServidores(result.getInt("no_servidores"));
                
                simulacao.setTempoTotal(result.getInt("no_tempo_total"));
                return simulacao;
            }
             
        });
            
        return listSimulacao;
	}
	
	@Override
	public Simulacao getSimulacaoById(int id) throws SQLException {
		final JdbcTemplate jdbcTemplate = getJdbcTemplate();
		
        String sqlSelectSimulacao = 
		"SELECT                                                                                                       "
		+"    s.id_simulacao,                                                                                         "
		+"    dchegada.ID_TIPO_DISTRIBUICAO AS ID_TIPO_DISTRIBUICAO_CHEGADA,                                          "
		+"    dchegada.SIGLA_DISTRIBUICAO AS SG_DISTRIBUICAO_CHEGADA,                                                 "
		+"    dchegada.TIPO_DISTRIBUICAO AS NM_DISTRIBUICAO_CHEGADA,                                                  "
		+"    datendimento.ID_TIPO_DISTRIBUICAO AS ID_TIPO_DISTRIBUICAO_ATENDIMENTO,                                  "
		+"    datendimento.SIGLA_DISTRIBUICAO AS SG_DISTRIBUICAO_ATENDIMENTO,                                         "
		+"    datendimento.TIPO_DISTRIBUICAO AS NM_DISTRIBUICAO_ATENDIMENTO,                                          "
		+"    tipoFila.ID_TIPO_DISCIPLINA_FILA,                                                                       "
		+"    tipoFila.SIGLA_TIPO_FILA,                                                                               "
		+"    tipoFila.NOME_TIPO_FILA,                                                                                "
		+"    s.no_populacao,                                                                                         "
		+"    no_tempo_total,                                                                                         "
		+"    no_servidores                                                                                           "
		+"FROM                                                                                                        "
		+"    simulacao s                                                                                             "
		+"        INNER JOIN                                                                                          "
		+"    tipo_distribuicao dchegada ON dchegada.ID_TIPO_DISTRIBUICAO = s.ID_TIPO_DISTRIBUICAO_CHEGADA            "
		+"        INNER JOIN                                                                                          "
		+"    tipo_distribuicao datendimento ON datendimento.ID_TIPO_DISTRIBUICAO = s.ID_TIPO_DISTRIBUICAO_ATENDIMENTO"
		+"        INNER JOIN                                                                                          "
		+"    tipo_disciplina_fila tipoFila ON tipoFila.ID_TIPO_DISCIPLINA_FILA = s.ID_TIPO_DISCIPLINA_FILA           "
		+"    where id_simulacao  = ?																			      ";
        
        final String sqlBuscaParametrosSimulacao = 
		 "SELECT                                                              "
		 +"    paramsim.ID_PARAMETRO,                                         "
		 +"    SIGLA_PARAMETRO,                                               "
		 +"    NOME_PARAMETRO,                                                "
		 +"    ID_SIMULACAO,                                                  "
		 +"    VALOR_PARAMETRO,                                               "
		 +"    VALOR_TEMPO,                                                   "
		 +"    DISTRIBUICAO_TIPO                                              "
		 +"FROM                                                               "
		 +"    parametros_simulacao paramsim                                  "
		 +"        INNER JOIN                                                 "
		 +"    parametros param ON paramsim.ID_PARAMETRO = param.ID_PARAMETRO "
		 +"where DISTRIBUICAO_TIPO =? and ID_SIMULACAO = ?                    ";
        
        final String  sqlGetClientesPorSimulacao = 
        				 " SELECT                                      "
				        +"    ID_RESULTADO_CLIENTE,                    "
				        +"    ID_SIMULACAO,                            "
				        +"    NO_CLIENTE,                              "
				        +"    NO_POSICAO,                              "
				        +"    ATENDENTE,                               "
				        +"    TEMPO_ATENDIMENTO,                       "
				        +"    HORA_CHEGADA,                            "
				        +"    HORA_SAIDA,                              "
				        +"    ID_RESULTADO_CLIENTE                     "
				        +" FROM                                        "
				        +"    resultado_cliente where ID_SIMULACAO = ? ";
        
        final String  sqlGetEstatisticaPorSimulacao = 
        				 " SELECT                                                         "
        				+"    id_simulacao, id_estatistica, valor_media, valor_variancia  "
        				+" FROM                                                           "
        				+"    estatistica_simulacao where id_simulacao = ?                ";
        
	               
        final String idSimulacao[] = new String[] {String.valueOf(id)};
        
        List<Simulacao> listSimulacao = jdbcTemplate.query(sqlSelectSimulacao, idSimulacao, new RowMapper<Simulacao>() {
       	 
            public Simulacao mapRow(ResultSet result, int rowNum) throws SQLException {
            	Simulacao simulacao = new Simulacao();
            	simulacao.setIdSimulacao(result.getInt("id_simulacao"));
            	
            	simulacao.setIdTipoDistribuicaoChegada(result.getInt("ID_TIPO_DISTRIBUICAO_CHEGADA"));
            	simulacao.setSiglaDistribuicaoChegada(result.getString("SG_DISTRIBUICAO_CHEGADA"));
            	simulacao.setNomeDistribuicaoChegada(result.getString("NM_DISTRIBUICAO_CHEGADA"));
            	
     
            	String nomeParametrosChegada[] = new String[] {Constantes.TIPO_DISTRIBUICAO_CHEGADA,String.valueOf(simulacao.getIdSimulacao())};
            	List<Parametro> parametrosChegada = jdbcTemplate.query(sqlBuscaParametrosSimulacao,nomeParametrosChegada,new RowMapper<Parametro>(){
					@Override
					public Parametro mapRow(ResultSet rs, int arg1) throws SQLException {
					Parametro parametro = new Parametro();
						parametro.setIdTipoParametro(rs.getInt("ID_PARAMETRO"));
						parametro.setTipoDistribuicao(rs.getString("DISTRIBUICAO_TIPO"));
						parametro.setValorParametro(rs.getDouble("VALOR_PARAMETRO"));
						parametro.setValorTempo(rs.getDouble("VALOR_TEMPO"));
						parametro.setNomeParametro(rs.getString("NOME_PARAMETRO"));
						parametro.setSiglaParametro(rs.getString("SIGLA_PARAMETRO"));
						return parametro;
					}
            		
            	});
            	String nomeParametrosAtendimento[] = new String[] {Constantes.TIPO_DISTRIBUICAO_ATENDIMENTO,String.valueOf(simulacao.getIdSimulacao())};
            	List<Parametro> parametrosAtendimento = jdbcTemplate.query(sqlBuscaParametrosSimulacao,nomeParametrosAtendimento,new RowMapper<Parametro>(){
					@Override
					public Parametro mapRow(ResultSet rs, int arg1) throws SQLException {
					Parametro parametro = new Parametro();
						parametro.setIdTipoParametro(rs.getInt("ID_PARAMETRO"));
						parametro.setTipoDistribuicao(rs.getString("DISTRIBUICAO_TIPO"));
						parametro.setValorParametro(rs.getDouble("VALOR_PARAMETRO"));
						parametro.setValorTempo(rs.getDouble("VALOR_TEMPO"));
						parametro.setNomeParametro(rs.getString("NOME_PARAMETRO"));
						parametro.setSiglaParametro(rs.getString("SIGLA_PARAMETRO"));
						return parametro;
					}
            		
            	});
            	
            	List<Cliente> clientes = jdbcTemplate.query(sqlGetClientesPorSimulacao,idSimulacao,new RowMapper<Cliente>(){
					@Override
					public Cliente mapRow(ResultSet rs, int arg1) throws SQLException {
						int posicao = rs.getInt("NO_POSICAO");
						double horaChegada =  rs.getDouble("HORA_CHEGADA");
						Cliente cliente = new Cliente(posicao, horaChegada);
						cliente.setAtendente(rs.getInt("ATENDENTE"));
						cliente.setAtendido(true);
						cliente.setTempoAtendimento(rs.getDouble("TEMPO_ATENDIMENTO"));
						cliente.setHoraSaida(rs.getDouble("HORA_SAIDA"));
						cliente.setId(rs.getInt("NO_CLIENTE"));
						return cliente;
					}
            		
            	});
            	
            	List<Estatistica> estatisticas = jdbcTemplate.query(sqlGetEstatisticaPorSimulacao,idSimulacao,new RowMapper<Estatistica>(){
					@Override
					public Estatistica mapRow(ResultSet rs, int arg1) throws SQLException {
						
						int codigoEstatistica = rs.getInt("id_estatistica");
						double media =  rs.getDouble("valor_media");
						double variancia =  rs.getDouble("valor_variancia");
				
					   
						Estatistica estatistica = new Estatistica(codigoEstatistica, media, variancia);
						
						
						return estatistica;
					}
            		
            	});
            	
            	simulacao.setEstatisticas(toVectorEstatistica(estatisticas));
            	simulacao.setClientes(toVectorCliente(clientes));
            	simulacao.setParametrosChegada(toVectorParametro(parametrosChegada));
            	simulacao.setParametrosAtendimento(toVectorParametro(parametrosAtendimento));
            	simulacao.setIdTipoDistribuicaoAtendimento(result.getInt("ID_TIPO_DISTRIBUICAO_ATENDIMENTO"));
            	simulacao.setSiglaDistribuicaoAtendimento(result.getString("SG_DISTRIBUICAO_ATENDIMENTO"));
            	simulacao.setNomeDistribuicaoAtendimento(result.getString("NM_DISTRIBUICAO_ATENDIMENTO"));
               
            	simulacao.setSiglaTipoFila(result.getString("SIGLA_TIPO_FILA"));
                simulacao.setNomeTipoFila(result.getString("NOME_TIPO_FILA"));
                simulacao.setNoServidores(result.getInt("no_servidores"));
                
                simulacao.setTempoTotal(result.getInt("no_tempo_total"));
                return simulacao;
            }
             
        });
            
        return listSimulacao.get(0);
	}
	

	

	
	private Parametro[] toVectorParametro(List<Parametro> lista){
		int tamanhoLista = lista.size();
		Parametro[] parametros = new Parametro[lista.size()];

	    for(int i=0; i< tamanhoLista; i++){
	    	parametros[i]= lista.get(i);
	    }
		
	    return parametros;
	}
	
	private Cliente[] toVectorCliente(List<Cliente> lista){
		int tamanhoLista = lista.size();
		Cliente[] parametros = new Cliente[lista.size()];

	    for(int i=0; i< tamanhoLista; i++){
	    	parametros[i]= lista.get(i);
	    }
		
	    return parametros;
	}
	
	private Estatistica[] toVectorEstatistica(List<Estatistica> lista){
		int tamanhoLista = lista.size();
		Estatistica[] estatisticas = new Estatistica[lista.size()];

	    for(int i=0; i< tamanhoLista; i++){
	    	estatisticas[i]= lista.get(i);
	    }
		
	    return estatisticas;
	}
	
	private JdbcTemplate getJdbcTemplate() throws SQLException{
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("root");
		

		//PROD
		//dataSource.setPassword("12345678");
	    //dataSource.setUrl("jdbc:mysql://dbsimulafila.cgepkwykt1l8.us-west-2.rds.amazonaws.com:3306/simulafila");
		
	    //DEV
	    dataSource.setPassword("123456");
	    dataSource.setUrl("jdbc:mysql://localhost/simulafila");

	    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
}
