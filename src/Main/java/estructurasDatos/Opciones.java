package estructurasDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Objeto Opciones, este objeto almacena los parametros del fichero de propiedades Option.properties.
 * @author Tino
 *
 */
public class Opciones {
	/**
	 * Fichero que contiene distintas opciones del problema.
	 */
	private static Properties prop = new Properties();
	/**
	 * Tamaño usado en cada uno de los slots de tiempo en los que se divide el turno del problema. La unidad de medida esta establecida en minutos.
	 * No se garantiza su funcionamiento en esta version para slots con una tamaño diferente a 5 minutos.
	 */
	private int tamanoSlots;
	/**
	 * 
	 * @param nombreFichero Ruta del fichero con las opciones para la resolucion del problema.
	 */
	public Opciones(String nombreFichero){
		getProperties(nombreFichero);
		this.tamanoSlots = Integer.parseInt(prop.getProperty("tamañoDeSlots"));
	
	}
	
	
	public static void getProperties(String propFileName) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(propFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			getProp().load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static Properties getProp() {
		return prop;
	}
	public void setProp(Properties prop) {
		Opciones.prop = prop;
	}
	public int getTamanoSlots() {
		return tamanoSlots;
	}
	public void setTamanoSlots(int tamanoSlots) {
		this.tamanoSlots = tamanoSlots;
	}
}
