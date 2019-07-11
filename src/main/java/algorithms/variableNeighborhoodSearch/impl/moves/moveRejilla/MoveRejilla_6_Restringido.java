package algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveRejilla;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.List;

public class MoveRejilla_6_Restringido extends MoveRejilla {

    private final int c1;
    private final int c2;

    public MoveRejilla_6_Restringido(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, int c1, int c2) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * C1 debe tener trabajo en el intervalo elegido
     * C2 debe estar trabajando en el intervalo elegido
     * El sector y la posición de trabajo podrán ser diferentes.
     */
    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int desde, int hasta) {
        String substring1 = x.getTurnos().get(c1).substring(desde, hasta);
        String substring2 = x.getTurnos().get(c2).substring(desde, hasta);
        return MetaheuristicUtil.esTrabajo(substring1) && MetaheuristicUtil.esTrabajo(substring2);
    }

    @Override
    protected int obtenerIndiceControlador1(List<Integer> c1Indices, Solucion x) {
        return c1;
    }

    @Override
    protected int obtenerIndiceControlador2(List<Integer> c2Indices) {
        return c2;
    }

    @Override
    public String toString() {
        return "MoveMaxCarga_6";
    }

}
