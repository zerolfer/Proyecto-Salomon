package InicializarPoblacion;

import estructurasDatos.Auxiliares.ObjAux1;
import estructurasDatos.DominioDelProblema.*;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import main.MainPruebas;
import org.apache.commons.lang3.StringUtils;
import patrones.Patrones;

import java.util.*;

import static herramientas.CridaUtils.*;

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
    public static final int descanso = 6; // {LEGACY: Ajustado experimentalmente}

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

        Solucion individuo = inicializarIndividuo(descanso, maxT, minT, minD, entrada, p, patrones);
        poblacion = comprobarCondicionesEntorno(individuo, poblacion, entrada, patrones, p);

        System.out.println("La poblacion inicial es de: " + poblacion.size() + " individuo(s)");
        MainPruebas.problema += ("La poblacion inicial es de: " + poblacion.size() + " individuo(s)" + "\n");
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

        // FASE 1
        Solucion individuo = entrada.getDistribucionInicial().clone();

        if (entrada.getSectorizacionModificada() != null) {

            // < PASO 1 > [ + cambio por afines si es posible ]
            Set<Sector> nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad =
                    eliminarSectoresCerrados(entrada.getSlotMomentoActual(), entrada.getSectorizacion(),
                            entrada.getSectorizacionModificada(), entrada.getControladores(),
                            entrada.getListaNuevosSectoresAbiertosTrasMomentoActual(), individuo, entrada.getMapaAfinidad());
            // < PASO 2 >
            introducirPlantillasNuevosSectores(entrada, nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad,
                    individuo, descanso, maxT, minT, minD);
        }

        // < PASO 3 >
        eliminarControladoresBaja(entrada, individuo);
        // < PASO 4 >
        anadirControladoresAlta(entrada, individuo);
        // FUTURE: < PASO 5 >
