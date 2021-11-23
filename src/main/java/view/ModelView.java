package view;

import javafx.scene.image.ImageView;

public class ModelView implements ObjectView{
    private ImageView image;
    private String path;

    public ModelView(ImageView image, double positionx, double positiony, String path){
        this.path = path;
        setImage(image);
        setPosition(positionx,positiony);
    }
    public ModelView(ImageView image, String path){
        setImage(image);
        this.path = path;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage(){
        return image;
    }

    public void setPosition(double x, double y){
        image.setLayoutX(x);
        image.setLayoutY(y);
    }

    public double getPositionX(){
        return image.getLayoutX();
    }

    public double getPositionY(){
        return image.getLayoutY();
    }

    public String getPath(){
        return path;
    }
}
