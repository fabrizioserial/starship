package util;

import controller.AsteroidController;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.ModelShape;
import strategy.BulletStrategy;
import strategy.ElectricShotStrategy;
import strategy.FireShotStrategy;
import view.ModelView;
import view.ObjectView;

import java.io.IOException;
import java.util.Random;

public class Utils {
    public static boolean isInScreen(Pane pane, ObjectView view , Vector2 to){
        return to.getX() > 0 && to.getX() < pane.getWidth() - view.getImage().getImage().getWidth() && to.getY() > 0 && to.getY() < pane.getHeight() - view.getImage().getImage().getHeight();
    }

    public static AsteroidController respawnAsteroid(double width, double height, Pane pane){
        ImageLoader imageLoader = new ImageLoader();
        double positionx=0;
        double positiony=0;

        Random random = new Random();
        int isHorizontalOrVertical = random.nextInt(4);

        switch (isHorizontalOrVertical) {
            case 0 -> {
                positionx = random.nextInt((int) width);
                positiony = height-100;
            }
            case 1 -> {
                positionx = width-100;
                positiony = random.nextInt((int) height);
            }
            case 2 -> {
                positionx = random.nextInt((int) width);
                positiony = 50;
            }
            case 3 -> {
                positionx = 50;
                positiony = random.nextInt((int) height);
            }
        }

        String getRandomImage = "asteroid"+(random.nextInt(3)+1)+".png";
        AsteroidController asteroidController = new AsteroidController();

        double size = (int) ((Math.random() * (150 - 50)) + 50);

        double velocity = random.nextInt(100) / (double) 100;

        try {
            ModelView view = new ModelView(new ImageView(imageLoader.loadFromResources(getRandomImage,  size, size)),positionx,positiony,getRandomImage);
            ModelShape asteroid = new ModelShape(new Circle(size/2));
            asteroidController = new AsteroidController(view,asteroid,velocity,5);
            asteroidController.getView().getImage().setRotate(Math.atan2(view.getPositionX() - pane.getWidth()/2, view.getPositionY() - pane.getHeight()/2));
            asteroidController.getShape().getShape().setRotate(asteroidController.getView().getImage().getRotate());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return asteroidController;
    }

    public static BulletStrategy getBulletStrategy(String starship) {
        System.out.println(starship);
        switch (starship){
            case "ELECTRIC":
                return new ElectricShotStrategy();
            case "FIRE":
                return new FireShotStrategy();
            default:
                return new FireShotStrategy();
        }
    }
}
