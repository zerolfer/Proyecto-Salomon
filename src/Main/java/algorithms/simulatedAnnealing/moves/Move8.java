package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Movimiento;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;
import java.util.HashSet;

public class Move8 {

    private static int TurnoAleatorio(ArrayList<Integer> contc, ArrayList<String> individuo) {
        int c = (int) (Math.floor(Math.random() * ((individuo.size() - 1) - 0 + 1)) + 0);
        boolean noEsta = true;
        while (noEsta) {
            noEsta = false;
            for (int w = 0; w < contc.size(); w++) {
                if (contc.get(w) == c) {
                    c = (int) (Math.floor(Math.random() * ((individuo.size() - 1) - 0 + 1)) + 0);
                    noEsta = true;
                    break;
                }
            }
        }
        return c;
    }

    private static ArrayList<ArrayList<Integer>> GetIntervalos(String trabajador) {
        ArrayList<ArrayList<Integer>> intervalo = new ArrayList<>();
        ArrayList<Integer> inicio = new ArrayList<>();
        ArrayList<Integer> fin = new ArrayList<>();
        for (int i = 0; i < trabajador.length(); i += 3) {
            if (!trabajador.substring(i, i + 3).equals("111")) {
                if (i == 0 || (trabajador.substring(i - 3, i).equals("111"))) {
                    inicio.add(i);
                }
            }
        }
        for (int i = trabajador.length() - 3; i >= 0; i -= 3) {
            if (!trabajador.substring(i, i + 3).equals("111")) {
                if (i == trabajador.length() - 3 || (trabajador.substring(i + 3, i + 6).equals("111"))) {
                    fin.add(i + 3);
                }
            }
        }
        for (int i = 0; i < inicio.size(); i++) {
            ArrayList<Integer> pos = new ArrayList<>();
            pos.add(inicio.get(i));
            int f = fin.get(0);
            for (int e = 0; e < fin.size(); e++) {
                if (fin.get(e) < f && inicio.get(i) < fin.get(e)) {
                    f = fin.get(e);
                }
            }
            pos.add(f);
            intervalo.add(pos);
        }
        return intervalo;
    }

    private static Solucion DoChange1(ArrayList<String> dosInd, Solucion individuo, Movimiento mov, ParametrosAlgoritmo_SA pa) {
        /*
         * Realiza el transpaso de trabajo entre dos controladores,
         * y comprueba que el controlador que entrega la carga de trabajo, tenga algo mas (no sea todo ceros), si no se elimina
         */
        @SuppressWarnings("unchecked")
        ArrayList<String> turnos = (ArrayList<String>) individuo.getTurnos().clone();
        turnos.set(mov.getDador(), dosInd.get(0));
        turnos.set(mov.getReceptor(), dosInd.get(1));
        if (individuo.getControladores().size() != individuo.getTurnos().size()) {
            boolean descanso = true;
            for (int i = 0; i < dosInd.get(0).length(); i++) {
                if (dosInd.get(0).charAt(i) != '1') {
                    descanso = false;
                }
            }
            ArrayList<Controlador> cs = new ArrayList<>();
            for (Controlador c : individuo.getControladores()) {
                cs.add((Controlador) c.clone());
            }
            if (descanso) {
                for (int i = 0; i < cs.size(); i++) {
                    if (cs.get(i).getTurnoAsignado() == mov.getDador()) {
                        cs = asignarControlador(i, mov.getDador(), cs);
                        break;
                    }
                }
                turnos.remove(mov.getDador());
                Solucion individuo2 = (Solucion) individuo.clone();
                individuo2.setControladores(cs);
                individuo2.setTurnos(turnos);
                return individuo2;
            }
        }
        Solucion individuo2 = (Solucion) individuo.clone();
        individuo2.setTurnos(turnos);
        return individuo2;
    }

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

