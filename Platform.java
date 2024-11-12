package com.badlogic.drop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Platform {
    private int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN); // Set color for the platform
        g.fillRect(x, y, width, height); // Draw the platform
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // Return the bounding box for collision detection
    }

    public int getY() {
        return y; // Return the y position of the platform
    }
}
