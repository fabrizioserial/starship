import edu.austral.dissis.starships.game.GameApplication;
import edu.austral.dissis.starships.game.GameContext;
import edu.austral.dissis.starships.game.WindowSettings;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import static javafx.application.Application.launch;

public class App {

    public static void main(String[] args) {
        launch(Game.class);
    }

}

public class Game extends GameApplication {
    @Override
    public @NotNull WindowSettings setupWindow() {
        return WindowSettings.fromTitle("Starships Game").withSize(1920, 1080);
    }

    @Override
    public Parent initRoot(@NotNull GameContext context) {
        return new Starships(context).start();
    }
}
