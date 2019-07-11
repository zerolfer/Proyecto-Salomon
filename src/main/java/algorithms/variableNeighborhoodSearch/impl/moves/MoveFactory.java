package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.moveMaxCarga.*;
import algorithms.variableNeighborhoodSearch.impl.moves.moveRejilla.*;
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
                return new MoveLibre(entrada, patrones, parametros, parametrosAlgoritmo);

            case "movRejilla":
                return new MoveRejilla(entrada, patrones, parametros, parametrosAlgoritmo);

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

            case "movMaxCarga.1":
                return new MoveMaxCarga_1(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movMaxCarga.2":
                return new MoveMaxCarga_2(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movMaxCarga.3":
                return new MoveMaxCarga_3(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movMaxCarga.4":
                return new MoveMaxCarga_4(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movMaxCarga.5":
                return new MoveMaxCarga_5(entrada, patrones, parametros, parametrosAlgoritmo);
            case "movMaxCarga.6":
                return new MoveMaxCarga_6(entrada, patrones, parametros, parametrosAlgoritmo);


            case "movLibre":
                return new MoveLibre(entrada, patrones, parametros, parametrosAlgoritmo);

            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");

        }
    }

    public static NeighborStructure createNeighborhoodRestringido(String id, Entrada entrada, Patrones patrones,
                                                                  Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, int c1, int c2) {
        switch (id) {

            case "movRejilla.1.Restringido":
                return new MoveRejilla_1_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);
            case "movRejilla.2.Restringido":
                return new MoveRejilla_2_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);
            case "movRejilla.3.Restringido":
                return new MoveRejilla_3_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);
            case "movRejilla.4.Restringido":
                return new MoveRejilla_4_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);
            case "movRejilla.5.Restringido":
                return new MoveRejilla_5_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);
            case "movRejilla.6.Restringido":
                return new MoveRejilla_6_Restringido(entrada, patrones, parametros, parametrosAlgoritmo, c1, c2);

            default:
                throw new RuntimeException("Movimiento \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");
        }

    }
}
