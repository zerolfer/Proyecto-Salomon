package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import patrones.Patrones;

public class Move2_1 extends Move2 {

    protected Move2_1(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
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

}
