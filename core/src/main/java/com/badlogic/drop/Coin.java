package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin extends Item {
    private Texture texture;
    private Rectangle bounds;
    private boolean collected = false;

    public Coin(Texture texture, float x, float y) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return new Vector2(bounds.x, bounds.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }
}
