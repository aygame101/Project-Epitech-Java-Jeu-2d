package com.badlogic.drop;

import com.badlogic.drop.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class VictoryScreen implements Screen {
    private final Main game;
    private Texture victoryImage;
    private SpriteBatch spriteBatch;

    public VictoryScreen(Main game) {
        this.game = game;

        // Charger l'image de victoire
        victoryImage = new Texture(Gdx.files.internal("Victory.png"));
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dessiner l'image de victoire
        spriteBatch.begin();
        spriteBatch.draw(victoryImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Méthode appelée lors du redimensionnement de l'écran
    }

    @Override
    public void pause() {
        // Méthode appelée lorsque le jeu est mis en pause
    }

    @Override
    public void resume() {
        // Méthode appelée lorsque le jeu reprend
    }

    @Override
    public void hide() {
        // Méthode appelée lorsque l'écran est masqué
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        victoryImage.dispose();
    }
}
