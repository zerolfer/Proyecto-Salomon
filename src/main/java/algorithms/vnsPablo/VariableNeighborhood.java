/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.vnsPablo;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import patrones.Restricciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author slimbook
 */
public class VariableNeighborhood extends Thread {

    /**
     * @param args the command line arguments
     */
    private static Solucion s;
    private static int explored = 0;
    private static int nlSize = 0;

    public static Solucion nVariableNeighborhood(Solucion solIni, patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {

        double globalTotalFitnes = 0;
        boolean stopCondition = false;
        //int neighborhood=0;
        double totalFitnes = 0;
        int cont = 0;
        Solucion solFin = (Solucion) solIni.clone();
        double[] fit;
        int size = 3;
        boolean fase2 = false;
        long time = 0;
        int iterations = 0;

        fit = DeciderFitnessFunction.switchFitnessF(solFin, patrones, entrada, parametros, parametrosAlg);
        //double a=Restricciones.penalizacionPorRestriccionesSinTiempos(  solFin, patrones, entrada, parametros);

        globalTotalFitnes = fit[0];
        /*for (Controlador contr : solIni.getControladores() ){
            System.out.println(contr.getTurno());
        }*/
        //printTurnos(solFin);
        int n[] = {0, 1, 2, 3};
        NeighborhoodHandler neighborhood = new NeighborhoodHandler(n);
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        long duration = 0;
  /*  	Traces t = null;
		try {
			t = new Traces();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
*/
        do {
            explored = 0;
            iterations++;
            //solFin = shakingNeighborhood(solIni,neighborhood);
            List<Solucion> neighbourhoodList = Collections.synchronizedList(new ArrayList<Solucion>());
            Solucion solIniCopy = (Solucion) solFin.clone();
            //System.out.println("Vecindad "+ neighborhood.getNeigborhood()+ " Tamaño "+ neighborhood.getSize());
            neighbourhoodList = fillNeighborhoodThreads(neighborhood.wide, solFin, neighborhood.getSize(), neighborhood.getNeigborhood(), patrones, entrada, parametros, parametrosAlg);
            //System.out.println("Tamaño de la vecindad "+ neighbourhoodList.size());

            if (neighbourhoodList.size() < 5) {
                neighborhood.nextNeighborhood();
                continue;
            }
            solFin = localSearchNeighborhoods(solFin, neighborhood.getSize(), neighborhood.getNeigborhood(), neighbourhoodList, patrones, entrada, parametros, parametrosAlg);
            //
            /*if (parametrosAlg.getFuncionFitnessFase2().equals("fitGolbal") == false) 
            {
            	
            }*/
            //printTurnos(solFin);
            fit = DeciderFitnessFunction.switchFitnessF(solFin, patrones, entrada, parametros, parametrosAlg);

            totalFitnes = fit[0];
            double restriccionesIncumplidas = Restricciones.comprobarRestriccionesOptimizado(solFin, patrones, entrada, parametros);
            if (restriccionesIncumplidas == 0 && parametrosAlg.getFuncionFitnessFase2().equals("reduccionControladoresYRestricciones")) {
                endTime = System.currentTimeMillis();

                duration = (endTime - startTime);
                System.out.println("Eureka");
                System.out.println(solFin.getTurnos());
  /*              try {
  					t.TraceFileCsv(parametrosAlg.getFuncionFitnessFase2(),iterations,duration,totalFitnes, neighborhood.getNeigborhood(),neighborhood.laps, nlSize,explored);
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
   */
                parametrosAlg.setFuncionFitnessFase2("fitGlobal");
                globalTotalFitnes = 0;
                continue;
                //return solFin;
            }
            /*if ((Restricciones.penalizacionPorRestriccionesSinTiempos(  solFin, patrones, entrada, parametros))==0 )
            {
                System.out.println("Encontrada otra factible." );
                
            }*/
            //double restriccionesIncumplidas = Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros);

            endTime = System.currentTimeMillis();

            duration = (endTime - startTime);
            neighbourhoodList.clear();
            if (totalFitnes > globalTotalFitnes) {
                //if (neighborhood.laps>0 && (globalTotalFitnes-totalFitnes<0.001))return null;

                globalTotalFitnes = totalFitnes;

                if (solFin.getTurnos().size() == solFin.getControladores().size()) {
                    int n2[] = {3, 0, 2, 1};
                    neighborhood = new NeighborhoodHandler(n2);
                }


   /*             try {
  					t.TraceFileCsv(parametrosAlg.getFuncionFitnessFase2(),iterations,duration,totalFitnes, neighborhood.getNeigborhood(),neighborhood.laps, nlSize,explored);
				} catch (IOException e) {
					e.printStackTrace();
				}
    */
                System.out.println("New global fitnes in localsearch: [" + globalTotalFitnes + "] con la vecindad [" + neighborhood.getNeigborhood() + "] at iteration [" + iterations + "]");
                System.out.println("Turns size [" + solFin.getTurnos().size() + "]");
                neighborhood.resetNeighborhood();

            } else {

                if (neighborhood.laps > 3) {
                    if (restriccionesIncumplidas == 0) {
                        return solFin;
                    } else {
                        return solFin;
                    }
                }
                neighborhood.nextNeighborhood();
                solFin = (Solucion) solIniCopy.clone();
            }
            	/*//printTurnos(solFin);
                if (neighborhood!=2)
                {
                    System.out.println("Vecindario: ["+neighborhood+"]. Global fitnes : ["+globalTotalFitnes+"]" );

                	neighborhood++;
                }
                else
                {
                	//printTurnos(solFin);
                    if((Restricciones.penalizacionPorRestriccionesSinTiempos(  solFin, patrones, entrada, parametros))==0) 
                    {
                    	neighborhood=0;
                    }
                    
                }
                solFin=null;
                solFin= (Solucion)solIniCopy.clone(); 
                //System.out.println( "Size: ["+size+"], i: ["+i+"]");
*/
        }

        while (true);

        //for (Controlador contr : solFin.getControladores() ){
        //  System.out.println(contr.getTurno());


    }

    public static void determineNeighborhoods(Solucion solIni) {
        System.out.println(solIni.getControladores().size());
        int size = solIni.getControladores().size();
        for (int i = 0; i < size; i++) {
            solIni.getControladores().get(i).setTurno(solIni.getTurnos().get(i));

        }
        for (int i = 0; i < size; i++) {
            //System.out.println(solIni.getControladores().get(i).getTurno());            
        }
    }

    public static List<Solucion> fillNeighborhoodThreads(int wide, Solucion solIni, int size, int neighboorhood, patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {
        Solucion solFin = (Solucion) solIni.clone();
        Solucion solIniCopia = (Solucion) solIni.clone();
        List<Solucion> neighbourhoodList = Collections.synchronizedList(new ArrayList<Solucion>());
        String firstCandidate;
        String secondCandidate;
        int explorationSize = 15;


        switch (neighboorhood) {
            case 0:
            case 3:
            case 1:
            case 2:

                //System.out.println("Evaluatng neigbourhood");
                neighbourhoodList.clear();
                int turnSize = solFin.getTurnos().size();
                ArrayList<Integer> choice = new ArrayList<>();
                if (turnSize > solFin.getControladores().size()) {
                    ArrayList<Integer> ramp = new ArrayList<>();
                    for (int i = 0; i < turnSize; i++) {
                        for (int i2 = 0; i2 < turnSize - i; i2++) ramp.add(i);
                    }
                    Random rand = new Random();
                    for (int i = 0; i < explorationSize; i++) choice.add(ramp.get(rand.nextInt(ramp.size())));
                } else {
                    for (int i = 0; i < turnSize; i++) choice.add(i);
                }
                //ExecutorService threadPool = Executors.newFixedThreadPool();

                ExecutorService threadPool = Executors.newFixedThreadPool(choice.size());
                for (int i = 0; i < choice.size(); i++)//nTurno < solFin.getTurnos().size();
                {
                    threadPool.submit(new FillNeigbourhoodListTask(neighboorhood, solIni, choice.get(i), neighbourhoodList, size, patrones, entrada, parametros, parametrosAlg));
                }
                threadPool.shutdown();
                while (!threadPool.isTerminated()) {
                }
                neighbourhoodList.removeAll(Collections.singleton(null));


                break;

            case 5:
                for (Controlador ctrl : solFin.getControladores()) {
                    for (Controlador ctrl2 : solFin.getControladores()) {
                        if (ctrl.hashCode() == ctrl2.hashCode()) continue;
                        Controlador aux = ctrl2;
                        ctrl2 = ctrl;
                        ctrl = aux;
                        //fillTurnsWithControllers(solFin);

                        double restriccionesIncumplidas = Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros);
                        if (restriccionesIncumplidas == 0) neighbourhoodList.add((Solucion) solFin.clone());

                        solFin = (Solucion) solIni.clone();
                    }
                }
                break;
            case 20:
                //printTurnos(solFin);
                try {
                    for (String turno : solFin.getTurnos()) {
                        //< For each block of the turn 
                        for (int i = 0; i < turno.length(); i = i + 3) {

                            // [FIRST CANDIDATE SELECTION] Get first candidate String. It can't contain '1' and all elements must be equals
                            firstCandidate = turno.substring(i, i + size);
                            //System.err.println(candidate);
                            if (firstCandidate.indexOf('1') != -1) continue;
                            //END OF [FIRST CANDIDATE SELECTION]

                            // For each block in the same string.
                            for (int i2 = 0; i2 < turno.length(); i2 = i2 + 3) {
                                // We don't want the new indez to be within the first index    
                                if ((i2 < i)
                                        || (i2 > i + size)
                                ) {

                                    // [SECOND CANDIDATE SELECTION] Get second candidate String. It must be all '1'.
                                    secondCandidate = turno.substring(i2, i2 + size);
                                    ///< If candidate block i'ts STRING_DESCANSO, look for the next block
                                    String candidate = "";
                                    for (int i3 = 0; i3 < size; i3++) {
                                        candidate = candidate.concat("1");
                                    }
                                    if (!secondCandidate.equals(candidate)) continue;
                                    // END OF [SECOND CANDIDATE SELECTION]


                                    try {
                                        if (
                                                (firstCandidate.substring(0, 3).equals(turno.substring(i2 - size, i2)))
                                                        || (firstCandidate.substring(size - 3, size).equals(turno.substring(i2 + size, i2 + (2 * size))))
                                        ) {
                                            for (String turno2 : solFin.getTurnos()) {

                                                if ((firstCandidate.equals(turno2.substring(i2, i2 + size)))
                                                        && (secondCandidate.equals(turno2.substring(i, i + size)))
                                                ) {

                                                    //System.out.println(turno);
                                                    //	System.out.println(turno2);
                                                    String aux = turno.substring(0, i) + secondCandidate + turno.substring(i + size, i2) + firstCandidate + turno.substring(i2 + size);
                                                    String aux2 = turno2.substring(0, i) + firstCandidate + turno2.substring(i + size, i2) + secondCandidate + turno2.substring(i2 + size);
                                                    turno = aux;
                                                    turno2 = aux2;
                                                    aux = null;
                                                    aux2 = null;

                                                    neighbourhoodList.add((Solucion) solFin.clone());

                                                    solFin = (Solucion) solIni.clone();

                                                }
                                            }
                                        }
                                    } catch (StringIndexOutOfBoundsException exception) {
                                        //System.err.println("Out of bounds exception 1");
                                        solFin = (Solucion) solIni.clone();
                                        continue;
                                    }

                                    //System.out.println( "Global fitnes in localsearch: ["+globalTotalFitnes+"]");
                                }
                            }
                        }
                    }
                } catch (StringIndexOutOfBoundsException excepcion) {
                    solFin = (Solucion) solIni.clone();
                    //System.err.println("Out of bounds exception 2");
                }

                break;

            default:
                break;
        }

        return neighbourhoodList;
    }

    public static ArrayList<Solucion> fillNeighborhood(Solucion solIni, int size, int neighboorhood, patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {
        Solucion solFin = (Solucion) solIni.clone();
        Solucion solIniCopia = (Solucion) solIni.clone();
        ArrayList<Solucion> neighbourhoodList = new ArrayList<>();
        String firstCandidate;
        String secondCandidate;


        switch (neighboorhood) {
            case 0:

                //System.out.println("Evaluatng neigbourhood");
                for (Controlador ctrl : solFin.getControladores()) {
                    try {
                        //< For each block of the turn 
                        for (int i = 0; i < ctrl.getTurno().length(); i = i + 3) {
                            firstCandidate = ctrl.getTurno().substring(i, i + size);
                            ///< If candidate block i'ts STRING_DESCANSO, look for the next block
                            String candidate = "";
                            for (int i2 = 0; i2 < size; i2++) {
                                candidate = candidate.concat("1");
                            }
                            //System.err.println(candidate);
                            if (firstCandidate.equals(candidate)) continue;

                            int hash = ctrl.hashCode();
                            for (Controlador ctrl2 : solFin.getControladores()) {

                                if (hash != ctrl2.hashCode()) {
                                    secondCandidate = ctrl2.getTurno().substring(i, i + size);
                                    if (!secondCandidate.equals(candidate)) continue;
                                    try {
                                        if (
                                                ((firstCandidate.substring(0, 3).equals(ctrl2.getTurno().substring(i - 3, i)))
                                                        || (firstCandidate.substring(size - 3, size).equals(ctrl2.getTurno().substring(i + size, i + size + 3)))
                                                )
                                                        && (!firstCandidate.equals(secondCandidate))
                                        ) {
                                            String aux2 = ctrl2.getTurno().substring(0, i) + firstCandidate + ctrl2.getTurno().substring(i + size);
                                            String aux = ctrl.getTurno().substring(0, i) + secondCandidate + ctrl.getTurno().substring(i + size);
                                            ctrl.setTurno(aux);
                                            ctrl2.setTurno(aux2);
                                            aux = null;
                                            aux2 = null;
                                            fillTurnsWithControllers(solFin);

                                            double restriccionesIncumplidas = Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros);

                                            if (restriccionesIncumplidas == 0)
                                                neighbourhoodList.add((Solucion) solFin.clone());

                                            solFin = (Solucion) solIni.clone();

                                        }
                                    } catch (StringIndexOutOfBoundsException exception) {
                                        //System.err.println("Out of bounds exception 1");
                                        solFin = (Solucion) solIni.clone();
                                        continue;
                                    }

                                    //System.out.println( "Global fitnes in localsearch: ["+globalTotalFitnes+"]");
                                }
                            }
                        }
                    } catch (StringIndexOutOfBoundsException excepcion) {
                        solFin = (Solucion) solIni.clone();
                        //System.err.println("Out of bounds exception 2");
                    }
                }
                break;
            case 2:
                //printTurnos(solFin);
                try {
                    for (Controlador ctrl : solFin.getControladores()) {
                        //< For each block of the turn 
                        for (int i = 0; i < ctrl.getTurno().length(); i = i + 3) {

                            // [FIRST CANDIDATE SELECTION] Get first candidate String. It can't contain '1' and all elements must be equals
                            firstCandidate = ctrl.getTurno().substring(i, i + size);
                            //System.err.println(candidate);
                            if (firstCandidate.indexOf('1') != -1) continue;
                            //END OF [FIRST CANDIDATE SELECTION]

                            // For each block in the same string.
                            for (int i2 = 0; i2 < ctrl.getTurno().length(); i2 = i2 + 3) {
                                // We don't want the new indez to be within the first index    
                                if ((i2 < i)
                                        || (i2 > i + size)
                                ) {

                                    // [SECOND CANDIDATE SELECTION] Get second candidate String. It must be all '1'.
                                    secondCandidate = ctrl.getTurno().substring(i2, i2 + size);
                                    ///< If candidate block i'ts STRING_DESCANSO, look for the next block
                                    String candidate = "";
                                    for (int i3 = 0; i3 < size; i3++) {
                                        candidate = candidate.concat("1");
                                    }
                                    if (!secondCandidate.equals(candidate)) continue;
                                    // END OF [SECOND CANDIDATE SELECTION]

                                    int hash = ctrl.hashCode();

                                    try {
                                        if (
                                                (firstCandidate.substring(0, 3).equals(ctrl.getTurno().substring(i2 - size, i2)))
                                                        || (firstCandidate.substring(size - 3, size).equals(ctrl.getTurno().substring(i2 + size, i2 + (2 * size))))
                                        ) {
                                            for (Controlador ctrl2 : solFin.getControladores()) {
                                                if (hash == ctrl2.hashCode()) continue;
                                                if ((firstCandidate.equals(ctrl2.getTurno().substring(i2, i2 + size)))
                                                        && (secondCandidate.equals(ctrl2.getTurno().substring(i, i + size)))
                                                ) {

                                                    //System.out.println(ctrl.getTurno());
                                                    //System.out.println(ctrl2.getTurno());
                                                    String aux = ctrl.getTurno().substring(0, i) + secondCandidate + ctrl.getTurno().substring(i + size, i2) + firstCandidate + ctrl.getTurno().substring(i2 + size);
                                                    String aux2 = ctrl2.getTurno().substring(0, i) + firstCandidate + ctrl2.getTurno().substring(i + size, i2) + secondCandidate + ctrl2.getTurno().substring(i2 + size);
                                                    ctrl.setTurno(aux);
                                                    ctrl2.setTurno(aux2);
                                                    aux = null;
                                                    aux2 = null;
                                                    fillTurnsWithControllers(solFin);
                                                    double restriccionesIncumplidas = Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros);

                                                    if (restriccionesIncumplidas == 0)
                                                        neighbourhoodList.add((Solucion) solFin.clone());

                                                    solFin = (Solucion) solIni.clone();

                                                }
                                            }
                                        }
                                    } catch (StringIndexOutOfBoundsException exception) {
                                        //System.err.println("Out of bounds exception 1");
                                        solFin = (Solucion) solIni.clone();
                                        continue;
                                    }

                                    //System.out.println( "Global fitnes in localsearch: ["+globalTotalFitnes+"]");
                                }
                            }
                        }
                    }
                } catch (StringIndexOutOfBoundsException excepcion) {
                    solFin = (Solucion) solIni.clone();
                    //System.err.println("Out of bounds exception 2");
                }

                break;
            default:
                break;
        }

        return neighbourhoodList;
    }


