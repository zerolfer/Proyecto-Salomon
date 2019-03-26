package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;

public class DeciderGreedyMove {
    public static Solucion switchMoves(Solucion individuo, int gMax, int gMin, double desviacionTipica,
                                       ParametrosAlgoritmo_SA parametrosAlg, Patrones patrones, Entrada entrada,
                                       Parametros parametros, ArrayList<String> iteracion) {
        String move = parametrosAlg.getMovimientosEntornoGreedy();
        Solucion mov = null;
        switch (move) {
            case "movimientoGreedy1":
                /**/
                mov = GreedyMove1.movimientoGready(individuo, patrones, entrada, parametros, parametrosAlg);
                break;
            case "movimientoGreedy2":
                /*Utilizo la misma funcion que al reparar soluciones pero adaptada para que funcione con estos datos*/
                mov = GreedyMove2.movimientoGready(individuo, patrones, entrada, parametros, parametrosAlg);
                break;
        }

        return mov;
    }
}