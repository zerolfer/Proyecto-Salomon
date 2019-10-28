package algorithms.variableNeighborhoodSearch.impl.neighborhood;

import algorithms.variableNeighborhoodSearch.NeighborhoodSet;
import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil.NeighborAuxCompuesto;
import algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil.NeighborAuxSimple;
import algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil.NeighborSetAux;
import estructurasDatos.Auxiliares.Random;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodSetProbabilistico implements NeighborhoodSet {

    private List<NeighborSetAux> diversificacion; // movMaxCarga & movLibre
    private List<NeighborSetAux> intensificacion; // movRejilla

    private NeighborhoodStructure movActual;

    private List<NeighborSetAux> historico;
    private List<NeighborSetAux> diversificacionNoUtilizados;
    private List<NeighborSetAux> intensificacionNoUtilizados;

    // Usa loops (promedio 26.65 ms) //
    public NeighborhoodSetProbabilistico(String[] nombresMovimientos, Entrada entrada, Patrones patrones,
                                         Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        historico = new ArrayList<>();
        diversificacion = new ArrayList<>();
        intensificacion = new ArrayList<>();

        List<NeighborhoodStructure> rejilla = new ArrayList<>();
        List<NeighborhoodStructure> maxCarga = new ArrayList<>();


        for (String move : nombresMovimientos) {
            NeighborhoodStructure moveStructure =
                    MoveFactory.createNeighborhood(move, entrada, patrones, parametros, parametrosAlgoritmo);

            // Determinamos el tipo de estructura auxiliar que necesitamos (lista o simple):

            // compuesto
            if (move.contains("movRejilla."))
                rejilla.add(moveStructure);

                // compuesto
            else if (move.contains("movMaxCarga."))
                maxCarga.add(moveStructure);

                // simple
            else if (move.equals("movRejilla")) {
                NeighborSetAux aux = new NeighborAuxSimple(moveStructure);
                intensificacion.add(aux);
            }

            // simple
            else {
                NeighborSetAux aux = new NeighborAuxSimple(moveStructure);
                diversificacion.add(aux);
            }
        }
        if (!maxCarga.isEmpty()) diversificacion.add(new NeighborAuxCompuesto(maxCarga));
        if (!rejilla.isEmpty()) intensificacion.add(new NeighborAuxCompuesto(rejilla));
        diversificacionNoUtilizados = new ArrayList<>(diversificacion);
        intensificacionNoUtilizados = new ArrayList<>(intensificacion);

        nextNeighborhood();
    }

    @Override
    public boolean hayEntornosSinUsar() {
        for (NeighborSetAux div : diversificacion)
            if (!div.utilizado()) return true;
        for (NeighborSetAux inte : intensificacion)
            if (!inte.utilizado()) return true;

        return false;
    }

    @Override
    public void reset() {
        for (NeighborSetAux structure : historico)
            structure.reset();

        diversificacionNoUtilizados = new ArrayList<>(diversificacion);
        intensificacionNoUtilizados = new ArrayList<>(intensificacion);
    }

    @Override
    public void nextNeighborhood() {
        NeighborSetAux aux;
        if ((Random.nextInt(2) == 0 && !diversificacionNoUtilizados.isEmpty()) || intensificacionNoUtilizados.isEmpty()) {

            if (diversificacionNoUtilizados.isEmpty())
                throw new RuntimeException("Todos los entornos han sido utilizados anteriormemte y no se ha reiniciado la estructura");

            int x = Random.nextInt(diversificacionNoUtilizados.size());
            aux = diversificacionNoUtilizados.get(x);

            this.movActual = aux.elegir();

            if (aux.utilizado()) {
                diversificacionNoUtilizados.remove(aux);
                historico.add(aux);
            }

        } else {
            int x = Random.nextInt(intensificacionNoUtilizados.size());
            aux = intensificacionNoUtilizados.get(x);

            this.movActual = aux.elegir();

            if (aux.utilizado()) {
                intensificacionNoUtilizados.remove(aux);
                historico.add(aux);
            }
        }
    }

    @Override
    public NeighborhoodStructure getCurrentNeighborhood() {
        return movActual;
    }

/*
    // Usa regex (promedio 31.52 ms) //
    private static Pattern rejillaComplejoPattern = Pattern.compile("movRejilla\\..+");
    private static Pattern rejillaSimplePattern = Pattern.compile("movRejilla");
    private static Pattern maxCargaComplejoPattern = Pattern.compile("movMaxCarga\\..+");

    public NeighborhoodSetProbabilistico(String[] nombresMovimientos, Entrada entrada, Patrones patrones,
                                         Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        diversificacion = new ArrayList<>();
        intensificacion = new ArrayList<>();

        List<NeighborhoodStructure> rejilla = new ArrayList<>();
        List<NeighborhoodStructure> maxCarga = new ArrayList<>();


        for (String move : nombresMovimientos) {
            NeighborhoodStructure moveStructure =
                    MoveFactory.createNeighborhood(move, entrada, patrones, parametros, parametrosAlgoritmo);

            // Determinamos el tipo de estructura auxiliar que necesitamos (lista o simple):

            // compuesto
            if (rejillaComplejoPattern.matcher(move).matches())
                rejilla.add(moveStructure);

                // compuesto
            else if (maxCargaComplejoPattern.matcher(move).matches())
                maxCarga.add(moveStructure);

                // simple
            else if (rejillaSimplePattern.matcher(move).matches())
                intensificacion.add(new NeighborAuxSimple(moveStructure));

                // simple
            else
                diversificacion.add(new NeighborAuxSimple(moveStructure));
        }
        if (!maxCarga.isEmpty()) diversificacion.add(new NeighborAuxCompuesto(maxCarga));
        if (!rejilla.isEmpty()) intensificacion.add(new NeighborAuxCompuesto(rejilla));
    }
*/

}
