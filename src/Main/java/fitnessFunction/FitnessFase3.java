package fitnessFunction;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Parametros;

import java.util.ArrayList;

/**
 * Esta clase contiene todas las funciones objetivos que se utilizan en la fase de optimizacion del algoritmo (fase 3).
 *
 * @author Tino
 */
public class FitnessFase3 {
    /*Obj1.1*/

    /**
     * Objetivo 1.1: Tiempo optimo de trabajo continuo
     *
     * @param numeroControladores   Numero de controladores.
     * @param numeroFranjasHorarias Numero de slots que forman el turno.
     * @param tabla                 Lista de turnos de trabajo.
     * @param parametros            Parametros del problema.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double tiempoOptTrabajoDescanso(int numeroControladores, int numeroFranjasHorarias, ArrayList<String> tabla, Parametros parametros) {
        String str = null;
        int tPosicion = 0;
        int diferencia = 0;
        double max = (parametros.getTiempoTrabOpt() - parametros.getTiempoTrabMin()) * (numeroFranjasHorarias / 6);
        ArrayList<Integer> desvios = new ArrayList<>();
        for (int i = 0; i < numeroControladores; i++) {
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (!str.equals("111")) {
                    tPosicion += parametros.getTamanoSlots();
                    if (j + 1 == numeroFranjasHorarias) {
                        if (tPosicion != 0) {
                            diferencia += Math.abs(tPosicion - parametros.getTiempoTrabOpt());
                            tPosicion = 0;
                        }
                    }
                } else {
                    if (tPosicion != 0) {
                        diferencia += Math.abs(tPosicion - parametros.getTiempoTrabOpt());
                        tPosicion = 0;
                    }
                }
            }
            desvios.add(diferencia);
            diferencia = 0;

        }
        double fitness = 0;
        for (int i = 0; i < numeroControladores; i++) {
            fitness += desvios.get(i);
        }
        fitness = fitness / numeroControladores;
        fitness = (max - fitness) / max;
        return fitness;
    }

    /**
     * Objetivo 1.2: Tiempo optimo de trabajo en una misma posicion continuo.
     *
     * @param numeroControladores   Numero de controladores.
     * @param numeroFranjasHorarias Numero de slots que forman el turno.
     * @param tabla                 Lista de turnos de trabajo.
     * @param parametros            Parametros del problema.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double tiempoOptTrabajoPosicion(int numeroControladores, int numeroFranjasHorarias, ArrayList<String> tabla, Parametros parametros) {
        String str = null;
        String strPrevia = null;
        int tPosicion = 0;
        int diferencia = 0;
        ArrayList<Integer> desvios = new ArrayList<>();
        double max = (parametros.getTiempoPosOpt() - parametros.getTiempoTrabMin()) * 8 * (numeroFranjasHorarias / 30);
        for (int i = 0; i < numeroControladores; i++) {
            tPosicion = 0;
            for (int j = 0; j <= numeroFranjasHorarias; j++) {
                if (j == numeroFranjasHorarias) {
                    if (!strPrevia.equals("111")) {
                        diferencia += Math.abs(tPosicion - parametros.getTiempoPosOpt());
                    }
                } else {
                    str = tabla.get(i).substring(j * 3, j * 3 + 3);
                    if (strPrevia != null) {
                        if (str.equals(strPrevia)) {
                            if (!str.equals("111")) {
                                tPosicion += parametros.getTamanoSlots();
                            }
                        } else {
                            if (!strPrevia.equals("111")) {
                                diferencia += Math.abs(tPosicion - parametros.getTiempoPosOpt());
                                tPosicion = parametros.getTamanoSlots();
                            } else {
                                tPosicion = parametros.getTamanoSlots();
                            }
                        }
                    } else if (!str.equals("111")) {
                        tPosicion = parametros.getTamanoSlots();
                    }
                    strPrevia = str;
                }
            }
            strPrevia = null;
            desvios.add(diferencia);
            diferencia = 0;
        }
        double fitness = 0;
        for (int i = 0; i < numeroControladores; i++) {
            fitness += desvios.get(i);
        }
        fitness = fitness / numeroControladores;
        fitness = (max - fitness) / max;
        return fitness;
    }

    /**
     * Objetivo 1.3: Porcentaje de trabajo optimo entre posiciones (Ejecutivo/Planificador).
     *
     * @param numeroControladores   Numero de controladores.
     * @param numeroFranjasHorarias Numero de slots que forman el turno.
     * @param tabla                 Lista de turnos de trabajo.
     * @param parametros            Parametros del problema.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double tiempoPorcentaje(int numeroControladores, int numeroFranjasHorarias, ArrayList<String> tabla, Parametros parametros) {
        String str = null;
        double penalizacion = 0;
        double diferencia = 0;
        int numeroDesviaciones = 0; //Esta variable es para saber cuantos controladores se desvían
        //De momento esa variable no se usa

        ArrayList<Double> array = new ArrayList<>();
        for (int i = 0; i < numeroControladores; i++) {
            array.add(i, 0.0);
            double tTotal = 0;
            double tEjecutivo = 0;
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (!str.equals("111")) {
                    if (Character.isUpperCase(str.charAt(0))) {
                        tEjecutivo += parametros.getTamanoSlots();
                    }
                    tTotal += parametros.getTamanoSlots();
                }
            }
            diferencia = tEjecutivo / tTotal;
            if (diferencia < parametros.getPorcentPosMin()) {
                diferencia = parametros.getPorcentPosMin() - diferencia;
                array.set(i, diferencia / parametros.getPorcentPosMin());// 0.4 es la maxima diferencia para dejarlo normalizado
                numeroDesviaciones += 1;
            } else if (diferencia > parametros.getPorcentPosMax()) {
                diferencia = diferencia - parametros.getPorcentPosMax();
                array.set(i, diferencia / parametros.getPorcentPosMin());// 0.4 es la maxima diferencia para dejarlo normalizado
                numeroDesviaciones += 1;
            }
        }
        for (int i = 0; i < numeroControladores; i++) {
            penalizacion += array.get(i);
        }
        return penalizacion / numeroControladores;
    }

    /**
     * Objetivo 2: Similitud de la solucion a estadillos previamiente utilizados.
     *
     * @param numeroControladores   Numero de controladores.
     * @param numeroFranjasHorarias Numero de slots que forman el turno.
     * @param tabla                 Lista de turnos de trabajo.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double estadillos(int numeroControladores, int numeroFranjasHorarias, ArrayList<String> tabla) {
        int count = 0;
        for (int i = 0; i < numeroControladores; i++) {
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                String actual = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (j < numeroFranjasHorarias - 1) {
                    String derecha = tabla.get(i).substring((j + 1) * 3, (j + 1) * 3 + 3);
                    if (actual.equalsIgnoreCase(derecha)) {
                        count++;
                    }
                }
                if (i < numeroControladores - 1) {
                    String abajo = tabla.get(i + 1).substring(j * 3, j * 3 + 3);
                    if (actual.equalsIgnoreCase(abajo)) {
                        count++;
                    }
                }
            }
        }
        double max = (numeroControladores - 1) * (numeroFranjasHorarias - 1) * 2;
        double normalizado = 1 - ((max - count) / max);
        return normalizado;
    }

    /**
     * Objetivo 3.1: Minimizar numero de intervalos de descansos. Lo cual es equivalente a agruparlos.
     *
     * @param numeroControladores   Numero de controladores.
     * @param numeroFranjasHorarias Numero de slots que forman el turno.
     * @param tabla                 Lista de turnos de trabajo.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double minimizarIntervalos(int numeroControladores, int numeroFranjasHorarias, ArrayList<String> tabla) {
        String strPrevia = "";
        String str = null;
        double min = numeroControladores;
        double max = (numeroFranjasHorarias / 6) * numeroControladores;
        int numIntervalosDescanso = 0;
        for (int i = 0; i < numeroControladores; i++) {
            strPrevia = "";
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (str.equals("111")) {
                    if (!strPrevia.equals("111")) {
                        numIntervalosDescanso += 1;
                    }
                }
                strPrevia = str;
            }
        }
        double normalizado = (max - numIntervalosDescanso) / (max - min);
        return normalizado;
    }

    /**
     * Objetivo 4: Equilibrio de la carga de trabajo de los controladores.
     *
     * @param tabla Lista de turnos de trabajo.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double homogenea(ArrayList<String> tabla) {
        double mean = Fitness.getMean(tabla);
        double desvTipica = Fitness.getStdDev(tabla);
        double normalizado = (mean - desvTipica) / mean;
        return normalizado;
    }

    /**
     * Objetivo 3.2: Maximizar el numero de sectores elementales que cubren los controladores para mantener las acreditaciones.
     *
     * @param entrada                    Entrada del problema
     * @param numControladores           Numero de controladores.
     * @param numTurnos                  Numero de slots que forman el turno.
     * @param turnos                     Lista de turnos de trabajo.
     * @param sectoresElementalesTotales Numero total de sectores elementales.
     * @return Valor de la funcion objetivo normalizado.
     */
    public static double maximizarAcreditacion(Entrada entrada, int numControladores, int numTurnos, ArrayList<String> turnos, double sectoresElementalesTotales) {
        ArrayList<Sector> sectoresAbiertos = entrada.getListaSectoresAbiertos();
        double cnt = 0;
        for (int i = 0; i < turnos.size(); i++) {
            String turno = turnos.get(i);
            ArrayList<Sector> sectoresTurno = new ArrayList<>();
            for (int j = 0; j < turno.length(); j += 3) {
                if (!turno.substring(j, j + 3).equalsIgnoreCase("111")) {
                    for (Sector sector : sectoresAbiertos) {
                        if (sector.getId().equalsIgnoreCase(turno.substring(j, j + 3)) && !sectoresTurno.contains(sector)) {
                            sectoresTurno.add(sector);
                        }
                    }
                }
            }
            ArrayList<String> elementales = new ArrayList<>();
            for (int j = 0; j < sectoresTurno.size(); j++) {
                ArrayList<String> sectoresEl = sectoresTurno.get(j).getSectoresElementales();
                for (int k = 0; k < sectoresEl.size(); k++) {
                    String sectorEl = sectoresEl.get(k);
                    boolean contain = false;
                    for (int l = 0; l < elementales.size(); l++) {
                        if (sectorEl.equalsIgnoreCase(elementales.get(l))) {
                            contain = true;
                        }
                    }
                    if (!contain) {
                        elementales.add(sectorEl);
                    }
                }
            }
            cnt += elementales.size();
        }
        double max = turnos.size() * sectoresElementalesTotales;
        double min = turnos.size();
        double fit = 1 - ((max - cnt) / (max - min));

        return fit;
    }
}
