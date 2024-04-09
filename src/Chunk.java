import greenfoot.Greenfoot;
import greenfoot.World;

import java.io.File;
import java.util.Scanner;

public class Chunk {

    public Tile[][] map = new Tile[16][16];

    private int x;
    private int y;

    public Chunk(int x, int y){
        this.x = x;
        this.y = y;
        System.out.printf("Chunk %d-%d is reading data\n", x, y);
        readChunk(x + "-" + y);
    }

    /**
     * Reads chunkdata from a file
     * @param chunkID accepts String in format "x-y"
     */
    private Chunk readChunk(String chunkID){
        try {
            File file = new File("./src/mapdata/overworld/chunk-" + chunkID + ".dat");
            if (!file.exists()){
                file = new File("./src/mapdata/overworld/chunk-4-4.dat");
            }
            Scanner reader = new Scanner(file);
            int counter = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] string = data.replaceAll(" ", "").split(",");
                for (int i = 0; i < string.length; i++){
                    int id = Integer.parseInt(string[i]);
                    //System.out.println(id);
                    int tileX = (this.x * 16) + i;
                    int tileY = (this.y * 16) + counter;
                    map[counter][i] = new Tile(id, tileX, tileY);
                }
                counter++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    public void unload(World world){
        for (Tile[] tiles : map){
            for (Tile tile : tiles){
                world.removeObject(tile);
            }
        }
    }

}
