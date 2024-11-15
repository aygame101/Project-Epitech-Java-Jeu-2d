package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private Game game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private Player player;
    private Texture playerTexture;

    // Variables pour la gestion des salles
    private MapLayer roomsLayer;
    private Vector2 currentRoomCenter;

    // La classe interne pour encapsuler les informations de la salle
    public class RoomInfo {
        private String name;
        private Rectangle rectangle;
        private float width;
        private float height;

        public RoomInfo(String name, Rectangle rectangle) {
            this.name = name;
            this.rectangle = rectangle;
            this.width = rectangle.getWidth();
            this.height = rectangle.getHeight();
        }

        public String getName() {
            return name;
        }
        public Rectangle getRectangle() {
            return rectangle;
        }
        public float getWidth()  {
            return width;
        }
        public float getHeight() {
            return height;
        }
    }

    public GameScreen(Game game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(80, 60, camera);
        this.map = new TmxMapLoader().load("The_complete_Map.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);

        roomsLayer = map.getLayers().get("Rooms"); // Assurez-vous que votre couche de salles s'appelle "Rooms"

        player = new Player();
        Gdx.input.setInputProcessor(player);

        playerTexture = new Texture(Gdx.files.internal("Player.png"));
        batch = new SpriteBatch();
    }

    private void moveToRoom(int roomX, int roomY) {
        // Implementation omitted for shortness
    }

    private String getRoomNameByCoordinates(int x, int y) {
        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rect = rectangleObject.getRectangle();
                if (rect.contains(x, y)) {
                    return object.getName();
                }
            }
        }
        return null; // ou lever une exception ou gérer le cas où aucune room n'est trouvée à ces coordonnées
    }

    private RoomInfo getRoomBounds(String roomName) {
        for (MapObject object : roomsLayer.getObjects()) {
            if (object.getName().equals(roomName) && object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();
                return new RoomInfo(roomName, rectangle);
            }
        }
        return null; // ou lever une exception ou gérer le cas où l'objet n'est pas trouvé
    }

    @Override
    public void show() {
        // Implementation omitted for shortness
    }

    @Override
    public void render(float delta) {

        updatePlayerPosition(delta);

        // Efface l'écran avant de dessiner chaque nouvelle image
        Gdx.gl.glClearColor(0, 0, 0, 1); // Couleur de fond (noir)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the map
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(playerTexture, Player.getX(), Player.getY(), 1, 1);  // Dessiner le joueur

        for (Projectiles p : player.getProjectiles()) {
            p.render(batch);
        }
        batch.end();

    }

    private void updatePlayerPosition(float delta) {
        if (player.isLeftMove()) {
            player.setX(player.getX() - 5 * delta);  // Déplacer vers la gauche
        }
        if (player.isRightMove()) {
            player.setX(player.getX() + 5 * delta);  // Déplacer vers la droite
        }
        if (player.isJumpMove()) {
            player.setY(player.getY() + 10 * delta);  // Simuler un saut
        }
        //Gravité et dash
        player.update(delta);
    }

    private void handleInput() {
        // Code pour la gestion des entrées utilisateur
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
