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
        for (int i = 0; i < poblacionInicial.size(); i++) {//Paralelizar
            individuo = simulatedAnnealing(poblacionInicial.get(i), i, pa, p, patrones, entrada);
            if (individuo != null) {
                poblacionMejorada.add(individuo);
            }
        }
        /**/
        double maxAux = 0, minAux = 1000000000, mediaAux = 0;
        for (int i = 0; i < MainPruebas.tiemposPorSolucionAux.length; i++) {
            double a = MainPruebas.tiemposPorSolucionAux[i];
            mediaAux += a;
            if (maxAux < a) {
                maxAux = a;
            }
            if (minAux > a) {
                minAux = a;
            }
        }
        MainPruebas.TiemposResultadosproblema[2] = mediaAux / MainPruebas.tiemposPorSolucionAux.length;
        MainPruebas.TiemposResultadosproblema[3] = maxAux;
        MainPruebas.TiemposResultadosproblema[4] = minAux;
        MainPruebas.TiemposResultadosproblema[5] = mediaAux;
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
        MainPruebas.problema += ("Aplicando al individuo " + n + "..." + "\n");
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


        /*Trazas1*/
//		ArrayList<ArrayList<String>> trazas = new ArrayList<ArrayList<String>>();
//		ArrayList<String> iteracion = new ArrayList<String>();
//		String trazaL ="Iteracion;Mejora;EmpeoraAceptada;EmpeoraNoAceptada\n";
        int m = 0, pa = 0, pn = 0;
