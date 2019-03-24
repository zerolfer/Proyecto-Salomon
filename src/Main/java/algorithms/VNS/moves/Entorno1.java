package algorithms.VNS.moves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.vnsPablo.FillNeigbourhoodListTask;
import estructurasDatos.Movimiento;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;
import patrones.Restricciones;

public class Entorno1 extends Thread {
	
	public static ArrayList<Solucion> movimiento(Solucion individuo1, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg, int tmnIntervalo) {
		ArrayList<Solucion> soluciones = new ArrayList<Solucion>();
		List<Solucion> listaSoluciones = Collections.synchronizedList(new ArrayList<Solucion>());
		listaSoluciones.clear();
		int tmn = individuo1.getTurnos().size();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(tmn);
		for(int i=0;i<tmn;i++){
			threadPool.submit(new ThreadEntorno1(listaSoluciones, i, individuo1, tmnIntervalo, patrones, entrada, parametros, parametrosAlg));
        }
		
        threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        listaSoluciones.removeAll(Collections.singleton(null));
			
			

		return soluciones;
	}
	
	
	
	
}
