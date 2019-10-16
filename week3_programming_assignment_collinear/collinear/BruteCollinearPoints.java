/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> ls4;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException("null constructor argument not allowed..."); }

        Arrays.sort(points);
        if (points[0].compareTo(points[1]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }

        for (Point p : points) {
            if (p == null) { throw new IllegalArgumentException("constructor argument contains null points..."); }
        }

        this.ls4 = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    double slopeIJ = points[i].slopeTo(points[j]);
                    double slopeIK = points[i].slopeTo(points[k]);
                    if (slopeIJ == slopeIK) {
                        for (int m = k + 1; m < points.length - 2; m++) {
                            if (slopeIK == points[i].slopeTo(points[m])) {
                                this.ls4.add(new LineSegment(points[i], points[m]));
                            }
                        }
                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return this.ls4.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return (LineSegment[]) this.ls4.toArray();
    }
}
