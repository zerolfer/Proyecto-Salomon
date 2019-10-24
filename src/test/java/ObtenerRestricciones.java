import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;
import patrones.Restricciones;
import pruebasCasos.DeciderCase;

import java.util.HashMap;

import static herramientas.lecturaSoluciones.LecturaSoluciones.leerSoluciones;
import static main.Main.*;

public class ObtenerRestricciones {


    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < casos.length; i++) {
            System.out.println("caso " + casos[i]);
            for (String[] s : mapa.get(casos[i]))
                main1(nEjecucion, "Caso" + casos[i], s);
            System.out.println();
        }
    }

    static HashMap<Integer, String[][]> mapa;

    static {
        mapa=new HashMap<>();
        mapa.put(1, new String[][]{
                {"Caso1Id1m-01-01-2019", "Caso1-1_CurrentDate_15-08-2019-21-31-29"},
                {"Caso1Id1m-01-01-2019", "Caso1-1_CurrentDate_15-08-2019-21-41-30"},
                {"Caso1Id1m-01-01-2019", "Caso1-1_CurrentDate_15-08-2019-21-51-31"},
        });
        mapa.put(3, new String[][]{
                {"Caso3Id2m-01-01-2019", "Caso3-1_CurrentDate_15-08-2019-22-01-31"},
                {"Caso3Id2m-01-01-2019", "Caso3-1_CurrentDate_15-08-2019-22-11-31"},
                {"Caso3Id2m-01-01-2019", "Caso3-1_CurrentDate_15-08-2019-22-21-32"},
        });
        mapa.put(4, new String[][]{
                {"Caso4Id3t-16-01-2019", "Caso4-1_CurrentDate_15-08-2019-22-31-32"},
                {"Caso4Id3t-16-01-2019", "Caso4-1_CurrentDate_15-08-2019-22-41-32"},
                {"Caso4Id3t-16-01-2019", "Caso4-1_CurrentDate_15-08-2019-22-51-32"},
        });
        mapa.put(5, new String[][]{
                {"Caso5Id5t-16-01-2019", "Caso5-1_CurrentDate_15-08-2019-23-01-32"},
                {"Caso5Id5t-16-01-2019", "Caso5-1_CurrentDate_15-08-2019-23-11-32"},
                {"Caso5Id5t-16-01-2019", "Caso5-1_CurrentDate_15-08-2019-23-21-33"},
        });
        mapa.put(6, new String[][]{
                {"Caso6Id4t-14-01-2019", "Caso6-1_CurrentDate_15-08-2019-23-31-33"},
                {"Caso6Id4t-14-01-2019", "Caso6-1_CurrentDate_15-08-2019-23-41-33"},
                {"Caso6Id4t-14-01-2019", "Caso6-1_CurrentDate_15-08-2019-23-51-33"},
        });
        mapa.put(7, new String[][]{
                {"Caso7Id6t-19-10-2018", "Caso7-1_CurrentDate_16-08-2019-00-01-33"},
                {"Caso7Id6t-19-10-2018", "Caso7-1_CurrentDate_16-08-2019-00-11-33"},
                {"Caso7Id6t-19-10-2018", "Caso7-1_CurrentDate_16-08-2019-00-21-34"},
        });
        mapa.put(8, new String[][]{
                {"Caso8Id0m-13-09-2018", "Caso8-1_CurrentDate_16-08-2019-00-31-34"},
                {"Caso8Id0m-13-09-2018", "Caso8-1_CurrentDate_16-08-2019-00-41-34"},
                {"Caso8Id0m-13-09-2018", "Caso8-1_CurrentDate_16-08-2019-00-51-34"},
        });
        mapa.put(9, new String[][]{
                {"Caso9Id1m-13-09-2018", "Caso9-1_CurrentDate_16-08-2019-01-01-34"},
                {"Caso9Id1m-13-09-2018", "Caso9-1_CurrentDate_16-08-2019-01-11-35"},
                {"Caso9Id1m-13-09-2018", "Caso9-1_CurrentDate_16-08-2019-01-21-35"},
        });
    }

    public static void main1(int ejecucion, String caso, String[] namefiles) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(caso);

        // Carga de los parámetros del dominio del problema:
        Parametros parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        ParametrosAlgoritmo parametrosAlgoritmo = new ParametrosAlgoritmo();

        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);

//        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
//        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        Solucion sol =
                leerSoluciones("C:\\Users\\Administrador\\Desktop\\Resultados SA y VNS\\" +
                        "Condicion Parada 10 Minutos\\" + namefiles[0] + "\\VNS\\Soluciones\\" + namefiles[1] +
                        "\\solucion2.txt", entrada);
        Patrones patrones = new Patrones(entrada, parametros);

        double res = Restricciones.penalizacionPorRestricciones(sol, patrones, entrada, parametros);
        System.out.println(res);
    }

}
