package fitnessFunction;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.PesosObjetivos;
import estructurasDatos.Solucion;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

import static herramientas.CridaUtils.*;


/**
 * Clase utilizada para guardar las distintas funciones objetivo del problema.
 *
 * @author Tino
 */
public class Fitness {

    private static double ctrlsCompletos = 0;
    public static double sectoresElementalesTotales = 0;

    /**
     * Funcion objetivo utilizada para reducir el numero de controladores de forma progresiva. Permite soluciones invalidas y no las penaliza.
     *
     * @param individuo  Solucion
     * @param entrada    Entrada del problema.
     * @param patrones   Patrones utilizados para comprobar el cumplimiento de las restricciones.
     * @param parametros Parametros del problema.
     * @return Valor de la funcion objetivo (no normalizado).
     */
    public static double[] fitReduccionControladoresAIS(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        /*
         * Fitness function allow Invalid solutions
         * Porcentaje de relleno de turnos (quitando los dos menos llenos y multiplicando por el inverso del numero de turnos)*/
        double letras = 0;
        double tmn = individuo.getTurnos().size();
        for (int i = 0; i < individuo.getTurnos().size(); i++) {
            int[] sum = slotsClassification(individuo.getTurnos().get(i));
            letras += (sum[1] * (i + 1));
        }

        //double p = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        double fitness = letras / (tmn * tmn);
        double[] fit = {0};
        fit[0] = fitness;
        return fit;//Busca maximos (+)
    }

    /**
     * Funcion objetivo para la penalizacion de restricciones.
     *
     * @param individuo  Solucion
     * @param entrada    Entrada del problema.
     * @param patrones   Patrones utilizados para comprobar el cumplimiento de las restricciones.
     * @param parametros Parametros del problema.
     * @return Valor de la funcion objetivo (no normalizado).
     */
    public static double[] fitnessOnlyConstrains(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        /*
         * Fitness function allow Invalid solutions
         * Porcentaje de relleno de turnos (quitando los dos menos llenos y multiplicando por el inverso del numero de turnos)*/

        double p = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        double[] fit = {1000 - p};
        return fit;//Busca maximos (+)
    }


    /**
     * Metodo que devuelve un array con el numero de slots de trabajo y descanso.
     *
     * @param controlador Turno de trabajo.
     * @return Array con el numero de slots de descanso y trabajo. <br/>
     *          Posicion 0: descanso y fuera de turno <br/>
     *          Posicion 1: trabajos
     */
    public static int[] slotsClassification(String controlador) {
        int[] sum = {0, 0};
        int unos = 0, letras = 0;
        for (int e = 0; e < controlador.length(); e += 3) {
            if (controlador.substring(e, e + 3).equals(STRING_DESCANSO)|| controlador.substring(e, e + 3).equals(STRING_NO_TURNO)) {
                unos++;
            } else {
                letras++;
            }
        }
        sum[0] = unos;
        sum[1] = letras;
        return sum;

    }

    /**
     * Media de la carga de trabajo.
     *
     * @param arrayList Turnos de trabajo.
     * @return Media de la carga de trabajo.
     */
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

