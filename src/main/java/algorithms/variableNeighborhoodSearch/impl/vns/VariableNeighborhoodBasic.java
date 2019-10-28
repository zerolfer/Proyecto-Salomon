package algorithms.variableNeighborhoodSearch.impl.vns;

import algorithms.variableNeighborhoodSearch.impl.AbstractVariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;

public class VariableNeighborhoodBasic extends AbstractVariableNeighborhoodSearch {

    /**
     * VNS de tipo determinista
     *
     * @param parametros
     * @param parametrosAlgoritmo
     * @param entrada
     */
    VariableNeighborhoodBasic(Parametros parametros, Patrones patrones,
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
        Solucion x_prime_2 = super.getCurrentNeighborhood().bestImprovement(x_prime);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[BVNS] fitness inicial: " + fitness(x)[0] + " | \t" +
                    "fitness sol aleatoria: " + fitness(x_prime)[0] + " | \t" +
                    "fitness sol aleatoria tras BL: " + fitness(x_prime_2)[0]);
        }

        return x_prime_2;

    }

    @Override
    public String toString() {
        return "BVNS";
    }
}