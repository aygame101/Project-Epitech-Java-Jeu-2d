package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class OptionsScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;

    // Add an array of window sizes
    private final int[][] windowSizes = {
        {640, 480},
        {960, 540},
        {1920, 1080}
    };
    private int currentSizeIndex = 0;

    public OptionsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        TextButton controlsButton = new TextButton("Controls", skin);
        controlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ControlsScreen(game));
            }
        });

        TextButton windowSizeButton = new TextButton("Window Size", skin);
        windowSizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change window size and iterate to next size
                currentSizeIndex = (currentSizeIndex + 1) % windowSizes.length;
                int width = windowSizes[currentSizeIndex][0];
                int height = windowSizes[currentSizeIndex][1];
                Gdx.graphics.setWindowedMode(width, height);
            }
        });

        TextButton volumeButton = new TextButton("Volume", skin);
        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Example action to adjust volume
                game.setVolume(0.5f); // Example volume level
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game)); // Go back to the main menu
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true); // Enable table debug lines (optional)
        stage.addActor(table);

        table.add(controlsButton).center().padBottom(10);
        table.row();
        table.add(windowSizeButton).center().padBottom(10);
        table.row();
        table.add(volumeButton).center().padBottom(10);
        table.row();
        table.add(backButton).center();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
