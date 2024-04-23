import dialogue.Text;
import greenfoot.Actor;

import java.util.*;

public class Renderer {
    Game game;
    Player player;
    private boolean instantiated = false;

    final int cellSize = 64;
    final int mapSize = 16;

    private int chunkX;
    private int chunkY;

    public List<BaseEntity> entities = new ArrayList<>();

    private Chunk[][] chunkMap = new Chunk[3][3];
    private final Text text;
    private final Actor dialogueBox;
    private BaseActor currentTextSource;

    public Renderer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.dialogueBox = new UI(this);
        this.game.addObject(dialogueBox, 800, 780);
        this.text = new Text(this.game, dialogueBox);

        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 3; c++){
                chunkMap[i][c] = new Chunk(c, i, game);
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
                        game.addObject(chunkMap[c][i].map[y][x][0], 0, 0);
                    }
                }
            }
        }
        chunkX = player.chunkX;
        chunkY = player.chunkY;
    }

    public void showText(String text, BaseActor textSource){
        this.text.showText(text);
        this.currentTextSource = textSource;
        this.dialogueBox.getImage().setTransparency(255);
    }


    /**
     * Method to render the environment. Manipulates location of tiles for every position in a grid of 3x3 chunks
     */
    void render(){
        String stringValue = String.format("X: %.2f \\nY: %.2f", player.pos.x, player.pos.y);
        this.text.showText(stringValue);
        if (this.currentTextSource != null){
            if (this.currentTextSource.pos.subtract(this.player.pos).magnitude() >= 7){
                dialogueBox.getImage().setTransparency(0);
            }
        }
        int dx = player.chunkX - chunkX;
        int dy = player.chunkY - chunkY;
        if((dx != 0 || dy != 0) && instantiated){
            System.out.printf("Player moved to another chunk! Direction x %d, y %d\n", dx, dy);
            Chunk[][] tmpChunkMap = chunkMap;
            System.out.printf("Richtung x:%d\n", dx);
            for(int i = 0; i < 3; i++){
                for(int c = 0; c < 3; c++){
                    tmpChunkMap[i][c] = new Chunk((int) tmpChunkMap[i][c].pos.x+dx, (int) tmpChunkMap[i][c].pos.y+dy, game);
                }
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
                        int ScreenX = (i * cellSize * 16) + (x * cellSize) - (4 * cellSize) - (int) ((player.xInChunk) * cellSize);
                        int ScreenY = (c * cellSize * 16) + (y * cellSize - cellSize / 2) - (9 * cellSize) - (int) ((player.yInChunk) * cellSize);
                        chunkMap[c][i].map[y][x][0].setLocation(ScreenX, ScreenY);
                    }
                }
            }
        }
        for (BaseEntity e : entities){

            int ScreenX = (int) ((e.pos.x * cellSize) - ((player.xInChunk) * cellSize) - ((player.chunkX -1) * 16 * cellSize) - (4 * cellSize) + (cellSize / 2));
            int ScreenY = (int) ((e.pos.y * cellSize) - ((player.yInChunk) * cellSize) - ((player.chunkY -1) * 16 * cellSize) - (9 * cellSize));
            if(e.getWorld() == null){
                game.addObject(e, ScreenX, ScreenY);
                continue;
            }
            e.setLocation(ScreenX, ScreenY);
        }
    }

}
