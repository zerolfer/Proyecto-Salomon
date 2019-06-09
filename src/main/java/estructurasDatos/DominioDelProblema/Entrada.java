package estructurasDatos.DominioDelProblema;

import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;
import fitnessFunction.Fitness;
import herramientas.CridaUtils;
import org.apache.commons.lang3.StringUtils;
import rwFiles.Lectura;

import java.util.*;

import static herramientas.CridaUtils.STRING_DESCANSO;
import static herramientas.CridaUtils.STRING_SEPARADOR_CSV;


/**
 * Objeto Entrada, el cual contiene toda la informacion que debe ser introducida como entrada al programa para la resolucion del problema.
 *
 * @author Tino
 */
public class Entrada {

    private final Solucion distribucionInicial;
    /**
     * <p>
     * Lista de los sectores que se abren en la nueva sectorización.
     * Incluye aquellos sectores que, si bien no son nuevos,
     * se abren en un momento diferente en la nueva sectorización
     * </p>
     * <p>
     * (Son tratados como nuevos en
     * {@link InicializarPoblacion#introducirPlantillasNuevosSectores})
     * </p>
     * <p>
     * Inicializado en {@link #crearListaNuevosSectoresAbiertos}
     * </p>
     */
    private final ArrayList<Sector> listaNuevosSectoresAbiertosTrasMomentoActual;
    /**
     * Lista con los controladores disponibles y sus caracteristicas.
     */
    private ArrayList<Controlador> controladores;
    /**
     * Nucleos con sectores abiertos para la instacia del problema a resolver.
     */
    private ArrayList<Nucleo> nucleos;
    /**
     * Turno a resolver
     */
    private Turno turno;
    /**
     * Lista de todos los sectores que componen el espacio aereo del aeropuerto.
     */
    private ArrayList<Sector> listaSectores;
    /**
     * Sectorizacion utilizada en la instancia del problema. La sectorizacion es el conjunto de sectores que se abren y cierran durante la duracion del turno.
     */
    private ArrayList<Set<String>> sectorizacion;
    /**
     * Indica los sectores que tienen afinidad entre si.
     */
    private ArrayList<ArrayList<String>> matrizAfinidad;
    private Map<String, Set<String>> mapaAfinidad;
    /**
     * Lista de los sectores operativos en la instancia del problema.
     */
    private ArrayList<Sector> listaSectoresAbiertos;
    /**
     * Lista con todos los sectores y los volumenes asociados a estos.
     */
    private HashMap<Sector, ArrayList<String>> volumnsOfSectors;
    /**
     * Carga de trabajo total representada en slots.
     */
//    private int cargaTrabajo;
    private ArrayList<Set<String>> sectorizacionModificada;
    private int slotMomentoActual;

    /**
     * Contructor
     *
     * @param controladores           Lista con los controladores disponibles y sus caracteristicas.
     * @param nucleos                 Nucleos con sectores abiertos para la instacia del problema a resolver.
     * @param turno                   Turno a resolver.
     * @param listaSectores           Lista de todos los sectores que componen el espacio aereo del aeropuerto.
     * @param listaSectoresAbiertos   Lista de los sectores operativos en la instancia del problema.
     * @param sectorizacion           Sectorizacion utilizada en la instancia del problema. La sectorizacion es el conjunto de sectores que se abren y cierran durante la duracion del turno.
     * @param matrizAfinidad          Indica los sectores que tienen afinidad entre si.
     * @param volumnsOfSectors        Lista con todos los sectores y los volumenes asociados a estos.
     * @param cargaTrabajo            Carga de trabajo total representada en slots.
     * @param sectorizacionModificada
     */
    public Entrada(ArrayList<Controlador> controladores, ArrayList<Nucleo> nucleos, Turno turno,
                   ArrayList<Sector> listaSectores, ArrayList<Sector> listaSectoresAbiertos,
                   ArrayList<Sector> listaNuevosSectoresAbiertosTrasMomentoActual, ArrayList<Set<String>> sectorizacion,
                   ArrayList<ArrayList<String>> matrizAfinidad, Map<String, Set<String>> mapaAfinidad,
                   HashMap<Sector, ArrayList<String>> volumnsOfSectors, /*int cargaTrabajo,*/
                   ArrayList<Set<String>> sectorizacionModificada,
                   Solucion distribucionInicial, int slotMomentoActual) {
        this.controladores = controladores;
        this.nucleos = nucleos;
        this.turno = turno;
        this.listaSectores = listaSectores;
        this.sectorizacion = sectorizacion;
        this.matrizAfinidad = matrizAfinidad;
        this.mapaAfinidad = mapaAfinidad;
        this.listaSectoresAbiertos = listaSectoresAbiertos;
        this.listaNuevosSectoresAbiertosTrasMomentoActual = listaNuevosSectoresAbiertosTrasMomentoActual;
        this.volumnsOfSectors = volumnsOfSectors;
//        this.cargaTrabajo = cargaTrabajo;
        this.sectorizacionModificada = sectorizacionModificada;
        this.distribucionInicial = distribucionInicial;
        this.slotMomentoActual = slotMomentoActual;
    }


