package hmm.utils;

import hmm.types.Point;
import org.junit.Test;

import static org.junit.Assert.*;

public class OfflineMapMatchingTest extends OfflineMapMatching {

    private Point p1 = new Point(116.408078, 39.920248);
    private Point p2 = new Point(116.399184, 39.919916);
    private Point p3 = new Point(116.343328, 39.948574);

    @Test
    public void testComputeDistance() {
        OfflineMapMatching distance_test = new OfflineMapMatching();
        double d1 = distance_test.computeDistance(p1, p2);
        double d2 = distance_test.computeDistance(p1, p3);
        System.out.println(d1);
        assertEquals(759, d1, 759 * 0.05);
        System.out.println(d2);
        assertEquals(6400, d2, 6400 * 0.05);
    }

}