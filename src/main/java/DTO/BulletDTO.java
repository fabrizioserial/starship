package DTO;

import controller.BulletController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import model.ModelShape;
import strategy.Bullet;
import strategy.BulletElement;
import view.ModelView;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class BulletDTO implements Serializable {
    private double posX;
    private double posY;
    private String resourceName;
    private double rotate;
    private boolean visible;
    private double velocity;
    private double damage;

    @SneakyThrows
    public Bullet toBulletElement() {
        Bullet bulletElement = new Bullet(posX,posY,rotate,velocity,damage,resourceName);
        bulletElement.setShape(new ModelShape(new Circle(8)));
        bulletElement.setVisible(visible);
        return bulletElement;
    }
}
