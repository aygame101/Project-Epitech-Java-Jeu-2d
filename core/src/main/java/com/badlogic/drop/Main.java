
package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
    // Le SpriteBatch et le BitmapFont sont ici pour être partagés entre les écrans
    public SpriteBatch batch;
    public BitmapFont font;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private OptionsScreen optionsScreen;
    Skin skin;
    private AssetManager assetManager;

    @Override
    public void create() {
        // Initialise les ressources partagées
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        // Efface l'écran avant de dessiner chaque nouvelle image
        Gdx.gl.glClearColor(0, 0, 0, 1); // Couleur de fond (noir)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Appel de la méthode render() de l'écran actuel
        super.render();
        handleInput();
    }

    @Override
    public void dispose() {
        // Libération des ressources globales
        batch.dispose();
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (skin != null) {
            skin.dispose();
            font.dispose();
        }


        // Dispose l'écran actuel (appel automatique à screen.dispose())
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    // Méthode pour changer d’écran
    public void changeScreen(ScreenType screenType) {
        switch (screenType) {
            case GAME:
                this.setScreen(gameScreen);
                break;
            case MENU:
                this.setScreen(menuScreen);
                break;
            case OPTIONS:
                this.setScreen(optionsScreen);
                break;
            case GAMEOVER:
                this.setScreen(new GameOverScreen(this));
                break;
        }
    }

    public void startGame() {
        this.setScreen(new GameScreen(this));
    }

    public void showOptions() {
        this.setScreen(new OptionsScreen(this));
    }

    public void showGameOver() {
        this.setScreen(new GameOverScreen(this));
    }

    public float getVolume() {
        return 50;
    }

    public void setVolume(float value) {
    }

    public void saveCommand(String command) {
    }

    // Enum pour référencer les différents écrans du jeu
    public enum ScreenType {
        GAME, MENU, OPTIONS, GAMEOVER
    }

    private void handleInput() {
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                this.setScreen(new PauseScreen(this));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                this.setScreen(new GameOverScreen(this));
            }
    }
}
