package algorithms.variableNeighborhoodSearch.impl.moves;

import algorithms.MetaheuristicUtil;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.DominioDelProblema.Nucleo;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static herramientas.CridaUtils.*;

public class Move3_ extends AbstractNeighborStructure {
    public Move3_(Entrada entrada, Patrones patrones, Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
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
        Set<Integer> c1Indices = IntStream.range(0, x.getTurnos().size())
                .boxed().collect(Collectors.toSet());


        // paso 1: elegimos un controlador aleatoriamente
        while (c1Indices.size() > 0) {
            int c1 = random.nextInt(c1Indices.size());
            c1Indices.remove(c1); // para evitar repetidos

            // paso 2 se elige un periodo de trabajo aleatoriamente
            List<int[]> trabajosC1 = getintervalos(x.getTurnos().get(c1));
            Set<Integer> trabajoC1Indices = IntStream.range(0, trabajosC1.size())
                    .boxed().collect(Collectors.toSet());
            while (trabajoC1Indices.size() > 0) {
                int intervaloIdx = random.nextInt(trabajosC1.size());
                int[] periodo = trabajosC1.get(intervaloIdx);
                trabajoC1Indices.remove(intervaloIdx);

                // y la logitud
                List<Integer> longitudesList = new ArrayList<>();
                int maxLongIntervalo = periodo[1] - periodo[0];
                for (int i = 3; i <= maxLongIntervalo; i++) // FIXME: < o <= ??
                    if (i % 3 == 0) longitudesList.add(i);
                // intentamos mover la mayor carga posible
                for (int idxLongitud = longitudesList.size() - 1; idxLongitud >= 0; idxLongitud--) {
//                while (longitudesList.size() > 0) { // (NOTE: lo comentado es la version anterior)
//                    int idxLongitud = random.nextInt(longitudesList.size()); // NOTE: esto podria ser otro movimiento, que en lugar de optar por el mayor cambio de carga, lo haga aleatoriamente!!
                    int longitud = longitudesList.get(idxLongitud);
//                    longitudesList.remove(idxLongitud);


                    // paso 3 elegimos un segundo controlador
                    Set<Integer> c2Indices = IntStream.range(0, x.getTurnos().size())
                            .boxed().collect(Collectors.toSet());
                    c2Indices.remove(c1); // no hay que comparar consigo mismo

                    while (c2Indices.size() > 0) {
                        int c2 = random.nextInt(c2Indices.size());
                        c2Indices.remove(c2); // para evitar repetidos

//                        if (!x.getControladores().contains(c2)) { // si es imaginario, puede aceptar la carga perfectamente
//                            doChange();
//                            return x;
//                        }
                        // sino, hay que comprobar que los nucleos sean compatibles con el controlador
                        Set<String> sectoresC1 = obtenerSectores(x, c1, periodo[0], periodo[0] + longitud);
                        Set<String> sectoresC2 = obtenerSectores(x, c2, periodo[0], periodo[0] + longitud);
                        if (comprobarNucleos(sectoresC1, sectoresC2)) {
                            doChange(x, x.getTurnos().get(c1), x.getTurnos().get(c2),
                                    periodo[0], periodo[0] + longitud, c1, c2);

                            return x;
                        }
                    }
                }
            }


        }
        return x_inicial;
    }

    private Set<String> obtenerSectores(Solucion x, int controlador, int desde, int hasta) {
        String turno = x.getTurnos().get(controlador).substring(desde, hasta);
        Set<String> sectores = new HashSet<>();
        for (int i = 0; i < turno.length(); i += LONGITUD_CADENAS) {
            sectores.add(turno.substring(i, i + LONGITUD_CADENAS).toLowerCase()); // se añadira en caso de no estar ya
        }
        return sectores;

    }

    private boolean comprobarNucleos(Set<String> sectoresC1, Set<String> sectoresC2) {

        Set<Nucleo> nucleosC1 = new HashSet<>();
        for (String sector : sectoresC1) {
            nucleosC1.addAll(
                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector)
            );
        }

        Set<Nucleo> nucleosC2 = new HashSet<>();
        for (String sector : sectoresC2) {
            nucleosC2.addAll(
                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector)
            );
            if (!nucleosC1.containsAll(
                    MetaheuristicUtil.obtenerNucleosAlQuePerteneceUnSector(super.getNucleos(), sector))
            ) return false;
        }
        return true;
    }

    private List<int[]> getintervalos(String turno) {
        List<int[]> res = new ArrayList<>();

        // recorremos el turno
        int i = super.getSlotMomentoActual();
        while (i + LONGITUD_CADENAS <= turno.length() && turno.substring(i, i + LONGITUD_CADENAS).equals(STRING_DESCANSO))
            i += 3; // saltamos los descansos
        for (int f = i; f + LONGITUD_CADENAS <= turno.length(); f += LONGITUD_CADENAS) {
            if (esDescanso(turno.substring(f, f + LONGITUD_CADENAS))) {
                res.add(new int[]{i, f});
                i = f;
                while (i + LONGITUD_CADENAS < turno.length() && esDescanso(turno.substring(i, i + LONGITUD_CADENAS)))
                    i += 3; // saltamos los descansos
                f = i;
            }
        }
        if (!turno.substring(turno.length() - 3).equals(STRING_DESCANSO))
            res.add(new int[]{i, turno.length()});

        return res;
    }

    private boolean esDescanso(String string) {
        return string.equals(STRING_DESCANSO) || string.equals(STRING_NO_TURNO);
    }


    @Override
    public Solucion generarSolucionAleatoria(Solucion x) {
        return null;
    }

}
