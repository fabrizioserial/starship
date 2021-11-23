package model;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;

public class ModelShape implements ModelInterface{

    private Shape shape;

    public ModelShape(Shape shape){
        this.shape = shape;
    }

    public void moveShape(Vector2 to){
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }

    public @NotNull Shape getShape(){
        return shape;
    }
}
