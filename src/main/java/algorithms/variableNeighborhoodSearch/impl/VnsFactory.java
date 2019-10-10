package algorithms.variableNeighborhoodSearch.impl;

import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

public class VnsFactory {

    /**
     * FACTORY METHOD, {@link ParametrosAlgoritmo.VNS}
     */
    public static VariableNeighborhoodSearch getVNS(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        String id = parametrosAlgoritmo.VNS.getTipoVNS();
        switch (id) {

            case "VND":
                return new VariableNeighborhoodDescendent(parametros, patrones, parametrosAlgoritmo, entrada);
            case "RVNS":
                return new VariableNeighborhoodReduced(parametros, patrones, parametrosAlgoritmo, entrada);
            case "BVNS":
                return new VariableNeighborhoodBasic(parametros, patrones, parametrosAlgoritmo, entrada);
            case "GVNS":
                return new VariableNeighborhoodSearchGeneral(parametros, patrones, parametrosAlgoritmo, entrada);
            case "SVNS":
                return new VariableNeighborhoodSkewed(parametros, patrones, parametrosAlgoritmo, entrada);

            default:
                throw new RuntimeException("Tipo de VNS \"" + id + "\" no encontrado." +
                        "Por favor, revisar fichero src/resources/algorithm.properties");

        }
    }
}
