package dialogue;

import greenfoot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Text {
    private BufferedImage[] chars;
    private BufferedImage sheet;
    private final int frameSize = 8;
    private final String filePrefix = (new File("./src/")).exists()? "./src/" : "./";
    private final String sheetPath = "images/textSheet.png";
    private final String path = this.filePrefix + "images/tmp/";
    private final World game;
    private Actor dialogueBox;
    private final int maxCharPerLine = 10;

    private String textLookup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ,.;:-_#'+*<>|^1234567890!\"$%&/()=? ";

    public Text(World game, Actor dialogueBox){
        this.game = game;
        this.dialogueBox = dialogueBox;
        try {
            this.sheet = ImageIO.read(new File(this.filePrefix + sheetPath));
            createFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showText(String text){
        BufferedImage img = chars[textLookup.indexOf(text.charAt(0))];
        GreenfootImage returnImg;
        for (int i = 1; i < text.length(); i++){
            int index = textLookup.indexOf(text.charAt(i));
            if(index == -1) index = 0;
            img = merge(img, chars[index], frameSize, 0);
        }
        try {
            File file = new File(this.path + text.hashCode() + "text.png");
            ImageIO.write(img, "png", file);
            returnImg = new GreenfootImage(this.path + text.hashCode() + "text.png");
            file.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        returnImg.scale(returnImg.getWidth() * 3, returnImg.getHeight() * 3);
        //game.getBackground().drawImage(returnImg, 0, 700);
        dialogueBox.setImage(returnImg);
    }

    private BufferedImage merge(BufferedImage source, BufferedImage image2, int addedWidth, int addedHeight){
        int newWidth = source.getWidth() + addedWidth;
        int newHeight = source.getHeight() + addedHeight;

        BufferedImage mergedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.drawImage(image2, source.getWidth(), (source.getHeight() - frameSize) + addedHeight, null);
        g2d.dispose();

        return mergedImage;
    }


    void createFrames() throws IOException {
        int size = (this.sheet.getHeight() / this.frameSize) * (this.sheet.getWidth() / this.frameSize);
        this.chars = new BufferedImage[size];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length-1];
        System.out.println(key);
        for(int c = 0; c < this.sheet.getHeight() / this.frameSize; c++){
            for(int i = 0; i < this.sheet.getWidth() / this.frameSize; i++){
                BufferedImage subImg = this.sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                this.chars[i + (c * 16)] = subImg;
            }
        }
    }
}
