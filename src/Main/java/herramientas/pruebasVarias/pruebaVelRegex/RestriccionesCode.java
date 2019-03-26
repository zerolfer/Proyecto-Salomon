package herramientas.pruebasVarias.pruebaVelRegex;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Turno;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;

import java.util.ArrayList;

public class RestriccionesCode {

    public static long comprobarRestricciones(Solucion individuo, Parametros parametros, Entrada entrada, ParametrosAlgoritmo_SA pa) {
        long t1 = System.currentTimeMillis();
        /*Inicio Restricciones*/
        int p = 0;
        p += comprobarTrabajoPosicionMinimoConsecutivo(individuo.getTurnos(), pa);
        p += comprobarDescansoMinimoConsecutivo(individuo.getTurnos());
        p += comprobarTrabajoMinimoConsecutivo(individuo.getTurnos());
        p += comprobarTurnoVacio(individuo);
        p += comprobarControladorTurnoCorto(individuo, entrada);//CONFIRMAR CON TURNOS LARGOS Y CORTOS
        p += comprobarTrabajoMaximoConsecutivo(individuo.getTurnos());
        p += comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros);
        return System.currentTimeMillis() - t1;
    }

    private static int comprobarPorcentajeDescanso(Solucion individuo, Entrada entrada, Turno turno, Parametros parametros) {
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
                for (int j = 0; j < t.length(); j += 3) {
                    if (t.substring(j, j + 3).equalsIgnoreCase("111")) {
                        cnt++;
                    }
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

    private static int comprobarTrabajoMaximoConsecutivo(ArrayList<String> turnos) {
        int p = 0;
        int max = 24;
        for (String turno : turnos) {
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt > max && cnt != 0) {
                        p++;
                    }
                    cnt = 0;
                }
            }
        }
        return p;
    }

    private static int comprobarControladorTurnoCorto(Solucion individuo, Entrada entrada) {//TODO: CONFIRMAR CON TURNOS LARGOS Y CORTOS
        int p = 0;
        int resto = entrada.getTurno().getTl()[1] - entrada.getTurno().getTc()[1];
        int inicioTCorto = entrada.getTurno().getTc()[0];


        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getTurno().equalsIgnoreCase("TC")) {
                if (controladores.get(i).getTurnoAsignado() != -1) {
                    String t = turnos.get(controladores.get(i).getTurnoAsignado());
                    if (inicioTCorto == 0) {
                        for (int j = t.length() - (resto * 3); j < t.length(); j += 3) {
                            if (!t.substring(i, i + 3).equalsIgnoreCase("111")) {
                                p++;
                            }
                        }
                        //array[1] = Pattern.compile("^.*(111){"+ resto +"}$");
                    } else {
                        for (int j = 0; j < (inicioTCorto * 3); j += 3) {
                            if (!t.substring(i, i + 3).equalsIgnoreCase("111")) {
                                p++;
                            }
                        }
                        //array[1] = Pattern.compile("^(111){"+ inicioTCorto +"}.*$");
                    }
                }
            }
        }
        return p;
    }

    private static int comprobarTurnoVacio(Solucion individuo) {
        int p = 0;
        int min = 3;
        for (String turno : individuo.getTurnos()) {
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                }
            }
            if (cnt == 0) {
                p++;
            }
            cnt = 0;
        }
        return p;
    }

    private static int comprobarTrabajoMinimoConsecutivo(ArrayList<String> turnos) {
        int p = 0;
        int min = 3;
        for (String turno : turnos) {
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt < min && cnt != 0) {
                        p++;
                    }
                    cnt = 0;
                }
            }
            if (cnt < min && cnt != 0) {
                p++;
            }
        }
        return p;
    }

    private static int comprobarDescansoMinimoConsecutivo(ArrayList<String> turnos) {
        int p = 0;
        int min = 3;
        for (String turno : turnos) {
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt < min && cnt != 0) {
                        p++;
                    }
                    cnt = 0;
                }
            }
        }
        return p;
    }

    private static int comprobarTrabajoPosicionMinimoConsecutivo(ArrayList<String> turnos, ParametrosAlgoritmo_SA pa) {
        int p = 0;
        int min = 3;
        for (String turno : turnos) {
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (turno.substring(i, i + 3).equals("111") && cnt == 0) {

                } else if (turno.substring(i, i + 3).equals("111") && cnt < min) {
                    p++;
                } else if (turno.substring(i, i + 3).equals("111") && cnt >= min) {
                    cnt = 0;
                } else if (!turno.substring(i, i + 3).equals("111")) {
                    if (i == 0) {
                        cnt++;
                    } else {
                        if (turno.substring(i, i + 3).equals(turno.substring(i - 3, i))) {
                            cnt++;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt == 0) {
                            cnt++;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt < min) {
                            p++;
                            cnt = 1;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt >= min) {
                            cnt = 1;
                        }
                    }
                }
            }
        }
        return p;
    }

}
