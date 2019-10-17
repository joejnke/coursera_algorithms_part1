/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> ls4;
    private LineSegment[] ls4List;
    private int numlines;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException("null constructor argument not allowed..."); }

        for (Point p : points) {
            if (p == null) { throw new IllegalArgumentException("constructor argument contains null points..."); }
        }

        Arrays.sort(points);
        if (points.length > 1) {
            for (int i = 0, j = 1; j < points.length; i++, j++) {
                if (points[i].compareTo(points[j]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }
            }
        }

        // if (points[0].compareTo(points[1]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }


        this.ls4 = new ArrayList<LineSegment>();
        this.numlines = 0;
        // this.ls4List = new LineSegment[points.length * points.length];
        // for (Point p : points) {System.out.println(p); }
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    double slopeIJ = points[i].slopeTo(points[j]);
                    double slopeIK = points[i].slopeTo(points[k]);
                    if (slopeIJ == slopeIK) {
                        for (int m = k + 1; m < points.length; m++) {
                            if (slopeIK == points[i].slopeTo(points[m])) {
                                this.ls4.add(new LineSegment(points[i], points[m]));
                                // System.out.println("lines: " + points[i].toString() + " -> "
                                //                            + points[j].toString() + " -> "
                                //                            + points[k].toString() + " -> "
                                //                            + points[m].toString());

                                // this.ls4List[this.numlines] = new LineSegment(points[i], points[m]);
                                this.numlines++;
                            }
                        }
                    }
                }
            }
        }

        this.ls4List = new LineSegment[this.ls4.size()];
        for (int i = 0; i < this.ls4.size(); i++) {
            this.ls4List[i] = this.ls4.get(i);
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return this.ls4List.length; // this.numlines; //this.ls4.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return this.ls4List; //(LineSegment[]) this.ls4.toArray();
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
