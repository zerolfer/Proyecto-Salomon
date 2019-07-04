package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.CridaUtils;
import patrones.Patrones;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Move5 extends AbstractNeighborStructure {

    protected Move5(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    @Override
    protected Solucion buscarSolucion(Solucion x_inicial) {
        Solucion x = x_inicial.clone();
        List<Integer> c1Indices = IntStream.range(0, x.getTurnos().size())
                .boxed().collect(Collectors.toList());


        // paso 1: elegimos un controlador EN ORDEN
        while (c1Indices.size() > 0) {
            int c1 = c1Indices.get(0);
            c1Indices.remove(0); // para evitar repetidos

            // paso 2 se elige un periodo de trabajo aleatoriamente
//            List<int[]> trabajosC1 = getintervalos(x.getTurnos().get(c1)); // NOTE: para usar intervalos de trabajo
            List<int[]> trabajosC1 = super.getRejillas(x.getTurnos(), c1);
            Set<Integer> trabajoC1Indices = IntStream.range(0, trabajosC1.size())
                    .boxed().collect(Collectors.toSet());
            while (trabajoC1Indices.size() > 0) {
                int intervaloIdx = random.nextInt(trabajosC1.size());
                int[] periodo = trabajosC1.get(intervaloIdx);
                trabajoC1Indices.remove(intervaloIdx);

                // paso 3 elegimos un segundo controlador
                List<Integer> c2Indices = IntStream.range(0, x.getTurnos().size())
                        .boxed().collect(Collectors.toList());
                c2Indices.remove(c1); // no hay que comparar consigo mismo

                while (c2Indices.size() > 0) {
                    int idx2 = random.nextInt(c2Indices.size());
                    int c2 = c2Indices.get(idx2);
                    c2Indices.remove(idx2); // para evitar repetidos

                    if (!comprobarRestriccionesMovimiento(x, c1, c2, periodo[0], periodo[1]))
                        continue; // NOTE o ´return x´, segun si queremos o no que se prueben todos

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
     * cada subtipo de movimiento tendrá sus restricciones concretas
     *
     * @param x
     * @param c1
     * @param c2
     * @param i
     * @param i1
     */
    protected abstract boolean comprobarRestriccionesMovimiento(Solucion x, int c1, int c2, int i, int i1);

    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return buscarSolucion(x);
    }

}
