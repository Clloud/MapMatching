package hmm.utils;

import hmm.types.Edge;
import hmm.types.Node;
import hmm.types.Vertex;

public class Graph {
    int vertexnum;
    int arcnum;
    Vertex[] vertex;
    double INF = Double.POSITIVE_INFINITY;

    public Graph(int vertexnum, int arcnum) {
        this.vertexnum = vertexnum;
        this.arcnum = arcnum;
        this.vertex = new Vertex[vertexnum];
    }

    public void LinkGraph(long[] vert, Edge[] edge) {
        //读入顶点，并初始化
        for (int i = 0; i < vertexnum; i++) {
            vertex[i] = new Vertex();
            vertex[i].data = vert[i];       //顶点值
            vertex[i].firstEdge = null;  //还没有邻接点，当然没有边了
        }
        //初始化边
        for (int i = 0; i < edge.length; i++) {
            long start = edge[i].start;
            long end = edge[i].end;
            //获取顶点对应的序号
            int p1 = getPosition(start);
            int p2 = getPosition(end);
            //1.把p2连接在以p1为头的链表中
            Node node1 = new Node();
            node1.index = p2;
            node1.weight = edge[i].weight;
            linkedLast(p1, node1);
            //2.因为是无向图，所以还需要把p1连接在以p2为头的链表中
            Node node2 = new Node();
            node2.index = p1;
            node2.weight = edge[i].weight;
            linkedLast(p2, node2);
        }
    }

    public int getPosition(long v) {
        for (int i = 0; i < vertexnum; i++) {
            if (vertex[i].data == v) {
                return i;
            }
        }
        //不存在这样的顶点则返回-1
        return -1;
    }

    public void linkedLast(int index, Node node) {
        if (vertex[index].firstEdge == null) {
            vertex[index].firstEdge = node;
        } else {
            Node tmp = vertex[index].firstEdge;
            while (tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = node;
        }
    }

    public double PathDistance(long start, long end) {
        int s = getPosition(start);
        int e = getPosition(end);
        return dijkstra(start)[e];
    }

    public double[] dijkstra(long start) {
        int s = getPosition(start);
        int[] path = new int[vertex.length];  //记录到起点经过的顶点路径
        double[] distance = new double[vertex.length];  //记录到起点的距离
        boolean[] visited = new boolean[vertex.length]; //标记是否访问过
        //初始化到起点的距离
        for (int i = 0; i < vertex.length; i++) {
            distance[i] = getWeight(s, i);
            if (i != s && distance[i] < INF) {
                path[i] = s;
            } else {
                path[i] = -1;
            }
        }
        visited[s] = true;  //起点已经访问过了
        //遍历所有顶点，并更新到起点的距离
        for (int i = 0; i < vertex.length; i++) {
            if (i == s) {
                continue;
            }
            double min = INF;
            int k = -1;
            //找到距离起点距离最短的顶点
            //distance[j]=0,表示已经访问过了
            for (int j = 0; j < vertex.length; j++) {
                if (visited[j] == false && distance[j] < min) {
                    min = distance[j];
                    k = j;
                }
            }
            //for循环结束后，k就是要找的那个顶点
            visited[k] = true;  //表示第k个顶点已经访问过了
            //更新顶点k的邻接点到起点的最小距离
            for (int j = 0; j < vertex.length; j++) {
                //如果不是k的邻接点
                if (getWeight(k, j) == INF) {
                    continue;
                }
                double tmp = distance[k] + getWeight(k, j);
                //如果是未被访问过的邻接点，则更新其到起点的距离
                if (visited[j] == false && distance[j] > tmp) {
                    distance[j] = tmp;
                    path[j] = k;  //这里的意思是，顶点j到达起点，必定经过顶点k
                }
            }
        }

        for (int i = 0; i < vertex.length; i++) {
            //System.out.print(vertex[s].data + " --> "+vertex[i].data);
            //System.out.print("    distance:" + distance[i]);
            //System.out.println();  //换行
        }
        return distance;
    }

    private double getWeight(int start, int end) {
        if (start == end)
            return 0;
        Node node = vertex[start].firstEdge;
        while (node != null) {
            if (end == node.index)
                return node.weight;
            node = node.next;
        }

        return INF;
    }

    public void putEdge(Edge theEdge) {
        long v1, v2;
        double weight;
        v1 = theEdge.start;
        v2 = theEdge.end;
        weight = theEdge.weight;

        if (getPosition(v1) == -1) {
            Vertex[] newvertex = new Vertex[vertexnum + 1];
            for (int i = 0; i < vertexnum; i++) {
                newvertex[i] = vertex[i];
            }
            newvertex[vertexnum] = new Vertex();
            newvertex[vertexnum].data = v1;
            newvertex[vertexnum].firstEdge = null;
            vertexnum++;
            vertex = newvertex;
        }

        if (getPosition(v2) == -1) {
            Vertex[] newvertex = new Vertex[vertexnum + 1];
            for (int i = 0; i < vertexnum; i++) {
                newvertex[i] = vertex[i];
            }
            newvertex[vertexnum] = new Vertex();
            newvertex[vertexnum].data = v2;
            newvertex[vertexnum].firstEdge = null;
            vertexnum++;
            vertex = newvertex;
        }

        int p1 = getPosition(v1);
        int p2 = getPosition(v2);

        Node node1 = new Node();
        node1.index = p2;
        node1.weight = weight;
        linkedLast(p1, node1);
        Node node2 = new Node();
        node2.index = p1;
        node2.weight = weight;
        linkedLast(p2, node2);

        arcnum++;
    }
}
