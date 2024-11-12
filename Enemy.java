package com.badlogic.drop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Enemy {
    private int x, y, dx;
    private final int WIDTH = 50, HEIGHT = 50;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = (Math.random() < 0.5) ? 2 : -2; // Randomly set the direction
    }

    public void update() {
        x += dx;
        if (x < 0 || x > 800 - WIDTH) {
            dx = -dx; // Reverse direction if hitting the wall
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
