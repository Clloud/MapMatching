package hmm.reader;

import hmm.types.Point;
import hmm.types.RoadEdge;
import java.io.*;
import java.util.*;

public class RoadDataReader {

    private final static String filePath = "E:\\Files\\Project\\2019 Summer Research\\MapMatching\\data";

    public static List<RoadEdge> getData(String fileName) {
        File file = new File(filePath, fileName);
        List<RoadEdge> roadEdges = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s = in.readLine();
            while ((s = in.readLine()) != null) {
                String t[] = s.split("\\t");
                RoadEdge roadEdge = new RoadEdge(
                        Long.parseLong(t[0]),
                        Long.parseLong(t[1]),
                        Long.parseLong(t[2]),
                        t[3].equals("1"),
                        line(t[6])
                );
                roadEdges.add(roadEdge);
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

        return roadEdges;
    }

    private static List<Point> line(String lineString) {
        List<Point> result = new ArrayList<>();
        lineString = lineString.substring(11, lineString.length() - 1);
        String t[] = lineString.split(",");

        for (String temp : t) {
            String s[] = temp.trim().split("\\s");
            result.add(new Point(
                    Double.parseDouble(s[0]),
                    Double.parseDouble(s[1])
            ));
        }
        return result;
    }
}
