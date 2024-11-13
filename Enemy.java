package com.badlogic.drop;

import com. badlogic. gdx. graphics. Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.*;

class Enemy {
    private int x, y, dx;
    private final int WIDTH = 50, HEIGHT = 50;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = (Math.random() < 0.5) ? 2 : -2; // Randomly set the direction
    }
    public int getEY() {
        return y; // Return the y position of the platform
    }

    public float getEX() {
        return x;
    }
    public void update() {
        x += dx;
        if (x < 0 || x > 800 - WIDTH) {
            dx = -dx; // Reverse direction if hitting the wall
        }
    }

    public void draw(SpriteBatch g) {
        g.setColor(Color.RED);
        g.flush();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

}

