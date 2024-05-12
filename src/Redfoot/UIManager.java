package Redfoot;

import vector.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    private Map<String, UI> panels = new HashMap<>() {
    };
    private final Game game;
    private ArrayList<Item> inventory = new ArrayList<>();
    private UI[] visualInventory = new UI[10];

    public UIManager(Game game) {
        this.game = game;
        for (int i = 0; i < 10; i++) {
            UI uiElement = new UI(game.render);
            this.game.addObject(uiElement, 800 - (300) + i * 72, 840);
            visualInventory[i] = uiElement;
        }
    }

    public void setElement(UI element, String key, Vector2 pos) {
        element.pos = pos;
        this.panels.put(key, element);
        this.game.addObject(element, (int) pos.x, (int) pos.y);
        element.pos = pos;
    }

    public UI getElement(String key) {
        return panels.get(key);
    }

    public void disableElement(String key) {
        this.panels.get(key).getImage().setTransparency(0);
        this.panels.get(key).active = false;
    }

    public void enableElement(String key) {
        this.panels.get(key).getImage().setTransparency(255);
        this.panels.get(key).active = true;
    }

    public void setPosition(String key, Vector2 pos) {  // works in screenspace
        this.panels.get(key).setLocation((int) pos.x, (int) pos.y);
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
        if (this.panels.get("hotbar").active) {
            for (int i = 0; i < Math.min(this.inventory.size(), 10); i++) {
                UI uiElement = new UI(game.render);
                uiElement.setImage(this.inventory.get(i).img);
                this.game.addObject(uiElement, 476 + i * 72, 850);
            }
        }
    }

    public void setSelectionIndex(int index) {
        this.panels.get("selection").setLocation(476 + index * 72, 850);
    }

    public void update() {

    }
}
