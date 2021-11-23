package interfaces;

import strategy.BulletElement;

public interface ObserverPlayer extends Observer{
    public void updateScore();
    public void updateHealth(double damage);
    public boolean hasThisBullet(BulletElement bulletElement);
}
