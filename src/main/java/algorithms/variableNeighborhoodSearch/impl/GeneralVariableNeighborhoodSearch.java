package algorithms.variableNeighborhoodSearch.impl;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import estructurasDatos.Solucion;
import herramientas.Log;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class GeneralVariableNeighborhoodSearch extends AbstractVariableNeighborhoodSearch {

    private VariableNeighborhoodDescendent vnd;

    public GeneralVariableNeighborhoodSearch(Parametros parametros, Patrones patrones, ParametrosAlgoritmo parametrosAlgoritmo, Entrada entrada) {
        super(parametros, patrones, parametrosAlgoritmo, entrada);
        this.vnd = new VariableNeighborhoodDescendent(parametros, patrones, parametrosAlgoritmo, entrada);

        List<NeighborStructure> result = new ArrayList<>();

        for (String id : new String[]{"movRejilla.1", "movRejilla.2",
                "movRejilla.3", "movRejilla.4",
                "movRejilla.5", "movRejilla.6"}
        )
            result.add(MoveFactory.createNeighborhood(id, entrada, patrones, parametros, parametrosAlgoritmo));

        this.vnd.neighborStructures = result;
    }

    @Override
    protected Solucion vnsImplemetation(Solucion x) {

        Object[] temp = super.getCurrentNeighborhood().generarSolucionAleatoria(x);
        Solucion x_prime = (Solucion) temp[0];
        int c1 = (int) temp[1], c2 = (int) temp[2];

        List<NeighborStructure> result = new ArrayList<>();
        for (String id : new String[]{"movRejilla.1.Restringido", "movRejilla.2.Restringido",
                "movRejilla.3.Restringido", "movRejilla.4.Restringido",
                "movRejilla.5.Restringido", "movRejilla.6.Restringido"}
        )
            result.add(MoveFactory.createNeighborhoodRestringido(id, getEntrada(), getPatrones(), getParametros(), getParametrosAlgoritmo(), c1, c2));

        this.vnd.neighborStructures = result;

        Solucion x_prime_2 = vnd.vnsImplemetation(x_prime, c1, c2);

        if (Log.isOn() && Log.checkIter(super.contadorIteraciones)) {
            Log.info("[GVNS] fitness inicial: " + fitness(x) + " | \t" +
                    "fitness sol aleatoria: " + fitness(x_prime) + " | \t" +
                    "fitness sol aleatoria tras VND: " + fitness(x_prime_2));
        }

        return x_prime_2;


    }
}
