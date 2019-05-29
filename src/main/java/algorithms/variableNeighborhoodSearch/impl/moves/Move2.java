package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class Move2 extends AbstractNeighborStructure {
    public Move2(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected Solucion buscarSolucion(Solucion x) {
        return null;
    }

    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return null;
    }
}
