package Redfoot;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
import greenfoot.World;
import greenfoot.Actor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import vector.Vector2;

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
    private int pathfindingTickIndex = -1;
    private File[] dir;
    private GreenfootSound menuMusic;

    public Renderer render;
    public ArrayList<Actor> deletionList = new ArrayList<>();

    public Game(int tps) {
        super(1600, 900, 1, false);
        setDir();
        this.tps = tps;
        Player player = new Player(this, new Vector2(18.5, 20.5));
        this.render = new Renderer(this, player);

        NPC npc = new NPC(this.render);
        addObject(npc, 0, 0);
        this.render.addEntity(npc);

        NPC2 npc2 = new NPC2(this.render);
        addObject(npc2, 0, 0);
        this.render.addEntity(npc2);

        Questmaster questmaster = new Questmaster(this.render);
        addObject(questmaster, 0, 0);
        this.render.addEntity(questmaster);

        Weapon sword = new Weapon(this.render, WeaponEnum.SILVERSWORD);
        sword.pos = new Vector2(21, 21);
        spawnEntity(sword);

        Weapon broadsword = new Weapon(this.render, WeaponEnum.BROADSWORD);
        broadsword.pos = new Vector2(23, 21);
        spawnEntity(broadsword);

        spellItem fireBall = new spellItem(this.render, WeaponEnum.FIREBALL);
        fireBall.pos = new Vector2(25, 21);
        spawnEntity(fireBall);

        Weapon coppersword = new Weapon(this.render, WeaponEnum.COPPERSWORD);
        coppersword.pos = new Vector2(27, 21);
        spawnEntity(coppersword);

        this.addObject(player, 800, 450);
        this.setPaintOrder(UI.class, Player.class, BaseActor.class, Item.class, Tile.class);

        menuMusic = new GreenfootSound("./sound/Menu.mp3");
        menuMusic.setVolume(0);
        menuMusic.playLoop();

        Greenfoot.start();
    }

    public void spawnEntity(BaseEntity enemy){
        addObject(enemy, 0, 0);
        this.render.addEntity(enemy);
    }

    public Game() {
        this(30);
    }

    private void setDir(){
        File file = new File("./images/textures/");
        File[] files = file.listFiles();
        this.dir = new File[files.length];
        for (File f : files){
            int index;
            String[] keys = f.getName().split("-");
            index = Integer.parseInt(keys[0]);
            dir[index] = f;
        }
    }

    public void setImageByID(int id, Tile tile, int imgscale){
        if(dir == null) return;
        if (id >= 0 && id < dir.length){
            GreenfootImage img = new GreenfootImage(dir[id].getPath());
            img.scale(imgscale, imgscale);
            tile.setImage(img);
            String[] keys = dir[id].getName().replace(".png", "").split("-");
            if(keys[1].equals("N")) tile.hasCollider = true;
        }
    }

    @Override
    public void act() {
        for (Actor actor : deletionList){
            this.removeObject(actor);
        }
        Greenfoot.setSpeed(100);
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
        handleCollision();
        if (this.tick % 3 == 0) this.getObjects(BaseActor.class).forEach((a)->a.blockTick(state3));
        if (this.tick % 2 == 0) this.getObjects(BaseActor.class).forEach((a)->a.entityTick(state2));

        List<BaseEntity> pathfindingEntities = this.getObjects(BaseEntity.class);
        int pathfindingTick = (int) Math.ceil((double) this.tps / pathfindingEntities.size());
        if (this.tick % pathfindingTick == 0) {
            State stateP = new State(this.tick, pathfindingEntities.size() * pathfindingTick, this.deltaTime, this);
            this.pathfindingTickIndex = (this.pathfindingTickIndex + 1) % pathfindingEntities.size();
            pathfindingEntities.get(this.pathfindingTickIndex).pathfindingTick(stateP);
        }

        render.render();

        this.tick++;
    }

    private void handleCollision(){
        List<BaseEntity> actorList = new ArrayList<>(this.getObjects(BaseEntity.class));
        actorList.removeIf(actor -> !actor.hasCollider);
        List<BaseActor> otherList = new ArrayList<>(this.getObjects(BaseActor.class));
        otherList.removeIf(actor -> !actor.hasCollider);

        for (BaseEntity actor : actorList){
            actor.collided = false;
            for (BaseActor other : otherList){
                if (actor == other || !actor.hasCollider || !other.hasCollider) continue;
                if(actor.pos == null || other.pos == null) continue;
                Vector2 mtv = CollisionDetection.checkCollision(actor, other);
                if (mtv != null) {
                    actor.collided = true;
                    actor.onCollision(other, mtv);
                    break;
                }
            }
        }
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
