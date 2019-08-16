package hmm;

import com.bmw.hmm.SequenceState;
import hmm.types.*;
import hmm.utils.GpsData;
import hmm.utils.OfflineMapMatching;
import hmm.utils.RoadNetwork;

import java.util.List;

public class MapMatching {
    public static void main(String args[]) {
//        List<GpsMeasurement> gpsMeasurements = GpsData.getData();

        List<RoadEdge> roadEdges = RoadNetwork.getData();
        RoadEdgeIndex roadEdgeIndex = new RoadEdgeIndex();
        for (RoadEdge roadEdge : roadEdges) {
            roadEdgeIndex.add(roadEdge);
        }
        roadEdgeIndex.tree.visualize(6000,9000).save("target/mytree.png");
        List entries = roadEdgeIndex.search(new Point(-122.732318937778, 47.8899192810059), 200);

//        OfflineMapMatching offlineMapMatching = new OfflineMapMatching();
//        List<SequenceState<RoadPosition, GpsMeasurement, RoadPath>> result
//                = offlineMapMatching.testMapMatching();
//        System.out.println(result.size());
    }
}
