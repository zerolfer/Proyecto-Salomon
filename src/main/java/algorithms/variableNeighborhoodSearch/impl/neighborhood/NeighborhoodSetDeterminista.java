package algorithms.variableNeighborhoodSearch.impl.neighborhood;

import algorithms.variableNeighborhoodSearch.NeighborhoodSet;
import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodSetDeterminista implements NeighborhoodSet {

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNeighborSet}
     */
    List<NeighborhoodStructure> neighborhoodStructures;

    /**
     * <p>The index of the current neighborhood of the
     * {@link #neighborhoodStructures} list.</p>
     * <p>Aka " k "</p>
     */
    private int currentNeighborhoodIndex = 0;

    public NeighborhoodSetDeterminista(String[] nombresMovimientos, Entrada entrada, Patrones patrones,
                                       Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        neighborhoodStructures = new ArrayList<>();

        // TODO: refactorizar a ParametrosAlgoritmo.initializeNeighborStructures???? ver cuando se implemente el probabilistico
        for (String id : nombresMovimientos) {
            // FACTORY METHOD
            neighborhoodStructures.add(MoveFactory.createNeighborhood(id, entrada, patrones, parametros, parametrosAlgoritmo));
        }
    }

    @Override
    public boolean hayEntornosSinUsar() {
        return currentNeighborhoodIndex < neighborhoodStructures.size();
    }

    @Override
    public void reset() {
        currentNeighborhoodIndex = 0;
    }

    @Override
    public void nextNeighborhood() {
        currentNeighborhoodIndex++;
    }

    @Override
    public NeighborhoodStructure getCurrentNeighborhood() {
        if (currentNeighborhoodIndex < neighborhoodStructures.size())
            return neighborhoodStructures.get(this.currentNeighborhoodIndex);
        return neighborhoodStructures.get(0);

    }
}
