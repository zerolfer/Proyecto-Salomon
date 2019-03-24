package estructurasDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import trazas.Trazas;
/**
 * En este objeto se cargan los parametros del algoritmo usado para la resolucion del problema mediante el fichero algorithm.properties, el cual se encuentra en la carpeta CRIDA/recursos
 * @author Tino
 *
 */
public class ParametrosAlgoritmo_SA {
	/**
	 * Fichero que contiene los parametros del algoritmo de resolucion.
	 */
	private static Properties propParametrosAlgoritmo = new Properties();
	/**
	 * Temperatura inicial del recocido simulado.
	 */
	private double temperaturaInicial;
	/**
	 * Porcentaje de descenso de la temperatura inicial.
	 */
	private double descensoTemperatura;
	/**
	 * Numero de iteraciones hasta que se produce un descenso de la temperatura.
	 */
	private int iteracionesTemperaturaL;
	/**
	 * Porcentaje de mejora que el algoritmo debe superar en X iteraciones para que este no finalice.
	 */
	private double condicionParadaPorcent;
	/**
	 * Numero de ciclos necesarios para que se compruebe el porcentaje de mejora necesario para finalizar el algoritmo.
	 */
	private int condicionParadaCiclos;
	/**
	 * Condicion de parada adiccional, la cual evita que finalice el algoritmo en las fases iniciales con temperatura elevada por no mejorar la solucion durante el proceso de exploracion.
	 * Mientras se acepte un porcentaje de soluciones superior al establecido el algoritmo sigue iterando.
	 */
	private double condicionParadaNumeroMejoras;
	/**
	 * Numero maximo de slots que se permiten en los movimiento utilizados para generar soluciones del entorno. Esta unidad se representa en minutos.
	 */
	private int tamañoMaxMov;
	/**
	 * Numero minimo de slots que se permiten en los movimiento utilizados para generar soluciones del entorno. Esta unidad se representa en minutos.
	 */
	private int tamañoMinMov;
	/**
	 * Nombre de la funcion objetivo que se utiliza en la fase2 del algoritmo.
	 */
	private String funcionFitnessFase2;
	/**
	 * Nombre de la funcion objetivo que se utiliza en la fase3 del algoritmo.
	 */
	private String funcionFitnessFase3;
	/**
	 * Nombre del movimiento escogido para generar soluciones del entorno por el algoritmo.
	 */
	private String movimientosEntorno;
	/**
	 * Si el movimiento esta formado por dos sub-movimientos, la probabilidad de eleccion del segundo movimiento frente al primero.
	 */
	private double porcentajeEleccionMov;
	
	private String algoritmo;
	private String movimientosEntornoGreedy;
	
	/**
	 * 
	 * @param nombreFicheroP Ruta del fichero con los parametros del algoritmo.
	 */
	public ParametrosAlgoritmo_SA(String nombreFicheroP){
		getPropertiesP(nombreFicheroP);
		this.setAlgoritmo(propParametrosAlgoritmo.getProperty("algoritmo"));
		this.temperaturaInicial = Double.parseDouble(propParametrosAlgoritmo.getProperty("temperaturaInicial"));
		this.descensoTemperatura = Double.parseDouble(propParametrosAlgoritmo.getProperty("descensoTemperatura"));
		this.iteracionesTemperaturaL = Integer.parseInt(propParametrosAlgoritmo.getProperty("iteracionesTemperatura"));
		this.condicionParadaPorcent = Double.parseDouble(propParametrosAlgoritmo.getProperty("condicionParadaPorcent"));
		this.condicionParadaCiclos = Integer.parseInt(propParametrosAlgoritmo.getProperty("condicionParadaCiclos"));
		this.condicionParadaNumeroMejoras = Double.parseDouble(propParametrosAlgoritmo.getProperty("condicionParadaNumeroMejoras"));
		this.tamañoMaxMov = Integer.parseInt(propParametrosAlgoritmo.getProperty("tamañoMaxMov"));
		this.tamañoMinMov = Integer.parseInt(propParametrosAlgoritmo.getProperty("tamañoMinMov"));
		Trazas.setnTmnsIntervalos(((tamañoMaxMov-tamañoMinMov)/3));
		this.funcionFitnessFase2 = (propParametrosAlgoritmo.getProperty("funcionFitnessFase2"));
		this.setFuncionFitnessFase3((propParametrosAlgoritmo.getProperty("funcionFitnessFase3")));
		this.movimientosEntorno = (propParametrosAlgoritmo.getProperty("movimientosEntorno"));
		this.setMovimientosEntornoGreedy(propParametrosAlgoritmo.getProperty("movimientosEntornoGreedy"));
		this.porcentajeEleccionMov = Double.parseDouble(propParametrosAlgoritmo.getProperty("porcentajeEleccionMov"));
	}
	
