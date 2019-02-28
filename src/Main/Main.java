package Main;

import java.util.ArrayList;
import java.util.Date;

import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;
import pruebasCasos.DeciderCase;

public class Main {
	public static String propFileParameters = "recursos/problemParameters.properties";
	public static String propFileOptions= "recursos/options.properties";
	public static String propFileParametersAlgorithm = "recursos/algorithm.properties";
	
	public static String entradaPath = "Caso2";
	public static String entradaId = "Id1n-06-03-2017";
	public static String entorno = "Canarias";
	
	public static String carpetaSoluciones= "";
	public static String carpetaTrazas= "";
	public static Date date = new Date();
	public static void main(String[] args) {
		int nEjecucion = 1;
		int[] casos = {65,69,7,13,22,30};
		
		main1(nEjecucion,"Caso"+casos[2]);
	}
	
	public static void main1 (int ejecucion,String caso) {
		/*INICIALIZACION DE DATOS*/
		DeciderCase.switchCase(caso);//El caso4 no tiene solucion
		
		Parametros parametros = new Parametros(propFileParameters, propFileOptions);
		ParametrosAlgoritmo_SA parametrosAlgoritmo = new ParametrosAlgoritmo_SA(propFileParametersAlgorithm);
		//TODO: MODIFICAR ENTRADA PARA EL NUEVO PROBLEMA
		Entrada entrada = Entrada.leerEntrada(parametros, entradaPath,entradaId,entorno);
		Patrones patrones = new Patrones(entrada, parametros);
		carpetaSoluciones= "resultados/"+entradaPath+entradaId+"/"+parametrosAlgoritmo.getAlgoritmo()+"/Soluciones/";
		carpetaTrazas= "resultados/"+entradaPath+entradaId+"/"+parametrosAlgoritmo.getAlgoritmo()+"/Trazas/";
		
		switch (parametrosAlgoritmo.getAlgoritmo()) {
			case "SA":
				Main_SA.main_sa(parametros, parametrosAlgoritmo, entrada, patrones);
			break;
			case "VNS":
				//Main_VNS.main_vns(parametros, parametrosAlgoritmo, entrada, patrones);
			break;
			default:
				System.out.println("Algoritmo"+parametrosAlgoritmo.getAlgoritmo()+" no encontrado." );
			break;
		}
		
		Patrones.nuc = new ArrayList<String>();
	}
}