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
    private final MapLayer platformsLayer;  // Calque pour les plateformes
    private Rectangle currentRoomRect;

    public GameScreen(Game game) {
        map = new TmxMapLoader().load("The_Complete_Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        batch = new SpriteBatch();
        player = new Player(100, 100, 16, 16); // Position et taille initiale du joueur
        playerTexture = new Texture("player.png");
        roomsLayer = map.getLayers().get("Rooms");
        platformsLayer = map.getLayers().get("Platformes");  // Initialiser le calque des plateformes

        // Initialiser la salle avec `Room0`
        currentRoomRect = getRoomRectangle(map, "Room0");
        if (currentRoomRect != null) {
            loadRoom(currentRoomRect);
        }

        positionPlayerAtStart();
        camera.update();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updatePlayerPosition(delta);
        handleInput();
        checkRoomChange();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        batch.end();
        // Mettre à jour la position du joueur
        updatePlayerPosition(delta);

        // Vérifier les collisions après la mise à jour de la position du joueur
        checkCollisions();
    }

    private void updatePlayerPosition(float delta) {
        player.update(delta);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setX(player.getX() - 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setX(player.getX() + 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setY(player.getY() + 200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setY(player.getY() - 200 * Gdx.graphics.getDeltaTime());
        }
    }

    private void checkRoomChange() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle roomRect = rectObject.getRectangle();

                if (playerRect.overlaps(roomRect) && !roomRect.equals(currentRoomRect)) {
                    loadRoom(roomRect);
                    break;
                }
            }
        }
    }

    private void loadRoom(Rectangle roomRect) {
        if (currentRoomRect != null && currentRoomRect.equals(roomRect)) {
            return;
        }

        float roomWidth = roomRect.getWidth();
        float roomHeight = roomRect.getHeight();

        camera.viewportWidth = roomWidth;
        camera.viewportHeight = roomHeight;
        camera.position.set(roomRect.x + roomWidth / 2, roomRect.y + roomHeight / 2, 0);
        camera.update();

        currentRoomRect = roomRect;
    }

    private void positionPlayerAtStart() {
        MapObject startObject = getMapObjectByName(map, "Start0");
        if (startObject != null) {
            Vector2 startPosition = getObjectPosition(startObject);
            player.setX(startPosition.x);
            player.setY(startPosition.y);
        } else {
            player.setX(100);
            player.setY(100);
        }
    }

    private Rectangle getRoomRectangle(TiledMap map, String roomName) {
        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                if (rectObject.getName() != null && rectObject.getName().equals(roomName)) {
                    return rectObject.getRectangle();
                }
            }
        }
        return null;
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
        return new Vector2();
    }

    private void checkCollisions() {
        player.setOnGround(false); // Réinitialiser l'état "au sol"

        // Vérifier uniquement les collisions avec le calque "Platformes"
        for (MapObject object : platformsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle platformRect = ((RectangleMapObject) object).getRectangle();
                Rectangle playerRect = player.getBoundingBox();

                // Vérifier la collision avec la plateforme
                if (playerRect.overlaps(platformRect)) {
                    // Ajuster la position du joueur pour "atterrir" sur la plateforme
                    player.setY(platformRect.y + platformRect.height);
                    player.setOnGround(true);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
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
}
