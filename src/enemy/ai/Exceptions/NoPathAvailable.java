package enemy.ai.Exceptions;

import enemy.ai.IntelligenceEnum;
import vector.Vector2;

public class NoPathAvailable extends Exception {
    public NoPathAvailable(Vector2 a, Vector2 b) {
        super(String.format("No path available from %1$,.2f|%2$,.2f to %3$,.2f|%4$,.2f!", a.x, a.y, b.x, b.y));
    }

    public NoPathAvailable(Vector2 a, Vector2 b, IntelligenceEnum intelligence) {
        super(String.format("No path available from %1$,.2f|%2$,.2f to %3$,.2f|%4$,.2f with intelligence %5$s (max range %6$,.1f/%7$,.1f)", a.x, a.y, b.x, b.y, intelligence, intelligence.aggressionRange, intelligence.chasingRange));
    }
}
