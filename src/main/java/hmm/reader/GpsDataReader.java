package hmm.reader;

import hmm.types.GpsMeasurement;
import hmm.types.Point;
import java.io.*;
import java.util.*;

public class GpsDataReader {

    private final static String filePath = "E:\\Files\\Project\\2019 Summer Research\\MapMatching\\data";

    public static List<GpsMeasurement> getData(String fileName) {
        File file = new File(filePath, fileName);
        List<GpsMeasurement> gpsMeasurements = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s = in.readLine();
            while ((s = in.readLine()) != null) {
                String temp[] = s.split("\\t");
                Point point = new Point(Double.parseDouble(temp[3]), Double.parseDouble(temp[2]));
                GpsMeasurement gpsMeasurement = new GpsMeasurement(seconds(temp[1]), point);
                // TODO filter out points within 2σ of the previous point
                gpsMeasurements.add(gpsMeasurement);
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
        return gpsMeasurements;
    }

    private static Date seconds(String time) {
        String t[] = time.split(":");
        int second = Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
        Calendar c = new GregorianCalendar(2009, 0, 17);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }

}