    public static Solucion localSearchNeighborhoods(Solucion solIni, int size, int neighboorhood, List<Solucion> neighbourhoodList, patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {
        Solucion solFin = (Solucion) solIni.clone();
        Solucion solBest = (Solucion) solIni.clone();
        Solucion solIniCopia = (Solucion) solIni.clone();
        Controlador randController = null;
        Controlador randController2 = null;
        double globalTotalFitnes = 0;
        double localMax = 0;
        double[] fit;
        ArrayList<Solucion> Factibles = new ArrayList<>();

        fit = DeciderFitnessFunction.switchFitnessF(solFin, patrones, entrada, parametros, parametrosAlg);
        double initialFitness = fit[0];
        double totalFitnes = fit[0];
        globalTotalFitnes = totalFitnes;
        int choice;
        int choice2;
        boolean completed = false;


        String firstCandidate;
        String secondCandidate;
        nlSize = neighbourhoodList.size();
        boolean condicionParada = false;
        int tries = 0;
        int maxTries = nlSize / 10;
        double aceptacion = 0.005;
        int option = 1;


        Random rand = new Random();
        int xPrime = 0;
        if (maxTries > 0) {
            xPrime = rand.nextInt((int) (nlSize * ((double) 1 / maxTries))) + (int) (nlSize * ((double) tries / maxTries));
        } else {
            xPrime = rand.nextInt(nlSize);
        }
        double[] fitness = new double[nlSize];
        Solucion candidate1;

        switch (option) {

            case 1:
                maxTries = 2;
                int dir = 1;
                int repeated = 0;
                try {

                    candidate1 = neighbourhoodList.get(xPrime);
                } catch (NullPointerException e) {
                    neighbourhoodList.removeAll(Collections.singleton(null));
                    xPrime = rand.nextInt((int) (nlSize * ((double) 1 / maxTries))) + (tries) * (int) (nlSize * ((double) tries / maxTries));
                    dir = (rand.nextInt(2) != 0) ? 1 : -1;
                    candidate1 = neighbourhoodList.get(xPrime);
                }


                fit = DeciderFitnessFunction.switchFitnessF(candidate1, patrones, entrada, parametros, parametrosAlg);


                fitness[xPrime] = fit[0];
                int badVisited = 0;
                int minBadVisited = nlSize / 4;
                if (minBadVisited < 10) minBadVisited = nlSize / 10;


                do {

                    if (xPrime == 0 || xPrime == nlSize - 1) {
                        xPrime = rand.nextInt((int) (nlSize * ((double) 1 / maxTries))) + (int) (nlSize * ((double) tries / maxTries));
                        dir = (rand.nextInt(2) != 0) ? 1 : -1;
                        continue;
                    }

                    Solucion rightCandidate;

                    try {
                        rightCandidate = neighbourhoodList.get(xPrime + dir);
                    } catch (NullPointerException e) {
                        neighbourhoodList.removeAll(Collections.singleton(null));
                        tries++;
                        xPrime = rand.nextInt((int) (nlSize * ((double) 1 / maxTries))) + (int) (nlSize * ((double) tries / maxTries));
                        continue;
                    }
                    if (fitness[xPrime + dir] == 0 && rightCandidate != null) {
                        try {

                            if (rightCandidate.getTurnos().size() != rightCandidate.getControladores().size()) {
                                rightCandidate = MetaheuristicUtil.orderByLazyCriteria(rightCandidate);
                            }

                            if (rightCandidate.getControladores().size() != rightCandidate.getTurnos().size()) {
                                boolean descanso = true;
                                for (int i = 0; i < rightCandidate.getTurnos().get(0).length(); i++) {
                                    if (rightCandidate.getTurnos().get(0).charAt(i) != '1') {
                                        descanso = false;
                                        break;
                                    }
                                }

                                if (descanso) {
                                    ArrayList<Controlador> cs = new ArrayList<>();
                                    int turnoOwner = 0;
                                    for (int i = 0; i < rightCandidate.getControladores().size(); i++) {
                                        if (rightCandidate.getControladores().get(i).getTurnoAsignado() == 0) {
                                            turnoOwner = i;
                                            break;
                                        }
                                    }
                                    for (Controlador c : rightCandidate.getControladores()) {
                                        cs.add((Controlador) c.clone());
                                    }
                                    for (int i = 0; i < cs.size(); i++) {
                                        if (cs.get(i).getTurnoAsignado() == turnoOwner) {
                                            cs = asignarControlador(i, turnoOwner, cs);
                                            break;
                                        }
                                    }
                                    rightCandidate.getTurnos().remove(0);

                                    rightCandidate.setControladores(cs);
                                }
                            }
                            fit = DeciderFitnessFunction.switchFitnessF(rightCandidate, patrones, entrada, parametros, parametrosAlg);
                            explored++;

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        fitness[xPrime + dir] = fit[0];
                    } else repeated++;

                    int best = dir;


                    if (fitness[xPrime + best] > fitness[xPrime]) {
                        xPrime += best;

                        badVisited++;
                        if (fitness[xPrime] > globalTotalFitnes) {
                            solBest = neighbourhoodList.get(xPrime);
                            globalTotalFitnes = fitness[xPrime];
                            //tries=0;
                            badVisited = 0;


                            if (solBest.getTurnos().size() != solBest.getControladores().size()) {
                                solBest = MetaheuristicUtil.orderByLazyCriteria(solBest);
                            }

                            if (solBest.getControladores().size() != solBest.getTurnos().size()) {
                                boolean descanso = true;
                                for (int i = 0; i < solBest.getTurnos().get(0).length(); i++) {
                                    if (solBest.getTurnos().get(0).charAt(i) != '1') {
                                        descanso = false;
                                        break;
                                    }
                                }

                                if (descanso) {
                                    ArrayList<Controlador> cs = new ArrayList<>();
                                    int turnoOwner = 0;
                                    for (int i = 0; i < solBest.getControladores().size(); i++) {
                                        if (solBest.getControladores().get(i).getTurnoAsignado() == 0) {
                                            turnoOwner = i;
                                            break;
                                        }
                                    }
                                    for (Controlador c : solBest.getControladores()) {
                                        cs.add((Controlador) c.clone());
                                    }
                                    for (int i = 0; i < cs.size(); i++) {
                                        if (cs.get(i).getTurnoAsignado() == turnoOwner) {
                                            cs = asignarControlador(i, turnoOwner, cs);
                                            break;
                                        }
                                    }
                                    solBest.getTurnos().remove(0);

                                    solBest.setControladores(cs);


                                }
                            }
                        }
                    } else {
                        if (badVisited > minBadVisited) {
                            tries++;
                            if (maxTries > 0) {
                                int c = ((int) (nlSize * ((double) 1 / maxTries)) != 0) ? (int) (nlSize * ((double) 1 / maxTries)) : 1;
                                xPrime = rand.nextInt(c) + (int) (nlSize * ((double) tries / maxTries));
                                badVisited = 0;
                            } else {
                                xPrime = rand.nextInt(nlSize);
                            }
                            dir = (rand.nextInt(2) != 1) ? 1 : -1;
                        } else {
                            xPrime++;
                            badVisited++;
                        }
                    }

                }
                while (tries < maxTries);
                //System.out.println("------------");
				/*for(double x: fitness)
				{
					if (x!=0.0)
					{
						//System.out.print("X");
						explored++;
					}
					//else System.out.print("_");
				}*/
                /*System.out.println("------------");*/
				/*System.out.println("Size of neighbourhood: " + nlSize );
				System.out.println("Explored the "+(double)explored/fitness.length+" percent.");
				System.out.println("Repeated: "+repeated);*/

                break;
        }
        return solBest;

    }

    /*public static Solucion shakingNeighborhood(Solucion solIni, int neighborhood,patrones.Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg)
    {
        Solucion solFin = (Solucion) solIni.clone();
        int tries=0;
        Random rand = new Random();

        switch (neighborhood){
            case 0:
                String firstCandidate;
                String secondCandidate;
                Controlador randController =   null;
                Controlador randController2 = null;
                int choice;
                int choice2;
                boolean completed = false;
                int maxTries = 50; 
                int size = rand.nextInt(solFin.getControladores().size()/2);
                do{
                    do{
                        randController = solFin.getControladores().get(rand.nextInt(solFin.getControladores().size()));
                        //System.out.println("Length: "+randController.getTurno().length());
                        choice=(((rand.nextInt((randController.getTurno().length()-9)/3))*3)+6);
                        //System.out.println("Choice: "+choice);
                        firstCandidate= randController.getTurno().substring(choice, choice+3);
                        //System.out.println("First candidate:" + firstCandidate);
                    }
                    while (    (STRING_DESCANSO.equals(firstCandidate) )
                            && (  (firstCandidate.equals(randController.getTurno().substring(choice-3, choice)))
                                &&(firstCandidate.equals(randController.getTurno().substring(choice+3, choice+6)))
                               )
                    		
                          );
                    //System.out.println(firstCandidate);
                    for (Controlador randCont : solFin.getControladores() ){
                        secondCandidate = randCont.getTurno().substring(choice, choice+3);
                        //System.out.println("Choice: " + choice);

                        if (   (firstCandidate.equals(randCont.getTurno().substring(choice-3, choice)) || firstCandidate.equals(randCont.getTurno().substring(choice+3, choice+6)) ) && (STRING_DESCANSO.equals(secondCandidate)))
                        {
                        	//System.out.println("Seccond candidate:" + secondCandidate);
                            //System.out.println("Previous Seccond candidate:" + randCont.getTurno().substring(choice-3, choice));
                            String aux2 = randCont.getTurno().substring(0, choice) + firstCandidate + randCont.getTurno().substring(choice +3);
                            String aux = randController.getTurno().substring(0, choice) + secondCandidate + randController.getTurno().substring(choice +3);
                            randController.setTurno(aux);
                            randCont.setTurno(aux2);
                            completed = true;
                            randController2=randCont;
                            //System.out.println("Cambiado en "+ firstCandidate + " por " + secondCandidate + " en " + choice);
                            if (     
                                    ((Restricciones.penalizacionPorRestriccionesSinTiempos(solFin, patrones, entrada, parametros))!=0)
                              )
                           {
                            	System.out.println("Encontrada otra factible." );
                           	completed=true;
                           	break;
                           }
                           else
                           {
                               aux2 = randCont.getTurno().substring(0, choice) + secondCandidate + randCont.getTurno().substring(choice +3);
                               aux = randController.getTurno().substring(0, choice) + firstCandidate + randController.getTurno().substring(choice +3);
                               randController.setTurno(aux);
                               randCont.setTurno(aux2);
                               
                           }
                            
                        }
                    }
                    

                    firstCandidate="";
                    secondCandidate="";
                    maxTries--;
                }
                while (!completed & maxTries>0);
                if(maxTries==0) solFin=solIni;
                
                do{
                    randController2 = solFin.getControladores().get(rand.nextInt(solFin.getControladores().size()));
                    choice2 = rand.nextInt(randController2.getTurno().length()-1)+1;
                    secondCandidate = randController2.getTurno().charAt((choice));
                    tries++;
                    System.out.println("Intentos: "+ tries);
                }
                while (    (secondCandidate != '1' ) 
                        || ( randController2.getTurno().charAt((choice - 1)) != firstCandidate )
                      );
                char a[]=randController.getTurno().toCharArray();
                a[choice]=
                randController.setTurno(randController.getTurno.substring(0,choice)+secondCandidate+myName.substring(5)); = myName.substring(0,4)+'x'+myName.substring(5);
                
                
                System.out.println("Lo Conseguí");
                break;
            case 1:
                randController = solFin.getControladores().get(rand.nextInt(solFin.getControladores().size()));
                        //System.out.println("Length: "+randController.getTurno().length());
                choice=(((rand.nextInt((randController.getTurno().length()-3)/3))*3)+3);
                        //System.out.println("Choice: "+choice);
                choice2= (((rand.nextInt((randController.getTurno().length()-3)/3))*3)+3);;
          //              System.out.println("Choice1: "+choice);
       // System.out.println("Choice2: "+choice2);
        if(choice==choice2)break;
                if(choice<choice2)changePosition(randController, choice, choice2);
                else changePosition(randController, choice2, choice);
                break;
            default:
                break;
        
        }   
        return solFin;
    }*/
    public static void changePosition(Controlador ctrl, int posIni, int posFin) {
        String s = ctrl.getTurno();

        // System.out.println(s);

        String aux = s.substring(0, posIni) + s.substring(posFin, posFin + 3) + s.substring(posIni + 3, posFin) + s.substring(posIni, posIni + 3) + s.substring(posFin + 3);
        /*ArrayList<String> turnos = new ArrayList<>();
        for (int i=0; i<s.length();i=i+3)
            turnos.add(s.substring(i-2,i+1));
        String aux=turnos.get(posIni);
        turnos.set(posIni,turnos.get(posFin));
        turnos.set(posFin, aux);
        String total="";
        for (int i=0;i<turnos.size();i++){
            total=total.concat(turnos.get(i));
        }*/
        //System.out.println(aux);
        ctrl.setTurno(aux);
    }

    public static void fillTurnsWithControllers(Solucion solIni) {
        int size = solIni.getControladores().size();
        ArrayList<String> n = new ArrayList<>();
        for (int a = 0; a < size; a++) {
            n.add(solIni.getControladores().get(a).getTurno());

        }
        solIni.setTurnos(n);
    }

    public static void evaluateSolution(Solucion solIni) {
        ;
    }


    public static void printTurnos(Solucion solIni) {
        for (Controlador contr : solIni.getControladores()) {
            System.out.println(contr.getTurno());
        }
    }
    
    
   /* public static void evaluate1Neighbourhood(int dist, ArrayList<Controlador> ctrls, Parametros param){

        ArrayList<Controlador> ctrls2 = (ArrayList<Controlador>)ctrls.clone();
        int iNumCtrls = ctrls.get(0).getTurno().length() /3;
        for(int i=0; i< ctrls.size();i++){
            int items=ctrls2.get(i).getTurno().length()/3;
            System.out.print(items);
            for(int i2=0;i2<items-1; i2++){
                for(int i3=0;i3<items-1; i3++){
                    changePosition(ctrls2.get(i),i2,i3);
                    //System.out.println(String.format(ctrls2.get(i).getTurno()));
                    ArrayList<String> table = new ArrayList<>();
                    for(Controlador ctrl:ctrls)
                    {
                        table.add(ctrl.getTurno());
                    }
                    
                    System.out.println( CalcularFitness.tiempoOptTrabajoDescanso(21,iNumCtrls, table ,param) );
                    changePosition(ctrls2.get(i),i3,i2);
                    //aux(i,i2,i3,ctrls2);
                    
                }
            }
        }
    }*/
/*    public static void aux(int i, int i2, int i3, ArrayList<Controlador> ctrls2){
                    changePosition(ctrls2.get(i),i2,i3);
                    System.out.println(String.format(ctrls2.get(i).getTurno()));
                    changePosition(ctrls2.get(i),i3,i2);
    
    }*/
    /*public static void changePosition(Controlador ctrl, int posIni, int posFin)
    {
        String s=  ctrl.getTurno();
        ArrayList<String> turnos = new ArrayList<>();
        for (int i=2; i<s.length();i=i+3)
            turnos.add(s.substring(i-2,i+1));
        String aux=turnos.get(posIni);
        turnos.set(posIni,turnos.get(posFin));
        turnos.set(posFin, aux);
        String total="";
        for (int i=0;i<turnos.size();i++){
            total=total.concat(turnos.get(i));
        }
        ctrl.setTurno(total);
    }*/


    private static ArrayList<Controlador> asignarControlador(int posC, int dador, ArrayList<Controlador> cs) {
        for (int i = 0; i < cs.size(); i++) {
            if (cs.get(i).getTurnoAsignado() > dador) {
                cs.get(i).setTurnoAsignado(cs.get(i).getTurnoAsignado() - 1);
            }
        }
        ArrayList<Integer> aux = new ArrayList<>();
        for (int i = 0; i < cs.size(); i++) {
            aux.add(cs.get(i).getTurnoAsignado());
        }
        for (int j = 0; j < cs.size(); j++) {
            boolean esta = false;
            for (int i = 0; i < aux.size(); i++) {
                if (aux.get(i) == j) {
                    esta = true;
                }
            }
            if (!esta) {
                cs.get(posC).setTurnoAsignado(j);
                break;
            }
        }

        return cs;
    }


}




