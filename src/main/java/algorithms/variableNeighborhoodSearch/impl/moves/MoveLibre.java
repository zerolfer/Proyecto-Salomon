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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static herramientas.CridaUtils.LONGITUD_CADENAS;
import static herramientas.CridaUtils.STRING_NO_TURNO;

public class MoveLibre extends AbstractNeighborhoodStructure {

    MoveLibre(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
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
    protected Solucion buscarSolucion(Solucion x_inicial) {
        Solucion x = x_inicial.clone();
        List<Integer> c1Indices = IntStream.range(0, x.getTurnos().size())
                .boxed().collect(Collectors.toList());


        // paso 1: elegimos un controlador aleatoriamente
        while (c1Indices.size() > 0) {
            int idx1 = Random.nextInt(c1Indices.size());
            int c1 = c1Indices.get(idx1);
            c1Indices.remove(idx1); // para evitar repetidos

            // paso 2 se elige un periodo de trabajo aleatoriamente
            List<int[]> trabajosC1 = getIntervalos(x.getTurnos().get(c1));
            Set<Integer> trabajoC1Indices = IntStream.range(0, trabajosC1.size())
                    .boxed().collect(Collectors.toSet());
            while (trabajoC1Indices.size() > 0) {
                int intervaloIdx = Random.nextInt(trabajosC1.size());
                int[] periodo = trabajosC1.get(intervaloIdx);
                trabajoC1Indices.remove(intervaloIdx);

                // y la logitud
//                List<Integer> longitudesList = new ArrayList<>();
                int maxLongIntervalo = (periodo[1] - periodo[0]);
//                for (int i = LONGITUD_CADENAS; i <= maxLongIntervalo; i += LONGITUD_CADENAS)
//                    if (i % InicializarPoblacion.descanso == 0)  // NOTE si permitimos movimientos libres no solo de multiplos del tamaño de los descansos, hay mejoria en el fitness (descomentar linea para dejar de permitir movimientos de tamaño libre)
//                    longitudesList.add(i);
                // intentamos mover la mayor carga posible
                for (int longitud = maxLongIntervalo; longitud > 0; longitud -= LONGITUD_CADENAS) { // TODO verificar que este cambio es correcto
//                while (longitudesList.size() > 0) { // (NOTE: lo comentado es la version anterior)
//                    int idxLongitud = Random.nextInt(longitudesList.size()); // NOTE: esto podria ser otro movimiento, que en lugar de optar por el mayor cambio de carga, lo haga aleatoriamente!!
//                    int longitud = longitudesList.get(idxLongitud);
//                    longitudesList.remove(idxLongitud);


                    // paso 3 elegimos un segundo controlador
                    List<Integer> c2Indices = IntStream.range(0, x.getTurnos().size())
                            .boxed().collect(Collectors.toList());
                    c2Indices.remove(c1); // no hay que comparar consigo mismo

                    while (c2Indices.size() > 0) {
                        int idx2 = Random.nextInt(c2Indices.size());
                        int c2 = c2Indices.get(idx2);
                        c2Indices.remove(idx2); // para evitar repetidos

//                        if (!x.getControladores().contains(c2)) { // si es imaginario, puede aceptar la carga perfectamente
//                            doChange();
//                            return x;
//                        }

                        // Aquí se comprueban las restricciones de dominio
                        if (x.getTurnos().get(c1).substring(periodo[0], periodo[0] + longitud).contains(STRING_NO_TURNO)
                                || x.getTurnos().get(c2).substring(periodo[0], periodo[0] + longitud).contains(STRING_NO_TURNO))
                            continue; // NOTE: o ´return x´, si queremos que no se prueben todos

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

                            return MetaheuristicUtil.reordenarYEliminarTurnos(x);
                        }
                    }
                }
            }


        }
        return x_inicial;
    }


    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return buscarSolucion(x);
    }

    @Override
    public String toString() {
        return "MoveLibre";
    }
}
