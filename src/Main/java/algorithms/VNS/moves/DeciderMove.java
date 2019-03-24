package algorithms.VNS.moves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import estructurasDatos.DominioDelProblema.Entrada;
import patrones.Patrones;

public class DeciderMove extends Thread {
	
	public static ArrayList<Solucion> switchMoves(String move, Solucion individuo, ParametrosAlgoritmo_SA parametrosAlg, Patrones patrones, Entrada entrada, Parametros parametros, int tmnIntervalo){
		ArrayList<Solucion> soluciones = new ArrayList<Solucion>();
		List<Solucion> listaSoluciones = Collections.synchronizedList(new ArrayList<Solucion>());
		listaSoluciones.clear();
		int tmn = individuo.getTurnos().size();
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		
		switch(move){
			case "entorno1":
				for(int i=0;i<tmn;i++){
					threadPool.submit(new ThreadEntorno1(listaSoluciones, i, (Solucion) individuo.clone(), tmnIntervalo, patrones, entrada, parametros, parametrosAlg));
		        }
			break;
			case "entorno2":
				for(int i=0;i<tmn;i++){
					threadPool.submit(new ThreadEntorno2(listaSoluciones, i, (Solucion) individuo.clone(), tmnIntervalo, patrones, entrada, parametros, parametrosAlg));
		        }
			break;
			case "entorno3":
				for(int i=0;i<tmn;i++){
					threadPool.submit(new ThreadEntorno3(listaSoluciones, i, (Solucion) individuo.clone(), tmnIntervalo, patrones, entrada, parametros, parametrosAlg));
		        }
			break;
			case "entorno4":
				for(int i=0;i<tmn;i++){
					threadPool.submit(new ThreadEntorno4(listaSoluciones, i, (Solucion) individuo.clone(), tmnIntervalo, patrones, entrada, parametros, parametrosAlg));
		        }
			break;
		}
		threadPool.shutdown();
        while(!threadPool.isTerminated()) {}
        listaSoluciones.removeAll(Collections.singleton(null));
			
        for (Solucion solucion : listaSoluciones) {
			soluciones.add(solucion);
		}
		listaSoluciones.clear();
		return soluciones;
	}
}
