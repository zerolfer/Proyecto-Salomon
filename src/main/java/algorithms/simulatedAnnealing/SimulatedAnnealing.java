package algorithms.simulatedAnnealing;


import algorithms.MetaheuristicUtil;
import algorithms.simulatedAnnealing.moves.DeciderMove;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import main.MainPruebas;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase principal del algoritmo de optimizacion (SA).
 *
 * @author Tino
 */
public class SimulatedAnnealing {
    /**
     * Metodo para utilizar el SA, en todos las soluciones de la lista "poblacionInicial".
     *
     * @param poblacionInicial Conjunto de soluciones.
     * @param pa               Parametros del algoritmo.
     * @param p                Parametros del problema.
     * @param patrones         Patrones utilizados para la comprobacion de restricciones.
     * @param entrada          Entrada del problema.
     * @return Conjunto de soluciones optimizadas.
     */
    public static ArrayList<Solucion> bucleSA(List<Solucion> poblacionInicial, ParametrosAlgoritmo pa,
                                              Parametros p, Patrones patrones, Entrada entrada) {
        /*
         * Bucle para operar sobre varios individuos si es necesario
         */
        ArrayList<Solucion> poblacionMejorada = new ArrayList<>();
        Solucion individuo = null;
        for (int i = 0; i < poblacionInicial.size(); i++) {
            individuo = simulatedAnnealing(poblacionInicial.get(i), i, pa, p, patrones, entrada);
            if (individuo != null) {
                poblacionMejorada.add(individuo);
            }
        }
        System.out.println("FIN RECOCIDO");
        return poblacionMejorada;
    }

