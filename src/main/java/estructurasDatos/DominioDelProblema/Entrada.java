package estructurasDatos.DominioDelProblema;

import estructurasDatos.Parametros;
import fitnessFunction.Fitness;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Objeto Entrada, el cual contiene toda la informacion que debe ser introducida como entrada al programa para la resolucion del problema.
 *
 * @author Tino
 */
public class Entrada {

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
    private ArrayList<ArrayList<String>> sectorizacion;
    /**
     * Indica los sectores que tienen afinidad entre si.
     */
    private ArrayList<ArrayList<String>> matrizAfinidad;
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
    private int cargaTrabajo;
    
    private ArrayList<ArrayList<String>> sectorizacionModificada;
    private ArrayList<Controlador> controladoresModificados;

    /**
     * Contructor
     *
     * @param controladores         Lista con los controladores disponibles y sus caracteristicas.
     * @param nucleos               Nucleos con sectores abiertos para la instacia del problema a resolver.
     * @param turno                 Turno a resolver.
     * @param listaSectores         Lista de todos los sectores que componen el espacio aereo del aeropuerto.
     * @param listaSectoresAbiertos Lista de los sectores operativos en la instancia del problema.
     * @param sectorizacion         Sectorizacion utilizada en la instancia del problema. La sectorizacion es el conjunto de sectores que se abren y cierran durante la duracion del turno.
     * @param matrizAfinidad        Indica los sectores que tienen afinidad entre si.
     * @param volumnsOfSectors      Lista con todos los sectores y los volumenes asociados a estos.
     * @param cargaTrabajo          Carga de trabajo total representada en slots.
     * @param controladoresModificados 
     * @param sectorizacionModificada 
     */
    public Entrada(ArrayList<Controlador> controladores, ArrayList<Nucleo> nucleos, Turno turno, ArrayList<Sector> listaSectores, ArrayList<Sector> listaSectoresAbiertos, ArrayList<ArrayList<String>> sectorizacion, ArrayList<ArrayList<String>> matrizAfinidad, HashMap<Sector, ArrayList<String>> volumnsOfSectors, int cargaTrabajo, ArrayList<ArrayList<String>> sectorizacionModificada, ArrayList<Controlador> controladoresModificados) {
        this.controladores = controladores;
        this.nucleos = nucleos;
        this.turno = turno;
        this.listaSectores = listaSectores;
        this.sectorizacion = sectorizacion;
        this.matrizAfinidad = matrizAfinidad;
        this.listaSectoresAbiertos = listaSectoresAbiertos;
        this.volumnsOfSectors = volumnsOfSectors;
        this.cargaTrabajo = cargaTrabajo;
        this.sectorizacionModificada = sectorizacionModificada;
        this.controladoresModificados = controladoresModificados;
    }

   

