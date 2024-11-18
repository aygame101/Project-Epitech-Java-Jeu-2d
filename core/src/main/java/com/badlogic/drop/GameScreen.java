package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final Player player;
    private final Texture playerTexture;
    private final MapLayer roomsLayer;
    private Rectangle currentRoomRect;

    public GameScreen(Game game) {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(400,250, camera);
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        player = new Player(0, 0); // Position initiale du joueur

        // Charger la carte
        map = new TmxMapLoader().load("The_Complete_Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 16/16f);
        roomsLayer = map.getLayers().get("Rooms"); // Nom de la couche contenant les rooms

        // Charger la première salle (room0)
        initializeRoom0();

        // Positionner le joueur sur l'objet "Start" lors de l'initialisation
        positionPlayerAtStart();

        camera.update();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Efface l'écran
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Appelle des méthodes de mise à jour
        updatePlayerPosition(delta);
        handleInput();
        checkRoomChange();

        // Met à jour le rendu de la carte et de la caméra
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Autres dessinateurs (par exemple, SpriteBatch)
        batch.setProjectionMatrix(camera.combined);

        // Dessiner le joueur
        batch.begin();
        batch.draw(playerTexture, player.getX(), player.getY());
        batch.end();

        // Mettre à jour la caméra pour suivre le joueur
        camera.update();
    }

    private void updatePlayerPosition(float delta) {
        player.update(delta);
        checkRoomChange();
    }

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setLeftMove(true);
        } else {
            player.setLeftMove(false);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setRightMove(true);
        } else {
            player.setRightMove(false);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.isOnGround()) {
            player.setJumpMove(true);
        } else {
            player.setJumpMove(false);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            player.setDashMove(true);
        } else {
            player.setDashMove(false);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setShoot(true);
        } else {
            player.setShoot(false);
        }
    }

    private MapObject getMapObjectByName(TiledMap map, String name) {
        for (MapLayer layer : map.getLayers()) {
            for (MapObject object : layer.getObjects()) {
                if (name.equals(object.getName())) {
                    return object;
                }
            }
        }
        return null;
    }

    private Vector2 getObjectPosition(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            return new Vector2(rect.x, rect.y);
        }
        return null;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
        batch.dispose();
        playerTexture.dispose();
    }

    private void checkRoomChange() {
        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle roomRect = ((RectangleMapObject) object).getRectangle();
                if (roomRect.contains(player.getPosition().x, player.getPosition().y)) {
                    loadRoom(roomRect);
                    break;
                }
            }
        }
    }

    private void loadRoom(Rectangle roomRect) {
        currentRoomRect = roomRect;
        // Met à jour la caméra pour correspondre aux dimensions de la salle actuelle
        float roomWidth = roomRect.getWidth();
        float roomHeight = roomRect.getHeight();

        camera.viewportWidth = roomWidth;
        camera.viewportHeight = roomHeight;
        camera.position.set(roomRect.x + roomWidth / 2, roomRect.y + roomHeight / 2, 0);
        camera.update();

        // Positionner le joueur sur l'objet "Start" lors du chargement de la salle
        positionPlayerAtStart();

        // Réinitialiser les objets de la salle, si nécessaire
        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle objectRect = ((RectangleMapObject) object).getRectangle();
                // Initialiser ou réinitialiser les objets dans la nouvelle salle
                if (roomRect.overlaps(objectRect)) {
                    System.out.println("Initializing object in room at: " + objectRect);
                }
            }
        }
        System.out.println("Loading room at: " + roomRect);
    }

    private void initializeRoom0() {
        MapObject roomObject = getMapObjectByName(map, "room0");

        if (roomObject instanceof RectangleMapObject) {
            currentRoomRect = ((RectangleMapObject) roomObject).getRectangle();
            loadRoom(currentRoomRect);
        }
    }

    private void positionPlayerAtStart() {
        MapObject startObject = getMapObjectByName(map, "Start1");
        if (startObject != null) {
            Vector2 startPosition = getObjectPosition(startObject);
            player.setX(startPosition.x);
            player.setY(startPosition.y);
        } else {
            // Valeurs par défaut si l'objet de départ n'est pas trouvé
            player.setX(100);
            player.setY(100);
            System.out.println("No start position found in room.");
        }
    }
}
