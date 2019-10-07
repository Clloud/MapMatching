package hmm.storage;

import hmm.reader.GpsDataReader;
import hmm.reader.TrajectoryDataReader;
import hmm.types.GpsMeasurement;
import hmm.types.RoadEdge;
import hmm.types.RoadNetwork;
import hmm.utils.OfflineMapMatching;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GetFile {

    private List<RoadEdge> roadEdges;
    private RoadNetwork roadNetwork;
    private Long trajectoryId = 0L;

    public GetFile(String filePath, List<RoadEdge> roadEdges, RoadNetwork roadNetwork) throws IOException {
        this.roadEdges = roadEdges;
        this.roadNetwork = roadNetwork;
        File file = new File(filePath);
        func(file);
    }

    private void func(File file) throws IOException {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory())
                func(f);
            if (f.isFile()) {
                // prepare data
                List<GpsMeasurement> trajectoryMeasurements = TrajectoryDataReader.getData(f.getPath());
//                List gpsMeasurements = GpsDataReader.getData("gps_data.txt");

                // map matching
                OfflineMapMatching offlineMapMatching = new OfflineMapMatching(trajectoryMeasurements, roadEdges, roadNetwork);
                List result = offlineMapMatching.run();

                // output result in trajectoryId.txt
                Printer printer = new Printer(roadNetwork);
                printer.writeFile(result, trajectoryId);
                trajectoryId++;
            }
        }
    }

//    public static void main(String args[]) throws IOException {
//        List roadEdges = RoadDataReader.getData("road_network.txt");
//        RoadNetwork roadNetwork = new RoadNetwork(roadEdges);
//        new GetFile("D:/HMM/test2/Data", roadEdges, roadNetwork);
//    }

}