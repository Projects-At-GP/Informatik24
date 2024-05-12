package Redfoot;

import vector.Vector2;

import java.util.ArrayList;

public class collider {
    ArrayList<Vector2> vertices = new ArrayList<>();

    public void square(double scale, double yoffset) {
        this.vertices.clear();
        this.vertices.add(new Vector2(-0.5 * scale, (-0.5 * scale) + yoffset)); // top    left  corner
        this.vertices.add(new Vector2(0.5 * scale, (-0.5 * scale) + yoffset));  // top    right corner
        this.vertices.add(new Vector2(0.5 * scale, (0.5 * scale) + yoffset));   // bottom right corner
        this.vertices.add(new Vector2(-0.5 * scale, (0.5 * scale) + yoffset));  // bottom left  corner
    }

    public void square(double scale) {
        square(scale, 0);
    }

    public void octagon(double scale, double yoffset) {
        this.vertices.clear();
        this.vertices.add(new Vector2(0.2 * scale, (-0.5 * scale) + yoffset));  // (1) NE
        this.vertices.add(new Vector2(0.5 * scale, (-0.2 * scale) + yoffset));  // (2) EN
        this.vertices.add(new Vector2(0.5 * scale, (0.2 * scale) + yoffset));   // (3) ES
        this.vertices.add(new Vector2(0.2 * scale, (0.5 * scale) + yoffset));   // (4) SE
        this.vertices.add(new Vector2(-0.2 * scale, (0.5 * scale) + yoffset));  // (5) SW
        this.vertices.add(new Vector2(-0.5 * scale, (0.2 * scale) + yoffset));  // (6) WS
        this.vertices.add(new Vector2(-0.5 * scale, (-0.2 * scale) + yoffset)); // (7) WN
        this.vertices.add(new Vector2(-0.2 * scale, (-0.5 * scale) + yoffset)); // (8) NW
    }

    public void octagon(double scale) {
        octagon(scale, 0);
    }
}
