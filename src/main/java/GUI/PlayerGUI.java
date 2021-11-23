package GUI;

import interfaces.ObservableGUI;
import interfaces.Observer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import view.ObjectView;

public class PlayerGUI implements Observer {
    int idPlayer;
    ObservableGUI observable;
    int lives;
    int maxlives;
    Shape background;
    Shape liveShape;
    Text scoreShape;
    String scoreText;
    ObjectView view;

    public PlayerGUI(){}

    public PlayerGUI(ObjectView view){
        this.background = new Rectangle(100,20, Paint.valueOf("#000"));
        this.liveShape = new Rectangle(100,20, Paint.valueOf("#00FF00"));
        this.scoreText = "0";
        this.scoreShape = new Text(scoreText);
        this.scoreShape.setFill(Paint.valueOf("#FFF"));
        this.view = view;

    }

    public Shape getBackground(){
        return background;
    }

    public Shape getPlayerLives(){
        return liveShape;
    }

    public void setObservable(ObservableGUI observable){
        this.observable = observable;
        this.lives = observable.getHealth();
    }

    @Override
    public void update() {
        this.maxlives = observable.getMaxLives();
        this.lives = observable.getHealth();
        setScoreText(observable.getScore());
    }

    public void setScoreText(int score){
        this.scoreText = String.valueOf(score);
        this.scoreShape.setText(scoreText);
    }

    public void showPlayerLive(){
        double positionX = view.getImage().getLayoutX() -20;
        double positionY = view.getImage().getLayoutY() -30;
        background.setLayoutX(positionX);
        background.setLayoutY(positionY);
        liveShape.setLayoutX(positionX);
        liveShape.setLayoutY(positionY);
        liveShape.setScaleX(((double) lives)/((double) maxlives));
        scoreShape.setLayoutX(positionX);
        scoreShape.setLayoutY(positionY-20);
    }

    public Text getScoreShape(){
        return scoreShape;
    }



}
