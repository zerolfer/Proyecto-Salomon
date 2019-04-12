package InicializarPoblacion;

import estructurasDatos.Auxiliares.ObjAux1;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.DominioDelProblema.Propiedades;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import main.MainPruebas;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.jam.xml.TunnelledException;

import patrones.Patrones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import static herramientas.CridaUtils.LONGITUD_CADENAS;
import static herramientas.CridaUtils.STRING_DESCANSO;
import static herramientas.CridaUtils.STRING_NO_TURNO;
/**
 * Clase utilizada para la inicializacion de un conjunto de soluciones iniciales.
 *
 * @author Tino
 */
public class InicializarPoblacion {

    /**
     * Variable auxiliar que indica si la reparacion de ciertas restricciones incumplidas por las soluciones es posible.
     */
    public static boolean posible = true;

    /**
     * Metodo de inicializacion principal.
     *
     * @param entrada  Entrada del problema.
     * @param p        Parametros del problema.
     * @param patrones Patrones para la comprobacion de restricciones.
     * @return Conjunto de soluciones iniciales (No necesariamente factibles).
     */
    public static ArrayList<Solucion> inicializarPoblacion(Entrada entrada, Parametros p, Patrones patrones) {
        ArrayList<Solucion> poblacion = new ArrayList<>();

        int maxD = (p.getTiempoTrabMax() / p.getTamanoSlots()) / 2;
        int minD = p.getTiempoDesMin() / p.getTamanoSlots();
        int maxT = p.getTiempoTrabMax() / p.getTamanoSlots();
        int minT = p.getTiempoTrabMin() / p.getTamanoSlots();
        int descanso =6;

        Solucion individuo = inicializarIndividuo(descanso, maxT, minT, minD, entrada, p, patrones);
        poblacion = comprobarCondicionesEntorno(individuo, poblacion, entrada, patrones, p);
        
        System.out.println("La poblacion inicial es de: " + poblacion.size() + " individuos");
        MainPruebas.problema += ("La poblacion inicial es de: " + poblacion.size() + " individuos" + "\n");
        return poblacion;
    }

    /**
     * Metodo para devolver solo las soluciones factibles (Se deben descomentar las partes comentadas, en caso
     * contrario el metodo devuelve todas las soluciones).
     *
     * @param individuo  Solucion.
     * @param poblacion  Conjunto de soluciones.
     * @param entrada    Entrada del problema.
     * @param patrones   Patrones para la comprobacion de restricciones.
     * @param parametros Paramtros del problema.
     * @return Poblacion con el nuevo individuo.
     */
    private static ArrayList<Solucion> comprobarCondicionesEntorno(Solucion individuo, ArrayList<Solucion> poblacion,
                                                                   Entrada entrada, Patrones patrones,
                                                                   Parametros parametros) {
//		/*En el caso de querer introducir solo soluciones factibles descomentar el codigo*/
//		if(Restricciones.penalizacionPorRestricciones(individuo, patrones, entrada, parametros)==0){
//			System.out.println();
        poblacion.add(individuo);
//		}
        return poblacion;
    }

