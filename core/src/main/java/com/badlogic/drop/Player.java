package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Player extends InputAdapter {

    private float x;        // Position en X
    private float y;        // Position en Y
    private float width;    // Largeur du joueur
    private float height;   // Hauteur du joueur
    private static int coins = 0;

    private float velocityY = 0;   // Vitesse verticale
    private float gravity = -2f;   // Force de gravité
    private boolean onGround = true;  // Indicateur si le joueur est au sol
    private Vector2 velocity;

    private Array<Projectile> projectiles; // Liste des projectiles tirés par le joueur
    private static ArrayList<ItemShop> itemsPossedes;

    private Rectangle boundingBox;

    private Texture koalaTexture;          // Texture pour le joueur
    private Animation<TextureRegion> stand, walk, jump;
    private float stateTime;
    private boolean facesRight = true;

    private enum State {
        Standing, Walking, Jumping
    }

    private State state = State.Standing;

    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.projectiles = new Array<>();
        this.itemsPossedes = new ArrayList<>();
        this.boundingBox = new Rectangle(x, y, width, height);

        this.koalaTexture = new Texture("Player.png");
        TextureRegion[] regions = TextureRegion.split(koalaTexture, 18, 26)[0];
        stand = new Animation<>(0, regions[0]);
        jump = new Animation<>(0, regions[1]);
        walk = new Animation<>(0.15f, regions[2], regions[3], regions[4]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.velocity = new Vector2();
        this.stateTime = 0;
    }

    // Getters et setters pour x, y, width, et height
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        this.boundingBox.setX(x);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        this.boundingBox.setY(y);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.boundingBox.setWidth(width);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        this.boundingBox.setHeight(height);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public static int getCoins() {
        return coins;
    }

    public static void ajouterItem(ItemShop item) {
        itemsPossedes.add(item);
    }

    // Méthode mise à jour pour la logique du joueur
    public void update(float delta) {
        stateTime += delta;

        if (delta > 0.1f){
            delta = 0.1f;
        }

        // Update the velocity and position
        if (!onGround) {
            velocityY += gravity * delta;
        }
        y += velocityY * delta;

        if (onGround) {
            velocityY = 0;
            y = Math.max(y, 0); // Prevent sinking below ground level
            if (state != State.Walking) {
                state = State.Standing;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -Koala.MAX_VELOCITY;
            if (onGround) state = State.Walking;
            facesRight = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = Koala.MAX_VELOCITY;
            if (onGround) state = State.Walking;
            facesRight = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            velocityY = Koala.JUMP_VELOCITY;
            state = State.Jumping;
            onGround = false;
        }

        x += velocity.x * delta;
        y += velocityY * delta;

        boundingBox.setPosition(x, y);
        checkForCollisions();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shoot();
        }

        // Apply damping to the velocity on the x-axis
        velocity.x *= Koala.DAMPING;
    }

    private void checkForCollisions() {
        // Votre logique de détection de collision
    }

    private void shoot() {
        // Create a new projectile and add it to the projectiles array
        projectiles.add(new Projectile(x, y));
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                velocity.x = -Koala.MAX_VELOCITY;
                if (onGround) state = State.Walking;
                facesRight = false;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                velocity.x = Koala.MAX_VELOCITY;
                if (onGround) state = State.Walking;
                facesRight = true;
                break;
            case Input.Keys.UP:
            case Input.Keys.W:
            case Input.Keys.SPACE:
                if (onGround) {
                    velocityY = Koala.JUMP_VELOCITY;
                    state = State.Jumping;
                    onGround = false;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
            case Input.Keys.A:
            case Input.Keys.D:
                velocity.x = 0;
                if (onGround) state = State.Standing;
                break;
        }
        return true;
    }

    public void render(Batch batch) {
        TextureRegion frame;
        switch (state) {
            case Standing:
                frame = stand.getKeyFrame(stateTime);
                break;
            case Walking:
                frame = walk.getKeyFrame(stateTime);
                break;
            case Jumping:
                frame = jump.getKeyFrame(stateTime);
                break;
            default:
                frame = stand.getKeyFrame(stateTime);
        }

        if (facesRight) {
            batch.draw(frame, x, y, width, height);
        } else {
            batch.draw(frame, x + width, y, -width, height);
        }
    }

    // Une classe interne pour représenter les projectiles
    private static class Projectile {
        private float x;
        private float y;
        private float speed; // Vitesse du projectile

        public Projectile(float x, float y) {
            this.x = x;
            this.y = y;
            this.speed = 10; // ou toute autre vitesse appropriée
        }

        public void update(float delta) {
            x += speed * delta;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    // Les valeurs provenant de Koala
    private static class Koala {
        static final float WIDTH = 18 / 16f;
        static final float HEIGHT = 26 / 16f;
        static final float MAX_VELOCITY = 10f;
        static final float JUMP_VELOCITY = 40f;
        static final float DAMPING = 0.87f;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