    /**
     * Algoritmo de recocido simulado.
     *
     * @param individuo     Solucion.
     * @param n             Numero de solucion.
     * @param parametrosAlg Parametros del algoritmo.
     * @param parametros    Parametros del problema.
     * @param patrones      Patrones utilizados para la comprobacion de restricciones.
     * @param entrada       Entrada del problema.
     * @return Devuelve una solucion optimizada.
     */
    public static Solucion simulatedAnnealing(Solucion individuo, int n, ParametrosAlgoritmo parametrosAlg,
                                              Parametros parametros, Patrones patrones, Entrada entrada) {
        /*
         * Ordena la matriz por la vagueza de los controladores y elimina los controladores que no trabajan
         * Realiza cambios en la matriz con movimientoTrabajo un numero de veces igual a ciclos
         * Y devuelve la matriz con los cambios.
         */

        System.out.println("Aplicando al individuo " + n + "...");
        long t1 = System.currentTimeMillis();
        long ti1 = t1;
        long ti2 = 0;
        double pem = 0;
        double temperatura = parametrosAlg.SA.getTemperaturaInicial();
        double descensoTemp = parametrosAlg.SA.getDescensoTemperatura();
        String formulaDesc = "clasica";
        if (descensoTemp == -1) { //USO DE Formulas distintas para el descenso de temp
            formulaDesc = "Aarts";
            descensoTemp = 0.9;
        } else if (descensoTemp == -2) {
            formulaDesc = "Adaptativa";
        }
        double iteracionesTemp = parametrosAlg.SA.getIteracionesTemperaturaL();
        boolean cutOff = false;
        if (iteracionesTemp == -1) { //USO DE CUTOOF
            iteracionesTemp = 3000;
            cutOff = true;
        }
        double condicionParadaCiclos = parametrosAlg.SA.getCondicionParadaCiclos();
        double condicionParadaPorcent = parametrosAlg.SA.getCondicionParadaPorcent();
        double numeroMejoras = parametrosAlg.SA.getCondicionParadaNumeroMejoras();
        int gMax = (parametrosAlg.SA.getTamañoMaxMov() / parametros.getTamanoSlots()) * 3;
        int gMin = (parametrosAlg.SA.getTamañoMinMov() / parametros.getTamanoSlots()) * 3;
        int iteracionActual = 0;
        double val1 = 0, val2 = 0, porcentMejora = 1;
        individuo = MetaheuristicUtil.orderByLazyCriteria(individuo);
        double rand = 0;
        double empeora = 0;
        double aMejor = 1;
        Solucion bestSol = individuo;
        double[] fitIteraciones1 = DeciderFitnessFunction.switchFitnessF(individuo, patrones, entrada, parametros, parametrosAlg);
        double[] fitIteraciones2;
        double fitAnt = fitIteraciones1[0];
        double bestFit = fitAnt;


        int m = 0;

        while (iteracionActual % condicionParadaCiclos != 0 || (condicionParadaPorcent < porcentMejora || aMejor >= numeroMejoras)) {

            Solucion individuo2 = DeciderMove.switchMoves(individuo, gMax, gMin, 0, parametrosAlg, patrones, entrada,
                    parametros, new ArrayList<String>());
            if (individuo.getTurnos().size() != individuo.getControladores().size()) {
                individuo2 = MetaheuristicUtil.orderByLazyCriteria(individuo2);
            }
            fitIteraciones2 = DeciderFitnessFunction.switchFitnessF(individuo2, patrones, entrada, parametros,
                    parametrosAlg);
            val1 = fitIteraciones1[0];
            val2 = fitIteraciones2[0];
            if(fitIteraciones2[2]==1) {individuo = individuo2;fitIteraciones1 = fitIteraciones2;bestFit = val2;bestSol = individuo2;break;}
            if (val1 >= val2) {
                empeora = Math.exp((-(val1 - val2) / temperatura)); //Porcentaje de empeoramiento
                if ((rand = Math.random()) < empeora) {
                    individuo = individuo2;
                    fitIteraciones1 = fitIteraciones2;
				} else {
                    individuo2 = null;
                }
            } else if (val1 < val2) {
                if (bestFit < val2) {
                    bestFit = val2;
                    bestSol = individuo2;
                }
                m++;
                individuo = individuo2;
                fitIteraciones1 = fitIteraciones2;

            }
 
            if (cutOff && m == 1000) {
                temperatura = temperatura * descensoTemp;
            }
            /*Trazas3*/
            iteracionActual++;
            if (iteracionActual % iteracionesTemp == 0) {
                if (formulaDesc.equalsIgnoreCase("Clasica")) {
                    temperatura = temperatura * descensoTemp;
                } else if (formulaDesc.equalsIgnoreCase("Aarts")) {
                    temperatura =
                            parametrosAlg.SA.getTemperaturaInicial() / (1 + descensoTemp * Math.log(1 + iteracionActual));
                } else if (formulaDesc.equalsIgnoreCase("Adaptativa")) {
                    temperatura = 0.15 * (1 - bestFit + temperatura - ((double) iteracionActual / 500000.0));
                    //elevado a la 1. TODO: PRUEBAAAAAA!
                    if (temperatura < 0) {
                        temperatura = 0.00000000000001;
                    }
                    //System.out.println("temp: "+temperatura);
                }
                pem = parametrosAlg.SA.getPorcentajeEleccionMov() * 0.99;
                parametrosAlg.SA.setPorcentajeEleccionMov(pem);
                aMejor = m / iteracionesTemp;
                m = 0;
            }
            if (iteracionActual % condicionParadaCiclos == 0) {
                porcentMejora = (bestFit * 100 / fitAnt) - 100;
                ti2 = System.currentTimeMillis();
                System.out.println("TempAct: " + temperatura + "| " + "Iteracion: " + iteracionActual + " |Tama" +
                        "ño Ind: " + individuo.getTurnos().size() + " |Porct. de Mejora: " + porcentMejora + "% o N. " +
                        "Mejoras: " + aMejor + " |FITAnt .. FITBest: " + fitAnt + " " + bestFit + " |tiempo: " + (((ti2 - ti1) / 1000.0) / 60.0));
                ti1 = System.currentTimeMillis();
                if (bestFit > fitAnt) {
                    fitAnt = bestFit;
                }
            }
            if (bestFit == 1) {
                break;
            }
        }
        long t2 = System.currentTimeMillis();
        //System.out.println("Soluciones con el mismo fitness: "+ tmpcnt + "/"+iteracionActual);
        double res = Restricciones.penalizacionPorRestricciones(bestSol, patrones, entrada, parametros);
        double tSol = (((t2 - t1) / 1000.0) / 60.0);
        MainPruebas.tiemposPorSolucionAux[n] = tSol;
        MainPruebas.problema += ("TIME: " + tSol + " | Restricciones: " + res + "\n");
        System.out.println("TIME: " + tSol + " | Restricciones: " + res);
        if (res == 0) {
            MainPruebas.tiempoFactibleAux += tSol;
            if (MainPruebas.TiemposResultadosproblema[1] == 0) {
                MainPruebas.TiemposResultadosproblema[1] =
                        (((System.currentTimeMillis() - MainPruebas.TiemposResultadosproblema[0]) / 1000.0) / 60.0);
            }
        }
        return bestSol;
    }

}

