package algorithms.variableNeighborhoodSearch;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public interface NeighborStructure {
    Solucion bestImprovement(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa);

    Solucion FirstImprovement(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa);

    Solucion Shake(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa);
}