    public static Entrada leerEntrada(Parametros parametros, String path, String entradaId, String entorno) {
        ArrayList<String> fAperturaSectores = Lectura.Listar("entrada/Casos/" + path + "/AperturaSectorizaciones_" + entradaId + ".csv");
        ArrayList<String> fRecursosDisponibles = Lectura.Listar("entrada/Casos/" + path + "/RecursosDisponibles_" + entradaId + ".csv");
        ArrayList<String> fTurno = Lectura.Listar("entrada/Casos/" + path + "/Turno_" + entradaId + ".csv");
        ArrayList<String> fModificacionSectores = Lectura.Listar("entrada/Casos/" + path + "/ModificacionSectorizaciones_" + entradaId + ".csv", true);

        ArrayList<String> fModificacionRecursos = Lectura.Listar("entrada/Casos/" + path + "/ModificacionRecursos_" + entradaId + ".csv", true);
        ArrayList<String> fDistribucionInicial = Lectura.Listar("entrada/Casos/" + path + "/DistribucionInicial_" + entradaId + ".csv");


        ArrayList<String> fListaSectoresElementales = Lectura.Listar("entrada/" + entorno + "/ListaSectoresElementales_" + entorno + ".csv");
        ArrayList<String> fMatrizAfinidad = Lectura.Listar("entrada/" + entorno + "/MatrizAfinidad_" + entorno + ".csv");
        ArrayList<String> fSectoresNucleos = Lectura.Listar("entrada/" + entorno + "/SectoresNucleos_" + entorno + ".csv");
        ArrayList<String> fSectorizacionSectoresVolumenes = Lectura.Listar("entrada/" + entorno + "/SectorizacionesSectoresVolumenes_" + entorno + ".csv");

        ArrayList<Controlador> controladores = crearControladores(fRecursosDisponibles);
        ArrayList<Sector> listaSectores = crearListaSectores(fSectoresNucleos, fListaSectoresElementales);
        ArrayList<Nucleo> nucleos = crearNucleos(fSectoresNucleos, listaSectores);
        Map<String, Set<String>> mapaAfinidad = crearMapaAfinidad(fMatrizAfinidad, listaSectores);
        ArrayList<ArrayList<String>> matrizAfinidad = crearMatrizAfinidad(fMatrizAfinidad, listaSectores);


        Turno turno = crearTurno(fTurno, parametros);
        ArrayList<Set<String>> sectorizacion = crearSectorizacion(fAperturaSectores, fSectorizacionSectoresVolumenes, turno, listaSectores);
        ArrayList<Set<String>> sectorizacionModificada = null;
        ArrayList<Sector> listaNuevosSectoresAbiertosTrasMomentoActual = null; // Hay que verificar que son NULL o no al acceder a ellos

        int slotMomentoActual = crearMomentoActual(turno, fDistribucionInicial);

        if (!fModificacionSectores.isEmpty()) {
            sectorizacionModificada = crearSectorizacion(fModificacionSectores, fSectorizacionSectoresVolumenes, turno, listaSectores);
            listaNuevosSectoresAbiertosTrasMomentoActual =
                    crearListaNuevosSectoresAbiertos(slotMomentoActual, sectorizacion, sectorizacionModificada, listaSectores);
        }
        if (!fModificacionRecursos.isEmpty()) {
            modificarControladores(controladores, fModificacionRecursos, turno);
        }

        ArrayList<Sector> listaSectoresAbiertos =
                crearListaSectoresAbiertos(slotMomentoActual, sectorizacion, listaSectores);

        HashMap<Sector, ArrayList<String>> volumnsOfSectors = crearHashMapSectoresVolumenes(listaSectoresAbiertos, fSectorizacionSectoresVolumenes);
//        int cargaTrabajo = calcularCargaTrabajo(sectorizacion, controladores, listaNuevosSectoresAbiertosTrasMomentoActual);

        Solucion distribucionInicial = crearSolucionInicial(fDistribucionInicial, listaSectores, controladores, parametros);

        calcularCargaTrabajo(sectorizacion, controladores, listaSectoresAbiertos);

        Entrada entrada = new Entrada(controladores, nucleos, turno, listaSectores, listaSectoresAbiertos, listaNuevosSectoresAbiertosTrasMomentoActual,
                sectorizacion, matrizAfinidad, mapaAfinidad,
                volumnsOfSectors, sectorizacionModificada, distribucionInicial, slotMomentoActual);

        return entrada;
    }

