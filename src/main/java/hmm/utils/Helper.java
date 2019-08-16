package hmm.utils;

import hmm.types.Point;

public class Helper {

    /*
     * Returns the great circle distance [m] between two GPS points.
     */
    public static double computeDistance(Point p1, Point p2) {
        double EARTH_RADIUS = 6378.137;
        double radLat1 = rad(p1.latitude);
        double radLat2 = rad(p2.latitude);
        double a = radLat1 - radLat2;
        double b = rad(p1.longitude) - rad(p2.longitude);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS * 1000;
        s = Math.round(s * 1000d) / 1000d;
        return s;
    }

    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /* convert distance difference [m] to longitude difference [°] */
    public static double distanceToLongitude(Point point, double d) {
        return d / (Math.cos(rad(point.latitude)) * 111318.078);
    }

    /* convert distance difference [m] to latitude difference [°] */
    public static double distanceToLatitude(double d) {
        return d / 111319.5;
    }

    /*
     *  Returns the great circle distance [m] from GPS point p1 to line determined by
     *  GPS point p2 and GPS point p3.
     */
    public static double computeDistance(Point p1, Point p2, Point p3) {
        // TODO
        return 0;
    }

}
