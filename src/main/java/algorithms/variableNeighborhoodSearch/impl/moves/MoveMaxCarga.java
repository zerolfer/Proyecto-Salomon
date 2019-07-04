package algorithms.variableNeighborhoodSearch.impl.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class MoveMaxCarga extends MoveTemplate {

    protected MoveMaxCarga(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected int obtenerIndiceControlador1(List<Integer> c1Indices) {
        return obtenerIndiceControlador1EnOrden(c1Indices);
    }

    @Override
    protected List<int[]> obtenerTrabajosControlador1(ArrayList<String> turnos, int c1) {
        return getIntervalos(turnos.get(c1));
    }

    @Override
    protected boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int i, int i1) {
        return true; // no hay restricciones de movimiento salvo las del dominio del problema
    }
}
