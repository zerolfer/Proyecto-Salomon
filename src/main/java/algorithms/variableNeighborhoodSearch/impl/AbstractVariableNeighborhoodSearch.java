package algorithms.variableNeighborhoodSearch.impl;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;

import java.util.List;

public abstract class AbstractVariableNeighborhoodSearch implements VariableNeighborhoodSearch {

    /**
     * Parametros del dominio del problema
     */
    private final Parametros parametros;

    private final Patrones patrones;

    /**
     * Información del dominio del problema
     */
    private final Entrada entrada;

    /*
     * este campo solamente es necesario por la
     * implementación actual de la función fitness
     */
    private final ParametrosAlgoritmo parametrosAlgoritmo;

    ////////////////////////////
    ///  PARÁMETROS DEL VNS  ///
    ////////////////////////////

    /**
     * Véase {@link ParametrosAlgoritmo#getMaxMilisecondsAllowed}
     */
    private long maxTimeAllowed;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNeighborStructures}
     */
    List<NeighborStructure> neighborStructures;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNumMaxIteracionesSinMejoraBusquedaLocal()}
     */
    private int numMaxIteracionesSinMejora;

    /**
     * <p>The index of the current neighborhood of the
     * {@link #neighborStructures} list.</p>
     * <p>Aka " k "</p>
     */
    private int currentNeighborhoodIndex = 0;
    private int numeroIteracionesSinMejora = 0;
    public static long initTime;

    ///////////////
    ///  DEBUG  ///
    ///////////////

    int contadorIteraciones;

    AbstractVariableNeighborhoodSearch(Parametros parametros, Patrones patrones,
                                       ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        this.parametros = parametros;
        this.patrones = patrones;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
        this.entrada = entrada;

        this.maxTimeAllowed = parametrosAlgoritmo.getMaxMilisecondsAllowed();
        this.neighborStructures = parametrosAlgoritmo.VNS.getNeighborStructures();
        this.numMaxIteracionesSinMejora = parametrosAlgoritmo.VNS.getNumMaxIteracionesSinMejoraVNS();

    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) {
        MetaheuristicUtil.orderByLazyCriteria(x);
        initTime = System.currentTimeMillis();
        contadorIteraciones = 1;
        long t = 0;
        Solucion x_prime;
        do {
            while (currentNeighborhoodIndex < neighborStructures.size()) {

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    String s = "[VNS] tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s" +
                            "    |    " + "#Iteracion: " + contadorIteraciones +
                            "    |    " + "Fitness actual: " + fitness(x) +
                            "    |    vecindad actual: " + getCurrentNeighborhood() +
                            "    |    " + "numero de iteraciones sin mejora: " + numeroIteracionesSinMejora +
                            "\n";
                    Log.info(s);
                }

                Log.csvLog(contadorIteraciones++, t, fitness((x)), x.getTurnos().size(), numeroIteracionesSinMejora,
                        currentNeighborhoodIndex);

                // dada la estructura de vecindad actual, se ejecuta la busqueda según la implementación concreta
                x_prime = vnsImplemetation(x);

                // se decide si seguir en esa estructura de vecindad u otra,
                // y se actualiza la solución actual a aquella con mejor valor objetivo de entre la anterior y la nueva
                x = neighborhoodChange(x, x_prime);


            }
            // se actualiza el tiempo (condición de parada)
            t = System.currentTimeMillis() - initTime;
            currentNeighborhoodIndex = 0;
        } while (t < maxTimeAllowed && numeroIteracionesSinMejora < numMaxIteracionesSinMejora);

        Log.debug("[Fin VNS] Fitness final: " + fitness(x) +
                "    |    " + "numeroIteracionesSinMejora: " + numeroIteracionesSinMejora + " de " + numMaxIteracionesSinMejora +
                "    |    " + "tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s de " + maxTimeAllowed / 1000 + "s" +
                "    |    " + "tamaño: " + x.getTurnos().size());
        Log.csvLog(contadorIteraciones, t, fitness((x)), x.getTurnos().size(), numeroIteracionesSinMejora,
                currentNeighborhoodIndex);

        return x;
    }

    // neighborhood change
    private Solucion neighborhoodChange(Solucion x, Solucion x_prime) {

        if (fitness(x_prime) > fitness(x)) { // si es mejor... NOTE: maximización
            x = x_prime; // Make a move
            currentNeighborhoodIndex = 0; // reset the neighborhood iteration
            numeroIteracionesSinMejora = 0;
        } else {
            currentNeighborhoodIndex++;   // Next neighborhood of the list
            numeroIteracionesSinMejora++;

        }
        return x;
    }


    /**
     * <p> Implementación de la búsqueda propiamente dicha del VNS concreto.
     * Ésta puede realizarse --según el tipo de VNS instanciado-- de
     * manera determinista, estocástica o una una combinación de ambos. </p>
     *
     * <p> Debe realizar los cambios oportunos para a partir de una solución dada,
     * obtenga la siguente solución a evaluar. </p>
     *
     * @param x Solución inicial de la cual se ha de partir, para
     *          lograr una solución incluida dentro del conjunto de soluciones
     *          factibles pertenecientes a la vecindad (neighborhood) actual
     *          {@link #currentNeighborhoodIndex} <br/> ( <code>x_prime "pertenece a" N_k(x) </code>)
     * @return Siguiente solcuíon <code>x_prime</code> a evaluar para continuar el *bucle* VNS
     */
    protected abstract Solucion vnsImplemetation(Solucion x);
//    protected Solucion vnsImplemetation(Solucion x) {
//
    ///////////////////
    //  localSearch  //
    ///////////////////

//        Solucion x_prime;
//        int numIteracionesSinMejora = 0;
//        double f_x = getCurrentNeighborhood().fitness(x);
//        double f_x_prime;
//
//        do {
//            Object[] raw = encontrarSolucionEntornoActual(x);
//            x_prime = (Solucion) raw[0];
//            f_x_prime = (double) raw[1];
//
    // si la solucion encontrada es mejor, se actualiza
//            if (f_x < f_x_prime) {
//                x = x_prime;
//                f_x = f_x_prime;
//                numIteracionesSinMejora = 0;
//
//            } else {
    // ... pero si no era mejor, se incrementa el numero de iteraciones sin mejoría
//                numIteracionesSinMejora++;
//            }
//        } while (numIteracionesSinMejora < NUM_MAX_ITER_SIN_MEJORA);
    // y así hasta que se haya que la busqueda local deje de ser fructífera

//    }

    /**
     * Método ADAPTER que retorna el valor de la función objetivo
     *
     * @param x solucion incial
     * @return valor total ponderado de la funcion objetivo para la solucion x
     */
    double fitness(Solucion x) {
        return getCurrentNeighborhood().fitness(x);
    }

    /*
     * (used by the subclases)
     */
    NeighborStructure getCurrentNeighborhood() {
        if (currentNeighborhoodIndex < neighborStructures.size())
            return neighborStructures.get(this.currentNeighborhoodIndex);
        return neighborStructures.get(0);
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public ParametrosAlgoritmo getParametrosAlgoritmo() {
        return parametrosAlgoritmo;
    }

    public Patrones getPatrones() {
        return patrones;
    }

}
