package vector;

public class Vector {
    double x;
    double y;
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector vector){
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public double dot(Vector vector){
        return (this.x * vector.x + this.y * vector.y);
    }
}
