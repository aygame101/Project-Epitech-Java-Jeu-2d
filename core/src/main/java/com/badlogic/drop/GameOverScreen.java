package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {

    private final Main game;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Texture gameOverTexture;

    public GameOverScreen(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        gameOverTexture = new Texture(Gdx.files.internal("gameover.png"));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(gameOverTexture, Gdx.graphics.getWidth() / 2 - gameOverTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOverTexture.getHeight() / 2);
        font.draw(spriteBatch, "Game Over", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 50);
        font.draw(spriteBatch, "Press SPACE to Retry", Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 - 50);
        spriteBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game)); // Retourner au jeu principal
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        gameOverTexture.dispose();
    }
}