    private static int crearMomentoActual(Turno turno, ArrayList<String> fDistribucionInicial) {
        String[] linea = fDistribucionInicial.get(0).split(";");
        return calcularSlot(turno, linea[1]);
    }

    private static Solucion crearSolucionInicial(List<String> entrada, ArrayList<Sector> listaSectores,
                                                 ArrayList<Controlador> controladores, Parametros parametros) {
        ArrayList<String> turnos = new ArrayList<>();
        List<Integer> intervalos = new ArrayList<>();
        for (int i = 1; i < entrada.size(); i++) {
            String[] columnas = entrada.get(i).split(";");
            if (columnas[0].contains("-")) // (Si se cambia por "equals" no funciona)
                intervalos = actualizarIntervalos(columnas);
            else { // en caso contrario, creamos el turno y lo asignamos al controlador de la primera columna
                String turno = crearDistribucionDelTurno(intervalos, columnas, listaSectores, parametros);
                asignarControlador(Integer.parseInt(columnas[0].substring(1)), turnos.size(), controladores);
                turnos.add(turno);
            }
        }
        return new Solucion(turnos, controladores, 0);
    }

    private static String crearDistribucionDelTurno(List<Integer> intervalos, String[] columnas,
                                                    List<Sector> listaSectores, Parametros parametros) {
        StringBuilder resultado = new StringBuilder();
        assert intervalos.size() == columnas.length - 1;

        // saltamos la primera columna
        for (int i = 1; i < columnas.length; i++) {
            int intervaloActual = intervalos.get(i - 1); // restamos uno debido a la disonancia de las dos listas
            String idSector = STRING_DESCANSO;
            if (!columnas[i].contains(STRING_DESCANSO)) {
                idSector = obtenerIdSector(columnas[i], listaSectores);
                idSector = Character.isUpperCase(columnas[i].charAt(0)) ? idSector.toUpperCase() : idSector/*.toLowerCase()*/;
            }
            resultado.append(StringUtils.repeat(idSector, intervaloActual / parametros.getTamanoSlots()));
        }

        return resultado.toString();
    }

    /**
     * Obtiene el identificador de un sector a partir de su nombre
     *
     * @param nombreSector  nombre del sector que queremos buscar
     * @param listaSectores lista de sectores de la instancia actual del problema
     * @return id del sector
     */
    private static String obtenerIdSector(String nombreSector, List<Sector> listaSectores) {
        for (Sector sector : listaSectores) {
            if (sector.getNombre().equalsIgnoreCase(nombreSector))
                return sector.getId();
        }
        throw new RuntimeException("No encontrado Sector con nombre " + nombreSector);
    }

    private static void asignarControlador(int idControlador, int indice, List<Controlador> controladores) {
        for (Controlador controlador : controladores) {
            if (controlador.getId() == idControlador) {
                controlador.setTurnoAsignado(indice);
                return;
            }
        }
    }

    private static List<Integer> actualizarIntervalos(String[] columnas) {
        List<Integer> result = new ArrayList<>();
        // saltamos la primera columna
        for (int i = 1; i < columnas.length; i++) {
            result.add(Integer.parseInt(columnas[i]));
        }
        return result;
    }

    private static void modificarControladores(ArrayList<Controlador> controladores,
                                               ArrayList<String> entrada, Turno turno) {
        for (int i = 1; i < entrada.size(); i++) {
            String[] linea = entrada.get(i).split(";");
            Controlador c = null;
            boolean existe = false;
            int lineaId = Integer.parseInt(linea[1].substring(1));
            for (int j = 0; j < controladores.size(); j++) {
                if (controladores.get(j).getId() == lineaId) {
                    existe = true;
                    c = controladores.get(j);
                    c.setBajaAlta(linea[0].equals("ALTA") ? Propiedades.ALTA : Propiedades.BAJA);
                    c.setSlotBajaAlta(calcularSlot(turno, linea[2]));
                    controladores.set(j, c);
                }

            }
            if (!existe) {
                if (linea[3].equalsIgnoreCase("PTD")) {
                    c = (new Controlador(lineaId, linea[5], linea[4], true, false,
                            false, Propiedades.ALTA, calcularSlot(turno, linea[2])));
                } else if (linea[3].equalsIgnoreCase("CON")) {
                    c = (new Controlador(lineaId, linea[5], linea[4], false, true,
                            false, Propiedades.ALTA, calcularSlot(turno, linea[2])));
                }

                controladores.add(c);
            }

        }
    }

