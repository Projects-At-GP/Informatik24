import greenfoot.World;

import java.util.Locale;
import java.util.logging.Logger;


public class Game extends World {
    private static final Logger logger;

    static {
        Locale.setDefault(Locale.US);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] @%3$s: %5$s %n");
        logger = Logger.getLogger("root");
    }

    private final int tps;
    protected int deltaTime = 0;  // in milliseconds
    private long curAct = 0;

    public Game(int tps) {
        super(1, 1, 1, false);  // TODO
        this.tps = tps;
    }

    public Game() {
        this(20);
    }

    @Override
    public void act() {
        updateDeltaTime();
        double secondsUntilNextTick = (1d / this.tps) - (this.deltaTime / 1000d);
        try {
            Thread.sleep((long) (secondsUntilNextTick * 1000));
        } catch (IllegalArgumentException e) {
            logger.warning(String.format("Lagging behind! Should already have ticked %s seconds ago!", secondsUntilNextTick));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // TODO: tick-logic for actors
    }

    private void updateDeltaTime() {
        long lastAct = this.curAct;
        this.curAct = System.currentTimeMillis();
        this.deltaTime = (int) (this.curAct - lastAct);
        // edge case: initial start
        if (this.deltaTime < 0) updateDeltaTime();
    }

    /**
     * Datatype to hold information to be passed into tick-methods
     */
    public static final class State {
        // the tick during which the method is invoked
        public long tick;

        // ticks since the method was last invoked (e.g. 1 if was called the previous tick)
        public int sinceLastTick;

        // a reference to the master itself
        public Game game;
    }
}