	public static Properties getPropParametrosAlgoritmo() {
		return propParametrosAlgoritmo;
	}

	public static void setPropParametrosAlgoritmo(Properties propParametrosAlgoritmo) {
		ParametrosAlgoritmo_SA.propParametrosAlgoritmo = propParametrosAlgoritmo;
	}

	public double getTemperaturaInicial() {
		return temperaturaInicial;
	}

	public void setTemperaturaInicial(double temperaturaInicial) {
		this.temperaturaInicial = temperaturaInicial;
	}

	public double getDescensoTemperatura() {
		return descensoTemperatura;
	}

	public void setDescensoTemperatura(double descensoTemperatura) {
		this.descensoTemperatura = descensoTemperatura;
	}

	public int getIteracionesTemperaturaL() {
		return iteracionesTemperaturaL;
	}

	public void setIteracionesTemperaturaL(int iteracionesTemperaturaL) {
		this.iteracionesTemperaturaL = iteracionesTemperaturaL;
	}

	public double getCondicionParadaPorcent() {
		return condicionParadaPorcent;
	}

	public void setCondicionParadaPorcent(double condicionParadaPorcent) {
		this.condicionParadaPorcent = condicionParadaPorcent;
	}

	public int getCondicionParadaCiclos() {
		return condicionParadaCiclos;
	}

	public void setCondicionParadaCiclos(int condicionParadaCiclos) {
		this.condicionParadaCiclos = condicionParadaCiclos;
	}

	public double getCondicionParadaNumeroMejoras() {
		return condicionParadaNumeroMejoras;
	}

	public void setCondicionParadaNumeroMejoras(double condicionParadaNumeroMejoras) {
		this.condicionParadaNumeroMejoras = condicionParadaNumeroMejoras;
	}

	public int getTamañoMaxMov() {
		return tamañoMaxMov;
	}

	public void setTamañoMaxMov(int tamañoMaxMov) {
		this.tamañoMaxMov = tamañoMaxMov;
	}

	public int getTamañoMinMov() {
		return tamañoMinMov;
	}

	public void setTamañoMinMov(int tamañoMinMov) {
		this.tamañoMinMov = tamañoMinMov;
	}

	public static Properties getPropParametros() {
		return propParametrosAlgoritmo;
	}

	public static void setPropParametros(Properties propParametros) {
		ParametrosAlgoritmo_SA.propParametrosAlgoritmo = propParametros;
	}

	public static void getPropertiesP(String propFileName) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(propFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			getPropParametros().load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFuncionFitnessFase2() {
		return funcionFitnessFase2;
	}

	public void setFuncionFitnessFase2(String funcionFitnessFase2) {
		this.funcionFitnessFase2 = funcionFitnessFase2;
	}

	public String getMovimientosEntorno() {
		return movimientosEntorno;
	}

	public void setMovimientosEntorno(String movimientosEntorno) {
		this.movimientosEntorno = movimientosEntorno;
	}

	public double getPorcentajeEleccionMov() {
		return porcentajeEleccionMov;
	}

	public void setPorcentajeEleccionMov(double porcentajeEleccionMov) {
		this.porcentajeEleccionMov = porcentajeEleccionMov;
	}

	public String getFuncionFitnessFase3() {
		return funcionFitnessFase3;
	}

	public void setFuncionFitnessFase3(String funcionFitnessFase3) {
		this.funcionFitnessFase3 = funcionFitnessFase3;
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

	public String getMovimientosEntornoGreedy() {
		return movimientosEntornoGreedy;
	}

	public void setMovimientosEntornoGreedy(String movimientosEntornoGreedy) {
		this.movimientosEntornoGreedy = movimientosEntornoGreedy;
	}

}
