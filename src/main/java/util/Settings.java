package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.StartshipController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import lombok.SneakyThrows;
import view.ModelView;

import java.io.File;
import java.io.IOException;

public class Settings {
    private static final ImageLoader imageLoader = new ImageLoader();

    public static int PLAYERS;
    public static int LIVES;

    public static KeyCode[][] PLAYER_KEYSBOARD;

    public static String[] SHIP_NAMES;
    public static String[] BULLET_BY_SHIP;


    public static StartshipController[] getPlayerShips() {
        try {
            return new StartshipController[]{
                    new StartshipController(new ModelView(new ImageView(imageLoader.loadFromResources(SHIP_NAMES[1], 60, 70)), 200, 200,SHIP_NAMES[1])),
                    new StartshipController(new ModelView(new ImageView(imageLoader.loadFromResources(SHIP_NAMES[0], 60, 70)), 1000, 200,SHIP_NAMES[0])),
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setShip(int i, String shipName) {
        SHIP_NAMES[i] = shipName;
    }

    @SneakyThrows
    public static void reloadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        SettingJSON settingFromJson = mapper.readValue(new File("setting.json"), SettingJSON.class);
        PLAYERS = settingFromJson.PLAYERS;
        LIVES = settingFromJson.LIVES;
        PLAYER_KEYSBOARD = settingFromJson.PLAYER_KEYSBOARD;
        SHIP_NAMES = settingFromJson.SHIP_NAMES;
        BULLET_BY_SHIP = settingFromJson.BULLET_BY_SHIP;

    }
}
