package herramientas.pruebasVarias.InfoFitness;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import fitnessFunction.Fitness;
import patrones.Patrones;

import java.util.ArrayList;

import static herramientas.CridaUtils.STRING_DESCANSO;

public class FitnessFase3Info {

    public static void fitGlobal(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        int numControladores = individuo.getControladores().size();
        int numTurnos = individuo.getTurnos().get(0).length() / 3;

        double traPosicion = tiempoOptTrabajoPosicion(numControladores, numTurnos, individuo.getTurnos(), parametros);
        double traDescanso = tiempoOptTrabajoDescanso(numControladores, numTurnos, individuo.getTurnos(), parametros);
        double porcentaje = 1 - tiempoPorcentaje(numControladores, numTurnos, individuo.getTurnos(), parametros);

        double estadillos = estadillos(numControladores, numTurnos, individuo.getTurnos());

        double minimizarIntervalos = minimizarIntervalos(numControladores, numTurnos, individuo.getTurnos());
        double maximizarAcreditacion = maximizarAcreditacion(entrada, numControladores, numTurnos, individuo.getTurnos(), Fitness.sectoresElementalesTotales);

        double homogenea = homogenea(individuo.getTurnos());

    }



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
        double c = 0, e = 0, d = 0, x = 0;
        for (int i = 0; i < numeroControladores; i++) {
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (!str.equals(STRING_DESCANSO)) {
                    tPosicion += parametros.getTamanoSlots();
                    if (j + 1 == numeroFranjasHorarias) {
                        if (tPosicion != 0) {
                            diferencia += Math.abs(tPosicion - parametros.getTiempoTrabOpt());
                            if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 15) {
                                c++;
                            }
                            if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 20) {
                                d++;
                            }
                            if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 25) {
                                e++;
                            }
                            x++;
                            tPosicion = 0;
                        }
                    }
                } else {
                    if (tPosicion != 0) {
                        diferencia += Math.abs(tPosicion - parametros.getTiempoTrabOpt());
                        if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 15) {
                            c++;
                        }
                        if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 20) {
                            d++;
                        }
                        if (Math.abs(tPosicion - parametros.getTiempoTrabOpt()) >= 25) {
                            e++;
                        }
                        x++;
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
        System.out.println("DifMedia-TrabajoOpt: " + fitness + "|Total mas 15:" + c + "|Total mas 20:" + d + "|Total mas 25:" + e + "|Sobre un total de: " + x);
        System.out.println("DifMedia-TrabajoOpt: " + fitness + "|Total mas 15:" + (1 - (c / x)) + "|Total mas 20:" + (1 - (d / x)) + "|Total mas 25:" + (1 - (e / x)));
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
        double c = 0, e = 0, d = 0, x = 0;
        for (int i = 0; i < numeroControladores; i++) {
            tPosicion = 0;
            for (int j = 0; j <= numeroFranjasHorarias; j++) {
                if (j == numeroFranjasHorarias) {
                    if (!strPrevia.equals(STRING_DESCANSO)) {
                        diferencia += Math.abs(tPosicion - parametros.getTiempoPosOpt());
                        if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 10) {
                            c++;
                        }
                        if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 15) {
                            d++;
                        }
                        if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 25) {
                            e++;
                        }
                        x++;
                    }
                } else {
                    str = tabla.get(i).substring(j * 3, j * 3 + 3);
                    if (strPrevia != null) {
                        if (str.equals(strPrevia)) {
                            if (!str.equals(STRING_DESCANSO)) {
                                tPosicion += parametros.getTamanoSlots();
                            }
                        } else {
                            if (!strPrevia.equals(STRING_DESCANSO)) {
                                diferencia += Math.abs(tPosicion - parametros.getTiempoPosOpt());
                                if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 10) {
                                    c++;
                                }
                                if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 15) {
                                    d++;
                                }
                                if (Math.abs(tPosicion - parametros.getTiempoPosOpt()) >= 25) {
                                    e++;
                                }
                                x++;
                                tPosicion = parametros.getTamanoSlots();
                            } else {
                                tPosicion = parametros.getTamanoSlots();
                            }
                        }
                    } else if (!str.equals(STRING_DESCANSO)) {
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
        System.out.println("DifMedia-PosicionOpt: " + fitness + "|Total mas 10:" + c + "|Total mas 15:" + d + "|Total mas 25:" + e + "|Sobre un total de: " + x);
        System.out.println("DifMedia-PosicionOpt: " + fitness + "|Total mas 10:" + (1 - (c / x)) + "|Total mas 15:" + (1 - (d / x)) + "|Total mas 25:" + (1 - (e / x)));
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
        double x = 0;
        int c = 0, d = 0, e = 0;
        for (int i = 0; i < numeroControladores; i++) {
            array.add(i, 0.0);
            double tTotal = 0;
            double tEjecutivo = 0;
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (!str.equals(STRING_DESCANSO)) {
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
                x += diferencia;
                if (diferencia > 0.05) {
                    c++;
                }
                if (diferencia > 0.1) {
                    d++;
                }
                if (diferencia > 0.15) {
                    e++;
                }
            } else if (diferencia > parametros.getPorcentPosMax()) {
                diferencia = diferencia - parametros.getPorcentPosMax();
                array.set(i, diferencia / parametros.getPorcentPosMin());// 0.4 es la maxima diferencia para dejarlo normalizado
                numeroDesviaciones += 1;
                x += diferencia;
                if (diferencia > 0.05) {
                    c++;
                }
                if (diferencia > 0.1) {
                    d++;
                }
                if (diferencia > 0.15) {
                    e++;
                }
            }
        }
        System.out.println("SumaMediaDif%: " + x / numeroControladores + "| Total mas 5%: " + c + " |Total mas 10%: " + d + " |Total mas 15%: " + e);
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
            int in = 0;
            for (int j = 0; j < numeroFranjasHorarias; j++) {
                str = tabla.get(i).substring(j * 3, j * 3 + 3);
                if (str.equals(STRING_DESCANSO)) {
                    if (!strPrevia.equals(STRING_DESCANSO)) {
                        numIntervalosDescanso += 1;
                        in++;
                    }
                }
                strPrevia = str;
            }
            System.out.print("Rest c" + i + ":" + in + " - ");
        }
        System.out.println("\nNº Intervalos Descanso: " + numIntervalosDescanso);
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
        double mean = getMean(tabla);
        double desvTipica = getStdDev(tabla);
        double normalizado = (mean - desvTipica) / mean;
        System.out.println("Mean: " + mean + " - SD: " + desvTipica);
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
                if (!turno.substring(j, j + 3).equalsIgnoreCase(STRING_DESCANSO)) {
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
        System.out.println("Acreditaciones: " + cnt / turnos.size());
        double max = turnos.size() * sectoresElementalesTotales;
        double min = turnos.size();
        double fit = 1 - ((max - cnt) / (max - min));

        return fit;
    }

    public static double getMean(ArrayList<String> arrayList) {
        double sum = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.get(i).length(); j += 3) {
                if (!arrayList.get(i).substring(j, j + 3).equals("000") && !arrayList.get(i).substring(j, j + 3).equals(STRING_DESCANSO)) {
                    sum++;
                }
            }
        }
        return (sum * 5) / arrayList.size();
    }

    public static double getVariance(ArrayList<String> arrayList) {
        double mean = getMean(arrayList);
        double temp = 0;
        double sum = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.get(i).length(); j += 3) {
                if (!arrayList.get(i).substring(j, j + 3).equals("000") && !arrayList.get(i).substring(j, j + 3).equals(STRING_DESCANSO)) {
                    sum++;
                }
            }
            System.out.print("Work c" + i + ": " + sum * 5 + " - ");
            temp += ((sum * 5) - mean) * ((sum * 5) - mean);
            sum = 0;
        }
        System.out.println("");
        return temp / arrayList.size();
    }

    public static double getStdDev(ArrayList<String> arrayList) {
        return Math.sqrt(getVariance(arrayList));
    }
}
