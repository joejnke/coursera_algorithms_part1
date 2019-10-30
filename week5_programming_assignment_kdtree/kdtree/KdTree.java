/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    // instance variables
    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        // this.root = new Node();
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls insert() with a null argument...");

        this.root = insert(this.root,
                           new RectHV(0.0, 0.0, 1.0, 1.0),
                           p, true);
        this.size++;
    }

    private Node insert(Node x, RectHV inRect, Point2D point, boolean xOrder) {
        // if the tree is empty and hence root is null
        if (x == null) {
            return new Node(point,
                            inRect, // new RectHV(0.0, 0.0, 1.0, 1.0),
                            null, null); // yet to be handled
        }

        // use x as key using X-order comparator of point2D
        if (xOrder) {
            int cmp = Point2D.X_ORDER.compare(point, x.p);
            /* if the point is equal in the x order then we need to recall insert to this tree again but in y order
            * x  = insert(x, x.rect, point, true);
            * */
            if (cmp < 0) x.lb  = insert(x.lb, new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax()), point, false);
            else x.rt = insert(x.rt, new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax()), point, false);

            return x;
        }

        // use y as key using Y-order comparator of point2D
        else {
            int cmp = Point2D.Y_ORDER.compare(point, x.p);
            /* if the point is equal in the y order then we need to recall insert to this tree again but in x order
             * x  = insert(x, x.rect, point, false);
             * */
            if (cmp < 0) x.lb  = insert(x.lb, new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y()), point, true);
            else x.rt = insert(x.rt,  new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax()), point, true);

            return x;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls contains() with a null argument...");
        return contains(this.root, p, true);
    }

    private boolean contains(Node x, Point2D point, boolean xOrder) {
        // if the tree is empty and hence root is null
        if (x == null) return false; // yet to be handled

        // if the root point is equal to query point
        if (x.p.equals(point)) return true;

        // use x as key using X-order comparator of point2D
        if (xOrder) {
            int cmp = Point2D.X_ORDER.compare(point, x.p);
            // if (cmp == 0) return true;
            if (cmp < 0) return contains(x.lb, point, false);
            else return contains(x.rt, point, false);
        }

        // use y as key using Y-order comparator of point2D
        else {
            int cmp = Point2D.Y_ORDER.compare(point, x.p);
            // if (cmp == 0) return true;
            if (cmp < 0) return contains(x.lb, point, true);
            else return contains(x.rt, point, true);
        }
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) throw new UnsupportedOperationException("calls draw() on an empty tree...");
        draw(root, true);
    }

    private void draw(Node x, boolean xOrder) {
        // draw the point representing the node
        x.p.draw();

        // draw the line that divide the rectangle corresponding to the node
        if (xOrder) {
            // draw vertical line with red color
            StdDraw.setPenColor(Color.red);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());

            // draw left subtree
            if (x.lb != null) draw(x.lb, false);

            // draw right subtree
            if (x.rt != null) draw(x.rt, false);
        }

        else {
            // draw horizontal line with blue color
            StdDraw.setPenColor(Color.blue);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());

            // draw left subtree
            if (x.lb != null) draw(x.lb, true);

            // draw right subtree
            if (x.rt != null) draw(x.rt, true);
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("calls range() with a null argument...");
        return range(this.root, rect);
    }

    private Iterable<Point2D> range(Node x, RectHV rect) {
        ArrayList<Point2D> pointInRect = new ArrayList<Point2D>();
        if (x == null) return pointInRect;

        // if rect intersects the tree rooted at x
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p)) pointInRect.add(x.p);

            // find range in left subtree
            pointInRect.addAll((ArrayList<Point2D>) range(x.lb, rect));
            // find range in right subtree
            pointInRect.addAll((ArrayList<Point2D>) range(x.rt, rect));

            // // if rect intersects with the left subtree of node x
            // if (x.lb != null && rect.intersects(x.lb.rect)) {
            //     pointInRect.addAll((ArrayList<Point2D>) range(x.lb, rect));
            // }
            //
            // // if rect intersects with the right subtree of node x
            // if (x.rt != null && rect.intersects(x.rt.rect)) {
            //     pointInRect.addAll((ArrayList<Point2D>) range(x.rt, rect));
            // }
        }

        return pointInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls nearest() with a null argument...");
        p.theta();
        return nearest(this.root, p);
    }

    private Point2D nearest(Node x, Point2D p) {
        // if the tree is empty
        if (x == null) return null;

        // take the subtree root node as the closest point
        Point2D nearest = x.p;
        if (x.lb == null && x.rt == null) return nearest;

        // if not possible to go left, then go right
        if (x.lb == null || Double.compare(p.distanceSquaredTo(nearest), x.lb.rect.distanceSquaredTo(p)) < 0) {
            Point2D tempNear = nearest(x.rt, p);
            if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                nearest = tempNear;
            }

            return nearest;
        }

        // if not possible to go right, then go left
        if (x.rt == null || Double.compare(p.distanceSquaredTo(nearest), x.rt.rect.distanceSquaredTo(p)) < 0) {
            Point2D tempNear = nearest(x.lb, p);
            if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                nearest = tempNear;
            }

            return nearest;
        }

        // if possible to go either way
        else {
            double thetaLBMax = new Point2D(x.lb.rect.xmax(), x.lb.rect.ymax()).theta(); // polar angle of (Xmax, Ymax) of x.lb.rect
            double thetaRootP = x.p.theta(); // polar angle of the tree-root point (i.e x.p)
            double thetaP = p.theta(); // polar angle of query point

            // check if query point is same side as x.lb or in x.lb, if yes go left then right
            if (x.lb.rect.contains(p) ||
                    (((thetaLBMax >= thetaRootP) && !(thetaP <= thetaRootP)) ||
                            ((thetaLBMax <= thetaRootP) && (thetaP <= thetaRootP)))) {

                // go left
                Point2D tempNear = nearest(x.lb, p);
                if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                    nearest = tempNear;
                }

                // go right
                tempNear = nearest(x.rt, p);
                if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                    nearest = tempNear;
                }

                return nearest;
            }

            // else it's same side as x.rt, hence go right and then left
            else {
                // go right
                Point2D tempNear = nearest(x.rt, p);
                if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                    nearest = tempNear;
                }

                // go left
                tempNear = nearest(x.lb, p);
                if (tempNear != null && Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
                    nearest = tempNear;
                }

                return nearest;
            }
        }


        // // find nearest point in the left subtree
        // if (x.lb != null && !(Double.compare(p.distanceSquaredTo(nearest), x.lb.rect.distanceSquaredTo(p)) < 0)) {
        //     Point2D tempNear = nearest(x.lb, p);
        //     if (Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
        //         nearest = tempNear;
        //     }
        // }
        //
        // // find nearest point in the right subtree
        // if (x.rt != null && !(Double.compare(p.distanceSquaredTo(nearest), x.rt.rect.distanceSquaredTo(p)) < 0)) {
        //     Point2D tempNear = nearest(x.rt, p);
        //     if (Double.compare(p.distanceSquaredTo(tempNear), p.distanceSquaredTo(nearest)) < 0) {
        //         nearest = tempNear;
        //     }
        // }
        //
        // return nearest;
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree testKd = new KdTree();
        Point2D insertP = new Point2D(0.65, 0.8);
        RectHV rangeRect = new RectHV(0.576, 0.786,0.818 ,0.817);
        testKd.insert(insertP);
        System.out.println("set contains (0.5, 0.56): " + testKd.contains(new Point2D(0.5, 0.56)));

        // In in = new In(args[0]);
        // while (in.hasNextLine()) { // && !in.readLine().isEmpty()) {
        //     try {
        //         Point2D toInsert = new Point2D(in.readDouble(), in.readDouble());
        //         testKd.insert(toInsert);
        //         System.out.println(toInsert.toString());
        //     }
        //
        //     catch (NoSuchElementException e) {
        //         System.out.println(e.toString());
        //     }
        //
        // }
        // System.out.println("set size is: " +  testKd.size());

        System.out.println("range( " + rangeRect.toString() + " ): " +  testKd.range(rangeRect));


    }
}
