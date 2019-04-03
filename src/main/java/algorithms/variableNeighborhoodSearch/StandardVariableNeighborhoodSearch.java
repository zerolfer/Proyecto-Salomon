package algorithms.variableNeighborhoodSearch;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;

import java.util.List;

public abstract class StandardVariableNeighborhoodSearch implements VariableNeighborhoodSearch {

    /**
     * Parametros del dominio del problema
     */
    private final Parametros parametros;

    /**
     * Parámetros del VNS
     */
    private final ParametrosAlgoritmo parametrosAlgoritmo;

    /**
     * Información del dominio del problema
     */
    private final Entrada entrada;

    private

    List<NeighborStructure> neighborStructures;

    /**
     * <p>
     * Tiempo máximo total que le permitimos al VNS ejecutarse,
     * puesto que en nuestro sistema es crucial el tiempo,
     * no debemos superar el umbral marcado.
     * </p>
     */
    private long maxTimeAllowed;

    protected StandardVariableNeighborhoodSearch(Parametros parametros,
                                                 ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        this.parametros = parametros;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
        this.entrada = entrada;

        this.maxTimeAllowed = parametrosAlgoritmo.VNS.getMaxMilisecondsAllowed();

    }

    boolean addNeighborhood(NeighborStructure neighborStructure) {
        return this.neighborStructures.add(neighborStructure);
    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) { // TODO: parametros del método como parámetros de la clase comunes
        long time = System.currentTimeMillis();
        long t = 0;
        Integer currentNeighborhoodIndex = 0; // k
        do {
            Solucion x_prime = vnsImplemetation();

            // neighborhood change
            if (f(x_prime) < f(x)) {
                x = x_prime; // Make a move
                currentNeighborhoodIndex = 1;// reset the neighborhood iteration
            } else
                currentNeighborhoodIndex++; // Next neighborhood of the list


            t = System.currentTimeMillis() - time;
        } while (t < parametrosAlgoritmo.VNS.getMaxMilisecondsAllowed());
        return x;
    }

    protected abstract Solucion vnsImplemetation();

    private Solucion neighborhoodChange(Solucion x, Solucion x_prime, int k) {
        if (f(x_prime) < f(x)) {
            x = x_prime; // Make a move
            k = 1;// reset the neighborhood iteration
        } else
            k++; // Next neighborhood of the list
        return x;
    }

    private double f(Solucion x) {
        return DeciderFitnessFunction.switchFitnessF(x, null, entrada, parametros, parametrosAlgoritmo)[0]; //TODO
    }
}