//        eliminarImaginariosSiEsPosible(...);

        // FASE 2
        //individuo.setTurnos(reparacionSolucionesAdapter(entrada, p, individuo.getTurnos(), minT, patrones));

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
        String t = turnos.get(0);
        //TODO: PROBAR QUE LOS CORTES EN LA CADENA SEAN CORRECTOS
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getBajaAlta() == Propiedades.ALTA && controladores.get(i).getSlotBajaAlta() != 0) {
                int momentoAlta = controladores.get(i).getSlotBajaAlta() * LONGITUD_CADENAS;
                String turno = "";
                for (int j = 0; j < t.length(); j += LONGITUD_CADENAS) {
                    if (j < momentoAlta) {
                        turno += STRING_NO_TURNO;
                    } else {
                        turno += STRING_DESCANSO;
                    }
                }
                turnos.add(turno);
                controladores.get(i).setTurnoAsignado(turnos.size() - 1);
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
                String cadFin = t.substring(momentoBaja * LONGITUD_CADENAS);
                StringBuilder cadIni = new StringBuilder(t.substring(0, momentoBaja * LONGITUD_CADENAS));
                for (int j = momentoBaja * LONGITUD_CADENAS; j < t.length(); j += LONGITUD_CADENAS) {
                    cadIni.append(STRING_NO_TURNO);
                }
                String tmp = "";
                for (int j = 0; j < momentoBaja * LONGITUD_CADENAS; j += LONGITUD_CADENAS) {
                    if (entrada.getSlotMomentoActual() * LONGITUD_CADENAS > j) {
                        tmp += STRING_NO_TURNO;
                    } else {
                        tmp += STRING_DESCANSO;
                    }
                }
                cadFin = tmp + cadFin;
                turnos.set(c.getTurnoAsignado(), cadIni.toString());
                turnos.add(cadFin);
            }
        }

    }

    /*
     * Importante: modifica la lista de sectores ( entrada.getListaNuevosSectoresAbiertosTrasMomentoActual() )
     */
    private static Set<Sector> eliminarSectoresCerrados(int slotMomentoActual, ArrayList<Set<String>> sectorizacion,
                                                        ArrayList<Set<String>> sectorizacionModificada,
                                                        List<Controlador> controladores, List<Sector> sectores,
                                                        Solucion distribucionInicial, Map<String, Set<String>> mapaAfinidad) {

        Collection<String> sectoresCerrados = null;
        Collection<String> sectoresQueSeAbren = null;
        Set<Sector> nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad = new HashSet<>(sectores);
        boolean hayCambio = false;
        int idxInicio = slotMomentoActual;

        // iteramos por todos los slots de la instancia del problema
        for (int slot = slotMomentoActual; slot < sectorizacion.size(); slot++) {

            // Solamente vamos a computar los slots en los que haya cambio en los sectores cerrados
            // Recorremos la distribución de trozo en trozo, para mayor eficiencia
            if (sectoresCerrados == null  // si estamos al inicio
                    || !sectorizacionModificada.get(slot).equals(sectorizacionModificada.get(slot - 1)) // si hay cambio
                    || !sectorizacion.get(slot).equals(sectorizacion.get(slot - 1))) {

                // si ya se habia alcanzado un cambio, modificamos los turnos en el intervalo que va
                // desde el slot donde se produjo dicho cambio, hasta el actual
                if (hayCambio) {
                    modificarTurno(idxInicio, slot, sectoresCerrados, sectoresQueSeAbren,
                            controladores, sectores, distribucionInicial, mapaAfinidad,
                            nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad);
                    hayCambio = false;
                }

                // como hay un cambio, obtenemos los nuevos sectores cerrados, será una lista vacia o no
                sectoresCerrados = obtenerSectoresCerrados(sectorizacion.get(slot), sectorizacionModificada.get(slot));
                sectoresQueSeAbren = obtenerNuevosSectoresAbiertos(sectorizacion.get(slot), sectorizacionModificada.get(slot), sectores);

                // como ha habido un cambio, lo marcamos en la baliza hayCambio,
                // y guardamos la posicion (numero del slot) de dicho cambio para
                // utilizarlo en el if anterior la proxima vez que se detecte un cambio
                if (sectoresCerrados.size() > 0 || sectoresQueSeAbren.size() > 0) {
                    hayCambio = true;
                    idxInicio = slot;
                }
            }
        }
        // si solo hay un unico cambio en toda la sectorización, el cambio no habrá sido computado, lo computamos
        if (hayCambio) {
            modificarTurno(idxInicio, sectorizacion.size(), sectoresCerrados, sectoresQueSeAbren,
                    controladores, sectores, distribucionInicial, mapaAfinidad, nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad);
        }
        return nuevosSectoresQueNoHayaSidoAsignadosPorAfinidad;
    }

    /**
     * Actualiza el turno (el String) <b>de todos los controladores</b> dentro del intervalo de tiempo
     * (en slots de 3 caracteres cada uno) fijado en los parámetros <br/>
     * Importante: este método MODIFICA LA <code>distribucionInicial<code/> pasada como argumento
     * y la lista de sectores ( entrada.getListaNuevosSectoresAbiertosTrasMomentoActual() )
     *
     * @param slotInicio                                     inicio del intervalo (numero de slot)
     * @param slotFin                                        final del intervalo (numero de slot)
     * @param sectoresCerrados                               lista de sectores cerrados en el intervalo
     * @param nuevosSectoresQueNoHanSidoAsignadosPorAfinidad
     */
    private static void modificarTurno(int slotInicio, int slotFin, final Collection<String> sectoresCerrados,
                                       final Collection<String> sectoresQueSeAbren,
                                       List<Controlador> controladores, List<Sector> sectoresAbiertosTrasMomentoActual,
                                       Solucion distribucionInicial, Map<String, Set<String>> mapaAfinidad,
                                       Set<Sector> nuevosSectoresQueNoHanSidoAsignadosPorAfinidad) {
        // para cada sector que se cierra
        for (String sectorCerrado : sectoresCerrados) {
//            Pattern patron = Pattern.compile("(?i)" + sectorCerrado);

            String stringParaSustituir = ""; // por defecto, si no hay afinidades, se cambia por descanso
            String afin = buscarSectorAfin(sectorCerrado, sectoresQueSeAbren, mapaAfinidad);
            Sector sectorAfin = null;
            if (!afin.equals("")) {
                sectorAfin = CridaUtils.findSectorById(sectoresAbiertosTrasMomentoActual, afin);
                if (sectoresAbiertosTrasMomentoActual.contains(sectorAfin))
                    stringParaSustituir = afin;
            }

            // para cada uno de los turnos de la distribucion inicial
            for (int j = 0; j < distribucionInicial.getTurnos().size(); j++) {
                String s = distribucionInicial.getTurnos().get(j); // turno original

                if (!(contains(s, sectorCerrado, slotInicio, slotFin)
                        && checkAcreditaciones(sectorAfin, controladores, afin, j))) // TODO: segurarse de que el check es necesario o no!!!!! FIXME: Pendiente de CRIDA !!!
                    continue;

                String previo = s.substring(0, slotInicio * LONGITUD_CADENAS);
                String medio;

                // si no hay sector afín, lo sustituye por descansos
                if (stringParaSustituir.equals(""))
                    medio = StringUtils.replaceIgnoreCase(
                            s.substring(slotInicio * LONGITUD_CADENAS, slotFin * LONGITUD_CADENAS), sectorCerrado, STRING_DESCANSO
                    ); // sustituimos todas las apariciones del sector (ya sea mayus o minus) por descansos

                    // pero si sí hay un afín aun no utilizado, se sustituye por ese
                else {
                    medio = reemplazarPorAfin(s, slotInicio, slotFin, sectorCerrado,
                            stringParaSustituir);
                    nuevosSectoresQueNoHanSidoAsignadosPorAfinidad.remove(sectorAfin);
                    sectoresQueSeAbren.remove(afin);
                }

                String posterior = s.substring(slotFin * LONGITUD_CADENAS);
                distribucionInicial.getTurnos().set(j, previo + medio + posterior);  // recomponemos y actualizamos el turno
            }
        }
    }

    private static boolean contains(String string, String sectorCerrado, int slotInicio, int slotFin) {
        return StringUtils.containsIgnoreCase(
                string.substring(slotInicio * LONGITUD_CADENAS, slotFin * LONGITUD_CADENAS), sectorCerrado
        );
    }

    /*
     * TODO: ASEGURARSE DE SI ES REALMENTE NECESARIO ESTA COMPROBACIÓN O NO (Preguntar a CRIDA)
     * FIXME: ES REALMENTE NECESARIO ESTA COMPROBACIÓN?!?!
     */
    private static boolean checkAcreditaciones(Sector sectorAfin, List<Controlador> controladores, String afin, int indice) {

        Controlador controlador = controladores.get(indice);
        if (controlador.getTurnoAsignado() != indice) // esto ahorra tiempo de cómputo innecesario en algunos casos 
            controlador = CridaUtils.obtenerControladorTurno(indice, controladores);

        if (controlador != null && controlador.isCON() && !sectorAfin.isRuta())
            return false;
        return true;
    }

    private static String reemplazarPorAfin(String s, int slotInicio, int slotFin,
                                            String sectorCerrado, String stringParaSustituir) {

        String medio = s.substring(slotInicio * LONGITUD_CADENAS, slotFin * LONGITUD_CADENAS);
        medio = StringUtils.replace(medio, sectorCerrado, stringParaSustituir); // vienen en minúscula por defecto
        medio = StringUtils.replace(medio, sectorCerrado.toUpperCase(), stringParaSustituir.toUpperCase());
        return medio;

    }

    private static String buscarSectorAfin(String sectorCerrado, Collection<String> sectores,
                                           Map<String, Set<String>> mapaAfinidad) {
        if (sectores.contains(sectorCerrado)) return sectorCerrado; // [Caso 4]
        for (String sectorAbierto : sectores) {
            if (CridaUtils.esAfin(sectorCerrado, sectorAbierto, mapaAfinidad))
                return sectorAbierto;
        }
        return "";
    }

    private static Set<String> obtenerSectoresCerrados(Set<String> iniciales, Set<String> modificados) {
        /*
         * iniciales - modificados = cerrados
         *  {a,b,c}  -   {a,c,d,e}     =     {b}
         */

        return diferenciaConjuntos(iniciales, modificados);
    }

    private static Set<String> obtenerNuevosSectoresAbiertos(Set<String> iniciales, Set<String> modificados, List<Sector> sectores) {
        /*
         * modificados - iniciales = nuevos abiertos
         *  {a,c,d,e}  -   {a,b,c}     =     {d,e}
         */
        return diferenciaConjuntos(modificados, iniciales)/*.stream().filter(id -> {
            for (Sector s : sectores) {
                if (id.equals(s.getId()))
                    return true;
            }
            return false;
        }).collect(Collectors.toSet())*/;
    }

