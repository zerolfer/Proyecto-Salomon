package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GreedyMove1 {
    public final static int NUM_HILOS = 4;

    public static Solucion movimientoGready(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg) {
        ArrayList<ParalelMove1> array = new ArrayList<>();
        ArrayList<Double> fitness = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_HILOS);  //El parñmetro es en numero de threads que quieres lanzar
        ParalelMove1 mov = null;
        for (int i = 0; i < individuo.getTurnos().size(); i++) {
            String t = individuo.getTurnos().get(i);
            for (int j = 0; j < t.length() - 3; j += 3) {
                if (!t.substring(j, j + 3).equalsIgnoreCase(t.substring(j + 3, j + 6)) && !t.substring(j, j + 3).equals("111")) {
                    mov = new ParalelMove1(i, j, individuo, patrones, entrada, parametros, parametrosAlg);
                    array.add(mov);
                    threadPool.submit(mov);
                }
            }
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }

        for (int i = 0; i < array.size(); i++) {
            fitness.add(DeciderFitnessFunction.switchFitnessF(array.get(i).individuo, patrones, entrada, parametros, parametrosAlg)[0]);
        }
        double bestFit = DeciderFitnessFunction.switchFitnessF(individuo, patrones, entrada, parametros, parametrosAlg)[0];
        Solucion bestSol = null;
        for (int i = 0; i < fitness.size(); i++) {
            if (bestFit < fitness.get(i)) {
                bestFit = fitness.get(i);
                bestSol = array.get(i).individuo;
            }
        }
        if (bestSol == null) {
            return individuo;
        } else {
            return bestSol;
        }
    }
}
