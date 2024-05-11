package dialogue;

import Redfoot.Game;
import Redfoot.UI;
import greenfoot.*;
import vector.Vector2;

import javax.imageio.ImageIO;
import java.awt.Color;
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
    private final Game game;
    private Actor dialogueBox;

    private String textLookup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ,.;:-_#'+*<>|^1234567890!\"$%&/()=? ";

    public Text(Game game, Actor dialogueBox){
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

    public void popup(String text, Vector2 pos, int time){ // pos works in screenspace
        Popup thread = new Popup();
        thread.startup(pos, this.game, getTextImage(text, chars[textLookup.indexOf(" ")], 16, 10), time);
    }

    public void showTextBox(String text){
        this.dialogueBox.setImage(getTextImage(text, box, 0, 0));
        this.dialogueBox.getImage().setTransparency(255);
    }

    public void showText(String text, Actor actor){
        actor.setImage(getTextImage(text, chars[textLookup.indexOf(" ")], 16, 10));
        actor.getImage().setTransparency(255);
    }

    public GreenfootImage getTextImage(String text, BufferedImage baseImg, int xOffset2, int yOffset2){
        BufferedImage img = baseImg;
        GreenfootImage returnImg;
        Color colour = Color.BLACK;
        int line = 0;
        int coloumn = 0;
        for (int i = 0; i < text.length(); i++){
            if (text.charAt(i) == '\\') {
                if (text.charAt(i+1) == 'n'){
                    line++;
                    coloumn = 0;
                    i += 2;
                } else if(text.charAt(i+1) == '$'){
                    colour = getColorFromHex(text.substring(i + 2, i + 8));
                    i += 8;
                }
            }
            coloumn++;
            int index = textLookup.indexOf(text.charAt(i));
            if(index == -1) index = 0; // display 'A' if char does not exist in lookup
            img = merge(img, changeImageColour(chars[index], colour), coloumn, line, xOffset2, yOffset2);
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
        return returnImg;
    }

    public static Color getColorFromHex(String hexColor) {
        // Parse hex string to RGB components
        int red = Integer.parseInt(hexColor.substring(0, 2), 16);
        int green = Integer.parseInt(hexColor.substring(2, 4), 16);
        int blue = Integer.parseInt(hexColor.substring(4, 6), 16);

        // Create and return Color object
        return new Color(red, green, blue);
    }

    public static BufferedImage changeImageColour(BufferedImage image, Color colour) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                if ((pixel >> 24) != 0x00) { // Check if pixel is not transparent
                    pixel = colour.getRGB() & 0x00FFFFFF | (pixel & 0xFF000000); // Keep the original alpha value
                }
                result.setRGB(x, y, pixel);
            }
        }

        return result;
    }

    private BufferedImage merge(BufferedImage source, BufferedImage image2, int coloumn, int line, int xOffset2, int yOffset2){
        int newWidth = Math.max(8 + coloumn * 8, source.getWidth());
        int newHeight = Math.max(8 + line * 8, source.getHeight());

        BufferedImage mergedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.drawImage(image2, coloumn * 8 + (xOffset - xOffset2), line * 8 + (yOffset - yOffset2), null);
        g2d.dispose();

        return mergedImage;
    }


    void createFrames() throws IOException {
        int size = (this.sheet.getHeight() / this.frameSize) * (this.sheet.getWidth() / this.frameSize);
        this.chars = new BufferedImage[size];
        String[] keys = sheetPath.replace(".png", "").split("/");
        String key = keys[keys.length-1];
        for(int c = 0; c < this.sheet.getHeight() / this.frameSize; c++){
            for(int i = 0; i < this.sheet.getWidth() / this.frameSize; i++){
                BufferedImage subImg = this.sheet.getSubimage(i * this.frameSize, c * this.frameSize, this.frameSize, this.frameSize);
                this.chars[i + (c * 16)] = subImg;
            }
        }
    }
}

class Popup extends Thread{
    private final int timeToRun = 1000;
    private Vector2 pos;
    private Game game;
    private GreenfootImage img;

    public void run(){
        long time = System.currentTimeMillis();
        UI actor = new UI(this.game.render);
        actor.pos = this.pos;
        this.game.addObject(actor, 100, 100);
        this.game.render.addParticle(actor);
        actor.setImage(img);
        try {
            sleep(timeToRun);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.game.deletionList.add(actor);
        this.game.render.releaseParticle(actor);
    }

    public void startup(Vector2 pos, Game game, GreenfootImage img, int timeToRun){
        this.pos = pos;
        this.game = game;
        this.img = img;
        this.start();
    }
}
