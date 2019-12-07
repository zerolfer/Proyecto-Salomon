package main;

import algorithms.variableNeighborhoodSearch.VariableNeighborhoodSearch;
import algorithms.variableNeighborhoodSearch.impl.vns.VnsFactory;
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
        return main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, estrucVecindad, Main.carpetaTrazas);
    }

    public static List<Solucion> main_vns(String caso, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo,
                                          Entrada entrada, Patrones patrones, List<Solucion> poblacionInicial,
                                          String estrucVecindad, String carpetaTrazas) {

        parametrosAlgoritmo.VNS.initializeNeighborStructures(entrada, patrones, parametros, parametrosAlgoritmo, estrucVecindad);

        VariableNeighborhoodSearch vns = VnsFactory.getVNS(entrada, patrones, parametros, parametrosAlgoritmo);

        List<Solucion> res = new ArrayList<>();

        Log.open(carpetaTrazas);

        String s = "[ Ejecutando " + vns + " ] " + "[ " + caso + " ] ";
        if (parametrosAlgoritmo.VNS.getFlagCondicionParadaTiempo())
            s += String.format("[ Por %d segundos ] ", parametrosAlgoritmo.getMaxMilisecondsAllowed() / 1000);
        if (parametrosAlgoritmo.VNS.getFlagCondicionParadaPorcentajeMejora())
            s += String.format("[ Porcent. Mejora Min. de %f ]", parametrosAlgoritmo.VNS.getPorcentajeMinimoMejoria());
        Log.debug(s);

        for (Solucion solucion : poblacionInicial) {
            res.add(
                    startExecution(vns, solucion, parametrosAlgoritmo)
            );
        }

//        Log.debug(Log.retrieveValue().toString()); FIXME
        Log.close();
        return res;
    }

    private static Solucion startExecution(VariableNeighborhoodSearch vnd, Solucion solucion, ParametrosAlgoritmo p) {

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