    /**
     * Varianza de la carga de trabajo.
     *
     * @param arrayList Turnos de  trabajo.
     * @return Varianza de la carga de trabajo.
     */
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
            temp += ((sum * 5) - mean) * ((sum * 5) - mean);
            sum = 0;
        }
        return temp / arrayList.size();
    }

    /**
     * Desviacion estandar de la carga de trabajo.
     *
     * @param arrayList Turnos de  trabajo.
     * @return Desviacion estandar de la carga de trabajo.
     */
    public static double getStdDev(ArrayList<String> arrayList) {
        return Math.sqrt(getVariance(arrayList));
    }

    /**
     * Funcion objetivo utilizada para la reduccion de controladores(hasta el numero de controladores requerido) y reducir el numero de restriciones incumplidas.
     *
     * @param individuo  Solucion.
     * @param entrada    Entrada del problema.
     * @param patrones   Patrones utilizados para comprobar las restricciones.
     * @param parametros Parametros del problema.
     * @return Array: Valor de la funcion objetivo normalizado + primer objetivo normalizado + Segundo objetivo normalizado.
     */
    public static double[] fitPonderadoRestricYNumCtrls(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        double fit = 0, f1 = 0, f2 = 0;
        double tmn = individuo.getTurnos().size();
        double nSlots = entrada.getSectorizacion().size();
        double maxRestricciones = 0;
        if (entrada.getTurno().getNombre().equalsIgnoreCase("Noche")) {
            maxRestricciones = 20 * tmn; //(16 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        } else {
            maxRestricciones = 18 * tmn; //(14 * tmn)+ (4*(individuo.getTurnos().get(0).length()/3)*0.5*tmn);
        }


        //double r = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        double r = Restricciones.comprobarRestriccionesEnParalelo(individuo, patrones, entrada, parametros);

        //System.out.println(maxRestricciones);
        ///if(tempR<r){tempR=r;System.out.println(tempR);}
        double cReq = entrada.getControladores().size();
        if (cReq >= tmn) {
            fit = (maxRestricciones - r) / maxRestricciones;
            f2 = fit;
            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit);
            }
        } else {
            /*Inicio Normalizacion*/
            double cMax = entrada.getListaSectoresAbiertos().size() * 3;
            double f1Max = 0;
            double f1Min = 0;
            for (int i = 1; i <= tmn; i++) {
                if (i < Math.floor(getCtrlsCompletos())) {
                    f1Min += nSlots * (i * 2);
                }
            }
            f1Min = f1Min / (cMax * cMax);
            for (int i = 1; i <= tmn; i++) {
                if (!(i <= (tmn - Math.floor(getCtrlsCompletos())))) {
                    f1Max += nSlots * (i * 2);
                }
            }
            f1Max = f1Max / (cReq * cReq);
            /*Fin Normalizacion*/
            for (int i = 0; i < tmn; i++) {
                int[] sum = slotsClassification(individuo.getTurnos().get(i));
                f1 += (sum[1] * ((i * 2) + 1));
            }
            f1 = f1 / (tmn * tmn);
            f1 = (f1 - f1Min) / (f1Max - f1Min); //igual a: 1 - ((f1Max - f1) / (f1Max - f1Min));
            f2 = (maxRestricciones - r) / maxRestricciones;
            fit = f1 * 0.4 + f2 * 0.6;
            if (fit < 0 || fit > 1) {
                System.out.println("Error, el fitness deberia ser un valor entre 0 y 1: " + fit + " - " + f1 + " - " + f2);
            }
        }

        double[] f = {fit, f1, f2, 0, 0};
        return f;
    }

    public static double getCtrlsCompletos() {
        return ctrlsCompletos;
    }

    public static void setCtrlsCompletos(double ctrlsCompletos) {
        Fitness.ctrlsCompletos = ctrlsCompletos;
    }

    /**
     * Funcion objetivo fase 3. Objetivos deseables. Suma ponderada de 4 objetivos con distintos sub-objetivos.
     *
     * @param individuo  Solucion.
     * @param entrada    Entrada del problema.
     * @param patrones   Patrones utilizados para la comprobacion de restricciones.
     * @param parametros Parametros del problema.
     * @return Array: Valor de la funcion objetivo normalizado + 4 objetivos sin ponderar.
     */
    public static double[] fitGlobal(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        int numControladores = individuo.getControladores().size();
        int numTurnos = individuo.getTurnos().get(0).length() / 3;
        double traPosicion = FitnessFase3.tiempoOptTrabajoPosicion(numControladores, numTurnos, individuo.getTurnos(), parametros);
        double traDescanso = FitnessFase3.tiempoOptTrabajoDescanso(numControladores, numTurnos, individuo.getTurnos(), parametros);
        double porcentaje = 1 - FitnessFase3.tiempoPorcentaje(numControladores, numTurnos, individuo.getTurnos(), parametros);
        PesosObjetivos pObj = parametros.getPesosObjetivos();
        double cDeseables = pObj.getPesoObj1Sub1() * traPosicion + pObj.getPesoObj1Sub2() * traDescanso + pObj.getPesoObj1Sub3() * porcentaje;

        double estadillos = FitnessFase3.estadillos(numControladores, numTurnos, individuo.getTurnos());

        double minimizarIntervalos = FitnessFase3.minimizarIntervalos(numControladores, numTurnos, individuo.getTurnos());
        double maximizarAcreditacion = FitnessFase3.maximizarAcreditacion(entrada, numControladores, numTurnos, individuo.getTurnos(), sectoresElementalesTotales);

        double obj3 = minimizarIntervalos * pObj.getPesoObj3Sub1() + maximizarAcreditacion * pObj.getPesoObj3Sub2();

        double homogenea = FitnessFase3.homogenea(individuo.getTurnos());

        double fitness = pObj.getPesoObj1() * cDeseables + pObj.getPesoObj2() * estadillos + pObj.getPesoObj3() * minimizarIntervalos + pObj.getPesoObj4() * homogenea;

        double[] f = {fitness, cDeseables, estadillos, obj3, homogenea};
        return f;
    }

    public static double getSectoresElementalesTotales() {
        return sectoresElementalesTotales;
    }

    public static void setSectoresElementalesTotales(double sectoresElementalesTotales) {
        Fitness.sectoresElementalesTotales = sectoresElementalesTotales;
    }

    /**
     * Funcion obtivo en pruebas.
     *
     * @param individuo  Solucion
     * @param entrada    Entrada de problema..
     * @param patrones   Patrones utilizados para la comprobacion de restricciones.
     * @param parametros Parametros del problema.
     * @return Valor de la funcion objetivo.
     */
    public static double[] fitPonderadoRestricYNumCtrls2(Solucion individuo, Entrada entrada, Patrones patrones, Parametros parametros) {
        double fit = 0, f2 = 0, f3 = 0;
        double tmn = individuo.getTurnos().size();
        double nSlots = entrada.getSectorizacion().size();
        double maxRestricciones = 0;
        if (entrada.getTurno().getNombre().equalsIgnoreCase("Noche")) {
            maxRestricciones = (16 * tmn) + (4 * (individuo.getTurnos().get(0).length() / 3) * 0.5 * tmn);
        } else {
            maxRestricciones = (14 * tmn) + (4 * (individuo.getTurnos().get(0).length() / 3) * 0.5 * tmn);
        }
        double r = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
        //System.out.println(maxRestricciones);
        ///if(tempR<r){tempR=r;System.out.println(tempR);}
        if (individuo.getControladores().size() >= tmn) {
            fit = (maxRestricciones - r) / maxRestricciones;
            f2 = fit;
            if (fit < 0 || fit > 1) {
                System.out.println(fit);
            }
        } else {
            /*Calculo de trabajo x Controladores imaginarios*/
            int p = 0;
            for (int i = 0; i < tmn; i++) {
                p += findTrabajoImg(individuo, i);
            }
            f2 = (maxRestricciones - r) / maxRestricciones;
            fit = f2 * 0.8 + p;
            if (fit < 0 || fit > 1) {
                System.out.println(fit);
            }
        }

        double[] f = {fit, 0, f2, 0, 0};
        return f;
    }

    /**
     * Pruebas: Comprueba el trabajo de los controladores imaginarios.
     *
     * @param individuo
     * @param i
     * @return
     */
    private static int findTrabajoImg(Solucion individuo, int i) {
        boolean find = false;
        int p = 0;
        for (int j = 0; j < individuo.getControladores().size(); j++) {
            if (individuo.getControladores().get(j).getTurnoAsignado() == i) {
                find = true;
            }
        }
        if (!find) {
            String t = individuo.getTurnos().get(i);
            for (int j = 0; j < t.length(); j += 3) {
                if (!t.substring(j, j + 3).equalsIgnoreCase(STRING_DESCANSO)) {
                    p++;
                }
            }
        }
        return p;
    }
}
