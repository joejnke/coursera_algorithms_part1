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

public class FastCollinearPoints {
    private LineSegment[] ls4List;
    // private int numPoints;
    // private int numlines;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        // this.numlines = 0;
        ArrayList<LineSegment> als4 = new ArrayList<LineSegment>();
        ArrayList<String> als4Str = new ArrayList<String>();
        // this.ls4List = new LineSegment[points.length * points.length];

        // if (points.length >= 4) {
        //     for (Point p0 : points) {
        //         Point[] tempPoints = points;
        //         ArrayList<Point> collinearPoints = new ArrayList<Point>();
        //
        //         Arrays.sort(tempPoints, p0.slopeOrder());
        //         // System.out.println("######### " + p0.toString() + " ##########");
        //         Point lineEnd = tempPoints[1];
        //         double refSlope = p0.slopeTo(lineEnd);
        //         this.numPoints = 2;
        //         collinearPoints.add(p0);
        //         collinearPoints.add(lineEnd);
        //
        //         for (int i = 2; i < tempPoints.length; i++) {
        //             if (Double.compare(p0.slopeTo(tempPoints[i]), refSlope) == 0) {
        //                 lineEnd = tempPoints[i];
        //                 collinearPoints.add(lineEnd);
        //                 this.numPoints++;
        //             }
        //
        //             else {
        //                 refSlope = p0.slopeTo(tempPoints[i]);
        //                 if (this.numPoints > 3) {
        //                     // collinearPoints.sort();
        //                     Collections.sort(collinearPoints);
        //                     this.als4.add(new LineSegment(collinearPoints.get(0), collinearPoints.get(collinearPoints.size()-1)));
        //                     // this.ls4List[this.numlines] = new LineSegment(p0, lineEnd);
        //                     this.numlines++;
        //                 }
        //                 this.numPoints = 2;
        //             }
        //         }
        //
        //         System.out.println(this.als4.size());
        //     }
        //
        // }

        // for (Point p : points) {
        //     System.out.println("out######### " + p.toString() + " ##########");
        // }

        for (Point centerPoint : points) {
            Point[] tempPoints = points.clone();

            // sort with slope order relative to centerPoint
            Arrays.sort(tempPoints, centerPoint.slopeOrder());

            /* assuming there are atleast four points in the point list */
            Point[] tempLineStartEnd = new Point[]{centerPoint, tempPoints[1]};
            // after sorted, tempLineStartEnd[0] will be start of the line segment and tempLineStartEnd[1] will be end of the line segment
            Arrays.sort(tempLineStartEnd);

            Point lineStart = tempLineStartEnd[0]; // set to minimum of centerPoint and tempPoints[1]
            Point lineEnd = tempLineStartEnd[1]; // set to maximum of centerPoint and tempPoints[1]
            double refSlope = centerPoint.slopeTo(tempPoints[1]);
            int numCollPoints = 2;

            // System.out.println("########## center point updated to " + centerPoint.toString() + " #########");
            for (int i = 2; i < tempPoints.length; i++) {
                if (Double.compare(centerPoint.slopeTo(tempPoints[i]), refSlope) == 0) {
                    lineEnd = tempPoints[i];
                    numCollPoints++;
                    // System.out.println("########## line segment sequence end updated to " + lineEnd.toString() + " #########");
                }

                else if (numCollPoints >= 4) {
                    // set lineStart and lineEnd the right value.
                    tempLineStartEnd[0] = centerPoint;
                    tempLineStartEnd[1] = lineEnd;
                    // after sorted, tempLineStartEnd[0] will be start of the line segment and tempLineStartEnd[1] will be end of the line segment
                    Arrays.sort(tempLineStartEnd);

                    // form and store the line segment
                    LineSegment toAdd = new LineSegment(lineStart, tempLineStartEnd[1]);
                    if (!als4Str.contains(toAdd.toString())) {
                        als4Str.add(toAdd.toString());
                        als4.add(toAdd);
                        // System.out.println("########## line segment " + lineStart.toString() + " -> " + tempLineStartEnd[1].toString() + " created #########");
                    }
                    // else {
                    //     System.out.println("########## line segment " + lineStart.toString() + " -> " + tempLineStartEnd[1].toString() + " exists #########");
                    // }
                    // this.als4.add(new LineSegment(lineStart, tempLineStartEnd[1]));
                    // System.out.println("########## line segment " + lineStart.toString() + " -> " + tempLineStartEnd[1].toString() + " created #########");

                    // reset numCollPoints to 2
                    numCollPoints = 2;

                    // update refSlope to slope of center point and point i
                    refSlope = centerPoint.slopeTo(tempPoints[i]);

                    // reset lineStart and lineEnd -- to whom? encapsulate the code below into a method
                    tempLineStartEnd[0] = centerPoint;
                    tempLineStartEnd[1] = tempPoints[i];
                    Arrays.sort(tempLineStartEnd);
                    lineStart = tempLineStartEnd[0];
                    lineEnd = tempLineStartEnd[1];
                }

                else {
                    // reset numCollPoints to 2
                    numCollPoints = 2;

                    // update refSlope to slope of center point and point i (index of the next collinear sequence begin point)
                    refSlope = centerPoint.slopeTo(tempPoints[i]);

                    // reset lineStart and lineEnd -- to whom? encapsulate the code below into
                    // a method named resetSequence(Point centerPoint, int sequenceStart)
                    tempLineStartEnd[0] = centerPoint;
                    tempLineStartEnd[1] = tempPoints[i];
                    Arrays.sort(tempLineStartEnd);
                    lineStart = tempLineStartEnd[0];
                    lineEnd = tempLineStartEnd[1];
                    // System.out.println("########## line segment reseted to start from" + tempPoints[i].toString() + " #########");
                }

                // if there is only one line segment where all the points are collinear
                // or if the final point is reached while the sequence containes long enough collinear points.
                if (numCollPoints >= 4 && (i + 1) == points.length) {
                    // set lineStart and lineEnd the right value.
                    tempLineStartEnd[0] = centerPoint;
                    tempLineStartEnd[1] = lineEnd;
                    // after sorted, tempLineStartEnd[0] will be start of the line segment and tempLineStartEnd[1] will be end of the line segment
                    Arrays.sort(tempLineStartEnd);

                    // form and store the line segment
                    LineSegment toAdd = new LineSegment(lineStart, tempLineStartEnd[1]);
                    if (!als4Str.contains(toAdd.toString())) {
                        als4Str.add(toAdd.toString());
                        als4.add(toAdd);
                        // System.out.println("########## line segment " + lineStart.toString() + " -> " + tempLineStartEnd[1].toString() + " created #########");
                    }
                    // else {
                    //     System.out.println("########## line segment " + lineStart.toString() + " -> " + tempLineStartEnd[1].toString() + " exists #########");
                    // }
                }
            }
        }

