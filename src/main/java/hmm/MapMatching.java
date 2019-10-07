package hmm;

import com.bmw.hmm.SequenceState;
import hmm.reader.GpsDataReader;
import hmm.reader.RoadDataReader;
import hmm.reader.TrajectoryDataReader;
import hmm.reader.osmToWay;
import hmm.types.Point;
import hmm.types.RoadEdge;
import hmm.types.RoadNetwork;
import hmm.types.RoadPosition;
import hmm.utils.OfflineMapMatching;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapMatching {
    public static void main(String args[]) throws IOException {
        // 加载北京的地图数据
        osmToWay way = new osmToWay("beijing-latest.osm");
        List<RoadEdge> roadEdges = way.getRoadEdges();
        Map<Long, Point> pointMap = way.pointMap;
//        List roadEdges = RoadDataReader.getData("road_network.txt");


        // road network
        RoadNetwork roadNetwork = new RoadNetwork(roadEdges);

        // prepare data
        List gpsMeasurements = GpsDataReader.getData("gps_data.txt").subList(0, 60);
//        List trajectoryMeasurements = TrajectoryDataReader.getData("D:/HMM/test2/Data/000/Trajectory/20081024020959.plt");


        // map matching
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching(gpsMeasurements, roadEdges, roadNetwork);
//        OfflineMapMatching offlineMapMatching = new OfflineMapMatching(trajectoryMeasurements, roadEdges,roadNetwork);
        List result = offlineMapMatching.run();

        // output result
        File file1 = new File("./target/result1.txt");
        File file2 = new File("./target/result2.txt");
        File file3 = new File("./target/road_network_information.txt");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        BufferedWriter out3 = new BufferedWriter(new FileWriter(file3));
        List<Long> roadIds = new ArrayList<>();
        Set<Long> roadNodes = roadNetwork.nodes;

        System.out.println(result.size());
        for (Object o : result) {
            long roadId = ((RoadPosition) ((SequenceState) o).state).edgeId;
            Point pointPosition = ((RoadPosition) ((SequenceState) o).state).position;
            long fromNodeId = roadNetwork.roadEdges.get(roadId).fromNodeId;
            long toNodeId = roadNetwork.roadEdges.get(roadId).toNodeId;
            System.out.println(roadId);
            out1.write(Long.toString(roadId));
            out1.write(",");
            out1.write(Long.toString(fromNodeId));
            out1.write(",");
            out1.write(Long.toString(toNodeId));
            out1.write(",");
            out1.write(Double.toString(pointPosition.longitude));
            out1.write(",");
            out1.write(Double.toString(pointPosition.latitude));
            out1.write("\r\n");
            if (roadIds.size() == 0 || roadId != roadIds.get(roadIds.size() - 1)) {
                roadIds.add(roadId);
                out2.write(Long.toString(roadId));
                out2.write("\r\n");
            }
        }

        System.out.println(roadNodes.size());
        for (long roadNodeId : roadNodes) {
            out3.write(Long.toString(roadNodeId));
            out3.write(",");
            out3.write(Double.toString(pointMap.get(roadNodeId).longitude));
            out3.write(",");
            out3.write(Double.toString(pointMap.get(roadNodeId).latitude));
            out3.write("\r\n");
        }

        out1.close();
        out2.close();
        out3.close();
    }
}