//    private static List<List<String>> obtenerNuevosSectoresAbiertos(ArrayList<ArrayList<String>> sectorizacionInicial, ArrayList<ArrayList<String>> sectorizacionModificada) {
//        /*
//         * modificados - iniciales = nuevos abiertos
//         *  {a,c,d,e}  -   {a,b,c}     =     {d,e}
//         */
//        HashSet<List<String>> set = new HashSet<>();
//        for (int i = 0; i < sectorizacionInicial.size(); i++) {
//            List<String> iniciales = sectorizacionInicial.get(i);
//            List<String> modificados = sectorizacionModificada.get(i);
//            set.add(diferenciaConjuntos(modificados, iniciales));
//        }
//        return new ArrayList<>(set);
//    }

    private static Set<String> diferenciaConjuntos(Set<String> c1, Set<String> c2) {

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
        Set<String> res = new HashSet<>();
        /*List<String> saltar = new ArrayList<>();*/
        // TODO: Alternativa? Registro índices que ya sabemos seguro que no está porque ya han sido usados? mas/menos eficiente?

        for (String el1 : c1) {
            boolean encontrado = false;
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
        Set<String> res2 = new HashSet<>(c1);
        res2.removeIf(c2::contains);
        assert res.equals(res2);
        return res2;

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
     * @param entrada  Entrada del problema
     * @param p        Parametros del problema.
     * @param minT     Tiempo de trabajo minimo.
     * @param patrones Patrones para comprobacion de restricciones.
     * @return Lista de los turnos de trabajo con las modificaciones pertenentes reducir la infactibilidad.
     */
    public static ArrayList<ArrayList<String>> reparacionSoluciones(Entrada entrada, Parametros p,
                                                                    ArrayList<ArrayList<String>> cadenasDeTurnos,
                                                                    int minT, Patrones patrones) {

        /*
        {   LEGACY  }
        */
        for (int i = 0; i < cadenasDeTurnos.size(); i++) {
            ArrayList<String> turno = cadenasDeTurnos.get(i);
            int cont = 0;
            int limit = 0;
            for (int l = 0; l < turno.size(); l++) {
                if (!turno.get(l).equals(STRING_DESCANSO)) {
                    String ant = turno.get(l);
                    if (l + 1 < turno.size() && ant.equals(turno.get(l + 1))) {
                        cont++;
                    } else {
                        if (cont < minT - 1 && l >= entrada.getSlotMomentoActual()) { //No se cumple el trabajo minimo
                            // NOTE: Modificada condición
                            // TODO: comprobar si la condicion es correcta

                            cadenasDeTurnos = ArreglarSoluciones.arregloTrabajoMinimo(entrada, p, cadenasDeTurnos, i,
                                    turno, l, ant, patrones);
                            limit++;
                            if (limit > 100) {
                                break;
                            }
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
     * @param entrada                                        Entrada del problema.
     * @param nuevosSectoresQueYaHanSidoAsignadosPorAfinidad
     * @param descanso                                       Tamaño del descanso para la plantilla.
     * @param maxT                                           Tiempo de trabajo maximo.
     * @param minT                                           Tiempo de trabajo minimo.
     * @param minD                                           Tiempo de descanso minimo.
     * @return Lista de turnos de trabajo.
     */
    private static void introducirPlantillasNuevosSectores(Entrada entrada,
                                                           Set<Sector> nuevosSectoresQueYaHanSidoAsignadosPorAfinidad,
                                                           Solucion distribucion, int descanso, int maxT, int minT, int minD) {
        ArrayList<String> turnos = distribucion.getTurnos(); // esto es la matriz de trabajo

        ArrayList<Set<String>> sectorizacionPorSlots = entrada.getSectorizacionModificada();
//        List<Sector> sectoresNuevosAbiertosTrasMomentoActual = entrada.getListaNuevosSectoresAbiertosTrasMomentoActual();
        ArrayList<Integer> secNoc = new ArrayList<>();

        for (Sector sector : nuevosSectoresQueYaHanSidoAsignadosPorAfinidad) {
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

                    if (j < entrada.getSlotMomentoActual()) {
                        plantilla = introducirCeros(plantilla);
                        continue;
                    }

                    Set<String> sectoresAbiertos = sectorizacionPorSlots.get(j);
                    boolean open = false;
                    for (/*int k = 0; k < sectoresAbiertos.size(); k++*/String sectAbiert : sectoresAbiertos) {
                        if (sector.getId().equalsIgnoreCase(/*sectoresAbiertos.get(k)*/sectAbiert)) {
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
                    plantilla = introducirSectorNoche(plantilla, nuevosSectoresQueYaHanSidoAsignadosPorAfinidad,
                            sector.getNoche(), sectorizacionPorSlots);
                }
            }

            // meter la plantilla a los turnos de la instancia, actualizando el formato
            introducirEnTurnos(plantilla, turnos);
//            controladores.add(new Controlador()) // TODO: AÑADIR CONTROLADORES IMAGINARIOS
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
                                                                      Set<Sector> sectores, int noche,
                                                                      ArrayList<Set<String>> sectorizacion) {
        ArrayList<String> c4 = new ArrayList<>();
        plantilla.add(c4);
        /*Lista de sectores noche = int noche*/
        ArrayList<Sector> sectoresNocturnos = new ArrayList<>();
        for (Sector sectore : sectores) {
            if (sectore.getNoche() == noche) {
                sectoresNocturnos.add(sectore);
            }
        }
        for (Set<String> sectoresAbiertos : sectorizacion) {
            for (/*int k = 0; k < sectoresAbiertos.size(); k++*/String sectorAbierto : sectoresAbiertos) {
                for (Sector sectoresNocturno : sectoresNocturnos) {
                    if (sectoresNocturno.getId().equalsIgnoreCase(sectorAbierto)) {
                        plantilla = introducirSlotSectorNocturno(plantilla, sectoresNocturno.getId());
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
            pseudocontrolador.forEach(turnoString::append);

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
        return introducirString(plantilla, STRING_DESCANSO);
    }

    /**
     * Metodo utilizado para rellenar las plantillas incompletas con ceros, que indican que estan fuera del
     * turno, y no pueden ser modificados.
     *
     * @param plantilla Plantilla 3x1.
     * @return Plantilla con un slot de ceros mas en los 3 turnos.
     */
    private static ArrayList<ArrayList<String>> introducirCeros(ArrayList<ArrayList<String>> plantilla) {
        return introducirString(plantilla, STRING_NO_TURNO);
    }


    private static ArrayList<ArrayList<String>> introducirString(ArrayList<ArrayList<String>> plantilla,
                                                                 final String STRING) {
        ArrayList<String> c1 = plantilla.get(plantilla.size() - 3);
        ArrayList<String> c2 = plantilla.get(plantilla.size() - 2);
        ArrayList<String> c3 = plantilla.get(plantilla.size() - 1);
        c1.add(STRING);
        c2.add(STRING);
        c3.add(STRING);
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

    public static ArrayList<String> reparacionSolucionesAdapter(Entrada entrada, Parametros parametros,
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
        ArrayList<ArrayList<String>> cadenasDeTurnos = reparacionSoluciones(entrada, parametros, turnos2, minT, patrones);

        return transformacionSoluciones(cadenasDeTurnos);
    }

}
