package com.badlogic.drop;

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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
    private final Player player;
    private final Texture playerTexture;
    private final MapLayer roomsLayer;
    private final TiledMapTileLayer platformsLayer;  // Calque pour les plateformes
    private Rectangle currentRoomRect;

    private Array<Enemy> enemies;
    private final Texture enemyTexture;

    private final Pool<Rectangle> rectPool;

    public GameScreen(Main game) {
        // initialisation des champs omis pour la brièveté
        map = new TmxMapLoader().load("The_Complete_Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        viewport = new FitViewport(1366, 768, camera);
        batch = new SpriteBatch();
        playerTexture = new Texture("Player.png");
        player = new Player(100, 100, 16, 16);
        roomsLayer = map.getLayers().get("Rooms");
        platformsLayer = (TiledMapTileLayer) map.getLayers().get("Platformes");
        rectPool = new Pool<Rectangle>() {
            @Override
            protected Rectangle newObject() {
                return new Rectangle();
            }
        };

        enemyTexture = new Texture("enemy.png");
        enemies = new Array<>();

        // Charger la salle initiale
        Rectangle initialRoom = getRoomRectangle(map, "InitialRoom");
        if (initialRoom != null) {
            loadRoom(initialRoom);
        } else {
            Gdx.app.log("GameScreen", "Initial room not found.");
        }

        positionPlayerAtStart();
        spawnEnemies();
    }

    private void spawnEnemies() {
        enemies.add(new Enemy(300, 100, 16, 16, -50, "enemy.png"));
        enemies.add(new Enemy(500, 150, 16, 16, -70, "enemy.png"));
        // Ajoutez autant d'ennemis que nécessaire
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
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
        batch.end();



        // Mettre à jour la position du joueur
        updatePlayerPosition(delta);

        // Vérifier les collisions après la mise à jour de la position du joueur
        checkCollisions();

        checkTeleporterCollision();
    }

    private void updatePlayerPosition(float delta) {
        player.update(delta);
    }
    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
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
    private void checkPlatformCollisions() {
        Array<Rectangle> platformTiles = new Array<>();
        int startX = (int) (player.getX() / platformsLayer.getTileWidth());
        int startY = (int) (player.getY() / platformsLayer.getTileHeight());
        int endX = (int) ((player.getX() + player.getWidth()) / platformsLayer.getTileWidth());
        int endY = (int) ((player.getY() + player.getHeight()) / platformsLayer.getTileHeight());

        getTiles(startX, startY, endX, endY, platformTiles);

        for (Rectangle tile : platformTiles) {
            if (player.getBoundingBox().overlaps(tile)) {
                // gestion des collisions ici
                player.setOnGround(true);
                break;
            }
        }
    }

    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = platformsLayer; // Utilise le calque des plateformes
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
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



    private void checkCollisions() {
        player.setOnGround(false); // Réinitialiser l'état "au sol"

        // Obtient les coordonnées en tuiles du joueur
        int playerTileX = (int) (player.getX() / platformsLayer.getTileWidth());
        int playerTileY = (int) (player.getY() / platformsLayer.getTileHeight());

        // Vérifie les collisions
        for (int x = playerTileX; x < playerTileX + player.getWidth() / platformsLayer.getTileWidth(); x++) {
            for (int y = playerTileY; y < playerTileY + player.getHeight() / platformsLayer.getTileHeight(); y++) {
                if (platformsLayer.getCell(x, y) != null) {
                    // Ajuster la position du joueur pour "atterrir" sur la plateforme
                    player.setY(y * platformsLayer.getTileHeight() + platformsLayer.getTileHeight());
                    player.setOnGround(true);
                    return;
                }
            }
        }
        for (Enemy enemy : enemies) {
            if (player.getBoundingBox().overlaps(enemy.getBoundingBox())) {
                player.setY(player.getY() - 5);
            }
        }
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

        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        enemyTexture.dispose();
    }
    }

