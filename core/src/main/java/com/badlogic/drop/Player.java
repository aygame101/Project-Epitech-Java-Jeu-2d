package com.badlogic.drop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Player extends InputAdapter {

    private float x;        // Position en X
    private float y;        // Position en Y
    private float width;    // Largeur du joueur
    private float height;   // Hauteur du joueur
    private static  int coins = 0;

    private float velocityY = 0;   // Vitesse verticale
    private float gravity = -0.5f; // Force de gravité
    private boolean onGround = true; // Indicateur si le joueur est au sol

    private Array<Projectile> projectiles; // Liste des projectiles tirés par le joueur

    private static ArrayList<ItemShop> itemsPossedes;

    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.projectiles = new Array<>();
    }

    // Getters et setters pour x, y, width, et height
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // Méthode mise à jour pour la logique du joueur
    public void update(float delta) {
        // Applique la gravité si le joueur n'est pas au sol
        if (!onGround) {
            velocityY += gravity;
        } else {
            velocityY = 0;
        }

        // Met à jour la position Y en fonction de la vitesse verticale
        y += velocityY * delta;

        // Logique pour empêcher le joueur de sortir des limites définies (exemple)
        if (y < 0) {
            y = 0;
            onGround = true;
        }

        // Logique de mise à jour pour les projectiles tirés
        for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                // Déplacement à gauche
                setX(getX() - 10); // Ajuster la valeur de déplacement selon le besoin
                break;
            case Input.Keys.RIGHT:
                // Déplacement à droite
                setX(getX() + 10); // Ajuster la valeur de déplacement selon le besoin
                break;
            case Input.Keys.UP:
                // Sauter
                if (onGround) {
                    velocityY = 10; // Ajuster la valeur de saut selon le besoin
                    onGround = false;
                }
                break;
            case Input.Keys.SPACE:
                // Tirer un projectile
                shoot();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Aucune action spécifique pour keyUp dans cet exemple
        return false;
    }

    private void shoot() {
        // Crée un nouveau projectile et l'ajoute à la liste
        Projectile projectile = new Projectile(x + width / 2, y + height);
        projectiles.add(projectile);
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    // Une classe interne pour représenter les projectiles
    private static class Projectile {
        private float x;
        private float y;
        private float speed; // Vitesse du projectile

        public Projectile(float x, float y) {
            this.x = x;
            this.y = y;
            this.speed = 300; // Ajuster la vitesse du projectile selon le besoin
        }

        public void update(float delta) {
            // Met à jour la position du projectile
            y += speed * delta;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public static int getCoins() {
        return coins;
    }

    public static void ajouterItem(ItemShop item) {
        itemsPossedes.add(item);
        System.out.println("You've gained : " + item.getName());
    }
}
