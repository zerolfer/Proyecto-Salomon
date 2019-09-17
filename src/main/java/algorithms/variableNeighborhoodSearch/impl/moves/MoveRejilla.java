package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

import static herramientas.CridaUtils.STRING_NO_TURNO;

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
        return super.getRejillas(turnos);
    }

    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int desde, int hasta) {
        return !x.getTurnos().get(c1).substring(desde, hasta).contains(STRING_NO_TURNO)
                && !x.getTurnos().get(c2).substring(desde, hasta).contains(STRING_NO_TURNO);
        // no hay restricciones de movimiento salvo las del dominio del problema
        // (en este caso, que no se hagan movimientos sin hay un slot '000' de no turno)
    }

    @Override
    public String toString() {
        return "MoveRejilla";
    }
}
