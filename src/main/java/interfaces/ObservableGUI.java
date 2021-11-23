package interfaces;

import javafx.scene.image.ImageView;

public interface ObservableGUI extends Observable{
    public int getHealth();

    public int getMaxLives();

    public ImageView getView();

    public int getScore();

}
