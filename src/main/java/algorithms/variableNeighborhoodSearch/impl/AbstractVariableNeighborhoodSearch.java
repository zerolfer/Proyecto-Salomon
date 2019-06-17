package algorithms.variableNeighborhoodSearch.impl;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.Fitness;
import patrones.Patrones;

import java.util.List;
import java.util.logging.Logger;

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

    private final static Logger LOGGER = Logger.getLogger("ProyectoSalomon");

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
    private List<NeighborStructure> neighborStructures;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNumMaxIteracionesBusquedaLocal()}
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

    private int contadorIteraciones;
    private int trazaCadaTantasIteraciones = 100;

    protected AbstractVariableNeighborhoodSearch(Parametros parametros, Patrones patrones,
                                                 ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        this.parametros = parametros;
        this.patrones = patrones;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
        this.entrada = entrada;

        this.maxTimeAllowed = parametrosAlgoritmo.getMaxMilisecondsAllowed();
        this.neighborStructures = parametrosAlgoritmo.VNS.getNeighborStructures();
        this.numMaxIteracionesSinMejora = parametrosAlgoritmo.VNS.getNumMaxIteracionesSinMejora();

    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) {
        initTime = System.currentTimeMillis();
        long t = 0;
        Solucion x_prime;
        contadorIteraciones = 0;
        do {
            while (getCurrentNeighborhoodIndex() < neighborStructures.size()) {
                // dada la estructura de vecindad actual, se ejecuta la busqueda según la implementación concreta
                x_prime = vnsImplemetation(x);

                if (contadorIteraciones % trazaCadaTantasIteraciones == 0) {
                    LOGGER.info("tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s");
                    LOGGER.info("Fitness actual: " + fitness(x));
                }

                // se decide si seguir en esa estructura de vecindad u otra,
                // y se actualiza la solución actual a aquella con mejor valor objetivo de entre la anterior y la nueva
                x = neighborhoodChange(x, x_prime);

                contadorIteraciones++;
            }
            // se actualiza el tiempo (condición de parada)
            t = System.currentTimeMillis() - initTime;
            currentNeighborhoodIndex = 0;
        } while (t < maxTimeAllowed && numeroIteracionesSinMejora < numMaxIteracionesSinMejora);
        return x;
    }

    // neighborhood change
    Solucion neighborhoodChange(Solucion x, Solucion x_prime) {
        if (fitness(x_prime) > fitness(x)) { // si es mejor... NOTE: maximización
            x = x_prime; // Make a move
            currentNeighborhoodIndex = 1; // reset the neighborhood iteration
            numeroIteracionesSinMejora = 0;
        } else {
            currentNeighborhoodIndex++;   // Next neighborhood of the list
            numeroIteracionesSinMejora++;
        }
        if (contadorIteraciones % trazaCadaTantasIteraciones == 0) {
            LOGGER.info("se produce cambio de vecindad, al indice " + currentNeighborhoodIndex);
            LOGGER.info("numero de iteraciones sin mejora: " + numeroIteracionesSinMejora);
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
//        double f_x = getCurrentNeighborHood().fitness(x);
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
        return getCurrentNeighborHood().fitness(x);
    }

    boolean esMejorQue(Solucion x, Solucion y) {
        return Fitness.esMejorQue(patrones, entrada, parametros, parametrosAlgoritmo, x, y);
    }

    /**
     * @param x Solucion
     * @return Array cuya primera componente es la solucion alcanzada y
     * la segunda componente es un double que representa su valor de fitness
     */
    Solucion encontrarSolucionEntornoActual(Solucion x) {
        return getCurrentNeighborHood().firstImprovement(x);
    }

    /*
     * (used by the subclases)
     */
    public NeighborStructure getCurrentNeighborHood() {
        return neighborStructures.get(this.currentNeighborhoodIndex);
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

    public long getMaxTimeAllowed() {
        return maxTimeAllowed;
    }

    public int getCurrentNeighborhoodIndex() {
        return currentNeighborhoodIndex;
    }

    public Patrones getPatrones() {
        return patrones;
    }

}
