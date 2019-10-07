package hmm.reader;

import hmm.types.GpsMeasurement;
import hmm.utils.Helper;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TrajectoryDataReaderTest extends Helper {
    Long trajectoryId=12L;

    @Test
    public void getData() throws IOException {
        //List<GpsMeasurement> a=TrajectoryDataReader.getFile(new File("D:/HMM/test2"));
        //System.out.print(a.size());

//    private final static String rootPath = "D:/HMM/test2";
//    public List<GpsMeasurement> trajectoryMeasurements = new ArrayList<>();
//
//    public TrajectoryDataReader() {
//        getFile(new File(rootPath));
//    }
//
//    private void getFile(File rootFile) {
//        File[] fs = rootFile.listFiles();
//        for (File f : fs) {
//            if (f.isDirectory()) {
//                getFile(f);
//            }
//            if (f.isFile()) {
//                this.trajectoryMeasurements=getData(f.getPath());
//                System.out.println(f.getPath());
//            }
//        }
//    }

        String pathName="./target/result/"+trajectoryId.toString()+".txt";
        File file1 = new File(pathName);
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file1));
        out1.write(",");
        out1.close();

    }
}