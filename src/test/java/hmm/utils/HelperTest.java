package hmm.utils;

import hmm.types.Point;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelperTest{

    private final Point p1 = new Point(116.408078, 39.920248);
    private final Point p2 = new Point(116.399184, 39.919916);
    private final Point p3 = new Point(116.343328, 39.948574);

    @Test
    public void testComputeDistance() {
        double d1 = Helper.computeDistance(p1, p2);
        double d2 = Helper.computeDistance(p1, p3);
        assertEquals(759, d1, 759 * 0.05);
        assertEquals(6400, d2, 6400 * 0.05);
    }

}