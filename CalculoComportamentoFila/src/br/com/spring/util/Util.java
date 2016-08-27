package br.com.spring.util;



public class Util {


	
	/**
	 * Verifica se texto é numero válido
	 * @param texto
	 * @return true> se texto é número válido / False> não é númerico
	 */
	public static boolean isNumeroTry(String texto) {  
	    try {  
	        Integer.parseInt(texto);  
	        return true;  
	    } catch (NumberFormatException nfex) {  
	        return false;  
	    }  
	}
	
	/**
	 * Calcula Probabilidade Exponencial
	 * @param lambida
	 * @param x
	 * @return função probabilidade de Exponencial
	 */
	public static double calculaProbabilidadeExponencial(double x, double lambda) {  
	    try { 
	    	if(x < 0)
	    		return 0;
	    	else
	    		return Math.pow(Math.E*lambda, -lambda * x);
	    } catch (Exception nfex) {  
	        return 0;  
	    }  
	}
	
	/**
	 * Calcula Probabilidade Poisson
	 * @param lambida
	 * @param x
	 * @return função probabilidade de poisson
	 */
	public static double calculaProbabilidadePoisson(int x, double lambda) {  
	    try {
	        return (Math.pow(Math.E,-lambda)*Math.pow(lambda, x))/factorial(x);
	    } catch (Exception nfex) {  
	        return 0;  
	    }  
	}
	
	/**
	 * Calcula Probabilidade Erlang k
	 * @param lambida
	 * @param k
	 * @param x
	 * @return função probabilidade de Erlang K
	 */
	public static double calculaProbabilidadeErlangk(int x, double lambda, int k) {  
	    try {
	        return (Math.pow(lambda,k)*Math.pow(x,k-1)*Math.pow(Math.E,-lambda*x))/factorial(k-1);
	    } catch (Exception nfex) {  
	    	nfex.printStackTrace();
	        return 0;  
	    }  
	}
	
	/**
	 * Calcula Fatorial 
	 * @param n
	 * @return função fatorial
	 */
	 public static int factorial(int n) {
		 int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
	 }
	 
	 /**
	 * Calcula Variancia Global
	 * @param vetor[double]
	 * @param media
	 * @return double Variancia
	 */
	 public static double calculaVarianciaGlobal(double[] vetor,double media) {

		double variancia = 0;
		
	    for (int i = 0; i < vetor.length; i++) {
	        variancia = variancia + Math.pow((media -vetor[i]), 2);
	    }
	    
	    variancia = variancia/(vetor.length-1);
	    
	    return variancia;
	 }
	
}