package br.com.spring.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ResourceBundleProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private static final Logger                log              = Logger.getLogger(ResourceBundleProperties.class.getName());

    private final String                    NOME_ARQUIVO     = "/jdbc.properties";

    private static ResourceBundleProperties ref;

    private ResourceBundleProperties() {
        super();
        load();
    }

    public static String getDriverClassName() {
        if (ref == null)
            ref = new ResourceBundleProperties();

        return ref.getProperty(Constantes.JDBC_CLASS_NAME);
    }
    
    public static String getUrlJdbc() {
        if (ref == null)
            ref = new ResourceBundleProperties();

        return ref.getProperty(Constantes.JDBC_URL);
    }
    
    public static String getUserName() {
        if (ref == null)
            ref = new ResourceBundleProperties();

        return ref.getProperty(Constantes.JDBC_USER);
    }
    
    public static String getPassword() {
        if (ref == null)
            ref = new ResourceBundleProperties();

        return ref.getProperty(Constantes.JDBC_PASSWORD);
    }
    
    public synchronized void load() {
        try {
            InputStream is = this.getClass().getResourceAsStream(NOME_ARQUIVO);
            load(is);
            is.close();
        }
        catch (IOException e) {
            log.severe("Ocorreu um erro ao ler arquivo de propriedades. " + e);
        }
    }

    public synchronized void store() throws IOException {
        OutputStream os = new FileOutputStream(this.getClass().getResource(NOME_ARQUIVO).getFile());
        super.store(os, null);
    }

}
