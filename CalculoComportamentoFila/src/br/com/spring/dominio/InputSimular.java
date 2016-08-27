package br.com.spring.dominio;

import org.springframework.web.multipart.MultipartFile;

public class InputSimular {
	   private MultipartFile[] planilhaCargaChegada;
	   private MultipartFile[] planilhaCargaAtendimento;
	   
	   private Integer inputTipoChegada;
	   private Double  taxaMediaChegada;
	   private Integer taxaChegadaDeterministico;
	   private Integer parametroDeFormaChegadaErlang;
	   private Double  taxaChegadaErlang;
	   
	   private Integer inputTipoAtendimento;
	   private Double  taxaMediaAtendimento;
	   private Double taxaAtendimentoDeterministico;
	   private Integer parametroDeFormaAtendimentoErlang;
	   private Double taxaAtendimentoErlang;
	   
	   private Integer tamanhoPopulacao;
	   private Integer inputTipoFila;
	   private Integer servidores;
	   private Integer tempo;

	   
	   
	public MultipartFile[] getPlanilhaCargaChegada() {
		return planilhaCargaChegada;
	}
	
	public void setPlanilhaCargaChegada(MultipartFile[] planilhaCargaChegada) {
		this.planilhaCargaChegada = planilhaCargaChegada;
	}
	
	public MultipartFile[] getPlanilhaCargaAtendimento() {
		return planilhaCargaAtendimento;
	}
	
	public void setPlanilhaCargaAtendimento(MultipartFile[] planilhaCargaAtendimento) {
		this.planilhaCargaAtendimento = planilhaCargaAtendimento;
	}
	
	public Integer getServidores() {
		return servidores;
	}
	
	public void setServidores(Integer servidores) {
		this.servidores = servidores;
	}

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public Integer getInputTipoChegada() {
		return inputTipoChegada;
	}

	public void setInputTipoChegada(Integer inputTipoChegada) {
		this.inputTipoChegada = inputTipoChegada;
	}

	public Double getTaxaMediaChegada() {
		return taxaMediaChegada;
	}

	public void setTaxaMediaChegada(Double taxaMediaChegada) {
		this.taxaMediaChegada = taxaMediaChegada;
	}

	public Integer getTaxaChegadaDeterministico() {
		return taxaChegadaDeterministico;
	}

	public void setTaxaChegadaDeterministico(Integer taxaChegadaDeterministico) {
		this.taxaChegadaDeterministico = taxaChegadaDeterministico;
	}

	public Integer getInputTipoAtendimento() {
		return inputTipoAtendimento;
	}

	public void setInputTipoAtendimento(Integer inputTipoAtendimento) {
		this.inputTipoAtendimento = inputTipoAtendimento;
	}

	public Double getTaxaMediaAtendimento() {
		return taxaMediaAtendimento;
	}

	public void setTaxaMediaAtendimento(Double taxaMediaAtendimento) {
		this.taxaMediaAtendimento = taxaMediaAtendimento;
	}

	public Double getTaxaAtendimentoDeterministico() {
		return taxaAtendimentoDeterministico;
	}

	public void setTaxaAtendimentoDeterministico(
			Double taxaAtendimentoDeterministico) {
		this.taxaAtendimentoDeterministico = taxaAtendimentoDeterministico;
	}

	public Integer getParametroDeFormaChegadaErlang() {
		return parametroDeFormaChegadaErlang;
	}

	public void setParametroDeFormaChegadaErlang(
			Integer parametroDeFormaChegadaErlang) {
		this.parametroDeFormaChegadaErlang = parametroDeFormaChegadaErlang;
	}

	public Integer getParametroDeFormaAtendimentoErlang() {
		return parametroDeFormaAtendimentoErlang;
	}

	public void setParametroDeFormaAtendimentoErlang(
			Integer parametroDeFormaAtendimentoErlang) {
		this.parametroDeFormaAtendimentoErlang = parametroDeFormaAtendimentoErlang;
	}

	public Double getTaxaChegadaErlang() {
		return taxaChegadaErlang;
	}

	public void setTaxaChegadaErlang(Double taxaChegadaErlang) {
		this.taxaChegadaErlang = taxaChegadaErlang;
	}

	public Double getTaxaAtendimentoErlang() {
		return taxaAtendimentoErlang;
	}

	public void setTaxaAtendimentoErlang(Double taxaAtendimentoErlang) {
		this.taxaAtendimentoErlang = taxaAtendimentoErlang;
	}

	public void setInputTipoFila(Integer inputTipoFila) {
		this.inputTipoFila = inputTipoFila;
	}

	public Integer getInputTipoFila() {
		return inputTipoFila;
	}

	public Integer getTamanhoPopulacao() {
		return tamanhoPopulacao;
	}

	public void setTamanhoPopulacao(Integer tamanhoPopulacao) {
		this.tamanhoPopulacao = tamanhoPopulacao;
	}


	 
	}