    private static ArrayList<String> ChangeCont(Solucion ind, Movimiento mov, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg) {
        /*
         * Comprueba que se pueda hacer un transpaso de trabajo entre dos controladores,
         * si es posible devuelve los dos individuos con el cambio ya hecho, si no devuelve null
         */

        ArrayList<String> individuo = ind.getTurnos();
        String controladorVago = individuo.get(mov.getDador());
        String controladorP = individuo.get(mov.getReceptor());
        String cadena = controladorVago.substring(mov.getInicio(), mov.getFin());
        String cadenaP = controladorP.substring(mov.getInicio(), mov.getFin());
        boolean t = false;// comprobarLibre(individuo, mov, cadena);
        //boolean m = comprobarNucleos(cadena, controladorP, patrones);
        //boolean m1 = comprobarNucleos(cadenaP, controladorVago, patrones);
        boolean m = comprobarNucleos1(entrada.getNucleos(), ind, mov, patrones);
        //boolean m = comprobarNucleos2(cadena, controladorP, entrada.getNucleos());
        //boolean m1 = comprobarNucleos2(cadenaP, controladorVago, entrada.getNucleos());


        if (t || !m) {
            return null;
        }

        String controladorVago2 = controladorVago.substring(0, mov.getInicio()) + cadenaP + controladorVago.substring(mov.getFin(), controladorVago.length());
        String controladorP2 = controladorP.substring(0, mov.getInicio()) + cadena + controladorP.substring(mov.getFin(), controladorP.length());

        ArrayList<String> dosInd = new ArrayList<>();
        dosInd.add(controladorVago2);
        dosInd.add(controladorP2);

        if (!parametrosAlg.getFuncionFitnessFase2().equalsIgnoreCase("reduccionControladoresYRestricciones")) {
            Solucion ind2 = DoChange1(dosInd, ind, mov, parametrosAlg);
            if (Restricciones.penalizacionPorRestricciones(ind2, patrones, entrada, parametros) != 0) {
                return null;
            }
        }
        return dosInd;
    }

    private static boolean comprobarNucleos(String cad, String conP, Patrones patrones) {
        ArrayList<String> nuc = Patrones.nuc;
        String[] cadenas = {cad + conP};
        for (int i = 0; i < cadenas.length; i++) {
            boolean correcto = false;
            String cadena = cadenas[i];
            for (int j = 0; j < nuc.size(); j++) {
                String posibles[] = patrones.getArray()[5 + j].toString().split(";");
                boolean bn = true;
                for (int l = 0; l < cadena.length(); l += 3) {
                    if (!cadena.substring(l, l + 3).equalsIgnoreCase("111")) {
                        boolean sectorPos = false;
                        for (int k = 0; k < posibles.length; k++) {
                            if (cadena.substring(l, l + 3).equalsIgnoreCase(posibles[k])) {
                                sectorPos = true;
                                break;
                            }
                        }
                        if (!sectorPos) {
                            bn = false;
                            break;
                        }
                    }
                }
                if (bn) {
                    correcto = true;
                    break;
                }
            }
            if (!correcto) {
                return false;
            }
        }
        return true;
    }

