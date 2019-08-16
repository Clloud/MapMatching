package hmm.types;

public class Edge {
    public long start;
    public long end;
    public double weight;

    public Edge(long v1, long v2, double wgt) {
        start = v1;
        end = v2;
        weight = wgt;
    }
}
