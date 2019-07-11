package algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveRejilla;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.List;

public class MoveRejilla_2_Restingido extends MoveRejilla {

    private int c1;
    private int c2;

    public MoveRejilla_2_Restingido(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public void setC2(int c2) {
        this.c2 = c2;
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
    protected int obtenerIndiceControlador1(List<Integer> c1Indices, Solucion x) {
        return c1;
    }

    @Override
    protected int obtenerIndiceControlador2(List<Integer> c2Indices) {
        return c2;
    }

    @Override
    public String toString() {
        return "MoveMaxCarga_2";
    }

}
