package algorithms.variableNeighborhoodSearch;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public interface NeighborStructure {

    Solucion generarSolucionAleatoria(Solucion x);

    Object[] busquedaLocal(Solucion x);
}
