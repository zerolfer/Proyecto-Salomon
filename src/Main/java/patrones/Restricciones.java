package patrones;


import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.DominioDelProblema.Turno;
import estructurasDatos.Parametros;
import estructurasDatos.Solucion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tino
 */
public class Restricciones {
    /**
     * Variable utilizada para almacenar los tiempos que se emplean el la comprobacion de cada una de las restricciones durante todo el proceso.
     * Existe una funcion opcional con la que no alamcenar los tiempos.
     */
    public static long[] timers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /**
     * Variable utilizada para almacenar las restricciones incumplidas de cada tipo (esta variable se reinicia cada vez que se llama a la funcion).
     */
    public static double[] restriccionesNoCumplidas = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //public static double[] pesoPorRestriccion={1.2,1.2,1.5,1,1.5,1,1.5,1,1,1,1,1.2,1,1};
    /**
     * Vector para otorgar distintos pesos a cada una de las restricciones.
     */
    public static double[] pesoPorRestriccion = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    /**
     * Valor de penalizacion calculado en funcion del numero de slots que tiene el turno. Este valor si inicializa cuando se crean los patrones.
     */
    public static double penalizacion = 0;

    public static final int NUM_HILOS = 4;

    /**
     * Metodo para calcular las restricciones que incumple una solucion. Este valor es ponderado en funcion de la importancia de las restricciones y el numero de veces que las incumpla.
     * Ademas esta funcion ofrece informacion sobre los tiempos empleados por restriccion y las restricciones incumplidas.
     *
     * @param individuo  Solucion.
     * @param patrones   Patrones utilizados para la comprobacion de las restricciones.
     * @param entrada    Entrada con todos los datos de la instancia del problema.
     * @param parametros Parametros del problema.
     * @return Numero de restricciones incumplidas ponderado.
     */
    public static double penalizacionPorRestricciones(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros) {
        double p = 0;
        double n = 0;
        //Restriccion 2
        long t1 = System.currentTimeMillis();
        n = comprobarNucleoTrabajo(individuo, patrones);
        p += (n * pesoPorRestriccion[0]);
        timers[0] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[0] = n;
        //Restriccion 3: No se necesita comprobar, ya que la acreditacion PTD puede controlar todos los sectores
        //Restriccion 4
        t1 = System.currentTimeMillis();
        n = comprobarTipoSector(individuo, patrones);
        p += (n * pesoPorRestriccion[1]);
        timers[1] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[1] = n;
        //Restriccion 5 y 6
        t1 = System.currentTimeMillis();
        n = comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros);
        p += (n * pesoPorRestriccion[2]);
        timers[2] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[2] = n;
        //Restriccion 7
        t1 = System.currentTimeMillis();
        n = comprobarSectoresAbiertosNoche(individuo.getTurnos(), individuo.getControladores(), patrones);
        p += (n * pesoPorRestriccion[3]);
        timers[3] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[3] = n;
        //Restriccion 8
        t1 = System.currentTimeMillis();
        n = comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), parametros);
        //	p +=(n*pesoPorRestriccion[4]);
        timers[4] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[4] = n;
        //Restriccion 9
        t1 = System.currentTimeMillis();
        n = comprobarControladorTurnoCorto(individuo, entrada);
        p += (n * pesoPorRestriccion[5]);
        timers[5] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[5] = n;
        //Restriccion 10
        t1 = System.currentTimeMillis();
        n = comprobarVentanaTrabajoDescanso(individuo.getTurnos(), parametros);
        p += n;
        timers[6] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[6] = n;
        //Restriccion 11
        t1 = System.currentTimeMillis();
        n = comprobarCambioPosicion(individuo.getTurnos(), entrada.getMatrizAfinidad(), entrada.getListaSectoresAbiertos());
        p += (n * pesoPorRestriccion[7]);
        timers[7] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[7] = n;
        //Restriccion 12 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.
        //Restriccion 13
        t1 = System.currentTimeMillis();
        n = comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), parametros);
        p += (n * pesoPorRestriccion[8]);
        timers[8] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[8] = n;
        //Restriccion 14
        t1 = System.currentTimeMillis();
        n = comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), parametros);
        p += (n * pesoPorRestriccion[9]);
        timers[9] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[9] = n;
        //Restriccion 15
        t1 = System.currentTimeMillis();
        n = comprobarTrabajoPosicionMinimoConsecutivoNoRegex(individuo.getTurnos(), parametros);
        p += (n * pesoPorRestriccion[10]);
        timers[10] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[10] = n;
        //Restriccion 16 TODO: PROBAR CODIGO CRIDA
        t1 = System.currentTimeMillis();
        n = comprobarNumMaximoSectores(individuo.getTurnos(), entrada, parametros);
        p += (n * pesoPorRestriccion[11]);
        timers[11] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[11] = n;
        //Restriccion 17 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.

        //Restriccion x: Todo controlador tiene que tener un turno asignado y a todo turno se le tiene que asignar un controlador
        t1 = System.currentTimeMillis();
        n = comprobarControladorAsignado(individuo);
        p += (n * pesoPorRestriccion[12]);
        timers[12] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[12] = n;
        //Restriccion x: Todo controlador debe trabajar. No puede existir un turno vacio
        t1 = System.currentTimeMillis();
        n = comprobarTurnoVacio(individuo);
        p += (n * pesoPorRestriccion[13]);
        timers[13] += System.currentTimeMillis() - t1;
        restriccionesNoCumplidas[13] = n;
        return p;
    }

    /**
     * Metodo para calcular las restricciones que incumple una solucion. Este valor es ponderado en funcion de la importancia de las restricciones y el numero de veces que las incumpla.
     * Esta funcion NO ofrece informacion sobre los tiempos empleados por restriccion ni las restricciones incumplidas.
     *
     * @param individuo  Solucion.
     * @param patrones   Patrones utilizados para la comprobacion de las restricciones.
     * @param entrada    Entrada con todos los datos de la instancia del problema.
     * @param parametros Parametros del problema.
     * @return Numero de restricciones incumplidas ponderado.
     */
    public static double penalizacionPorRestriccionesSinTiempos(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros) {
        double p = 0;
        //Restriccion 2
        p += comprobarNucleoTrabajo(individuo, patrones) * pesoPorRestriccion[0];
        //Restriccion 3: No se necesita comprobar, ya que la acreditacion PTD puede controlar todos los sectores
        //Restriccion 4
        p += comprobarTipoSector(individuo, patrones) * pesoPorRestriccion[1];
        //Restriccion 5 y 6
        p += comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros) * pesoPorRestriccion[2];
        //Restriccion 7
        p += comprobarSectoresAbiertosNoche(individuo.getTurnos(), individuo.getControladores(), patrones) * pesoPorRestriccion[3];
        //Restriccion 8
        p += comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), parametros) * pesoPorRestriccion[4];
        //Restriccion 9
        p += comprobarControladorTurnoCorto(individuo, entrada) * pesoPorRestriccion[5];
        //Restriccion 10
        p += comprobarVentanaTrabajoDescanso(individuo.getTurnos(), parametros) * pesoPorRestriccion[6];
        //Restriccion 11
        p += comprobarCambioPosicion(individuo.getTurnos(), entrada.getMatrizAfinidad(), entrada.getListaSectores()) * pesoPorRestriccion[7];
        //Restriccion 12 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.
        //Restriccion 13
        p += comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), parametros) * pesoPorRestriccion[8];
        //Restriccion 14
        p += comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), parametros) * pesoPorRestriccion[9];
        //Restriccion 15
        p += comprobarTrabajoPosicionMinimoConsecutivoNoRegex(individuo.getTurnos(), parametros) * pesoPorRestriccion[10];
        //Restriccion 16 TODO:REVISAR CODIGO CRIDA
        p += comprobarNumMaximoSectores(individuo.getTurnos(), entrada, parametros) * pesoPorRestriccion[11];
        //Restriccion 17 --> Es una restriccion que ya esta implicita en otras y se comprueba antes.

        //Restriccion x: Todo controlador tiene que tener un turno asignado y a todo turno se le tiene que asignar un controlador
        p += comprobarControladorAsignado(individuo) * pesoPorRestriccion[12];
        //Restriccion x: Todo controlador debe trabajar. No puede existir un turno vacio
        p += comprobarTurnoVacio(individuo) * pesoPorRestriccion[13];

        return p;
    }

    public static double comprobarRestriccionesEnParalelo(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros) {
        ArrayList<RestricionesParalelizadas> arrayRes = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_HILOS);  //El parámetro es en numero de threads que quieres lanzar
        RestricionesParalelizadas restricciones = null;
        for (int i = 1; i <= 14; i++) {
            restricciones = new RestricionesParalelizadas(i, individuo, patrones, entrada, parametros, 0);
            arrayRes.add(restricciones);
            threadPool.submit(restricciones);
        }
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
	/*	try {
			threadPool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        double n = 0;
        for (int i = 0; i < arrayRes.size(); i++) {
            n += arrayRes.get(i).n;
        }
        return n;
    }

    /**
     * Metodo para calcular las restricciones que incumple una solucion. Este valor es ponderado en funcion de la importancia de las restricciones y el numero de veces que las incumpla.
     * Esta funcion NO ofrece informacion sobre los tiempos empleados por restriccion ni las restricciones incumplidas.
     *
     * @param individuo  Solucion.
     * @param patrones   Patrones utilizados para la comprobacion de las restricciones.
     * @param entrada    Entrada con todos los datos de la instancia del problema.
     * @param parametros Parametros del problema.
     * @return 0 - restricciones incumplidas. 1 - restricciones incumplidas.
     */
    public static int comprobarRestriccionesOptimizado(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros) {

        if (comprobarNucleoTrabajo(individuo, patrones) != 0) {
            return 1;
        }
        if (comprobarTipoSector(individuo, patrones) != 0) {
            return 1;
        }
        if (comprobarPorcentajeDescanso(individuo, entrada, entrada.getTurno(), parametros) != 0) {
            return 1;
        }
        if (comprobarSectoresAbiertosNoche(individuo.getTurnos(), individuo.getControladores(), patrones) != 0) {
            return 1;
        }
        if (comprobarTrabajoMaximoConsecutivo(individuo.getTurnos(), parametros) != 0) {
            return 1;
        }
        if (comprobarControladorTurnoCorto(individuo, entrada) != 0) {
            return 1;
        }
        if (comprobarVentanaTrabajoDescanso(individuo.getTurnos(), parametros) != 0) {
            return 1;
        }
        if (comprobarCambioPosicion(individuo.getTurnos(), entrada.getMatrizAfinidad(), entrada.getListaSectores()) != 0) {
            return 1;
        }
        if (comprobarTrabajoMinimoConsecutivo(individuo.getTurnos(), parametros) != 0) {
            return 1;
        }
        if (comprobarDescansoMinimoConsecutivo(individuo.getTurnos(), parametros) != 0) {
            return 1;
        }
        if (comprobarTrabajoPosicionMinimoConsecutivoNoRegex(individuo.getTurnos(), parametros) != 0) {
            return 1;
        }
        if (comprobarNumMaximoSectores(individuo.getTurnos(), entrada, parametros) != 0) {
            return 1;
        }
        if (comprobarControladorAsignado(individuo) != 0) {
            return 1;
        }
        if (comprobarTurnoVacio(individuo) != 0) {
            return 1;
        }

        return 0;
    }

    /**
     * Metodo utilizado para la comprobacion de restricciones puntuales en la heuristica de creacion de soluciones inciales.
     *
     * @param cadenasDeTurnos Turnos de trabajo
     * @param patrones        Patrones utilizados en la comprobacion de restricciones.
     * @param parametros      Parametros del problema.
     * @return Devuelve la suma de restricciones incumplidas.
     */
    public static int comprobarRestriccionesArregloSoluciones(ArrayList<ArrayList<String>> cadenasDeTurnos, Patrones patrones, Parametros parametros) {
        int p = 0;
        ArrayList<String> turnos = InicializarPoblacion.transformacionSoluciones(cadenasDeTurnos);

        p += comprobarTrabajoMaximoConsecutivo(turnos, parametros);
        p += comprobarVentanaTrabajoDescanso(turnos, parametros);
        p += comprobarDescansoMinimoConsecutivo(turnos, parametros);
        p += comprobarTrabajoMinimoConsecutivo(turnos, parametros);
        return p;
    }

    /**
     * CRIDA: REVISAR - Codigo para la comprobacion del numero de sectores maximos por los que puede pasar un controlador.
     *
     * @param turnos     Turnos de trabajo de los controladores.
     * @param entrada    Entrada de la instancia del problema.
     * @param parametros Parametros del problema.
     * @return Numero de veces que se viola la restriccion.
     */
    public static int comprobarNumMaximoSectores(ArrayList<String> turnos, Entrada entrada, Parametros parametros) {
        //TODO: En vez de sumar uno al incumplir, se puede sumar la diferencia (es decir, por cuanto pasa la restriccion), da mas informacion.
        int p = 0;
        for (String turno : turnos) {
            ArrayList<Sector> sectoresAbiertosEnTurno = listaDeSectoresTurno(turno, entrada.getListaSectoresAbiertos());
            if (calculate(sectoresAbiertosEnTurno, entrada.getVolumnsOfSectors()) > parametros.getNumSctrsMax()) {
                p++;
            }
        }
        return p;
    }

    private static ArrayList<Sector> listaDeSectoresTurno(String turno, ArrayList<Sector> listaSectoresAbiertos) {
        ArrayList<Sector> sectoresEnTurno = new ArrayList<>();
        String slot = "", slotAnt = "";
        for (int k = 0; k < turno.length(); k += 3) {
            slot = turno.substring(k, k + 3);
            if (!slot.equalsIgnoreCase(slotAnt) && !slot.equalsIgnoreCase("111")) {
                for (int j = 0; j < listaSectoresAbiertos.size(); j++) {
                    if (listaSectoresAbiertos.get(j).getId().equalsIgnoreCase(slot)) {
                        if (!sectoresEnTurno.contains(listaSectoresAbiertos.get(j))) {
                            sectoresEnTurno.add(listaSectoresAbiertos.get(j));
                        }
                    }
                }
            }
            slotAnt = slot;
        }
        return sectoresEnTurno;
    }

    public static int calculate(ArrayList<Sector> solucionToTest, HashMap<Sector, ArrayList<String>> volumnsOfSectors) {
        int counter = 0;

        @SuppressWarnings("unchecked")
        ArrayList<Sector> resto = (ArrayList<Sector>) solucionToTest.clone();

        ArrayList<Sector> toDelete = new ArrayList<>();
        for (Sector sector : resto) {
            if (toDelete.contains(sector)) {
                continue;
            }
            String pivot = null;
            int maxHits = 0;
            ArrayList<String> tempVolumen = new ArrayList<>();
            for (Sector sector1 : resto) {
                for (String toInsert : volumnsOfSectors.get(sector1)) {
                    if (!tempVolumen.contains(toInsert) && !toDelete.contains(sector1))
                        tempVolumen.add(toInsert);
                }
            }
            for (String volumen : tempVolumen) {
                int hits = countHitsPerSector(volumen, resto, volumnsOfSectors);
                if (hits > maxHits) {
                    pivot = volumen;
                    maxHits = hits;
                }
            }

            for (Sector s : resto) {
                if (volumnsOfSectors.get(s).contains(pivot))
                    toDelete.add(s);
            }
            counter++;
        }
        return counter;
    }

    public static int countHitsPerSector(String volumen, ArrayList<Sector> sectores, HashMap<Sector, ArrayList<String>> volumnsOfSectors) {
        int hits = 0;
        for (Sector s : sectores) {
            for (String v : volumnsOfSectors.get(s)) {
                if (v.equalsIgnoreCase(volumen))
                    hits++;
            }
        }
        return hits;
    }

    /**
     * Metodo para comprobar si se cumple el tiempo minimo de trabajo consecutivo en una posicion en una solucion con el uso de REGEX.
     *
     * @param turnos   Turnos de trabajo de los controladores.
     * @param patrones Patrones usados en la comprobacion de restricciones
     * @return Numero de veces que se viola la restriccion.
     */
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

    /**
     * Metodo para comprobar si se cumple el tiempo minimo de trabajo consecutivo en una posicion en una solucion.
     *
     * @param turnos   Turnos de trabajo de los controladores.
     * @param patrones Patrones usados en la comprobacion de restricciones
     * @return
     */
    public static double comprobarTrabajoPosicionMinimoConsecutivoNoRegex(ArrayList<String> turnos, Parametros pa) {
        double p = 0;
        int pMin = (pa.getTiempoPosMin() / pa.getTamanoSlots());
        for (String turno : turnos) {
            double t1 = 0;
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (turno.substring(i, i + 3).equals("111") && cnt == 0) {

                } else if (turno.substring(i, i + 3).equals("111") && cnt < pMin) {
                    if (t1 == 0) {
                        p++;
                        t1 = 1;
                    } else {
                        p = p + 0.05;
                    }
                } else if (turno.substring(i, i + 3).equals("111") && cnt >= pMin) {
                    cnt = 0;
                } else if (!turno.substring(i, i + 3).equals("111")) {
                    if (i == 0) {
                        cnt++;
                    } else {
                        if (turno.substring(i, i + 3).equals(turno.substring(i - 3, i))) {
                            cnt++;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt == 0) {
                            cnt++;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt < pMin) {
                            if (t1 == 0) {
                                p++;
                                t1 = 1;
                            } else {
                                p = p + 0.05;
                            }
                            cnt = 1;
                        } else if (!turno.substring(i, i + 3).equals(turno.substring(i - 3, i)) && cnt >= pMin) {
                            cnt = 1;
                        }
                    }
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de descanso minimo consecutivo en una solucion mediante REGEX.
     *
     * @param turnos   Turnos de trabajo de los controladores.
     * @param patrones Patrones usados en la comprobacion de restricciones
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarDescansoMinimoConsecutivo(ArrayList<String> turnos, Parametros pa) {
        double p = 0;
        int dMin = (pa.getTiempoDesMin() / pa.getTamanoSlots());
        for (String turno : turnos) {
            double t1 = 0;
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt < dMin && cnt != 0) {
                        if (t1 == 0) {
                            p++;
                            t1 = 1;
                        } else {
                            p = p + 0.05;
                        }
                    }
                    cnt = 0;
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de trabajo minimo consecutivo en una solucion mediante REGEX.
     *
     * @param turnos   Turnos de trabajo de los controladores.
     * @param patrones Patrones usados en la comprobacion de restricciones
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarTrabajoMinimoConsecutivo(ArrayList<String> turnos, Parametros pa) {
        double p = 0;
        int tMin = (pa.getTiempoTrabMin() / pa.getTamanoSlots());
        for (String turno : turnos) {
            int cnt = 0;
            double t1 = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt < tMin && cnt != 0) {
                        if (t1 == 0) {
                            p++;
                            t1 = 1;
                        } else {
                            p = p + 0.05;
                        }
                    }
                    cnt = 0;
                }
            }
            if (cnt < tMin && cnt != 0) {
                if (t1 == 0) {
                    p++;
                    t1 = 1;
                } else {
                    p = p + 0.05;
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de que ningun controlador se encuentre descansando todo el turno en una solucion mediante REGEX.
     *
     * @param individuo Solucion.
     * @param patrones  Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static int comprobarTurnoVacio(Solucion individuo) {
        int p = 0;
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

    /**
     * Metodo para la comprobacion de la condicion de que todos los controladores tengan un turno asignado y ningun turno se encuentre sin asignar.
     *
     * @param individuo Solucion.
     * @return Numero de veces que se viola la restriccion.
     */
    public static int comprobarControladorAsignado(Solucion individuo) {
        int p = 0;
        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getTurnoAsignado() == -1) {
                p++;
            }
        }
        for (int i = 0; i < turnos.size(); i++) {
            boolean cntrAsig = false;
            for (int j = 0; j < controladores.size(); j++) {
                if (controladores.get(j).getTurnoAsignado() == i) {
                    cntrAsig = true;
                    break;
                }
            }
            if (!cntrAsig) {
                p++;
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de descanso minimo cada dos horas y media en una solucion.
     *
     * @param turnos     Turnos de trabajo de los controladores.
     * @param parametros Parametros del problema.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarVentanaTrabajoDescanso(ArrayList<String> turnos, Parametros parametros) {
        double p = 0;
        String t = "";
        int ventanaTiempo = (parametros.getTiempoTrabMax() + parametros.getTiempoDesPorTrabajo()) * 3 / parametros.getTamanoSlots();
        int dMin = parametros.getTiempoDesPorTrabajo() / parametros.getTamanoSlots();
        int tMax = parametros.getTiempoTrabMax() / parametros.getTamanoSlots();
        double x = 0;
        for (int i = 0; i < turnos.size(); i++) {
            t = turnos.get(i);
            int ds = 0;
            int tr = 0;
            for (int j = 0; j < t.length(); j += 3) {
                if (t.substring(j, j + 3).equalsIgnoreCase("111")) {
                    ds++;
                } else {
                    tr++;
                }
                if (j >= ventanaTiempo) {
                    if (t.substring(j - ventanaTiempo, j - ventanaTiempo + 3).equalsIgnoreCase("111")) {
                        ds--;
                    } else {
                        tr--;
                    }
                    if (ds < dMin && tr > tMax) {
                        x += penalizacion;
                    }
                }
            }
            if (x != 0) {
                p = p + x + 1;
                x = 0;
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de que todos los cambios de posicion realizados sin descanso entre sectores sean afines.
     *
     * @param turnos    Turnos de trabajo de los controladores.
     * @param arrayList Matriz de afinidad.
     * @param listaSec  Lista de los sectores abiertos.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarCambioPosicion(ArrayList<String> turnos, ArrayList<ArrayList<String>> arrayList, ArrayList<Sector> listaSec) {
        double p = 0;
        String t = "";
        double x = 0;
        for (int j = 0; j < turnos.size(); j++) {
            t = turnos.get(j);
            for (int i = 0; i < t.length() - 3; i += 3) {
                if (!t.substring(i, i + 3).equalsIgnoreCase("111") && !t.substring(i + 3, i + 6).equalsIgnoreCase("111") && !t.substring(i, i + 3).equals(t.substring(i + 3, i + 6))) {
                    if (t.substring(i, i + 3).equals(t.substring(i, i + 3).toUpperCase()) && t.substring(i + 3, i + 6).equals(t.substring(i + 3, i + 6).toUpperCase())) {
                        if (!comprobarAfinidad(t.substring(i, i + 3), t.substring(i + 3, i + 6), arrayList, listaSec)) {
                            x += penalizacion;
                        }
                    }
                }
            }
            if (x != 0) {
                p = p + 1 + x;
                x = 0;
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de afinida entre dos sectores.
     *
     * @param sct1           Sector 1.
     * @param sct2           Sector 2.
     * @param matrizAfinidad Matriz de afinidad.
     * @param listaSec       Lista de sectores abiertos.
     * @return False: No son afines. True: Son afines.
     */
    private static boolean comprobarAfinidad(String sct1, String sct2, ArrayList<ArrayList<String>> matrizAfinidad, ArrayList<Sector> listaSec) {
        int n1 = 0;
        int n2 = 0;
        for (int i = 0; i < matrizAfinidad.get(0).size(); i++) {
            if (sct1.equalsIgnoreCase(matrizAfinidad.get(0).get(i))) {
                n1 = i;
            }
            if (sct2.equalsIgnoreCase(matrizAfinidad.get(0).get(i))) {
                n2 = i;
            }
        }
        if (matrizAfinidad.get(n1).get(n2).equalsIgnoreCase("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo para la comprobacion de la condicion de que un controlador con acreditacion CON no puede trabajar en sectores de aproximacion.
     *
     * @param individuo Solucion.
     * @param patrones  Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarTipoSector(Solucion individuo, Patrones patrones) {
        double p = 0;
        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        double x = 0;
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).isCON()) {
                int numTurno = controladores.get(i).getTurnoAsignado();
                if (numTurno != -1) {
                    String posibles[] = patrones.getArray()[4].toString().split(";");
                    String t = turnos.get(numTurno);
                    for (int j = 0; j < t.length(); j += 3) {
                        boolean bn = false;
                        for (int k = 0; k < posibles.length; k++) {
                            if (t.substring(j, j + 3).equalsIgnoreCase(posibles[k])) {
                                bn = true;
                            }
                        }
                        if (!bn) {
                            x += penalizacion;
                        }
                    }
                    if (x != 0) {
                        p = p + 1 + x;
                        x = 0;
                    }
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de que un controlador con perteneciente a un nucleo solo puede trabajar en sectores de ese nucleo.
     *
     * @param individuo Solucion.
     * @param patrones  Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarNucleoTrabajo(Solucion individuo, Patrones patrones) {
        double p = 0;
        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        ArrayList<String> nuc = Patrones.nuc;
        double x = 0;
        for (int i = 0; i < controladores.size(); i++) {
            int numTurno = controladores.get(i).getTurnoAsignado();
            if (numTurno != -1) {
                for (int j = 0; j < nuc.size(); j++) {
                    if (controladores.get(i).getNucleo().equalsIgnoreCase(nuc.get(j))) {
                        String posibles[] = patrones.getArray()[5 + j].toString().split(";");
                        String t = turnos.get(numTurno);

                        for (int l = 0; l < t.length(); l += 3) {
                            boolean bn = false;
                            for (int k = 0; k < posibles.length; k++) {
                                if (t.substring(l, l + 3).equalsIgnoreCase(posibles[k])) {
                                    bn = true;
                                }
                            }
                            if (!bn) {
                                x += penalizacion;
                            }
                        }
                        if (x != 0) {
                            p = p + 1 + x;
                            x = 0;
                        }
                    }
                }
            } else {
                p++;
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de que un controlador de turno corto no puede trabajar durante el turno largo.
     *
     * @param individuo Solucion.
     * @param patrones  Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarControladorTurnoCorto(Solucion individuo, Entrada entrada) {
        double p = 0;
        int resto = entrada.getTurno().getTl()[1] - entrada.getTurno().getTc()[1];
        int inicioTCorto = entrada.getTurno().getTc()[0];


        ArrayList<Controlador> controladores = individuo.getControladores();
        ArrayList<String> turnos = individuo.getTurnos();
        for (int i = 0; i < controladores.size(); i++) {
            double t1 = 0;
            if (controladores.get(i).getTurno().equalsIgnoreCase("TC")) {
                if (controladores.get(i).getTurnoAsignado() != -1) {
                    String t = turnos.get(controladores.get(i).getTurnoAsignado());
                    if (inicioTCorto == 0) {
                        for (int j = t.length() - (resto * 3); j < t.length(); j += 3) {
                            if (!t.substring(i, i + 3).equalsIgnoreCase("111")) {
                                if (t1 == 0) {
                                    p++;
                                    t1 = 1;
                                } else {
                                    p = p + 0.05;
                                }
                            }
                        }
                        //array[1] = Pattern.compile("^.*(111){"+ resto +"}$");
                    } else {
                        for (int j = 0; j < (inicioTCorto * 3); j += 3) {
                            if (!t.substring(i, i + 3).equalsIgnoreCase("111")) {
                                if (t1 == 0) {
                                    p++;
                                    t1 = 1;
                                } else {
                                    p = p + 0.05;
                                }
                            }
                        }
                        //array[1] = Pattern.compile("^(111){"+ inicioTCorto +"}.*$");
                    }
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de trabajo maximo consecutivo mediante REGEX.
     *
     * @param turnos   Turnos de trabajo de los controladores.
     * @param patrones Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static double comprobarTrabajoMaximoConsecutivo(ArrayList<String> turnos, Parametros parametros) {
        double p = 0;
        int tMax = (parametros.getTiempoTrabMax() / parametros.getTamanoSlots());
        for (String turno : turnos) {
            double t = 0;
            int cnt = 0;
            for (int i = 0; i < turno.length(); i += 3) {
                if (!turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    cnt++;
                } else if (turno.substring(i, i + 3).equalsIgnoreCase("111")) {
                    if (cnt > tMax && cnt != 0) {
                        if (t == 0) {
                            p++;
                            t = 1;
                        } else {
                            p = p + 0.05;
                        }
                    }
                    cnt = 0;
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion de que todos los sectores nocturnos se cubran por 4 controladores.
     *
     * @param turnos        Turnos de trabajo de los controladores.
     * @param controladores Lista de controladores de la solucion.
     * @param patrones      Patrones usados en la comprobacion de restricciones.
     * @return Numero de veces que se viola la restriccion.
     */
    public static int comprobarSectoresAbiertosNoche(ArrayList<String> turnos, ArrayList<Controlador> controladores, Patrones patrones) {
        int p = 0;
        int n = 90 - 1;

        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getTurnoNoche() != 0) {
                if (patrones.getArray()[n + controladores.get(i).getTurnoNoche()].matcher(turnos.get(controladores.get(i).getTurnoAsignado())).matches()) {
                    p++;
                }
            }
        }
        for (int i = 0; i < controladores.size(); i++) {
            if (controladores.get(i).getTurnoNoche() != 0) {
                int times = 0;
                for (int j = 0; j < controladores.size(); j++) {
                    if (controladores.get(j).getTurnoNoche() == i) {
                        times++;
                    }
                }
                if (times < 4) {
                    p++;
                }
            }
        }
        return p;
    }

    /**
     * Metodo para la comprobacion de la condicion impuesta sobre los porcentajes de descansos minimo durante le turno de dia y noche.
     *
     * @param individuo  Solucion
     * @param entrada    Entrada de la instancia del problema
     * @param patrones   Patrones usados en la comprobacion de restricciones.
     * @param turno      Turno de la instancia del problema.
     * @param parametros Parametros del problema.
     * @return Numero de veces que se viola la restriccion.
     */
    public static int comprobarPorcentajeDescanso(Solucion individuo, Entrada entrada, Turno turno, Parametros parametros) {
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
}