    private static int calcularSlot(Turno turno, String momento) {
        int[] distancia = Turno.turnosSlots(turno.getInicioTL(), turno.getFinTL(), momento, momento);
        return distancia[2];
    }


    private static int calcularCargaTrabajo(ArrayList<Set<String>> sectorizacion, ArrayList<Controlador> controladores, ArrayList<Sector> listaSectoresAbiertos) {
        int c = 0;
        for (int i = 0; i < sectorizacion.size(); i++) {
            c += sectorizacion.get(i).size();
        }

        Fitness.setCtrlsCompletos((c * 2) / sectorizacion.size());
        return (c * 2);
    }

    private static HashMap<Sector, ArrayList<String>> crearHashMapSectoresVolumenes(ArrayList<Sector> listaSectoresAbiertos, ArrayList<String> fSectorizacionSectoresVolumenes) {
        HashMap<Sector, ArrayList<String>> volumnsOfSectors = new HashMap<>();
        for (int i = 0; i < listaSectoresAbiertos.size(); i++) {
            Sector s = listaSectoresAbiertos.get(i);
            volumnsOfSectors.put(s, new ArrayList<String>());
            for (int j = 1; j < fSectorizacionSectoresVolumenes.size(); j++) {
                if (s.getNombre().equalsIgnoreCase(fSectorizacionSectoresVolumenes.get(j).split(";")[1])) {
                    String vol = fSectorizacionSectoresVolumenes.get(j).split(";")[2];
                    boolean esta = false;
                    for (int k = 0; k < volumnsOfSectors.get(s).size(); k++) {
                        if (volumnsOfSectors.get(s).get(k).equalsIgnoreCase(vol)) {
                            esta = true;
                        }
                    }
                    if (!esta) {
                        volumnsOfSectors.get(s).add(vol);
                    }
                }
            }
        }
        return volumnsOfSectors;
    }

    private static ArrayList<Sector> crearListaSectoresAbiertos(int slotMomentoActual, ArrayList<Set<String>> sectorizacion, ArrayList<Sector> listaSectores) {
        List<Sector> sectoresAbiertos = new ArrayList<>();

        // para cada slot
        for (int i = slotMomentoActual; i < sectorizacion.size(); i++) {
            Set<String> slot = sectorizacion.get(i);

            // para cada sector abierto en ese slot
            for (String sct : slot) {

                // Si aun no esta en la lista (lo ponemos primero para mayor eficiencia)
                // No es necesario verificar la capitalización puesto que todos vienen con minúscula
                if (!CridaUtils.containsSectorById/*IngoreCase*/(sectoresAbiertos, sct)) {

                    // buscamos el "Sector" de entre todos los sectores de la instancia del problema
                    Sector s = CridaUtils.findSectorById(listaSectores, sct);

                    // si no existe el sector es que algo va mal
                    if (s == null)
                        throw new RuntimeException("No se encuentra el sector con id \"" + sct + "\"");

                        // pero si todo va bien, entonces lo añadimos a la lista
                    else sectoresAbiertos.add(s.clone());
                }

            }
        }
        return new ArrayList<>(sectoresAbiertos);
    }

    private static ArrayList<Sector> crearListaNuevosSectoresAbiertos(int slotMomentoActual,
                                                                      ArrayList<Set<String>> sectorizacion,
                                                                      ArrayList<Set<String>> sectorizacionModificada,
                                                                      ArrayList<Sector> listaSectores) {
        List<Sector> sectoresAbiertos = new ArrayList<>();

        // para cada slot
        for (int numSlot = slotMomentoActual; numSlot < sectorizacion.size(); numSlot++) {
            Set<String> slot = sectorizacionModificada.get(numSlot);

            // para cada sector abierto en ese slot
            for (String sct : slot) {

                // Si aun no esta en la lista (esta condición primero para mayor eficiencia)
                // (No es necesario verificar la capitalización puesto que todos vienen con minúscula)
                if (!CridaUtils.containsSectorById/*IngoreCase*/(sectoresAbiertos, sct)
                        // Y si tampoco pertenece a la sectorización antigua
                        // (con esta condición, si un sector no-nuevo se abre en la nueva sectorización,
                        // en un momento que antes no lo hacía, también se le considera como nuevo
                        // [y será tratado como los demás: le añadirá plantilla, etc.])
                        && !sectorizacion.get(numSlot).contains(sct)) {

                    // buscamos el "Sector" de entre todos los sectores de la instancia del problema
                    Sector s = CridaUtils.findSectorById(listaSectores, sct);

                    // si no existe el sector es que algo va mal
                    if (s == null)
                        throw new RuntimeException("No se encuentra el sector con id \"" + sct + "\"");

                        // pero si todo va bien, entonces lo añadimos a la lista
                    else sectoresAbiertos.add(s.clone());
                }

            }
        }
        return new ArrayList<>(sectoresAbiertos);
    }


