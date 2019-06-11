package main;

import InicializarPoblacion.InicializarPoblacion;
import algorithms.VNS.VNS;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import fitnessFunction.DeciderFitnessFunction;
import patrones.Patrones;
import patrones.Restricciones;

import java.util.ArrayList;

public class Main_VNS_pablo {

    public static void main_vns(Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada,
                                Patrones patrones) {

        /*INICIALIZACION DE SOLUCIONES FACTIBLES*/
        ArrayList<Solucion> poblacionInicial = InicializarPoblacion.inicializarPoblacion(entrada, parametros, patrones);
        //ArrayList<Solucion> poblacionInicial = new ArrayList<Solucion>();poblacionInicial.add(poblacionInicial1.get
        // (0));
        ArrayList<Solucion> poblacionReducirControladores = VNS.bucleVNS(poblacionInicial, parametrosAlgoritmo,
                parametros, patrones, entrada);

        double res = 0;
        ArrayList<Solucion> poblacionFactible = new ArrayList<>();
        for (int i = 0; i < poblacionReducirControladores.size(); i++) {
            if ((res = Restricciones.penalizacionPorRestricciones(poblacionReducirControladores.get(i), patrones,
                    entrada, parametros)) == 0) {
                poblacionFactible.add(poblacionReducirControladores.get(i));
            }
            System.out.println(i + "-Restricciones incumplidas: " + res);
        }
        /*PRESENTACION DE RESULTADOS Y TRAZAS*/

        rwFiles.EscrituraExcel.EscrituraSoluciones("PoblacionFactible", Main.carpetaSoluciones,
                poblacionReducirControladores, entrada, patrones, parametros, parametrosAlgoritmo);
        trazas.Trazas.archivarYLimpiarTrazas(poblacionReducirControladores, Main.propFileOptions, parametrosAlgoritmo);
        trazas.Trazas.limpiarTrazas();
        /*FIN PRESENTACION DE RESULTADOS Y TRAZAS*/
        System.out.println("Done");

    }
}
