package hmm;

import hmm.types.GpsMeasurement;
import hmm.utils.GpsData;
import hmm.utils.OfflineMapMatching;

import java.util.Iterator;
import java.util.List;

public class MapMatching {
    public static void main(String args[]) {
        List<GpsMeasurement> gpsMeasurements = GpsData.getData();
        OfflineMapMatching offlineMapMatching = new OfflineMapMatching();
        offlineMapMatching.testMapMatching();
    }
}
