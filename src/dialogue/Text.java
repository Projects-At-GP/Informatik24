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
    private BufferedImage box;
    private final int xOffset = 8;
    private final int yOffset = 10;
    private final int frameSize = 8;
    private final String filePrefix = (new File("./src/")).exists()? "./src/" : "./";
    private final String sheetPath = "images/textSheet.png";
    private final String boxPath = "images/textbox.png";
    private final String path = this.filePrefix + "images/tmp/";
    private final World game;
    private Actor dialogueBox;

    private String textLookup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ,.;:-_#'+*<>|^1234567890!\"$%&/()=? ";

    public Text(World game, Actor dialogueBox){
        this.game = game;
        this.dialogueBox = dialogueBox;
        try {
            this.sheet = ImageIO.read(new File(this.filePrefix + sheetPath));
            this.box = new BufferedImage(256, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = this.box.createGraphics();
            g2d.drawImage(ImageIO.read(new File(this.filePrefix + boxPath)), 0, 0,256 , 64, null);
            g2d.dispose();
            createFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showText(String text){
        BufferedImage img = box;
        GreenfootImage returnImg;
        int line = 0;
        int coloumn = 0;
        for (int i = 0; i < text.length(); i++){
            if (text.charAt(i) == '\\') {
                if (text.charAt(i+1) == 'n'){
                    line++;
                    coloumn = 0;
                    i += 2;
                }
            }
            coloumn++;
            int index = textLookup.indexOf(text.charAt(i));
            if(index == -1) index = 0;
            img = merge(img, chars[index], coloumn, line);
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
        dialogueBox.setImage(returnImg);
    }

    private BufferedImage merge(BufferedImage source, BufferedImage image2, int coloumn, int line){
        int newWidth = Math.max(8 + coloumn * 8, source.getWidth());
        int newHeight = Math.max(8 + line * 8, source.getHeight());

        BufferedImage mergedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.drawImage(image2, coloumn * 8 + xOffset, line * 8 + yOffset, null);
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