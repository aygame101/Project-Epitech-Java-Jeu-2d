package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Projectiles {
    private float x, y;  // Position du projectile
    private float velocityX;  // Vitesse du projectile
    private Texture texture;  // Texture du projectile

    public Projectiles(float x, float y, float velocityX) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.texture = new Texture("Projectiles.png");  // Assurez-vous que vous avez une texture pour le projectile
    }

    public void update(float delta) {
        // Déplacer le projectile
        x += velocityX * delta;
    }

    public void render(SpriteBatch batch) {
        // Dessiner le projectile à sa position actuelle
        batch.draw(texture, x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        texture.dispose();
    }
}
