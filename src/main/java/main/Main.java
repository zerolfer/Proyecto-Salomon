package main;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;
import pruebasCasos.DeciderCase;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static String propFileParameters = Main.class.getResource("/problemParameters.properties").getPath();
    public static String propFileOptions = Main.class.getResource("/options.properties").getPath();
//    public static String propFileParametersAlgorithm = Main.class.getResource("/algorithm.properties").getPath();

    public static String entradaPath = "Caso2";
    public static String entradaId = "Id1n-06-03-2017";
    public static String entorno = "Canarias";

    public static String carpetaSoluciones = "";
    public static String carpetaTrazas = "";
    public static Date date = new Date();

    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1};

        main1(nEjecucion, "Caso" + 1);
    }

    public static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(caso);//El caso4 no tiene solucion

        // Carga de los parámetros del dominio del problema:
        Parametros parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        ParametrosAlgoritmo parametrosAlgoritmo = new ParametrosAlgoritmo();

        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);
        Patrones patrones = new Patrones(entrada, parametros);


        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        switch (parametrosAlgoritmo.getAlgoritmo()) {
            case "SA":
                Main_SA.main_sa(parametros, parametrosAlgoritmo, entrada, patrones);
                break;
            case "VNS":
                //Main_VNS.main_vns(parametros, parametrosAlgoritmo, entrada, patrones);
                break;
            default:
                System.out.println("Algoritmo" + parametrosAlgoritmo.getAlgoritmo() + " no encontrado.");
                break;
        }

        Patrones.nuc = new ArrayList<>();
    }
}
