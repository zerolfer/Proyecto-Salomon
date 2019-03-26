package algorithms.simulatedAnnealing.moves;

import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo_SA;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;

/**
 * Clase utilizada para la eleccion de las soluciones del entorno a partir de los ficheros .properties.
 *
 * @author Tino
 */
public class DeciderMove {
    /**
     * Metodo utilizado para la eleccion del movimiento.
     *
     * @param individuo        Solucion
     * @param gMax             Slots maximos que puede contener el intervalo.
     * @param gMin             Slots minimos que puede conterner el intervalo.
     * @param desviacionTipica Parametro para la eleccion de controlador (cuando se realiza de forma PseudoAleatoria).
     * @param parametrosAlg    Parametros del algoritmo.
     * @param patrones         Patrones utilizados para comprobar las restricciones.
     * @param entrada          Entrada del problema.
     * @param parametros       Parametros del problema.
     * @param iteracion        Array con informacion del las trazas del algoritmo.
     * @return Nueva solucion generada por el movimiento a partir de la anterior.
     */
    public static Solucion switchMoves(Solucion individuo, int gMax, int gMin, double desviacionTipica,
                                       ParametrosAlgoritmo_SA parametrosAlg, Patrones patrones, Entrada entrada,
                                       Parametros parametros, ArrayList<String> iteracion) {
        String move = parametrosAlg.getMovimientosEntorno();
        Solucion mov = null;
        switch (move) {
            case "movimiento1"://Intervalo maximo a minimo
                /*Este movimiento genera una nueva solucion a partir de un cambio de un intervalo de trabajo entre
                dos controladores,
                 * a la hora de escoger el intervalo se utiliza una granularidad maxima simpre primero y si esto no
                 * genera un individuo valido,
                 * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
                  * individuo valido,
                 * se reduce en uno el tamaño del intervalo, asi hasta llegar a una granularidad minima, despues de
                 * esto se modifica el primer controlador.*/
                mov = Move1.movimientoTrabajo(individuo, gMax, gMin, patrones, entrada, parametros, parametrosAlg,
                        iteracion);
                break;
            case "movimiento2"://Intervalo aleatorio entre max y min
                /*Este movimiento genera una nueva solucion a partir de un cambio de un intervalo de trabajo entre
                dos controladores,
                 * a la hora de escoger el intervalo se utiliza una granularidad aleatoria entre un maximo y minimo
                 * simpre y si esto no genera un individuo valido,
                 * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
                  * individuo valido,
                 * se prueba con otra granularidad distinta e igualmente aleatoria, asi hasta probar con todas,
                 * despues de esto se modifica el primer controlador.*/
                mov = Move2.movimientoTrabajoIntervalosAleatorios(individuo, gMax, gMin, patrones, entrada,
                        parametros, parametrosAlg, iteracion);
                break;
            case "movimiento3"://Intervalo aleatorio entre max y min con multiplos de 3 slots
                /*Este movimiento genera una nueva solucion a partir de un cambio de un intervalo de trabajo entre
                dos controladores,
                 * a la hora de escoger el intervalo se utiliza una granularidad aleatoria que sea multiplo de 3
                 * slots (15 mins) entre un maximo y minimo simpre y si esto no genera un individuo valido,
                 * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
                  * individuo valido,
                 * se reduce en uno el tamaño del intervalo, asi hasta llegar a una granularidad minima, despues de
                 * esto se modifica el primer controlador.*/
                mov = Move3.movimientoTrabajoIntervalosPseudoAleatorios(individuo, gMax, gMin, patrones, entrada,
                        parametros, parametrosAlg, iteracion);
                break;
            case "movimiento4"://Intervalo aleatorio entre max y min + Cruce entre controladores
                /*En este caso mezclamos dos movimientos con cierta probabilidad: 1. El primer movimiento genera una
                nueva solucion a partir de un cambio de un intervalo de trabajo entre dos controladores,
                 * a la hora de escoger el intervalo se utiliza una granularidad aleatoria que sea multiplo de 3
                 * slots (15 mins) entre un maximo y minimo simpre y si esto no genera un individuo valido,
                 * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
                  * individuo valido,
                 * se escoge otro tamaño de intervalo, asi hasta acabar con los numeros, despues de esto se modifica
                 * el primer controlador.
                 * 2. El segundo movimiento escoge dos controladores aleatorios, y sin escoger un intervalo de
                 * trabajo divide su turno en dos partes aleatoriamente intercambiando estas entre si, si es posible,
                 * si no escoge otros controladores aleatoriamente y repite hasta que sea posible*/
                mov = Move4.doblemovimiento(individuo, gMax, gMin, patrones, entrada, parametros, parametrosAlg,
                        iteracion);
                break;
            case "movimiento5"://Intercambia el turno de trabajo entre dos controladores
                mov = Move5.movimientoControladores(individuo);
                break;
            case "movimiento6"://Es el movimiento 3 realizado 2 veces
                ArrayList<String> tmp = new ArrayList<>();
                Solucion mov1 = Move6.movimientoTrabajoIntervalosPseudoAleatorios(individuo, gMax, gMin, patrones,
                        entrada, parametros, parametrosAlg, tmp);
                mov = Move6.movimientoTrabajoIntervalosPseudoAleatorios(mov1, gMax, gMin, patrones, entrada,
                        parametros, parametrosAlg, tmp);
//				 Solucion mov1 = Move6.movimientoTrabajoIntervalosPseudoAleatorios(individuo,gMax,gMin, patrones,
//				 entrada, parametros, parametrosAlg, tmp);
//				 mov = Move6.movimientoTrabajoIntervalosPseudoAleatorios(mov1,gMax,gMin, patrones, entrada,
//				 parametros, parametrosAlg, iteracion);

                break;
            case "movimiento7"://Es el movimiento 4 realizado 4 veces
                ArrayList<String> tmp1 = new ArrayList<>();
                mov1 = Move7.doblemovimiento(individuo, gMax, gMin, patrones, entrada, parametros, parametrosAlg, tmp1);
                mov1 = Move7.doblemovimiento(mov1, gMax, gMin, patrones, entrada, parametros, parametrosAlg, tmp1);
                mov1 = Move7.doblemovimiento(mov1, gMax, gMin, patrones, entrada, parametros, parametrosAlg, tmp1);
                mov = Move7.doblemovimiento(mov1, gMax, gMin, patrones, entrada, parametros, parametrosAlg, iteracion);
                break;
            case "movimiento8"://Es el movimiento 4 pero con una nueva forma de calcular si los sectores pertenecen
                // al mismo nucleo y no realiza cruce hasta que alzance el numero de controladores requerido.
                mov = Move8.doblemovimiento(individuo, gMax, gMin, patrones, entrada, parametros, parametrosAlg,
                        iteracion);
                break;
        }
        return mov;
    }
}
