package main;

import InicializarPoblacion.InicializarPoblacion;
import algorithms.simulatedAnnealing.SimulatedAnnealing;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
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


    public static void main_sa(Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada,
                               Patrones patrones, List<Solucion> poblacionInicial) {//Principal

        MainPruebas.problema += "Fase 2:" + "\n";

        /*INICIALIZACION DE SOLUCIONES FACTIBLES*/
//        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);

        //ArrayList<Solucion> poblacionInicial = new ArrayList<Solucion>();

        ArrayList<Solucion> poblacionReducirControladores = new ArrayList<>();
        poblacionReducirControladores = SimulatedAnnealing.bucleSA(poblacionInicial, parametrosAlgoritmo, parametros, patrones, entrada);

        double res = 0;
        MainPruebas.numFactibleAux = 0;
        ArrayList<Solucion> poblacionFactible = new ArrayList<>();
        for (int i = 0; i < poblacionReducirControladores.size(); i++) {
            if ((res = Restricciones.penalizacionPorRestricciones(poblacionReducirControladores.get(i), patrones, entrada, parametros)) == 0) {
                poblacionFactible.add(poblacionReducirControladores.get(i));
                MainPruebas.numFactibleAux++;
            }
            MainPruebas.problema += (i + "-Restricciones incumplidas: " + res + "\n");
            System.out.println(i + "-Restricciones incumplidas: " + res);
        }
        /*PRESENTACION DE RESULTADOS Y TRAZAS*/
        rwFiles.EscrituraExcel.EscrituraSoluciones("PoblacionFactible", Main.carpetaSoluciones,
                poblacionReducirControladores, entrada, patrones, parametros, parametrosAlgoritmo);
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
        MainPruebas.problema += ("Fase 3:" + "\n");

        /*MainPruebas*/
        parametrosAlgoritmo.setFuncionFitnessFase2(parametrosAlgoritmo.getFuncionFitnessFase3());
        //parametrosAlgoritmo.setCondicionParadaNumeroMejoras(0);//Esto elimina esta condicion de parada para la fase 3 ya que no es necesaria.
        parametrosAlgoritmo.SA.setCondicionParadaPorcent(0.1);//Modifica el porcentaje de mejora de la condicion de
        // parada para la fase 3.

        ArrayList<Solucion> poblacionOptimizada = new ArrayList<>();
        poblacionOptimizada = SimulatedAnnealing.bucleSA(poblacionFactible, parametrosAlgoritmo, parametros, patrones, entrada);

        double[] fit;
        for (int i = 0; i < poblacionOptimizada.size(); i++) {
            fit = DeciderFitnessFunction.switchFitnessF(poblacionOptimizada.get(i), patrones, entrada, parametros, parametrosAlgoritmo);
            String cadFitness = "Fitness inicial de " + i + "--> ";
            for (int j = 0; j < fit.length; j++) {
                cadFitness += "fit" + j + " = " + fit[j] + " | ";
            }
            MainPruebas.problema += (cadFitness + "\n");
            System.out.println(cadFitness);
        }

        /*PRESENTACION DE RESULTADOS Y TRAZAS*/
        rwFiles.EscrituraExcel.EscrituraSoluciones("PoblacionOptimizada", Main.carpetaSoluciones, poblacionOptimizada, entrada, patrones, parametros, parametrosAlgoritmo);
        //	trazas.Trazas.archivarYLimpiarTrazas(poblacionOptimizada, Main.propFileOptions, parametrosAlgoritmo);
        /*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/
        System.out.println("Done");
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