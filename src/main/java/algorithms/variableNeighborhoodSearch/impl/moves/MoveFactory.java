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
            case "mov1":
                return new Move1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2":
                return new Move2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3":
//                return new Move3();
                // TODO: and so on...
            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }
    }
}
