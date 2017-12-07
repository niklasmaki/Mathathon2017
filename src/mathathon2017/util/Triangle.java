package mathathon2017.util;

public class Triangle {

    public Coordinate a = new Coordinate();
    public Coordinate b = new Coordinate();
    public Coordinate c = new Coordinate();
    public int red, green, blue;
    public int opacity;

    public void setColor(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public String toString() {
        return "{\"a\":"+a+","+
                "\"b\":"+b+","+
                "\"c\":"+c+","+
                "\"red\":"+red+","+
                "\"green\":"+green+","+
                "\"blue\":"+blue+","+
                "\"opacity\":"+opacity+"}";
    }

    public Triangle copy() {
        Triangle t = new Triangle();
        t.setColor(red, green, blue);
        t.opacity = opacity;
        t.a = a.copy();
        t.b = b.copy();
        t.c = c.copy();
        return t;

    }
}
