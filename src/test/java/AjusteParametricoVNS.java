import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main;
import main.Main_VNS;
import patrones.Patrones;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AjusteParametricoVNS {

    public static String propFileExternoParametros = "./parametros_algoritmo.properties";
    private static URL propFileParameters = Main.class.getResource("/problemParameters.properties");
    private static URL propFileOptions = Main.class.getResource("/options.properties");
//    public static String propFileParametersAlgorithm = Main.class.getResource("/algorithm.properties").getPath();

    private static String entradaPath = "Caso1";
    private static String entradaId = "Id1n-06-03-2017";
    private static String entorno = "Canarias";

    private static String carpetaSoluciones = "";
    private static String carpetaTrazas = "";
    public static Date date = new Date();

    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1, /*3, 4, 5, 6, 7, 8, 9*/};
        for (int caso : casos) main1(nEjecucion, "Caso" + caso);
    }


    private static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        switchCase(caso);

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
//        ajusteTipoVNS(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteVecindades(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteOrdenEntornos(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteProbabilidadDiversif(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteVariacionProbabilidad(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteCiclosProbabilidad(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
//        ajusteTipoEntornos(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        ajusteCiclosBusqueda(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);



        //        testarAlphas(caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        // testarTiempos(caso, entrada, patrones, poblacionInicial, solEntrada);
