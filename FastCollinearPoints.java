/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final ResizingArrayQueue<LineSegment> segments;
    private int numberOfSegments;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) {
            throw new IllegalArgumentException("argument to FastCollinearPoints constructor is null");
        }

        for (int i = 0; i < points.length; i++)
        {
            if (points[i] == null)
            {
                throw new IllegalArgumentException("there is a null object in the array");
            }
        }

        for (int i = 0; i < points.length; i++)
        {
            for (int j = i + 1; j < points.length; j++)
            {
                if (points[i].compareTo(points[j]) == 0)
                {
                    throw new IllegalArgumentException("there is a pair of repeating point in the array");
                }
            }
        }

        segments = new ResizingArrayQueue<LineSegment>();
        numberOfSegments = 0;

        Point[] pointsCopy = copyArray(points);
        for (int i = 0; i < points.length; i++)
        {
            Arrays.sort(pointsCopy, points[i].slopeOrder());
            for (int j = 1; j < pointsCopy.length; j++)
            {
                ResizingArrayQueue<Point> candidates = new ResizingArrayQueue<Point>();
                double slopeFirst = pointsCopy[j].slopeTo(points[i]);
                int n = 0;
                int k = j;
                while (k < pointsCopy.length && slopeFirst == pointsCopy[k].slopeTo(points[i]))
                {
                    candidates.enqueue(pointsCopy[k]);
                    n++;
                    k++;
                }
                if (n >= 3)
                {
                    candidates.enqueue(points[i]);
                    Point[] candidatesArr = candidates(candidates, n + 1);
                    Arrays.sort(candidatesArr);
                    LineSegment newSegment = new LineSegment(candidatesArr[0], candidatesArr[n]);
                    boolean duplicate = false;
                    for (LineSegment segment:
                            segments) {
                        if (segment.toString().equals(newSegment.toString()))
                        {
                            duplicate = true;
                            j = k - 1;
                            break;
                        }
                    }
                    if (duplicate) continue;
                    segments.enqueue(newSegment);
                    numberOfSegments++;
                    j = k - 1;
                }
            }
        }

    }
    private Point[] copyArray(Point[] arr)
    {
        Point[] copy = new Point[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            copy[i] = arr[i];
        }
        return copy;
    }
    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }
    public LineSegment[] segments()                // the line segments
    {
        int i = 0;
        LineSegment[] result = new LineSegment[numberOfSegments];
        for (LineSegment segment:
                segments) {
            result[i++] = segment;
        }
        return result;
    }
    private Point[] candidates(ResizingArrayQueue<Point> arr, int number)
    {
        int i = 0;
        Point[] result = new Point[number];
        for (Point point:
                arr) {
            result[i++] = point;
        }
        return result;
    }

    public static void main(String[] args) {

        Point[] points = new Point[10];
        points[0] = new Point(5000, 7000);
        points[1] = new Point(0, 2000);
        points[2] = new Point(5000, 3000);
        points[3] = new Point(3000, 5000);
        points[4] = new Point(9000, 3000);
        points[5] = new Point(6000, 0);
        points[6] = new Point(9000, 9000);
        points[7] = new Point(8000, 5000);
        points[8] = new Point(7000, 9000);
        points[9] = new Point(2000, 4000);
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.008);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(StdDraw.BLACK);
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
