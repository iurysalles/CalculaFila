package br.com.spring.dados;

import java.sql.SQLException;
import java.util.Collection;

import br.com.spring.dominio.Parametro;
import br.com.spring.dominio.Simulacao;

public interface ISimulacao {

	
	/**
	 * grava a simula��o
	 * @return
	 */
	public void gravaSimulacao(Simulacao simulacao) throws SQLException;;
	
	/**
	 * get ultima simula��o
	 * @return
	 */
	public int getIdUltimaSimulacao() throws SQLException;
	
	/**
	 * lista simula��es
	 * @return
	 */
	public Collection<Simulacao> listaSimulacoes() throws SQLException;
	
	/**
	 * Simula��o by id
	 * @return
	 */
	public Simulacao getSimulacaoById(int id) throws SQLException;
	
	/**
	 * Simula��o Distribuicao
	 * @return
	 */
	public Collection<Parametro> getDistribuicao(String idSimulacao, String tipoDistribuicao) throws SQLException;
	
	/**
	 * Delete Simulacao
	 * @return
	 */
	public void deleteSimulacao(int idSimulacao) throws SQLException;

}
