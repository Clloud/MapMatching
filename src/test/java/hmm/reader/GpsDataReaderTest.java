package hmm.reader;

import hmm.types.GpsMeasurement;
import hmm.utils.Helper;
import hmm.utils.OfflineMapMatching;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GpsDataReaderTest extends Helper {

    @Test
    public void getData() {
        List<GpsMeasurement> a=GpsDataReader.getData("gps_data.txt");
        System.out.print(a.size());
    }
}