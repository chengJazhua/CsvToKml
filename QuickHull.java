
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;
 
public class QuickHull
{
    public static ArrayList<Point2D.Double> quickHull(ArrayList<Point2D.Double> points)
    {
        ArrayList<Point2D.Double> convexHull = new ArrayList<Point2D.Double>();
        if (points.size() < 3)
            return (ArrayList) points.clone();
 
        int minPoint = -1, maxPoint = -1;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).x < minX)
            {
                minX = points.get(i).x;
                minPoint = i;
            }
            if (points.get(i).x > maxX)
            {
                maxX = points.get(i).x;
                maxPoint = i;
            }
        }
        Point2D.Double A = points.get(minPoint);
        Point2D.Double B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);
 
        ArrayList<Point2D.Double> leftSet = new ArrayList<Point2D.Double>();
        ArrayList<Point2D.Double> rightSet = new ArrayList<Point2D.Double>();
 
        for (int i = 0; i < points.size(); i++)
        {
            Point2D.Double p = points.get(i);
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);
 
        return convexHull;
    }
 
    public static double distance(Point2D.Double A, Point2D.Double B, Point2D.Double C)
    {
        double ABx = B.x - A.x;
        double ABy = B.y - A.y;
        double num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
        if (num < 0)
            num = -num;
        return num;
    }
 
    public static void hullSet(Point2D.Double A, Point2D.Double B, ArrayList<Point2D.Double> set,
            ArrayList<Point2D.Double> hull)
    {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1)
        {
            Point2D.Double p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        double dist = Double.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++)
        {
            Point2D.Double p = set.get(i);
            double distance = distance(A, B, p);
            if (distance > dist)
            {
                dist = distance;
                furthestPoint = i;
            }
        }
        Point2D.Double P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);
 
        // Determine who's to the left of AP
        ArrayList<Point2D.Double> leftSetAP = new ArrayList<Point2D.Double>();
        for (int i = 0; i < set.size(); i++)
        {
            Point2D.Double M = set.get(i);
            if (pointLocation(A, P, M) == 1)
            {
                leftSetAP.add(M);
            }
        }
 
        // Determine who's to the left of PB
        ArrayList<Point2D.Double> leftSetPB = new ArrayList<Point2D.Double>();
        for (int i = 0; i < set.size(); i++)
        {
            Point2D.Double M = set.get(i);
            if (pointLocation(P, B, M) == 1)
            {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);
 
    }
 
    public static int pointLocation(Point2D.Double A, Point2D.Double B, Point2D.Double P)
    {
        double cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }
}