    private static ArrayList<ArrayList<String>> crearMatrizAfinidad(ArrayList<String> entrada, ArrayList<Sector> listaSectores) {
        ArrayList<ArrayList<String>> matrizAfinidad = new ArrayList<>();
        ArrayList<String> filaAf = new ArrayList<>();
        for (int i = 0; i < entrada.size(); i++) {
            String[] fila = entrada.get(i).split(";");
            for (int j = 0; j < fila.length; j++) {
                if (i == 0 || j == 0) {
                    int s = -1;
                    for (int k = 0; k < listaSectores.size(); k++) {
                        if (fila[j].replaceAll("'", "").equalsIgnoreCase(listaSectores.get(k).getNombre())) {
                            s = k;
                        }
                    }
                    if (s == -1) {
                        filaAf.add(fila[j]);
                    } else {
                        filaAf.add(listaSectores.get(s).getId());
                    }
                } else {
                    filaAf.add(fila[j]);
                }
            }
            matrizAfinidad.add(filaAf);
            filaAf = new ArrayList<>();
        }

        return matrizAfinidad;
    }

    private static Map<String, Set<String>> crearMapaAfinidad(ArrayList<String> entrada, ArrayList<Sector> listaSectores) {
        Map<String, Set<String>> mapaAfinidad = new HashMap<>();

        String[] nombres = entrada.get(0).replaceAll("'", "").split(STRING_SEPARADOR_CSV);
        for (int i = 1; i < entrada.size(); i++) {
            Set<String> afines = new HashSet<>();
            String[] fila = entrada.get(i).replaceAll("'", "").split(STRING_SEPARADOR_CSV);
            for (int j = 1; j < fila.length; j++) {
                if (Integer.parseInt(fila[j]) >= 1)
                    afines.add(CridaUtils.findSectorByName(nombres[j], listaSectores).getId());
            }
            mapaAfinidad.put(CridaUtils.findSectorByName(fila[0], listaSectores).getId(), afines);
        }
        return mapaAfinidad;
    }


    private static ArrayList<Set<String>> crearSectorizacion(ArrayList<String> entrada, ArrayList<String> confSectores, Turno turno, ArrayList<Sector> listaSectores) {
        ArrayList<ArrayList<String>> sectorizacionTemp = new ArrayList<>();
        ArrayList<Set<String>> sectorizacion = new ArrayList<>();
        for (int i = 0; i < turno.getTl()[1]; i++) {
            ArrayList<String> slot = new ArrayList<>();
            slot.add(STRING_DESCANSO);
            sectorizacionTemp.add(slot);
        }
        for (int i = 1; i < entrada.size(); i++) {
            String[] linea = entrada.get(i).split(";");
            if (linea[1].equalsIgnoreCase("SECTOR")) {
                sectorizacionTemp = introducirSector(linea, sectorizacionTemp, turno, listaSectores, false);
            } else if (linea[1].equalsIgnoreCase("CONF")) {
                sectorizacionTemp = introducirListaSectores(linea, confSectores, sectorizacionTemp, turno, listaSectores);
            } else if (linea[1].equalsIgnoreCase("SECTORNOCTURNO")) {
                sectorizacionTemp = introducirSector(linea, sectorizacionTemp, turno, listaSectores, true);
            }
        }
        for (int i = 0; i < sectorizacionTemp.size(); i++) {
            ArrayList<String> slot = sectorizacionTemp.get(i);
            slot.remove(0);
            sectorizacion.add(new HashSet<>(slot));
        }

        return sectorizacion;
    }

    private static ArrayList<ArrayList<String>> introducirListaSectores(String[] linea, ArrayList<String> confSectores, ArrayList<ArrayList<String>> sectorizacion, Turno turno, ArrayList<Sector> listaSectores) {
        ArrayList<String> idSector = encontrarConfiguracion(linea, confSectores, listaSectores);
        String iniTL = turno.getInicioTL();

        int l = obtenerLongitud(linea[4], linea[5]);
        if (l < 0) {
            l = obtenerLongitud(linea[4], "24:00:00");
            l += obtenerLongitud("00:00:00", linea[5]);
        }
        int nl = obtenerLongitud(iniTL, linea[4]);
        if (nl < 0) {
            nl = obtenerLongitud(iniTL, "24:00:00");
            nl += obtenerLongitud("00:00:00", linea[4]);
        }

        for (int i = nl; i < l + nl; i++) {
            if (sectorizacion.size() == i) {
                ArrayList<String> lstS = new ArrayList<>(idSector);
                sectorizacion.add(i, lstS);
            } else {
                ArrayList<String> lstS = sectorizacion.get(i);
                lstS.addAll(idSector);
                sectorizacion.set(i, lstS);
            }
        }
        return sectorizacion;
    }

