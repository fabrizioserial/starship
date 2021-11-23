package util;

import javafx.scene.input.KeyCode;
import lombok.Data;

@Data
public class SettingJSON {

    public int LIVES;
    public int PLAYERS;
    public KeyCode[][] PLAYER_KEYSBOARD;
    public String[] SHIP_NAMES;
    public String[] BULLET_BY_SHIP;

    public SettingJSON() {}

}