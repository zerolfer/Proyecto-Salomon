package estructurasDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * En este objeto se cargan los parametros del problema mediante los ficheros problemParameters.properties y options.properties, los cuales se encuentran en la carpeta CRIDA/recursos
 * @author Tino
 *
 */
public class Parametros {
	/**
	 * Fichero que contiene los parametros del problema.
	 */
	private static Properties propParametros = new Properties();
	/**
	 * Fichero que contiene distintas opciones del problema.
	 */
	private static Properties propOpciones = new Properties();
	/**
	 * Porcentaje de descanso obligatorio durante un turno diurno. El porcentaje es representado entre 0 y 1.
	 */
	private double porcentDescansoDia;
	/**
	 * Porcentaje de descanso obligatorio durante un turno nocturno. El porcentaje es representado entre 0 y 1.
	 */
	private double porcentDescansoNoche;
	/**
	 * Tiempo de trabajo maximo continuo permitido para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoTrabMax;
	/**
	 * Tiempo de trabajo minimo continuo permitido para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoTrabMin;
	/**
	 * Tiempo de descanso minimo continuo permitido para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoDesMin;
	/**
	 * Tiempo de descanso minimo permitido para un controladoren un intervalo de 2 horas y media. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoDesPorTrabajo;
	/**
	 * Tiempo de trabajo minimo continuo en una posicion permitido para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoPosMin;
	/**
	 * Tiempo de trabajo optimo continuo en una posicion para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoPosOpt;
	/**
	 * Tiempo de trabajo optimo continuo para un controlador. La unidad de medida esta establecida en minutos.
	 */
	private int tiempoTrabOpt;
	/**
	 * Numero maximo de sectores elementales que un controlador puede tener asignados.
	 */
	private int numSctrsMax ;
	/**
	 * Porcentaje maximo de trabajo deseable entre las posiciones de ejecutivo y planificador.  El porcentaje es representado entre 0 y 1.
	 */
	private double porcentPosMax;
	/**
	 * Porcentaje minimo de trabajo deseable entre las posiciones de ejecutivo y planificador.  El porcentaje es representado entre 0 y 1.
	 */
	private double porcentPosMin;
	/**
	 * Tamaño usado en cada uno de los slots de tiempo en los que se divide el turno del problema. La unidad de medida esta establecida en minutos.
	 * No se garantiza su funcionamiento en esta version para slots con una tamaño diferente a 5 minutos.
	 */
	private int tamanoSlots;
	/**
	 * Objeto donde se almacenan los distintos pesos asignados a la funcion objetivo de la fase 3.
	 */
	private PesosObjetivos pesosObjetivos;
	/**
	 * 
	 * @param nombreFicheroP Ruta del fichero con las propiedades del problema.
	 * @param nombreFicheroO Ruta del fichero con las opciones para la resolucion del problema.
	 */
	public Parametros(String nombreFicheroP, String nombreFicheroO){
		getPropertiesP(nombreFicheroP);
		this.porcentDescansoDia = Double.parseDouble(propParametros.getProperty("porcentajeDeDescansoDuranteDia"));
		this.porcentDescansoNoche = Double.parseDouble(propParametros.getProperty("porcentajeDeDescansoDuranteNoche"));
		this.tiempoTrabMax = Integer.parseInt(propParametros.getProperty("tiempoDeTrabajoMaximo"));
		this.tiempoTrabMin = Integer.parseInt(propParametros.getProperty("tiempoDeTrabajoMinimo"));
		this.tiempoDesMin = Integer.parseInt(propParametros.getProperty("tiempoDeDescansoMinimo"));
		this.tiempoDesPorTrabajo = Integer.parseInt(propParametros.getProperty("tiempoDeDescansoNoContinuoMinimo"));
		this.tiempoPosMin = Integer.parseInt(propParametros.getProperty("tiempoDeTrabajoEnPosicionMinimo"));
		this.tiempoPosOpt = Integer.parseInt(propParametros.getProperty("tiempoDeTrabajoEnPosicionOptimo"));
		this.tiempoTrabOpt = Integer.parseInt(propParametros.getProperty("tiempoDeTrabajoOptimo"));
		this.numSctrsMax = Integer.parseInt(propParametros.getProperty("numeroDeSectoresDistintosMaximo"));
		this.porcentPosMax = Double.parseDouble(propParametros.getProperty("porcentajeDeTrabajoMaximoEnPosicion"));
		this.porcentPosMin = Double.parseDouble(propParametros.getProperty("porcentajeDeTrabajoMinimoEnPosicion"));
		this.pesosObjetivos = (new PesosObjetivos(nombreFicheroP));

		getPropertiesO(nombreFicheroO);
		this.tamanoSlots = Integer.parseInt(propOpciones.getProperty("tamañoDeSlots"));
	}
	
