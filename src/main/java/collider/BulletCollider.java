package collider;

import controller.BulletController;
import interfaces.Observer;
import interfaces.ObserverPlayer;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import strategy.BulletElement;

public class BulletCollider implements GameCollider{

    private final BulletElement controller;
    private final ObserverPlayer player;

    public BulletCollider(BulletElement controller, ObserverPlayer player){
        this.controller = controller;
        this.player = player;
    }

    public double getDamage(){
        return controller.getDamage();
    }

    @Override
    public @NotNull Shape getShape() {
        return controller.getShape();
    }

    @Override
    public void handleCollisionWith(GameCollider collider) {
        collider.handleCollisionWith(this);
    }

    @Override
    public void handleCollisionWith(StartshipCollider startship) {
        if (controller.getVisible() && !startship.hasThisBullet(controller)){
            controller.setVisible(false);
            startship.receiveDamage(this.controller.getDamage());
            player.updateScore();
        }
    }

    @Override
    public void handleCollisionWith(AsteroidCollider asteroid) {
        if(controller.getVisible()){
            controller.setVisible(false);
            player.updateScore();
        }
    }
}
