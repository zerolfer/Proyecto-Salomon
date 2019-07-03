package main;

import InicializarPoblacion.InicializarPoblacion;
import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;
import pruebasCasos.DeciderCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static String propFileExternoParametros = "./parametros_algoritmo.properties";
    public static URL propFileParameters = Main.class.getResource("/problemParameters.properties");
    public static URL propFileOptions = Main.class.getResource("/options.properties");
//    public static String propFileParametersAlgorithm = Main.class.getResource("/algorithm.properties").getPath();

    public static String entradaPath = "Caso1";
    public static String entradaId = "Id1n-06-03-2017";
    public static String entorno = "Canarias";

    public static String carpetaSoluciones = "";
    public static String carpetaTrazas = "";
    public static Date date = new Date();

    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1,3,4,5,6,7,8,9};
        for (int i = 0; i < casos.length; i++) {
        main1(nEjecucion, "Caso" + casos[i]);// loadCasoFromProperties());
		}
    }

    public static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(caso);

        // Carga de los parámetros del dominio del problema:
        Parametros parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        ParametrosAlgoritmo parametrosAlgoritmo = new ParametrosAlgoritmo();

        // HACK: USAR PARA EL JAR DEPLOYMENT /////////////////////////////////////////////////////////
    //    parametrosAlgoritmo.sobreescribirParametrosViaExterna(propFileExternoParametros);
        //////////////////////////////////////////////////////////////////////////////////////////////
        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);
        Patrones patrones = new Patrones(entrada, parametros);

        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<Solucion> solEntrada = new ArrayList<>();
        solEntrada.add(entrada.getDistribucionInicial());
        //
        //  rwFiles.EscrituraExcel.EscrituraSoluciones("OutputInicial", Main.carpetaSoluciones,
        //          solEntrada, entrada, patrones, parametros, parametrosAlgoritmo);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // la distribucion inicial está en "entrada"
        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);

        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
//        poblacionInicial.forEach(MetaheuristicUtil::orderByLazyCriteria);
        solEntrada.addAll(poblacionInicial);
//        rwFiles.EscrituraExcel.EscrituraSoluciones("Inicial+Fase1", Main.carpetaSoluciones, solEntrada,
//                entrada, patrones, parametros, parametrosAlgoritmo);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        switch (parametrosAlgoritmo.getAlgoritmo()) {
            case "SA":
                solEntrada.addAll(
                        Main_SA.main_sa(parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial)
                );
                break;
            case "VNS":
                solEntrada.addAll(
                        Main_VNS.main_vns(parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial)
                );
                break;
            default:
                System.err.println("Algoritmo \"" + parametrosAlgoritmo.getAlgoritmo() + "\" no encontrado.");
                break;
        }

        Patrones.nuc = new ArrayList<>();

        // OUTPUT ///////////////////////////////////////////////////////////////////////////////////////////////////
        StringBuilder sb = new StringBuilder();
        sb.append(caso);
        sb.append("-");
        sb.append(ejecucion);
//        sb.append("-Inicial+Fase1+Fase2");

        rwFiles.EscrituraExcel.EscrituraSoluciones(caso + "-" /*+ ejecucion*/ + "-DistribuciónInicial+SolucionInicial+Final",
                Main.carpetaSoluciones, solEntrada, entrada, patrones, parametros, parametrosAlgoritmo);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    // HACK: usar solo en la fase JAR DEPLOYMENT
    private static String loadCasoFromProperties() {
        Properties parametrosExternos = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(propFileExternoParametros);
            parametrosExternos.load(input);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return parametrosExternos.getProperty("numeroDelCasoParaResolver");

    }

}
