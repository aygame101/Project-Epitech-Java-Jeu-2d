package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private final int WIDTH = 10; // Width of the bullet
    private final int HEIGHT = 10; // Height of the bullet
    private int dy; // Bullet's vertical speed

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.dy = 10; // Set a speed for the bullet
    }

    public void update() {
        y += dy; // Move the bullet upwards
    }

    public void draw(SpriteBatch batch) {
        batch.setColor(Color.PINK); // Set color to pink
        batch.draw(new Texture("white.png"), x, y, WIDTH, HEIGHT); // Draw a pink square
        batch.setColor(Color.WHITE); // Reset color to white
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOffScreen() {
        return y > Gdx.graphics.getHeight(); // Check if the bullet is off the bottom of the screen
    }

    public void dispose() {
        // Dispose of any resources if necessary
    }
}
