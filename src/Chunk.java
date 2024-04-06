import greenfoot.Greenfoot;

public class Chunk {

    public Tile[][] map = new Tile[16][16];

    public Chunk(){
        for(int i = 0; i < 16; i++){
            for(int c = 0; c < 16; c++){
                map[c][i] = new Tile(Greenfoot.getRandomNumber(2));
            }
        }
    }

}
