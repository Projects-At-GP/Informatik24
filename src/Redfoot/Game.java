package Redfoot;

import greenfoot.*;
import vector.Vector2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class Game extends World {
    private static final Logger logger;

    static {
        Locale.setDefault(Locale.US);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] @%3$s: %5$s %n");
        logger = Logger.getLogger("root");
    }

    public final int tps;  // the ticks per second
    protected float deltaTime = 0;  // time between two successive ticks in seconds
    private long curAct = 0;  // the timestamp of the current act
    private long tick = 0;  // the tick counter
    private int pathfindingTickIndex = -1;  // counter to iterate over every actor to distribute the pathfindingTick ticks even
    private File[] dir;
    private GreenfootSound menuMusic;
    public List<Vector2> openChestList = new ArrayList<>();
    public List<Vector2> openDoorList = new ArrayList<>();

    public Renderer render;
    public ArrayList<Actor> deletionList = new ArrayList<>();
    public ArrayList<Vector2> playerPath = new ArrayList<>();
    private int counter;

    public Game(int tps) {
        super(1600, 900, 1, false);

        File tmpDir = new File("./images/tmp");
        tmpDir.mkdir();

        setDir();
        this.tps = tps;
        Player player = new Player(this, new Vector2(18.5, 20.5));
        this.render = new Renderer(this, player);

        NPC npc = new NPC(this.render, "overworld");
        addObject(npc, 0, 0);
        this.render.addEntity(npc);

        NPC2 npc2 = new NPC2(this.render, "overworld");
        addObject(npc2, 0, 0);
        this.render.addEntity(npc2);

        NPC3 npc3 = new NPC3(this.render, "overworld");
        addObject(npc3, 0, 0);
        this.render.addEntity(npc3);

        Questmaster questmaster = new Questmaster(this.render, "overworld");
        addObject(questmaster, 0, 0);
        this.render.addEntity(questmaster);

        //Weapon sword = new Weapon(this.render, WeaponEnum.SILVERSWORD, this.render.world);
        //sword.pos = new Vector2(21, 21);
        //spawnEntity(sword);

        //Weapon broadsword = new Weapon(this.render, WeaponEnum.BROADSWORD, this.render.world);
        //broadsword.pos = new Vector2(23, 21);
        //spawnEntity(broadsword);

        Weapon coppersword = new Weapon(this.render, WeaponEnum.COPPERSWORD, this.render.world);
        coppersword.pos = new Vector2(21, 21);
        spawnEntity(coppersword);

        this.addObject(player, 800, 450);
        this.setPaintOrder(UI.class, Player.class, BaseActor.class, Item.class, Tile.class);

        menuMusic = new GreenfootSound("./sound/Menu.mp3");
        menuMusic.setVolume(0);
        menuMusic.playLoop();

        Greenfoot.start();
    }

    /**
     * Spawn a new entity to the world
     * @param entity the new entity
     */
    public void spawnEntity(BaseEntity entity) {
        addObject(entity, 0, 0);
        this.render.addEntity(entity);
    }

    public Game() {
        this(30);
    }

    private void setDir() {
        File file = new File("./images/textures/");
        File[] files = file.listFiles();
        this.dir = new File[files.length];
        for (File f : files) {
            int index;
            String[] keys = f.getName().split("-");
            index = Integer.parseInt(keys[0]);
            dir[index] = f;
        }
    }

    public void setImageByID(int id, Tile tile, int imgscale) {
        if (dir == null) return;
        if (id >= 0 && id < dir.length) {
            GreenfootImage img = new GreenfootImage(dir[id].getPath());
            img.scale(imgscale, imgscale);
            tile.setImage(img);
            String[] keys = dir[id].getName().replace(".png", "").split("-");
            if (keys[1].equals("N")) tile.hasCollider = true;
        }
    }

    /**
     * Does multiple actions successively:
     * <ol>
     *     <li>delete ceased actors</li>
     *     <li>update our lovely delta time</li>
     *     <li>sleep based on delta time to keep our TPS stable</li>
     *     <li>finish initialization (awake & start) of actors if they need to</li>
     *     <li>tick priorityTick, blockTick, entityTick and pathfindingTick in their respective intervals</li>
     *     <li>re-render the game</li>
     * </ol>
     */
    @Override
    public void act() {
        for (Actor actor : deletionList) {
            this.removeObject(actor);
        }
        Greenfoot.setSpeed(100);
        updateDeltaTime();
        double secondsUntilNextTick = (1d / this.tps) - (this.deltaTime / 1000d);
        try {
            Thread.sleep((long) (secondsUntilNextTick * 1000));
        } catch (IllegalArgumentException e) {
            logger.warning(String.format("Lagging behind! Should already have ticked %s ms ago!", (int) Math.abs(secondsUntilNextTick * 1000)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (BaseActor actor : this.getObjects(BaseActor.class)) {
            if (!actor.started) {
                actor.awake();
                actor.start();
                actor.started = true;
            }
        }

        State state1 = new State(this.tick, 1, this.deltaTime, this);
        State state2 = new State(this.tick, 2, this.deltaTime, this);
        State state3 = new State(this.tick, 3, this.deltaTime, this);

        this.getObjects(BaseActor.class).forEach((a) -> a.priorityTick(state1));
        handleCollision();
        if (this.tick % 3 == 0) this.getObjects(BaseActor.class).forEach((a) -> a.blockTick(state3));
        if (this.tick % 2 == 0) this.getObjects(BaseActor.class).forEach((a) -> a.entityTick(state2));
        if (this.tick % 600 == 0) writeStatData();

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

    private void writeStatData(){
        // Write Data to file
        String filePath = "./statisticData/data" + counter;
        try {
            FileWriter writer = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            StringBuilder line = new StringBuilder();
            for (Vector2 pos : playerPath){
                line.append(pos.x).append(",").append(pos.y).append(";");
            }
            bufferedWriter.write(line.toString());
            bufferedWriter.close();
            counter++;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Reset playerPath
    }

    private void handleCollision() {
        List<BaseEntity> actorList = new ArrayList<>(this.getObjects(BaseEntity.class));
        actorList.removeIf(actor -> !actor.hasCollider);
        List<BaseActor> otherList = new ArrayList<>(this.getObjects(BaseActor.class));
        otherList.removeIf(actor -> !actor.hasCollider);

        for (BaseEntity actor : actorList) {
            actor.collided = false;
            for (BaseActor other : otherList) {
                if (actor == other || !actor.hasCollider || !other.hasCollider) continue;
                if (actor.pos == null || other.pos == null) continue;
                Vector2 mtv = CollisionDetection.checkCollision(actor, other);
                if (mtv != null) {
                    actor.collided = true;
                    actor.onCollision(other, mtv);
                    break;
                }
            }
        }
    }

    /**
     * update our lovely delta time
     */
    private void updateDeltaTime() {
        long lastAct = this.curAct;
        this.curAct = System.currentTimeMillis();
        this.deltaTime = (float) (this.curAct - lastAct) / 1000;
        // edge case: initial start
        if (this.deltaTime < 0) updateDeltaTime();
        if (this.deltaTime > 1000000) updateDeltaTime();
    }

    /**
     * Datatype to hold information to be passed into tick-methods
     */
    public static final class State {
        public long tick;  // the tick during which the method is invoked
        public int sinceLastTick;  // ticks since the method was last invoked (e.g. 1 if was called the previous tick)
        public float deltaTime;  // delta time since last game-tick (NOTE: only usable if method gets ticked every tick as deltaTime doesn't respect any tick spans)
        public Game game;  // a reference to the master itself

        public State(long tick, int sinceLastTick, float deltaTime, Game game) {
            this.tick = tick;
            this.sinceLastTick = sinceLastTick;
            this.deltaTime = deltaTime;
            this.game = game;
        }

    }
}
