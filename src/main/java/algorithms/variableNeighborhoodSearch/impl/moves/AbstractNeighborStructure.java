package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.AbstractVariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import fitnessFunction.Fitness;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import patrones.Patrones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractNeighborStructure implements NeighborStructure {
    private static final int MAX_ITERACIONES_BUSQUEDA_LOCAL = 20; // TODO: tune parameter
    private Entrada entrada;
    private Patrones patrones;
    private Parametros parametros;
    private ParametrosAlgoritmo parametrosAlgoritmo;

    /* Para mejorar la eficiencia general del metodo*/
    static Map<Solucion, Double> mapaSoluciones;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNumMaxIteracionesBusquedaLocal()}
     */
    private int numMaxIteracionesBusquedaLocal;


    AbstractNeighborStructure(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        this.entrada = entrada;
        this.patrones = patrones;
        this.parametros = parametros;
        this.parametrosAlgoritmo = parametrosAlgoritmo;

        this.mapaSoluciones = new HashMap<>();
        this.numMaxIteracionesBusquedaLocal=parametrosAlgoritmo.VNS.getNumMaxIteracionesBusquedaLocal();

    }

    public double fitness(Solucion x) {
        Double fit = mapaSoluciones.get(x);
        if (fit == null) {
            fit = DeciderFitnessFunction.switchFitnessF(x, null, entrada, parametros, parametrosAlgoritmo)[0];
            mapaSoluciones.put(x, fit);
        }
        return fit;
    }

    @Override
    public Solucion bestImprovement(Solucion solucionInicial) {
        Solucion x = solucionInicial.clone();
        Solucion x_prime = x;
        int numIter = 0;
        // En la busqueda local, iteramos repetidas veces hasta que no haya mejora
        while (numIter <= MAX_ITERACIONES_BUSQUEDA_LOCAL &&
                AbstractVariableNeighborhoodSearch.initTime - System.currentTimeMillis() < parametrosAlgoritmo.getMaxMilisecondsAllowed()) {
            System.out.println("ejecutando BL");
            x_prime = buscarSolucion(x);


            // OUTPUT /////////////////////////////////////////////////////////////////////////////////
//
//            List<Solucion> solArray = new ArrayList<>();
//            solArray.add(x);
//            rwFiles.EscrituraExcel.EscrituraSoluciones("Fase2-it" + numIter, Main.carpetaSoluciones, solArray,
//                    entrada, patrones, parametros, parametrosAlgoritmo);
//
            //////////////////////////////////////////////////////////////////////////////////////////


            double f_x = fitness(x);
            double f_x_prime = fitness(x_prime);
            if (f_x_prime > f_x) { // si es mejor...paramos NOTE: maximización
//                return x;
                x = x_prime;
            }

            numIter++;
            System.out.println("[BL] tiempo: " + (AbstractVariableNeighborhoodSearch.initTime - System.currentTimeMillis()) / 1000);
        }
        return x;
    }

    @Override
    public Solucion firstImprovement(Solucion solucionInicial) {
        Solucion x = solucionInicial.clone();
        Solucion x_prime;

        double f_x = fitness(x);
        double f_x_prime;

        int numIt = 1;
        // Iteramos repetidas veces hasta que haya mejora
        do {
            x_prime = buscarSolucion(x);
            f_x_prime = fitness(x_prime);
            numIt++;
        } while (f_x_prime <= f_x && numIt <= MAX_ITERACIONES_BUSQUEDA_LOCAL); // si es mejor...paramos NOTE: maximización
        return x_prime;
    }

    /**
     * {ABSTRACT METHOD} utilizado para obtener una solución (aleatoria)
     * dentro del entorno a partir de la solucion inicial x
     *
     * @param x solucion inicial
     * @return solucion dentro del entrono actual N_k(x)
     */
    protected abstract Solucion buscarSolucion(Solucion x);

    //    @Override
//    public Solucion buscarSolucion(Solucion solActual, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        Solucion x = solActual.clone();
//
//        XoRoShiRo128PlusRandom r = new XoRoShiRo128PlusRandom();
//
//        int idx1, idx2, num1 = 0, num2 = 0;
//
//        int[] numSlotsNecesarios;
//        int numMax = x.getTurnos().size();
//        String turnoA, turnoB;
//        do {
//            idx1 = r.nextInt(numMax);
//            turnoA = x.getTurnos().get(idx1);
//            numSlotsNecesarios = MetaheuristicUtil.comprobarTrabajoMinimo(turnoA);
//            num1++;
//        } while (numSlotsNecesarios[0] <= 0 || num1 < numMax);
//
//        int idx;
//        String previoB, posteriorB;
//        if (num1 >= numMax) return x;
//        do {
//            idx2 = r.nextInt(numMax);
//            turnoB = x.getTurnos().get(idx2);
//            num2++;
//            idx = numSlotsNecesarios[1];
//            previoB = turnoB.substring(0, idx);
//            posteriorB = turnoB.substring(idx + numSlotsNecesarios[0] * LONGITUD_CADENAS);
//        } while (idx1 == idx2 || !esPosibleAsignacion(previoB, posteriorB) || num2 < numMax);
//
//        if (num2 >= numMax) return x;
//
//        doChange(x, turnoA, turnoB, previoB, posteriorB, idx, numSlotsNecesarios, idx1, idx2);
//
//        return x;
//
//    }

//    protected abstract void doChange(int[] idxs);
//
//    @Override
//    public Solucion busquedaLocal(Solucion x, Entrada e, Patrones pt, Parametros p, ParametrosAlgoritmo pa) {
//        return null;
//    }

    void doChange(Solucion x, String turnoA, String turnoB,
                  int desde, int hasta, int idx1, int idx2) {

        // obtenemos el resto de trozos
        String previoB = turnoB.substring(0, desde);
        String medioB = turnoB.substring(desde, hasta);
        String posteriorB = turnoB.substring(hasta);

        String previoA = turnoA.substring(0, desde);
        String medioA = turnoA.substring(desde, hasta);
        String posteriorA = turnoA.substring(hasta);

        // reconstruimos
        String nuevoTurnoA = previoA + medioB + posteriorA;
        String nuevoTurnoB = previoB + medioA + posteriorB;

        // modificamos la matriz de turnos real
        x.getTurnos().set(idx1, nuevoTurnoA);
        x.getTurnos().set(idx2, nuevoTurnoB);
    }

    protected boolean esMejorQue(Solucion x, Solucion solucionInicial) {
        return Fitness.esMejorQue(patrones, entrada, parametros, parametrosAlgoritmo, x, solucionInicial);
    }

    XoRoShiRo128PlusRandom random = new XoRoShiRo128PlusRandom();

    List<Nucleo> getNucleos() {
        return this.entrada.getNucleos();
    }

    int getSlotMomentoActual() {
        return entrada.getSlotMomentoActual();
    }
}
