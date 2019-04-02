package herramientas.pruebasVarias.pruebaVelRegex;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Turno;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class RestriccionesRegex {

    public static long comprobarRestricciones(Solucion individuo, Patrones patrones, Parametros parametros, Entrada entrada) {
        long t1 = System.currentTimeMillis();
        /*Inicio Restricciones*/
        comprobarTrabajoPosicionMinimoConsecutivo(individuo.getTurnos(), patrones);
        comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), patrones);
        comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), patrones);
        comprobarTurnoVacio(individuo, patrones);
        comprobarControladorTurnoCorto(individuo, patrones);
        comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), patrones);
        comprobarPorcentajeDescanso(individuo, entrada, patrones, entrada.getTurno(), parametros);
        /*Fin Restricciones*/
        return System.currentTimeMillis() - t1;
    }

    private static int comprobarTrabajoPosicionMinimoConsecutivo(ArrayList<String> turnos, Patrones patrones) {
        int p = 0;
        for (int i = 0; i < turnos.size(); i++) {
		/*	if(patrones.getArray()[118].matcher(turnos.get(i)).find()||patrones.getArray()[119].matcher(turnos.get(i)).find()) {
				p++;
			}
		*/
            boolean pat = false;
            for (int j = 40; j <= 54; j++) {
                if (patrones.getArray()[j].matcher(turnos.get(i)).matches()) {
                    pat = true;
                    break;
                }
            }
            if (!pat) {
                p++;
            }

        }
        return p;
    }

    private static int comprobarDescansoMinimoConsecutivo(ArrayList<String> turnos, Patrones patrones) {
        int p = 0;
        for (int i = 0; i < turnos.size(); i++) {
		/*	if(patrones.getArray()[117].matcher(turnos.get(i)).find()) {
				p++;
			}
		*/
            boolean pat = false;
            for (int j = 55; j <= 66; j++) {
                if (patrones.getArray()[j].matcher(turnos.get(i)).matches()) {
                    pat = true;
                    break;
                }
            }
            if (!pat) {
                p++;
            }

        }
        return p;
    }

    public static int comprobarTrabajoMinimoConsecutivo(ArrayList<String> turnos, Patrones patrones) {
        int p = 0;
        for (int i = 0; i < turnos.size(); i++) {
	/*		if(patrones.getArray()[116].matcher(turnos.get(i)).find()) {
				p++;
			}
	*/
            boolean pat = false;
            for (int j = 25; j <= 36; j++) {
                if (patrones.getArray()[j].matcher(turnos.get(i)).matches()) {
                    pat = true;
                    break;
                }
            }
            if (!pat) {
                p++;
            }

        }
        return p;
    }

    public static int comprobarTurnoVacio(Solucion individuo, Patrones patrones) {
        int p = 0;
        ArrayList<String> turnos = individuo.getTurnos();
        for (int i = 0; i < turnos.size(); i++) {
            if (patrones.getArray()[0].matcher(turnos.get(i)).matches()) {
                p++;
            }
        }
        return p;
    }

    public static int comprobarControladorTurnoCorto(Solucion individuo, Patrones patrones) {
        int p = 0;
        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getTurno().equalsIgnoreCase("TC")) {
                if (controladores.get(i).getTurnoAsignado() != -1) {
                    if (!patrones.getArray()[1].matcher(turnos.get(controladores.get(i).getTurnoAsignado())).matches()) {
                        p++;
                    }
                }
            }
        }
        return p;
    }

    public static int comprobarTrabajoMaximoConsecutivo(ArrayList<String> turnos, Patrones patrones) {
        int p = 0;
        for (int i = 0; i < turnos.size(); i++) {
	/*		if(patrones.getArray()[115].matcher(turnos.get(i)).find()) {
				p++;
			}
	*/
            boolean pat = false;
            for (int j = 10; j <= 21; j++) {
                if (patrones.getArray()[j].matcher(turnos.get(i)).matches()) {
                    pat = true;
                    break;
                }
            }
            if (!pat) {
                p++;
            }


        }
        return p;
    }

    public static int comprobarPorcentajeDescanso(Solucion individuo, Entrada entrada, Patrones patrones, Turno turno, Parametros parametros) {
        int p = 0;
        String t = "";
        ArrayList<String> turnos = individuo.getTurnos();
        ArrayList<Controlador> controladores = individuo.getControladores();
        int slotsDesTL = turno.getSlotsDesTL();
        int slotsDesTC = turno.getSlotsDesTC();
        for (int i = 0; i < controladores.size(); i++) {
            boolean m = true;
            int numTurno = controladores.get(i).getTurnoAsignado();
            if (numTurno != -1) {
                t = turnos.get(controladores.get(i).getTurnoAsignado());
                int cnt = 0;
                Matcher mat = patrones.getArray()[2].matcher(t);
                while (mat.find()) {
                    cnt++;
                }
                if (controladores.get(i).getTurno().equalsIgnoreCase("TL") && slotsDesTL > cnt) {
                    m = false;
                } else if (controladores.get(i).getTurno().equalsIgnoreCase("TC") && slotsDesTC > cnt) {
                    m = false;
                }
            }
            if (!m || numTurno == -1) {
                p++;
            }
        }
        return p;
    }
}
