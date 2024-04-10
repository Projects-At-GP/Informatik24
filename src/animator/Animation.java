package animator;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Animation {

    BufferedImage animSheet;
    Actor actor;

    int frameSize;
    int scale;
    int animFrameCount;
    int baseImg;
    final String path = ((new File("./src/")).exists()? "./src/" : "./") + "images/tmp";

    int counter;
    int currentAnim;


    GreenfootImage[][] frames;
    public boolean isRunning;

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg){
        try {
            this.actor = actor;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            this.animSheet = ImageIO.read(new File(animSheetPath));
            createFrames(this.animSheet);
            this.animFrameCount = animSheet.getWidth() / frameSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createFrames(BufferedImage sheet) throws IOException{
        frames = new GreenfootImage[animSheet.getHeight() / frameSize][animSheet.getWidth() / frameSize];
        for(int c = 0; c < sheet.getHeight() / frameSize; c++){
            for(int i = 0; i < sheet.getWidth() / frameSize; i++){
                BufferedImage subImg = sheet.getSubimage(i * frameSize, c * frameSize, frameSize, frameSize);
                File file = new File(path + "tmp" + c + "_" + i + "_.png");
                ImageIO.write(subImg, "png", file);
                frames[c][i] = new GreenfootImage(path + "tmp" + c + "_" + i + "_.png");
                file.delete();
            }
        }
    }

    public void update(){
        if(counter >= animFrameCount) counter = 0;
        GreenfootImage img;
        if(isRunning) {
            img = frames[currentAnim][counter];
        } else {
            img = frames[currentAnim][baseImg];
        }
        img.scale(frameSize * scale, frameSize * scale);
        actor.setImage(img);
        counter++;
    }

    public void setAnim(int animId){
        currentAnim = animId;
    }
}
