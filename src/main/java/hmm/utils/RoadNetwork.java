package hmm.utils;

import hmm.types.GpsMeasurement;
import hmm.types.Point;

import java.io.*;

public class RoadNetwork {
    private final static String filePath = "E:\\Files\\Project\\2019 Summer Research\\MapMatching\\data";

    private final static String fileName = "road_network.txt";

    public static void getData() {
        File file = new File(filePath, fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s = in.readLine();

            s = in.readLine();
            String temp[] = s.split("\\t");


            while ((s = in.readLine()) != null) {
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