	public static Entrada leerEntrada(Parametros parametros, String path, String entradaId, String entorno) {
        ArrayList<String> fAperturaSectores = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/AperturaSectorizaciones_" + entradaId + ".csv");
        ArrayList<String> fRecursosDisponebles = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/RecursosDisponibles_" + entradaId + ".csv");
        ArrayList<String> fTurno = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/Turno_" + entradaId + ".csv");
        ArrayList<String> fModificacionSectores = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/ModificacionSectorizaciones_" + entradaId + ".csv");
        //TODO: Tratamiento exception cuando no encuentra el fichero (xq no existe)
        ArrayList<String> fModificacionRecursos = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/ModificacionRecursos_" + entradaId + ".csv");
        ArrayList<String> fDistribucionInicial = rwFiles.Lectura.Listar("entrada/Casos/" + path + "/DistribucionInicial_" + entradaId + ".csv");
        

        ArrayList<String> fListaSectoresElementales = rwFiles.Lectura.Listar("entrada/" + entorno + "/ListaSectoresElementales_" + entorno + ".csv");
        ArrayList<String> fMatrizAfinidad = rwFiles.Lectura.Listar("entrada/" + entorno + "/MatrizAfinidad_" + entorno + ".csv");
        ArrayList<String> fSectoresNucleos = rwFiles.Lectura.Listar("entrada/" + entorno + "/SectoresNucleos_" + entorno + ".csv");
        ArrayList<String> fSectorizacionSectoresVolumenes = rwFiles.Lectura.Listar("entrada/" + entorno + "/SectorizacionesSectoresVolumenes_" + entorno + ".csv");

        ArrayList<Controlador> controladores = crearControladores(fRecursosDisponebles);
        ArrayList<Sector> listaSectores = crearListaSectores(fSectoresNucleos, fListaSectoresElementales);
        ArrayList<Nucleo> nucleos = crearNucleos(fSectoresNucleos, listaSectores);
        ArrayList<ArrayList<String>> matrizAfinidad = crearMatrizAfinidad(fMatrizAfinidad, listaSectores);


        Turno turno = crearTurno(fTurno, parametros);
        ArrayList<ArrayList<String>> sectorizacion = crearSectorizacion(fAperturaSectores, fSectorizacionSectoresVolumenes, turno, listaSectores);
        ArrayList<ArrayList<String>> sectorizacionModificada = null;
        ArrayList<Controlador> controladoresModificados = null;
        
        if(!fModificacionSectores.isEmpty()) {        	
        	sectorizacionModificada = crearSectorizacion(fAperturaSectores, fSectorizacionSectoresVolumenes, turno, listaSectores);
        }
        if(!fModificacionRecursos.isEmpty()) {
        	controladoresModificados = crearControladoresModificados(controladores,fModificacionRecursos);        	
        }
        

        ArrayList<Sector> listaSectoresAbiertos = crearListaSectoresAbiertos(sectorizacion, listaSectores);
        HashMap<Sector, ArrayList<String>> volumnsOfSectors = crearHashMapSectoresVolumenes(listaSectoresAbiertos, fSectorizacionSectoresVolumenes);
        int cargaTrabajo = calcularCargaTrabajo(sectorizacion, controladores, listaSectoresAbiertos);

        Entrada entrada = new Entrada(controladores, nucleos, turno, listaSectores, listaSectoresAbiertos, sectorizacion, matrizAfinidad, volumnsOfSectors, cargaTrabajo,sectorizacionModificada,controladoresModificados);

        return entrada;
    }

    private static ArrayList<Controlador> crearControladoresModificados(ArrayList<Controlador> controladores2,
			ArrayList<String> fModificacionRecursos) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int calcularCargaTrabajo(ArrayList<ArrayList<String>> sectorizacion, ArrayList<Controlador> controladores, ArrayList<Sector> listaSectoresAbiertos) {
        int c = 0;
        for (int i = 0; i < sectorizacion.size(); i++) {
            c += sectorizacion.get(i).size();
        }
		/*
		double f = 0;
		for (int i = 0; i < controladores.size(); i++) {
			f += i * (sectorizacion.size()) *0.95; //CONSIDERAMOS QUE COMO MAXIMO PUEDE TRABAJAR UN 90% DEL TURNO AUNQUE SEA INFACTIBLE
		}
		f = f/(controladores.size()* controladores.size());
		
		double f2 = 0;
		for (int i = 0; i < controladores.size(); i++) {
			f2 += i * ((c*2) / controladores.size()) *0.45; //CONSIDERAMOS QUE COMO MINIMO TRABAJA UN 50% DEL TURNO POR SECTORES ABIERTOS
		}
		f2 = f2/(controladores.size()* controladores.size());
		
		
		Fitness.setNormalizacionRedCtrlMax(f);
		Fitness.setNormalizacionRedCtrlMin(f2);
		*/
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
		/*
		Iterator<Entry<Sector, ArrayList<String>>> it = volumnsOfSectors.entrySet().iterator();
		while (it.hasNext()) {
		    HashMap.Entry<Sector, ArrayList<String>> e = it.next();
		    Sector key = e.getKey();
		    ArrayList<String> value = e.getValue();
		    if (value.isEmpty()) {
		        it.remove();
		    }
		}
		*/
        return volumnsOfSectors;
    }

