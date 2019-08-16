package hmm.utils;

import hmm.types.Point;

public class Helper {

    /*
     * Returns the great circle distance between two GPS points.
     */
    public static double computeDistance(Point p1, Point p2) {
        double EARTH_RADIUS = 6378.137;
        double radLat1 = rad(p1.latitude);
        double radLat2 = rad(p2.latitude);
        double a = radLat1 - radLat2;
        double b = rad(p1.longitude) - rad(p2.longitude);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }


}
