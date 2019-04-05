package main;

import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import algorithms.variableNeighborhoodSearch.impl.VariableNeighborhoodDescendent;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.List;

public class Main_VNS {

    public static void main_vns(Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada,
                                Patrones patrones, List<Solucion> poblacionInicial) {
//
//        /*PRESENTACION DE RESULTADOS Y TRAZAS*/
//
//        rwFiles.EscrituraExcel.EscrituraSoluciones("PoblacionFactible", Main.carpetaSoluciones,
//                poblacionReducirControladores, entrada, patrones, parametros, parametrosAlgoritmo);
//        trazas.Trazas.archivarYLimpiarTrazas(poblacionReducirControladores, Main.propFileOptions, parametrosAlgoritmo);
//        trazas.Trazas.limpiarTrazas();
//        /*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/
//
//
//        /*PRESENTACION DE RESULTADOS Y TRAZAS*/
//        rwFiles.EscrituraExcel.EscrituraSoluciones("PoblacionOptimizada", Main.carpetaSoluciones, poblacionOptimizada
//                , entrada, patrones, parametros, parametrosAlgoritmo);
//        trazas.Trazas.archivarYLimpiarTrazas(poblacionOptimizada, Main.propFileOptions, parametrosAlgoritmo);
//        /*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/
//        System.out.println("Done");
//
        VariableNeighborhoodSearch vnd = new VariableNeighborhoodDescendent(parametros, parametrosAlgoritmo, entrada);

        for (Solucion solucion : poblacionInicial) {
            vnd.startExecution(solucion);
        }

    }
}
