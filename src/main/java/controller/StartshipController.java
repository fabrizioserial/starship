package controller;

import DTO.PlayerDTO;
import DTO.StartshipControllerDTO;
import collider.StartshipCollider;
import edu.austral.dissis.starships.vector.Vector2;
import interfaces.ObserverPlayer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import lombok.Builder;
import model.ModelShape;
import util.Utils;
import view.ModelView;

public class StartshipController implements PlayerController {


    private ModelView view;
    private double velocity;
    private final ModelShape ship;
    private StartshipCollider startshipCollider;

    public StartshipController(ModelView view){
        setView(view);
        this.ship = new ModelShape(
                new Rectangle(
                        (int) view.getImage().getImage().getWidth(),
                        (int)  view.getImage().getImage().getHeight()
                )
        );
        ship.getShape().setLayoutX(view.getPositionX());
        ship.getShape().setLayoutY(view.getPositionY());
        this.startshipCollider = new StartshipCollider(this);
    }

    public void moveFoward(double secondsSinceLastFrame, Pane pane){
        double movement = secondsSinceLastFrame * velocity;
        Vector2 movementVector = Vector2.vectorFromModule(movement, (Math.toRadians(view.getImage().getRotate()) - Math.PI/2));
        Vector2 from = Vector2.vector((float) view.getImage().getLayoutX(), (float) view.getImage().getLayoutY());
        Vector2 to = from.add(movementVector);
        if (Utils.isInScreen(pane,view,to)) {
            move(to);
        }
    }

    public void rotate(double movement,double side){
        view.getImage().setRotate(view.getImage().getRotate() + (movement * side));
        ship.getShape().setRotate(view.getImage().getRotate());
    }

    public void move(Vector2 toPosition){
        view.getImage().setLayoutX(toPosition.getX());
        view.getImage().setLayoutY(toPosition.getY());
        ship.moveShape(Vector2.vector(toPosition.getX() + (view.getImage().getImage().getWidth() - ((Rectangle) ship.getShape()).getWidth())/2, toPosition.getY() + (view.getImage().getImage().getHeight() - ((Rectangle) ship.getShape()).getHeight())/2));
    }

    public void fire(ObserverPlayer player, Pane pane, BulletController bulletController){
        bulletController.fire(player,pane ,this);
    }

    public void setView(ModelView view) {
        this.view = view;
    }

    public ImageView getImage(){
        return view.getImage();
    }

    public void setVelocity(double v){
        this.velocity = v;
    }

    public ModelShape getShape(){
        return ship;
    }

    public ModelShape getShip(){
        return ship;
    }

    @Override
    public ModelView getView() {
        return view;
    }

    public StartshipCollider getCollider(){
        return startshipCollider;
    }

    public StartshipControllerDTO toDTO() {
        return StartshipControllerDTO.builder()
                .posX(view.getPositionX())
                .posY(view.getPositionY())
                .rotate(view.getImage().getRotate())
                .resourceName(view.getPath())
                .velocity(velocity)
                .build();

    }

}
