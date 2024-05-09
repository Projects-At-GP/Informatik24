package vector;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Addition of vectors
    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    // Subtraction of vectors
    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    // Dot product of vectors
    public double dotProduct(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    // Cross product of vectors
    public double crossProduct(Vector2 other) {
        return this.x * other.y - this.y * other.x;
    }

    // Magnitude of vector
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Unit vector
    public Vector2 normalize() {
        double mag = magnitude();
        if (mag == 0) return new Vector2(0, 0);
        return new Vector2(x / mag, y / mag);
    }

    // Perpendicular vector
    public Vector2 perpendicular() {
        // For a 2D vector (x, y), the vector perpendicular to it is (-y, x)
        return new Vector2(-y, x);
    }

    // scale vector by scalar
    public Vector2 scale(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2) return ((Vector2) obj).x == this.x && ((Vector2) obj).y == this.y;
        return false;
    }
}