//		String trazaFit = "Fitness*;Fitness;FitnessF1;FitnessF2;FitnessF3;FitnessF4;Temperatura\n";
//		int tmpcnt=0;
        /*Trazas1*/
        while (iteracionActual % condicionParadaCiclos != 0 || (condicionParadaPorcent < porcentMejora || aMejor >= numeroMejoras)) {
            /*Trazas2*/
//			iteracion.add(""+iteracionActual);//ciclo
            /*Trazas2*/
//			Solucion individuo2 = DeciderMove.switchMoves(individuo,gMax,gMin, 0, parametrosAlg, patrones, entrada,
//			parametros, iteracion);
            Solucion individuo2 = DeciderMove.switchMoves(individuo, gMax, gMin, 0, parametrosAlg, patrones, entrada,
                    parametros, new ArrayList<String>());
            if (individuo.getTurnos().size() != individuo.getControladores().size()) {
                individuo2 = MetaheuristicUtil.orderByLazyCriteria(individuo2);
            }

            //fitIteraciones1 = DeciderFitnessFunction.switchFitnessF(individuo, patrones, entrada, parametros,
            // parametrosAlg);
            fitIteraciones2 = DeciderFitnessFunction.switchFitnessF(individuo2, patrones, entrada, parametros,
                    parametrosAlg);
            val1 = fitIteraciones1[0];
            val2 = fitIteraciones2[0];

            if (val1 >= val2) {
//				if(val1==val2){tmpcnt++;}
                empeora = Math.exp((-(val1 - val2) / temperatura)); //Porcentaje de empeoramiento
                if ((rand = Math.random()) < empeora) {
                    individuo = individuo2;
                    fitIteraciones1 = fitIteraciones2;
//					trazaFit=trazaFit+(bestFit+"").replace(".",",")+";"+(val2+"").replace(".",",")+";"+
//					(fitIteraciones2[1]+"").replace(".",",")+";"+(fitIteraciones2[2]+"").replace(".",",")+";"+
//					(fitIteraciones2[3]+"").replace(".",",")+";"+(fitIteraciones2[4]+"").replace(".",",")+";"+
//					(temperatura+"").replace(".", ",")+"\n";
                } else {
                    individuo2 = null;
//					trazaFit=trazaFit+(bestFit+"").replace(".",",")+";"+(val1+"").replace(".",",")+";"+
//					(fitIteraciones1[1]+"").replace(".",",")+";"+(fitIteraciones1[2]+"").replace(".",",")+";"+
//					(fitIteraciones1[3]+"").replace(".",",")+";"+(fitIteraciones1[4]+"").replace(".",",")+";"+
//					(temperatura+"").replace(".", ",")+"\n";
                }
            } else if (val1 < val2) {
                if (bestFit < val2) {
                    bestFit = val2;
                    bestSol = individuo2;
                }
                m++;
                individuo = individuo2;
                fitIteraciones1 = fitIteraciones2;
//				trazaFit=trazaFit+(bestFit+"").replace(".",",")+";"+(val2+"").replace(".",",")+";"+
//				(fitIteraciones2[1]+"").replace(".",",")+";"+(fitIteraciones2[2]+"").replace(".",",")+";"+
//				(fitIteraciones2[3]+"").replace(".",",")+";"+(fitIteraciones2[4]+"").replace(".",",")+";"+
//				(temperatura+"").replace(".", ",")+"\n";
            }
            /*Trazas3*/
//			iteracion.add((val1+"").replace(".", ","));//fitness Ind
//			iteracion.add((fitIteraciones1[1]+"").replace(".", ","));//fitness Ind f1
//			iteracion.add((fitIteraciones1[2]+"").replace(".", ","));//fitness Ind f2
//			iteracion.add((Math.abs(val2-val1)+""));//fitness IndGenerado
//			iteracion.add((bestFit+"").replace(".", ","));//fitness Ind
//			iteracion.add((temperatura+"").replace(".", ","));
//			if(val1>=val2){
//				iteracion.add("0");//mejora?
//				iteracion.add((empeora+""));//probabilidad de eleccion
//				if(rand<empeora){
//					iteracion.add("1");//Cambia?
//					pa++;
//				}else{
//					iteracion.add("0");//Cambia?
//					pn++;
//				}
//			}else{
//				iteracion.add("1");//mejora?
//				iteracion.add("0");//probabilidad de eleccion
//				iteracion.add("0");//Cambia?
//			}
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
                /*Trazas4*/
//				trazaL =trazaL +iteracionActual+";"+m+";"+pa+";"+pn+"\n";
                pa = 0;
                pn = 0;
                m = 0;
                /*Trazas4*/
            }
            if (iteracionActual % condicionParadaCiclos == 0) {
//				System.out.println(pem);
                porcentMejora = (bestFit * 100 / fitAnt) - 100;
                ti2 = System.currentTimeMillis();
                MainPruebas.problema += ("TempAct: " + temperatura + "| " + "Iteracion: " + iteracionActual + " |Tama" +
                        "ño Ind: " + individuo.getTurnos().size() + " |Porct. de Mejora: " + porcentMejora + "% o N. " +
                        "Mejoras: " + aMejor + " |FITAnt .. FITBest: " + fitAnt + " " + bestFit + " |tiempo: " + (((ti2 - ti1) / 1000.0) / 60.0) + "\n");
                //	System.out.print("TempAct: "+ temperatura+"| ");
                //	System.out.println("Iteracion: "+ iteracionActual +" |Tamaño Ind: " +individuo.getTurnos().size()
                //	+ " |Porct. de Mejora: "+ porcentMejora+ "% o N. Mejoras: "+aMejor+ " |FITAnt .. FITBest: "+
                //	fitAnt +" .. "+ bestFit + " |tiempo: "+ (((ti2-ti1)/1000.0)/60.0));

                ti1 = System.currentTimeMillis();
                if (bestFit > fitAnt) {
                    fitAnt = bestFit;
                }
            }
//			trazas.add(iteracion);
//			iteracion = new ArrayList<String>();
            if (bestFit == 1) {
                break;
            }
        }
        long t2 = System.currentTimeMillis();
        //System.out.println("Soluciones con el mismo fitness: "+ tmpcnt + "/"+iteracionActual);
        double res = Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros);
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
        /*Trazas5*/
//		Trazas.getTrazas2().add(Main.Main.carpetaTrazas+"/trazasL"+n+".csv");
//		Trazas.getTrazas2().add(trazaL);
//		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
//		Trazas.getTrazas2().add(Main.Main.carpetaTrazas+"/trazas-"+parametrosAlg.getFuncionFitnessFase2()
//		+"-Fit"+n+"_"+dateFormat.format(new Date())+".csv");
//		Trazas.getTrazas2().add(trazaFit);
//	
//		Traza traza = new Traza(individuo.getTurnos().size(),n,trazas, (t2-t1));
//		Trazas.addTraza(traza);
//		trazas = new ArrayList<ArrayList<String>>();
        /*Trazas5*/
        return bestSol;
    }

}

