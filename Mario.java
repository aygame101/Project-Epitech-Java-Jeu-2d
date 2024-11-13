package com.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Mario extends Game {
    // Le SpriteBatch et le BitmapFont sont ici pour être partagés entre les écrans
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        // Initialise les ressources partagées
        batch = new SpriteBatch();
        font = new BitmapFont(); // Police de base, à personnaliser si besoin

        // Charger la première scène (écran de jeu)
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        // Efface l'écran avant de dessiner chaque nouvelle image
        Gdx.gl.glClearColor(0, 0, 0, 1); // Couleur de fond (noir)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Appel de la méthode render() de l'écran actuel
        super.render();
    }

    @Override
    public void dispose() {
        // Libération des ressources globales
        batch.dispose();
        font.dispose();

        // Dispose l'écran actuel (appel automatique à screen.dispose())
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    // Optionnel : méthode pour changer facilement d’écran
    public void changeScreen(ScreenType screenType) {
        switch (screenType) {
            case GAME:
                this.setScreen(new GameScreen(this));
                break;
            case MENU:
                this.setScreen(new MenuScreen(this));
                break;
            case GAME_OVER:
                this.setScreen(new GameOverScreen(this));
                break;
        }
    }

    // Enum pour référencer les différents écrans du jeu
    public enum ScreenType {
        GAME, MENU, GAME_OVER
    }
}
