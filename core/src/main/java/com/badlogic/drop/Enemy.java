package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private float x, y, width, height;
    private float velocityX;
    private Texture texture;
    private Rectangle boundingBox;

    public Enemy(float x, float y, float width, float height, float velocityX, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.texture = new Texture(texturePath);
        this.boundingBox = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        x += velocityX * delta;
        boundingBox.setPosition(x, y);
    }

    public void render(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    // Disposez des ressources lorsque l'ennemi est détruit
    public void dispose() {
        texture.dispose();
    }
}
