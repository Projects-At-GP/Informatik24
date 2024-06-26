# Informatik24

---
## Table Of Contents
- [Playing the game](#playing-the-game)
- [Running the code](#running-the-code)
- [Map Editor](#map-editor)
---


## Playing the game

Download the most up to date release from the releases section. You need to install Greenfoot as well and open
Cryptcrawl/Informatik24-develop/src/project.greenfoot as a Greenfoot project-file. Then just press 'start' at the bottom
of the screen :) Use `W`​`A`​`S`​`D` to walk and `E` to interact with NPCs. We got some Art (for example the Character Sheets
and the fake Mountain Tiles) from Lanea Zimmerman (https://opengameart.org/content/tiny-16-basic) as they are still
placeholders. However these will eventually be phased out as we create our own textures.

### Content

We are proud to have a large explorable World with multiple small and large settlements.
Just go around and explore a bit! You might find a local wanting to chat with you :)

![Screenshot of a Village](./githubImages/screenshotFarm.png)

There are currently two Enemy-Types in the game. These are the spider, a mobile enemy that has low health and only deals
minimum damage as well as the blob, which has more HP and does considerable damage. If you have any ideas for future
enemies feel free to contact us or open an issue!

Here you have some screenshots of our current enemies and combat system.

![Screenshot of spider](./githubImages/screenshotSpiders.png)
![Screenshot of spider](./githubImages/screenshotBlobs.png)

## Running the code

### Greenfoot

Just open `./src/project.greenfoot` as you would do with any other Greenfoot project.

### IntelliJ

#### Setup

https://github.com/Rc-Cookie/greenfoot-sources-combined/blob/main/Greenfoot%20in%20IntelliJ.pdf

#### Run Configuration For IntelliJ

*filename in project root: `.idea/runConfigurations/Run_Scenario.xml`*

```xml

<component name="ProjectRunConfigurationManager">
    <configuration default="false" name="Run Scenario" type="Application" factoryName="Application">
        <option name="ALTERNATIVE_JRE_PATH" value="greenfoot"/>
        <option name="ALTERNATIVE_JRE_PATH_ENABLED" value="true"/>
        <option name="MAIN_CLASS_NAME" value="greenfoot.export.GreenfootScenarioApplication"/>
        <module name="Informatik24"/>
        <option name="VM_PARAMETERS"
                value="--module-path=&quot;REPLACE_ME&quot; --add-modules=javafx.controls,javafx.fxml"/>
        <option name="WORKING_DIRECTORY" value="$PROJECT_DIR$/src"/>
        <method v="2">
            <option name="Make" enabled="true"/>
        </method>
    </configuration>
</component>
```

> replace `REPLACE_ME` with e.g. `C:\Program Files\Greenfoot371\lib\javafx\lib` *(
probably `C:\Program Files\Greenfoot\lib\javafx\lib`)*

## Map Editor

We also have nice little [Map Editor](https://github.com/CC35A/MapEditor)! 🙃

