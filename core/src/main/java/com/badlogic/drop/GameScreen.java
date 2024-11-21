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
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    //le perso
    private final Player player;
    private final Texture playerTexture;
    //les rooms
    private final MapLayer roomsLayer;
    private Rectangle currentRoomRect;
    //les platformes et les murs
    private final TiledMapTileLayer platformsLayer;

    private PlayerUpdater playerUpdater;

    private final Pool<Rectangle> rectPool;

    private Array<Coin> coins;
    private Texture coinTexture;
    private HUD hud;

    public GameScreen(Game game) {
        // initialisation des champs omis pour la brièveté
        map = new TmxMapLoader().load("The_Complete_Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        viewport = new FitViewport(1366, 768, camera);
        batch = new SpriteBatch();
        //gere le perso
        playerTexture = new Texture("Player.png");
        player = new Player(100, 100, 16, 16);
        //gere les room
        roomsLayer = map.getLayers().get("Rooms");
        //Platformes + murs
        platformsLayer = (TiledMapTileLayer) map.getLayers().get("Platformes");

        playerUpdater = new PlayerUpdater(player, platformsLayer,currentRoomRect);
        //HUD
        hud = new HUD(batch);
        //coinTexture = new Texture("coin.png");
        coins = new Array<>();
        loadCoins();

        rectPool = new Pool<Rectangle>() {
            @Override
            protected Rectangle newObject() {
                return new Rectangle();
            }
        };

        // Charger la salle initiale
        Rectangle initialRoom = getRoomRectangle(map, "InitialRoom");
        if (initialRoom != null) {
            loadRoom(initialRoom);
        } else {
            Gdx.app.log("GameScreen", "Initial room not found.");
        }

        positionPlayerAtStart();

        playerUpdater = new PlayerUpdater(player, platformsLayer,currentRoomRect);
    }

    @Override
    public void show() {}

    private void checkCoinCollision() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Coin coin : coins) {
            if (!coin.isCollected() && playerRect.overlaps(coin.getBounds())) {
                coin.collect(); // Marque la pièce comme collectée
                hud.addCoin(); // Incrémente le compteur de pièces dans le HUD
                Gdx.app.log("GameScreen", "Coin collected. Total coins: " + hud.getCoinCount());
            }
        }
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkRoomChange();
        checkCoinCollision();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dessin du joueur
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Dessin des pièces non collectées
        for (Coin coin : coins) {
            if (!coin.isCollected() && isCoinVisible(coin)) {
                batch.draw(coin.getTexture(), coin.getPosition().x, coin.getPosition().y);
            }
        }

        batch.end();

        // Dessiner le HUD après tous les autres éléments
        hud.draw();

        updatePlayerPosition(delta);
        playerUpdater.updatePlayer(delta);
        checkTeleporterCollision();
    }

    private void updatePlayerPosition(float delta) {
        playerUpdater.updatePlayer(delta);
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
        playerUpdater.setCurrentRoomRect(currentRoomRect); // Injecte la salle actuelle dans PlayerUpdater
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
            Gdx.app.log("GameScreen", "Start position 'Start0' not found. Player positioned at default start location.");
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
        return null;
    }

    private void checkTeleporterCollision() {
        MapLayer teleportLayer = map.getLayers().get("Teleporters");
        if (teleportLayer != null) {
            Array<RectangleMapObject> teleporters = new Array<>();
            for (MapObject mapObject : teleportLayer.getObjects()) {
                if (mapObject instanceof RectangleMapObject) {
                    teleporters.add((RectangleMapObject) mapObject);
                }
            }

            for (RectangleMapObject mapObject : teleporters) {
                Rectangle rectangle = mapObject.getRectangle();
                if (player.getBoundingBox().overlaps(rectangle)) {
                    String destination = (String) mapObject.getProperties().get("destination");
                    if (destination != null) {
                        teleportPlayerTo(destination);
                    }
                }
            }
        }
    }

    private void teleportPlayerTo(String destination) {
        MapObject destinationObject = getMapObjectByName(map, destination);
        if (destinationObject != null) {
            Vector2 destinationPosition = getObjectPosition(destinationObject);
            player.setX(destinationPosition.x);
            player.setY(destinationPosition.y);

            Rectangle destinationRoom = getRoomRectangle(map, destination);
            if (destinationRoom != null) {
                loadRoom(destinationRoom);
            } else {
                Gdx.app.log("GameScreen", "Destination room not found for teleporter.");
            }
        } else {
            Gdx.app.log("GameScreen", "Destination object not found for teleporter.");
        }
    }

    private void loadCoins() {
        MapLayer coinLayer = map.getLayers().get("Coins");
        if (coinLayer != null) {
            for (MapObject object : coinLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectObject = (RectangleMapObject) object;
                    Rectangle rect = rectObject.getRectangle();
                    coins.add(new Coin(coinTexture, rect.x, rect.y));
                    Gdx.app.log("GameScreen", "Coin loaded at: " + rect.x + ", " + rect.y);
                }
            }
        } else {
            Gdx.app.log("GameScreen", "No Coins layer found in the map.");
        }
    }

    private boolean isCoinVisible(Coin coin) {
        float left = camera.position.x - (camera.viewportWidth / 2);
        float right = camera.position.x + (camera.viewportWidth / 2);
        float bottom = camera.position.y - (camera.viewportHeight / 2);
        float top = camera.position.y + (camera.viewportHeight / 2);

        Vector2 coinPos = coin.getPosition();

        return coinPos.x > left && coinPos.x < right && coinPos.y > bottom && coinPos.y < top;
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
        hud.dispose();
        coinTexture.dispose();
    }
}
