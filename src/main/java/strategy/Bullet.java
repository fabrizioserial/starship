package strategy;

import DTO.BulletDTO;
import collider.BulletCollider;
import edu.austral.dissis.starships.file.ImageLoader;
import interfaces.ObserverPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import lombok.Builder;
import model.ModelShape;
import view.ModelView;

import java.io.IOException;

public class Bullet implements BulletElement {

    private ModelView view;
    private ModelShape shape;
    private boolean visible;
    private double velocity;
    private double damage;
    private BulletCollider collider;
    private String path;

    public Bullet(double x, double y, double rotate, double velocity, double damage,String path ,ObserverPlayer player){
        this.path = path;
        setView(x,y);
        view.getImage().setRotate(rotate);
        this.visible = true;
        this.velocity = velocity;
        this.damage = damage;
        this.collider = new BulletCollider(this,player);
    }
    public Bullet(double x, double y, double rotate, double velocity, double damage,String path){
        this.path = path;
        setView(x,y);
        view.getImage().setRotate(rotate);
        this.visible = true;
        this.velocity = velocity;
        this.damage = damage;
    }

    public void setCollider(ObserverPlayer observerPlayer) {
        this.collider= new BulletCollider(this,observerPlayer);
    }

    @Override
    public ModelView getView() {
        return view;
    }

    @Override
    public void setShape(ModelShape shape) {
        this.shape = shape;
        shape.getShape().setLayoutX(view.getPositionX());
        shape.getShape().setLayoutY(view.getPositionY());
    }

    @Override
    public void setView(double x, double y) {
        try{
            ImageLoader imageLoader = new ImageLoader();
            ImageView image = new ImageView(imageLoader.loadFromResources(path,40,40));
            this.view = new ModelView(image,path);
            view.setPosition(x,y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Shape getShape() {
        return shape.getShape();
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean getVisible() {
        return visible;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public ModelShape getBulletShape() {
        return shape;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public BulletCollider getCollider() {
        return collider;
    }

    public BulletDTO toDTO(){
        return BulletDTO.builder()
                .posX(view.getPositionX())
                .posY(view.getPositionY())
                .resourceName(view.getPath())
                .rotate(view.getImage().getRotate())
                .damage(damage)
                .velocity(velocity)
                .visible(visible)
                .build();
    }
}
