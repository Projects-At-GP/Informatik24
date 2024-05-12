package Redfoot;

import vector.Vector2;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;

public class Chunk {
    private static final Logger logger;

    static {
        logger = Logger.getLogger("Chunk");
    }

    public Tile[][][] map = new Tile[16][16][2];
    public final Vector2 pos;

    private final String chunkPrefix = (new File("./src/")).exists() ? "./src/" : "./";
    private final String defaultChunk = chunkPrefix + "mapdata/";
    private final Game game;
    private final String world;

    public Chunk(int x, int y, Game game, String world) {
        this.pos = new Vector2(x, y);
        this.world = world;
        this.game = game;
        logger.info(String.format("Reading data: %d-%d", x, y));
        readChunk(x + "-" + y);
    }

    /**
     * Reads chunkdata from a file
     *
     * @param chunkID accepts String in format "x-y"
     */
    private Chunk readChunk(String chunkID) {
        try {
            File file = new File(this.chunkPrefix + "mapdata/" + world + "/chunk-" + chunkID + ".dat");
            if (!file.exists()) {
                file = new File(this.defaultChunk + world + "/default.dat");
            }
            Scanner reader = new Scanner(file);
            int counter = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] string = data.replaceAll(" ", "").split(",");
                for (int i = 0; i < string.length; i++) {
                    int id = Integer.parseInt(string[i]);
                    //System.out.println(id);
                    Vector2 tilePos = new Vector2((this.pos.x * 16) + i, (this.pos.y * 16) + counter);
                    map[counter][i][0] = new Tile(id, tilePos, this.game, this);
                }
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
