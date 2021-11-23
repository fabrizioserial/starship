package DTO;

import collider.AsteroidCollider;
import controller.AsteroidController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.Builder;
import lombok.SneakyThrows;
import model.ModelShape;
import view.ModelView;

import java.io.Serializable;

@Builder
public class AsteroidDTO implements Serializable {
    private double velocity;
    private int health;
    private double posX;
    private double posY;
    private String resourceName;
    private double rotate;
    private double size;

    @SneakyThrows
    public AsteroidController toAsteroid() {
        AsteroidController asteroidController;
        ImageLoader imageLoader = new ImageLoader();
        ModelView view = new ModelView(new ImageView(imageLoader.loadFromResources(resourceName,  size, size)),posX,posY,resourceName);
        ModelShape asteroid = new ModelShape(new Circle(size - 40));
        asteroidController = new AsteroidController(view,asteroid,velocity,5);
        asteroidController.getView().getImage().setRotate(rotate);
        asteroidController.getShape().getShape().setRotate(rotate);
        return asteroidController;
    }
}
