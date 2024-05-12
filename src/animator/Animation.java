package animator;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import dialogue.Text;

public class Animation {

    private BufferedImage animSheet;
    private Actor actor;

    private int frameSize;
    private int scale;
    private int animFrameCount;
    private int baseImg;
    private String sheetPath;
    private final String filePrefix = (new File("./src/")).exists()? "./src/" : "./";
    final String path = this.filePrefix + "images/tmp/";

    private int counter;
    private int currentAnim;
    private GreenfootImage[][] frames;

    private HashMap<String, GreenfootImage[][]> framesColour = new HashMap<String, GreenfootImage[][]>();
    public int frameCount;
    private boolean isRunning;
    private List<String> colours;
    private String currentColour = "Default";

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg, List<String> colours){
        try {
            this.actor = actor;
            this.sheetPath = animSheetPath;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            this.animSheet = ImageIO.read(new File(this.filePrefix + animSheetPath));
            this.animFrameCount = this.animSheet.getWidth() / frameSize;
            createFrames(this.animSheet, "Default");

            if(colours != null){
                for(String colour : colours){
                    createFrames(this.animSheet, colour);
                }
            }

            File tmpDir = new File("./images/tmp");
            if(!tmpDir.exists()){
                tmpDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg){
        this(animSheetPath, actor, frameSize, scale, baseImg, null);
    }

    private void createFrames(BufferedImage sheet, String colour) throws IOException{
        if(framesColour.containsKey(colour)) return;
        this.frameCount = this.animSheet.getWidth() / this.frameSize;
        frames = new GreenfootImage[this.animSheet.getHeight() / this.frameSize][frameCount];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length-1];
        for(int c = 0; c < sheet.getHeight() / this.frameSize; c++){
            for(int i = 0; i < sheet.getWidth() / this.frameSize; i++){
                BufferedImage subImg = sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                if(!colour.equals("Default")){
                    subImg = Text.changeImageColour(subImg, Text.getColorFromHex(colour));
                }
                File file = new File(this.path + key + colour + "tmp" + c + "_" + i + "_.png");
                ImageIO.write(subImg, "png", file);
                frames[c][i] = new GreenfootImage(this.path + key + colour + "tmp" + c + "_" + i + "_.png");
                file.delete();
            }
        }
        framesColour.putIfAbsent(colour, frames);
    }

    public void update(){
        if(this.counter >= this.animFrameCount) this.counter = 0;
        GreenfootImage img = framesColour.get(currentColour)[currentAnim][this.isRunning ? this.counter : this.baseImg];
        img.scale(this.frameSize * this.scale, this.frameSize * this.scale);
        this.counter++;
        actor.setImage(img);
    }

    public void setImage(int id){
        GreenfootImage img = framesColour.get(currentColour)[currentAnim][id];
        img.scale(this.frameSize * this.scale, this.frameSize * this.scale);
        actor.setImage(img);
    }

    public void setAnim(int animId){
        this.currentAnim = animId;
    }

    public void stop(){
        this.isRunning = false;
        update();
    }

    public void resume(){
        this.isRunning = true;
    }

    public void resetCounter(){
        this.counter = 0;
    }

    public void setColour(String colourCode){
        this.currentColour = colourCode;
    }
}
