package herramientas.pruebasVarias.pruebaVelRegex;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import InicializarPoblacion.InicializarPoblacion;
import algorithms.simulatedAnnealing.moves.DeciderMove;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;
import patrones.RestricionesParalelizadas;
import pruebasCasos.DeciderCase;

public class pruebasTiemposMain {
	public static String propFileParameters = "resources/problemParameters.properties";
	public static String propFileOptions= "resources/options.properties";
	public static String propFileParametersAlgorithm = "resources/algorithm.properties";
	
	public static String entradaPath = "Caso2";
	public static String entradaId = "Id1n-06-03-2017";
	public static String entorno = "Canarias";
	
	public static String carpetaSoluciones= "resultados/"+entradaPath+entradaId+"/Soluciones/";
	public static String carpetaTrazas= "resultados/"+entradaPath+entradaId+"/Trazas/";
	
	
	public static void main(String[] args) { //PARALELOVSSECUENCIAL
		/*Iniciar patrones*/
		int[] it = {1000,5000,10000,50000,100000,250000};long t1=0,t2=0,t3=0,t4=0,t5=0,t6=0;
		DeciderCase.switchCase("Caso10");//El caso4 no tiene solucion
		Parametros parametros = new Parametros(propFileParameters, propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlg = new ParametrosAlgoritmo_SA(propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, entradaPath,entradaId,entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		/*Iniciar solucion*/
		
		for(int j=0;j<it.length;j++) {
			Solucion individuo = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones).get(0);
			for(int i=0;i<it[j];i++) {
				/*Comprobar restricciones Sec*/
				t1 += RestriccionesParalelo.comprobarRestriccionesSecuencial(individuo, parametros, entrada, patrones);
				/*Comprobar restricciones Par (1hilo)*/
				t2 += RestriccionesParalelo.comprobarRestriccionesParalelo(individuo, parametros, entrada, patrones,1);
				/*Comprobar restricciones Par (2hilos)*/
				t3 += RestriccionesParalelo.comprobarRestriccionesParalelo(individuo, parametros, entrada, patrones,2);
				/*Comprobar restricciones Par (4hilos)*/
				t4 += RestriccionesParalelo.comprobarRestriccionesParalelo(individuo, parametros, entrada, patrones,4);
				/*Comprobar restricciones Par (8hilos)*/
				t5 += RestriccionesParalelo.comprobarRestriccionesParalelo(individuo, parametros, entrada, patrones,8);
				/*Comprobar restricciones Par (12hilos)*/
				t6 += RestriccionesParalelo.comprobarRestriccionesParalelo(individuo, parametros, entrada, patrones,12);
				/*Modificar solucion*/
				individuo = DeciderMove.switchMoves(individuo,12,3, 0, parametrosAlg, patrones, entrada, parametros, new ArrayList<String>());
			}
			/*Mostrar tiempos*/
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo secuencial ha sido de "+ (t1/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo paralelizacido (1 hilos) ha sido de "+ (t2/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo paralelizacido (2 hilos) ha sido de "+ (t3/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo paralelizacido (4 hilos) ha sido de "+ (t4/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo paralelizacido (8 hilos) ha sido de "+ (t5/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo paralelizacido (12 hilos) ha sido de "+ (t6/1000.0)/60.0 + " minutos.");
		}
	}
	
	
	
	
	public static void mainCODEVSREGEX(String[] args) {
		/*Iniciar patrones*/
		int[] it = {500000,750000,1000000,2000000};long t1=0,t2=0;
		DeciderCase.switchCase("Caso10");//El caso4 no tiene solucion
		Parametros parametros = new Parametros(propFileParameters, propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlg = new ParametrosAlgoritmo_SA(propFileParametersAlgorithm);
		Entrada entrada = Entrada.leerEntrada(parametros, entradaPath,entradaId,entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		/*Iniciar solucion*/
		
		for(int j=0;j<it.length;j++) {
			Solucion individuo = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones).get(0);
			for(int i=0;i<it[j];i++) {
				/*Comprobar restricciones Regex*/
				t1 += RestriccionesRegex.comprobarRestricciones(individuo,patrones, parametros, entrada);
				/*Comprobar restricciones Code*/
				t2 += RestriccionesCode.comprobarRestricciones(individuo, parametros, entrada, parametrosAlg);
				/*Modificar solucion*/
				individuo = DeciderMove.switchMoves(individuo,12,3, 0, parametrosAlg, patrones, entrada, parametros, new ArrayList<String>());
			}
			/*Mostrar tiempos*/
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con expresiones regulares ha sido de "+ (t1/1000.0)/60.0 + " minutos.");
			System.out.println("El tiempo para comprobar las "+it[j]+" soluciones, con codigo ha sido de "+ (t2/1000.0)/60.0 + " minutos.");
		}
		
	}
	
}
