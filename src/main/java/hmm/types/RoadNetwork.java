package hmm.types;

import java.io.Serializable;
import java.util.*;


public class RoadNetwork implements Serializable {
    private int vertexNum;
    private int edgeNum;
    private Set<Long> roadVertex = new LinkedHashSet<>();
    private List<Vertex> vertex = new ArrayList<>();
    private List<Long> roadList;
    private List<Long> roadEdgeId = new ArrayList<>();
    private List<RoadEdge> roadEdgesCopy;
    private double INF = Double.POSITIVE_INFINITY;

    public static class Node implements Serializable {
        long adj;
        double weight;
        Node next;
    }

    public static class Vertex implements Serializable {
        long data;
        Node firstEdge;
    }

    public static class Edge implements Serializable {
        long start;
        long end;
        double weight;
    }

    /**
     * Construct the road network through roadEdges.
     */
    public RoadNetwork(List<RoadEdge> roadEdges) {
        roadEdgesCopy = roadEdges;
        edgeNum = roadEdges.size();

        for (RoadEdge roadEdge : roadEdges) {
            roadVertex.add(roadEdge.fromNodeId);
            roadVertex.add(roadEdge.toNodeId);
            roadEdgeId.add(roadEdge.edgeId);
        }
        vertexNum = roadVertex.size();
        roadList = new ArrayList<>(roadVertex);

        for (int i = 0; i < vertexNum; i++) {
            Vertex vertexI = new Vertex();
            vertexI.data = roadList.get(i);
            vertexI.firstEdge = null;
            vertex.add(vertexI);
        }

        for (int i = 0; i < edgeNum; i++) {
            Edge edgeI = new Edge();
            edgeI.start = roadEdges.get(i).fromNodeId;
            edgeI.end = roadEdges.get(i).toNodeId;
            edgeI.weight = roadEdges.get(i).roadLength;

            Node node1 = new Node();
            node1.adj = roadList.indexOf(edgeI.end);
            node1.weight = edgeI.weight;
            node1.next = null;

            if (vertex.get(roadList.indexOf(edgeI.start)).firstEdge == null) {
                vertex.get(roadList.indexOf(edgeI.start)).firstEdge = node1;
            } else {
                Node tmp = vertex.get(roadList.indexOf(edgeI.start)).firstEdge;
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = node1;
            }

            if (roadEdges.get(i).twoWay) {
                Node node2 = new Node();
                node2.adj = roadList.indexOf(edgeI.start);
                node2.weight = edgeI.weight;
                node2.next = null;

                if (vertex.get(roadList.indexOf(edgeI.end)).firstEdge == null) {
                    vertex.get(roadList.indexOf(edgeI.end)).firstEdge = node2;
                } else {
                    Node tmp = vertex.get(roadList.indexOf(edgeI.end)).firstEdge;
                    while (tmp.next != null) {
                        tmp = tmp.next;
                    }
                    tmp.next = node2;
                }
            }
        }
    }

    private double getWeight(int start, int end) {
        if (start == end)
            return 0;
        Node node = vertex.get(start).firstEdge;
        while (node != null) {
            if (end == node.adj)
                return node.weight;
            node = node.next;
        }
        return INF;
    }

    public double computePathDistance(RoadPosition from, RoadPosition to) {
        RoadEdge f = roadEdgesCopy.get(roadEdgeId.indexOf(from.edgeId));
        RoadEdge t = roadEdgesCopy.get(roadEdgeId.indexOf(to.edgeId));
        long fromNodeId = f.fromNodeId;
        long toNodeId = t.toNodeId;
        long fromNodeIdReverse = f.toNodeId;
        long toNodeIdReverse = t.fromNodeId;
        double beforeTo = dijkstra(fromNodeId, toNodeId)[1];
        double afterFrom = dijkstra(fromNodeId, toNodeId)[2];
        double d, d1, d2;

        if (beforeTo == t.fromNodeId && afterFrom == f.toNodeId) {
            d = dijkstra(fromNodeId, toNodeId)[0];
            d1 = from.fraction * f.roadLength;
            d2 = (1 - to.fraction) * t.roadLength;
        } else if (beforeTo != t.fromNodeId && afterFrom == f.toNodeId) {
            d = dijkstra(fromNodeId, toNodeIdReverse)[0];
            d1 = from.fraction * f.roadLength;
            d2 = to.fraction * t.roadLength;
        } else if (beforeTo == t.fromNodeId && afterFrom != f.toNodeId) {
            d = dijkstra(fromNodeIdReverse, toNodeId)[0];
            d1 = (1 - from.fraction) * f.roadLength;
            d2 = (1 - to.fraction) * t.roadLength;
        } else {
            d = dijkstra(fromNodeIdReverse, toNodeIdReverse)[0];
            d1 = (1 - from.fraction) * f.roadLength;
            d2 = to.fraction * t.roadLength;
        }
        return d - d1 - d2;
    }

