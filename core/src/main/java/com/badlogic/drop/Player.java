package com.badlogic.drop;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public class Player extends InputAdapter {
    public static final float DAMPING = 0.87f; // Facteur d'amortissement
    //donnée de base du joueur
    private float x;
    private float y;
    private float width;
    private float height;
    private float velocityY = 0;
    private float velocityX = 0;
    private float gravity = -2f;
    private boolean onGround = true;
    private Vector2 velocity;
    private Array<Projectile> projectiles;
    public float stateTime;
    //animation (non fonctionelle)
    private boolean facesRight = true;
    private enum State { Standing, Walking, Jumping }
    private State state = State.Standing;
    //les piece
    private int coinCount = 0;
    private static int coinN = 0;
    //Shop (inutiliser pour le moment)
    private static ArrayList<ItemShop> itemsPossedes;
    //Les items
    private static boolean longJump;

    //initialise le player
    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = new Vector2(0, 0);
        this.projectiles = new Array<>();
    }

    //les constructeur
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, width, height);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

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

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
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

    public void addCoin() {
        coinCount++;
    }
    public static int getCoin() {
        return coinN;
    }

    public int getCoinCount() {
        coinN = coinCount;
        return coinCount;
    }

    public static boolean GotLongJump(){
        return longJump;
    }

    public void setLongJump(boolean longJump) {
        this.longJump = longJump;
    }


    //constructeur inutiliser pour le moment
    public static void ajouterItem(ItemShop item) {
        itemsPossedes.add(item);
    }

    public float getGravity() {
        return gravity;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }


    //gere la hitbox du perso
    public static class BoundingBox {
        float x;
        float y;
        private float width;
        private float height;

        public BoundingBox(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width - 4;
            this.height = height - 4;
        }

        public boolean overlaps(Rectangle other) {
            return x < other.x + other.width && x + width > other.x &&
                y < other.y + other.height && y + height > other.y;
        }

        public Rectangle toRectangle() {
            return new Rectangle(x, y, width, height);
        }
    }

    //gere les projectiles (inutiliser)
    private static class Projectile {
        private float x;
        private float y;
        private float speed;

        public Projectile(float x, float y) {
            this.x = x;
            this.y = y;
            this.speed = 5f;
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
}
