package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla.*;
import algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla.MoveRejilla_4;
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
                return new Move3(entrada, patrones, parametros, parametrosAlgoritmo);

            case "movRejilla.1":
                return new MoveRejilla_1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movRejilla.2":
                return new MoveRejilla_2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movRejilla.3":
                return new MoveRejilla_3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movRejilla.4":
                return new MoveRejilla_4(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movRejilla.5":
                return new MoveRejilla_5(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movRejilla.6":
                return new MoveRejilla_6(entrada, patrones, parametros, parametrosAlgoritmo);

            case "movMaxCarga":
                return new MoveMaxCarga(entrada, patrones, parametros, parametrosAlgoritmo);

            case "movLibre":
//                return new MoveMaxCarga(entrada, patrones, parametros, parametrosAlgoritmo); // TODO

            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");

        }
    }
}
