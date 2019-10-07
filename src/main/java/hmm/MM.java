package hmm;

import hmm.reader.RoadDataReader;
import hmm.reader.osmToWay;
import hmm.storage.GetFile;
import hmm.types.Point;
import hmm.types.RoadEdge;
import hmm.types.RoadNetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MM {
    public static void main(String args[]) throws IOException {
        List<RoadEdge> roadEdges = RoadDataReader.getData("road_network_beijing.txt");
        // 加载北京的地图数据
//        osmToWay way = new osmToWay("beijing-latest.osm");
//        List<RoadEdge> roadEdges = way.getRoadEdges();
//        Map<Long, Point> pointMap = way.pointMap;

        // road network
        RoadNetwork roadNetwork = new RoadNetwork(roadEdges);

        // process file
        new GetFile("D:/HMM/test/Data", roadEdges, roadNetwork);

        // output road network information in road_network_information.txt
        File file3 = new File("./target/road_network_information.txt");
        BufferedWriter out3 = new BufferedWriter(new FileWriter(file3));
        HashMap<Long, Point> roadNodes = roadNetwork.roadNodes;

        System.out.println(roadNodes.size());
        for (long roadNodeId : roadNodes.keySet()) {
            out3.write(Long.toString(roadNodeId));
            out3.write(",");
            out3.write(Double.toString(roadNodes.get(roadNodeId).longitude));
//            out3.write(Double.toString(pointMap.get(roadNodeId).longitude));
            out3.write(",");
            out3.write(Double.toString(roadNodes.get(roadNodeId).latitude));
//            out3.write(Double.toString(pointMap.get(roadNodeId).latitude));
            out3.write("\r\n");
        }
        out3.close();
    }
}
