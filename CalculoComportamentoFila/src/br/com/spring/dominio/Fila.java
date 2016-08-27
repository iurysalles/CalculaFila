package br.com.spring.dominio;

import java.util.Collection;

public class Fila {
	
	private Long tempo;
	Collection<Cliente> fila;
	
	
	public Long getTempo() {
		return tempo;
	}
	public void setTempo(Long tempo) {
		this.tempo = tempo;
	}
	public Collection<Cliente> getFila() {
		return fila;
	}
	public void setFila(Collection<Cliente> fila) {
		this.fila = fila;
	}
	public Integer getTamanho() {
		return this.fila.size();
	}
	
	
}
