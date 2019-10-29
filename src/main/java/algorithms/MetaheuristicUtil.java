package algorithms;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.DominioDelProblema.Sector;
import estructurasDatos.Solucion;
import fitnessFunction.Fitness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static herramientas.CridaUtils.*;

/*
 * Clase que refactoriza métodos comunes necesarios en ambas metaheurísticas
 */
public class MetaheuristicUtil {

    /**
     * Ordena en la solucion, los turnos de trabajo de menor a mayor carga de trabajo en la lista.
     *
     * @param ind Solucion
     * @return Solucion ordenada.
     */
    public static Solucion orderByLazyCriteria(Solucion ind) { // LEGACY
        /*
         * Ordena un array de controladores (el que tiene menos letras se situa primero y asi en orden)
         * Este orden se usa para saber cual es el controlador que menos trabaja.
         */
        ArrayList<Controlador> controladores = ind.getControladores();
        ArrayList<String> individuo = ind.getTurnos();

        // si aun tenemos controladores imaginarios
        // (mas controladores asignados que los que tenemos en lista),
        // debemos ordenarlos por carga
        if (individuo.size() > controladores.size()) {

            ArrayList<Integer> numControladores = new ArrayList<>();
            ArrayList<String> individuo2 = new ArrayList<>();
            ArrayList<Integer> order = new ArrayList<>();

            // obtener carga de trabajo de cada controlador y buscar el mayor
            int mayor = 0;
            for (String s : individuo) {
                int[] sum = Fitness.slotsClassification(s);

                order.add(sum[1]);

                if (sum[1] > mayor)
                    mayor = sum[1];
            }

            Set<Integer> indices = getIndicesTurnosControladoresImaginarios(controladores, individuo.size());
            for (int i : indices) {
                order.set(i, order.get(i) - mayor);
            }
//        for (int i = 0; i < order.size(); i++) {
//            if (controladores.get(i).isImaginario())
//                order.set(i, order.get(i) - mayor);
//        }

            for (int e = 0; e < order.size(); e++) {

                // buscar el turno con menor carga de trabajo
                int r = 0;
                int lAnt = 288;
                for (int i = 0; i < order.size(); i++) {
                    if (lAnt > order.get(i)) {
                        lAnt = order.get(i);
                        r = i;
                    }
                }
                order.set(r, 300);
                individuo2.add(individuo.get(r));
                if (lAnt >= 0) // solo si no es imaginario
                    for (int i = 0; i < controladores.size(); i++) {
                        if (r == controladores.get(i).getTurnoAsignado()) {
                            numControladores.add(controladores.get(i).getId());
                            break;
                        }
                    }
            }
            for (int i = 0; i < numControladores.size(); i++)
                buscarControladorPorId(numControladores.get(i), controladores).setTurnoAsignado(i + indices.size());

            ind.setTurnos(individuo2);
            ind.setControladores(controladores);
            ind.setYaOrdenado(false);
            return ind;
        } else
            // pero si ya no hay imaginarios, para que el fitness que compara plantillas sea efectivo,
            // debemos volver a ordenarlo como estaba antes
            if (ind.isYaOrdenado())
                return ind;
            else { // ordenar como en la inicial
                ArrayList<String> individuo2 = new ArrayList<>();
//                ArrayList<Integer> order = new ArrayList<>();

                for (int i = 0; i < controladores.size(); i++) {
                    Controlador c = controladores.get(i);
                    individuo2.add(individuo.get(c.getTurnoAsignado()));
                    c.setTurnoAsignado(i);
                }

                ind.setTurnos(individuo2);
//                ind.setControladores(controladores);
                ind.setYaOrdenado(true);
                return ind;
            }
    }

    /**
     * Método <code>ADAPTER</code> para obtener los indices de aquellos turnos que no son asignados a
     * ninguno de los controladores existentes. Eso se hace porque en la version anterior del software,
     * no todos los controladores imaginarios se añaden a la lista de controladores, sino que se crea
     * una incoherencia entre las longitudes de la lista "controladores" de la lista que representa
     * los "turnos". <br/> Para resolver esto, se hace una búsqueda de los índices que no son asignados
     * nunca a ningún controlador
     */
    private static Set<Integer> getIndicesTurnosControladoresImaginarios(ArrayList<Controlador> controladores, int size) {
        Set<Integer> indices = new HashSet<>();
        // el primer índice es el 0
        for (int i = 0; i < size; i++) indices.add(i);
        for (Controlador controlador : controladores)
//            if (!controlador.isImaginario()) // si el controlador es imaginario, nos quedamos con el índice!!
            indices.removeIf(value -> value == controlador.getTurnoAsignado());
        return indices;
    }

    private static Controlador buscarControladorPorId(int id, List<Controlador> controladores) {
        for (int j = 0; j < controladores.size(); j++) {
            if (id == controladores.get(j).getId()) {
                return controladores.get(j);
            }
        }
        throw new RuntimeException("Controlador no encontrado");
    }

    public static int[] comprobarTrabajoMinimo(String turnoA) {
        int numSlotsTrabajando = 0;
        for (int i = 0; i < turnoA.length(); i += 3) {
            if (esTrabajo(turnoA.substring(i, i + 3)))
                numSlotsTrabajando++;
            else {
                if (numSlotsTrabajando > 0 && numSlotsTrabajando < 3) // 1 slot ~ 5min, trabajo minimo es 15 mim ~ 3 slots
                    return new int[]{3 - numSlotsTrabajando, i};
                else
                    numSlotsTrabajando = 0;
            }
        }
        return new int[]{0, -1};
    }

