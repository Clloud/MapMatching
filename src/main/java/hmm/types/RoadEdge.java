package hmm.types;

import java.util.List;
import static hmm.utils.Helper.computeDistance;

public class RoadEdge {

    public final long edgeId;

    public final long fromNodeId;

    public final long toNodeId;

    public final boolean twoWay;

    public final List<Point> line;

    public double roadLength;   // [m]

    public RoadEdge(long edgeId, long fromNodeId, long toNodeId, boolean twoWay, List<Point> line) {
        this.edgeId = edgeId;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.twoWay = twoWay;
        this.line = line;
        this.roadLength = computeRoadLength(line);
    }

    private double computeRoadLength(List<Point> line) {
        double sum = 0;
        for (int i = 0; i < line.size() - 1; i++) {
            sum += computeDistance(line.get(i), line.get(i+1));
        }
        return sum;
    }
}