    /**
     * Metodo para la creación de una solución inicial con descansos de un tamaño determinado.
     *
     * @param descanso Tamaño de los descansos de la plantilla usada para la creacion de soluciones.
     * @param maxT     Tiempo de trabajo maximo.
     * @param minT     Tiempo de trabajo minimo.
     * @param minD     Tiempo de descanso minimo.
     * @param entrada  Entrada del problema.
     * @param p        Parametros del problema.
     * @param patrones Patrones para la comprobacion de las restricciones.
     * @return Solucion creada mediante plantillas 3x1.
     */
    private static Solucion inicializarIndividuo(int descanso, int maxT, int minT, int minD, Entrada entrada,
                                                 Parametros p, Patrones patrones) {

        Solucion individuo = entrada.getDistribucionInicial().clone();
        if (entrada.getSectorizacionModificada() != null) {
            // < PASO 1 >
            eliminarSectoresCerrados(entrada.getSlotMomentoActual(), entrada.getSectorizacion(),
                    entrada.getSectorizacionModificada(), individuo);
            // < PASO 2 >
            introducirPlantillasNuevosSectores(entrada, descanso, maxT, minT, minD);
        }
        
        // < PASO 3 >
        eliminarControladoresBaja(entrada,individuo);
        // < PASO 4 >
        anadirControladoresAlta(entrada,individuo);
        
        return individuo;



        /*         LEGACY:         */

        //FASE 1
//        ArrayList<ArrayList<String>> cadenasDeTurnos = introducirPlantillasNuevosSectores(entrada, descanso, maxT, minT, minD);
        //FASE 2
//        cadenasDeTurnos = reparacionSoluciones(entrada, p, cadenasDeTurnos, minT, patrones);
        //FASE 3
//        ArrayList<String> turnos = transformacionSoluciones(cadenasDeTurnos);
//        Solucion individuo = asignacionControladores(turnos, entrada, descanso);
        //FASE 4
//        return individuo;
    }

private static void anadirControladoresAlta(Entrada entrada, Solucion individuo) {
	ArrayList<Controlador> controladores = individuo.getControladores();
	ArrayList<String> turnos = individuo.getTurnos();
	String t  = turnos.get(0);
	//TODO: PROBAR QUE LOS CORTES EN LA CADENA SEAN CORRECTOS
	for (int i = 0; i < controladores.size(); i++) {
		if (controladores.get(i).getBajaAlta() == Propiedades.ALTA && controladores.get(i).getSlotBajaAlta()!=0) {
			int momentoAlta = controladores.get(i).getSlotBajaAlta()*LONGITUD_CADENAS;
			String turno ="";
			for (int j = 0; j < t.length(); j+=LONGITUD_CADENAS) {
				if (j<momentoAlta) {
					turno += STRING_NO_TURNO;					
				}else {
					turno += STRING_DESCANSO;
				}
			}
			turnos.add(turno);
			controladores.get(i).setTurnoAsignado(turnos.size()-1);
		}
	}
	}

private static void eliminarControladoresBaja(Entrada entrada, Solucion individuo) {
	ArrayList<Controlador> controladores = individuo.getControladores();
	ArrayList<String> turnos = individuo.getTurnos();
	for (int i = 0; i < controladores.size(); i++) {
		if (controladores.get(i).getBajaAlta() == Propiedades.BAJA) {
			Controlador c = controladores.get(i);
			int momentoBaja = c.getSlotBajaAlta();
			String t = turnos.get(c.getTurnoAsignado());
			String cadFin = t.substring(momentoBaja*LONGITUD_CADENAS, t.length());
			String cadIni = t.substring(0,momentoBaja*LONGITUD_CADENAS);
			for (int j = momentoBaja*LONGITUD_CADENAS; j < t.length(); j+=LONGITUD_CADENAS) {
				cadIni += STRING_NO_TURNO;
			}
			for (int j = 0; j < momentoBaja*LONGITUD_CADENAS; j+=LONGITUD_CADENAS) {
				if(entrada.getSlotMomentoActual()*LONGITUD_CADENAS>j) {
					cadFin = STRING_NO_TURNO + cadFin;
				}else {
					cadFin = STRING_DESCANSO + cadFin;
				}
			}
			Controlador cImaginario = c.clone();
			cImaginario.setBajaAlta(Propiedades.ALTA);
			cImaginario.setSlotBajaAlta(0);
			cImaginario.setId(controladores.size());
			cImaginario.setImaginario(true);
			cImaginario.setTurnoAsignado(turnos.size()-1);
			turnos.set(c.getTurnoAsignado(), cadIni);
			turnos.add(cadFin);
			controladores.add(cImaginario);
		}
	}
		
	}

//    private static Solucion procesarDistribucionInicial(Solucion distribucionInicial, Entrada entrada) {
//        Solucion sol = distribucionInicial.clone();
//        if (entrada.getControladores() != null)
//            ; // TODO eliminarControladoresBaja();
//        if (entrada.getSectorizacionModificada() != null) {
//             < PASO 1 >
//            eliminarSectoresCerrados(entrada.getSlotMomentoActual(), entrada.getSectorizacion(),
//                    entrada.getSectorizacionModificada(), sol);
//             < PASO 2 >
//            introducirPlantillaNuevosSectores(); // < Paso 2 >
//        }
//        return sol;
//    }
    private static void eliminarSectoresCerrados(int slotMomentoActual, ArrayList<ArrayList<String>> sectorizacion,
                                                 ArrayList<ArrayList<String>> sectorizacionModificada,
                                                 Solucion distribucionInicial) {

        Collection<String> sectoresCerrados = null;
        boolean hayCambio = false;
        int idxInicio = slotMomentoActual;

        // iteramos por todos los slots de la instancia del problema
        for (int slot = slotMomentoActual; slot < sectorizacion.size(); slot++) {

            // Solamente vamos a computar los slots en los que haya cambio en los sectores cerrados
            // Recorremos la distribución de trozo en trozo, para mayor eficiencia
            if (sectoresCerrados == null  // si estamos al inicio
                    || !sectorizacionModificada.get(slot).equals(sectorizacionModificada.get(slot - 1)) // si hay cambio
                    || !sectorizacion.get(slot).equals(sectorizacion.get(slot - 1))) {

                // como hay un cambio, obtenemos los nuevos sectores cerrados, será una lista vacia o no
                sectoresCerrados = obtenerSectoresCerrados(sectorizacion.get(slot), sectorizacionModificada.get(slot));

                // si ya se habia alcanzado un cambio, modificamos los turnos en el intervalo que va
                // desde el slot donde se produjo dicho cambio, hasta el actual
                if (hayCambio) {
                    modificarTurno(idxInicio, slot, sectoresCerrados, distribucionInicial);
                    hayCambio = false;
                }

                // como ha habido un cambio, lo marcamos en la baliza hayCambio,
                // y guardamos la posicion (numero del slot) de dicho cambio para
                // utilizarlo en el if anterior la proxima vez que se detecte un cambio
                if (sectoresCerrados.size() > 0) {
                    hayCambio = true;
                    idxInicio = slot;
                }
            }
        }
        // si solo hay un unico cambio en toda la sectorización, el cambio no habrá sido computado, lo computamos
        if (hayCambio) {
            modificarTurno(idxInicio, sectorizacion.size(), sectoresCerrados, distribucionInicial);
        }
    }

