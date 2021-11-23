package DTO;

import controller.BulletController;
import controller.StartshipController;
import javafx.scene.input.KeyCode;
import lombok.Builder;
import lombok.Data;
import model.Player;
import util.Input;
import util.Settings;
import util.Utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@Builder
public class PlayerDTO implements Serializable {
    private int id;
    private int score;
    private int health;
    private int maxLives;
    private StartshipControllerDTO shipController;
    private BulletControllerDTO bulletController;

    private KeyCode keyForward;
    private KeyCode keyRotateLeft;
    private KeyCode keyBackward;
    private KeyCode keyRotateRight;
    private KeyCode keyShoot;

    public Player toPlayer() {
        StartshipController shipController = this.shipController.toStarshipController();
        BulletController bulletController = this.bulletController.toBulletController();


        Player player = Player.builder()
                .id(id)
                .score(score)
                .health(health)
                .maxLives(maxLives)
                .startshipController(shipController)
                .playerInput(new Input(keyForward, keyRotateLeft, keyRotateRight, keyShoot))
                .bulletController(bulletController)
                .build();

        bulletController.setFireView(Utils.getBulletStrategy(Settings.BULLET_BY_SHIP[Arrays.stream(Settings.SHIP_NAMES).collect(Collectors.toList()).indexOf(shipController.getView().getPath())]));
        bulletController.fireStrategyList.forEach(bulletElement -> bulletElement.setCollider(player));


        return player;
    }
}
