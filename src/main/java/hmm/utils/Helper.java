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
    public static double computeDistance_round(Point p1, Point p2, Point p3) {
        double a = computeDistance(p2, p3);
        double b = computeDistance(p1, p2);
        double c = computeDistance(p1, p3);
        double cos_alpha = (a * a + b * b - c * c) / (2 * a * b);
        double cos_beta = (a * a + c * c - b * b) / (2 * a * c);
        if (cos_alpha <= 0) {
            return b;
        }
        if (cos_beta <= 0) {
            return c;
        }
        return (Math.sqrt((a + b + c) * (a + b - c) * (a + c - b) * (b + c - a)) / (2 * a));
    }

/*
    public static double computeDistance_line(Point p1, Point p2, Point p3) {
        double d_x = p1.latitude;
        double d_y = p2.longitude;
        double point1_x = p2.latitude;
        double point1_y = p2.longitude;
        double point2_x = p3.latitude;
        double point2_y = p3.longitude;
        double cross = (point2_x - point1_x) * (d_x - point1_x) + (point2_y - point1_y) * (d_y - point1_y);
        double dist2 = Math.pow((point2_x - point1_x), 2) + Math.pow((point2_y - point1_y), 2);

        if (cross <= 0) {
            return Math.sqrt(Math.pow((d_x - point1_x), 2) + Math.pow((d_y - point1_y), 2));
        }

        if (cross >= dist2) {
            return Math.sqrt(Math.pow((d_x - point2_x), 2) + Math.pow((d_y - point2_y), 2));
        }
        double r = cross / dist2;
        double p_x = point1_x + (point2_x - point1_x) * r;
        double p_y = point1_y + (point2_y - point1_y) * r;
        return Math.sqrt(Math.pow((d_x - p_x), 2) + Math.pow((d_y - p_y), 2));
    }
 */

}
