package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

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
                // TODO: and so on...
            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }
    }
}
