import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main_VNS;
import patrones.Patrones;
import pruebasCasos.DeciderCase;

import java.util.ArrayList;
import java.util.List;

import static main.Main.*;

public class AjusteParametricoVNS {

    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < casos.length; i++)
            main1(nEjecucion, "Caso" + casos[i]);
    }

    public static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(caso);

        // Carga de los parámetros del dominio del problema:
        Parametros parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        ParametrosAlgoritmo parametrosAlgoritmo = new ParametrosAlgoritmo();

        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);
        Patrones patrones = new Patrones(entrada, parametros);

        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<Solucion> solEntrada = new ArrayList<>();
        solEntrada.add(entrada.getDistribucionInicial());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // la distribucion inicial está en "entrada"
        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);

        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        solEntrada.addAll(poblacionInicial);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // NOTE: modificar esto unicamente //////////////////////////////////////////////////////////////////////////
        testarAlphas(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        // testarTiempos(caso, entrada, patrones, poblacionInicial, solEntrada);
//        testarVecindades(caso, entrada, patrones, poblacionInicial, solEntrada);

        // NOTE /////////////////////////////////////////////////////////////////////////////////////////////////////


        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        String sb = caso + "-" + ejecucion;
        rwFiles.EscrituraExcel.EscrituraSoluciones(sb/*caso + "-" /*+ ejecucion*/ /*+ "-Inicial+Fase1+Fase2"*/,
                main.Main.carpetaSoluciones, solEntrada, entrada, patrones, parametros, parametrosAlgoritmo);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private static void testarAlphas(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                     ParametrosAlgoritmo parametrosAlgoritmo,
                                     ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {
//        double[] alphas = new double[]{15, 15.5, 16, 16.5, 17, 17.5, 18, 18.5, 19, 19.5, 20};
        double[] alphas = new double[]{0.5, 1, 2, 5, 10, 15, 20, 30, 40, 50};
        double inicial = parametrosAlgoritmo.VNS.getAlpha();
        parametrosAlgoritmo.VNS.setTipoVNS("SVNS");
        for (double alpha : alphas) {
            parametrosAlgoritmo.VNS.setAlpha(alpha);
            executeXTimesVNS(1, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//            System.err.println("x--------------------------------------------------------------------------------x");
        }
        parametrosAlgoritmo.VNS.setAlpha(inicial);

    }

    private static void testarTiempos(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                      ParametrosAlgoritmo parametrosAlgoritmo,
                                      List<Solucion> poblacionInicial, List<Solucion> solEntrada) {
        Integer[] ts = new Integer[]{
                10, 15,
                20, 25,
                30, 35,
                40, 45,
                50, 55,
                60
        };

        long inicial = parametrosAlgoritmo.getMaxMilisecondsAllowed();
        for (Integer t : ts) {
            parametrosAlgoritmo.setMaxMilisecondsAllowed(t * 60000);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        }
        parametrosAlgoritmo.setMaxMilisecondsAllowed(inicial);

    }

    private static void testarVecindades(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                         ParametrosAlgoritmo parametrosAlgoritmo,
                                         List<Solucion> poblacionInicial, List<Solucion> solEntrada) {

        String v1 = "movRejilla,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movMaxCarga.5,movMaxCarga.6,movLibre",
                v2 = "movMaxCarga,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movRejilla.5,movRejilla.6,movLibre",

                v3 = "movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movMaxCarga.5,movMaxCarga.6,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movRejilla.5,movRejilla.6,movLibre",
                v4 = "movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movRejilla.5,movRejilla.6,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movMaxCarga.5,movMaxCarga.6,movLibre",

                v5 = "movRejilla.1,movMaxCarga.1,movRejilla.2,movMaxCarga.2,movRejilla.3,movMaxCarga.3,movRejilla.4,movMaxCarga.4,movRejilla.5,movMaxCarga.5,movRejilla.6,movMaxCarga.6,movLibre",
                v6 = "movMaxCarga.1,movRejilla.1,movMaxCarga.2,movRejilla.2,movMaxCarga.3,movRejilla.3,movMaxCarga.4,movRejilla.4,movMaxCarga.5,movRejilla.5,movMaxCarga.6,movRejilla.6,movLibre";

        executeXTimesVNSAndSave(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v1);

        System.err.println("x---------------x");
        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v2);

        System.err.println("x---------------x");
        executeXTimesVNSAndSave(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v3);

        System.err.println("x---------------x");
        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v4);

        System.err.println("x---------------x");
        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v5);

        System.err.println("x---------------x");
        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, v6);
    }


    private static void executeXTimesVNS(int X, String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                         ParametrosAlgoritmo parametrosAlgoritmo,
                                         List<Solucion> poblacionInicial, List<Solucion> solEntrada, String str) {
        for (int i = 0; i < X; i++)
            Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, str);
    }

    private static void executeXTimesVNS(int X, String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                         ParametrosAlgoritmo parametrosAlgoritmo,
                                         List<Solucion> poblacionInicial, List<Solucion> solEntrada) {
        for (int i = 0; i < X; i++)
            Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, "");
    }

    private static void executeXTimesVNSAndSave(int X, String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                ParametrosAlgoritmo parametrosAlgoritmo,
                                                List<Solucion> poblacionInicial, List<Solucion> solEntrada, String str) {
        for (int i = 0; i < X; i++) {
            List<Solucion> r =
                    Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, str);
            if (i == 9) solEntrada.addAll(r);
        }

    }

}
