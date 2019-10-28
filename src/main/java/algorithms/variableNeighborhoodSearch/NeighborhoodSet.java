package algorithms.variableNeighborhoodSearch;

public interface NeighborhoodSet {

    NeighborhoodStructure getEntorno();

    boolean hayEntornosSinUsar();

    void reset();

    void nextNeighborhood();

    NeighborhoodStructure getCurrentNeighborhood();
}
