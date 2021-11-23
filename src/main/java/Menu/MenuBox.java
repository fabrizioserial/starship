package Menu;

import edu.austral.dissis.starships.file.ImageLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class MenuBox extends StackPane {

    @SneakyThrows
    public MenuBox(String title, String ship, MenuItem... items) {
        Rectangle bg = new Rectangle(1280, 720);
        bg.setX(800);
        bg.setOpacity(0.2);

        Text text = new Text(title + " ");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        text.setFill(Color.WHITE);

        /*ImageLoader loader = new ImageLoader();
        ImageView imageView = new ImageView(loader.loadFromResources(ship,100,150));
        getChildren().add(imageView);*/

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(200, 0, 0, 0));
        vBox.getChildren().add(text);;
        vBox.getChildren().addAll(items);
        vBox.setAlignment(Pos.TOP_CENTER);


        getChildren().addAll(bg, vBox);
    }

    public MenuBox(ImageView logo, MenuItem... items) {
        Rectangle bg = new Rectangle(1280, 720);
        bg.setX(500);
        bg.setOpacity(0.2);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 0, 0, 0));
        vBox.getChildren().addAll(logo);
        vBox.getChildren().addAll(items);
        vBox.setAlignment(Pos.CENTER);
        vBox.setCenterShape(true);


        getChildren().addAll(bg, vBox);
    }
}