//        testarVecindades(caso, entrada, patrones, poblacionInicial, solEntrada);

        // NOTE /////////////////////////////////////////////////////////////////////////////////////////////////////


        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        String sb = caso + "-" + ejecucion;
        rwFiles.EscrituraExcel.EscrituraSoluciones(sb/*caso + "-" /*+ ejecucion*/ /*+ "-Inicial+Fase1+Fase2"*/,
                main.Main.carpetaSoluciones, solEntrada, entrada, patrones, parametros, parametrosAlgoritmo);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private static void ajusteCiclosBusqueda(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                 ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {

        int[] ciclos = new int[]{50, 100, 1000, 5000, 6000, 7000, 10000};
        for (int iter : ciclos) {
            parametrosAlgoritmo.VNS.setNumIteracionesParaComprobarCondicionParadaPorcentaje(iter);
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/4-NumCiclosPorcentajeMejoria/" +
                    iter + "/";
            System.err.println(carpetaTrazas);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        }
    }


    private static void ajusteTipoEntornos(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                           ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {

        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-Vecindades/" + "TipoEntornos/determinista/";
        System.err.println(carpetaTrazas);
        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
    }


    private static void ajusteCiclosProbabilidad(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                    ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {

//        int[] ciclos = new int[]{1, 5, 10, 25, 50, 100, 1000};
        int[] ciclos = new int[]{60, 65, 70, 75, 80, 85, 90, 95};
        for (int iter : ciclos) {
            parametrosAlgoritmo.VNS.setCambioProbabilidadIteraciones(iter);
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-Vecindades/" + "probabilisticos/iter/" +
                    iter + "/";
            System.err.println(carpetaTrazas);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        }
    }


    private static void ajusteVariacionProbabilidad(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                    ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {

        double[] variacion = new double[]{0.001, 0.01, 0.1, .2};
        for (double var : variacion) {
            parametrosAlgoritmo.VNS.setVariacionProbabilidad(var);
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-Vecindades/" + "probabilisticos/var/" +
                    var + "/";
            System.err.println(carpetaTrazas);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        }
    }


    private static void ajusteProbabilidadDiversif(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                   ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {

        double[] probabilidadDiversificacion = new double[]{1, 0.95, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
        for (double prob : probabilidadDiversificacion) {
            parametrosAlgoritmo.VNS.setProbabilidadDiversificacion(prob);
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-Vecindades/" + "probabilisticos/prob/" +
                    "prob-" + prob + "/";
            System.err.println(carpetaTrazas);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
        }
    }

    private static void ajusteOrdenEntornos(String caso, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {
        char[] identificadores = new char[]{'a', 'b', 'c', 'd'};
        String[] valores = new String[]{
                "movRejilla,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movLibre",
                "movMaxCarga,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movLibre",
                "movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movLibre",
                "movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movLibre"
        };
        for (int i = 0; i < valores.length; i++) {
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-OrdenEntornos_Bis/" + identificadores[i] + "/";
            System.err.println(carpetaTrazas);
            executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, valores[i]);
        }
    }


    private static void ajusteVecindades(String caso, Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {
        char[] identificadores = new char[]{'a', 'b', 'c', 'd'};
        String[] valores = new String[]{
                "movRejilla,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movLibre",
                "movMaxCarga,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movLibre",
                "movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movLibre",
                "movRejilla.1,movRejilla.2,movRejilla.3,movRejilla.4,movMaxCarga.1,movMaxCarga.2,movMaxCarga.3,movMaxCarga.4,movLibre"
        };
        double[] probabilidadDiversificacion = new double[]{/*1, 0.95,*/ .9, .8, .7, .6, .5, .4, .3, .2, .1};
        double[] variacion = new double[]{0.001, 0.01, 0.1, .2};
        int[] ciclos = new int[]{1, 5, 10, 25, 50, 100, 1000};
        for (double prob : probabilidadDiversificacion) {
            parametrosAlgoritmo.VNS.setProbabilidadDiversificacion(prob);
            for (double vari : variacion) {
                parametrosAlgoritmo.VNS.setVariacionProbabilidad(vari);
                for (int ciclo : ciclos) {
                    parametrosAlgoritmo.VNS.setCambioProbabilidadIteraciones(ciclo);
                    for (int i = 0; i < valores.length; i++) {
                        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/2-Vecindades/" + "probabilisticos/" +
                                "prob-" + prob + "/" + "var-" + vari + "/iter-" + ciclo + "/" + identificadores[i] + "/";
                        System.err.println(carpetaTrazas);
                        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada, valores[i]);
                    }
                }
            }
        }
    }

    private static void ajusteTipoVNS(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                      ParametrosAlgoritmo parametrosAlgoritmo,
                                      ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {
        for (String tipoVNS : new String[]{"VND", "RVNS", "BVNS", "GVNS", "SVNS"}) {
            carpetaTrazas = "resultados/" + entradaPath + entradaId + "/1-TipoVNS/" + tipoVNS + "/";
            parametrosAlgoritmo.VNS.setTipoVNS(tipoVNS);
            if (tipoVNS.equals("SVNS")) {

                String[] funcionesDistancia = {/*"slots", */"fitness"};
                double[] alphas = new double[]{0.5, /*1, 2, 5, 10, */15, 20, 30, 40, 50};
                for (String funcionDistancia : funcionesDistancia) {
                    parametrosAlgoritmo.VNS.setFuncionDistancia(funcionDistancia);
                    for (double alpha : alphas) {
                        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/" + tipoVNS + "/" + funcionDistancia + "/" + alpha + "/";
                        parametrosAlgoritmo.VNS.setAlpha(alpha);
                        System.err.println(carpetaTrazas);
                        executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
                    }
                }
            } else {
                System.err.println(carpetaTrazas);
                executeXTimesVNS(10, caso, entrada, patrones, parametros, parametrosAlgoritmo, poblacionInicial, solEntrada);
            }
        }
    }


    private static void testarAlphas(String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                     ParametrosAlgoritmo parametrosAlgoritmo,
                                     ArrayList<Solucion> poblacionInicial, ArrayList<Solucion> solEntrada) {
//        double[] alphas = new double[]{15, 15.5, 16, 16.5, 17, 17.5, 18, 18.5, 19, 19.5, 20};
        double[] alphas = new double[]{/*0.5, 1, */2, /*5, 10, */15, 20, 30, 40, 50};
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
            Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, str, carpetaTrazas);
    }

    private static void executeXTimesVNS(int X, String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                         ParametrosAlgoritmo parametrosAlgoritmo,
                                         List<Solucion> poblacionInicial, List<Solucion> solEntrada) {
        for (int i = 0; i < X; i++)
            Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, "", carpetaTrazas);
    }

    private static void executeXTimesVNSAndSave(int X, String caso, Entrada entrada, Patrones patrones, Parametros parametros,
                                                ParametrosAlgoritmo parametrosAlgoritmo,
                                                List<Solucion> poblacionInicial, List<Solucion> solEntrada, String str) {
        for (int i = 0; i < X; i++) {
            List<Solucion> r =
                    Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, str, carpetaTrazas);
            if (i == 9) solEntrada.addAll(r);
        }

    }


    public static void switchCase(String caso) {
        switch (caso) {
            case "Caso1":
                entradaPath = "Caso1";
                entradaId = "Id1m-01-01-2019";
                entorno = "Barcelona";
                break;
            case "Caso2":
                entradaPath = "Caso2";
                entradaId = "xxx";
                entorno = "Sevilla";
                break;
            case "Caso3":
                entradaPath = "Caso3";
                entradaId = "Id2m-01-01-2019";
                entorno = "Barcelona";
                break;
            case "Caso4":
                entradaPath = "Caso4";
                entradaId = "Id3t-16-01-2019";
                entorno = "Madrid";
                break;
            case "Caso5":
                entradaPath = "Caso5";
                entradaId = "Id5t-16-01-2019";
                entorno = "Madrid";
                break;
            case "Caso6":
                entradaPath = "Caso6";
                entradaId = "Id4t-14-01-2019";
                entorno = "Madrid";
                break;
            case "Caso7":
                entradaPath = "Caso7";
                entradaId = "Id6t-19-10-2018";
                entorno = "Barcelona";
                break;
            case "Caso8":
                entradaPath = "Caso8";
                entradaId = "Id0m-13-09-2018";
                entorno = "Palma";
                break;
            case "Caso9":
                entradaPath = "Caso9";
                entradaId = "Id1m-13-09-2018";
                entorno = "Palma";
                break;
            default:
                System.err.println("No es un caso predefinido, introducir ID y entorno");
                break;
        }

    }

}