    private double[] dijkstra(long fromNodeId, long toNodeId) {
        int s = roadList.indexOf(fromNodeId);
        int e = roadList.indexOf(toNodeId);
        int[] path = new int[vertexNum];
        double[] distance = new double[vertexNum];
        boolean[] visited = new boolean[vertexNum];

        for (int i = 0; i < vertexNum; i++) {
            distance[i] = getWeight(s, i);
            if (i != s && distance[i] < INF)
                path[i] = s;
            else
                path[i] = -1;
        }
        visited[s] = true;
        for (int i = 0; i < vertexNum; i++) {
            if (i == s) continue;
            double min = INF;
            int k = -1;
            for (int j = 0; j < vertexNum; j++) {
                if (!visited[j] && distance[j] < min) {
                    min = distance[j];
                    k = j;
                }
            }
            visited[k] = true;
            for (int j = 0; j < vertexNum; j++) {
                if (getWeight(k, j) == INF) continue;
                double tmp = distance[k] + getWeight(k, j);
                if (!visited[j] && distance[j] > tmp) {
                    distance[j] = tmp;
                    path[j] = k;
                }
            }
        }

        int tp = e;
        int pre = e;
        double p1;
        if (path[e] != -1)
            p1 = vertex.get(path[e]).data;
        else
            p1 = -1;
        while (tp != s) {
            pre = tp;
            tp = path[tp];
        }
        double p2 = vertex.get(pre).data;
        return new double[]{distance[e], p1, p2};
    }

    public void putEdge(long fromNodeId, long toNodeId, double length, boolean TwoWay) {
        if (roadList.indexOf(fromNodeId) == -1) {
            roadVertex.add(fromNodeId);
            roadList.add(fromNodeId);

            Vertex vertexNew = new Vertex();
            vertexNew.data = fromNodeId;
            vertexNew.firstEdge = null;
            vertex.add(vertexNew);
            vertexNum++;
        }

        if (roadList.indexOf(toNodeId) == -1 && TwoWay) {
            roadVertex.add(toNodeId);
            roadList.add(toNodeId);

            Vertex vertexNew = new Vertex();
            vertexNew.data = toNodeId;
            vertexNew.firstEdge = null;
            vertex.add(vertexNew);
            vertexNum++;
        }

        Node node1 = new Node();
        node1.adj = roadList.indexOf(toNodeId);
        node1.weight = length;
        node1.next = null;
        if (vertex.get(roadList.indexOf(fromNodeId)).firstEdge == null) {
            vertex.get(roadList.indexOf(fromNodeId)).firstEdge = node1;
        } else {
            Node tmp = vertex.get(roadList.indexOf(fromNodeId)).firstEdge;
            while (tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = node1;
        }

        if (TwoWay) {
            Node node2 = new Node();
            node2.adj = roadList.indexOf(fromNodeId);
            node2.weight = length;
            node2.next = null;
            if (vertex.get(roadList.indexOf(toNodeId)).firstEdge == null) {
                vertex.get(roadList.indexOf(toNodeId)).firstEdge = node2;
            } else {
                Node tmp = vertex.get(roadList.indexOf(toNodeId)).firstEdge;
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = node2;
            }
        }
        edgeNum++;
    }
}
