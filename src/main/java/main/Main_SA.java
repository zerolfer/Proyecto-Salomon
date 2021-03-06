package main;

import algorithms.simulatedAnnealing.SimulatedAnnealing;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import herramientas.random.SplitRnd;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;
import java.util.List;

/**
 * Main
 *
 * @author Tino
 */
public class Main_SA {


    public static ArrayList<Solucion> main_sa(Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada,
                                              Patrones patrones, List<Solucion> poblacionInicial) {//Principal

        MainPruebas.problema += "Fase 2:" + "\n";
        long seed = System.currentTimeMillis();
		SplitRnd.init(seed);
        ArrayList<Solucion> poblacionSoluciones = new ArrayList<>();
        poblacionSoluciones = SimulatedAnnealing.bucleSA(poblacionInicial, parametrosAlgoritmo, parametros, patrones, entrada);

        double res = 0;
        double[] fit;
        MainPruebas.numFactibleAux = 0;
        for (int i = 0; i < poblacionSoluciones.size(); i++) {
            if ((res = Restricciones.penalizacionPorRestricciones(poblacionSoluciones.get(i), patrones, entrada, parametros)) == 0) {
                //poblacionFactible.add(poblacionSoluciones.get(i));
                MainPruebas.numFactibleAux++;
            }
            MainPruebas.problema += (i + "-Restricciones incumplidas: " + res + "\n");
            System.out.println(i + "-Restricciones incumplidas: " + res);
            
            fit = DeciderFitnessFunction.switchFitnessF(poblacionSoluciones.get(i), patrones, entrada, parametros, parametrosAlgoritmo);
            String cadFitness = "Fitness inicial de " + i + "--> ";
            for (int j = 0; j < fit.length; j++) {
                cadFitness += "fit" + j + " = " + fit[j] + " | ";
            }
            MainPruebas.problema += (cadFitness + "\n");
            System.out.println(cadFitness);
        }
        
        
        /*PRESENTACION DE RESULTADOS Y TRAZAS*/
        //	trazas.Trazas.archivarYLimpiarTrazas(poblacionReducirControladores, Main.propFileOptions,
        //	parametrosAlgoritmo);
        //	trazas.Trazas.limpiarTrazas();
        /*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/

        /*OPTIMIZACION DE SOLUCIONES*/
        /*MainPruebas*/

        MainPruebas.TiemposResultadosproblema[6] = MainPruebas.tiempoFactibleAux / MainPruebas.numFactibleAux;
        MainPruebas.tiempoFactibleAux = 0;
        for (int i = 0; i < MainPruebas.LeyendaTRP.length; i++) {
            MainPruebas.problema += MainPruebas.LeyendaTRP[i] + ": ";
            MainPruebas.problema += MainPruebas.TiemposResultadosproblema[i] + "; ";
        }
        MainPruebas.TiemposResultadosproblema[1] = 0;
        MainPruebas.problema += "NumeroSolFactibles: " + MainPruebas.numFactibleAux + "; ";

        MainPruebas.problema += "\n";
        
       
        System.out.println("Done");
        return poblacionSoluciones;//reordenarSoluciones(poblacionSoluciones);
        }
        
        public static ArrayList<Solucion> reordenarSoluciones(ArrayList<Solucion> poblacionSoluciones){
        	
        	for (int i = 0; i < poblacionSoluciones.size(); i++) {
    			Solucion s = poblacionSoluciones.get(i);
    			ArrayList<String> turnos = s.getTurnos();
    			ArrayList<Controlador> cnt = s.getControladores();
    			for (int j = 0; j < cnt.size(); j++) {
    				Controlador cnt1 = cnt.get(j);
    				int id =cnt1.getId();
    				int pos = cnt1.getTurnoAsignado();
    				String t1  =turnos.get(pos);
    				String t2 = turnos.get(id-1);
    				if(id-1 != pos) {
    	 				for (int k = 0; k < cnt.size(); k++) {
    						if(k!=j) {
    							Controlador cnt2 = cnt.get(k);
    							if(cnt2.getTurnoAsignado()==id-1) {
    								turnos.set(pos, t2);
    								turnos.set(id-1, t1);
    								cnt2.setTurnoAsignado(pos);
    								cnt.set(k, cnt2);
    								Controlador cnt1clone = cnt1.clone();
    								cnt2.setTurnoAsignado(id-1);
    								cnt.set(j, cnt2);
    								
    							}
    						}
    					}
     				}
    				
    			}
        	}
    		return poblacionSoluciones;
        }

    /**
     * Main secundario para la realización de pruebas
     *
     * @param args - Null
     */
    public static void mainPruebas(String[] args) {//Pruebas: Se usa para pruebas
        //MainPruebas.mainInicializacion(null);
        MainPruebas.mainSoluciones(null);
    }
}