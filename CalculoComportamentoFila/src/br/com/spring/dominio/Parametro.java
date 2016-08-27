package br.com.spring.dominio;

public class Parametro {
	private int idTipoParametro;
	private String tipoDistribuicao;
	private String nomeParametro;
	private String siglaParametro;
	private double valorParametro;
	private double valorTempo;
	
	public Parametro(){
	}
	
	public Parametro(int idTipoParametro, String tipoDistribuicao, double valorParametro){
		this.idTipoParametro = idTipoParametro;
		this.tipoDistribuicao = tipoDistribuicao;
		this.valorParametro = valorParametro;
	}
	
	public Parametro(int idTipoParametro, String tipoDistribuicao, double valorParametro, double valorTempo){
		this.idTipoParametro = idTipoParametro;
		this.tipoDistribuicao = tipoDistribuicao;
		this.valorParametro = valorParametro;
		this.valorTempo = valorTempo;
	}
	public void setIdTipoParametro(int idTipoParametro) {
		this.idTipoParametro = idTipoParametro;
	}
	public int getIdTipoParametro() {
		return idTipoParametro;
	}
	public void setTipoDistribuicao(String tipoDistribuicao) {
		this.tipoDistribuicao = tipoDistribuicao;
	}
	public String getTipoDistribuicao() {
		return tipoDistribuicao;
	}
	public void setValorParametro(double valorParametro) {
		this.valorParametro = valorParametro;
	}
	public double getValorParametro() {
		return valorParametro;
	}
	public void setValorTempo(double valorTempo) {
		this.valorTempo = valorTempo;
	}
	public double getValorTempo() {
		return valorTempo;
	}

	public String getNomeParametro() {
		return nomeParametro;
	}

	public void setNomeParametro(String nomeParametro) {
		this.nomeParametro = nomeParametro;
	}

	public String getSiglaParametro() {
		return siglaParametro;
	}

	public void setSiglaParametro(String siglaParametro) {
		this.siglaParametro = siglaParametro;
	}
	
	
}
