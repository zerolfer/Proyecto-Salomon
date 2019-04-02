package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;

public class DeciderGreedyMove {
    public static Solucion switchMoves(Solucion individuo, int gMax, int gMin, double desviacionTipica,
                                       ParametrosAlgoritmo ParametrosAlg, Patrones patrones, Entrada entrada,
                                       Parametros parametros, ArrayList<String> iteracion) {
        String move = ParametrosAlg.SA.getMovimientosEntornoGreedy();
        Solucion mov = null;
        switch (move) {
            case "movimientoGreedy1":
                /**/
                mov = GreedyMove1.movimientoGready(individuo, patrones, entrada, parametros, ParametrosAlg);
                break;
            case "movimientoGreedy2":
                /*Utilizo la misma funcion que al reparar soluciones pero adaptada para que funcione con estos datos*/
                mov = GreedyMove2.movimientoGready(individuo, patrones, entrada, parametros, ParametrosAlg);
                break;
        }

        return mov;
    }
}