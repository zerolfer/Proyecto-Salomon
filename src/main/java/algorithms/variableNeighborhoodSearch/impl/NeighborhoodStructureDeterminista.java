package algorithms.variableNeighborhoodSearch.impl;

import algorithms.variableNeighborhoodSearch.NeighborStructure;
import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import algorithms.variableNeighborhoodSearch.impl.moves.MoveFactory;
import estructurasDatos.DominioDelProblema.Entrada;
import estructurasDatos.Parametros;
import estructurasDatos.ParametrosAlgoritmo;
import patrones.Patrones;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodStructureDeterminista implements NeighborhoodStructure {

    /**
     * Véase {@link ParametrosAlgoritmo.VNS#getNeighborStructures}
     */
    List<NeighborStructure> neighborStructures;

    /**
     * <p>The index of the current neighborhood of the
     * {@link #neighborStructures} list.</p>
     * <p>Aka " k "</p>
     */
    private int currentNeighborhoodIndex = 0;

    public NeighborhoodStructureDeterminista(String[] nombresMovimientos, Entrada entrada, Patrones patrones,
                                             Parametros parametros, ParametrosAlgoritmo parametrosAlgoritmo) {
        neighborStructures = new ArrayList<>();

        // TODO: refactorizar a ParametrosAlgoritmo.initializeNeighborStructures???? ver cuando se implemente el probabilistico
        for (String id : nombresMovimientos) {
            // FACTORY METHOD
            neighborStructures.add(MoveFactory.createNeighborhood(id, entrada, patrones, parametros, parametrosAlgoritmo));
        }
    }

    @Override
    public NeighborStructure getEntorno() {
        return null;
    }

    @Override
    public boolean hayEntornosSinUsar() {
        return currentNeighborhoodIndex < neighborStructures.size();
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
    public NeighborStructure getCurrentNeighborhood() {
        if (currentNeighborhoodIndex < neighborStructures.size())
            return neighborStructures.get(this.currentNeighborhoodIndex);
        return neighborStructures.get(0);

    }
}
