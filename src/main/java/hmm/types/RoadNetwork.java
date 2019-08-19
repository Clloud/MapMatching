package hmm.types;

import java.util.*;

import static hmm.utils.Helper.computeDistance;

public class RoadNetwork {
    int VertexNum;
    int EdgeNum;
    Set<Long> roadVertex = new LinkedHashSet<>();
    List<Vertex> vertex = new ArrayList<>();
    List<Long> roadList;
    List<Long> roadEdgeId = new ArrayList<>();
    List<RoadEdge> roadEdges_save;
    double INF = Double.POSITIVE_INFINITY;

    public static class Node {
        long adj;
        double weight;
        Node next;
    }

    public static class Vertex {
        long data;
        Node firstEdge;
    }

    public static class Edge {
        long start;
        long end;
        double weight;
    }

    public RoadNetwork(List<RoadEdge> roadEdges) {
        // 根据roadEdges构造一个使用邻接链表保存的路网图
        roadEdges_save = roadEdges;
        EdgeNum = roadEdges.size();

        for (RoadEdge roadEdge : roadEdges) {
            roadVertex.add(roadEdge.fromNodeId);
            roadVertex.add(roadEdge.toNodeId);
            roadEdgeId.add(roadEdge.edgeId);
        }
        VertexNum = roadVertex.size();
        roadList = new ArrayList<>(roadVertex);

        for (int i = 0; i < VertexNum; i++) {
            Vertex vertex_i = new Vertex();
            vertex_i.data = roadList.get(i);
            vertex_i.firstEdge = null;
            vertex.add(vertex_i);
        }

        for (int i = 0; i < EdgeNum; i++) {
            Edge edge_i = new Edge();
            edge_i.start = roadEdges.get(i).fromNodeId;
            edge_i.end = roadEdges.get(i).toNodeId;
            edge_i.weight = roadEdges.get(i).roadLength;

            Node node1 = new Node();
            node1.adj = roadList.indexOf(edge_i.end);
            node1.weight = edge_i.weight;
            node1.next = null;

            if (vertex.get(roadList.indexOf(edge_i.start)).firstEdge == null) {
                vertex.get(roadList.indexOf(edge_i.start)).firstEdge = node1;
            } else {
                Node tmp = vertex.get(roadList.indexOf(edge_i.start)).firstEdge;
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = node1;
            }

            if (roadEdges.get(i).twoWay) {
                Node node2 = new Node();
                node2.adj = roadList.indexOf(edge_i.start);
                node2.weight = edge_i.weight;
                node2.next = null;

                if (vertex.get(roadList.indexOf(edge_i.end)).firstEdge == null) {
                    vertex.get(roadList.indexOf(edge_i.end)).firstEdge = node2;
                } else {
                    Node tmp = vertex.get(roadList.indexOf(edge_i.end)).firstEdge;
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
        long fromNodeId = roadEdges_save.get(roadEdgeId.indexOf(from.edgeId)).fromNodeId;
        long toNodeId = roadEdges_save.get(roadEdgeId.indexOf(to.edgeId)).toNodeId;
        long fromNodeId_2 = roadEdges_save.get(roadEdgeId.indexOf(from.edgeId)).toNodeId;
        long toNodeId_2 = roadEdges_save.get(roadEdgeId.indexOf(to.edgeId)).fromNodeId;
        double before_to = dijkstra(fromNodeId, toNodeId)[1];
        double after_from = dijkstra(fromNodeId, toNodeId)[2];
        RoadEdge f = roadEdges_save.get(roadEdgeId.indexOf(from.edgeId));
        RoadEdge t = roadEdges_save.get(roadEdgeId.indexOf(to.edgeId));
        if (before_to == t.fromNodeId &&
                after_from == f.toNodeId) {
            double d_all = dijkstra(fromNodeId, toNodeId)[0];
            double d1 = from.fraction * f.roadLength;
            double d2 = (1 - to.fraction) * t.roadLength;
            return d_all - d1 - d2;
        } else if (before_to != t.fromNodeId &&
                after_from == f.toNodeId) {
            double d_all = dijkstra(fromNodeId, toNodeId_2)[0];
            double d1 = from.fraction * f.roadLength;
            double d2 = to.fraction * t.roadLength;
            return d_all - d1 - d2;
        } else if (before_to == t.fromNodeId &&
                after_from != f.toNodeId) {
            double d_all = dijkstra(fromNodeId_2, toNodeId)[0];
            double d1 = (1 - from.fraction) * f.roadLength;
            double d2 = (1 - to.fraction) * t.roadLength;
            return d_all - d1 - d2;
        } else {
            double d_all = dijkstra(fromNodeId_2, toNodeId_2)[0];
            double d1 = (1 - from.fraction) * f.roadLength;
            double d2 = to.fraction * t.roadLength;
            return d_all - d1 - d2;
        }
    }

    private double[] dijkstra(long fromNodeId, long toNodeId) {
        int s = roadList.indexOf(fromNodeId);
        int e = roadList.indexOf(toNodeId);
        int[] path = new int[VertexNum];
        double[] distance = new double[VertexNum];
        boolean[] visited = new boolean[VertexNum];

        for (int i = 0; i < VertexNum; i++) {
            distance[i] = getWeight(s, i);
            if (i != s && distance[i] < INF) {
                path[i] = s;
            } else {
                path[i] = -1;
            }
        }
        visited[s] = true;
        for (int i = 0; i < VertexNum; i++) {
            if (i == s) {
                continue;
            }
            double min = INF;
            int k = -1;
            for (int j = 0; j < VertexNum; j++) {
                if (visited[j] == false && distance[j] < min) {
                    min = distance[j];
                    k = j;
                }
            }
            visited[k] = true;
            for (int j = 0; j < VertexNum; j++) {
                if (getWeight(k, j) == INF) {
                    continue;
                }
                double tmp = distance[k] + getWeight(k, j);
                if (visited[j] == false && distance[j] > tmp) {
                    distance[j] = tmp;
                    path[j] = k;
                }
            }
        }
        int tmp = e;
        int pre = e;
        double p1;
        if (path[e] != -1) {
            p1 = vertex.get(path[e]).data;
        } else {
            p1 = -1;
        }
        while (tmp != s) {
            pre = tmp;
            tmp = path[tmp];
        }
        double p2 = vertex.get(pre).data;
        return new double[]{distance[e], p1, p2};
    }

    public void putEdge(long fromNodeId, long toNodeId, double length, boolean TwoWay) {
        if (roadList.indexOf(fromNodeId) == -1) {
            roadVertex.add(fromNodeId);
            roadList.add(fromNodeId);

            Vertex vertex_new = new Vertex();
            vertex_new.data = fromNodeId;
            vertex_new.firstEdge = null;
            vertex.add(vertex_new);
            VertexNum++;
        }

        if (roadList.indexOf(toNodeId) == -1 && TwoWay) {
            roadVertex.add(toNodeId);
            roadList.add(toNodeId);

            Vertex vertex_new = new Vertex();
            vertex_new.data = toNodeId;
            vertex_new.firstEdge = null;
            vertex.add(vertex_new);
            VertexNum++;
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
        EdgeNum++;
    }
}
