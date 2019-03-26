package algorithms.VNS;

import algorithms.VNS.moves.DeciderMove;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import patrones.Patrones;

import java.util.ArrayList;

public class vnsBusquedaLocal {

    public static Solucion BusquedaLocal(String movimiento, Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlg, int tmn) {


        ArrayList<Solucion> soluciones = DeciderMove.switchMoves(movimiento, individuo, parametrosAlg, patrones, entrada, parametros, tmn);
        //System.out.println(movimiento+": "+soluciones.size() +" - tmnInt: "+ tmn);
/*		if(soluciones.size()==0) {
			System.out.println();
		}
*/
        int nSemillas = 4;
        double porcentajeEspacio = 0.33;
        double r = 0;
        long particionInicio = 1;
        long particionFin = soluciones.size() / nSemillas - 1;
        Solucion indNuevo = (Solucion) individuo.clone();
        int mejora = 0;
        double bestFit = DeciderFitnessFunction.switchFitnessF(individuo, patrones, entrada, parametros, parametrosAlg)[0];
        double newFit = 0;
        int m = 1;
        int maxNoMejoras = (int) (soluciones.size() * porcentajeEspacio);
        for (int i = 0; i < nSemillas; i++) {
            if ((r = Math.random()) < 0.5) {
                m = -1;
            } else {
                m = 1;
            }
            int x = (int) ((int) (Math.random() * particionFin) + particionInicio);
            while (mejora <= maxNoMejoras && soluciones.size() != 0) {
                try {
                    indNuevo = soluciones.get(x);
                } catch (Exception e) {
                    return individuo;
                }
                if (indNuevo.getTurnos().size() != indNuevo.getControladores().size()) {
                    indNuevo = VNS.orderByLazyCriteria(indNuevo);
                }
                newFit = DeciderFitnessFunction.switchFitnessF(indNuevo, patrones, entrada, parametros, parametrosAlg)[0];
                if (newFit <= bestFit) {
                    mejora++;
                } else {
                    bestFit = newFit;
                    individuo = indNuevo;
                    mejora = 0;
                }
                x = x + m;
                if (x < 0) {
                    x = soluciones.size() - 1;
                } else if (x >= soluciones.size()) {
                    x = 0;
                }
            }
            particionInicio = particionFin;
            particionFin += soluciones.size() / nSemillas;
        }
        return individuo;
    }
}
