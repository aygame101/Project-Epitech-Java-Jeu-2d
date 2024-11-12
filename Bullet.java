package com.badlogic.drop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

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

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
