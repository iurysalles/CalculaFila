package br.com.spring.util;

public class CalculaMMs {

	public static void main(String[] args) {
		
		double mu=1.0;
		double lambda = 10.0;
		int s= 12;
		
		double rho = lambda/mu;
		double p0 = 0.0;
		double temp = 0.0;
		
		// Inicio - Calculando P0
		for(int i=0;i<s;i++){
	
			temp += Math.pow(rho,i)/factorial(i);
			
		}
		temp = temp + (Math.pow(rho,s)/factorial(s))*((s*mu)/(s*mu - lambda));
		
		p0 = 1/temp;
		// Fim - Calculando P0
	
		
		double L = ((lambda*mu*Math.pow(mu*lambda,s))/(factorial(s-1)*Math.pow((s*mu-lambda),2)))*p0 + lambda/mu; 
		double Lq = L - rho;
		double Wq = Lq/lambda;
		double W = Wq + 1/mu;

		
		System.out.println("P0:" + p0);
		System.out.println("Lq:" + Lq);
		System.out.println("L:" +L);
		System.out.println("Wq:" + Wq);
		System.out.println("W:" +W);
		
	}
	
	public static int factorial(int n) {
	    int fact = 1; 
	    for (int i = 1; i <= n; i++) {
	        fact *= i;
	    }
	    return fact;
	}

}
