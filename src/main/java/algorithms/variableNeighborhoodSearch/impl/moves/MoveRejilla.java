package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class MoveRejilla extends MoveTemplate {

    protected MoveRejilla(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected int obtenerIndiceControlador1(List<Integer> c1Indices, Solucion x) {
        if (MetaheuristicUtil.esImaginario(c1Indices.get(0), x))
            return super.obtenerIndiceControlador1EnOrden(c1Indices);
        else return super.obtenerIndiceControlador1Aleatoriamente(c1Indices);
    }

    @Override
    protected List<int[]> obtenerTrabajosControlador1(ArrayList<String> turnos, int c1) {
        return super.getIntervalos(turnos.get(c1));
    }

    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int i, int i1) {
        return true;
    }

    @Override
    public String toString() {
        return "MoveRejilla";
    }
}
