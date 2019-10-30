/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class PointSET {
    // private SET<Point2D> pointSet;
    private TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        // this.pointSet = new SET<Point2D>();
        this.pointSet = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {

        if (p == null) throw new IllegalArgumentException("call insert() with null argument...");
        this.pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("call contains() with null argument...");
        return this.pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    // doesnot have limit to points in the x dimension
    // might requier to imlement the Bruteforce implementation
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("call range() with null argument...");
        Point2D rectMin = new Point2D(rect.xmin(), rect.ymin());
        Point2D rectMax = new Point2D(rect.xmax(), rect.ymax());
        TreeSet<Point2D> inRange = new TreeSet<Point2D>();
        for (Point2D p :  this.pointSet.subSet(rectMin, true, rectMax, true)) {
            if (rect.contains(p)) inRange.add(p);
        }

        return inRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("call nearest() with null argument...");
        if (this.pointSet.isEmpty()) return null;
        if (this.pointSet.contains(p)) return p;
        
        else {
            Iterator<Point2D> itr = this.pointSet.iterator();
            Point2D nearest = this.pointSet.first();
            double minDist = 4.0;
            
            while (itr.hasNext()) {
                Point2D nextP = itr.next();
                if (p.distanceSquaredTo(nextP) < minDist) {
                    minDist = p.distanceSquaredTo(nextP);
                    // System.out.println("distance to " + nextP.toString() + " = " + minDist);
                    nearest = nextP;
                }
            }
            StdDraw.setPenColor(Color.blue);
            nearest.draw();
            return nearest;
        }

        // Point2D successor = this.pointSet.higher(p);
        // Point2D predecessor = this.pointSet.lower(p);
        //
        // if (successor == null) {
        //     System.out.println("successor is null: ");
        //     System.out.println("predecessor: " + predecessor);
        //     return predecessor;
        // }
        // if (predecessor == null) {
        //     System.out.println("predecessor is null: ");
        //     System.out.println("successor: " + successor);
        //     return successor;
        // }
        //
        // System.out.println("successor: " + successor.toString());
        // StdDraw.setPenColor(Color.cyan);
        // successor.draw();
        // System.out.println("predecessor: " + predecessor.toString());
        // StdDraw.setPenColor(Color.blue);
        // predecessor.draw();
        // double predDist = p.distanceTo(predecessor);
        // double succDist = p.distanceTo(successor);
        //
        // if (Double.compare(succDist, predDist) <= 0) return successor;
        // else return predecessor;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET testPointSET  = new PointSET();
        Point2D origin = new Point2D(0.75, 0.125);
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 0.5);
        int setSize = 0;
        // read the n points from a file
        In in = new In(args[0]);
        // draw the set
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1, 1.5);
        StdDraw.setYscale(-1, 1.5);
        StdDraw.setPenRadius(0.005);

        System.out.println("############## TEST (all outputs are expected to be TRUE except for contains() !!! ) ##############");

        System.out.println("set is empty initialy: " + testPointSET.isEmpty());
        System.out.println("inserting elements...");
        while (in.hasNextLine()) { // && !in.readLine().isEmpty()) {
            try {
                Point2D toInsert = new Point2D(in.readDouble(), in.readDouble());
                testPointSET.insert(toInsert);
                System.out.println(toInsert.toString());
                setSize++;
            }

            catch (NoSuchElementException e) {
                System.out.println(e.toString());
            }

        }

        // testPointSET.insert(origin);
        // origin.draw();
        // StdDraw.show();
        setSize++;
        System.out.println("set size is " + setSize + ": " + (testPointSET.size())); // == setSize));
        System.out.println("set contains " + origin.toString() + ": " + testPointSET.contains(origin));
        System.out.println("drawing all the points in the set...");
        testPointSET.draw();
        StdDraw.show();

        Iterable<Point2D> range = testPointSET.range(rect);
        System.out.println("drawing all the points in the " + rect.toString() + " rectangle with red color...");
        StdDraw.setPenRadius(0.008);
        StdDraw.setPenColor(Color.red);
        // StdDraw.rectangle(rect.xmin(), rect.ymin(), ((rect.xmax() - rect.xmin())/2), ((rect.ymax() - rect.ymin())/2));
        for (Point2D p : range) {
            System.out.println(p.toString());
            p.draw();
        }
        System.out.println("the nearest point to " + origin.toString() + " is: " + testPointSET.nearest(origin));
        // StdDraw.setPenRadius(0.008);
        // StdDraw.setPenColor(Color.red);
        StdDraw.setPenColor(Color.YELLOW);
        origin.draw();
        StdDraw.setPenColor(Color.green);
        new Point2D(0.875, 0.0).draw();
        StdDraw.show();
    }

}
