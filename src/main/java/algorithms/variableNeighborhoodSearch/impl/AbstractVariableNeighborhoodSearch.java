package algorithms.variableNeighborhoodSearch.impl;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.NeighborhoodSet;
import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;
import patrones.Restricciones;

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

    private boolean flagCondicionParadaTiempo;
    private boolean flagCondicionParadaPorcentajeMejora;
    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNeighborSet}
     */
    protected NeighborhoodSet neighborhoodSet;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNumMaxIteracionesSinMejoraBusquedaLocal()}
     */
    private double porcentajeMinimoMejoria;

    protected double porcentajeMejora = 100;
    public static long initTime;

    ///////////////
    ///  DEBUG  ///
    ///////////////

    protected int contadorIteraciones;

    protected AbstractVariableNeighborhoodSearch(Parametros parametros, Patrones patrones,
                                                 ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        this.parametros = parametros;
        this.patrones = patrones;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
        this.entrada = entrada;

        this.maxTimeAllowed = parametrosAlgoritmo.getMaxMilisecondsAllowed();

        this.flagCondicionParadaTiempo = parametrosAlgoritmo.VNS.getFlagCondicionParadaTiempo();
        this.flagCondicionParadaPorcentajeMejora = parametrosAlgoritmo.VNS.getFlagCondicionParadaPorcentajeMejora();

        this.neighborhoodSet = parametrosAlgoritmo.VNS.getNeighborSet();
        this.porcentajeMinimoMejoria = parametrosAlgoritmo.VNS.getPorcentajeMinimoMejoria();
        this.numIteracionesCiclo = parametrosAlgoritmo.VNS.getNumIteracionesParaComprobarCondicionParadaPorcentaje();
    }


    /*
     * TEMPLATE METHOD
     */
    @Override
    public Solucion startExecution(Solucion x) {
        MetaheuristicUtil.reordenarYEliminarTurnos(x);
        initTime = System.currentTimeMillis();
        contadorIteraciones = 0;
        long t = 0;
        int contadorReinicios = 0;
        Solucion x_prime;
        double fitnessAnterior = -1;
        double fitnessMejor = -1;
        do {
            while (checkCondicionParadaTiempo(t) && neighborhoodSet.hayEntornosSinUsar() &&
                    checkCondicionParadaPorcentajeMejora()) {

                if (Log.isOn() && Log.checkIter(contadorIteraciones)) {
                    double[] fit = fitness(x);
                    String s = "[VNS] tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s" +
                            "\t|\t" + "#Iteracion: " + contadorIteraciones +
                            "\t|\t" + String.format("Fitness actual: %.16f (%.4f, %.4f, %.4f, %.4f)", fitness(x)[0], fit[1], fit[2], fit[3], fit[4]) +
                            "\t|\t" + "vecindad actual:" + getCurrentNeighborhood() +
                            "\t|\t" + "porcentaje de mejora: " + porcentajeMejora;
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
                        x.getTurnos().size(), porcentajeMejora, getCurrentNeighborhood(), fit[0], -1);

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
            neighborhoodSet.reset();
            contadorReinicios++;
        } while (checkCondicionParadaTiempo(t) && checkCondicionParadaPorcentajeMejora());

        double[] fit = fitness(x);
        Log.debug("[Fin VNS] Fitness final: " + fit[0] +
                "    |    " + "Fitness desglosado: [" + fit[1] + ", " + fit[2] + ", " + fit[3] + ", " + fit[4] + "]" +
                "    |    " + "iteraciones totales: " + contadorIteraciones +
                "    |    " + "porcentajeMejora: " + porcentajeMejora + " de " + porcentajeMinimoMejoria +
                "    |    " + "tiempo: " + (System.currentTimeMillis() - initTime) / 1000 + "s de " + maxTimeAllowed / 1000 + "s" +
                "    |    " + "tamaño: " + x.getTurnos().size() +
                "    |    " + "Numero de reinicios: " + contadorReinicios +
                "    |    " + "Restricciones: " + Restricciones.penalizacionPorRestricciones(x, getPatrones(), getEntrada(), getParametros()) + "\n");
        Log.csvLog(contadorIteraciones, t,
                fit[0], fit[1], fit[2], fit[3], fit[4],
                x.getTurnos().size(), porcentajeMejora,
                getCurrentNeighborhood(), fit[0], -1);

        return x;
    }

    // neighborhood change
    protected Solucion neighborhoodChange(Solucion x, Solucion x_prime) {

        if (checkCondicionReiniciarVecindad(x, x_prime)) { // si es mejor... NOTE: maximización
            x = x_prime; // Make a move
            neighborhoodSet.reset(); // reset the neighborhood iteration
        } else {
            neighborhoodSet.nextNeighborhood(contadorIteraciones);  // Next neighborhood of the list
        }
        return x;
    }

    protected boolean checkCondicionReiniciarVecindad(Solucion x, Solucion x_prime) {
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
     *          {@link #getNeighborSet().getCurrentNeighborhood()} <br/> ( <code>x_prime "pertenece a" N_k(x) </code>)
     * @return Siguiente solcuíon <code>x_prime</code> a evaluar para continuar el *bucle* VNS
     */
    protected abstract Solucion vnsImplemetation(Solucion x);

    /**
     * Método ADAPTER que retorna el valor de la función objetivo
     *
     * @param x solucion incial
     * @return valor total ponderado de la funcion objetivo para la solucion x
     */
    protected double[] fitness(Solucion x) {
        return getCurrentNeighborhood().fitness(x);
    }

    /*
     * (used by the subclases)
     */
    protected NeighborhoodSet getNeighborSet() {
        return neighborhoodSet;
    }

    /*
     * (used by the subclases)
     */
    protected NeighborhoodStructure getCurrentNeighborhood() {
        return neighborhoodSet.getCurrentNeighborhood();
    }


    protected Entrada getEntrada() {
        return entrada;
    }

    protected Parametros getParametros() {
        return parametros;
    }

    protected ParametrosAlgoritmo getParametrosAlgoritmo() {
        return parametrosAlgoritmo;
    }

    protected Patrones getPatrones() {
        return patrones;
    }

//    int getCurrentNeighborhoodIndex() {
//        return currentNeighborhoodIndex;
//    }

    protected double getPorcentajeMinimoMejoria() {
        return porcentajeMinimoMejoria;
    }

//    public void setCurrentNeighborhoodIndex(int currentNeighborhoodIndex) {
//        neighborStructures.reset();
//    }

    protected long getMaxTimeAllowed() {
        return maxTimeAllowed;
    }

    protected int getNumIteracionesCiclo() {
        return numIteracionesCiclo;
    }

    protected NeighborhoodSet getNeighborhoodSet() {
        return neighborhoodSet;
    }

    protected void setNeighborhoodSet(NeighborhoodSet neighborhoodSet) {
        this.neighborhoodSet = neighborhoodSet;
    }

    /**
     * Solo comprueba la concición si el flag del tiempo
     * (flagCondicionParadaTiempo) está activado
     */
    protected boolean checkCondicionParadaTiempo(long t) {
        if (flagCondicionParadaTiempo)
            return t < getMaxTimeAllowed();
        else return true;
    }

    /**
     * Solo comprueba la concición si el flag del porcentaje
     * de mejora (flagCondicionParadaPorcentajeMejora) está activado
     */
    protected boolean checkCondicionParadaPorcentajeMejora() {
        if (flagCondicionParadaPorcentajeMejora)
            return porcentajeMejora > getPorcentajeMinimoMejoria();
        else return true;
    }
}
