package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
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

import static herramientas.CridaUtils.LONGITUD_CADENAS;

public class MoveLibre extends AbstractNeighborStructure {

    public MoveLibre(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        super(entrada, patrones, parametros, parametrosAlgoritmo);
    }

    /**
     * case "movimiento3": Intervalo aleatorio entre max y min con multiplos de 3 slots
     * <br/>
     * Este movimiento genera una nueva solucion a partir de un cambio de un intervalo de trabajo entre
     * dos controladores,
     * a la hora de escoger el intervalo se utiliza una granularidad aleatoria que sea multiplo de 3
     * slots (15 mins) entre un maximo y minimo simpre y si esto no genera un individuo valido,
     * se prueba a modificar el segundo controlador (el que acepta el intervalo), si sigue sin generar un
     * individuo valido,
     * se reduce en uno el tamaño del intervalo, asi hasta llegar a una granularidad minima, despues de
     * esto se modifica el primer controlador.
     * <p>
     * Cuando se consigue hacer un intercambio, se retorna la nueva solucion
     */
    @Override
    protected Object[] buscarSolucion(Solucion x_inicial, int c1, int c2) {
        Solucion x = x_inicial.clone();
        int c1_inicial = c1, c2_inicial = c2;

        List<Integer> c1Indices = IntStream.range(0, x.getTurnos().size())
                .boxed().collect(Collectors.toList());


        // paso 1: elegimos un controlador aleatoriamente
        while (c1Indices.size() > 0) {
            if (c1_inicial == -1) {
                int idx1 = random.nextInt(c1Indices.size());
                c1 = c1Indices.get(idx1);
                c1Indices.remove(idx1); // para evitar repetidos
            }
            // paso 2 se elige un periodo de trabajo aleatoriamente
            List<int[]> trabajosC1 = getIntervalos(x.getTurnos().get(c1));
            Set<Integer> trabajoC1Indices = IntStream.range(0, trabajosC1.size())
                    .boxed().collect(Collectors.toSet());
            while (trabajoC1Indices.size() > 0) {
                int intervaloIdx = random.nextInt(trabajosC1.size());
                int[] periodo = trabajosC1.get(intervaloIdx);
                trabajoC1Indices.remove(intervaloIdx);

                // y la logitud
                List<Integer> longitudesList = new ArrayList<>();
                int maxLongIntervalo = (periodo[1] - periodo[0]);
                for (int i = LONGITUD_CADENAS; i <= maxLongIntervalo; i += LONGITUD_CADENAS)
//                    if (i % InicializarPoblacion.descanso == 0)  // NOTE si permitimos movimientos libres no solo de multiplos del tamaño de los descansos, hay mejoria en el fitness (descomentar linea para dejar de permitir movimientos de tamaño libre)
                    longitudesList.add(i);
                // intentamos mover la mayor carga posible
                for (int idxLongitud = longitudesList.size() - 1; idxLongitud >= 0; idxLongitud--) {
//                while (longitudesList.size() > 0) { // (NOTE: lo comentado es la version anterior)
//                    int idxLongitud = random.nextInt(longitudesList.size()); // NOTE: esto podria ser otro movimiento, que en lugar de optar por el mayor cambio de carga, lo haga aleatoriamente!!
                    int longitud = longitudesList.get(idxLongitud);
//                    longitudesList.remove(idxLongitud);


                    // paso 3 elegimos un segundo controlador
                    List<Integer> c2Indices = IntStream.range(0, x.getTurnos().size())
                            .boxed().collect(Collectors.toList());
                    c2Indices.remove(c1); // no hay que comparar consigo mismo

                    while (c2Indices.size() > 0) {
                        if (c2_inicial == -1) {
                            int idx2 = random.nextInt(c2Indices.size());
                            c2 = c2Indices.get(idx2);
                            c2Indices.remove(idx2); // para evitar repetidos
                        }
//                        if (!x.getControladores().contains(c2)) { // si es imaginario, puede aceptar la carga perfectamente
//                            doChange();
//                            return x;
//                        }
                        // sino, hay que comprobar que los nucleos sean compatibles con el controlador
                        Set<String> sectoresC1 = obtenerSectores(x, c1, periodo[0], periodo[0] + longitud);
                        Set<String> sectoresC2 = obtenerSectores(x, c2, periodo[0], periodo[0] + longitud);
                        if (sectoresC1 == null || sectoresC2 == null) continue;

                        if (comprobarNucleos(sectoresC1, sectoresC2,
                                CridaUtils.obtenerControladorTurno(c1, x.getControladores()),
                                CridaUtils.obtenerControladorTurno(c2, x.getControladores()))
                        ) {
                            doChange(x, x.getTurnos().get(c1), x.getTurnos().get(c2),
                                    periodo[0], periodo[0] + longitud, c1, c2);

                            return new Object[]{
                                    MetaheuristicUtil.orderByLazyCriteria(x),
                                    c1, c2
                            };
                        } else if (c2_inicial != -1) return new Object[]{x_inicial, -1, -1};
                    }
                }
            }


        }
        return new Object[]{x_inicial, -1, -1};
    }


    @Override
    public Object[] generarSolucionAleatoria(Solucion x) {
        return buscarSolucion(x, -1, -1);
    }

    @Override
    public String toString() {
        return "MoveLibre";
    }
}
