package Redfoot;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.logging.Logger;


public class BaseActor extends Actor {
    protected Logger logger;  // logger for logging
    protected Renderer renderer;

    public Vector2 pos;  // the position of the actor in the world
    public collider col;  // an optional collider for collision detection
    public boolean hasCollider = false;  // an indicator whether it supports collision detection or not
    public boolean started = false;  // an indicator whether the actor has been properly initialized or not
    public Vector2 screenPos;  // the position of the actor on the screen

    public BaseActor(Renderer renderer){
        this.renderer = renderer;
    }

    /**
     * Use Greenfoot's API to set the logger for each instance
     * @param world The world the object was added to.
     */
    @Override
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
     * Will be called once on startup before start.
     */
    protected void awake() {}

    /**
     * Used for entities to initiate.
     * Will be called once on startup.
     */
    protected void start() {}


    ///////// .*Tick()-methods to replace .act()
    /**
     * Ensured to be called every tick.
     * Also called in the first loop.
     */
    protected void priorityTick(Game.State state) {}

    /**
     * Used for blocks to tick.
     * Will be called every third tick. If ticked everything is ticked in the same tick.
     */
    protected void blockTick(Game.State state) {}

    /**
     * Used for entities to tick.
     * Will be called every second tick. If ticked everything is ticked in the same tick.
     */
    protected void entityTick(Game.State state) {}

    /**
     * Used for entities for pathfinding.
     * May not be called in constant intervals; depends on how many actors are present -> want to be ticked.
     * <b>Only one actor will be ticked at a time!</b>
     */
    protected void pathfindingTick(Game.State state) {}

    @Override
    public String toString() {
        return String.format("Actor with %s Redfoot.collider at position %s", hasCollider? "a" : "no", pos.toString());
    }
}