    /**
     * Actualiza el turno (el String) <b>de todos los controladores</b> dentro del intervalo de tiempo
     * (en slots de 3 caracteres cada uno) fijado en los parámetros
     * Importante: este método MODIFICA LA <code>distribucionInicial<code/> pasada como argumento
     *
     * @param slotInicio          inicio del intervalo (numero de slot)
     * @param slotFin             final del intervalo (numero de slot)
     * @param sectoresCerrados    lista de sectores cerrados en el intervalo
     * @param distribucionInicial distribucion inicial a modificar
     */
    private static void modificarTurno(int slotInicio, int slotFin, final Collection<String> sectoresCerrados,
                                       Solucion distribucionInicial) {
        // para cada sector que se cierra
        for (String sectorCerrado : sectoresCerrados) {
            Pattern patron = Pattern.compile("(?i)" + sectorCerrado);

            // para cada uno de los turnos de la distribucion inicial
            for (int j = 0; j < distribucionInicial.getTurnos().size(); j++) {
                String s = distribucionInicial.getTurnos().get(j); // turno original
                String previo = s.substring(0, slotInicio * LONGITUD_CADENAS);
                String medio = StringUtils.replaceIgnoreCase(
                        s.substring(slotInicio * LONGITUD_CADENAS, slotFin * LONGITUD_CADENAS), sectorCerrado, STRING_DESCANSO
                ); // sustituimos todas las apariciones del sector (ya sea mayus o minus) por descansos
                String posterior = s.substring(slotFin * LONGITUD_CADENAS);
                distribucionInicial.getTurnos().set(j, previo + medio + posterior);  // recomponemos y actualizamos el turno
            }
        }
    }

    private static List<String> obtenerSectoresCerrados(ArrayList<String> iniciales, ArrayList<String> modificados) {
        /*
         * iniciales - modificados = cerrados
         *  {a,b,c}  -   {a,c,d,e}     =     {b}
         */

        return diferenciaConjuntos(iniciales, modificados);
    }

//    private static Collection<String> obtenerNuevosSectoresAbiertos(ArrayList<String> iniciales, ArrayList<String> modificados) {
//        /*
//         * modificados - iniciales = nuevos abiertos
//         *  {a,c,d,e}  -   {a,b,c}     =     {d,e}
//         */
//        return diferenciaConjuntos(modificados, iniciales);
//    }

    private static List<List<String>> obtenerNuevosSectoresAbiertos(ArrayList<ArrayList<String>> sectorizacionInicial, ArrayList<ArrayList<String>> sectorizacionModificada) {
        /*
         * modificados - iniciales = nuevos abiertos
         *  {a,c,d,e}  -   {a,b,c}     =     {d,e}
         */
        HashSet<List<String>> set = new HashSet<>();
        for (int i = 0; i < sectorizacionInicial.size(); i++) {
            List<String> iniciales = sectorizacionInicial.get(i);
            List<String> modificados = sectorizacionModificada.get(i);
            set.add(diferenciaConjuntos(modificados, iniciales));
        }
        return new ArrayList<>(set);
    }

    private static List<String> diferenciaConjuntos(List<String> c1, List<String> c2) {

        /*
        // TODO: ¿Podría implementarse con HashSets de forma más eficiente?
        Set<String> cerradosSET = new HashSet<>(iniciales);
        Set<String> modificadosSET = new HashSet<>(modificados);
        if (cerradosSET.retainAll(modificadosSET)) {
            modificadosSET.forEach(System.out::println);
            System.out.println(("tenia tamaño " + modificadosSET.size()));
            return cerradosSET;
        } else return new HashSet<>();
        */
        List<String> res = new ArrayList<>();
        /*List<String> saltar = new ArrayList<>();*/
        // TODO: Alternativa? Registro índices que ya sabemos seguro que no está porque ya han sido usados? mas/menos eficiente?

        for (String el1 : c1) {
            Boolean encontrado = false;
            // buscar sector
            for (String el2 : c2) {
                if (/*!saltar.contains(el2) && */el1.equalsIgnoreCase(el2)) {
                    encontrado = true;
                    break; // sector encontrado, no se ha cerrado
                }
            }

            if (!encontrado)
                res.add(el1);

        }
        return res;

    }

