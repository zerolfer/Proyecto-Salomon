package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.move4.*;
import algorithms.variableNeighborhoodSearch.impl.moves.move4.Move4_4;
import algorithms.variableNeighborhoodSearch.impl.moves.move5.*;
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

            case "mov2.1":
                return new Move4_1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.2":
                return new Move4_2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.3":
                return new Move4_3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.4":
                return new Move4_4(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.5":
                return new Move4_5(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov2.6":
                return new Move4_6(entrada, patrones, parametros, parametrosAlgoritmo);

            case "mov3.1":
                return new Move5_1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3.2":
                return new Move5_2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3.3":
                return new Move5_3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3.4":
                return new Move5_4(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3.5":
                return new Move5_5(entrada, patrones, parametros, parametrosAlgoritmo);
            case "mov3.6":
                return new Move5_6(entrada, patrones, parametros, parametrosAlgoritmo);
            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }
    }
}