    private static ArrayList<String> encontrarConfiguracion(String[] linea, ArrayList<String> confSectores, ArrayList<Sector> listaSectores2) {
        ArrayList<String> idSectores = new ArrayList<>();
        String cnfS = linea[3];
        String nucleo = linea[0];
        for (int i = 1; i < confSectores.size(); i++) {
            String[] cnfLinea = confSectores.get(i).split(";");
            if (cnfLinea[3].equalsIgnoreCase(nucleo) && cnfLinea[0].equalsIgnoreCase(cnfS)) {
                for (int j = 0; j < listaSectores2.size(); j++) {
                    if (listaSectores2.get(j).getNombre().equalsIgnoreCase(cnfLinea[1])) {
                        if (!idSectores.contains(listaSectores2.get(j).getId())) {
                            idSectores.add(listaSectores2.get(j).getId());
                        }
                    }
                }
            }
        }
        return idSectores;
    }

    private static ArrayList<ArrayList<String>> introducirSector(String[] linea, ArrayList<ArrayList<String>> sectorizacion, Turno turno, ArrayList<Sector> listaSectores, boolean sectorNoc) {
        String idSector = "";
        for (int i = 0; i < listaSectores.size(); i++) {
            if (linea[3].equalsIgnoreCase(listaSectores.get(i).getNombre())) {
                idSector = listaSectores.get(i).getId();
                if (sectorNoc) {
                    listaSectores.get(i).setNoche(Integer.parseInt(linea[2].replaceAll(" ", "")));
                }
                break;
            }
        }
        String iniTL = turno.getInicioTL();
        int l = obtenerLongitud(linea[4], linea[5]);
        if (l < 0) {
            l = obtenerLongitud(linea[4], "24:00:00");
            l += obtenerLongitud("00:00:00", linea[5]);
        }
        int nl = obtenerLongitud(iniTL, linea[4]);
        if (nl < 0) {
            nl = obtenerLongitud(iniTL, "24:00:00");
            nl += obtenerLongitud("00:00:00", linea[4]);
        }
        for (int i = nl; i < l + nl; i++) {
            if (sectorizacion.size() == i) {
                ArrayList<String> lstS = new ArrayList<>();
                lstS.add(idSector);
                sectorizacion.add(lstS);
            } else {
                ArrayList<String> lstS = sectorizacion.get(i);
                lstS.add(idSector);
                sectorizacion.set(i, lstS);
            }
        }
        return sectorizacion;
    }

    private static int obtenerLongitud(String ini, String fin) {
        int iH = Integer.parseInt(ini.split(":")[0]);
        int iM = Integer.parseInt(ini.split(":")[1]);
        int fH = Integer.parseInt(fin.split(":")[0]);
        int fM = Integer.parseInt(fin.split(":")[1]);
        int h = (fH - iH) * 60;
        int m = fM - iM;
        int t = (h + m) / 5;
        return t;
    }

    private static ArrayList<Controlador> crearControladores(ArrayList<String> entrada) {
        ArrayList<Controlador> controladores = new ArrayList<>();
        for (int i = 1; i < entrada.size(); i++) {
            String[] linea = entrada.get(i).split(";");
            if (linea[1].equalsIgnoreCase("PTD")) {
                controladores.add(new Controlador(Integer.parseInt(linea[0].substring(1)), linea[3], linea[2], true, false, false, Propiedades.ALTA, 0));
            } else if (linea[1].equalsIgnoreCase("CON")) {
                controladores.add(new Controlador(Integer.parseInt(linea[0].substring(1)), linea[3], linea[2], false, true, false, Propiedades.ALTA, 0));
            }
        }
        return controladores;
    }

    private static ArrayList<Nucleo> crearNucleos(ArrayList<String> entrada, ArrayList<Sector> listaSectores2) {
        int id = 0;
        ArrayList<Nucleo> nucleos = new ArrayList<>();
        ArrayList<Sector> listaSectores = new ArrayList<>();
        String[] primer = entrada.get(1).split(";");
        for (int i = 2; i < primer.length; i++) {
            for (int j = 2; j < entrada.size(); j++) {
                String[] linea = entrada.get(j).split(";");
                if (linea.length > i && linea[i].equalsIgnoreCase("X")) {
                    for (int k = 0; k < listaSectores2.size(); k++) {
                        if (listaSectores2.get(k).getNombre().equalsIgnoreCase(linea[0])) {
                            listaSectores.add((Sector) listaSectores2.get(k).clone());
                            break;
                        }
                    }
                }
            }
            nucleos.add(new Nucleo(entrada.get(1).split(";")[i], "" + id, listaSectores));
            listaSectores = new ArrayList<>();
            id++;
        }
        return nucleos;
    }

