package DTO;

import collider.StartshipCollider;
import controller.StartshipController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import model.ModelShape;
import view.ModelView;

import java.io.Serializable;

@Data
@Builder
public class StartshipControllerDTO implements Serializable {
    private double velocity;
    private double posX;
    private double posY;
    private double rotate;
    private String resourceName;

    @SneakyThrows
    public StartshipController toStarshipController(){
        ImageLoader imageLoader = new ImageLoader();
        ModelView view = new ModelView(new ImageView(imageLoader.loadFromResources(resourceName, 60.0, 70.0)), posX,posY,resourceName);
        view.getImage().setRotate(rotate);
        StartshipController controller = new StartshipController(view);
        controller.getShape().getShape().setRotate(rotate);
        controller.setVelocity(velocity);
        return controller;
    }


}
