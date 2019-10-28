package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import algorithms.variableNeighborhoodSearch.impl.AbstractNeighborhoodStructure;
import estructurasDatos.Auxiliares.Random;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MoveTemplate extends AbstractNeighborhoodStructure {

    protected MoveTemplate(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected Solucion buscarSolucion(Solucion x_inicial) {
        Solucion x = x_inicial.clone();
        List<Integer> c1Indices = IntStream.range(0, x.getTurnos().size())
                .boxed().collect(Collectors.toList());


        // paso 1: elegimos un controlador aleatoriamente
        while (c1Indices.size() > 0) {
            int c1 = obtenerIndiceControlador1(c1Indices, x);

            // paso 2 se elige un periodo de trabajo aleatoriamente
            List<int[]> trabajosC1 = obtenerTrabajosControlador1(x.getTurnos(), c1);
            Set<Integer> trabajoC1Indices = IntStream.range(0, trabajosC1.size())
                    .boxed().collect(Collectors.toSet());
            while (trabajoC1Indices.size() > 0) {
                int intervaloIdx = Random.nextInt(trabajosC1.size());
                int[] periodo = trabajosC1.get(intervaloIdx);
                trabajoC1Indices.remove(intervaloIdx);

                // paso 3 elegimos un segundo controlador
                List<Integer> c2Indices = IntStream.range(0, x.getTurnos().size())
                        .boxed().collect(Collectors.toList());
                c2Indices.remove(c1); // no hay que comparar consigo mismo

                while (c2Indices.size() > 0) {
//                    int idx2 = Random.nextInt(c2Indices.size());
//                    int c2 = c2Indices.get(idx2);
//                    c2Indices.remove(idx2); // para evitar repetidos
                    int c2 = obtenerIndiceControlador2(c2Indices);

                    if (!comprobarRestriccionesMovimiento(x, c1, c2, periodo[0], periodo[1]))
                        continue; // NOTE: o ´return x´, si queremos que no se prueben todos

                    // sino, hay que comprobar que los nucleos sean compatibles con el controlador
                    Set<String> sectoresC1 = obtenerSectores(x, c1, periodo[0], periodo[1]);
                    Set<String> sectoresC2 = obtenerSectores(x, c2, periodo[0], periodo[1]);
                    if (sectoresC1 == null || sectoresC2 == null) continue;

                    // si es posible hacer el cambio...
                    if (comprobarNucleos(sectoresC1, sectoresC2,
                            CridaUtils.obtenerControladorTurno(c1, x.getControladores()),
                            CridaUtils.obtenerControladorTurno(c2, x.getControladores()))
                    ) {
                        doChange(x, x.getTurnos().get(c1), x.getTurnos().get(c2),
                                periodo[0], periodo[1], c1, c2);

                        return MetaheuristicUtil.reordenarYEliminarTurnos(x);
                    }
                }
            }
        }
        return x_inicial;

    }


    /**
     * Forma de obtener los indices del controlador 1.
     * dos alternativas: aleatoriamente ({@link #obtenerIndiceControlador1Aleatoriamente(List)})
     * y en orden ({@link #obtenerIndiceControlador1EnOrden(List)})
     */
//    protected abstract int obtenerIndiceControlador1(List<Integer> c1Indices, Solucion x);
    protected int obtenerIndiceControlador1(List<Integer> c1Indices, Solucion x) {
        if (MetaheuristicUtil.esImaginario(c1Indices.get(0), x))
            return obtenerIndiceControlador1EnOrden(c1Indices);
        else return obtenerIndiceControlador1Aleatoriamente(c1Indices);
    }

    protected int obtenerIndiceControlador1Aleatoriamente(List<Integer> c1Indices) {
        int idx1 = Random.nextInt(c1Indices.size());
        int c1 = c1Indices.get(idx1);
        c1Indices.remove(idx1); // para evitar repetidos
        return c1;
    }

    int obtenerIndiceControlador1EnOrden(List<Integer> c1Indices) {
        int c1 = c1Indices.get(0);
        c1Indices.remove(0); // para evitar repetidos
        return c1;
    }

    protected int obtenerIndiceControlador2(List<Integer> c2Indices) {
        return obtenerIndiceControlador1Aleatoriamente(c2Indices);
    }


    /**
     * Formas de obtener los trabajos del controlador 1.
     * Dos opciones:
     * <p>
     * mediante intervalos de trabajo:     getIntervalos(x.getTurnos().get(c1)); </br>
     * </p>
     * <p>
     * mediante rejillas:                  super.getRejillas(x.getTurnos());
     * </p>
     */
    protected abstract List<int[]> obtenerTrabajosControlador1(ArrayList<String> turnos, int c1);


    /**
     * cada subtipo de movimiento tendrá sus restricciones concretas
     *
     * @param x
     * @param c1
     * @param c2
     * @param desde
     * @param hasta
     */
    protected abstract boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int desde, int hasta);

    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return buscarSolucion(x);
    }

}
