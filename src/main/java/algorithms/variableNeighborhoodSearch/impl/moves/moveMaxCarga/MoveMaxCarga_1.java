package algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveRejilla;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class MoveRejilla_1 extends MoveRejilla {

    public MoveRejilla_1(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    /**
     * C1 debe tener trabajo en el intervalo elegido
     * C2 debe estar descansado en el intervalo elegido
     */
    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int desde, int hasta) {
        String substring1 = x.getTurnos().get(c1).substring(desde, hasta);
        String substring2 = x.getTurnos().get(c2).substring(desde, hasta);
        return MetaheuristicUtil.esTrabajo(substring1) && MetaheuristicUtil.esDescanso(substring2);
    }

    @Override
    public String toString() {
        return "MoveRejilla_1";
    }
}
