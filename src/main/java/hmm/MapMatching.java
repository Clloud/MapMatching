package hmm;

import com.bmw.hmm.SequenceState;
import hmm.types.GpsMeasurement;
import hmm.types.RoadEdge;
import hmm.types.RoadPath;
import hmm.types.RoadPosition;
import hmm.utils.GpsData;
import hmm.utils.OfflineMapMatching;
import hmm.utils.RoadNetwork;

import java.util.List;

public class MapMatching {
    public static void main(String args[]) {
//        List<GpsMeasurement> gpsMeasurements = GpsData.getData();

        List<RoadEdge> roadEdges = RoadNetwork.getData();
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching();
//        List<SequenceState<RoadPosition, GpsMeasurement, RoadPath>> result
//                = offlineMapMatching.testMapMatching();
//        System.out.println(result.size());
    }
}
