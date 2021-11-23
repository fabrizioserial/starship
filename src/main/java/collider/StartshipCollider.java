package collider;

import controller.PlayerController;
import interfaces.Observer;
import interfaces.ObserverPlayer;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import strategy.BulletElement;

public class StartshipCollider implements GameCollider{

    private final PlayerController playerController;
    private ObserverPlayer player;

    public StartshipCollider(PlayerController playerController){
        this.playerController = playerController;
    }

    public void setPlayerObserver(ObserverPlayer player){
        this.player = player;
    }

    @Override
    public void handleCollisionWith(GameCollider collider) {
        collider.handleCollisionWith(this);
    }

    @Override
    public void handleCollisionWith(AsteroidCollider asteroid) {
        player.updateHealth(1);
    }

    @Override
    public @NotNull Shape getShape() {
        return playerController.getShape().getShape();
    }

    public void receiveDamage(double damage){
        player.updateHealth(damage);
    }

    public boolean hasThisBullet(BulletElement bulletElement){
        return player.hasThisBullet(bulletElement);
    }
}
