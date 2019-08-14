package hmm.types;

import java.util.List;

public class RoadEdge {

    public final long edgeId;

    public final long fromNodeId;

    public final long toNodeId;

    public final boolean twoWay;

    public final List<Point> line;

    public RoadEdge(long edgeId, long fromNodeId, long toNodeId, boolean twoWay, List<Point> line) {
        this.edgeId = edgeId;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.twoWay = twoWay;
        this.line = line;
    }
}
