package algorithms.variableNeighborhoodSearch.impl.moves.moveMaxCarga;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveMaxCarga;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

public class MoveMaxCarga_2 extends MoveMaxCarga {

    public MoveMaxCarga_2(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    /**
     * C1 debe tener trabajo en el intervalo elegido
     * C2 debe estar trabajando en el intervalo elegido
     * El sector y la posición de trabajo de B1 deberán ser la misma que la de B2
     */
    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int desde, int hasta) {
        String substring1 = x.getTurnos().get(c1).substring(desde, hasta);
        String substring2 = x.getTurnos().get(c2).substring(desde, hasta);
        return MetaheuristicUtil.esTrabajo(substring1) && MetaheuristicUtil.esTrabajo(substring2)
                && substring1.equals(substring2);
    }

    @Override
    public String toString() {
        return "MoveMaxCarga_2";
    }

}
