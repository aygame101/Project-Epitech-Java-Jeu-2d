package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PlayerUpdater {
    private static final float GRAVITY = -2f;
    private Player player;
    private TiledMapTileLayer platformsLayer;
    private Pool<Rectangle> rectPool;
    private Array<Rectangle> tiles;
    private Rectangle currentRoomRect;
    private boolean debug = false;

    public PlayerUpdater(Player player, TiledMapTileLayer platformsLayer, Rectangle currentRoomRect) {
        this.player = player;
        this.platformsLayer = platformsLayer;
        this.rectPool = new Pool<Rectangle>() {
            @Override
            protected Rectangle newObject() {
                return new Rectangle();
            }
        };
        this.tiles = new Array<Rectangle>();
        this.currentRoomRect = currentRoomRect;
    }

    public void updatePlayer(float deltaTime) {
        if (deltaTime == 0) return;
        if (deltaTime > 0.1f) deltaTime = 0.1f;


        player.stateTime += deltaTime;

        // Check input and apply to velocity & state
        if ((Gdx.input.isKeyPressed(Keys.SPACE) || isTouched(0.5f, 1)) && player.isOnGround()) {
            player.getVelocity().y += 50; // JUMP_VELOCITY
            player.setOnGround(false);
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A) || isTouched(0, 0.25f)) {
            player.getVelocity().x = -50; // MAX_VELOCITY
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D) || isTouched(0.25f, 0.5f)) {
            player.getVelocity().x = 50; // MAX_VELOCITY
        }

        if (Gdx.input.isKeyJustPressed(Keys.B)) {
            debug = !debug;
        }

        // Apply gravity if we are falling
        player.getVelocity().add(0, GRAVITY);

        // Clamp the velocity to the maximum, x-axis only
        player.getVelocity().x = MathUtils.clamp(player.getVelocity().x, -300, 300);

        // If the velocity is < 1, set it to 0 and set state to Standing
        if (Math.abs(player.getVelocity().x) < 1) {
            player.getVelocity().x = 0;
        }

        // Multiply by delta time so we know how far we go in this frame
        player.getVelocity().scl(deltaTime);

        // Perform collision detection & response, on each axis, separately
        Rectangle playerRect = rectPool.obtain();
        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Get all tiles in the current room
        getAllTilesInCurrentRoom(tiles);

        // Check horizontal collisions
        playerRect.x += player.getVelocity().x;
        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                if (player.getVelocity().x > 0) {
                    player.setX(tile.x - player.getWidth());
                }
                if (player.getVelocity().x < 0) {
                    player.setX(tile.x + player.getWidth());
                }
                player.getVelocity().x = 0;
            }
        }
        playerRect.x = player.getX();

        // Check vertical collisions
        playerRect.y += player.getVelocity().y;

        boolean isOnGround = false; // Flag to track if player is on the ground

        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                if (player.getVelocity().y >= 1) {
                    // Collision from the bottom of the tile
                    player.setY(tile.y - tile.height);
                    player.getVelocity().y = 0; // Reset vertical velocity
                }
                if (player.getVelocity().y < 0) {
                    // Collision from the top of the tile (jumping)
                    player.setY(tile.y + tile.height);
                    player.getVelocity().y = 0; // Reset vertical velocity
                    isOnGround = true; // Set ground state to true
                }
            }
        }

// Update the player's ground state
        player.setOnGround(isOnGround);

        // Unscale the velocity by the inverse delta time and set the latest position
        player.setX(player.getX() + player.getVelocity().x);
        player.setY(player.getY() + player.getVelocity().y);
        player.getVelocity().scl(1 / deltaTime);

        // Apply damping to the velocity on the x-axis so we don't walk infinitely once a key was pressed
        player.getVelocity().x *= Player.DAMPING;

    }

    private boolean isTouched(float startX, float endX) {
        // Check for touch inputs between startX and endX
        // startX/endX are given between 0 (left edge of the screen) and 1 (right edge of the screen)
        for (int i = 0; i < 2; i++) {
            float x = Gdx.input.getX(i) / (float) Gdx.graphics.getWidth();
            if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentRoomRect(Rectangle currentRoomRect) {
        this.currentRoomRect = currentRoomRect;
    }

    public void getAllTilesInCurrentRoom(Array<Rectangle> tiles) {
        if (currentRoomRect == null) {
            throw new IllegalStateException("currentRoomRect n'est pas initialisé!");
        }

        rectPool.freeAll(tiles);
        tiles.clear();

        int startX = (int) Math.floor(currentRoomRect.x / platformsLayer.getTileWidth());
        int startY = (int) Math.floor(currentRoomRect.y / platformsLayer.getTileHeight());
        int endX = (int) Math.ceil((currentRoomRect.x + currentRoomRect.width) / platformsLayer.getTileWidth());
        int endY = (int) Math.ceil((currentRoomRect.y + currentRoomRect.height) / platformsLayer.getTileHeight());

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                TiledMapTileLayer.Cell cell = platformsLayer.getCell(x, y);
                if (cell != null && cell.getTile().getProperties().containsKey("Collidable")) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x * platformsLayer.getTileWidth(), y * platformsLayer.getTileHeight(), platformsLayer.getTileWidth(), platformsLayer.getTileHeight());
                    tiles.add(rect);
                }
            }
        }
    }
}
