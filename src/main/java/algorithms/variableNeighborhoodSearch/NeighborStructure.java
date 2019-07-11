package algorithms.variableNeighborhoodSearch;

import estructurasDatos.Solucion;

public interface NeighborStructure {

    Object[] generarSolucionAleatoria(Solucion x);

    Object[] bestImprovement(Solucion x, int c1, int c2);

    Object[] firstImprovement(Solucion x, int c1, int c2);

    double fitness(Solucion x);

}
