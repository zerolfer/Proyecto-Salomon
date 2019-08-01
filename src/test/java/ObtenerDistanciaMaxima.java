import InicializarPoblacion.InicializarPoblacion;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main_VNS;
import patrones.Patrones;
import pruebasCasos.DeciderCase;

import java.util.ArrayList;

import static main.Main.*;

public class ObtenerDistanciaMaxima {

    private static Parametros parametros;
    private static ParametrosAlgoritmo parametrosAlgoritmo;

    public static void main(String[] args) {
        int nEjecucion = 1;
        int[] casos = {1, 3, 4, 5, 6, 7, 8, 9};
        for (int caso : casos) main1(nEjecucion, "Caso" + caso);
    }

    private static void main1(int ejecucion, String caso) {
        /*INICIALIZACION DE DATOS*/
        DeciderCase.switchCase(caso);

        // Carga de los parámetros del dominio del problema:
        parametros = new Parametros(propFileParameters, propFileOptions);

        // Carga de los parámetros del algoritmo
        parametrosAlgoritmo = new ParametrosAlgoritmo();

        Entrada entrada = Entrada.leerEntrada(parametros, entradaPath, entradaId, entorno);
        Patrones patrones = new Patrones(entrada, parametros);

        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Soluciones/";
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + parametrosAlgoritmo.getAlgoritmo() + "/Trazas/";

        // la distribucion inicial está en "entrada"
        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);

        parametrosAlgoritmo.VNS.setTipoVNS("SVNS");
//        for (int i = 0; i < 1000; i++) TODO
            Main_VNS.main_vns(caso, parametros, parametrosAlgoritmo, entrada, patrones, poblacionInicial, "");


    }
}
