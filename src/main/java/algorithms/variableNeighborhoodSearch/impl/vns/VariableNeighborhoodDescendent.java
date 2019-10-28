package algorithms.variableNeighborhoodSearch.impl.vns;

import algorithms.variableNeighborhoodSearch.NeighborhoodSet;
import algorithms.variableNeighborhoodSearch.impl.AbstractVariableNeighborhoodSearch;
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
    VariableNeighborhoodDescendent(Parametros parametros, Patrones patrones,
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

//        Solucion x_prime = super.getCurrentNeighborhood().generarSolucionAleatoria(x);
        return super.getCurrentNeighborhood().bestImprovement(x);
    }

    @Override
    public String toString() {
        return "VND";
    }

    @Override
    public void setNeighborhoodSet(NeighborhoodSet neighborhoodSet) {
        super.setNeighborhoodSet(neighborhoodSet);
    }
}