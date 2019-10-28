package algorithms.variableNeighborhoodSearch.impl.vns;

import algorithms.variableNeighborhoodSearch.impl.AbstractVariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;

public class VariableNeighborhoodReduced extends AbstractVariableNeighborhoodSearch {

    /**
     * VNS de tipo determinista
     *
     * @param parametros
     * @param parametrosAlgoritmo
     * @param entrada
     */
    VariableNeighborhoodReduced(Parametros parametros, Patrones patrones,
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

        Solucion x_prime = super.getCurrentNeighborhood().generarSolucionAleatoria(x);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[RVNS] fitness inicial: " + fitness(x)[0] + " | \t" +
                    "fitness sol aleatoria: " + fitness(x_prime)[0]);
        }

        return x_prime;

    }

    @Override
    public String toString() {
        return "RVNS";
    }
}