package br.com.spring.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ManipulaArquivos {
	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"C:/Users/IURY/git/CalculaFila/git/CalculoComportamentoFila/properties/jdbc.properties");
		props.load(file);
		return props;

	}
	
}
