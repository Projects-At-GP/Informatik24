package animator;

import dialogue.Text;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Animation {

    private BufferedImage animSheet;
    private Actor actor;

    private int frameSize;
    private int scale;
    private int animFrameCount;
    private int baseImg;
    private String sheetPath;
    private final String filePrefix = (new File("./src/")).exists() ? "./src/" : "./";
    final String path = this.filePrefix + "images/tmp/";

    private int counter;
    private int currentAnim;
    private GreenfootImage[][] frames;

    private HashMap<String, GreenfootImage[][]> framesColor = new HashMap<String, GreenfootImage[][]>();
    public int frameCount;
    private boolean isRunning;
    private List<String> colors;
    private String currentColor = "Default";

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg, List<String> colors) {
        try {
            this.actor = actor;
            this.sheetPath = animSheetPath;
            this.baseImg = baseImg;
            this.frameSize = frameSize;
            this.scale = scale;
            System.out.println("Reading Sheet" + animSheetPath);
            this.animSheet = ImageIO.read(new File(animSheetPath));
            this.animFrameCount = this.animSheet.getWidth() / frameSize;
            createFrames(this.animSheet, "Default");

            if (colors != null) {
                for (String color : colors) {
                    createFrames(this.animSheet, color);
                }
            }

            File tmpDir = new File("./images/tmp");
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Animation(String animSheetPath, Actor actor, int frameSize, int scale, int baseImg) {
        this(animSheetPath, actor, frameSize, scale, baseImg, null);
    }

    private void createFrames(BufferedImage sheet, String color) throws IOException {
        if (framesColor.containsKey(color)) return;
        this.frameCount = this.animSheet.getWidth() / this.frameSize;
        frames = new GreenfootImage[this.animSheet.getHeight() / this.frameSize][frameCount];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length - 1];
        for (int c = 0; c < sheet.getHeight() / this.frameSize; c++) {
            for (int i = 0; i < sheet.getWidth() / this.frameSize; i++) {
                BufferedImage subImg = sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                if (!color.equals("Default")) {
                    subImg = Text.changeImageColor(subImg, Text.getColorFromHex(color));
                }
                File file = new File(this.path + key + color + "tmp" + c + "_" + i + "_.png");
                ImageIO.write(subImg, "png", file);
                frames[c][i] = new GreenfootImage(this.path + key + color + "tmp" + c + "_" + i + "_.png");
                file.delete();
            }
        }
        framesColor.putIfAbsent(color, frames);
    }

    public void update() {
        if (this.counter >= this.animFrameCount) this.counter = 0;
        GreenfootImage img = framesColor.get(currentColor)[currentAnim][this.isRunning ? this.counter : this.baseImg];
        img.scale(this.frameSize * this.scale, this.frameSize * this.scale);
        this.counter++;
        actor.setImage(img);
    }

    public void setImage(int id) {
        GreenfootImage img = framesColor.get(currentColor)[currentAnim][id];
        img.scale(this.frameSize * this.scale, this.frameSize * this.scale);
        actor.setImage(img);
    }

    public void setAnim(int animId) {
        this.currentAnim = animId;
    }

    public void stop() {
        this.isRunning = false;
        update();
    }

    public void resume() {
        this.isRunning = true;
    }

    public void resetCounter() {
        this.counter = 0;
    }

    public void setColor(String colorCode) {
        this.currentColor = colorCode;
    }
}
