package algorithms.variableNeighborhoodSearch.impl;

import algorithms.variableNeighborhoodSearch.AbstractVariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;

public class VariableNeighborhoodDescendent extends AbstractVariableNeighborhoodSearch {

    /**
     * VNS de tipo determinista
     *
     * @param parametros
     * @param parametrosAlgoritmo
     * @param entrada
     */
    public VariableNeighborhoodDescendent(Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        super(parametros, parametrosAlgoritmo, entrada);
    }

    /**
     * <p><b>Desciende lo maximo posible dentro de la estructura de vecindad actual</b></p>
     *
     * Inherited Java-Doc:{@inheritDoc}
     */
    @Override
    protected Solucion vnsImplemetation(Solucion solucion) {
        return super.getCurrentNeighborHood().bestImprovement(solucion);
    }

}
