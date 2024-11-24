package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public void Jump() {
    }

    public void updatePlayer(float deltaTime) {
        if (deltaTime == 0) return;
        if (deltaTime > 0.1f) deltaTime = 0.1f;


        player.stateTime += deltaTime;

        // Vérification des inputs du joueur
        if ((Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) && player.isOnGround()) {
            if (!player.GotLongJump()){
                player.getVelocity().y += 100;
                player.setOnGround(false);
            } else if (player.GotLongJump() && HUD.getNLongJump() > 0) {
                player.getVelocity().y += 150;
                player.setOnGround(false);
                HUD.addNLongJump();
            }
        }

        if(HUD.LJitem >0) {
            if (Gdx.input.isKeyJustPressed(Keys.K) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (player.GotLongJump()) {
                    player.setLongJump(false);
                    HUD.set_NoJ_view();
                }
            }
            if (Gdx.input.isKeyJustPressed(Keys.L) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                if (!player.GotLongJump()) {
                    player.setLongJump(true);
                    HUD.set_NJ_view();
                }
            }
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            player.getVelocity().x = -50; //vitesse max en -x
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            player.getVelocity().x = 50; //vitesse max en +x
        }

        if (HUD.WarpItems>0) {
            if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                if (HUD.getNWarp() > 0) {
                    player.setX(player.getX() + 75);
                    HUD.addNWarp();
                }
            }
            if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                if (HUD.getNWarp() > 0) {
                    player.setX(player.getX() - 75);
                    HUD.addNWarp();
                }
            }
        }


        // Gravité
        player.getVelocity().add(0, GRAVITY);


        player.getVelocity().x = MathUtils.clamp(player.getVelocity().x, -50, 50);


        if (Math.abs(player.getVelocity().x) < 1) {
            player.getVelocity().x = 0;
        }

        player.getVelocity().scl(deltaTime);

        // Détection des collisions pour chaque axe
        Rectangle playerRect = rectPool.obtain();
        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Get all tiles in the current room
        getAllTilesInCurrentRoom(tiles);

        // Vérification collisions horizontales
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

        // Vérification collisions verticales
        playerRect.y += player.getVelocity().y;

        boolean isOnGround = false; // Savoir si le joueur est au sol

        for (Rectangle tile : tiles) {
            if (playerRect.overlaps(tile)) {
                if (player.getVelocity().y < 0) {
                    player.setY(tile.y + tile.height);
                    player.getVelocity().y = 0;
                    isOnGround = true; // Joueur au sol
                }
                else if (player.getVelocity().y > 0){
                    player.setY(tile.y - tile.height);
                    player.getVelocity().y = 0;
                }
            }
        }

        player.setOnGround(isOnGround);

        player.setX(player.getX() + player.getVelocity().x);
        player.setY(player.getY() + player.getVelocity().y);
        player.getVelocity().scl(1 / deltaTime);


        player.getVelocity().x *= Player.DAMPING;

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
