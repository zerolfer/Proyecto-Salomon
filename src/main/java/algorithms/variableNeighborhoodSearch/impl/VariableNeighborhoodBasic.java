package algorithms.variableNeighborhoodSearch.impl;

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
    public VariableNeighborhoodBasic(Parametros parametros, Patrones patrones,
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

        Solucion x_prime = super.getCurrentNeighborHood().generarSolucionAleatoria(x);
        Solucion x_prime_2 = super.getCurrentNeighborHood().bestImprovement(x_prime);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[BVNS] fitness inicial: " + fitness(x));
            Log.info("[BVNS] fitness sol aleatoria: " + fitness(x_prime));
            Log.info("[BVNS] fitness sol aleatoria tras BL: " + fitness(x_prime_2));
            Log.info("");
        }

        return x_prime_2;

    }
}