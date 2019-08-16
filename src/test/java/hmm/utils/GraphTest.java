package hmm.utils;

import hmm.types.Edge;
import hmm.types.Point;
import hmm.types.RoadEdge;
import org.junit.Test;
import java.util.List;
import static hmm.utils.Helper.computeDistance;
import static org.junit.Assert.*;

public class GraphTest extends OfflineMapMatching {
    Edge e1 = new Edge(1, 2, 50);
    Edge e2 = new Edge(3, 4, 50);
    Edge e3 = new Edge(2, 4, 40);
    Edge e4 = new Edge(2, 5, 30);
    Edge e5 = new Edge(4, 6, 30);

    Edge e6 = new Edge(7, 2, 20);
    Edge e7 = new Edge(1, 7, 30);
    Edge e8 = new Edge(8, 2, 30);
    Edge e9 = new Edge(8, 4, 10);

    RoadNetwork roadnetwork = new RoadNetwork();
    List<RoadEdge> road = roadnetwork.getData();
    int EdgeNum=5;
    Edge[] testroad = new Edge[EdgeNum];
    long[] testvertex = {1, 2, 3, 4, 5, 6};

    @Test
    public void dijkstra() {
        for (int i = 0; i < EdgeNum; i++) {
            Point p1 = road.get(0).line.get(0);
            Point p2 = road.get(1).line.get(1);
            testroad[i] = new Edge(road.get(i).fromNodeId, road.get(i).toNodeId, computeDistance(p1, p2));
        }

        Graph G = new Graph(6, 5);
        Edge[] d = {e1, e2, e3, e4, e5};
        long[] vert = {1, 2, 3, 4, 5, 6};
        G.LinkGraph(vert, d);

        Graph GTest = new Graph(testvertex.length, EdgeNum);
        GTest.LinkGraph(testvertex, testroad);

        G.putEdge(e6);
        G.putEdge(e7);
        G.putEdge(e8);
        G.putEdge(e9);
        GTest.PathDistance(2,5);
        //G.PathDistance(5,8);
        //G.PathDistance(7,8);
        //assertEquals(30, GTest.PathDistance(2, 5), 0);
        //assertEquals(60, G.PathDistance(5, 8), 0);
        //assertEquals(50, G.PathDistance(7, 8), 0);
    }
}