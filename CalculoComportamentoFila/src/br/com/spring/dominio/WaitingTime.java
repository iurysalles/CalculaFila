package br.com.spring.dominio;

public class WaitingTime {
	private Double tempo;
	private Double mediaTempoEspera;
	
	
	public void setTempo(Double tempo) {
		this.tempo = tempo;
	}
	public Double getTempo() {
		return tempo;
	}
	public void setMediaTempoEspera(Double mediaTempoEspera) {
		this.mediaTempoEspera = mediaTempoEspera;
	}
	public Double getMediaTempoEspera() {
		return mediaTempoEspera;
	}
	
}
