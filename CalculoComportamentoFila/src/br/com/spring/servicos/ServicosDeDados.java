package br.com.spring.servicos;


import java.sql.SQLException;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.spring.dados.ISimulacao;
import br.com.spring.dados.mysql.SimulacaoDadosMysql;
import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.Simulacao;

@Service
@Transactional
public class ServicosDeDados {
	
	
	@Transactional
	public void deleteSimulacao(int idSimulacao) throws SQLException {
		ISimulacao simulacaoDados = new SimulacaoDadosMysql();
		simulacaoDados.deleteSimulacao(idSimulacao);
	}
	
	@Transactional
	public void gravaSimulacao(Simulacao simulacao) throws SQLException {
		ISimulacao simulacaoDados = new SimulacaoDadosMysql();
		simulacaoDados.gravaSimulacao(simulacao);
	}
	
	@Transactional
	public Collection<Simulacao> listaSimulacoes() throws SQLException {
		ISimulacao simulacaoDados = new SimulacaoDadosMysql();
		return simulacaoDados.listaSimulacoes();
	}
	
	@Transactional
	public Simulacao getSimulacao(int idSimulacao) throws SQLException {
		ISimulacao simulacaoDados = new SimulacaoDadosMysql();
		return simulacaoDados.getSimulacaoById(idSimulacao);
	}
	
	@Transactional
	public Collection<Parametro> getDistribuicao(String idSimulacao, String tipoDistribuicao) throws SQLException {
		ISimulacao simulacaoDados = new SimulacaoDadosMysql();
		return simulacaoDados.getDistribuicao(idSimulacao,tipoDistribuicao);
	}
	
}
