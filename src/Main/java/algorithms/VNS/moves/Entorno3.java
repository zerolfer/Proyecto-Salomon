package algorithms.VNS.moves;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Movimiento;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

public class Entorno3 {

    public static ArrayList<Solucion> movimiento(Solucion individuo1, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg, int tmnIntervalo) {
        ArrayList<Solucion> soluciones = new ArrayList<>();
        ArrayList<String> turnos = individuo1.getTurnos();
        int tmn = turnos.size();
        String c1 = "", c2 = "", intervalo = "", intervalo2 = "", ant = "", pos = "";
        for (int i = 0; i < tmn; i++) {
            for (int j = 0; j < tmn; j++) {
                //REGLA 4
                if (i < j || tmn == individuo1.getControladores().size()) {
                    c1 = turnos.get(i);
                    c2 = turnos.get(j);
                    for (int k = 0; k < c1.length() - tmnIntervalo * 3; k += 3) {
                        intervalo = c1.substring(k, k + (3 * tmnIntervalo));
                        intervalo2 = c2.substring(k, k + (3 * tmnIntervalo));
                        //REGLA 1 y 2
                        if (!intervalo.replaceAll("111", "").equalsIgnoreCase("") && intervalo2.replaceAll("111", "").equalsIgnoreCase("")) { //c1:NoVacio c2:Vacio
                            if (k != 0) {
                                ant = c2.substring(k - 3, k);
                            } else {
                                ant = "";
                            }
                            if (k + (3 * tmnIntervalo) != c1.length()) {
                                pos = c2.substring(k + (3 * tmnIntervalo), k + (3 * tmnIntervalo) + 3);
                            } else {
                                pos = "";
                            }
                            //REGLA 3
                            if ((!ant.equals(intervalo.substring(0, 3)) || ant.equals("111")) && (!pos.equals(intervalo.substring(intervalo.length() - 3, intervalo.length())) || pos.equals("111"))) {         //NO CONCATENA
                                Movimiento mov = new Movimiento(i, j, k, k + (3 * tmnIntervalo));
                                ArrayList<String> dosInd = ChangeCont(individuo1, mov, patrones, entrada, parametros, parametrosAlg);//comprueba que cumpla las condiciones (devuelve 2 string cambiados o null)
                                if (dosInd != null) {
                                    if (tmn != individuo1.getControladores().size()) {
                                        int timeNumber = (int) Math.ceil((tmn - i) / 2);
                                        for (int l = 0; l < timeNumber; l++) {
                                            soluciones.add(DoChange1(dosInd, individuo1, mov, parametrosAlg));
                                        }
                                    } else {
                                        soluciones.add(DoChange1(dosInd, individuo1, mov, parametrosAlg));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return soluciones;
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
        String controladorVago2 = controladorVago.substring(0, mov.getInicio()) + cadenaP + controladorVago.substring(mov.getFin(), controladorVago.length());
        String controladorP2 = controladorP.substring(0, mov.getInicio()) + cadena + controladorP.substring(mov.getFin(), controladorP.length());

        ArrayList<String> dosInd = new ArrayList<>();
        dosInd.add(controladorVago2);
        dosInd.add(controladorP2);

        if (!parametrosAlg.getFuncionFitnessFase2().equalsIgnoreCase("reduccionControladoresYRestricciones")) {//Si fase3
            Solucion ind2 = DoChange1(dosInd, ind, mov, parametrosAlg);
            if (Restricciones.comprobarRestriccionesOptimizado(ind2, patrones, entrada, parametros) != 0) {
                return null;
            }
        }
        return dosInd;
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
            if (descanso) {
                ArrayList<Controlador> cs = new ArrayList<>();
                for (Controlador c : individuo.getControladores()) {
                    cs.add((Controlador) c.clone());
                }
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


}
