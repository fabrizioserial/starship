package strategy;

import interfaces.Observer;
import interfaces.ObserverPlayer;

public interface BulletStrategy {

    public BulletElement fire(ObserverPlayer observer, double x, double y, double rotate);

    public double getDelay();

}
