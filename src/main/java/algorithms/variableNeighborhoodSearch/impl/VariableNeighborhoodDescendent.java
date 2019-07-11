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

        return (Solucion) super.getCurrentNeighborhood().bestImprovement(x, -1, -1)[0];

    }

    protected Solucion vnsImplemetation(Solucion x, int c1, int c2) {

        return (Solucion) super.getCurrentNeighborhood().bestImprovement(x, c1, c2)[0];

    }


}