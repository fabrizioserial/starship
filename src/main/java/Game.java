import DTO.AsteroidDTO;
import DTO.PlayerDTO;
import GUI.PlayerGUI;
import Menu.MenuContainer;
import Menu.MenuItem;
import collider.GameCollider;
import controller.AsteroidController;
import controller.StartshipController;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import javafx.scene.Parent;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Setter;
import model.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import serializer.GameSerializer;
import serializer.GameState;
import strategy.BulletElement;
import util.Input;
import util.Settings;
import util.Utils;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Game extends GameApplication {

    @Override
    public @NotNull
    WindowSettings setupWindow() {
        return WindowSettings.fromTitle("Starships!").withSize(1280, 720);
    }

    @Override
    public Parent initRoot(@NotNull GameContext context) {
        try {
            Settings.reloadConfig();
            return new GameManager(this, context).init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class GameManager {
    RootSetter rootSetter;
    GameContext context;
    MainTimer mainTimer;


    public GameManager(RootSetter rootSetter, GameContext gameContext) {
        this.rootSetter = rootSetter;
        this.context = gameContext;
    }

    boolean isMenu = true;

    boolean isIntro = true;

    Parent init() throws IOException {
        return isMenu ? loadIntro(null) : loadGame(null);
    }

    private void handleMouseClick() throws IOException {
        isIntro = !isIntro;
        rootSetter.setRoot(init());
    }

    private Parent loadIntro(@Nullable GameState gameState) throws IOException{
        Pane pane = new Pane();
        ImageLoader imageLoader = new ImageLoader();

        BackgroundImage myBackground = new BackgroundImage(imageLoader.loadFromResources("background.jpg", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBackground));

        ImageView logo = new ImageView(imageLoader.loadFromResources("logo.png",400,250));
        logo.setLayoutX(440);

        MenuItem exit = new MenuItem("EXIT");
        exit.setOnMouseClicked(event -> System.exit(0));

        MenuItem loadSaved = new MenuItem("LOAD GAME");
        loadSaved.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                if(gameState == null) {
                    rootSetter.setRoot(init());
                }
                rootSetter.setRoot(loadGame(GameSerializer.loadGame()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem resumeGame = new MenuItem("RESUME GAME");
        resumeGame.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                if(gameState == null) {
                    rootSetter.setRoot(init());
                }
                rootSetter.setRoot(loadGame(gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem save = new MenuItem("SAVE GAME");
        save.setOnMouseClicked(event -> GameSerializer.saveGame(gameState));

        MenuItem newGame = new MenuItem("NEW GAME");
        newGame.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                rootSetter.setRoot(loadGame(null));
            }
            catch (IOException e) {
                e.printStackTrace();
            }});

        MenuItem change_ship = new MenuItem("CHANGE SHIP");
        change_ship.setOnMouseClicked(event -> {
            try {
                rootSetter.setRoot(loadShipSelect(0, gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuContainer menu = new MenuContainer(logo, resumeGame, newGame, loadSaved, save, change_ship, exit);

        pane.getChildren().add(menu);

        pane.getChildren().add(logo);
        return pane;
    }

    private Parent loadGame(@Nullable GameState state) throws IOException {
        ImageLoader imageLoader = new ImageLoader();
        Pane pane = new Pane();

        BackgroundImage myBackground = new BackgroundImage(imageLoader.loadFromResources("background.jpg", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBackground));

        Player[] players ;
        List<AsteroidController> listAsteroidController = new ArrayList<>();
        List<PlayerGUI> playerGUIList = new ArrayList<>();

        if(state == null) {

            players = new Player[Settings.PLAYERS];
            for (int i = 0; i < Settings.PLAYERS; i++) {
                StartshipController startshipController = Objects.requireNonNull(Settings.getPlayerShips())[i];
                Input player1Input = new Input(Settings.PLAYER_KEYSBOARD[i][0],Settings.PLAYER_KEYSBOARD[i][1],
                        Settings.PLAYER_KEYSBOARD[i][2],Settings.PLAYER_KEYSBOARD[i][3]);
                Player player = new Player(i+1,Settings.LIVES,startshipController,player1Input);
                player.getStarshipController().getCollider().setPlayerObserver(player);
                player.getFireController().setFireView(Utils.getBulletStrategy(Settings.BULLET_BY_SHIP[Arrays.stream(Settings.SHIP_NAMES).collect(Collectors.toList()).indexOf(Settings.SHIP_NAMES[i])]));
                PlayerGUI playerGUI = new PlayerGUI(player.getStarshipController().getView());
                player.addObserver(playerGUI);
                playerGUI.setObservable(player);
                player.update();
                pane.getChildren().add(player.getStarshipController().getImage());
                pane.getChildren().add(playerGUI.getBackground());
                pane.getChildren().add(playerGUI.getPlayerLives());
                pane.getChildren().add(playerGUI.getScoreShape());
                playerGUIList.add(playerGUI);
                players[i] = player;
            }
            System.out.println(players.length);
            Arrays.stream(players).forEach(System.out::println);

        } else {
            players = state.getPlayers().stream().map(PlayerDTO::toPlayer).toArray(Player[]::new);
            for (int i = 0; i < players.length; i++) {
                players[i].getStarshipController().getCollider().setPlayerObserver(players[i]);
                PlayerGUI playerGUI = new PlayerGUI(players[i].getStarshipController().getView());
                players[i].addObserver(playerGUI);
                playerGUI.setObservable(players[i]);

                System.out.println(players[i].getMaxLives());
                players[i].setMaxLives(players[i].getMaxLives());
                players[i].setHealth(players[i].getHealth());
                players[i].notifyToObserver();

                pane.getChildren().add(players[i].getView());
                pane.getChildren().add(playerGUI.getBackground());
                pane.getChildren().add(playerGUI.getPlayerLives());
                pane.getChildren().add(playerGUI.getScoreShape());
                players[i].getFireController().getFireList().forEach(bulletElement -> {
                    pane.getChildren().add(bulletElement.getView().getImage());
                });

                playerGUIList.add(playerGUI);
            }
            listAsteroidController = state.getAsteroids().stream().map(AsteroidDTO::toAsteroid).collect(Collectors.toList());
            listAsteroidController.forEach(asteroidController -> {
                pane.getChildren().add(asteroidController.getView().getImage());
            });
        }


//Player player, PlayerGUI playerGUI, Pane pane , KeyTracker keyTracker
        if(mainTimer == null) mainTimer = new MainTimer(Arrays.stream(players).collect(Collectors.toList()), playerGUIList , pane , context.getKeyTracker());
        mainTimer.setPlayers(Arrays.stream(players).collect(Collectors.toList()));
        mainTimer.setKeyTracker(context.getKeyTracker());
        mainTimer.setPane(pane);
        mainTimer.setAsteroidControllerList(listAsteroidController);
        mainTimer.setPlayerGUI(playerGUIList);

        List<Player> finalPlayers = Arrays.stream(players).collect(Collectors.toList());
        List<AsteroidController> finalListAsteroidController = listAsteroidController;
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {

                try {
                    isMenu = !isMenu;
                    mainTimer.stop();
                    mainTimer.setPaused(true);
                    System.out.println("pause");
                    rootSetter.setRoot(loadIntro(new GameState(finalPlayers, finalListAsteroidController)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        mainTimer.start();

        return pane;
    }

    private Parent loadShipSelect(int i, GameState gameState) throws IOException {
        ImageLoader imageLoader = new ImageLoader();

        Pane pane = new Pane();

        BackgroundImage myBI= new BackgroundImage(imageLoader.loadFromResources("background.jpg", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));
        AtomicReference<String> ship = new AtomicReference<>("starship.png");

        MenuItem falcon = new MenuItem("MILLENNIUM FALCON");
        falcon.setOnMouseClicked(event -> {
            Settings.setShip(i, "starship.png");
            ship.set("starship.png");
        });


        MenuItem xwing = new MenuItem("XWING");
        xwing.setOnMouseClicked(event -> {
            Settings.setShip(i, "starship2.png");
            ship.set("starship2.png");
        });



        MenuItem next = new MenuItem("NEXT PLAYER");
        next.setOnMouseClicked(event -> {
            try {
                if(i == Settings.PLAYERS - 1) rootSetter.setRoot(loadShipSelect(0, gameState));
                else rootSetter.setRoot(loadShipSelect(i + 1, gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem back = new MenuItem("BACK TO MENU");
        back.setOnMouseClicked(event -> {
            try {
                rootSetter.setRoot(loadIntro(gameState));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuContainer menu = new MenuContainer("PLAYER: " + i , ship.get(), falcon, xwing, next, back);

        pane.getChildren().add(menu);

        return pane;
    }


}

@Setter
class MainTimer extends GameTimer {
    private KeyTracker keyTracker;
    private List<Player> players;
    private Pane pane;
    private List<PlayerGUI> playerGUI;
    CollisionEngine collisionEngine;
    private List<AsteroidController> asteroidControllerList;
    private boolean pause = false;

    public MainTimer (List<Player> player, List<PlayerGUI> playerGUI, Pane pane , KeyTracker keyTracker){
        this.players = player;
        this.keyTracker = keyTracker;
        this.pane = pane;
        this.playerGUI = playerGUI;
        this.collisionEngine = new CollisionEngine();
        this.asteroidControllerList = new ArrayList<>();
    }

    public void setPaused(boolean pause){
        this.pause = pause;
    }

    @Override
    public void nextFrame(double secondsSinceLastFrame) {
        if(pause) {
            secondsSinceLastFrame = 0;
            pause = false;
        }
        pane.requestFocus();


        collisionEngine.checkCollisions(getColliders());
        CheckAlive();
        updatePosition( secondsSinceLastFrame );
        generateAsteroid();

    }

    public void CheckAlive(){

        List<Player> playerToDelete = new ArrayList<>();
        List<PlayerGUI> playerGUIToDelete = new ArrayList<>();
        for (int i = 0; i < players.size(); i++){
            if(players.get(i).getLives() <= 0){
                playerToDelete.add(players.get(i));
                playerGUIToDelete.add(playerGUI.get(i));
                pane.getChildren().remove(players.get(i).getView());
                pane.getChildren().remove(playerGUI.get(i).getScoreShape());
                pane.getChildren().remove(playerGUI.get(i).getPlayerLives());
                pane.getChildren().remove(playerGUI.get(i).getBackground());
            }
        }

        playerGUIToDelete.forEach(playerGUI1 -> {
            playerGUI.remove(playerGUI1);
        });
        playerToDelete.forEach(i -> {
            players.remove(i);
        });

        if (players.size() == 0) {
            stop();
            StackPane gameOverRectangle = GameOver();
            gameOverRectangle.translateXProperty().setValue(pane.getWidth() / 2 - gameOverRectangle.getPrefWidth() / 2);
            gameOverRectangle.translateYProperty().setValue(pane.getHeight() / 2 - gameOverRectangle.getPrefHeight() / 2);
            pane.getChildren().add(gameOverRectangle);
        }

        List<BulletElement> bulletElementList = new ArrayList<>();
        players.forEach(player -> {
            player.getFireController().getFireList().forEach(fireElement -> {
                if(!fireElement.getVisible()){
                    bulletElementList.add(fireElement);
                }
            });
        });

        bulletElementList.forEach(fireElement -> {
            pane.getChildren().remove(fireElement.getView().getImage());
            pane.getChildren().remove(fireElement.getShape());
        });
        List<AsteroidController> asteroidToDelete = new ArrayList<>();
        asteroidControllerList.forEach(asteroidController -> {
            if( asteroidController.getHealth() == 0 ){
                asteroidToDelete.add(asteroidController);
            }
        });
        asteroidToDelete.forEach(asteroidController -> {
            asteroidControllerList.remove(asteroidController);
            pane.getChildren().remove(asteroidController.getView().getImage());
        });
    }

    /**
     * This generator works like this:
     * if verticalOrHorizontal is equals to
     *         ZERO -> spawn from top side
     *         ONE -> spawn from right side
     *         TWO -> spawn from bottom side
     *         THREE -> spawn from left side
     */
    public void generateAsteroid(){
        Random random = new Random();
        int isGointgToRespawn = random.nextInt(100);
        if(isGointgToRespawn > 95 && asteroidControllerList.size() < 10){
               AsteroidController asteroidToAdd = Utils.respawnAsteroid(pane.getWidth(), pane.getHeight(),pane);
               pane.getChildren().add(asteroidToAdd.getView().getImage());
            asteroidControllerList.add(asteroidToAdd);
        }
    }


    private StackPane GameOver() {
        Rectangle rectangle = new Rectangle(1280, 720);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.4);
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);

        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);

        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);

        blend.setBottomInput(ds);

        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#2692df"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);

        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);

        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#2692df"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);

        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#2692df"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);

        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);

        blend.setTopInput(blend1);


        Text text = new Text("GAME OVER");
        text.setFont(Font.font(150));
        text.setFill(Color.WHITE);
        text.setX(150);
        text.setY(150);
        text.setEffect(blend);


        StackPane stackPane = new StackPane(rectangle, text);
        stackPane.setPrefSize(1280, 720);

        return stackPane;
    }

    public void updatePosition( Double secondsSinceLastFrame ){
        double movement = 100 * secondsSinceLastFrame;

        for (int i = 0; i < players.size(); i++) {
            players.get(i).updatePosition(keyTracker, pane, movement);
            players.get(i).updatePositionFire(pane,movement);
            playerGUI.get(i).showPlayerLive();
        }

        asteroidControllerList.forEach(asteroidController -> {
            asteroidController.updatePosition(pane,movement);
        });

    }

    private List<GameCollider> getColliders() {
        List<GameCollider> colliders = new ArrayList<>();
        players.forEach(player -> {
            colliders.add(player.getStarshipController().getCollider());
            player.getFireController().getFireList().forEach(bulletElement -> {
                colliders.add( bulletElement.getCollider());
            });
        });

        asteroidControllerList.forEach(asteroidController -> {
            colliders.add( asteroidController.getCollider() );
        });

        return colliders;
    }
}