    /**
     * Metodo utilizado para modificar el formato de la matriz de turnos (Parse).
     *
     * @param cadenasDeTurnos Lista con los turnos de trabajo de los controladores.
     * @return Lista con los turnos de trabajo de los controladores (con diferente formato).
     */
    public static ArrayList<String> transformacionSoluciones(ArrayList<ArrayList<String>> cadenasDeTurnos) {
        ArrayList<String> turnos = new ArrayList<>();
        for (int i = 0; i < cadenasDeTurnos.size(); i++) {
            ArrayList<String> cad = cadenasDeTurnos.get(i);
            String c = "";
            for (int j = 0; j < cad.size(); j++) {
                c = c + cad.get(j);
            }
            turnos.add(c);
        }
        for (int i = 0; i < turnos.size(); i++) {
            String t = turnos.get(i).replaceAll("1", "");
            if (t.equalsIgnoreCase("")) {
                turnos.remove(i);
                i--;
            }
        }
        return turnos;
    }

    /**
     * Metodo para realizar una asignacion optima de los controladores aereos dispnibles a los distintos turnos.
     *
     * @param turnos   Turnos de trabajo.
     * @param entrada  Entrada del problema.
     * @param descanso Longitud de los descansos de los turnos en cuestion.
     * @return Solucion con los controladores disponibles asignados a los turnos.
     */
    public static Solucion asignacionControladores(ArrayList<String> turnos, Entrada entrada, int descanso) {
        ArrayList<String> controladores = new ArrayList<>();
        ArrayList<Controlador> c = new ArrayList<>();
        for (int i = 0; i < entrada.getControladores().size(); i++) {
            Controlador cntl = entrada.getControladores().get(i);
            c.add(new Controlador(cntl.getId(), cntl.getTurno(), cntl.getNucleo(), cntl.isPTD(), cntl.isCON(),
                    cntl.isImaginario(), cntl.getBajaAlta(), cntl.getSlotBajaAlta()));
        }
        /*INICIAR LISTA CONTORLADORES*/
        ArrayList<ObjAux1> clasificacionTurnos = new ArrayList<>();
        for (int i = 0; i < turnos.size(); i++) {
            boolean turnoLargo = comprobarTurno(turnos.get(i), entrada);
            boolean ptd = comprobarSector(turnos.get(i), entrada);
            ArrayList<String> nucleo = comprobarNucleo(turnos.get(i), entrada);
            if (nucleo.size() == 1) {
                clasificacionTurnos.add(0, new ObjAux1(turnoLargo, ptd, nucleo, i));
            } else {
                clasificacionTurnos.add(new ObjAux1(turnoLargo, ptd, nucleo, i));
            }
        }
        for (int i = 0; i < clasificacionTurnos.size(); i++) {//asignarControlador TL Y PTD
            ObjAux1 o = clasificacionTurnos.get(i);
            boolean encontrado = false;
            if (o.isTurnoLargo() && o.isPtd()) {
                for (int j = 0; j < c.size(); j++) {
                    if (c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                        for (int k = 0; k < o.getNucleo().size(); k++) {
                            if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                        "Controlador: " + c.get(j).getId());
                                clasificacionTurnos.remove(i);
                                i--;
                                encontrado = true;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < clasificacionTurnos.size(); i++) {//asignarControlador TC Y PTD
            ObjAux1 o = clasificacionTurnos.get(i);
            boolean encontrado = false;
            if (!o.isTurnoLargo() && o.isPtd()) {
                for (int j = 0; j < c.size(); j++) {
                    if (c.get(j).getTurno().equalsIgnoreCase("TC") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                        for (int k = 0; k < o.getNucleo().size(); k++) {
                            if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                        "Controlador: " + c.get(j).getId());
                                clasificacionTurnos.remove(i);
                                i--;
                                encontrado = true;
                            }
                        }
                    }
                }
                if (!encontrado) {
                    for (int j = 0; j < c.size(); j++) {
                        if (c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                            for (int k = 0; k < o.getNucleo().size(); k++) {
                                if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                    c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                    controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                            "Controlador: " + c.get(j).getId());
                                    clasificacionTurnos.remove(i);
                                    i--;
                                    encontrado = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < clasificacionTurnos.size(); i++) {//asignarControlador TL Y RUTA
            ObjAux1 o = clasificacionTurnos.get(i);
            boolean encontrado = false;
            if (o.isTurnoLargo() && !o.isPtd()) {
                for (int j = 0; j < c.size(); j++) {
                    if (c.get(j).getTurno().equalsIgnoreCase("TL") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                        for (int k = 0; k < o.getNucleo().size(); k++) {
                            if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                        "Controlador: " + c.get(j).getId());
                                clasificacionTurnos.remove(i);
                                i--;
                                encontrado = true;
                            }
                        }
                    }
                }
                if (!encontrado) {
                    for (int j = 0; j < c.size(); j++) {
                        if (c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                            for (int k = 0; k < o.getNucleo().size(); k++) {
                                if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                    c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                    controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                            "Controlador: " + c.get(j).getId());
                                    clasificacionTurnos.remove(i);
                                    i--;
                                    encontrado = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < clasificacionTurnos.size(); i++) {//asignarControlador TC Y RUTA
            ObjAux1 o = clasificacionTurnos.get(i);
            boolean encontrado = false;
            if (!o.isTurnoLargo() && !o.isPtd()) {
                for (int j = 0; j < c.size(); j++) {
                    if (c.get(j).getTurno().equalsIgnoreCase("TC") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                        for (int k = 0; k < o.getNucleo().size(); k++) {
                            if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                        "Controlador: " + c.get(j).getId());
                                clasificacionTurnos.remove(i);
                                i--;
                                encontrado = true;
                            }
                        }
                    }
                }
                if (!encontrado) {
                    for (int j = 0; j < c.size(); j++) {
                        if (c.get(j).getTurno().equalsIgnoreCase("TL") && !c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                            for (int k = 0; k < o.getNucleo().size(); k++) {
                                if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                    c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                    controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                            "Controlador: " + c.get(j).getId());
                                    clasificacionTurnos.remove(i);
                                    i--;
                                    encontrado = true;
                                }
                            }
                        }
                    }
                }
                if (!encontrado) {
                    for (int j = 0; j < c.size(); j++) {
                        if (c.get(j).getTurno().equalsIgnoreCase("TC") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                            for (int k = 0; k < o.getNucleo().size(); k++) {
                                if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                    c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                    controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                            "Controlador: " + c.get(j).getId());
                                    clasificacionTurnos.remove(i);
                                    i--;
                                    encontrado = true;
                                }
                            }
                        }
                    }
                }
                if (!encontrado) {
                    for (int j = 0; j < c.size(); j++) {
                        if (c.get(j).getTurno().equalsIgnoreCase("TL") && c.get(j).isPTD() && c.get(j).getTurnoAsignado() == -1 && !encontrado) {
                            for (int k = 0; k < o.getNucleo().size(); k++) {
                                if (c.get(j).getNucleo().equalsIgnoreCase(o.getNucleo().get(k)) && !encontrado) {
                                    c.get(j).setTurnoAsignado(clasificacionTurnos.get(i).getPosicion());
                                    controladores.add("Turno: " + clasificacionTurnos.get(i).getPosicion() + " " +
                                            "Controlador: " + c.get(j).getId());
                                    clasificacionTurnos.remove(i);
                                    i--;
                                    encontrado = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        /*FIN LISTA CONTROLADORES*/
        @SuppressWarnings("unchecked")
        ArrayList<Controlador> controladores1 = (ArrayList<Controlador>) c.clone();
        Solucion ind = new Solucion(turnos, controladores1, descanso);
        return ind;
    }

    /**
     * Metodo utilizado para devolver el nucleo o lista de nucleos al que pertenecen los sectores de un turno
     * (Unicamente es valido para turnos formados por un unico sector).
     *
     * @param turno   Turnos de trabajo
     * @param entrada Entrada del problema.
     * @return Lista de nucleos
     */
    private static ArrayList<String> comprobarNucleo(String turno, Entrada entrada) {
        /*
         * Solo funciona con soluciones iniciales: Solo funciona con turnos que contengan un sector*/
        ArrayList<String> nucleo = new ArrayList<>();
        ArrayList<Nucleo> nucleos = entrada.getNucleos();
        for (int i = 0; i < turno.length(); i += 3) {
            if (!turno.substring(i, i + 3).equalsIgnoreCase(STRING_DESCANSO)) {
                for (int j = 0; j < nucleos.size(); j++) {
                    ArrayList<Sector> sctNucleo = nucleos.get(j).getSectores();
                    for (int k = 0; k < sctNucleo.size(); k++) {
                        if (turno.substring(i, i + 3).equalsIgnoreCase(sctNucleo.get(k).getId())) {
                            String nuc = nucleos.get(j).getNombre();
                            boolean dentro = false;
                            for (int l = 0; l < nucleo.size(); l++) {
                                if (nuc.equalsIgnoreCase(nucleo.get(l))) {
                                    dentro = true;
                                }
                            }
                            if (!dentro) {
                                nucleo.add(nuc);
                            }
                        }
                    }
                }
                return nucleo;
            }
        }
        return null;
    }

    /**
     * Metodo utilizado para comprobar si los sectores de un turno son PDT (Unicamente es valido para turnos formados
     * por un unico sector).
     *
     * @param turno   Turnos de trabajo
     * @param entrada Entrada del problema.
     * @return True: En caso de que los sectores del turno sean PDT. False en caso que sean CON.
     */
    private static boolean comprobarSector(String turno, Entrada entrada) {
        ArrayList<Sector> listaSec = entrada.getListaSectores();
        for (int i = 0; i < turno.length(); i += 3) {
            for (int j = 0; j < listaSec.size(); j++) {
                if (turno.substring(i, i + 3).equalsIgnoreCase(listaSec.get(j).getId())) {
                    if (listaSec.get(j).isPDT()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Comprobar si el turno de trabajo es corto o es largo.
     *
     * @param turno   Turnos de trabajo
     * @param entrada Entrada del problema.
     * @return True: En caso de que el turno sea Corto. False en caso que el turno sea Largo.
     */
    private static boolean comprobarTurno(String turno, Entrada entrada) {
        int nSlot = 0;
        int[] tc = entrada.getTurno().getTc();
        for (int i = 0; i < turno.length(); i += 3) {
            String slot = turno.substring(i, i + 3);
            if (!slot.equalsIgnoreCase(STRING_DESCANSO) && (tc[0] > nSlot || tc[1] < nSlot)) {
                return true;
            }

            nSlot++;
        }
        return false;
    }

    /**
     * Metodo utilizado para la reparacion de soluciones, entendiendo esto, como la modificacion de las mismas para
     * reducir el numero de restricciones incumplidas.
     *
     * @param entrada         Entrada del problema
     * @param p               Parametros del problema.
     * @param cadenasDeTurnos Lista de los turnos de trabajo.
     * @param minT            Tiempo de trabajo minimo.
     * @param patrones        Patrones para comprobacion de restricciones.
     * @return Lista de los turnos de trabajo con las modificaciones pertenentes reducir la infactibilidad.
     */
    public static ArrayList<ArrayList<String>> reparacionSoluciones(Entrada entrada, Parametros p,
                                                                    ArrayList<ArrayList<String>> cadenasDeTurnos,
                                                                    int minT, Patrones patrones) {
        for (int i = 0; i < cadenasDeTurnos.size(); i++) {
            ArrayList<String> turno = cadenasDeTurnos.get(i);
            int cont = 0;
            for (int l = 0; l < turno.size(); l++) {
                if (!turno.get(l).equals(STRING_DESCANSO)) {
                    String ant = turno.get(l);
                    if (l + 1 < turno.size() && ant.equals(turno.get(l + 1))) {
                        cont++;
                    } else {
                        if (cont < minT - 1) { //No se cumple el trabajo minimo
                            cadenasDeTurnos = ArreglarSoluciones.arregloTrabajoMinimo(entrada, p, cadenasDeTurnos, i,
                                    turno, l, ant, patrones);
                            if (posible == false) {
                                posible = true;
                                return cadenasDeTurnos;
                            } //No es posible su arreglo
                            turno = cadenasDeTurnos.get(i);
                            l = -1;
                            cont = 0;//Reseteamos l=-1 (al iniciar el bucle de nuevo l++ =0) para volver a comprobar
                            // el turno despues de los cambios

                        }
                        cont = 0;
                    }
                }
            }
        }
        return cadenasDeTurnos;
    }

    /**
     * Metodo para la generacion de soluciones con plantillas de 3x1.
     *
     * @param entrada  Entrada del problema.
     * @param descanso Tamaño del descanso para la plantilla.
     * @param maxT     Tiempo de trabajo maximo.
     * @param minT     Tiempo de trabajo minimo.
     * @param minD     Tiempo de descanso minimo.
     * @return Lista de turnos de trabajo.
     */
    private static void introducirPlantillasNuevosSectores(Entrada entrada, int descanso, int maxT,
                                                                        int minT, int minD) {
        Solucion distribucion = entrada.getDistribucionInicial(); // individuo final a actualizar
        ArrayList<String> turnos = distribucion.getTurnos(); // esto es la matriz de trabajo

        ArrayList<ArrayList<String>> sectorizacionPorSlots = entrada.getSectorizacionModificada();
        ArrayList<Sector> sectoresAbiertosTrasMomentoActual = entrada.getListaSectoresAbiertosTrasMomentoActual();
        ArrayList<Integer> secNoc = new ArrayList<>();

        for (Sector sector : sectoresAbiertosTrasMomentoActual) {

            //
            // Crear plantilla para cada nuevo sector:
            //

            ArrayList<ArrayList<String>> plantilla = new ArrayList<>();// agrupacion de 3 controladores (usando plantilla 3x1)

            plantilla.add(new ArrayList<>());
            plantilla.add(new ArrayList<>());
            plantilla.add(new ArrayList<>());


            //
            // {LEGACY}
            // Rellenar plantilla:

            // si el sector no es nocturno
            if (sector.getNoche() == 0) {
                // para cada slot, verificamos si el sector está abierto en ese instante
                for (int j = 0; j < sectorizacionPorSlots.size(); j++) {

                    // FIXME: en las nuevas plantillas, antes del momento actual, añadimos 0s o 1s??
//                    if(j<entrada.getSlotMomentoActual())
//                        introducirCero(plantilla);

                    List<String> sectoresAbiertos = sectorizacionPorSlots.get(j);
                    boolean open = false;
                    for (int k = 0; k < sectoresAbiertos.size(); k++) {
                        if (sector.getId().equalsIgnoreCase(sectoresAbiertos.get(k))) {
                            open = true;
                            plantilla = introducirSector(plantilla, descanso, sector.getId());
                            break;
                        }
                    }
                    if (!open) {
                        plantilla = introducirDescanso(plantilla);
                    }
                }
            } else {
                boolean yaIntroducido = false;
                for (int j = 0; j < secNoc.size(); j++) {
                    if (secNoc.get(j) == sector.getNoche()) {
                        yaIntroducido = true;
                    }
                }
                if (!yaIntroducido) {
                    secNoc.add(sector.getNoche());
                    plantilla = introducirSectorNoche(plantilla, sectoresAbiertosTrasMomentoActual, sector.getNoche(), sectorizacionPorSlots);
                }
            }

            // meter la plantilla a los turnos de la instancia, actualizando el formato
            introducirEnTurnos(plantilla, turnos);
        }
    }

    /**
     * Introduccion de las plantillas utilizadas para los sectores de noche.
     *
     * @param plantilla     Lista de turnos de trabajo.
     * @param sectores      Lista de sectores.
     * @param noche         Indicador de los sectores marcados como turno de noche.
     * @param sectorizacion Sectorizacion del problema.
     * @return Lista de turnos de trabajo, con los turnos para cubrir los sectores nocturos incluidos, si estos existen.
     */
    private static ArrayList<ArrayList<String>> introducirSectorNoche(ArrayList<ArrayList<String>> plantilla,
                                                                      ArrayList<Sector> sectores, int noche,
                                                                      ArrayList<ArrayList<String>> sectorizacion) {
        ArrayList<String> c4 = new ArrayList<>();
        plantilla.add(c4);
        /*Lista de sectores noche = int noche*/
        ArrayList<Sector> sectoresNocturnos = new ArrayList<>();
        for (int j = 0; j < sectores.size(); j++) {
            if (sectores.get(j).getNoche() == noche) {
                sectoresNocturnos.add(sectores.get(j));
            }
        }
        for (int j = 0; j < sectorizacion.size(); j++) {
            ArrayList<String> sectoresAbiertos = sectorizacion.get(j);
            for (int k = 0; k < sectoresAbiertos.size(); k++) {
                for (int i = 0; i < sectoresNocturnos.size(); i++) {
                    if (sectoresNocturnos.get(i).getId().equalsIgnoreCase(sectoresAbiertos.get(k))) {
                        plantilla = introducirSlotSectorNocturno(plantilla, sectoresNocturnos.get(i).getId());
                    }
                }
            }
        }

        return plantilla;
    }

    /**
     * Metodo para la introduccion de los sectores nocturnos especificos.
     *
     * @param plantilla Plantilla de trabajo para sectores nocturnos.
     * @param id        ID del sector nocturno.
     * @return Devuelve la plantilla compuesta por 4 turnos.
     */
    private static ArrayList<ArrayList<String>> introducirSlotSectorNocturno(ArrayList<ArrayList<String>> plantilla,
                                                                             String id) {
        int desc = 9; //Por ejemplo, este descanso/trabajo siempre es el mismo al ser turno de noche "el tiempo en
        // posicion optimo son 45mins"
        ArrayList<String> c1 = plantilla.get(plantilla.size() - 4);
        ArrayList<String> c2 = plantilla.get(plantilla.size() - 3);
        ArrayList<String> c3 = plantilla.get(plantilla.size() - 2);
        ArrayList<String> c4 = plantilla.get(plantilla.size() - 1);
        int j = c1.size();
        while (j >= (desc * 4)) {
            j = j - desc * 4;
        }
        if (j < desc) {
            c1.add(id.toLowerCase());
            c2.add(id.toUpperCase());
            c3.add(STRING_DESCANSO);
            c4.add(STRING_DESCANSO);
        } else if (j < desc * 2) {
            c1.add(STRING_DESCANSO);
            c2.add(STRING_DESCANSO);
            c3.add(id.toUpperCase());
            c4.add(id.toLowerCase());
        } else if (j < desc * 3) {
            c1.add(id.toUpperCase());
            c2.add(id.toLowerCase());
            c3.add(STRING_DESCANSO);
            c4.add(STRING_DESCANSO);
        } else if (j < desc * 4) {
            c1.add(STRING_DESCANSO);
            c2.add(STRING_DESCANSO);
            c3.add(id.toLowerCase());
            c4.add(id.toUpperCase());
        }
        plantilla.set(plantilla.size() - 4, c1);
        plantilla.set(plantilla.size() - 3, c2);
        plantilla.set(plantilla.size() - 2, c3);
        plantilla.set(plantilla.size() - 1, c4);
        return plantilla;
    }

    /**
     * Metodo utilizado para la introduccion de plantillas 3x1 asociadas a un sector al conjunto de turnos de trabajo.
     *
     * @param plantilla Plantilla 3x1.
     * @param turnos    Conjunto de turnos de trabajo en construccion.
     * @return Conjunto de turnos de trabajo en construccion con la nueva plantilla incorporada
     */
    private static void introducirEnTurnos(ArrayList<ArrayList<String>> plantilla,
                                           ArrayList<String> turnos) {

        // Para cada "fila" de la plantilla
        plantilla.forEach((pseudocontrolador) -> {
            // primero cambiamos el formato
            StringBuilder turnoString = new StringBuilder();
            pseudocontrolador.forEach(slot -> turnoString.append(slot));

            // segundo lo añadimos al turno
            turnos.add(turnoString.toString());

        });

        // NOTE: Si no compila lo anterior, cámbiese por la version sin expresiones lambda:
        /*

        List<String> turnosFormateados = new ArrayList<>();
        for (ArrayList<String> pseudocontrolador : plantilla) {

            // primero cambiamos el formato
            StringBuilder turnoString = new StringBuilder();
            pseudocontrolador.forEach(slot->turnoString.append(slot));

            for (String slot : pseudocontrolador)
                turnoString.append(slot);

            // segundo lo añadimos al turno
            turnos.add(turnoString.toString());
        }

        */

    }

    /**
     * Metodo utilizado para rellenar las plantillas incompletas con descansos.
     *
     * @param plantilla Plantilla 3x1.
     * @return Plantilla con un slot de descanso mas en los 3 turnos.
     */
    private static ArrayList<ArrayList<String>> introducirDescanso(ArrayList<ArrayList<String>> plantilla) {
        ArrayList<String> c1 = plantilla.get(plantilla.size() - 3);
        ArrayList<String> c2 = plantilla.get(plantilla.size() - 2);
        ArrayList<String> c3 = plantilla.get(plantilla.size() - 1);
        c1.add(STRING_DESCANSO);
        c2.add(STRING_DESCANSO);
        c3.add(STRING_DESCANSO);
        plantilla.set(plantilla.size() - 3, c1);
        plantilla.set(plantilla.size() - 2, c2);
        plantilla.set(plantilla.size() - 1, c3);
        return plantilla;
    }

    /**
     * Metodo utilizado para la introduccion en las plantillas de trabajo el ID del sector que se cubrira con la
     * plantilla.
     *
     * @param plantilla Plantilla 3x1.
     * @param desc      Longitud del periodo de descanso.
     * @param id        ID del sector
     * @return Plantilla completa.
     */
    private static ArrayList<ArrayList<String>> introducirSector(ArrayList<ArrayList<String>> plantilla, int desc,
                                                                 String id) {
        ArrayList<String> c1 = plantilla.get(plantilla.size() - 3);
        ArrayList<String> c2 = plantilla.get(plantilla.size() - 2);
        ArrayList<String> c3 = plantilla.get(plantilla.size() - 1);
        int j = c1.size();
        while (j >= (desc * 3)) {
            j = j - desc * 3;
        }
        if (j < desc) {
            c1.add(id.toLowerCase());
            c2.add(id.toUpperCase());
            c3.add(STRING_DESCANSO);
        } else if (j < desc * 2) {
            c1.add(id.toUpperCase());
            c2.add(STRING_DESCANSO);
            c3.add(id.toLowerCase());
        } else if (j < desc * 3) {
            c1.add(STRING_DESCANSO);
            c2.add(id.toLowerCase());
            c3.add(id.toUpperCase());
        }
        plantilla.set(plantilla.size() - 3, c1);
        plantilla.set(plantilla.size() - 2, c2);
        plantilla.set(plantilla.size() - 1, c3);
        return plantilla;
    }

    public static ArrayList<String> reparacionSoluciones2(Entrada entrada, Parametros parametros,
                                                          ArrayList<String> turnos, int minT, Patrones patrones) {
        String turno = "";
        ArrayList<String> turno2 = new ArrayList<>();
        ArrayList<ArrayList<String>> turnos2 = new ArrayList<>();
        for (int i = 0; i < turnos.size(); i++) {
            turno = turnos.get(i);
            for (int j = 0; j < turno.length(); j += 3) {
                turno2.add(turno.substring(j, j + 3));
            }
            turnos2.add(turno2);
            turno2 = new ArrayList<>();
        }
        ArrayList<ArrayList<String>> cadenasDeTurnos = reparacionSoluciones(entrada, parametros, turnos2, minT,
                patrones);
        return transformacionSoluciones(cadenasDeTurnos);
    }

}
