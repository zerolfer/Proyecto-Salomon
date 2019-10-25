package algorithms.variableNeighborhoodSearch;

public interface NeighborhoodStructure {

    NeighborStructure getEntorno();

    boolean hayEntornosSinUsar();

    void reset();

    void nextNeighborhood();

    NeighborStructure getCurrentNeighborhood();
}
