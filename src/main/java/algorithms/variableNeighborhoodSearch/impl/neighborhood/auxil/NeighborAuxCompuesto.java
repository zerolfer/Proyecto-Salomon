package algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil;

import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;

import java.util.Arrays;
import java.util.List;

public class NeighborAuxCompuesto implements NeighborSetAux {

    private List<NeighborhoodStructure> neighborhoods;
    protected int index;

    public NeighborAuxCompuesto(NeighborhoodStructure... neighborhoods) {
        this.neighborhoods = Arrays.asList(neighborhoods);
        this.index = 0;
    }

    public NeighborAuxCompuesto(List<NeighborhoodStructure> neighborhoods) {
        this.neighborhoods = neighborhoods;
    }

    @Override
    public NeighborhoodStructure elegir() {
        if (utilizado()) throw new RuntimeException("Estructura de vencindad ya utilizada previamente. " +
                "Situación corrupta del sistema");
        return neighborhoods.get(index++);
    }

    @Override
    public boolean utilizado() {
        return index == neighborhoods.size();
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < neighborhoods.size(); i++) {
            NeighborhoodStructure neighborhood = neighborhoods.get(i);
            if (i < neighborhoods.size() - 1) sb.append(neighborhood).append(", ");
            else sb.append(neighborhood);
        }
        return sb.toString();
    }
}
