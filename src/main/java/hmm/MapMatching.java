package hmm;

import hmm.reader.GpsDataReader;
import hmm.reader.RoadDataReader;
import hmm.utils.OfflineMapMatching;

import java.util.List;

public class MapMatching {
    public static void main(String args[]) {
        // prepare data
        List gpsMeasurements = GpsDataReader.getData("test_gps_data.txt");
        List roadEdges = RoadDataReader.getData("test_road_network.txt");

        // map matching
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching(gpsMeasurements, roadEdges);
        List result = offlineMapMatching.run();
        System.out.println(result.size());
    }
}
