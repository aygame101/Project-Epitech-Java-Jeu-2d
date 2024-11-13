package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import java.awt.*;
import java.util.ArrayList;

class Player {
    private int x, y, dx, dy;
    private final int WIDTH = 50, HEIGHT = 50;
    private boolean onGround;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.onGround = true;
        this.dy = 0;
        this.dx = 0;
    }

    public void jump() {
        if (onGround) { // Only allow jumping if the player is on the ground
            dy = -15;
            onGround = false; // Player is no longer on the ground
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void draw(SpriteBatch g) {
        g.setColor(Color.BLUE); // Set color to blue for the player
        g.flush(); // Draw the player as a rectangle

    }

    public void update(ArrayList<Platform> platforms) {
        // Update the player's position based on the current horizontal velocity
        x += dx;

        // Gravity
        if (!onGround) {
            dy += 1; // Apply gravity
        }

        y += dy;

        // Check for ground collision
        onGround = false;
        for (Platform platform : platforms) {
            if (getBounds().intersects(platform.getBounds())) {
                // If the player is falling and hits the platform
                if (dy >= 0) {
                    y = platform.getY() - HEIGHT; // Position the player on top of the platform
                    dy = 0; // Reset vertical velocity
                    onGround = true; // Player is on the ground
                }
            }
        }

        // Prevent going off the screen
        if (x < 0) x = 0;
        if (x > 800 - WIDTH) x = 800 - WIDTH;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public Bullet shoot() {
        return new Bullet(x + WIDTH / 2, y + HEIGHT / 2); // Example of shooting from the player's position
    }

    public void moveLeft() {
        dx = -5; // Set horizontal movement speed to -5 pixels for moving left
    }

    public void moveRight() {
        dx = 5; // Set horizontal movement speed to 5 pixels for moving right
    }

    public boolean isOnGround() {
        return onGround; // Return the current state of onGround
    }

    public void stopMoving() {
        dx = 0; // Stop horizontal movement
    }
}
