package algorithms.variableNeighborhoodSearch.impl.neighborhood.auxil;

import algorithms.variableNeighborhoodSearch.NeighborhoodStructure;
import estructurasDatos.Auxiliares.Random;

import java.util.ArrayList;
import java.util.List;

public class NeighborAuxCompuestoNoOrdenado implements NeighborSetAux {

    private List<NeighborAuxSimple> neighborhoods;
    private List<Integer> idxNoUtilizados;

    public NeighborAuxCompuestoNoOrdenado(NeighborhoodStructure... neighborhoods) {
        this.neighborhoods = new ArrayList<>();
        this.idxNoUtilizados = new ArrayList<>();
        int i = 0;
        for (NeighborhoodStructure neighborhood : neighborhoods) {
            this.neighborhoods.add(new NeighborAuxSimple(neighborhood));
            idxNoUtilizados.add(i++);
        }
    }

    public NeighborAuxCompuestoNoOrdenado(List<NeighborhoodStructure> neighborhoods) {
        this.neighborhoods = new ArrayList<>();
        for (NeighborhoodStructure neighborhood : neighborhoods) {
            this.neighborhoods.add(new NeighborAuxSimple(neighborhood));
        }
    }

    @Override
    public NeighborhoodStructure elegir() {
        if (utilizado()) throw new RuntimeException("Grupo de Vencindad ya utilizado");
        int i = Random.nextInt();
        int idx = idxNoUtilizados.get(i);
        neighborhoods.get(idx).setUtilizado(true);
        idxNoUtilizados.remove(i);
        return neighborhoods.get(idx).getNeighborhood();
    }

    @Override
    public boolean utilizado() {
        return idxNoUtilizados.isEmpty();
    }

    @Override
    public void reset() {
        idxNoUtilizados = new ArrayList<>();
        for (int i = 0; i < neighborhoods.size(); i++)
            idxNoUtilizados.add(i);
    }
}
