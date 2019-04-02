package fitnessFunction;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

/**
 * Clase utilizada para la eleccion de la funcion fitness a partir de los ficheros .properties.
 *
 * @author Tino
 */
public class DeciderFitnessFunction {
    public static double[] switchFitnessF(Solucion individuo, Patrones patrones, Entrada entrada, Parametros parametros, ParametrosAlgoritmo parametrosAlg) {
        String funFit = parametrosAlg.getFuncionFitnessFase2();
        double[] fit = {0, 0, 0, 0, 0};
        /*Si se esta utilizando el SA solo se utilizara el valor fit[0]
         * */
        switch (funFit) {
            case "reduccionControladoresAIS":
                fit = Fitness.fitReduccionControladoresAIS(individuo, entrada, patrones, parametros);
                break;
            case "OnlyConstrains":
                fit = Fitness.fitnessOnlyConstrains(individuo, entrada, patrones, parametros);
                break;
            case "reduccionControladoresYRestricciones":
                fit = Fitness.fitPonderadoRestricYNumCtrls(individuo, entrada, patrones, parametros);
                break;
            case "reduccionControladoresYRestricciones2":
                /*Se penaliza unicamente las filas sin controladores asignados: Sin finalizar*/
                fit = Fitness.fitPonderadoRestricYNumCtrls2(individuo, entrada, patrones, parametros);
                break;
            case "fitGlobal":
                fit = Fitness.fitGlobal(individuo, entrada, patrones, parametros);
                break;
            case "other":
                System.out.println("REVISAR PROPERTIES: funcionFitness");
                break;

        }
        return fit;
    }
}
