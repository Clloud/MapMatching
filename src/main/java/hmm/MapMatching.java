package hmm;

import com.bmw.hmm.SequenceState;
import hmm.reader.GpsDataReader;
import hmm.reader.RoadDataReader;
import hmm.types.RoadPosition;
import hmm.utils.OfflineMapMatching;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapMatching {
    public static void main(String args[]) throws IOException {
        // prepare data
        List gpsMeasurements = GpsDataReader.getData("gps_data.txt").subList(0, 60);
        List roadEdges = RoadDataReader.getData("road_network.txt");

        // map matching
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching(gpsMeasurements, roadEdges);
        List result = offlineMapMatching.run();

        // output result
        File file1 = new File("./target/result1.txt");
        File file2 = new File("./target/result2.txt");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        List<Long> roadIds = new ArrayList<>();

        System.out.println(result.size());
        for (Object o : result) {
            long roadId = ((RoadPosition) ((SequenceState) o).state).edgeId;
            System.out.println(roadId);
            out1.write(Long.toString(roadId));
            out1.write("\r\n");
            if (roadIds.size() == 0 || roadId != roadIds.get(roadIds.size() - 1)) {
                roadIds.add(roadId);
                out2.write(Long.toString(roadId));
                out2.write("\r\n");
            }
        }
        out1.close();
        out2.close();
    }
}