    private static ArrayList<Sector> crearListaSectores(ArrayList<String> entrada, ArrayList<String> fListaSectoresElementales) {
        int cnt = 0;
        String[] idS = {"aaa", "aab", "aac", "aad", "aae", "aaf", "aag", "aah", "aai", "aaj", "aak", "aal", "aam", "aan", "aao", "aap", "aaq", "aar", "aas", "aat", "aau", "aav", "aaw", "aax", "aay", "aaz", "aba", "abb", "abc", "abd", "abe", "abf", "abg", "abh", "abi", "abj", "abk", "abl", "abm", "abn", "abo", "abp", "abq", "abr", "abs", "abt", "abu", "abv", "abw", "abx", "aby", "abz", "aca", "acb", "acc", "acd", "ace", "acf", "acg", "ach", "aci", "acj", "ack", "acl", "acm", "acn", "aco", "acp", "acq", "acr", "acs", "act", "acu", "acv", "acw", "acx", "acy", "acz", "ada", "adb", "adc", "add", "ade", "adf", "adg", "adh", "adi", "adj", "adk", "adl", "adm", "adn", "ado", "adp", "adq", "adr", "ads", "adt", "adu", "adv", "adw", "adx", "ady", "adz", "aea", "aeb", "aec", "aed", "aee", "aef", "aeg", "aeh", "aei", "aej", "aek", "ael", "aem", "aen", "aeo", "aep", "aeq", "aer", "aes", "aet", "aeu", "aev", "aew", "aex", "aey", "aez", "afa", "afb", "afc", "afd", "afe", "aff", "afg", "afh", "afi", "afj", "afk", "afl", "afm", "afn", "afo", "afp", "afq", "afr", "afs", "aft", "afu", "afv", "afw", "afx", "afy", "afz", "aga", "agb", "agc", "agd", "age", "agf", "agg", "agh", "agi", "agj", "agk", "agl", "agm", "agn", "ago", "agp", "agq", "agr", "ags", "agt", "agu", "agv", "agw", "agx", "agy", "agz", "aha", "ahb", "ahc", "ahd", "ahe", "ahf", "ahg", "ahh", "ahi", "ahj", "ahk", "ahl", "ahm", "ahn", "aho", "ahp", "ahq", "ahr", "ahs", "aht", "ahu", "ahv", "ahw", "ahx", "ahy", "ahz"};
        ArrayList<Sector> listaSectores = new ArrayList<>();
        ArrayList<String> elementales = new ArrayList<>();
        ArrayList<String> elementalesT = new ArrayList<>();
        for (int i = 2; i < entrada.size(); i++) {
            String[] linea = entrada.get(i).split(";");
            for (int j = 0; j < fListaSectoresElementales.size(); j++) {
                String[] el = fListaSectoresElementales.get(j).split(";");
                if (el[0].equalsIgnoreCase(linea[0]) && el.length > 1) {
                    elementales.add(el[1]);
                    boolean contain = false;
                    for (int k = 0; k < elementalesT.size(); k++) {
                        if (elementalesT.get(k).equalsIgnoreCase(el[1])) {
                            contain = true;
                        }
                    }
                    if (!contain) {
                        elementalesT.add(el[1]);
                    }
                }
            }
            if (linea[1].equalsIgnoreCase("Ruta")) {
                listaSectores.add(new Sector(linea[0], idS[cnt], false, true, 0, elementales));
            } else if (linea[1].equalsIgnoreCase("APP")) {
                listaSectores.add(new Sector(linea[0], idS[cnt], true, false, 0, elementales));
            }
            elementales = new ArrayList<>();
            cnt++;
        }
        Fitness.setSectoresElementalesTotales(elementalesT.size());
        return listaSectores;
    }

