package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    private MenuScreen MenuScreen;
    private GameScreen GameScreen;
    public Skin skin;
    private AssetManager assetManager;

    @Override
    public void create() {
        // Initialise les ressources partagées
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.load("assets/skins/uiskin.json", Skin.class);
        assetManager.load("assets/skins/default.fnt", BitmapFont.class);
        assetManager.load("assets/skins/uiskin.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        font = new BitmapFont(); // Police de base, à personnaliser si besoin

        try {
            skin = assetManager.get("assets/skins/uiskin.json", Skin.class);
        } catch (Exception e) {
            Gdx.app.error("Main", "Error loading skin", e);
        }
        // Charger la première scène (écran de jeu)
        this.setScreen(new GameScreen(this));

        MenuScreen = new MenuScreen(this);
        com.badlogic.drop.OptionsScreen optionsScreen = new OptionsScreen(this);

        setScreen(MenuScreen);
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
        assetManager.dispose();
        if (skin != null) {
            skin.dispose();
            font.dispose();
        }

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
            /*case MENU:
                this.setScreen(new MenuScreen(this));
                break;
            case GAME_OVER:
                this.setScreen(new GameOverScreen(this));
                break;*/
        }
    }

    // Enum pour référencer les différents écrans du jeu
    public enum ScreenType {
        GAME, MENU, OPTIONS, GAME_OVER
    }
}
