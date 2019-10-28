package algorithms.variableNeighborhoodSearch.impl;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import fitnessFunction.Fitness;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import patrones.Patrones;

import java.util.*;

import static algorithms.MetaheuristicUtil.esTrabajo;
import static herramientas.CridaUtils.*;

public abstract class AbstractNeighborhoodStructure implements NeighborhoodStructure {

    protected Entrada entrada;
    protected Patrones patrones;
    protected Parametros parametros;
    protected ParametrosAlgoritmo parametrosAlgoritmo;

    public static List<int[]> rejilla;

    /* Para mejorar la eficiencia general del metodo*/
    static Map<Solucion, double[]> mapaSoluciones;

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNumMaxIteracionesSinMejoraBusquedaLocal()}
     */
    private int numMaxIteracionesSinMejoraBusquedaLocal;


    protected AbstractNeighborhoodStructure(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        this.entrada = entrada;
        this.patrones = patrones;
        this.parametros = parametros;
        this.parametrosAlgoritmo = parametrosAlgoritmo;

        mapaSoluciones = new HashMap<>();
        this.numMaxIteracionesSinMejoraBusquedaLocal = parametrosAlgoritmo.VNS.getNumMaxIteracionesSinMejoraBusquedaLocal();
    }

    public double[] fitness(Solucion x) {
        double[] fit = mapaSoluciones.get(x);
        if (fit == null) {
            fit = DeciderFitnessFunction.switchFitnessF(x, patrones, entrada, parametros, parametrosAlgoritmo);
            mapaSoluciones.put(x, fit);
        }
        return fit;
    }

    @Override
    public Solucion bestImprovement(Solucion solucionInicial) {

        Solucion x = solucionInicial.clone();
        Solucion x_prime;
        int numIter = 0;
        double porcentajeMejora = 100;
        int cadaTantasIteraciones = parametrosAlgoritmo.VNS.getNumMaxIteracionesSinMejoraBusquedaLocal();
        double fitAnterior = -1;
        double fitActual = -1;

        // En la busqueda local, iteramos repetidas veces hasta que no haya mejora
        while (porcentajeMejora > parametrosAlgoritmo.VNS.getPorcentajeMinimoMejoria()/* FIXME CONDICION PARADA SESGADA
                && System.currentTimeMillis() - AbstractVariableNeighborhoodSearch.initTime < parametrosAlgoritmo.getMaxMilisecondsAllowed()*/) {

            x_prime = buscarSolucion(x);

            double f_x = fitness(x)[0];
            double f_x_prime = fitness(x_prime)[0];
            if (f_x_prime > f_x) { // si es mejor... NOTE: maximización
                x = x_prime;
                fitActual = f_x_prime;
                if (numIter % cadaTantasIteraciones == 0) {
                    if (fitAnterior == -1)
                        porcentajeMejora = 100;
                    else if (fitActual == -1)
                        porcentajeMejora = 0;
                    else
                        porcentajeMejora = Math.abs(fitAnterior - fitActual) * 100;

                    fitAnterior = f_x_prime;
                }
            } else if (numIter % cadaTantasIteraciones == 0) {
                if (fitAnterior == -1)
                    porcentajeMejora = 100;
                else if (fitActual == -1)
                    porcentajeMejora = 0;
                else
                    porcentajeMejora = Math.abs(fitAnterior - fitActual) * 100;

                fitAnterior = f_x;
            }

//            else
//                numIteracionesSinMejora++;

            numIter++;
//            if (Log.isOn() /*&& Log.checkIter(numIter)*/) {
//                Log.info("[BL] tiempo: " + (System.currentTimeMillis() - AbstractVariableNeighborhoodSearch.initTime) / 1000);
//                Log.info("[BL] fitness: " + fitness(x));
//                Log.info("[BL] iter sin mejora: " + numIteracionesSinMejora);
//            }
        }
        return x;
    }

    @Override
    public Solucion firstImprovement(Solucion solucionInicial) {
        Solucion x = solucionInicial.clone();
        Solucion x_prime;

        double f_x = fitness(x)[0];
        double f_x_prime;

        int numIt = 1;
        // Iteramos repetidas veces hasta que haya mejora
        do {
            x_prime = buscarSolucion(x);
            f_x_prime = fitness(x_prime)[0];
            numIt++;
        } while (f_x_prime <= f_x && numIt <= numMaxIteracionesSinMejoraBusquedaLocal); // si es mejor...paramos NOTE: maximización
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

    protected void doChange(Solucion x, String turnoA, String turnoB,
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

    protected XoRoShiRo128PlusRandom random = new XoRoShiRo128PlusRandom();

    List<Nucleo> getNucleos() {
        return this.entrada.getNucleos();
    }

    protected int getSlotMomentoActual() {
        return entrada.getSlotMomentoActual();
    }

    /**
     * Obtiene los sectores en los que el controlador trabaja dentro de un intervalo de la matriz de trabajo
     */
    protected Set<String> obtenerSectores(Solucion x, int controlador, int desde, int hasta) {
        String turno = x.getTurnos().get(controlador).substring(desde, hasta);
        Set<String> sectores = new HashSet<>();
        for (int i = getSlotMomentoActual() * LONGITUD_CADENAS; i <= turno.length() - LONGITUD_CADENAS; i += LONGITUD_CADENAS) {
            String sector = turno.substring(i, i + LONGITUD_CADENAS).toLowerCase();
            if (sector.equals(STRING_NO_TURNO)) return null; // no se puede hacer el cambio
            if (!sector.equals(STRING_DESCANSO))
                sectores.add(sector); // se añadira en caso de no estar ya
        }
        return sectores;

    }

    protected boolean comprobarNucleos(Set<String> sectoresC1, Set<String> sectoresC2, Controlador c1, Controlador c2) {

        if (c1 == null && c2 == null) return true; // si los dos son imaginario, el cambio se puede hacer sin problemas

//        Set<String> nucleosC1 = new HashSet<>();
//        for (String sector : sectoresC1) {
//            nucleosC1.addAll(
//                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector)
//            );
//        }
//
//        Set<String> nucleosC2 = new HashSet<>();
//        for (String sector : sectoresC2) {
//            if (sector.equals(STRING_DESCANSO)) continue;
//            nucleosC2.addAll(
//                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector)
//            );
//            if (!nucleosC1.containsAll(
//                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector))
//            ) return false;
//        }

        // si no es imaginario, comprobamos si esta acreditado para los sectores
        if (c1 != null)
            for (String sector : sectoresC2) {
                Set<String> nucleos =
                        MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(getNucleos(), sector);
                if (!nucleos.contains(c1.getNucleo())) return false;
            }
        if (c2 != null)
            for (String sector : sectoresC1) {
                Set<String> nucleos =
                        MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(getNucleos(), sector);
                if (!nucleos.contains(c2.getNucleo())) return false;
            }

        return true;

    }

    protected List<int[]> getIntervalos(String turno) {
        List<int[]> res = new ArrayList<>();

        // recorremos el turno
        int i = getSlotMomentoActual() * LONGITUD_CADENAS;

        while (i + LONGITUD_CADENAS <= turno.length() &&
                !esTrabajo(turno.substring(i, i + LONGITUD_CADENAS)))
            i += 3; // saltamos los descansos

        for (int f = i; f + LONGITUD_CADENAS <= turno.length(); f += LONGITUD_CADENAS) {
            if (!esTrabajo(turno.substring(f, f + LONGITUD_CADENAS))) {
                res.add(new int[]{i, f});
                i = f;

                while (i + LONGITUD_CADENAS <= turno.length() && !esTrabajo(turno.substring(i, i + LONGITUD_CADENAS)))
                    i += 3; // saltamos los descansos

                f = i;
            }
        }
        if (i < turno.length() - LONGITUD_CADENAS && esTrabajo(turno.substring(turno.length() - 3)))
            res.add(new int[]{i, turno.length()});

        return res;
    }

    private boolean esDescanso(String string) {
        return string.equals(STRING_DESCANSO) || string.equals(STRING_NO_TURNO);
    }

    /**
     * @return lista enteros de tamaño 2 con los índices de inicio y de fin no inclusive a nivel de string
     * sobre los que la matriz se divide en rejillas.
     */
    protected List<int[]> getRejillas(ArrayList<String> turnos) {
        List<int[]> resultado = new ArrayList<>();
        int desde = getSlotMomentoActual() * LONGITUD_CADENAS;

        for (int hasta = getSlotMomentoActual() * LONGITUD_CADENAS + LONGITUD_CADENAS;
             hasta <= turnos.get(0).length() - LONGITUD_CADENAS; hasta += LONGITUD_CADENAS) {

            for (String turno : turnos) {
                // si hay un cambio, hay rejilla
                if (!turno.substring(hasta - LONGITUD_CADENAS, hasta).equals(turno.substring(hasta, hasta + LONGITUD_CADENAS))) {
                    int[] r = {desde, hasta};
                    /*if (!resultado.contains(r))*/
                    resultado.add(r);
                    desde = hasta;
                    break;
                }
            }

        }
//        if (desde >= turnos.get(0).length()-LONGITUD_CADENAS)
        // hay que computar el último
        resultado.add(new int[]{desde, turnos.get(0).length()});
        return resultado;
    }
}
