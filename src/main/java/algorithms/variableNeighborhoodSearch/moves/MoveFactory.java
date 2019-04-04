package algorithms.variableNeighborhoodSearch.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import estructurasDatos.ParametrosAlgoritmo;

public class MoveFactory {

    /**
     * FACTORY METHOD, {@link ParametrosAlgoritmo.VNS}
     */
    public static NeighborStructure createNeighborhood(String id) {
        switch (id) {
            case "mov1":
                return new Move1();
            case "mov2":
                return new Move2();
            case "mov3":
                return new Move3();
            // TODO: and so on...
            default:
                throw new RuntimeException("Movimiento \"" + id + " no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }
    }
}