    private static boolean comprobarNucleos1(ArrayList<Nucleo> nucleos, Solucion ind, Movimiento mov, Patrones patrones) {
        ArrayList<String> individuo = ind.getTurnos();
        String controladorVago = individuo.get(mov.getDador());
        String controladorP = individuo.get(mov.getReceptor());
        String cadena = controladorVago.substring(mov.getInicio(), mov.getFin());
        String cadenaP = controladorP.substring(mov.getInicio(), mov.getFin());
        String controladorVago2 = controladorVago.substring(0, mov.getInicio()) + cadenaP + controladorVago.substring(mov.getFin(), controladorVago.length());
        String controladorP2 = controladorP.substring(0, mov.getInicio()) + cadena + controladorP.substring(mov.getFin(), controladorP.length());

        int x = 0, y = 0;
        while (ind.getControladores().get(x).getTurnoAsignado() != mov.getDador()) {
            x++;
            if (x == ind.getControladores().size()) {
                if (comprobarNucleos(controladorVago2, "", patrones) && comprobarNucleos(controladorP2, "", patrones)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        while (ind.getControladores().get(y).getTurnoAsignado() != mov.getReceptor()) {
            y++;
            if (y == ind.getControladores().size()) {
                if (comprobarNucleos(controladorVago2, "", patrones) && comprobarNucleos(controladorP2, "", patrones)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        String nucC1 = ind.getControladores().get(x).getNucleo();
        String nucC2 = ind.getControladores().get(y).getNucleo();

        for (int i = 0; i < nucleos.size(); i++) {
            ArrayList<Sector> sectoresN = nucleos.get(i).getSectores();
            if (nucleos.get(i).getNombre().equalsIgnoreCase(nucC1)) {
                for (int j = 0; j < controladorVago2.length(); j += 3) {
                    if (!controladorVago2.substring(j, j + 3).equalsIgnoreCase("111")) {
                        boolean in = false;
                        for (int k = 0; k < sectoresN.size(); k++) {
                            if (!sectoresN.get(k).getId().equalsIgnoreCase(controladorVago2.substring(j, j + 3))) {
                                in = true;
                            }
                        }
                        if (!in) {
                            return false;
                        }
                    }
                }
            }
            if (nucleos.get(i).getNombre().equalsIgnoreCase(nucC2)) {
                for (int j = 0; j < controladorP2.length(); j += 3) {
                    if (!controladorP2.substring(j, j + 3).equalsIgnoreCase("111")) {
                        boolean in = false;
                        for (int k = 0; k < sectoresN.size(); k++) {
                            if (!sectoresN.get(k).getId().equalsIgnoreCase(controladorP2.substring(j, j + 3))) {
                                in = true;
                            }
                        }
                        if (!in) {
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    private static boolean comprobarNucleos2(String cad, String con, ArrayList<Nucleo> nucleos) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < cad.length(); i += 3) {
            ids.add(cad.substring(i, i + 3));
        }
        for (int i = 0; i < con.length(); i += 3) {
            ids.add(con.substring(i, i + 3));
        }
        ArrayList<Integer> nucMax = new ArrayList<>();
        ArrayList<String> idsUnicos = creaListaDeUnicos(ids);
        ArrayList<ArrayList<Integer>> numNuc = new ArrayList<>();
        for (int k = 0; k < idsUnicos.size(); k++) {
            ArrayList<Integer> aux = new ArrayList<>();
            for (int i = 0; i < nucleos.size(); i++) {

                ArrayList<Sector> sectores = nucleos.get(i).getSectores();
                for (int j = 0; j < sectores.size(); j++) {
                    if (sectores.get(j).getId().equalsIgnoreCase(idsUnicos.get(k))) {
                        aux.add(i);
                    }
                }
            }
            numNuc.add(aux);
        }
        nucMax = numNuc.get(0);
        for (int i = 0; i < numNuc.size(); i++) {
            for (int j = 0; j < nucMax.size(); j++) {
                boolean one = false;
                int n = -1;
                for (int k = 0; k < numNuc.get(i).size(); k++) {
                    n = numNuc.get(i).get(k);
                    if (nucMax.get(j) == n) {
                        one = true;
                    }
                }
                if (!one && n != -1) {
                    nucMax = removeNucUnique(n, nucMax);
                    j--;
                }
            }
            if (nucMax.size() == 0) {
                return false;
            }
        }

        return true;
    }

    private static ArrayList<Integer> removeNucUnique(int n, ArrayList<Integer> nucMax) {
        for (int i = 0; i < nucMax.size(); i++) {
            if (nucMax.get(i) == n) {
                nucMax.remove(i);
                break;
            }
        }
        return nucMax;
    }

    public static ArrayList<String> creaListaDeUnicos(ArrayList<String> contenido) {
        //Crea un Set a partir del List y elimina elementos repetidos
        HashSet<String> hashSet = new HashSet<>(contenido);
        //se limpia el List
        contenido = new ArrayList<>();
        //Convierte el Set en List
        contenido.addAll(hashSet);
        return contenido;
    }

    private static boolean comprobarLibre(ArrayList<String> individuo, Movimiento mov, String cadena) {
        /*
         * Comprueba que el controlador que recibe la carga de trabajo (en un tiempo determinado), tiene libre ese hueco.
         */
        String controladorP = individuo.get(mov.getReceptor());
        String rest = controladorP.substring(mov.getInicio(), mov.getFin());
        for (int i = 0; i < rest.length(); i++) {
            if (rest.charAt(i) != '1') {
                return true;
            }
        }
        return false;
    }

    public static Solucion movimientoTrabajoIntervalosPseudoAleatorios(Solucion individuo1, int granularidadMax, int granularidadMin, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA pa, ArrayList<String> iteracion) {
        /*
         * Busca crear un cambio de trabajo entre dos trabajadores:
         */
        /*Trazas7*/
        int trysIntervalo = 0, trysTrab1 = 0, trysTrab2 = 0, tmnIntervalo = 0;
        /*Trazas7*/
        ArrayList<String> matrizTurnos = individuo1.getTurnos();
        ArrayList<Integer> contcv = new ArrayList<>();
        boolean encontrado = false;
        Solucion individuo2 = (Solucion) individuo1.clone();
        while (contcv.size() < matrizTurnos.size() && !encontrado) {
            trysTrab1++;
            int cv = TurnoAleatorio(contcv, matrizTurnos);
            contcv.add(cv);
            int granularidad = granularidadMax;
            ArrayList<ArrayList<Integer>> intervalos = GetIntervalos(matrizTurnos.get(cv));
            if (intervalos.size() != 0) {
                ArrayList<Integer> pos = intervalos.get((int) (Math.floor(Math.random() * ((intervalos.size() - 1) - 0 + 1)) + 0));
                ArrayList<Integer> listG = new ArrayList<>();
                for (int i = 9; i <= granularidadMax; i += 9) {
                    listG.add(i);
                }
                while (!encontrado && listG.size() != 0) {
                    trysIntervalo++;
                    granularidad = listG.remove((int) (Math.floor(Math.random() * ((listG.size() - 1) - 0 + 1)) + 0));
                    //System.out.println(listG.size());
                    if (pos.get(1) >= pos.get(0) + granularidad) {
                        pos.set(1, pos.get(0) + granularidad);
                    } else {
                        granularidad = pos.get(1) - pos.get(0);
                    }
                    ArrayList<Integer> contcs = new ArrayList<>();
                    contcs.add(cv);
                    while (contcs.size() < matrizTurnos.size()) {
                        int cs = TurnoAleatorio(contcs, matrizTurnos);
                        contcs.add(cs);
                        if (cs != cv) {
                            trysTrab2++;
                            tmnIntervalo = (pos.get(1) - pos.get(0));
                            Movimiento mov = new Movimiento(cv, cs, pos.get(0), pos.get(1));
                            ArrayList<String> dosInd = ChangeCont(individuo1, mov, patrones, entrada, parametros, pa);//comprueba que sea factible (devuelve 2 string cambiados o null)
                            if (dosInd != null) {
                                individuo2 = DoChange1(dosInd, individuo1, mov, pa);
                                encontrado = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                //System.out.println("noIntervalos: "+matrizTurnos.get(cv));/*No hay intervalos porque no hay trabajo en el turno (puede pasar cuando se alcance el numero de recursos)*/
            }
        }
        /*Trazas6*/
        iteracion.add("" + (tmnIntervalo));//Intervalo
        iteracion.add("" + trysIntervalo);//IntentosIntervalos
        iteracion.add("" + trysTrab1);//IntentosC1
        iteracion.add("" + trysTrab2);//IntentosC2
        /*Trazas6*/
        return individuo2;
    }

    public static Solucion doblemovimiento(Solucion individuo1, int gMax, int gMin, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg, ArrayList<String> iteracion) {
        double porcent = parametrosAlg.getPorcentajeEleccionMov();
        double rand = Math.random();
        Solucion ind = null;
        if (porcent >= rand && (individuo1.getControladores().size() >= individuo1.getTurnos().size())) {
            //CRUCE
            ind = movimientoTrabajoCruceControladores(individuo1, patrones, entrada, parametros, parametrosAlg, iteracion);
        } else {
            ind = movimientoTrabajoIntervalosPseudoAleatorios(individuo1, gMax, gMin, patrones, entrada, parametros, parametrosAlg, iteracion);
        }
        return ind;
    }

    private static Solucion movimientoTrabajoCruceControladores(Solucion individuo1, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg, ArrayList<String> iteracion) {

        /*
         * Busca crear un cambio de trabajo entre dos trabajadores:
         */
        /*Trazas7*/
        int trysTrab1 = 0, trysTrab2 = 0, rand = 0;
        /*Trazas7*/
        ArrayList<String> matrizTurnos = individuo1.getTurnos();
        ArrayList<Integer> contcv = new ArrayList<>();
        boolean encontrado = false;
        Solucion individuo2 = (Solucion) individuo1.clone();
        while (contcv.size() < matrizTurnos.size() && !encontrado) {
            trysTrab1++;
            int cv = TurnoAleatorio(contcv, matrizTurnos);
            contcv.add(cv);
            ArrayList<Integer> contcs = new ArrayList<>();
            contcs.add(cv);
            while (contcs.size() < matrizTurnos.size()) {
                int cs = TurnoAleatorio(contcs, matrizTurnos);
                contcs.add(cs);
                if (cs != cv) {
                    trysTrab2++;
                    rand = (int) Math.floor(Math.random() * (individuo1.getTurnos().get(0).length() / 3)) * 3;
                    Movimiento mov = new Movimiento(cv, cs, 0, rand);
                    ArrayList<String> individuo = individuo1.getTurnos();
                    String controladorVago = individuo.get(mov.getDador());
                    String controladorP = individuo.get(mov.getReceptor());
					
				/*	if(rand<controladorVago.length()-1){
						while(controladorVago.charAt(rand)==controladorVago.charAt(rand+1)){
							rand++;
							if(rand==controladorVago.length()-1){rand--;break;}
						}
						mov.setFin(rand);
					}else{rand--;}
				*/
                    String cadena = controladorVago.substring(0, rand);
                    String cadenaP = controladorP.substring(0, rand);

                    boolean t = false;// comprobarLibre(individuo, mov, cadena);
                    boolean m = comprobarNucleos(cadena, controladorP, patrones);
                    boolean m1 = comprobarNucleos(cadenaP, controladorVago, patrones);
                    //boolean m = comprobarNucleos2(controladorVago, cadenaP, entrada.getNucleos());
                    //boolean m1 = comprobarNucleos2(controladorP, cadena, entrada.getNucleos());

                    ArrayList<String> dosInd = new ArrayList<>();
                    if (t || !m || !m1) {
                        dosInd = null;
                        //	}else if(controladorVago.charAt(rand)==controladorVago.charAt(rand+1) || controladorP.charAt(rand)==controladorP.charAt(rand+1)){
                        //		dosInd = null;
                    } else {
                        String controladorVago2 = cadenaP + controladorVago.substring(rand, controladorVago.length());
                        String controladorP2 = cadena + controladorP.substring(rand, controladorP.length());

                        dosInd.add(controladorVago2);
                        dosInd.add(controladorP2);
                    }
                    if (dosInd != null) {
                        individuo2 = DoChange1(dosInd, individuo1, mov, parametrosAlg);
                        encontrado = true;
                        break;
                    }
                }
            }
        }
        /*Trazas6*/
        iteracion.add("" + (0));//Intervalo
        iteracion.add("" + 0);//IntentosIntervalos
        iteracion.add("" + trysTrab1);//IntentosC1
        iteracion.add("" + trysTrab2);//IntentosC2
        /*Trazas6*/
        return individuo2;
    }

}
