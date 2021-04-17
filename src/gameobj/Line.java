package gameobj;

import utils.GameKernel;
import utils.Global;

import java.awt.*;

public class Line implements GameKernel.PaintInterface, GameKernel.UpdateInterface {
    private Point point1;
    private Point point2;
    public Global.Direction getHorizontal(){
        if (point1.x<point2.x){
            return Global.Direction.RIGHT;
        }
        else if (point1.x>point2.x){
            return Global.Direction.LEFT;
        }else {
            return Global.Direction.NO_DIR;
        }
    }
    public Global.Direction getVertical(){
        if (point1.y>point2.y){
            return Global.Direction.UP;
        }
        else if (point1.y<point2.y){
            return Global.Direction.DOWN;
        }
        else {
            return Global.Direction.NO_DIR;
        }

    }


    public Line(int x1, int y1, int x2, int y2) {
        this.point1 = new Point(x1, y1);
        this.point2 = new Point(x2, y2);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine((int) this.point1.x, (int) this.point1.y, (int) this.point2.x, (int) this.point2.y);
    }

    @Override
    public void update() {

    }

    public boolean collision(GameObject obj) {
        Line line1 = new Line(obj.left(), obj.top(), obj.right(), obj.bottom());
        Line line2 = new Line(obj.left(), obj.bottom(), obj.right(), obj.top());
        if (getNode(line1) == null && getNode(line2) == null) {
            return false;
        }
        return true;
    }

    public Point getNode(Line line) {
        if ((slope() == line.slope()) && difference() != line.difference()) {
            return null;
        }
        double x = (line.difference() - difference()) / (slope() - line.slope());
        double y = x * slope() + difference();
        Point ans = new Point(x, y);
        if (range(ans) && line.range(ans)) {
            return ans;
        }
        return null;
    }
    public double distance(){
        return point1.distance(point2);
    }
    public Point getNum(int num,boolean isX){
        if (slope()==0){
            return new Point(num,point2.y);
        }else if (slope()==Double.POSITIVE_INFINITY||slope()==Double.NEGATIVE_INFINITY){
            return new Point(point2.x,num);

        }
        if (isX){
            return new Point(num,num*slope()+difference());
        }
        return new Point((num-difference())/slope(),num);
    }

    public double slope() {
        return (this.point2.y - this.point1.y) / (this.point2.x - this.point1.x);
    }

    public double difference() {
        return this.point1.y - slope() * this.point1.x;
    }

    public boolean range(Point point) {
        boolean xIsCorrect = (this.point1.x < point.x && point.x < this.point2.x) || (this.point1.x > point.x && point.x > this.point2.x);
        boolean yIsCorrect = (this.point1.y < point.y && point.y < this.point2.y) || (this.point1.y > point.y && point.y > this.point2.y);
        return xIsCorrect && yIsCorrect;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public static class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public boolean equal(Point point) {
            return point.x == this.x && point.y == this.y;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }
        public double distance(Point point){
            return Math.sqrt(Math.pow(x- point.x,2)+Math.pow(y- point.y,2));
        }
    }
}
