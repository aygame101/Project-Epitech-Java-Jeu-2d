package com.badlogic.drop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
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
    }

    public void jump() {
        if (onGround) { // Only allow jumping if the player is on the ground
            dy = -15; // Set a negative velocity for jumping
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

    public void draw(Graphics g) {
        g.setColor(Color.BLUE); // Set color to blue for the player
        g.fillRect(x, y, WIDTH, HEIGHT); // Draw the player as a  rectangle
    }

    public void update(ArrayList<Platform> platforms) {
        x += dx;

        // Gravity
        if (!onGround) {
            dy += 1; //  gravity
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
}
