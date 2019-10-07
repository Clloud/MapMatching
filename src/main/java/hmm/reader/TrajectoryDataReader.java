package hmm.reader;

import hmm.types.GpsMeasurement;
import hmm.types.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static hmm.utils.Helper.computeDistance;

public class TrajectoryDataReader {

    public static List<GpsMeasurement> getData(String filePath) {
        File file = new File(filePath);
        List<GpsMeasurement> trajectoryMeasurements = new ArrayList<>();
        double σ = 4.07;
        int lines = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s = in.readLine();
            while ((s = in.readLine()) != null) {
                lines++;
                if (lines >= 6) {
                    String temp[] = s.split(",");
                    Point point = new Point(Double.parseDouble(temp[1]), Double.parseDouble(temp[0]));
                    GpsMeasurement trajectoryMeasurement = new GpsMeasurement(seconds(temp[6]), point);
                    // filter out points within 2σ of the previous point
                    if (trajectoryMeasurements.size() == 0 || computeDistance(point, trajectoryMeasurements.get(trajectoryMeasurements.size() - 1).position) >= 2 * σ) {
                        trajectoryMeasurements.add(trajectoryMeasurement);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return trajectoryMeasurements;
    }

    private static Date seconds(String time) {
        String t[] = time.split(":");
        int second = Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
        Calendar c = new GregorianCalendar(2009, 0, 17);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }
}
