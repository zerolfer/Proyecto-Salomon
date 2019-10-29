package algorithms.variableNeighborhoodSearch;

public interface NeighborhoodSet {

    boolean hayEntornosSinUsar();

    void reset();

    void nextNeighborhood(int numIteraciones);

    NeighborhoodStructure getCurrentNeighborhood();
}
