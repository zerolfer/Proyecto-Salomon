package algorithms.variableNeighborhoodSearch;

import estructurasDatos.Solucion;

public interface NeighborStructure {
    Solucion bestImprovement(Solucion solucion);
    Solucion FirstImprovement(Solucion solucion);
    Solucion Shake(Solucion solucion);
}
