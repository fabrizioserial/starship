package strategy;

import interfaces.ObserverPlayer;
import javafx.scene.shape.Circle;
import model.ModelShape;

public class FireShotStrategy implements BulletStrategy {

    private final int delay;

    public FireShotStrategy(){
        this.delay = 500;
    }

    @Override
    public BulletElement fire(ObserverPlayer player, double x, double y, double rotate) {
        BulletElement singleShotInstance = new Bullet(x,y, rotate - 90,2.2,2,"fire2.gif", player);
        singleShotInstance.setShape(new ModelShape(new Circle(8)));
        return singleShotInstance;
    }

    @Override
    public double getDelay() {
        return delay;
    }

}
