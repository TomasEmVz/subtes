package subte.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class Configuracion {
	
	private static Configuracion configuracion = null;
	
	private Coordinador coordinador;

	private Map<Integer, Double> congestion;
	private ResourceBundle resourceBundle;	

	public static Configuracion getConfiguracion() {
		if (configuracion == null) {
			configuracion = new Configuracion();
		}
		return configuracion;
	}

	private Configuracion() {
		congestion = new HashMap<>();
	
		Properties prop = new Properties();
		InputStream input;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);

			congestion.put(1, Double.parseDouble(prop.getProperty("congestionBaja")));
			congestion.put(2, Double.parseDouble(prop.getProperty("congestionMedia")));
			congestion.put(3, Double.parseDouble(prop.getProperty("congestionAlta")));
			
			Locale.setDefault(new Locale(prop.getProperty("language"), prop.getProperty("country")));
			resourceBundle = ResourceBundle.getBundle(prop.getProperty("labels"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<Integer, Double> getCongestion() {		
		return congestion;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
	
}
