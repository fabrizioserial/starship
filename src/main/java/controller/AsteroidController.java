package controller;

import DTO.AsteroidDTO;
import DTO.BulletControllerDTO;
import collider.AsteroidCollider;
import collider.GameCollider;
import edu.austral.dissis.starships.vector.Vector2;
import interfaces.ObserverAsteroid;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.ModelShape;
import strategy.BulletElement;
import util.Utils;
import view.ModelView;

import java.util.stream.Collectors;

public class AsteroidController implements ObserverAsteroid,ObstacleControllerInterface {
    private ModelView view;
    private ModelShape asteroid;
    private double velocity;
    private AsteroidCollider collider;
    private int health;

    public AsteroidController(){
    }

    public AsteroidController(ModelView view,ModelShape asteroid,double velocity, int health){
        this.view = view;
        this.asteroid = asteroid;
        this.velocity = velocity;
        this.health = health;
        asteroid.getShape().setLayoutX(view.getPositionX() + view.getImage().getImage().getWidth()/2);
        asteroid.getShape().setLayoutY(view.getPositionY() + view.getImage().getImage().getHeight()/2);

        this.collider = new AsteroidCollider(this);
    }

    public void updatePosition(Pane pane, double movement){
        moveFoward(pane, movement);
    }

    public void moveFoward(Pane pane,double secondsSinceLastFrame){
        double movement = secondsSinceLastFrame * velocity;
        Vector2 movementVector = Vector2.vectorFromModule(movement, view.getImage().getRotate() - Math.PI/2);
        Vector2 from = Vector2.vector((float) view.getPositionX(), (float) view.getPositionY());
        Vector2 to = from.add(movementVector);
        if(Utils.isInScreen(pane,view,to)){
            move(to);
        }else{
            killAsteroid();
        }
    }

    public void move(Vector2 toPosition){
        view.getImage().setLayoutX(toPosition.getX());
        view.getImage().setLayoutY(toPosition.getY());
        asteroid.moveShape(Vector2.vector(toPosition.getX() + view.getImage().getImage().getWidth()/2, toPosition.getY() + view.getImage().getImage().getHeight()/2 ));
    }

    public void substractHealth(){
        this.health = health - 1;
    }

    public void killAsteroid(){
        this.health = 0;
    }

    @Override
    public void receiveDamage(double damage) {
        this.health = health - (int)damage;
        if(health < 0) {
            this.health = 0;
        }
    }

    public int getHealth(){
        return health;
    }

    public ModelShape getShape(){
        return asteroid;
    }

    public AsteroidCollider getCollider(){
        return collider;
    }


    public ModelView getView(){
       return view;
    }

    @Override
    public void update() {
        substractHealth();
    }

    @Override
    public void byeAsteroid() {
        killAsteroid();
    }

    public AsteroidDTO toDTO() {
        return AsteroidDTO.builder()
                .posX(view.getPositionX())
                .posY(view.getPositionY())
                .velocity(velocity)
                .rotate(view.getImage().getRotate())
                .health(health)
                .resourceName(view.getPath())
                .size(view.getImage().getImage().getHeight())
                .build();

    }
}
