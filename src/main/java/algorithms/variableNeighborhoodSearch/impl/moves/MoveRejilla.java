package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public abstract class MoveRejilla extends MoveTemplate {

    protected MoveRejilla(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected int obtenerIndiceControlador1(List<Integer> c1Indices) {
        return super.obtenerIndiceControlador1EnOrden(c1Indices);
    }

    @Override
    protected List<int[]> obtenerTrabajosControlador1(ArrayList<String> turnos, int c1) {
        return super.getIntervalos(turnos.get(c1));
    }
}
