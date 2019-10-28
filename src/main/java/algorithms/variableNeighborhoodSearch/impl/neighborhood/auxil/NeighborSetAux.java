package algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil;

import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;

public interface NeighborSetAux {
    NeighborhoodStructure elegir();

    boolean utilizado();

    void reset();
}
