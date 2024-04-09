package animator;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import javax.imageio.ImageIO;

public class Animation {

    BufferedImage animSheet;
    Actor actor;

    int frameSize;
    int scale;
    int animFrameCount;
    int baseImg;
    String path;

    int counter;
    int currentAnim;

    public boolean isRunning;

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg){
        try {
            this.path = animSheetPath;
            this.actor = actor;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            this.animSheet = ImageIO.read(new File(animSheetPath));
            createFrames(this.animSheet);
            this.animFrameCount = animSheet.getWidth() / frameSize;
        } catch (Exception e){ e.printStackTrace(); }
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    void createFrames(BufferedImage sheet) throws IOException{
        path = path.replace(".png", "/");
        deleteDirectory(new File(path));
        for(int c = 0; c < sheet.getHeight() / frameSize; c++){
            for(int i = 0; i < sheet.getWidth() / frameSize; i++){
                BufferedImage subImg = sheet.getSubimage(i * frameSize, c * frameSize, frameSize, frameSize);
                ImageIO.write(subImg, "png", new File(path + "tmp" + c + "_" + i + "_.png"));
            }
        }
    }

    public void update(){
        if(counter >= animFrameCount) counter = 0;
        GreenfootImage img;
        if(isRunning) {
            img = new GreenfootImage("./src/images/tmpPlayerAnim/crop_" + currentAnim + "_" + counter + "_.png");
        } else {
            img = new GreenfootImage("./src/images/tmpPlayerAnim/crop_0_" + baseImg + "_.png");
        }
        img.scale(frameSize * scale, frameSize * scale);
        actor.setImage(img);
        counter++;
    }

    public void setAnim(int animId){
        currentAnim = animId;
    }
}
