package br.com.spring.dominio;



public class Distribuicao{
	
	 private Integer ocorrencias;
	 private Double tempo;
	 private Double probabilidadeAcumulada;
	 
	public Distribuicao(Integer ocorrencias,Double probabilidadeAcumulada) {
		 this.ocorrencias = ocorrencias;
		 this.probabilidadeAcumulada = probabilidadeAcumulada;
		
     }
	public Distribuicao(Double tempo,Double probabilidadeAcumulada) {
		 this.tempo = tempo;
		 this.probabilidadeAcumulada = probabilidadeAcumulada;
		
    }
	public Distribuicao() {		
    }
	 
	public Integer getOcorrencias() {
		return ocorrencias;
	}
	
	public void setOcorrencias(Integer ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	public Double getProbabilidadeAcumulada() {
		return probabilidadeAcumulada;
	}
	
	public void setProbabilidadeAcumulada(Double probabilidadeAcumulada) {
		this.probabilidadeAcumulada = probabilidadeAcumulada;
	}

	public Double getTempo() {
		return tempo;
	}

	public void setTempo(Double tempo) {
		this.tempo = tempo;
	}
}