    private static Turno crearTurno(ArrayList<String> entrada, Parametros parametros) {
		/*
		int c=0;
		while(!entrada.get(c).contains("Turno:")){
			c++;
		}
		
		String nombre = entrada.get(c).split(":")[1].replaceAll(" ", "");
		String largoInicio = entrada.get(c+1).split(":")[1].replaceAll(" ", "");
		String cortoInicio = entrada.get(c+2).split(":")[1].replaceAll(" ", "");
		String largoFin = largoInicio.split("a")[1];
		String cortoFin = cortoInicio.split("a")[1];
		largoInicio =largoInicio.split("a")[0];
		cortoInicio = cortoInicio.split("a")[0];
		Turno turno = new Turno(nombre, largoInicio, largoFin, cortoInicio, cortoFin, parametros);
		return turno;
		*/
        String name = "";
        if (entrada.get(1).split(";")[1].equalsIgnoreCase("N")) {
            name = "Noche";
        } else if (entrada.get(1).split(";")[1].equalsIgnoreCase("M") || entrada.get(1).split(";")[1].equalsIgnoreCase("MC") || entrada.get(1).split(";")[1].equalsIgnoreCase("ML")) {
            name = "Mañana";
        } else if (entrada.get(1).split(";")[1].equalsIgnoreCase("T") || entrada.get(1).split(";")[1].equalsIgnoreCase("TC") || entrada.get(1).split(";")[1].equalsIgnoreCase("TL")) {
            name = "Tarde";
        }
        Turno turno = null;
        if (entrada.size() > 2) {
            if (entrada.get(1).split(";")[1].equalsIgnoreCase("ML") || entrada.get(1).split(";")[1].equalsIgnoreCase("TL")) {
                turno = new Turno(name, entrada.get(1).split(";")[2], entrada.get(1).split(";")[3], entrada.get(2).split(";")[2], entrada.get(2).split(";")[3], parametros);
            } else if (entrada.get(1).split(";")[1].equalsIgnoreCase("MC") || entrada.get(1).split(";")[1].equalsIgnoreCase("TC")) {
                turno = new Turno(name, entrada.get(2).split(";")[2], entrada.get(2).split(";")[3], entrada.get(1).split(";")[2], entrada.get(1).split(";")[3], parametros);
            }
        } else {
            turno = new Turno(name, entrada.get(1).split(";")[2], entrada.get(1).split(";")[3], entrada.get(1).split(";")[2], entrada.get(1).split(";")[3], parametros);
        }
        return turno;


    }

    public ArrayList<Set<String>> getSectorizacionModificada() {
        return sectorizacionModificada;
    }

    public ArrayList<Controlador> getControladores() {
        return controladores;
    }

    public void setControladores(ArrayList<Controlador> controladores) {
        this.controladores = controladores;
    }

    public ArrayList<Nucleo> getNucleos() {
        return nucleos;
    }

    public void setNucleos(ArrayList<Nucleo> nucleos) {
        this.nucleos = nucleos;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public ArrayList<Sector> getListaSectores() {
        return listaSectores;
    }

    public void setListaSectores(ArrayList<Sector> listaSectores) {
        this.listaSectores = listaSectores;
    }

    public ArrayList<Set<String>> getSectorizacion() {
        return sectorizacion;
    }

    public void setSectorizacion(ArrayList<Set<String>> sectorizacion) {
        this.sectorizacion = sectorizacion;
    }

    public ArrayList<ArrayList<String>> getMatrizAfinidad() {
        return matrizAfinidad;
    }

    public void setMatrizAfinidad(ArrayList<ArrayList<String>> matrizAfinidad) {
        this.matrizAfinidad = matrizAfinidad;
    }

    public ArrayList<Sector> getListaSectoresAbiertos() {
        return listaSectoresAbiertos;
    }

    public void setListaSectoresAbiertos(ArrayList<Sector> listaSectoresAbiertos) {
        this.listaSectoresAbiertos = listaSectoresAbiertos;
    }

    public HashMap<Sector, ArrayList<String>> getVolumnsOfSectors() {
        return volumnsOfSectors;
    }

    public void setVolumnsOfSectors(HashMap<Sector, ArrayList<String>> volumnsOfSectors) {
        this.volumnsOfSectors = volumnsOfSectors;
    }

//    public int getCargaTrabajo() {
//        return cargaTrabajo;
//    }

//    public void setCargaTrabajo(int cargaTrabajo) {
//        this.cargaTrabajo = cargaTrabajo;
//    }


    public Solucion getDistribucionInicial() {
        return distribucionInicial;
    }


    public int getSlotMomentoActual() {
        return slotMomentoActual;
    }


    public ArrayList<Sector> getListaNuevosSectoresAbiertosTrasMomentoActual() {
        return listaNuevosSectoresAbiertosTrasMomentoActual;
    }

    public Map<String, Set<String>> getMapaAfinidad() {
        return mapaAfinidad;
    }

    public void setMapaAfinidad(Map<String, Set<String>> mapaAfinidad) {
        this.mapaAfinidad = mapaAfinidad;
    }
}
