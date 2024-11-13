package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture platformTexture;
    private Texture backgroundTexture;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Platform> platforms;


    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load textures
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        enemyTexture = new Texture(Gdx.files.internal("enemy.png"));
        platformTexture = new Texture(Gdx.files.internal("platform.png"));
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        player = new Player(50, 50);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        platforms = new ArrayList<>();

        // Create platforms
        platforms.add(new Platform(100, 300, 200, 10));
        platforms.add(new Platform(400, 200, 200, 10));
        platforms.add(new Platform(50, 100, 200, 10));
        platforms.add(new Platform(0, 550, 800, 50));

        // Create enemies
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy((int) (Math.random() * 700), (int) (Math.random() * 400)));
        }
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the color buffer

        handleInput();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        } else {
            player.stopMoving(); // Stop moving when no key is pressed
        }

        // Update player
        player.update(platforms);

        // Update game logic
        for (Enemy enemy : enemies) {
            enemy.update(); // Update each enemy
        }

        // Update bullets and check for off-screen bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(); // Update bullet position
            if (bullet.isOffScreen()) {
                bullets.remove(i); // Remove bullet if it's off-screen
            }
        }

        // Check for bullet collisions with enemies
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bullets.remove(i);
                    enemies.remove(j);
                    break; // Exit the inner loop once a collision is detected
                }
            }
        }

        // Draw game elements
        batch.begin(); // Begin the batch

        // Draw background
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw player
        batch.draw(playerTexture, player.getX(), player.getY());

        // Draw enemies
        for (Enemy enemy : enemies) {
            batch.draw(enemyTexture, enemy.getEX(), enemy.getEY());
        }

        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }

        // Draw platforms
        for (Platform platform : platforms) {
            batch.draw(platformTexture, platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
        }

        batch.end(); // End the batch
    }

    private void handleInput() {
        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        // Move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        }
        // Jump
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.isOnGround()) {
            player.jump();
        }
        // Shoot
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            bullets.add(player.shoot());
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        enemyTexture.dispose();
        platformTexture.dispose();
        backgroundTexture.dispose();

        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
    }
}

