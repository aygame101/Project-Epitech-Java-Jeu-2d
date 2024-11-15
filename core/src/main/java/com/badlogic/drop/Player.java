package com.badlogic.drop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player extends InputAdapter {

    private static float x;  // Position initiale en X
    private static float y;  // Position initiale en Y
    private float velocityY = 0;  // Vitesse verticale du joueur
    private float gravity = -0.5f;  // Force de gravité, ajustez selon vos besoins
    private boolean onGround = true;  // Indicateur si le joueur est au sol

    private boolean leftMove = false;
    private boolean rightMove = false;
    private boolean jumpMove = false;
    private static boolean dashMove = false;
    private boolean shoot = false;
    private static float dashDuration = 0.5f; // Durée du dash en secondes
    private static float dashSpeed = 20f; // Vitesse du dash
    private static float dashTimeLeft = 0f; // Temps restant du dash

    private Array<Projectiles> projectiles;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        projectiles = new Array<Projectiles>();
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }


    public static float getX() {
        return x;
    }

    public void setX(float x) {
        Player.x = x;
    }

    public static float getY() {
        return y;
    }

    public void setY(float y) {
        Player.y = y;
    }

    public boolean isLeftMove() {
        return leftMove;
    }

    public void setLeftMove(boolean leftMove) {
        this.leftMove = leftMove;
    }

    public boolean isRightMove() {
        return rightMove;
    }

    public void setRightMove(boolean rightMove) {
        this.rightMove = rightMove;
    }

    public boolean isJumpMove() {
        return jumpMove;
    }

    public void setJumpMove(boolean jumpMove) {
        this.jumpMove = jumpMove;
    }

    public static boolean isDashMove() {
        return dashMove;
    }

    public static void setDashMove(boolean dashMove) {
        Player.dashMove = dashMove;
    }

    public boolean isShoot() {
        return shoot;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public Array<Projectiles> getProjectiles() {
        return projectiles;
    }

    public void update(float delta) {
        // Appliquer la gravité
        if (!onGround) {
            velocityY += gravity;
        }

        // Appliquer la vitesse verticale à la position Y
        y += velocityY * delta;

        // Empêcher le joueur de descendre en dessous de la "terre"
        if (y <= 5) {  // Remplacez "5" par la hauteur du sol appropriée
            y = 5;
            onGround = true;
            velocityY = 0;
        }

        // Gestion du mouvement latéral
        if (leftMove) {
            x -= 5 * delta;
        }
        if (rightMove) {
            x += 5 * delta;
        }
        //Gestion des projectiles
        for (Projectiles p : projectiles) {
            p.update(delta);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                setLeftMove(true);
                break;
            case Input.Keys.RIGHT:
                setRightMove(true);
                break;
            case Input.Keys.UP:
                // Autoriser le saut uniquement si le joueur est au sol
                if (onGround) {
                    setJumpMove(true);
                    velocityY = 10;  // Force de saut, ajustez selon vos besoins
                    onGround = false;
                }
                break;
            case Input.Keys.SPACE:
                if (!isDashMove()) {
                    setDashMove(true);  // Commencer le dash
                    dashTimeLeft = dashDuration;  // Réinitialiser le temps du dash
                }
            case Input.Keys.DOWN:
                setShoot(true);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                setLeftMove(false);
                break;
            case Input.Keys.RIGHT:
                setRightMove(false);
                break;
            case Input.Keys.UP:
                setJumpMove(false);
                break;
            case Input.Keys.SPACE:
                setDashMove(false);
                break;
             case Input.Keys.DOWN:
                 setShoot(false);
                 break;
            default:
                return false;
        }
        return true;
    }

    private void shoot() {
        // Créer un projectile et l'ajouter à la liste
        float projectileSpeed = 10f;  // Vitesse du projectile
        float projectileY = y + 0.5f;  // La position Y du projectile (juste devant le joueur)

        // Si le joueur se déplace à droite
        if (rightMove) {
            projectiles.add(new Projectiles(x + 1, projectileY, projectileSpeed));
        }
        // Si le joueur se déplace à gauche
        else if (leftMove) {
            projectiles.add(new Projectiles(x - 1, projectileY, -projectileSpeed));
        }
        // Si le joueur n'est pas en mouvement horizontal, tirer droit devant
        else {
            projectiles.add(new Projectiles(x, projectileY, projectileSpeed));
        }
    }

}

