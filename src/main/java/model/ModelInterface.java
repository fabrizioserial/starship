package model;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;

public interface ModelInterface {
    public void moveShape(Vector2 to);

    public @NotNull Shape getShape();
}
