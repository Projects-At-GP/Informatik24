import java.util.*;
import greenfoot.Color;

public class Renderer {
    Game game;
    Player player;
    private boolean instantiated = false;

    final int cellSize = 64;
    final int mapSize = 16;

    final float playerSize = 0.8F;
    final float tileSize = 1F;

    private int chunkX;
    private int chunkY;

    public List<EntityVisual> visuals = new ArrayList<>();

    private Chunk[][] chunkMap = new Chunk[3][3];

    NPC npc;

    public Renderer(Game game, Player player){
        this.game = game;
        this.player = player;
        // npc = new NPC(this);
        // game.addObject(npc,  800, 100);

        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 3; c++){
                chunkMap[c][i] = new Chunk(c, i);
            }
        }
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
                        game.addObject(chunkMap[c][i].map[y][x][0], 0, 0);
                    }
                }
            }
        }
        chunkX = player.chunkX;
        chunkY = player.chunkY;

        for (EntityVisual e : visuals){
            System.out.println("creating visual");
            game.removeObject(e);
            game.addObject(e, e.screenX, e.screenY);
        }
    }

    Tile checkCollision(){
        for(int i = 0; i < 3; i++){ // loop through x in chunk map
            for(int c = 0; c < 3; c++){ // loop through y in chunk map
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        Tile tile = chunkMap[c][i].map[y][x][0];
                        if(tile.walkable || Math.abs(tile.x - player.x) > 3 || Math.abs(tile.y - player.y) > 3) continue;
                        if (
                                player.x - playerSize / 2   < tile.x + tileSize &&
                                player.x + playerSize / 2   > tile.x            &&
                                player.y - playerSize / 2   < tile.y + tileSize &&
                                player.y + playerSize / 2   > tile.y
                        ) {
                            System.out.printf("Player Info: x: %f y:%f Collision Info: x:%d y:%d id:%d walkable:%b\n",player.x, player.y, tile.x, tile.y, tile.id, tile.walkable);
                            return tile;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * Method to render the environment. Manipulates location of tiles for every position in a grid of 3x3 chunks
     */
    void render(){
        game.getBackground().setColor(Color.GREEN);
        game.getBackground().drawRect(800-(int) (32*playerSize), 450-(int) (32*playerSize), (int) (64*playerSize), (int) (64*playerSize));
        int dx = player.chunkX - chunkX;
        int dy = player.chunkY - chunkY;
        if((dx != 0 || dy != 0) && instantiated){
            System.out.printf("Player moved to another chunk! Direction x %d, y %d\n", dx, dy);
            Chunk[][] tmpChunkMap = new Chunk[3][3];
            System.out.printf("Richtung x:%d\n", dx);
            if(dx == 1){
                System.out.println("nach rechts gegangen");

                tmpChunkMap[0][2] = new Chunk(0,player.chunkX+1);
                tmpChunkMap[1][2] = new Chunk(1,player.chunkX+1);
                tmpChunkMap[2][2] = new Chunk(2,player.chunkX+1);

                tmpChunkMap[0][1] = chunkMap[0][2];
                tmpChunkMap[1][1] = chunkMap[1][2];
                tmpChunkMap[2][1] = chunkMap[2][2];

                tmpChunkMap[0][0] = chunkMap[0][1];
                tmpChunkMap[1][0] = chunkMap[1][1];
                tmpChunkMap[2][0] = chunkMap[2][1];
            } else if (dx == -1) {
                System.out.println("nach links gegangen");

                tmpChunkMap[0][0] = new Chunk(0,player.chunkX-1);
                tmpChunkMap[1][0] = new Chunk(1,player.chunkX-1);
                tmpChunkMap[2][0] = new Chunk(2,player.chunkX-1);

                tmpChunkMap[0][1] = chunkMap[0][0];
                tmpChunkMap[1][1] = chunkMap[1][0];
                tmpChunkMap[2][1] = chunkMap[2][0];

                tmpChunkMap[0][2] = chunkMap[0][1];
                tmpChunkMap[1][2] = chunkMap[1][1];
                tmpChunkMap[2][2] = chunkMap[2][1];
            } else if (dy == 1) {
                System.out.println("nach unten gegangen");

                tmpChunkMap[2][0] = new Chunk(player.chunkY+1,0);
                tmpChunkMap[2][1] = new Chunk(player.chunkY+1,1);
                tmpChunkMap[2][2] = new Chunk(player.chunkY+1,2);

                tmpChunkMap[1][0] = chunkMap[2][0];
                tmpChunkMap[1][1] = chunkMap[2][1];
                tmpChunkMap[1][2] = chunkMap[2][2];

                tmpChunkMap[0][0] = chunkMap[1][0];
                tmpChunkMap[0][1] = chunkMap[1][1];
                tmpChunkMap[0][2] = chunkMap[1][2];
            } else if (dy == -1) {
                System.out.println("nach oben gegangen");

                tmpChunkMap[0][0] = new Chunk(player.chunkY-1,0);
                tmpChunkMap[0][1] = new Chunk(player.chunkY-1,1);
                tmpChunkMap[0][2] = new Chunk(player.chunkY-1,2);

                tmpChunkMap[1][0] = chunkMap[0][0];
                tmpChunkMap[1][1] = chunkMap[0][1];
                tmpChunkMap[1][2] = chunkMap[0][2];

                tmpChunkMap[2][0] = chunkMap[1][0];
                tmpChunkMap[2][1] = chunkMap[1][1];
                tmpChunkMap[2][2] = chunkMap[1][2];
            }

            chunkMap = tmpChunkMap;
            prepare();
        }
        instantiated = true;
        chunkX = player.chunkX;
        chunkY = player.chunkY;
        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 3; c++){
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        int ScreenX = (i * cellSize * 16) + (x * cellSize + cellSize / 2) - (4 * cellSize) - (int) ((player.xInChunk) * cellSize);
                        int ScreenY = (c * cellSize * 16) + (y * cellSize) - (9 * cellSize) - (int) ((player.yInChunk) * cellSize);
                        chunkMap[c][i].map[y][x][0].setLocation(ScreenX, ScreenY);
                    }
                }
            }
        }
        for (EntityVisual e : visuals){
            if(e.isStatic) continue;
            int ScreenX = (int) ((e.x * cellSize) - ((player.xInChunk) * cellSize));
            int ScreenY = (int) ((e.y * cellSize) - ((player.yInChunk) * cellSize));

            e.setLocation(ScreenX, ScreenY);
        }
    }

}
