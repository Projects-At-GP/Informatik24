package Redfoot;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.logging.Logger;


public class BaseActor extends Actor {
    protected Logger logger;
    protected Renderer renderer;

    public Vector2 pos;
    public collider col;
    public boolean hasCollider = false;
    public boolean started = false;

    public BaseActor(Renderer renderer){
        this.renderer = renderer;
    }

    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
        this.logger.finest(String.format("got added to the world at %d/%d", this.getX(), this.getY()));
    }

    /**
     * @param other reference to other Redfoot.collider
     * Will be called if a collision occurs
     */
    protected void onCollision(BaseActor other, Vector2 mtv){}

    /**
     * @param keyName the name of the key
     * @return 1 if key is pressed, otherwise 0
     */
    protected int intIsKeyPressed(String keyName) {
        return Greenfoot.isKeyDown(keyName) ? 1 : 0;
    }

    /**
     * @param dx positive right, negative left
     * @param dy positive down, negative up
     */
    public void move(int dx, int dy) {
        this.setLocation(this.getX() + dx, this.getY() + dy);
    }

    /**
     * Will be called once on startup before start.
     */
    protected void awake() {}

    /**
     * Used for entities to initiate.
     * Will be called once on startup.
     */
    protected void start() {}

    // .*Tick()-methods to replace .act()
    /**
     * Ensured to be called every tick.
     * Also called in the first loop.
     */
    protected void priorityTick(Game.State state) {}

    /**
     * Used for blocks to tick.
     * May not be called every tick (e.g. every second tick), but if ticked everything is ticked in the same tick.
     */
    protected void blockTick(Game.State state) {}

    /**
     * Used for entities to tick.
     * May not be called every tick (e.g. every second tick), but if ticked everything is ticked in the same tick.
     */
    protected void entityTick(Game.State state) {}

    /**
     * Used for entities for pathfinding.
     * May not be called in constant intervals; depends on how many enemies want to be ticked.
     */
    protected void pathfindingTick(Game.State state) {}

    @Override
    public String toString() {
        return String.format("Actor with %s Redfoot.collider at position %s", hasCollider? "a" : "no", pos.toString());
    }
}
