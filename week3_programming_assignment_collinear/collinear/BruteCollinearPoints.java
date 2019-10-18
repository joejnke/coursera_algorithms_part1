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
    private LineSegment[] ls4List;
    private Point[] pointsBCP;
    // private int numlines;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new IllegalArgumentException("null constructor argument not allowed..."); }
        this.pointsBCP = points.clone();

        for (Point p : pointsBCP) {
            if (p == null) { throw new IllegalArgumentException("constructor argument contains null points..."); }
        }

        Arrays.sort(pointsBCP);
        if (pointsBCP.length > 1) {
            for (int i = 0, j = 1; j < pointsBCP.length; i++, j++) {
                if (pointsBCP[i].compareTo(pointsBCP[j]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }
            }
        }

        // if (points[0].compareTo(points[1]) == 0) { throw new IllegalArgumentException("constructor argument contains repeated points..."); }


        List<LineSegment> ls4 = new ArrayList<LineSegment>();
        // this.numlines = 0;
        // this.ls4List = new LineSegment[points.length * points.length];
        // for (Point p : points) {System.out.println(p); }
        for (int i = 0; i < pointsBCP.length - 3; i++) {
            for (int j = i + 1; j < pointsBCP.length - 2; j++) {
                for (int k = j + 1; k < pointsBCP.length - 1; k++) {
                    double slopeIJ = pointsBCP[i].slopeTo(pointsBCP[j]);
                    double slopeIK = pointsBCP[i].slopeTo(pointsBCP[k]);
                    if (slopeIJ == slopeIK) {
                        for (int m = k + 1; m < pointsBCP.length; m++) {
                            if (slopeIK == pointsBCP[i].slopeTo(pointsBCP[m])) {
                                ls4.add(new LineSegment(pointsBCP[i], pointsBCP[m]));
                                // System.out.println("lines: " + points[i].toString() + " -> "
                                //                            + points[j].toString() + " -> "
                                //                            + points[k].toString() + " -> "
                                //                            + points[m].toString());

                                // this.ls4List[this.numlines] = new LineSegment(points[i], points[m]);
                                // this.numlines++;
                            }
                        }
                    }
                }
            }
        }

        this.ls4List = new LineSegment[ls4.size()];
        for (int i = 0; i < ls4.size(); i++) {
            this.ls4List[i] = ls4.get(i);
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return this.ls4List.length; // this.numlines; //this.ls4.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return this.ls4List.clone(); //(LineSegment[]) this.ls4.toArray();
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
