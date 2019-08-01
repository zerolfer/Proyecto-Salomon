package main;

import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import algorithms.variableNeighborhoodSearch.impl.VnsFactory;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class Main_VNS {

    public static List<Solucion> main_vns(String caso, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo,
                                          Entrada entrada, Patrones patrones, List<Solucion> poblacionInicial,
                                          String estrucVecindad) {
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
        parametrosAlgoritmo.initializeNeighborStructures(entrada, patrones, parametros, parametrosAlgoritmo, estrucVecindad);

        VariableNeighborhoodSearch vns = VnsFactory.setVNS(entrada, patrones, parametros, parametrosAlgoritmo);

        List<Solucion> res = new ArrayList<>();

        Log.open();
//        Log.debug
        Log.info("[ Ejecutando " + vns + " ] " +
                "[ " + caso + " ] " +
                "[ Por " + parametrosAlgoritmo.getMaxMilisecondsAllowed() / 1000 + " segundos ]");

        for (Solucion solucion : poblacionInicial) {
            res.add(
                    startExecution(vns, solucion, parametrosAlgoritmo)
            );
        }

        Log.debug(Log.retrieveValue().toString());
        Log.close();
        return res;
    }

    private static Solucion startExecution(VariableNeighborhoodSearch vnd, Solucion solucion, ParametrosAlgoritmo p)/* throws ExecutionException, TimeoutException, InterruptedException */ {

       /* Solucion res;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        res = executor.submit(new Callable<Solucion>() {
            @Override
            public Solucion call() throws Exception {*/
        return vnd.startExecution(solucion);
         /*   }
        }).get(p.getMaxMilisecondsAllowed(), TimeUnit.MILLISECONDS);
        executor.shutdown();
        return res;*/
    }

}
