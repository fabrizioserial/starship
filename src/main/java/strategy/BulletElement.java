package strategy;

import DTO.BulletDTO;
import collider.BulletCollider;
import interfaces.ObserverPlayer;
import javafx.scene.shape.Shape;

import model.ModelShape;
import view.ModelView;

public interface BulletElement {
    public ModelView getView();
    public void setShape(ModelShape shape);
    public void setView(double x, double y);
    public Shape getShape();
    public void setVisible(boolean visible);
    public boolean getVisible();
    public double getVelocity();
    public ModelShape getBulletShape();
    public double getDamage();
    public BulletCollider getCollider();
    public void setCollider(ObserverPlayer observerPlayer);
    public BulletDTO toDTO();
}
