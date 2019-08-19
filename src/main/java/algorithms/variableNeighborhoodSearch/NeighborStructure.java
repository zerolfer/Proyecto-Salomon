package algorithms.variableNeighborhoodSearch;

import estructurasDatos.Solucion;

public interface NeighborStructure {

    Solucion generarSolucionAleatoria(Solucion x);

    Solucion bestImprovement(Solucion x);

    Solucion firstImprovement(Solucion x);

    double[] fitness(Solucion x);
}
