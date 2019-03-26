package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class ParalelMove1 implements Runnable {
    int controlador;
    int posicion;
    Solucion individuo;
    Patrones patrones;
    Entrada entrada;
    Parametros parametros;
    ParametrosAlgoritmo_SA parametrosAlgoritmo;

    public ParalelMove1(int controlador, int posicion, Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo_SA parametrosAlgoritmo) {
        this.controlador = controlador;
        this.posicion = posicion;
        this.individuo = individuo;
        this.patrones = patrones;
        this.entrada = entrada;
        this.parametros = parametros;
        this.parametrosAlgoritmo = parametrosAlgoritmo;
    }

    @Override
    public void run() {
        String p = individuo.getTurnos().get(controlador).substring(posicion, posicion + 3);
        for (int i = 0; i < individuo.getTurnos().size(); i++) {
            if (controlador != i) {
                String t = individuo.getTurnos().get(i);
                if (p.equals(t.substring(posicion + 3, posicion + 6))) {
                    String c1 = individuo.getTurnos().get(controlador).substring(0, posicion) + t.substring(posicion, posicion + 3) + individuo.getTurnos().get(controlador).substring(posicion + 3, individuo.getTurnos().get(controlador).length());
                    String c2 = t.substring(0, posicion) + p + t.substring(posicion + 3, individuo.getTurnos().get(controlador).length());
                    individuo.getTurnos().set(controlador, c1);
                    individuo.getTurnos().set(i, c2);
                    i = individuo.getTurnos().size();
                }
            }
        }

    }
}
