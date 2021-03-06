package algorithms.VNS;

import algorithms.MetaheuristicUtil;
import algorithms.VNS.moves.DeciderMove;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import fitnessFunction.Fitness;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

public class VNS {

    public static ArrayList<Solucion> bucleVNS(ArrayList<Solucion> poblacionInicial, ParametrosAlgoritmo parametrosAlg, Parametros parametros, Patrones patrones, Entrada entrada) {
        ArrayList<Solucion> poblacionMejorada = new ArrayList<>();
        Solucion individuo = null;
        for (int i = 0; i < poblacionInicial.size(); i++) {
            individuo = vns(poblacionInicial.get(i), i, parametrosAlg, parametros, patrones, entrada);
            if (individuo != null) {
                poblacionMejorada.add(individuo);
            }
        }

        System.out.println("FIN VNS");
        return poblacionMejorada;
    }

    private static Solucion vns(Solucion individuo, int n, ParametrosAlgoritmo parametrosAlg, Parametros parametros, Patrones patrones, Entrada entrada) {

        // String[] movs = {"entorno2","entorno1","entorno3","entorno4"}; //TIME: 11.995116666666666 | Restricciones: 1.3333333333333333

        String[] movs = {"entorno1", "entorno2", "entorno4", "entorno3"};


        individuo = MetaheuristicUtil.orderByLazyCriteria(individuo);
        double bestFit = DeciderFitnessFunction.switchFitnessF(individuo, patrones, entrada, parametros, parametrosAlg)[0];
        double antFit = bestFit;
        boolean restar = false;
        int numVueltas = 3;
        int[] intervalotmn = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};//,13,14,15,16,17,18};
        System.out.println("Aplicando al individuo " + n + "...");
        long t1 = System.currentTimeMillis();
        Solucion individuo2 = null;
        for (int i = 0; i < numVueltas; i++) {
            long ti1 = System.currentTimeMillis();
            for (int j = 0; j < movs.length; j++) {
                double fit2 = 0;
                for (int numIntervalo = 0; numIntervalo < intervalotmn.length; numIntervalo++) {
                    /*SHAKING*/
                    ArrayList<Solucion> soluciones = DeciderMove.switchMoves(movs[j], individuo, parametrosAlg, patrones, entrada, parametros, intervalotmn[numIntervalo]);
                    Solucion individuo1 = (Solucion) individuo.clone();
                    if (soluciones.size() != 0) {
                        int a = (int) ((int) (Math.random() * soluciones.size()));
                        individuo1 = soluciones.get(a);
                    }
                    /*END SHAKING*/
                    individuo2 = vnsBusquedaLocal.BusquedaLocal(movs[j], individuo1, patrones, entrada, parametros, parametrosAlg, intervalotmn[numIntervalo]);
                    fit2 = DeciderFitnessFunction.switchFitnessF(individuo2, patrones, entrada, parametros, parametrosAlg)[0];

                    if (fit2 > bestFit) {
                        bestFit = fit2;
                        individuo = individuo2;
                        restar = true;
                        System.out.print("BF: " + bestFit + " - ");
                    }
                    if (restar) {
                        j = -1;
                        restar = false;
                        break;
                    }
                    if (bestFit == 1) {
                        i = numVueltas;
                        j = 4;
                    }

                }
            }
            long ti2 = System.currentTimeMillis();
            System.out.println("Iteracion: " + i + " |Tamaño Ind: " + individuo.getTurnos().size() + " |FITAnt .. FITBest: " + antFit + " " + bestFit + " |tiempo: " + (((ti2 - ti1) / 1000.0) / 60.0));
            antFit = bestFit;
        }
        long t2 = System.currentTimeMillis();
        System.out.println("TIME: " + (((t2 - t1) / 1000.0) / 60.0) + " | Restricciones: " + Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros));

        return individuo;
    }

}
