package algorithms.variableNeighborhoodSearch;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public interface NeighborStructure {

//    Solucion localSearch(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa);

    Solucion encontrarSolucionEntorno(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa);
}
