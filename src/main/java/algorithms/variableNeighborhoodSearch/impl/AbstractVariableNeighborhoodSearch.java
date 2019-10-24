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
import patrones.Restricciones;

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
    private final int numIteracionesCiclo;

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
    private double porcentajeMinimoMejoria;

    /**
     * <p>The index of the current neighborhood of the
     * {@link #neighborStructures} list.</p>
     * <p>Aka " k "</p>
     */
    private int currentNeighborhoodIndex = 0;
    double porcentajeMejora = 100;
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
        this.porcentajeMinimoMejoria = parametrosAlgoritmo.VNS.getPorcentajeMinimoMejoria();
        this.numIteracionesCiclo = parametrosAlgoritmo.VNS.getNumIteracionesParaComprobarCondicionParadaPorcentaje();
    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) {
        MetaheuristicUtil.orderByLazyCriteria(x);
        initTime = System.currentTimeMillis();
        contadorIteraciones = 0;
        long t = 0;
        int contadorReinicios = 0;
        Solucion x_prime;
        double fitnessAnterior = -1;
        double fitnessMejor = -1;
        do {
            while (t < maxTimeAllowed && /*FIXME CONDICION PARADA SESGADA*/currentNeighborhoodIndex < neighborStructures.size()/* &&
                    porcentajeMejora > porcentajeMinimoMejoria*/) {

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    String s = "[VNS] tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s" +
                            "    |    " + "#Iteracion: " + contadorIteraciones +
                            "    |    " + "Fitness actual: " + fitness(x)[0] +
                            "    |    vecindad actual: " + getCurrentNeighborhood() +
                            "    |    " + "porcentaje de mejora: " + porcentajeMejora;
                    Log.info(s);
                }


                // dada la estructura de vecindad actual, se ejecuta la busqueda según la implementación concreta
                x_prime = vnsImplemetation(x);

                // se decide si seguir en esa estructura de vecindad u otra,
                // y se actualiza la solución actual a aquella con mejor valor objetivo de entre la anterior y la nueva
                x = neighborhoodChange(x, x_prime);

                // se actualiza el tiempo (condición de parada)
                t = System.currentTimeMillis() - initTime;

                double[] fit = fitness(x);
                Log.csvLog(contadorIteraciones, t, fit[0],
                        fit[1], fit[2], fit[3], fit[4],
                        x.getTurnos().size(), porcentajeMejora,
                        currentNeighborhoodIndex);

                if (contadorIteraciones % numIteracionesCiclo == 0) {
                    // calcular porcentaje mejora
                    if (fitnessAnterior == -1)
                        porcentajeMejora = 100;
                    else if (fitnessMejor == -1)
                        porcentajeMejora = 0;
                    else
                        porcentajeMejora = Math.abs(fitnessMejor - fitnessAnterior) * 100; //(fitness(x)[0] * 100 / fitnessAnterior) - 100;

                    // actualizar fitness anterior por el actual
                    fitnessAnterior = fit[0];
                }
                // actualizar fitness mejor
                if (fit[0] > fitnessMejor) // NOTE: Maximizacion
                    fitnessMejor = fit[0];

                contadorIteraciones++;

                Log.info("", contadorIteraciones);
            }
            currentNeighborhoodIndex = 0;
            contadorReinicios++;
        } while (t < maxTimeAllowed /*FIXME CONDICION PARADA SESGADA && *//*porcentajeMejora > porcentajeMinimoMejoria*/);

        double[] fit = fitness(x);
        Log.debug("[Fin VNS] Fitness final: " + fit[0] +
                "    |    " + "Fitness desglosado: [" + fit[1] + ", " + fit[2] + ", " + fit[3] + ", " + fit[4] + "]" +
                "    |    " + "iteraciones totales: " + contadorIteraciones +
                "    |    " + "porcentajeMejora: " + porcentajeMejora + " de " + porcentajeMinimoMejoria +
                "    |    " + "tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s de " + maxTimeAllowed / 1000 + "s" +
                "    |    " + "tamaño: " + x.getTurnos().size() +
                "    |    " + "Numero de reinicios: " + contadorReinicios +
                "    |    " + "Restricciones: " + Restricciones.penalizacionPorRestricciones(x, getPatrones(), getEntrada(), getParametros()) + "\n");
        Log.csvLog(contadorIteraciones, t, fit[0], fit[1], fit[2], fit[3], fit[4], x.getTurnos().size(), porcentajeMejora,
                currentNeighborhoodIndex);

        return x;
    }

    // neighborhood change
    Solucion neighborhoodChange(Solucion x, Solucion x_prime) {

        if (checkCondicionReiniciarVecindad(x, x_prime)) { // si es mejor... NOTE: maximización
            x = x_prime; // Make a move
            currentNeighborhoodIndex = 0; // reset the neighborhood iteration
        } else {
            currentNeighborhoodIndex++;   // Next neighborhood of the list
        }
        return x;
    }

    boolean checkCondicionReiniciarVecindad(Solucion x, Solucion x_prime) {
        return fitness(x_prime)[0] > fitness(x)[0];
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

    /**
     * Método ADAPTER que retorna el valor de la función objetivo
     *
     * @param x solucion incial
     * @return valor total ponderado de la funcion objetivo para la solucion x
     */
    double[] fitness(Solucion x) {
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

    int getCurrentNeighborhoodIndex() {
        return currentNeighborhoodIndex;
    }

    public double getPorcentajeMinimoMejoria() {
        return porcentajeMinimoMejoria;
    }

    public void setCurrentNeighborhoodIndex(int currentNeighborhoodIndex) {
        this.currentNeighborhoodIndex = currentNeighborhoodIndex;
    }

    public long getMaxTimeAllowed() {
        return maxTimeAllowed;
    }

    public int getNumIteracionesCiclo() {
        return numIteracionesCiclo;
    }
}
