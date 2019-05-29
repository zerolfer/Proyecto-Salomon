package main;

import InicializarPoblacion.InicializarPoblacion;
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
        int[] casos = {1, 3};

        main1(nEjecucion, "Caso" + casos[0]);
    }

    public static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(/*caso*/"Caso" + loadCasoFromProperties());

        // Carga de los parámetros del dominio del problema:
        Parametros parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        ParametrosAlgoritmo parametrosAlgoritmo = new ParametrosAlgoritmo();

        //////////////////////////////////////////////////////////////////////////////////////
        parametrosAlgoritmo.sobreescribirParametrosViaExterna(propFileExternoParametros); // HACK: USAR PARA EL JAR DEPLOYMENT
        //////////////////////////////////////////////////////////////////////////////////////
        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);
        Patrones patrones = new Patrones(entrada, parametros);


        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        // la distribucion inicial está en "entrada"
        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);

        switch (parametrosAlgoritmo.getAlgoritmo()) {
            case "SA":
                Main_SA.main_sa(parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial);
                break;
            case "VNS":
                Main_VNS.main_vns(parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial);
                break;
            default:
                System.err.println("Algoritmo \"" + parametrosAlgoritmo.getAlgoritmo() + "\" no encontrado.");
                break;
        }

        Patrones.nuc = new ArrayList<>();
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
