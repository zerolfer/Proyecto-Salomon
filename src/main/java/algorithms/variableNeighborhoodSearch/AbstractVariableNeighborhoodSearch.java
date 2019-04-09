package algorithms.variableNeighborhoodSearch;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
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
     * Véase {@link ParametrosAlgoritmo.VNS#getMaxMilisecondsAllowed}
     */
    private long maxTimeAllowed;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNeighborStructures}
     */
    private List<NeighborStructure> neighborStructures;

    /**
     * <p>The index of the current neighborhood of the
     * {@link #neighborStructures} list.</p>
     * <p>Aka " k "</p>
     */
    private int currentNeighborhoodIndex = 0;


    protected AbstractVariableNeighborhoodSearch(Parametros parametros, Patrones patrones,
                                                 ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        this.parametros = parametros;
        this.patrones = patrones;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
        this.entrada = entrada;

        this.maxTimeAllowed = parametrosAlgoritmo.VNS.getMaxMilisecondsAllowed();
        this.neighborStructures = parametrosAlgoritmo.VNS.getNeighborStructures();


    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) {
        long initTime = System.currentTimeMillis();
        long t = 0;
        do {
            Solucion x_prime = vnsImplemetation(x);
            x = neighborhoodChange(x, x_prime);

            t += System.currentTimeMillis() - initTime;
        } while (t < maxTimeAllowed);
        return x;
    }

    // neighborhood change
    protected Solucion neighborhoodChange(Solucion x, Solucion x_prime) {
        if (fitness(x_prime) < fitness(x)) {
            x = x_prime; // Make a move
            currentNeighborhoodIndex = 1; // reset the neighborhood iteration
        } else
            currentNeighborhoodIndex++;   // Next neighborhood of the list
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
     * @param solucion Solución inicial de la cual se ha de partir, para
     *                 lograr una solución incluida dentro del conjunto de soluciones
     *                 factibles pertenecientes a la vecindad (neighborhood) actual
     *                 {@link #currentNeighborhoodIndex} <br/> ( <code>x_prime "pertenece a" N_k(x) </code>)
     * @return Siguiente solcuíon <code>x_prime</code> a evaluar para continuar el *bucle* VNS
     */
    protected abstract Solucion vnsImplemetation(Solucion solucion);

    /**
     * Método ADAPTER que retorna el valor de la función objetivo
     *
     * @param x
     * @return
     */
    private double fitness(Solucion x) {
        return DeciderFitnessFunction.switchFitnessF(x, null, entrada, parametros, parametrosAlgoritmo)[0];
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
