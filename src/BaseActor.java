import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.World;

import java.util.logging.Logger;


public class BaseActor extends Actor {
    protected Logger logger;
    protected int deltaTime = 0;  // in milliseconds
    private long curAct = 0;

    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
        this.logger.info(String.format("got added to the world at %d/%d", this.getX(), this.getY()));
        updateDeltaTime();
    }

    @Override
    public void act() {
        updateDeltaTime();
    }

    private void updateDeltaTime() {
        long lastAct = this.curAct;
        this.curAct = System.currentTimeMillis();
        this.deltaTime = (int) (this.curAct - lastAct);
    }

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
}
