package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Movimiento;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

import static herramientas.CridaUtils.STRING_DESCANSO;

public class Move6 {

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
            if (!trabajador.substring(i, i + 3).equals(STRING_DESCANSO)) {
                if (i == 0 || (trabajador.substring(i - 3, i).equals(STRING_DESCANSO))) {
                    inicio.add(i);
                }
            }
        }
        for (int i = trabajador.length() - 3; i >= 0; i -= 3) {
            if (!trabajador.substring(i, i + 3).equals(STRING_DESCANSO)) {
                if (i == trabajador.length() - 3 || (trabajador.substring(i + 3, i + 6).equals(STRING_DESCANSO))) {
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

    private static Solucion DoChange1(ArrayList<String> dosInd, Solucion individuo, Movimiento mov, ParametrosAlgoritmo pa) {
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

    private static ArrayList<String> ChangeCont(Solucion ind, Movimiento mov, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {
        /*
         * Comprueba que se pueda hacer un transpaso de trabajo entre dos controladores,
         * si es posible devuelve los dos individuos con el cambio ya hecho, si no devuelve null
         */

        ArrayList<String> individuo = ind.getTurnos();
        String controladorVago = individuo.get(mov.getDador());
        String controladorP = individuo.get(mov.getReceptor());
        String cadena = controladorVago.substring(mov.getInicio(), mov.getFin());
        String cadenaP = controladorP.substring(mov.getInicio(), mov.getFin());
        String controladorVago2 = controladorVago.substring(0, mov.getInicio()) + cadenaP + controladorVago.substring(mov.getFin(), controladorVago.length());
        String controladorP2 = controladorP.substring(0, mov.getInicio()) + cadena + controladorP.substring(mov.getFin(), controladorP.length());

        boolean t = false;// comprobarLibre(individuo, mov, cadena);
        boolean m = comprobarNucleos(controladorVago, cadenaP, patrones);
        boolean m1 = comprobarNucleos(controladorP, cadena, patrones);
        if (t || !m || !m1) {
            return null;
        }

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
                    if (!cadena.substring(l, l + 3).equalsIgnoreCase(STRING_DESCANSO)) {
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

    public static Solucion movimientoTrabajoIntervalosPseudoAleatorios(Solucion individuo1, int granularidadMax, int granularidadMin, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo pa, ArrayList<String> iteracion) {
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
                ArrayList<Integer> listG = crearListG(individuo1.getLongdescansos(), granularidadMax);

                while (!encontrado && listG.size() != 0) {
                    trysIntervalo++;
                    tmnIntervalo = (pos.get(1) - pos.get(0));
                    granularidad = listG.remove((int) (Math.floor(Math.random() * ((listG.size() - 1) - 0 + 1)) + 0));
                    if (granularidad >= tmnIntervalo) {
                        for (int i = 0; i < listG.size(); i++) {
                            if (tmnIntervalo <= listG.get(i)) {
                                listG.remove(i);
                            }
                        }
                    }
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

    private static ArrayList<Integer> crearListG(int longdescansos, int granularidadMax) {
        ArrayList<Integer> listG = new ArrayList<>();
        if (longdescansos == 3 || longdescansos == 6 || longdescansos == 9 || longdescansos == 12) {
            for (int i = 9; i <= granularidadMax; i += 9) {
                listG.add(i);
            }
        } else if (longdescansos == 4 || longdescansos == 8 || longdescansos == 12) {
            for (int i = 12; i <= granularidadMax; i += 12) {
                listG.add(i);
            }
        } else if (longdescansos == 5 || longdescansos == 10) {
            for (int i = 15; i <= granularidadMax; i += 15) {
                listG.add(i);
            }
        } else if (longdescansos == 0) {
            for (int i = 3; i <= granularidadMax; i += 3) {
                listG.add(i);
            }
        } else {
            listG.add(longdescansos * 3);
        }
        return listG;
    }

}
