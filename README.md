# Informatik24
Softwareprojekt


### Run Configuration For IntelliJ
*filename in project root: `.idea/runConfigurations/Run_Scenario.xml`*
```xml
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="Run Scenario" type="Application" factoryName="Application">
    <option name="ALTERNATIVE_JRE_PATH" value="greenfoot" />
    <option name="ALTERNATIVE_JRE_PATH_ENABLED" value="true" />
    <option name="MAIN_CLASS_NAME" value="greenfoot.export.GreenfootScenarioApplication" />
    <module name="Informatik24" />
    <option name="VM_PARAMETERS" value="--module-path=&quot;REPLACE_ME&quot; --add-modules=javafx.controls,javafx.fxml" />
    <method v="2">
      <option name="Make" enabled="true" />
    </method>
  </configuration>
</component>
```
> replace `REPLACE_ME` with e.g. `C:\Program Files\Greenfoot371\lib\javafx\lib` *(probably `C:\Program Files\Greenfoot\lib\javafx\lib`)*
