package animator;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Animation {

    private BufferedImage animSheet;
    private Actor actor;

    private int frameSize;
    private int scale;
    private int animFrameCount;
    private int baseImg;
    private String sheetPath;
    private final String filePrefix = (new File("./src/")).exists()? "./src/" : "./";
    final String path = this.filePrefix + "images/tmp";

    private int counter;
    private int currentAnim;


    private GreenfootImage[][] frames;
    private boolean isRunning;

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg){
        try {
            this.actor = actor;
            this.sheetPath = animSheetPath;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            this.animSheet = ImageIO.read(new File(this.filePrefix + animSheetPath));
            createFrames(this.animSheet);
            this.animFrameCount = this.animSheet.getWidth() / frameSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createFrames(BufferedImage sheet) throws IOException{
        this.frames = new GreenfootImage[this.animSheet.getHeight() / this.frameSize][this.animSheet.getWidth() / this.frameSize];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length-1];
        for(int c = 0; c < sheet.getHeight() / this.frameSize; c++){
            for(int i = 0; i < sheet.getWidth() / this.frameSize; i++){
                BufferedImage subImg = sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                File file = new File(this.path + key + "tmp" + c + "_" + i + "_.png");
                ImageIO.write(subImg, "png", file);
                this.frames[c][i] = new GreenfootImage(this.path + key + "tmp" + c + "_" + i + "_.png");
                file.delete();
            }
        }
    }

    public void update(){
        if(this.counter >= this.animFrameCount) this.counter = 0;
        GreenfootImage img = frames[currentAnim][this.isRunning ? this.counter : this.baseImg];
        img.scale(this.frameSize * this.scale, this.frameSize * this.scale);
        this.counter++;
        actor.setImage(img);
    }

    public void setAnim(int animId){
        this.currentAnim = animId;
    }

    public void stop(){
        this.isRunning = false;
    }

    public void resume(){
        this.isRunning = true;
    }
}
