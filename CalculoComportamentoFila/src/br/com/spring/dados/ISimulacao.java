package br.com.spring.dados;

import java.sql.SQLException;
import java.util.Collection;

import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.Simulacao;

public interface ISimulacao {

	
	/**
	 * grava a simulação
	 * @return
	 */
	public void gravaSimulacao(Simulacao simulacao) throws SQLException;;
	
	/**
	 * get ultima simulação
	 * @return
	 */
	public int getIdUltimaSimulacao() throws SQLException;
	
	/**
	 * lista simulações
	 * @return
	 */
	public Collection<Simulacao> listaSimulacoes() throws SQLException;
	
	/**
	 * Simulação by id
	 * @return
	 */
	public Simulacao getSimulacaoById(int id) throws SQLException;
	
	/**
	 * Simulação Distribuicao
	 * @return
	 */
	public Collection<Parametro> getDistribuicao(String idSimulacao, String tipoDistribuicao) throws SQLException;
	
	/**
	 * Delete Simulacao
	 * @return
	 */
	public void deleteSimulacao(int idSimulacao) throws SQLException;

}
