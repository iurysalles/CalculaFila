package br.com.spring.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;



public class Util {
	static MathContext mc = new MathContext(2, RoundingMode.CEILING);

	
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
	public static BigDecimal calculaCdfExponencial(double x, double lambda) {  
	    try { 
	    	if(x < 0)
	    		 return new BigDecimal(0);  
	    	else
	    		return (new BigDecimal(1).subtract(new BigDecimal(Math.pow(Math.E, -lambda*x))));
	    } catch (Exception nfex) {  
	        return new BigDecimal(0);  
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
	public static BigDecimal calculaCdfErlangk(double x, double lambda, int k) {  
	    try {
	    	BigDecimal x_1 = new BigDecimal(x);
	    	BigDecimal lambda_1 = new BigDecimal(lambda);
	    	BigDecimal sum = new BigDecimal(0);
	    	BigDecimal termo1 = lambda_1.multiply(x_1);
	    	BigDecimal e = new BigDecimal(Math.E);
	    	BigDecimal termo2 = new BigDecimal( Math.pow(e.doubleValue(), -(termo1.doubleValue()))   );
	    	BigDecimal um = new BigDecimal(1);
	    	
	    	for(int n=0;n<k;n++){
	    		sum = sum.add(um.divide(new BigDecimal(factorial(n)),mc).multiply(termo2.multiply(termo1.pow(n))));	
	    	}
	    	
	        return (um.subtract(sum));
	    } catch (Exception nfex) {  
	    	nfex.printStackTrace();
	        return BigDecimal.ZERO;  
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