package org.apache.storm.starter.polygon;

import java.lang.Math;

/**
 * Line is defined by starting point and ending point on 2D dimension.<br>
 *
 * @author Roman Kushnarenko (sromku@gmail.com)
 * Extended by Alex Gubbay to include method to calulate minimum distance to a point and to get the length of the line.
 */
public class Line {
    
    private final Point _start;
    private final Point _end;
    private double _a = Double.NaN;
    private double _b = Double.NaN;
    private boolean _vertical = false;

    public Line(Point start, Point end) {
        _start = start;
        _end = end;

        if (_end.x - _start.x != 0) {
            _a = ((_end.y - _start.y) / (_end.x - _start.x));
            _b = _start.y - _a * _start.x;
        } else {
            _vertical = true;
        }
    }

    /**
     * Indicate whereas the point lays on the line.
     *
     * @param point - The point to check
     * @return <code>True</code> if the point lays on the line, otherwise return <code>False</code>
     */
    public boolean isInside(Point point) {
        double maxX = _start.x > _end.x ? _start.x : _end.x;
        double minX = _start.x < _end.x ? _start.x : _end.x;
        double maxY = _start.y > _end.y ? _start.y : _end.y;
        double minY = _start.y < _end.y ? _start.y : _end.y;

        if ((point.x >= minX && point.x <= maxX) && (point.y >= minY && point.y <= maxY)) {
            return true;
        }
        return false;
    }
    
    public double distanceToLineSegment(Point point){
        
        //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
        //calculate distance to segment squared.
        //Then take the square root.
        
        //Get length of line segment.
        double length = pointToPointDistance(_start,_end);
        
        //Line segment is actually a point, return distance.
        if(length == 0){
           return pointToPointDistance(point,_start);
        }
        
        //Project point onto the line.
        double t = ((point.x - _start.x) * (_end.x - _start.x) + (point.y - _start.y) * (_end.y - _start.y))/length;
        
        //Clam from 0 to 1.
        t = Math.max(0,Math.min(1,t));
        Point temp = new Point(_start.x + t * (_end.x - _start.x), _start.y + t * (_end.y - _start.y));
        double distanceSquared = pointToPointDistance(point,temp);
        return Math.sqrt(distanceSquared);
            
        
    }
    
    public double getLength(){
        return Math.hypot(_start.x-_end.x, _start.y-_end.y);
    }
    
    private double pointToPointDistance(Point v, Point w){
       double xDist = v.x - w.x;
       double yDist = v.y - w.y;
       double result = Math.pow(xDist,2) + Math.pow(yDist,2);
      return result;
        
    }

    /**
     * Indicate whereas the line is vertical. <br>
     * For example, line like x=1 is vertical, in other words parallel to axis Y. <br>
     * In this case the A is (+/-)infinite.
     *
     * @return <code>True</code> if the line is vertical, otherwise return <code>False</code>
     */
    public boolean isVertical() {
        return _vertical;
    }

    /**
     * y = <b>A</b>x + B
     *
     * @return The <b>A</b>
     */
    public double getA() {
        return _a;
    }

    /**
     * y = Ax + <b>B</b>
     *
     * @return The <b>B</b>
     */
    public double getB() {
        return _b;
    }

    /**
     * Get start point
     *
     * @return The start point
     */
    public Point getStart() {
        return _start;
    }

    /**
     * Get end point
     *
     * @return The end point
     */
    public Point getEnd() {
        return _end;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", _start.toString(), _end.toString());
    }
}
