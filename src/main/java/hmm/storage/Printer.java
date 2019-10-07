package hmm.storage;

import com.bmw.hmm.SequenceState;
import hmm.types.Point;
import hmm.types.RoadNetwork;
import hmm.types.RoadPosition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Printer {

    private RoadNetwork roadNetwork;

    public Printer(RoadNetwork roadNetwork) {
        this.roadNetwork = roadNetwork;
    }

    public void writeFile(List result, Long trajectoryId) throws IOException {
        String pathName1 = "./target/result/" + trajectoryId.toString() + ".txt";
        String pathName2 = "./target/result2/" + trajectoryId.toString() + ".txt";
        File file1 = new File(pathName1);
        File file2 = new File(pathName2);
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2));
        List<Long> roadIds = new ArrayList<>();

        System.out.println(result.size());
        for (Object o : result) {
            long roadId = ((RoadPosition) ((SequenceState) o).state).edgeId;
            Point pointPosition = ((RoadPosition) ((SequenceState) o).state).position;
            long fromNodeId = roadNetwork.roadEdges.get(roadId).fromNodeId;
            long toNodeId = roadNetwork.roadEdges.get(roadId).toNodeId;
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
                out2.write(",");
                out2.write(Long.toString(fromNodeId));
                out2.write(",");
                out2.write(Long.toString(toNodeId));
                out2.write("\r\n");
            }
        }
        out1.close();
        out2.close();
    }
}
