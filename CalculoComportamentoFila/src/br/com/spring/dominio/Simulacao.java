package br.com.spring.dominio;


public class Simulacao {
	private int idSimulacao;
	private int idTipoDistribuicaoChegada;
	private int idTipoDistribuicaoAtendimento;
	private int tipoDisciplinaFila;
	private int noPopulacao;
	private int tempoTotal;
	private int noServidores;
	
	private String siglaDistribuicaoChegada;
	private String nomeDistribuicaoChegada;
	
	private String siglaDistribuicaoAtendimento;
	private String nomeDistribuicaoAtendimento;
	
	private String siglaTipoFila;
	private String nomeTipoFila;
	
	private Estatistica[] estatisticas;
	private Parametro[] parametrosChegada;
	private Parametro[] parametrosAtendimento;
	private Cliente[] clientes;
	
	public int getIdTipoDistribuicaoChegada() {
		return idTipoDistribuicaoChegada;
	}
	public void setIdTipoDistribuicaoChegada(int idTipoDistribuicaoChegada) {
		this.idTipoDistribuicaoChegada = idTipoDistribuicaoChegada;
	}
	public int getIdTipoDistribuicaoAtendimento() {
		return idTipoDistribuicaoAtendimento;
	}
	public void setIdTipoDistribuicaoAtendimento(int idTipoDistribuicaoAtendimento) {
		this.idTipoDistribuicaoAtendimento = idTipoDistribuicaoAtendimento;
	}
	public int getTipoDisciplinaFila() {
		return tipoDisciplinaFila;
	}
	public void setTipoDisciplinaFila(int tipoDisciplinaFila) {
		this.tipoDisciplinaFila = tipoDisciplinaFila;
	}
	public int getNoPopulacao() {
		return noPopulacao;
	}
	public void setNoPopulacao(int noPopulacao) {
		this.noPopulacao = noPopulacao;
	}
	public int getTempoTotal() {
		return tempoTotal;
	}
	public void setTempoTotal(int tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
	public int getNoServidores() {
		return noServidores;
	}
	public void setNoServidores(int noServidores) {
		this.noServidores = noServidores;
	}
	public Estatistica[] getEstatisticas() {
		return estatisticas;
	}
	public void setEstatisticas(
			Estatistica[] estatisticas) {
		this.estatisticas = estatisticas;
	}
	public int getIdSimulacao() {
		return idSimulacao;
	}
	public void setIdSimulacao(int idSimulacao) {
		this.idSimulacao = idSimulacao;
	}
	public Parametro[] getParametrosChegada() {
		return parametrosChegada;
	}
	public void setParametrosChegada(Parametro[] parametrosChegada) {
		this.parametrosChegada = parametrosChegada;
	}
	public Parametro[] getParametrosAtendimento() {
		return parametrosAtendimento;
	}
	public void setParametrosAtendimento(Parametro[] parametrosAtendimento) {
		this.parametrosAtendimento = parametrosAtendimento;
	}
	public Cliente[] getClientes() {
		return clientes;
	}
	public void setClientes(Cliente[] clientes) {
		this.clientes = clientes;
	}
	public String getSiglaDistribuicaoChegada() {
		return siglaDistribuicaoChegada;
	}
	public void setSiglaDistribuicaoChegada(String siglaDistribuicaoChegada) {
		this.siglaDistribuicaoChegada = siglaDistribuicaoChegada;
	}
	public String getNomeDistribuicaoChegada() {
		return nomeDistribuicaoChegada;
	}
	public void setNomeDistribuicaoChegada(String nomeDistribuicaoChegada) {
		this.nomeDistribuicaoChegada = nomeDistribuicaoChegada;
	}
	public String getSiglaDistribuicaoAtendimento() {
		return siglaDistribuicaoAtendimento;
	}
	public void setSiglaDistribuicaoAtendimento(
			String siglaDistribuicaoAtendimento) {
		this.siglaDistribuicaoAtendimento = siglaDistribuicaoAtendimento;
	}
	public String getNomeDistribuicaoAtendimento() {
		return nomeDistribuicaoAtendimento;
	}
	public void setNomeDistribuicaoAtendimento(
			String nomeDistribuicaoAtendimento) {
		this.nomeDistribuicaoAtendimento = nomeDistribuicaoAtendimento;
	}
	public String getSiglaTipoFila() {
		return siglaTipoFila;
	}
	public void setSiglaTipoFila(String siglaTipoFila) {
		this.siglaTipoFila = siglaTipoFila;
	}
	public String getNomeTipoFila() {
		return nomeTipoFila;
	}
	public void setNomeTipoFila(String nomeTipoFila) {
		this.nomeTipoFila = nomeTipoFila;
	}

	
}
