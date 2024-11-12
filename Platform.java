package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com. badlogic. gdx. graphics. Color;
import java.awt.*;

public class Platform {
    private int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch g) {
        g.setColor(Color.GREEN); // Set color for the platform
        g.flush(); // Draw the platform
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height); // Return the bounding box for collision detection
    }

    public int getY() {
        return y; // Return the y position of the platform
    }

    public float getX() {
        return 0;
    }

    public float getWidth() {
        return 0;
    }

    public float getHeight() {
        return 0;
    }
}


