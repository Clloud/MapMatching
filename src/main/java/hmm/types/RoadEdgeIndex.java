package hmm.types;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import java.util.List;

import static hmm.utils.Helper.*;

public class RoadEdgeIndex {

    public RTree<RoadEdge, Geometry> tree;

    public RoadEdgeIndex() {
        tree = RTree.star().create();
    }

    public void add(RoadEdge roadEdge) {
        for (int i = 0; i < roadEdge.line.size() - 1; i++) {
            Point currentPoint = roadEdge.line.get(i);
            Point nextPoint = roadEdge.line.get(i+1);
            tree = tree.add(roadEdge, Geometries.line(
                    currentPoint.longitude,
                    currentPoint.latitude,
                    nextPoint.longitude,
                    nextPoint.latitude
            ));
        }
    }

    /*
     * Returns geometry entries within the given radius [m].
     */
    public List<Entry<RoadEdge, Geometry>> search(GpsMeasurement gpsMeasurement, double radius) {
        Point point = gpsMeasurement.position;
        final double deltaLon = distanceToLongitude(point, radius);
        final double deltaLat = distanceToLatitude(radius);

        List<Entry<RoadEdge, Geometry>> entries =
                tree.search(Geometries.rectangle(
                        point.longitude - deltaLon,
                        point.latitude - deltaLat,
                        point.longitude + deltaLon,
                        point.latitude + deltaLat))
                        .toList().toBlocking().first();
        return entries;
    }
}
