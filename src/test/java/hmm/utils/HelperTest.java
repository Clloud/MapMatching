package hmm.utils;

import hmm.types.Point;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    private final Point p1 = new Point(116.408078, 39.920248);
    private final Point p2 = new Point(116.399184, 39.919916);
    private final Point p3 = new Point(116.343328, 39.948574);
    private final Point p4 = new Point(116.403909, 39.913302);

    @Test
    public void testComputeDistance() {
        double d1 = Helper.computeDistance(p1, p2);
        double d2 = Helper.computeDistance(p1, p3);
        assertEquals(759, d1, 759 * 0.05);
        assertEquals(6400, d2, 6400 * 0.05);
    }

//    @Test
//    public void testComputeDistance_round() {
//        double d3 = Helper.computeDistance_round(p3, p1, p2);
//        double d4 = Helper.computeDistance_round(p4, p1, p2);
//        //System.out.println(d3);
//        //System.out.println(d4);
//        assertEquals(5700, d3, 5700 * 0.05);
//        assertEquals(756, d4, 756 * 0.05);
//    }

/*
    @Test
    public void testComputeDistance_line() {
        Point p5 = new Point(10, 10);
        Point p6 = new Point(20, 20);
        Point p7 = new Point(10, 20);
        double d5 = Helper.computeDistance_line(p7, p5, p6);
        System.out.println(d5);
    }
 */

}