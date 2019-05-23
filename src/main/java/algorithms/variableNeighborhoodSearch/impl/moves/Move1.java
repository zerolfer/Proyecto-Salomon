package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class Move1 extends AbstractNeighborStructure {

//    @Override
    public Solucion bestImprovement(Solucion solActual, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
        Solucion x = solActual.clone(); // partimos de la solucion actual
        do {
            StringBuilder sbA = new StringBuilder(x.getTurnos().get(0));
            StringBuilder sbB = new StringBuilder(x.getTurnos().get(1));

            for (int i = 3; i <= 7; i++) {
                char charA = sbA.charAt(i);
                char charB = sbB.charAt(i);
                sbA.setCharAt(i, charB);
                sbB.setCharAt(i, charA);
            }

        } while (fitness(x,e,pt,p,pa) <= fitness(solActual,e,pt,p,pa)); // hasta que haya mejora en el fitness
        return x;
    }
}
