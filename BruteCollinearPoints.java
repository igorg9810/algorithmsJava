/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.ResizingArrayQueue;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final ResizingArrayQueue<LineSegment> segments;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null) {
            throw new IllegalArgumentException("argument to BruteCollinearPoints constructor is null");
        }
        segments = new ResizingArrayQueue<LineSegment>();
        numberOfSegments = 0;

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

        for (int i = 0; i < points.length - 3; i++)
        {
            for (int j = i + 1; j < points.length - 2; j++)
            {
                for (int k = j + 1; k < points.length - 1; k++)
                {
                    for (int r = k + 1; r < points.length; r++)
                    {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);
                        double slope3 = points[i].slopeTo(points[r]);
                        if (slope1 == slope2 && slope1 == slope3)
                        {
                            Point[] quartet = new Point[4];
                            quartet[0] = points[i];
                            quartet[1] = points[j];
                            quartet[2] = points[k];
                            quartet[3] = points[r];
                            Arrays.sort(quartet);
                            LineSegment newSegment = new LineSegment(quartet[0], quartet[3]);
                            boolean duplicate = false;
                            for (LineSegment segment:
                                    segments) {
                                LineSegment tempSegment = segment;
                                if (tempSegment.toString().equals(newSegment.toString()))
                                {
                                    duplicate = true;
                                    break;
                                }
                            }
                            if (duplicate) continue;
                            segments.enqueue(newSegment);
                            numberOfSegments++;
                        }
                    }
                }
            }
        }
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
}