    private static ArrayList<Sector> crearListaSectoresAbiertos(ArrayList<ArrayList<String>> sectorizacion, ArrayList<Sector> listaSectores) {
        ArrayList<Sector> sectoresAbiertos = new ArrayList<>();
        for (int i = 0; i < sectorizacion.size(); i++) {
            ArrayList<String> slot = sectorizacion.get(i);
            for (int j = 0; j < slot.size(); j++) {
                String sct = slot.get(j);
                for (int k = 0; k < listaSectores.size(); k++) {
                    if (sct.equalsIgnoreCase(listaSectores.get(k).getId())) {
                        boolean esta = false;
                        for (int l = 0; l < sectoresAbiertos.size(); l++) {
                            if (sectoresAbiertos.get(l).getId().equalsIgnoreCase(sct)) {
                                esta = true;
                            }
                        }
                        if (!esta) {
                            sectoresAbiertos.add((Sector) listaSectores.get(k).clone());
                        }
                    }
                }
            }
        }
        return sectoresAbiertos;
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

    private static ArrayList<ArrayList<String>> crearSectorizacion(ArrayList<String> entrada, ArrayList<String> confSectores, Turno turno, ArrayList<Sector> listaSectores) {
        ArrayList<ArrayList<String>> sectorizacion = new ArrayList<>();
        for (int i = 0; i < turno.getTl()[1]; i++) {
            ArrayList<String> slot = new ArrayList<>();
            slot.add("111");
            sectorizacion.add(slot);
        }
        for (int i = 1; i < entrada.size(); i++) {
            String[] linea = entrada.get(i).split(";");
            if (linea[1].equalsIgnoreCase("SECTOR")) {
                sectorizacion = introducirSector(linea, sectorizacion, turno, listaSectores, false);
            } else if (linea[1].equalsIgnoreCase("CONF")) {
                sectorizacion = introducirListaSectores(linea, confSectores, sectorizacion, turno, listaSectores);
            } else if (linea[1].equalsIgnoreCase("SECTORNOCTURNO")) {
                sectorizacion = introducirSector(linea, sectorizacion, turno, listaSectores, true);
            }
        }
        for (int i = 0; i < sectorizacion.size(); i++) {
            ArrayList<String> slot = sectorizacion.get(i);
            slot.remove(0);
            sectorizacion.set(i, slot);
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
            if(linea[1].equalsIgnoreCase("PTD")) {
            	controladores.add(new Controlador(Integer.parseInt(linea[0].substring(1)), linea[3], linea[2], true, false, false));
            } else if(linea[1].equalsIgnoreCase("CON")) {
            	controladores.add(new Controlador(Integer.parseInt(linea[0].substring(1)), linea[3], linea[2], false, true, false));	
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
    public ArrayList<ArrayList<String>> getSectorizacionModificada() {
		return sectorizacionModificada;
	}

	public void setSectorizacionModificada(ArrayList<ArrayList<String>> sectorizacionModificada) {
		this.sectorizacionModificada = sectorizacionModificada;
	}

	public ArrayList<Controlador> getControladoresModificados() {
		return controladoresModificados;
	}

	public void setControladoresModificados(ArrayList<Controlador> controladoresModificados) {
		this.controladoresModificados = controladoresModificados;
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

    public ArrayList<ArrayList<String>> getSectorizacion() {
        return sectorizacion;
    }

    public void setSectorizacion(ArrayList<ArrayList<String>> sectorizacion) {
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

    public int getCargaTrabajo() {
        return cargaTrabajo;
    }

    public void setCargaTrabajo(int cargaTrabajo) {
        this.cargaTrabajo = cargaTrabajo;
    }


}
