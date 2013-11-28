package com.arcasolutions.graphics;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import java.io.Serializable;

public class Polygon extends Path implements Serializable {

    // private final static String tag = "Polygon";

    /**
     *
     */
    private static final long serialVersionUID = 9120513210638215777L;

    /**
     * Bounds of the polygon
     */
    RectF bounds;

    /**
     * Total number of points
     */
    int npoints = 0;

    /**
     * Array of x coordinate
     */
    int[] xpoints;

    /**
     * Array of y coordinates
     */
    int[] ypoints;

    /**
     * Creates an empty polygon with 16 points
     */
    public Polygon() {
        this(16);
    }

    /**
     * Creates an empty polygon expecting a given number of points
     *
     * @param numPoints
     *            the total number of points in the Polygon
     */
    public Polygon(int numPoints) {
        super();
        xpoints = new int[numPoints];
        ypoints = new int[numPoints];
    }

    /**
     * Constructs and initializes a Polygon from the specified parameters
     *
     * @param xpoints
     *            an array of x coordinates
     * @param ypoints
     *            an array of y coordinates
     * @param npoints
     *            the total number of points in the Polygon
     */
    public Polygon(int[] xpoints, int[] ypoints, int npoints) {
        super();
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
        moveTo(xpoints[0], ypoints[0]);
        for (int p = 1; p < npoints; p++) {
            lineTo(xpoints[p], ypoints[p]);
        }
        close();
    }

    public void setNumberOfPoints(int numPoints) {
        npoints = numPoints;
    }

    /**
     * Appends the specified coordinates to this Polygon. Remember to close the
     * polygon after adding all the points.
     */
    public void addPoint(int x, int y) {
        // Log.d(tag, "addPoint - x: " + x + " y: " + y + " num: " + npoints);
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        if (npoints > 0) {
            lineTo(x, y);
        } else {
            moveTo(x, y);
        }
        npoints++;
    }

    /**
     * Gets the bounding box of this Polygon
     */
    public RectF getBounds() {
        return bounds;
    }

    /**
     * Determines whether the specified Point is inside this Polygon
     *
     * @param p
     *            the specified Point to be tested
     * @return true if the Polygon contains the Point; false otherwise
     */
    public boolean contains(Point p) {
        if (bounds != null) {
            return bounds.contains(p.x, p.y);
        } else {
            return false;
        }
    }

    public boolean inside(Point p) {
        if (bounds != null) {
            float x1,x2;
            int crossings = 0;
            float px = p.x;
            float py = p.y;

      /* Iterate through each line */
            for ( int i = 0; i < npoints; i++ ){

              /* This is done to ensure that we get the same result when
                 the line goes from left to right and right to left */
                if ( xpoints[i] < xpoints[ (i+1)%npoints ] ){
                    x1 = xpoints[i];
                    x2 = xpoints[(i+1)%npoints];
                } else {
                    x1 = xpoints[(i+1)%npoints];
                    x2 = xpoints[i];
                }

              /* First check if the ray is possible to cross the line */
                if ( px > x1 && px <= x2 && ( py < ypoints[i] || py <= ypoints[(i+1)%npoints] ) ) {
                    final float eps = 0.000001f;

                      /* Calculate the equation of the line */
                    float dx = xpoints[(i+1)%npoints] - xpoints[i];
                    float dy = ypoints[(i+1)%npoints] - ypoints[i];
                    float k;

                    if ( Math.abs(dx) < eps ){
                        k = Float.POSITIVE_INFINITY;   // math.h
                    } else {
                        k = dy/dx;
                    }

                    float m = ypoints[i] - k * xpoints[i];

                      /* Find if the ray crosses the line */
                    float y2 = k * px + m;
                    if ( py <= y2 ){
                        crossings++;
                    }
                }
            }

            return ( crossings % 2 == 1);
        } else {
            return false;
        }
    }

    /**
     * Determines whether the specified coordinates are inside this Polygon
     *
     * @param x
     *            the specified x coordinate to be tested
     * @param y
     *            the specified y coordinate to be tested
     * @return true if this Polygon contains the specified coordinates, (x, y);
     *         false otherwise
     */
    public boolean contains(int x, int y) {
        if (bounds != null) {
            return bounds.contains(x, y);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon entirely contains the specified set
     * of rectangular coordinates
     *
     * @param x
     *            the x coordinate of the top-left corner of the specified set
     *            of rectangular coordinates
     * @param y
     *            the y coordinate of the top-left corner of the specified set
     *            of rectangular coordinates
     * @param w
     *            the width of the set of rectangular coordinates
     * @param h
     *            the height of the set of rectangular coordinates
     * @return
     */
    public boolean contains(double x, double y, double w, double h) {
        if (bounds != null) {
            float fx = (float) x;
            float fy = (float) y;
            float fw = (float) w;
            float fh = (float) h;
            // not sure if math is correct here
            Path that = new Path();
            // start
            that.moveTo(fx, fy);
            // go right
            that.lineTo(fx + fw, fy);
            // go down
            that.lineTo(fx + fw, fy - fh);
            // go left
            that.lineTo(fx, fy - fh);
            // close
            that.close();
            // bounds holder
            RectF thatBounds = new RectF();
            that.computeBounds(thatBounds, false);
            return bounds.contains(thatBounds);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon entirely contains the specified
     * Rectangle
     *
     * @param r
     *            the specified RectF
     * @return true if this Polygon entirely contains the specified RectF; false
     *         otherwise.
     */
    public boolean contains(RectF r) {
        if (bounds != null) {
            return bounds.contains(r);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon intersects the interior of a
     * specified set of rectangular coordinates
     *
     * @param x
     *            the x coordinate of the specified rectangular shape's top-left
     *            corner
     * @param y
     *            the y coordinate of the specified rectangular shape's top-left
     *            corner
     * @param w
     *            the width of the specified rectangular shape
     * @param h
     *            the height of the specified rectangular shape
     * @return
     */
    public boolean intersects(double x, double y, double w, double h) {
        if (bounds != null) {
            float fx = (float) x;
            float fy = (float) y;
            float fw = (float) w;
            float fh = (float) h;
            // not sure if math is correct here
            Path that = new Path();
            // start
            that.moveTo(fx, fy);
            // go right
            that.lineTo(fx + fw, fy);
            // go down
            that.lineTo(fx + fw, fy - fh);
            // go left
            that.lineTo(fx, fy - fh);
            // close
            that.close();
            // bounds holder
            RectF thatBounds = new RectF();
            return RectF.intersects(bounds, thatBounds);
        } else {
            return false;
        }
    }

    /**
     * Tests if the interior of this Polygon intersects the interior of a
     * specified Rectangle
     *
     * @param r
     *            a specified RectF
     * @return true if this Polygon and the interior of the specified RectF
     *         intersect each other; false otherwise
     */
    public boolean intersects(RectF r) {
        if (bounds != null) {
            return RectF.intersects(bounds, r);
        } else {
            return false;
        }
    }

    /**
     * Invalidates or flushes any internally-cached data that depends on the
     * vertex coordinates of this Polygon
     */
    public void invalidate() {
        reset();
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        bounds = null;
    }

    /**
     * Close the current contour and generate the bounds.
     */
    @Override
    public void close() {
        super.close();
        // create bounds for this polygon
        bounds = new RectF();
        this.computeBounds(bounds, false);
    }

}