package hmm;

import com.bmw.hmm.SequenceState;
import hmm.reader.GpsDataReader;
import hmm.reader.RoadDataReader;
import hmm.storage.ToFille;
import hmm.types.RoadPosition;
import hmm.utils.OfflineMapMatching;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapMatching {
    public static void main(String args[]) throws IOException {
        // prepare data
        List gpsMeasurements = GpsDataReader.getData("test_gps_data.txt");
        List roadEdges = RoadDataReader.getData("test_road_network.txt");
        ToFille roadIdFile = new ToFille();

        // map matching
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching(gpsMeasurements, roadEdges);
        List result = offlineMapMatching.run();
        List<Long> roadIds = new ArrayList<>();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("D:/HMM/road_ids_result.txt"));

        for (Object o : result) {
            long roadId = ((RoadPosition) ((SequenceState) o).state).edgeId;
            if (roadIds.size() == 0 || roadId != roadIds.get(roadIds.size() - 1)) {
                roadIds.add(roadId);
                dos.writeLong(roadId);
            }
        }
        dos.close();
        System.out.println(roadIds);
    }
}
