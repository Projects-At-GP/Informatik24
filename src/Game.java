import greenfoot.Greenfoot;
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
    protected float deltaTime = 0;  // in seconds
    private long curAct = 0;
    private long tick = 0;

    public Renderer render;

    public Game(int tps) {
        super(1600, 900, 1, false);
        this.tps = tps;
        Player player = new Player(this, 18.5F, 20.5F);
        this.render = new Renderer(this, player);
        NPC npc = new NPC(this.render);
        addObject(npc, 0, 0);
        this.render.entities.add(npc);

        this.addObject(player, 800, 450);
        this.setPaintOrder(BaseActor.class, Tile.class);
        Greenfoot.start();
    }

    public Game() {
        this(30);
    }

    @Override
    public void act() {
        updateDeltaTime();
        double secondsUntilNextTick = (1d / this.tps) - (this.deltaTime / 1000d);
        try {
            Thread.sleep((long) (secondsUntilNextTick * 1000));
        } catch (IllegalArgumentException e) {
            logger.warning(String.format("Lagging behind! Should already have ticked %s ms ago!", (int) Math.abs(secondsUntilNextTick*1000)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (BaseActor actor : this.getObjects(BaseActor.class)){
            if(!actor.started){
                actor.awake();
                actor.start();
                actor.started = true;
            }
        }

        State state1 = new State(this.tick, 1, this.deltaTime, this);
        State state2 = new State(this.tick, 2, this.deltaTime, this);
        State state3 = new State(this.tick, 3, this.deltaTime, this);

        this.getObjects(BaseActor.class).forEach((a)->a.priorityTick(state1));
        if (this.tick % 3 == 0) this.getObjects(BaseActor.class).forEach((a)->a.blockTick(state3));
        if (this.tick % 2 == 0) this.getObjects(BaseActor.class).forEach((a)->a.entityTick(state2));

        render.render();

        this.tick++;
    }

    private void updateDeltaTime() {
        long lastAct = this.curAct;
        this.curAct = System.currentTimeMillis();
        this.deltaTime = (float) (this.curAct - lastAct) / 1000;
        //System.out.printf("lastAct: %d, curAct: %d, dt: %d\n", lastAct, this.curAct, this.deltaTime);
        // edge case: initial start
        if (this.deltaTime < 0) updateDeltaTime();
        if(this.deltaTime > 1000000) updateDeltaTime();
    }

    /**
     * Datatype to hold information to be passed into tick-methods
     */
    public static final class State {
        public State(long tick, int sinceLastTick, float deltaTime, Game game){
            this.tick = tick;
            this.sinceLastTick = sinceLastTick;
            this.deltaTime = deltaTime;
            this.game = game;
        }


        // the tick during which the method is invoked
        public long tick;

        // ticks since the method was last invoked (e.g. 1 if was called the previous tick)
        public int sinceLastTick;

        // delta time since last game-tick (NOTE: only usable if method gets ticked every tick as deltaTime doesn't respect any tick spans)
        public float deltaTime;  // not recommended to use, use State.tick or State.sinceLastTick instead for consistency

        // a reference to the master itself
        public Game game;
    }
}
