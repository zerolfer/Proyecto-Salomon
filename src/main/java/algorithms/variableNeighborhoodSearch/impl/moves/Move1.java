package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class Move1 extends AbstractNeighborStructure {

    @Override
    public Solucion bestImprovement(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        do {
//
//        } while (fitness(x,e,pt,p,pa) < fitness(x,e,pt,p,pa));
        return null;
    }

    @Override
    public Solucion FirstImprovement(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
        return null;
    }

    @Override
    public Solucion Shake(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
        return null;
    }
}
