package br.com.spring.dominio;

public class ServerUtilization {
	private Double percentual;
	private Long tempo;
	
	
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
	
	public Double getPercentual() {
		return percentual;
	}
	
	public void setTempo(Long tempo) {
		this.tempo = tempo;
	}
	
	public Long getTempo() {
		return tempo;
	}

}
