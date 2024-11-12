package com.badlogic.drop;
import com. badlogic. gdx. graphics. Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

class Bullet {
    private int x, y;
    private final int WIDTH = 5, HEIGHT = 5;
    private final int SPEED = -10; // Bullet moves upwards

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += SPEED;
    }

    public void draw(SpriteBatch g) {
        g.setColor(Color.RED);
        g.flush();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
