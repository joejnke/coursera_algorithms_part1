/* *****************************************************************************
 *  Name: Kirubel Kassaye
 *  Date: October 16, 2019
 *  Description: Algorithms Assignment3
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> ls4;
    private int numPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException("null constructor argument not allowed..."); }

        Arrays.sort(points);
        if (points[0].compareTo(points[1]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }

        for (Point p : points) {
            if (p == null) { throw new IllegalArgumentException("constructor argument contains null points..."); }
        }

        this.ls4 = new ArrayList<LineSegment>();

        for (Point p0 : points) {
            Arrays.sort(points, p0.slopeOrder());
            Point lineEnd = p0;
            double refSlope = p0.slopeTo(points[1]);

            for (int i = 2; i < points.length; i++) {
                if (p0.slopeTo(points[i]) == refSlope) {
                    lineEnd = points[i];
                    this.numPoints++;
                }

                else {
                    refSlope = p0.slopeTo(points[i]);
                    if (this.numPoints > 3) { this.ls4.add(new LineSegment(p0, lineEnd)); }
                    this.numPoints = 1;
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

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}