import greenfoot.Greenfoot;

import java.io.File;
import java.util.Scanner;

public class Chunk {

    public Tile[][] map = new Tile[16][16];

    public Chunk(int x, int y){
        System.out.printf("Chunk %d-%d is reading data\n", x, y);
        readChunk(x + "-" + y);
    }

    /**
     * Reads chunkdata from a file
     * @param chunkID accepts String in format "x-y"
     */
    private Chunk readChunk(String chunkID){
        try (Scanner reader = new Scanner(new File("./src/mapdata/overworld/chunk-" + chunkID + ".dat"));) {
            int counter = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] string = data.replaceAll(" ", "").split(",");
                for (int i = 0; i < string.length; i++){
                    int id = Integer.parseInt(string[i]);
                    map[counter][i] = new Tile(id);
                }
                counter++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

}
