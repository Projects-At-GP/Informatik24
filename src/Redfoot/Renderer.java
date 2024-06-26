package Redfoot;

import Redfoot.Enemies.Blob;
import Redfoot.Enemies.Spider;
import dialogue.Text;
import greenfoot.GreenfootImage;
import vector.Vector2;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class Renderer {
    private static final Logger logger;

    static {
        logger = Logger.getLogger("Chunk");
    }

    public Game game;  // the game instance
    public Player player;  // the player instance
    private boolean instantiated = false;
    private int instantiateIn = 1;

    final int cellSize = 64;
    final int mapSize = 16;

    private int chunkX;
    private int chunkY;

    private List<BaseEntity> entities = new ArrayList<>();
    private Queue<BaseEntity> ceasedEntities = new LinkedList<>();
    private List<BaseActor> particles = new ArrayList<>();

    private Chunk[][] chunkMap = new Chunk[3][3];  // the loaded chunks
    private final Text text;
    public final UIManager uiManager;
    private BaseActor currentTextSource;
    public String world = "overworld";
    private final String filePrefix = (new File("./src/")).exists() ? "./src/" : "./";
    private Vector2 currOffset = new Vector2(4, 9);

    public Renderer(Game game, Player player) {
        this.game = game;
        this.player = player;

        this.uiManager = new UIManager(this.game);
        this.uiManager.setElement(new UI(this), "dialogueBox", new Vector2(800, 700));
        this.text = new Text(this.game, this.uiManager.getElement("dialogueBox"));

        this.uiManager.setElement(new UI(this), "hotbar", new Vector2(800, 850));
        GreenfootImage img = new GreenfootImage(filePrefix + "images/hotbar.png");
        img.scale(744, 96);
        this.uiManager.getElement("hotbar").setImage(img);
        this.uiManager.enableElement("hotbar");

        this.uiManager.setElement(new UI(this), "selection", new Vector2(476, 850));
        img = new GreenfootImage(filePrefix + "images/selection.png");
        img.scale(76, 76);
        this.uiManager.getElement("selection").setImage(img);
        this.uiManager.enableElement("selection");

        for (int i = 0; i < 3; i++) {
            for (int c = 0; c < 3; c++) {
                chunkMap[i][c] = new Chunk(c, i, game, world);
            }
        }
        prepare();
    }

    public List<BaseEntity> getEntities() {
        return this.entities;
    }

    public void addEntity(BaseEntity entity) {
        this.entities.add(entity);
    }

    public void addParticle(BaseActor particle) {
        this.particles.add(particle);
    }

    public void releaseParticle(BaseActor particle) {
        this.particles.remove(particle);
    }

    public void ceaseEntity(BaseEntity entity) {
        entity.isDead = true;
        entity.pos = new Vector2(-0xDEAD, -0xDEAD);
        this.entities.remove(entity);
        this.ceasedEntities.add(entity);
        if (this.ceasedEntities.size() > 16) this.ceasedEntities.remove();
    }

    public void changeWorld(String world) {
        System.out.println("changing world");
        this.instantiated = false;
        this.instantiateIn = 2;
        this.world = world;
        if(world.equals("dungeon")) this.player.pos = new Vector2(1, 2);
        if(world.equals("overworld")) this.player.pos = new Vector2(3, 3);

        for (BaseEntity entity : entities) {
            if (!entity.world.equals(world)) {
                entity.active = false;
                entity.getImage().setTransparency(0);
            } else {
                entity.getImage().setTransparency(255);
                entity.active = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int c = 0; c < 3; c++) {
                chunkMap[i][c] = new Chunk(c-1, i-1, game, world);
            }
        }
        prepare();

        populateEnemies(world);
    }

    private void populateEnemies(String world){
        if(world.equals("dungeon")){
            this.game.spawnEntity(new Spider(this, new Vector2(13, 2), world));
            this.game.spawnEntity(new Blob(this, new Vector2(23, 6), world));
            this.game.spawnEntity(new Spider(this, new Vector2(26, 13), world));
            this.game.spawnEntity(new Spider(this, new Vector2(21, 15), world));
            this.game.spawnEntity(new Spider(this, new Vector2(10, 25), world));
            this.game.spawnEntity(new Spider(this, new Vector2(25, 17), world));
            this.game.spawnEntity(new Blob(this, new Vector2(32, 14), world));
            this.game.spawnEntity(new Blob(this, new Vector2(44, 7), world));
            this.game.spawnEntity(new Blob(this, new Vector2(45, 5), world));
            this.game.spawnEntity(new Blob(this, new Vector2(42, 2), world));
            this.game.spawnEntity(new Spider(this, new Vector2(32, 20), world));
            this.game.spawnEntity(new Blob(this, new Vector2(32, 26), world));
            this.game.spawnEntity(new Spider(this, new Vector2(9, 31), world));
            this.game.spawnEntity(new Blob(this, new Vector2(20, 32), world));
            this.game.spawnEntity(new Blob(this, new Vector2(43, 41), world));
            this.game.spawnEntity(new Spider(this, new Vector2(22, 43), world));
        }
    }


    /**
     * Method to prepare the environment. Creates new tiles for every position in a grid of 3x3 chunks
     */
    void prepare() {
        game.removeObjects(game.getObjects(Tile.class));
        for (int i = 0; i < 3; i++) { // loop through x in chunk map
            for (int c = 0; c < 3; c++) { // loop through y in chunk map
                for (int x = 0; x < mapSize; x++) {
                    for (int y = 0; y < mapSize; y++) {
                        game.addObject(chunkMap[c][i].map[y][x][0], 0, 0);
                    }
                }
            }
        }
        chunkX = (int) Math.floor(player.pos.x / 16);
        chunkY = (int) Math.floor(player.pos.y / 16);
    }

    public void showText(String text, BaseActor textSource) {
        this.text.showTextBox(text);
        this.currentTextSource = textSource;
    }

    private void renderUI() {
        this.uiManager.update();
    }


    /**
     * Method to render the environment. Manipulates location of tiles for every position in a grid of 3x3 chunks
     */
    public void render() {
        if (this.currentTextSource != null) {
            if (this.currentTextSource.pos.subtract(this.player.pos).magnitude() >= 4) {
                uiManager.disableElement("dialogueBox");
            }
        }
        int dx = player.chunkX - chunkX;
        int dy = player.chunkY - chunkY;
        if ((dx != 0 || dy != 0) && this.instantiated && this.instantiateIn <= 0) {
            logger.info(String.format("Redfoot.Player moved to another chunk! Direction x %d, y %d", dx, dy));
            Chunk[][] tmpChunkMap = chunkMap;
            for (int i = 0; i < 3; i++) {
                for (int c = 0; c < 3; c++) {
                    tmpChunkMap[i][c] = new Chunk((int) tmpChunkMap[i][c].pos.x + dx, (int) tmpChunkMap[i][c].pos.y + dy, game, world);
                }
            }

            chunkMap = tmpChunkMap;
            prepare();
        }
        if(this.instantiateIn > 0) this.instantiateIn--;
        this.instantiated = true;
        chunkX = player.chunkX;
        chunkY = player.chunkY;
        for (int i = 0; i < 3; i++) {
            for (int c = 0; c < 3; c++) {
                for (int x = 0; x < mapSize; x++) {
                    for (int y = 0; y < mapSize; y++) {
                        int ScreenX = (i * cellSize * 16) + (x * cellSize) - ((int) currOffset.x * cellSize) - (int) ((player.xInChunk) * cellSize);
                        int ScreenY = (c * cellSize * 16) + (y * cellSize - cellSize / 2) - ((int) currOffset.y * cellSize) - (int) ((player.yInChunk) * cellSize);
                        if (ScreenX <= -64 || ScreenX >= 1664 || ScreenY <= -64 || ScreenY >= 964) {
                            chunkMap[c][i].map[y][x][0].getImage().setTransparency(0);
                            continue;
                        }
                        chunkMap[c][i].map[y][x][0].setLocation(ScreenX, ScreenY);
                        chunkMap[c][i].map[y][x][0].getImage().setTransparency(255);
                    }
                }
            }
        }
        for (BaseEntity e : entities) {
            if (e.pos == null) continue;
            int ScreenX = (int) ((e.pos.x * cellSize) - ((player.xInChunk) * cellSize) - ((player.chunkX - 1) * 16 * cellSize) - (4.5 * cellSize) + (cellSize / 2));
            int ScreenY = (int) ((e.pos.y * cellSize) - ((player.yInChunk) * cellSize) - ((player.chunkY - 1) * 16 * cellSize) - (9.5 * cellSize));
            if (e.getWorld() == null) {
                game.addObject(e, ScreenX, ScreenY);
                continue;
            }
            if (!e.healthInWorld && e.hp != 0) {
                game.addObject(e.health, ScreenX, ScreenY - 50);
                e.healthInWorld = true;
            }
            e.screenPos = new Vector2(ScreenX, ScreenY);
            e.setLocation(ScreenX, ScreenY);
            e.health.setLocation(ScreenX, ScreenY - 50);
        }

        for (BaseActor p : particles) {
            if (p.pos == null) continue;
            int ScreenX = (int) ((p.pos.x * cellSize) - ((player.xInChunk) * cellSize) - ((player.chunkX - 1) * 16 * cellSize) - (4.5 * cellSize) + (cellSize / 2));
            int ScreenY = (int) ((p.pos.y * cellSize) - ((player.yInChunk) * cellSize) - ((player.chunkY - 1) * 16 * cellSize) - (9.5 * cellSize));
            if (p.getWorld() == null) {
                game.addObject(p, ScreenX, ScreenY);
                continue;
            }
            p.setLocation(ScreenX, ScreenY);
        }

        renderUI();
    }

    public CachedMapData exportToMapData() {
        int dimensions = this.chunkMap.length * this.mapSize;
        Tile[][] mapData = new Tile[dimensions][dimensions];

        int mapI, mapJ, chunkI, chunkJ;

        for (int i = 0; i < dimensions; i++) {
            mapI = i / this.chunkMap.length;
            chunkI = i % this.chunkMap.length;

            for (int j = 0; j < dimensions; j++) {
                mapJ = j / this.chunkMap.length;
                chunkJ = j % this.chunkMap.length;
                mapData[i][j] = this.chunkMap[chunkI][chunkJ].map[mapI][mapJ][0];
            }
        }
        return new CachedMapData(mapData, new Vector2(this.chunkX * this.mapSize, this.chunkY * this.mapSize));
    }

    public static final class CachedMapData {
        public final Tile[][] mapData;
        public final Vector2 topLeftCorner;

        public CachedMapData(Tile[][] mapData, Vector2 topLeftCorner) {
            this.mapData = mapData;
            this.topLeftCorner = topLeftCorner;
        }
    }
}
