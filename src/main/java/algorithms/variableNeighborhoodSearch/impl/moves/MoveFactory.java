package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;

public class MoveFactory {

    /**
     * FACTORY METHOD, {@link ParametrosAlgoritmo.VNS}
     */
    public static NeighborStructure createNeighborhood(String id, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        switch (id) {
            case "mov0":
                return new Move0(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov1":
                return new Move1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3":
                return new algorithms.variableNeighborhoodSearch.impl.moves.Move3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.1":
                return new Move2_1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.2":
                return new Move2_2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.3":
                return new Move2_3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.4":
                return new Move2_4(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.5":
                return new Move2_5(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.6":
                return new Move2_6(entrada, patrones, parametros, parametrosAlgoritmo);
            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }
    }
}
