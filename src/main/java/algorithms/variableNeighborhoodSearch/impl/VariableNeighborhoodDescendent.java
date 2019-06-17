package algorithms.variableNeighborhoodSearch.impl;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class VariableNeighborhoodDescendent extends AbstractVariableNeighborhoodSearch {

    /**
     * VNS de tipo determinista
     *
     * @param parametros
     * @param parametrosAlgoritmo
     * @param entrada
     */
    public VariableNeighborhoodDescendent(Parametros parametros, Patrones patrones,
                                          ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        super(parametros, patrones, parametrosAlgoritmo, entrada);
    }

    /**
     * <p><b>Desciende lo maximo posible dentro de la estructura de vecindad actual</b></p>
     * <p>
     * Inherited Java-Doc:{@inheritDoc}
     */
    @Override
    protected Solucion vnsImplemetation(Solucion x) {

//        Solucion x_prime = super.getCurrentNeighborHood().generarSolucionAleatoria(x);
        return super.getCurrentNeighborHood().firstImprovement(x);


//         dada la estructura de vecindad actual, se ejecuta la busqueda según la implementación concreta
//        return super.localSearch(x);
//
//         se decide si seguir en esa estructura de vencindad u otra,
//         y se actualiza la solución actual a aquella con mejor valor objetivo de entre la anterior y la nueva
//
//
//        if (super.fitness(solucion)>)
//
//        return super.getCurrentNeighborHood().bestImprovement(solucion, super.getEntrada(),
//                super.getPatrones(), super.getParametros(), super.getParametrosAlgoritmo());
//
//    return null;
//    }
    }
}