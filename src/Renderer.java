public class Renderer {
    Game game;
    Player player;
    final int cellSize = 32;
    final int mapSize = 16;

    private float playerX;
    private float playerY;

    private Chunk[][] chunkMap = new Chunk[5][5];

    public Renderer(Game game, Player player){
        this.game = game;
        this.player = player;

        for(int i = 0; i < 5; i++){
            for(int c = 0; c < 5; c++){
                chunkMap[c][i] = new Chunk();
            }
        }
        prepare();
    }

    void prepare(){
        game.removeObjects(game.getObjects(Tile.class));
        for(int i = 0; i < 5; i++){
            for(int c = 0; c < 5; c++){
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        int ScreenX = (int) ((i*cellSize*16) + ((x*cellSize+cellSize/2)- player.x*cellSize));
                        int ScreenY = (int) ((c*cellSize*16) + ((y*cellSize+cellSize/2)- player.y*cellSize));
                        game.addObject(chunkMap[c][i].map[y][x], ScreenX, ScreenY);
                    }
                }
            }
        }
    }

    void render(){
        if(playerX == player.x && playerY == player.y) return;
        playerX = player.x;
        playerY = player.y;
        for(int i = 0; i < 5; i++){
            for(int c = 0; c < 5; c++){
                for(int x = 0; x < mapSize; x++){
                    for(int y = 0; y < mapSize; y++){
                        int ScreenX = (int) ((i*cellSize*16) + ((x*cellSize+cellSize/2)- player.x*cellSize));
                        int ScreenY = (int) ((c*cellSize*16) + ((y*cellSize+cellSize/2)- player.y*cellSize));
                        chunkMap[c][i].map[y][x].setLocation(ScreenX, ScreenY);
                    }
                }
            }
        }
    }
}