    public static boolean esTrabajo(String str) {

        // caso base: longitud = 3
        if (str.length() == LONGITUD_CADENAS)
            return !str.equals(STRING_DESCANSO) &&
                    !str.equals(STRING_NO_TURNO);
        for (int i = 0; i < str.length(); i += 3) {
            if (!esTrabajo(str.substring(i, i + LONGITUD_CADENAS))) return false;
        }
        return true;
    }

    public static boolean esDescanso(String str) {

        // caso base: longitud = 3
        if (str.length() == LONGITUD_CADENAS)
            return str.equals(STRING_DESCANSO);
        for (int i = 0; i < str.length(); i += 3) {
            if (!esDescanso(str.substring(i, i + LONGITUD_CADENAS))) return false;
        }
        return true;
    }

    /*
     * usa  el equals de Sector, que comprueba unicamente el ID del sector
     */
    public static Set<String> obtenerNucleosAlQuePerteneceUnSector(List<Nucleo> nucleos, String idSector) {
        Set<String> res = new HashSet<>();
        for (Nucleo nucleo : nucleos) {
            if (nucleo.getSectores().contains(
                    new Sector(null, idSector, false, false, 0, null))
            ) res.add(nucleo.getNombre());
        }
        if (res.isEmpty())
            throw new RuntimeException("El sector con id " + idSector + " no pertenece a nigun núcleo!!");
        return res;
    }

    // éste método puede ser usado para debuguear más cómodamente
    public static List<String> split3In3(String string) {
        List<String> res = new ArrayList<>();//StringBuilder sb=new StringBuilder();
        for (int i = 0; i < string.length(); i += 3) {
            res.add(string.substring(i, i + LONGITUD_CADENAS));
        }
        return res;

    }

    public static boolean esImaginario(int idxControlador, Solucion x) {
        for (Controlador controlador : x.getControladores()) {
            if (controlador.getTurnoAsignado() == idxControlador)
                return false;
        }
        return true;
    }


    /**
     * Ordena en la solucion, los turnos de trabajo de menor a mayor carga de trabajo en la lista.
     *
     * @param ind Solucion
     * @return Solucion ordenada.
     */
    public static Solucion reordenarYEliminarTurnos(Solucion ind) { // LEGACY
        /*
         * Ordena un array de controladores (el que tiene menos letras se situa primero y asi en orden)
         * Este orden se usa para saber cual es el controlador que menos trabaja.
         */
        ArrayList<Controlador> controladores = ind.getControladores();
        ArrayList<String> individuo = ind.getTurnos();

        // si aun tenemos controladores imaginarios
        // (mas controladores asignados que los que tenemos en lista),
        // debemos ordenarlos por carga
        if (individuo.size() > controladores.size()) {

            ArrayList<Controlador> numControladores = new ArrayList<>();
            ArrayList<String> individuo2 = new ArrayList<>();
            ArrayList<Integer> order = new ArrayList<>();

            // obtener carga de trabajo de cada controlador y buscar el mayor
            int mayor = 0;
            for (String s : individuo) {
                int[] sum = Fitness.slotsClassification(s);

                order.add(sum[1]);

                if (sum[1] > mayor)
                    mayor = sum[1];
            }

            Set<Integer> indices = getIndicesTurnosControladoresImaginarios(controladores, individuo.size());
            Set<Integer> indicesAEliminar = new HashSet<>();
            for (int i : indices) {
                if (order.get(i) <= 0) {
                    eliminarControladorImaginario(i, ind);
                    indicesAEliminar.add(i);
                } else order.set(i, order.get(i) - mayor);
            }

            indicesAEliminar.forEach(indices::remove); // eliminar el objeto en sí
            indicesAEliminar.forEach(e -> order.remove((int) (e))); // eliminar por indice

//        for (int i = 0; i < order.size(); i++) {
//            if (controladores.get(i).isImaginario())
//                order.set(i, order.get(i) - mayor);
//        }

            for (int e = 0; e < order.size(); e++) {

                // buscar el turno con menor carga de trabajo
                int r = 0;
                int lAnt = 288;
                for (int i = 0; i < order.size(); i++) {
                    if (lAnt > order.get(i)) {
                        lAnt = order.get(i);
                        r = i;
                    }
                }
                order.set(r, 300);
                individuo2.add(individuo.get(r));
                if (lAnt >= 0) // solo si no es imaginario
                    for (Controlador controlador : controladores) {
                        if (r == controlador.getTurnoAsignado()) {
                            numControladores.add(controlador);
                            break;
                        }
                    }
            }
            for (int i = 0; i < numControladores.size(); i++)
                numControladores.get(i).setTurnoAsignado(i + indices.size());

            ind.setTurnos(individuo2);
            ind.setControladores(controladores);
            ind.setYaOrdenado(false);
            return ind;
        } else
            // pero si ya no hay imaginarios, para que el fitness que compara plantillas sea efectivo,
            // debemos volver a ordenarlo como estaba antes
            if (ind.isYaOrdenado())
                return ind;
            else { // ordenar como en la inicial
                ArrayList<String> individuo2 = new ArrayList<>();
//                ArrayList<Integer> order = new ArrayList<>();

                for (int i = 0; i < controladores.size(); i++) {
                    Controlador c = controladores.get(i);
                    individuo2.add(individuo.get(c.getTurnoAsignado()));
                    c.setTurnoAsignado(i);
                }

                ind.setTurnos(individuo2);
//                ind.setControladores(controladores);
                ind.setYaOrdenado(true);
                return ind;
            }
    }

    private static void eliminarControladorImaginario(int idxTurnoEliminado, Solucion ind) {
        for (Controlador controlador : ind.getControladores()) {
            if (controlador.getTurnoAsignado() > idxTurnoEliminado)
                controlador.setTurnoAsignado(controlador.getTurnoAsignado() - 1);
        }
        ind.getTurnos().remove(idxTurnoEliminado);
    }
}
