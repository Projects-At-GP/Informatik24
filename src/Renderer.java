import java.io.*;
import java.util.*;

public class Renderer {
    Game game;
    Player player;
    final int cellSize = 64;
    final int mapSize = 16;

    private int chunkX;
    private int chunkY;

    private Chunk[][] chunkMap = new Chunk[3][3];

    public Renderer(Game game, Player player){
        this.game = game;
        this.player = player;

        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 3; c++){
                chunkMap[c][i] = new Chunk(c, i);
            }
        }
        prepare();
    }
    /**
     * Method to prepare the environment. Creates new tiles for every position in a grid of 3x3 chunks
     */
    void prepare(){
        game.removeObjects(game.getObjects(Tile.class));
        for(int i = 0; i < 3; i++){ // loop through x in chunk map
            for(int c = 0; c < 3; c++){ // loop through y in chunk map
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        game.addObject(chunkMap[c][i].map[y][x], 0, 0);
                    }
                }
            }
        }
        chunkX = player.chunkX;
        chunkY = player.chunkY;
    }


    /**
     * Method to render the environment. Manipulates location of tiles for every position in a grid of 3x3 chunks
     */
    void render(){
        int dx = player.chunkX - chunkX;
        int dy = player.chunkY - chunkY;
        if(dx != 0 || dy != 0){
            System.out.printf("Player moved to another chunk! Direction x %d, y %d\n", dx, dy);
            Chunk[][] tmpChunkMap = new Chunk[3][3];
            switch (dx){
                case 1:
                {
                    tmpChunkMap[0][0] = chunkMap[0][2];
                    tmpChunkMap[1][0] = chunkMap[1][2];
                    tmpChunkMap[2][0] = chunkMap[2][2];

                    tmpChunkMap[0][1] = chunkMap[0][1];
                    tmpChunkMap[1][1] = chunkMap[1][1];
                    tmpChunkMap[2][1] = chunkMap[2][1];

                    tmpChunkMap[0][2] = new Chunk(0, 0);
                    tmpChunkMap[1][2] = new Chunk(0, 0);
                    tmpChunkMap[2][2] = new Chunk(0, 0);

                }
                case -1:
                {
                    tmpChunkMap[0][2] = chunkMap[0][1];
                    tmpChunkMap[1][2] = chunkMap[1][1];
                    tmpChunkMap[2][2] = chunkMap[2][1];

                    tmpChunkMap[0][1] = chunkMap[0][0];
                    tmpChunkMap[1][1] = chunkMap[1][0];
                    tmpChunkMap[2][1] = chunkMap[2][0];

                    tmpChunkMap[0][0] = new Chunk(0, 0);
                    tmpChunkMap[1][0] = new Chunk(0, 0);
                    tmpChunkMap[2][0] = new Chunk(0, 0);

                }
            }
        }
        chunkX = player.chunkX;
        chunkY = player.chunkY;
        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 3; c++){
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        int ScreenX = (i * cellSize * 16) + (x * cellSize + cellSize / 2) - (4 * cellSize) - (int) ((player.xInChunk) * cellSize);
                        int ScreenY = (c * cellSize * 16) + (y * cellSize + cellSize / 2) - (7 * cellSize) - (int) ((player.yInChunk) * cellSize);
                        chunkMap[c][i].map[y][x].setLocation(ScreenX, ScreenY);
                    }
                }
            }
        }
    }

}
