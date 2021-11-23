package collider;

import controller.AsteroidController;
import controller.ObstacleControllerInterface;
import controller.PlayerController;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;

public class AsteroidCollider implements GameCollider{

    private final ObstacleControllerInterface controller;

    public AsteroidCollider(ObstacleControllerInterface controller){

        this.controller = controller;
    }

    @Override
    public @NotNull Shape getShape() {
        return controller.getShape().getShape();
    }

    @Override
    public void handleCollisionWith(GameCollider collider) {
        collider.handleCollisionWith(this);
    }

    @Override
    public void handleCollisionWith(StartshipCollider ship) {
        controller.killAsteroid();
    }

    @Override
    public void handleCollisionWith(BulletCollider bullet) {
        controller.receiveDamage(bullet.getDamage());
    }

}
