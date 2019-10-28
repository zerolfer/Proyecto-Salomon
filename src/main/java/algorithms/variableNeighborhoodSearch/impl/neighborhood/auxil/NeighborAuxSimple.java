package algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil;

import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;

public class NeighborAuxSimple implements NeighborSetAux {

    private NeighborhoodStructure neighborhood;
    private boolean utilizado;

    public NeighborAuxSimple(NeighborhoodStructure neighborhood) {
        this.neighborhood = neighborhood;
        this.utilizado = false;
    }

    @Override
    public NeighborhoodStructure elegir() {
        utilizado = true;
        return neighborhood;
    }

    @Override
    public boolean utilizado() {
        return utilizado;
    }

    @Override
    public void reset() {
        utilizado = false;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }

    public NeighborhoodStructure getNeighborhood() {
        return neighborhood;
    }

    @Override
    public String toString() {
        return neighborhood.toString();
    }
}
