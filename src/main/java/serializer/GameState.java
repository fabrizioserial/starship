package serializer;



import DTO.AsteroidDTO;
import DTO.PlayerDTO;
import controller.AsteroidController;
import lombok.Data;
import model.Player;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameState implements Serializable {

    private final List<PlayerDTO> players;
    private final List<AsteroidDTO> asteroids;

    public GameState(List<Player> players, List<AsteroidController> asteroids) {
        this.players = players.stream().map(Player::toDTO).collect(Collectors.toList());
        this.asteroids = asteroids.stream().map(AsteroidController::toDTO).collect(Collectors.toList());
    }
}
