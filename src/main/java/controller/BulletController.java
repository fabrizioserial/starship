package controller;

import DTO.BulletControllerDTO;
import edu.austral.dissis.starships.vector.Vector2;
import interfaces.ObserverPlayer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import strategy.BulletElement;
import strategy.BulletStrategy;
import util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BulletController{
    public BulletStrategy bulletStrategy;
    public List<BulletElement> fireStrategyList;
    public double lastShoot;

    public BulletController(){
        fireStrategyList = new ArrayList<>();
        this.lastShoot = System.currentTimeMillis();
    }

    public void fire(ObserverPlayer player, Pane pane, PlayerController starShip){
        if( System.currentTimeMillis() - bulletStrategy.getDelay() > lastShoot ){
            BulletElement fire = bulletStrategy.fire(player,starShip.getView().getPositionX() + starShip.getView().getImage().getImage().getWidth()/2,starShip.getView().getPositionY() + starShip.getView().getImage().getImage().getHeight()/2, starShip.getView().getImage().getRotate());
            pane.getChildren().add(fire.getView().getImage());
            fireStrategyList.add(fire);
            this.lastShoot = System.currentTimeMillis();
        }
    }

    public void moveFoward(Pane pane, double secondsSinceLastFrame){
        List<BulletElement> elementsToDelete = new ArrayList<>();
        for(BulletElement fire : fireStrategyList){

            double movement = secondsSinceLastFrame * fire.getVelocity();
            Vector2 movementVector = Vector2.vectorFromModule(movement, (Math.toRadians(fire.getView().getImage().getRotate() + 90) - Math.PI/2));
            Vector2 from = Vector2.vector((float) fire.getView().getImage().getLayoutX(), (float) fire.getView().getImage().getLayoutY());
            Vector2 to = from.add(movementVector);

            if (!Utils.isInScreen(pane,fire.getView(),to)){
                elementsToDelete.add(fire);
            }else{
                move(fire,to);
            }
        }

        for (BulletElement bulletElement : elementsToDelete){
            pane.getChildren().remove(bulletElement.getView().getImage());
            fireStrategyList.remove(bulletElement);
        }
    }

    public void move(BulletElement fire, Vector2 toPosition){

        fire.getView().getImage().setLayoutX(toPosition.getX());
        fire.getView().getImage().setLayoutY(toPosition.getY());
        fire.getBulletShape().moveShape(Vector2.vector(toPosition.getX() + (fire.getView().getImage().getImage().getWidth() - ((Circle) fire.getShape()).getRadius())/2, toPosition.getY() + (fire.getView().getImage().getImage().getHeight() - ((Circle) fire.getShape()).getRadius())/2));
    }

    public void setFireView(BulletStrategy bulletStrategy){
        this.bulletStrategy = bulletStrategy;
    }

    public BulletStrategy getFireStrategy(){
        return bulletStrategy;
    }

    public List<BulletElement> getFireList(){
        return fireStrategyList;
    }

    public void setFireList(List<BulletElement> list){
        this.fireStrategyList = list;
    }

    public BulletControllerDTO toDTO() {
        return BulletControllerDTO.builder()
                .bullets(fireStrategyList.stream().map(BulletElement::toDTO).collect(Collectors.toList()))
                .build();

    }

}