        this.ls4List = new LineSegment[als4.size()];
        for (int i = 0; i < als4.size(); i++) {
            this.ls4List[i] = als4.get(i);
        }

        // if (points.length >= 4) {
        //     for (Point p0 : points) {
        //         Point[] tempPoints = points.clone();
        //         Arrays.sort(tempPoints, p0.slopeOrder());
        //         System.out.println("######### ######## ##########");
        //         for (Point p : tempPoints) {
        //             System.out.println("######### " + p.toString() + " ##########");
        //         }
        //         System.out.println("######### ######## ##########");
        //
        //         System.out.println("######### " + p0.toString() + " ##########");
        //         Point lineStart = tempPoints[0];
        //         Point lineEnd = tempPoints[1];
        //         double refSlope = p0.slopeTo(lineEnd);
        //         this.numPoints = 2;
        //
        //         for (int i = 1; i < tempPoints.length; i++) {
        //             if (Double.compare(p0.slopeTo(tempPoints[i]), refSlope) == 0) {
        //                 if (tempPoints[i].compareTo(lineEnd) > 0) {
        //                     // set prevEnd point to start if its less than both current start and end points
        //                     if (lineEnd.compareTo(lineStart) < 0) { lineStart = lineEnd; }
        //
        //                     // update current end point
        //                     lineEnd = tempPoints[i];
        //                 }
        //
        //                 if (tempPoints[i].compareTo(lineStart) < 0) {
        //                     // set prevStart point to end if its greater than both current start and end points
        //                     if (lineStart.compareTo(lineEnd) > 0) { lineEnd = lineStart; }
        //
        //                     // update current start point
        //                     lineStart = tempPoints[i];
        //                 }
        //
        //                 this.numPoints++;
        //             }
        //
        //             else {
        //                 refSlope = p0.slopeTo(tempPoints[i]);
        //                 if (this.numPoints > 3) {
        //                     LineSegment coll = new LineSegment(lineStart, lineEnd);
        //                     if (!this.als4.contains(coll)) {
        //                         System.out.println("got: " + coll.toString());
        //                         this.als4.add(coll);
        //                     }
        //                     else { System.out.println("got and jumped: " + coll.toString()); }
        //
        //                     System.out.println("numpoints: " + numPoints + "coll: " + coll.toString());
        //                     // this.ls4List[this.numlines] = new LineSegment(p0, lineEnd);
        //                     this.numlines++;
        //                 }
        //                 this.numPoints = 2;
        //             }
        //         }
        //
        //         System.out.println(this.als4.size());
        //     }
        //
        //     this.ls4List = new LineSegment[this.als4.size()];
        //     for (int i = 0; i < this.als4.size(); i++) {
        //         this.ls4List[i] = this.als4.get(i);
        //     }
        // }

        // for (Point p0 : points) {
        //     Arrays.sort(points, p0.slopeOrder());
        //     // System.out.println("######### " + p0.toString() + " ##########");
        //     Point lineEnd = p0;
        //     double refSlope = p0.slopeTo(p0);
        //
        //     for (int i = 0; i < points.length; i++) {
        //         if (Double.compare(p0.slopeTo(points[i]), refSlope) == 0) {
        //             lineEnd = points[i];
        //             this.numPoints++;
        //         }
        //
        //         else {
        //             refSlope = p0.slopeTo(points[i]);
        //             if (this.numPoints > 3) {
        //                 this.als4.add(new LineSegment(p0, lineEnd));
        //                 // this.ls4List[this.numlines] = new LineSegment(p0, lineEnd);
        //                 this.numlines++;
        //             }
        //             this.numPoints = 1;
        //         }
        //     }
        // }
        //
        // this.ls4List = new LineSegment[this.als4.size()];
        // for (int i = 0; i < this.als4.size(); i++) {
        //     this.ls4List[i] = this.als4.get(i);
        // }

    }

    // the number of line segments
    public int numberOfSegments() {
        return this.ls4List.length; // this.numlines; // this.ls4.size();
    }

    // the line segments
    public LineSegment[] segments() {
        // // int als4Size = this.als4.size();
        // for (int i = 0; i < this.als4.size(); i++) {
        //     for (int j = i + 1; j < this.als4.size(); j++) {
        //         if (this.als4.get(i).toString().equals(this.als4.get(j).toString())) {
        //             this.als4.remove(j);
        //         }
        //     }
        // }
        // this.ls4List = new LineSegment[this.als4.size()];
        // for (int i = 0; i < this.als4.size(); i++) {
        //     this.ls4List[i] = this.als4.get(i);
        // }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}