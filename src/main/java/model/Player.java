package model;

import DTO.PlayerDTO;
import controller.BulletController;
import controller.StartshipController;
import edu.austral.dissis.starships.game.KeyTracker;
import interfaces.ObservableGUI;
import interfaces.Observer;
import interfaces.ObserverPlayer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Builder;
import strategy.BulletElement;
import strategy.ElectricShotStrategy;
import strategy.FireShotStrategy;
import util.Input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Builder
public class Player implements ObservableGUI,PlayerInterface, ObserverPlayer, Serializable {
    private int score;
    private int health;
    private int id;
    private StartshipController startshipController;
    private Input playerInput;
    private List<Observer> observableList;
    private final BulletController bulletController;
    private int maxLives;

    public Player(int score, int health, int id, StartshipController startshipController, Input playerInput, List<Observer> observableList, BulletController bulletController, int maxLives) {
        this.score = score;
        this.health = health;
        this.id = id;
        this.startshipController = startshipController;
        this.playerInput = playerInput;
        this.bulletController = bulletController;
        this.maxLives = maxLives;
        this.observableList = new ArrayList<>();
    }

    public Player(int id,int health, StartshipController startshipController, Input playerInput){
        this.score = 0;
        this.maxLives = health;
        this.health = health;
        this.id = id;
        this.startshipController = startshipController;
        this.startshipController.setVelocity(1.5);
        this.playerInput = playerInput;
        this.observableList = new ArrayList<>();
        this.bulletController = new BulletController();
        bulletController.setFireView(startshipController.getView().getPath().equals("startship.png") ? new ElectricShotStrategy() : new FireShotStrategy());
    }

    public void setHealth(int health){
        this.health = health;
        this.notifyToObserver();
    }

    public void updatePosition( KeyTracker keyTracker, Pane pane, double secondsSinceLastFrame){
        keyTracker.getKeySet().forEach(  keyCode -> {
            if (keyCode == playerInput.keyFoward)  startshipController.moveFoward(secondsSinceLastFrame, pane);
            if (keyCode == playerInput.keyRotateLeft)  startshipController.rotate(secondsSinceLastFrame, -1.0);
            if (keyCode == playerInput.keyRotateRight)  startshipController.rotate(secondsSinceLastFrame, 1.0);
            if (keyCode == playerInput.keyFire) startshipController.fire(this,pane, bulletController);

        });
    }

    public void updatePositionFire( Pane pane, double secondsSinceLastFrame){
        bulletController.moveFoward(pane,secondsSinceLastFrame);
    }

    @Override
    public void addObserver(Observer observer) {
        observableList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observableList.remove(observer);
    }

    @Override
    public void notifyToObserver() {
        observableList.forEach(Observer::update);
    }

    @Override
    public void update() {
        observableList.forEach(Observer::update);
    }

    @Override
    public void updateScore() {
        this.score = score + 1;
        observableList.forEach(Observer::update);
    }

    @Override
    public void updateHealth(double damage) {
        this.health = health -(int) damage;
        if (health < 0){
            this.health = 0;
        }
        observableList.forEach(Observer::update);
    }

    public boolean hasThisBullet(BulletElement bulletElement){
        AtomicBoolean isHere = new AtomicBoolean(false);
        getFireController().getFireList().forEach(bulletElement1 -> {
            if (bulletElement == bulletElement1){
                isHere.set(true);
            }
        });
        return isHere.get();
    }

    public int getHealth(){
        return health;
    }

    public void setMaxLives(int max){
        this.maxLives = max;
        notifyToObserver();
    }

    public int getMaxLives(){
        return maxLives;
    }

    public ImageView getView() {
        return startshipController.getView().getImage();
    }

    public StartshipController getStarshipController(){
        return startshipController;
    }

    public BulletController getFireController(){
        return bulletController;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return health;
    }

    public PlayerDTO toDTO() {
        return PlayerDTO.builder()
                .id(id)
                .score(score)
                .health(health)
                .maxLives(maxLives)
                .shipController(startshipController.toDTO())
                .keyForward(playerInput.getKeyFoward())
                .keyRotateLeft(playerInput.getKeyRotateLeft())
                .keyRotateRight(playerInput.getKeyRotateRight())
                .keyShoot(playerInput.getKeyFire())
                .bulletController(bulletController.toDTO())
                .build();
    }
}
