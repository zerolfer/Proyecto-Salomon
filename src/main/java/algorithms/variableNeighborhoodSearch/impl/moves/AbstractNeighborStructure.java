package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import patrones.Patrones;

abstract class AbstractNeighborStructure implements NeighborStructure {

    double fitness(Solucion solucion, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        return DeciderFitnessFunction.switchFitnessF(solucion, patrones, entrada, parametros, parametrosAlgoritmo)[0];
    }
}
