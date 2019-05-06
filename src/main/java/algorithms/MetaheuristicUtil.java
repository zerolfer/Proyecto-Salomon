package algorithms;

import estructurasDatos.DominioDelProblema.Controlador;
import estructurasDatos.Solucion;
import fitnessFunction.Fitness;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<Integer> numControladores = new ArrayList<>();
        ArrayList<String> individuo2 = new ArrayList<>();
        ArrayList<String> individuo = ind.getTurnos();
        ArrayList<Integer> order = new ArrayList<>();

        // obtener carga de trabajo de cada controlador y buscar el mayor
        int mayor = 0;
        for (int i = 0; i < individuo.size(); i++) {
            int[] sum = Fitness.slotsClassification(individuo.get(i));

            order.add(sum[1]);

            if (sum[1] > mayor)
                mayor = sum[1];
        }

        for (int i = 0; i < order.size(); i++) {
            if (controladores.get(i).isImaginario())
                order.set(i, order.get(i) - mayor);
        }

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
            for (int i = 0; i < controladores.size(); i++) {
                if (r == controladores.get(i).getTurnoAsignado()) {
                    numControladores.add(controladores.get(i).getId());
                    break;
                }
            }
        }
        for (int i = 0; i < numControladores.size(); i++)
            buscarControladorPorId(numControladores.get(i), controladores).setTurnoAsignado(i);

        ind.setTurnos(individuo2);
        ind.setControladores(controladores);
        return ind;
    }

    private static Controlador buscarControladorPorId(int id, List<Controlador> controladores) {
        for (int j = 0; j < controladores.size(); j++) {
            if (id == controladores.get(j).getId()) {
                return controladores.get(j);
            }
        }
        throw new RuntimeException("Controlador no encontrado");
    }

}