	public static Properties getPropParametros() {
		return propParametros;
	}

	public static void setPropParametros(Properties propParametros) {
		Parametros.propParametros = propParametros;
	}

	public static Properties getPropOpciones() {
		return propOpciones;
	}

	public static void setPropOpciones(Properties propOpciones) {
		Parametros.propOpciones = propOpciones;
	}

	public double getPorcentDescansoDia() {
		return porcentDescansoDia;
	}

	public void setPorcentDescansoDia(double porcentDescansoDia) {
		this.porcentDescansoDia = porcentDescansoDia;
	}

	public double getPorcentDescansoNoche() {
		return porcentDescansoNoche;
	}

	public void setPorcentDescansoNoche(double porcentDescansoNoche) {
		this.porcentDescansoNoche = porcentDescansoNoche;
	}

	public int getTiempoTrabMax() {
		return tiempoTrabMax;
	}

	public void setTiempoTrabMax(int tiempoTrabMax) {
		this.tiempoTrabMax = tiempoTrabMax;
	}

	public int getTiempoTrabMin() {
		return tiempoTrabMin;
	}

	public void setTiempoTrabMin(int tiempoTrabMin) {
		this.tiempoTrabMin = tiempoTrabMin;
	}

	public int getTiempoDesMin() {
		return tiempoDesMin;
	}

	public void setTiempoDesMin(int tiempoDesMin) {
		this.tiempoDesMin = tiempoDesMin;
	}

	public int getTiempoDesPorTrabajo() {
		return tiempoDesPorTrabajo;
	}

	public void setTiempoDesPorTrabajo(int tiempoDesPorTrabajo) {
		this.tiempoDesPorTrabajo = tiempoDesPorTrabajo;
	}

	public int getTiempoPosMin() {
		return tiempoPosMin;
	}

	public void setTiempoPosMin(int tiempoPosMin) {
		this.tiempoPosMin = tiempoPosMin;
	}

	public int getTiempoPosOpt() {
		return tiempoPosOpt;
	}

	public void setTiempoPosOpt(int tiempoPosOpt) {
		this.tiempoPosOpt = tiempoPosOpt;
	}

	public int getTiempoTrabOpt() {
		return tiempoTrabOpt;
	}

	public void setTiempoTrabOpt(int tiempoTrabOpt) {
		this.tiempoTrabOpt = tiempoTrabOpt;
	}

	public int getNumSctrsMax() {
		return numSctrsMax;
	}

	public void setNumSctrsMax(int numSctrsMax) {
		this.numSctrsMax = numSctrsMax;
	}

	public double getPorcentPosMax() {
		return porcentPosMax;
	}

	public void setPorcentPosMax(double porcentPosMax) {
		this.porcentPosMax = porcentPosMax;
	}

	public double getPorcentPosMin() {
		return porcentPosMin;
	}

	public void setPorcentPosMin(double porcentPosMin) {
		this.porcentPosMin = porcentPosMin;
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
	public static void getPropertiesO(String propFileName) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(propFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			getPropOpciones().load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTamanoSlots() {
		return tamanoSlots;
	}

	public void setTamanoSlots(int tamanoSlots) {
		this.tamanoSlots = tamanoSlots;
	}

	public PesosObjetivos getPesosObjetivos() {
		return pesosObjetivos;
	}

	public void setPesosObjetivos(PesosObjetivos pesosObjetivos) {
		this.pesosObjetivos = pesosObjetivos;
	}
	
}
