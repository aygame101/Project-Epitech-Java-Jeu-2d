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

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final SpriteBatch batch;
    private final Player player;
    private final Texture playerTexture;

    private final MapLayer roomsLayer;

    public GameScreen(Game game) {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(16, 9, camera);

        this.map = new TmxMapLoader().load("The_Complete_Map.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

        roomsLayer = map.getLayers().get("Rooms");

        MapObject startObject = getMapObjectByName(map, "Start");
        Vector2 startCoordinates = getObjectPosition(startObject);

        player = new Player(startCoordinates.x, startCoordinates.y);
        Gdx.input.setInputProcessor(player);

        playerTexture = new Texture(Gdx.files.internal("Player.png"));
        batch = new SpriteBatch();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updatePlayerPosition(delta);

        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.draw(playerTexture, player.getPosition().x, player.getPosition().y);
        batch.end();

        checkRoomChange();
    }

    private void updatePlayerPosition(float delta) {
        if (player.isLeftMove()) {
            player.setX(player.getX() - 5 * delta);
        }
        if (player.isRightMove()) {
            player.setX(player.getX() + 5 * delta);
        }
        if (player.isJumpMove()) {
            player.setY(player.getY() + 10 * delta);
        }

        player.update(delta);
    }

    private void handleInput() {}

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
        // Gérez d'autres types d'objets si nécessaire
        return new Vector2(0, 0);
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
        // Logique pour charger la salle correspondant au rectangle
        // Vous pouvez ajuster cette méthode selon vos besoins
        System.out.println("Loading room at: " + roomRect);
    }
}
