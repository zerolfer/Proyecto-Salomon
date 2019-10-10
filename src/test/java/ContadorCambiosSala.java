import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import main.Main_VNS;
import patrones.Patrones;
import pruebasCasos.DeciderCase;
import rwFiles.Lectura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static herramientas.lecturaSoluciones.LecturaSoluciones.leerSoluciones;
import static main.Main.*;

public class ContadorCambiosSala {

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

        carpetaSoluciones = "resultados/" + entradaPath + entradaId + "/" + "VNS" + "/Soluciones/"; //NOTE: cambiase a SA es su caso
        carpetaTrazas = "resultados/" + entradaPath + entradaId + "/" + "VNS" + "/Trazas/";

        String[] directories = new File(carpetaSoluciones).list();
        for (String dir : Objects.requireNonNull(directories)) {
            Solucion sol = leerSoluciones(carpetaSoluciones + dir + "/solucion" + 3 + ".txt", entrada);
            Object[] aux = contarCambiosSala(sol, entrada);
            System.out.printf("Cambios sala %s:  %d de %d (Normalizado: %.5f)\n",
                    carpetaSoluciones + dir, aux[0], sol.getTurnos().size(), aux[1]);
        }
        System.out.println();

    }

    private static Object[] contarCambiosSala(Solucion solucion, Entrada entrada) {
        int actual = entrada.getSlotMomentoActual() * 3;
        ArrayList<String> turnos = solucion.getTurnos();
        int contador_cambios_sala = 0;
        double fit = 0;
        double norm = 1.0 / turnos.size();
        for (String turno : turnos) {
            if (turno.substring(actual - 3, actual).equals(turno.substring(actual, actual + 3))) {
                fit += norm;
            } else contador_cambios_sala++;
        }
        return new Object[]{contador_cambios_sala, fit};
    }

//    private static Solucion obtenerSolucionFichero(String path, Entrada entrada) {
//        return new Solucion(parseEntrada(path), entrada.getControladores(), 0);
//    }

}
