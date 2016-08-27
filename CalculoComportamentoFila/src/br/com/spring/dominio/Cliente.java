package br.com.spring.dominio;

public class Cliente {
	 private Integer id;
	 private Double horaChegada;
	 private Double tempoAtendimento;
	 private Double horaSaida;
	 private Integer posicao;
	 private Integer atendente;
	 private boolean atendido;
	 
	public Cliente(Integer posicao,Double horaChegada){
		this.horaChegada = horaChegada;
		this.posicao = posicao;
	}
	
	public Double getTempoFila() {
		return (horaSaida - horaChegada);
	}
	public Double getHoraChegada() {
		return horaChegada;
	}

	public void setHoraChegada(Double horaChegada) {
		this.horaChegada = horaChegada;
	}

	public Double getTempoAtendimento() {
		return tempoAtendimento;
	}

	public void setTempoAtendimento(Double tempoAtendimento) {
		this.tempoAtendimento = tempoAtendimento;
	}

	public Double getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Double horaSaida) {
		this.horaSaida = horaSaida;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public Integer getAtendente() {
		return atendente;
	}

	public void setAtendente(Integer atendente) {
		this.atendente = atendente;
	}

	public boolean isAtendido() {
		return atendido;
	}

	public void setAtendido(boolean atendido) {
		this.atendido = atendido;
	}
	
	public Double getHoraAtendimento() {
		Double horaAtendimento = horaSaida - tempoAtendimento;
		
		if(horaAtendimento < horaChegada)
			return horaChegada;
		else
			return horaAtendimento;
	}
	
	public Double getTaxaAtendimento() {
			return 1.0/tempoAtendimento;
	}
	
	public Double getTempoEspera() {
		return getHoraAtendimento() - horaChegada;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

