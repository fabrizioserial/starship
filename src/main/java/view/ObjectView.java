package view;

import javafx.scene.image.ImageView;

public interface ObjectView {

    public void setImage(ImageView image);

    public ImageView getImage();

    public void setPosition(double x, double y);

    public double getPositionX();

    public double getPositionY();
}
