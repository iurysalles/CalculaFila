package br.com.spring.dominio;

import java.text.DecimalFormat;

public class Estatistica {
	DecimalFormat fmt = new DecimalFormat("0.0000");
	private int codigoEstatistica;
	private double media;
	private double variancia;
	 
	public Estatistica(int codigoEstatistica, double media, double variancia){
		this.codigoEstatistica = codigoEstatistica;
		this.media= media;
		this.variancia = variancia;
	}
	public Estatistica(){
	}
	 
	public double getMedia() {
		return media;
	}
	
	public void setMedia(double media) {
		this.media = media;
	}
	
	public String getMediaFormatado() {
		return fmt.format(media);
	}
	
	public double getVariancia() {
		return variancia;
	}
	
	public void setVariancia(double variancia) {
		this.variancia = variancia;
	}
	
	public String getVarianciaFormatado() {
		return fmt.format(variancia);
	}
	
	public double getDesvioPadrao(){
		return Math.sqrt(variancia);
	}
	
	public String getDesvioPadraoFormatado(){
		return fmt.format(getDesvioPadrao());
	}
	
	public int getCodigoEstatistica() {
		return codigoEstatistica;
	}
	
	public void setCodigoEstatistica(int codigoEstatistica) {
		this.codigoEstatistica = codigoEstatistica;
	}

}
