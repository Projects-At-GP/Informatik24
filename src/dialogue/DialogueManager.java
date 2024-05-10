package dialogue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DialogueManager {

    public DialogueManager(){
        System.out.println("Dialogue enabled!");
        JSONParser parser = new JSONParser();
        try {
            JSONArray a = (JSONArray) parser.parse(new FileReader("./dialogues/testDialog.json"));
            for (Object o : a)
            {
                JSONObject person = (JSONObject) o;

                String name = (String) person.get("name");
                System.out.println(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
