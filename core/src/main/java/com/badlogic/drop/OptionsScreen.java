package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionsScreen implements Screen {
    private Stage stage;
    private Main game;
    private Skin skin;

    public OptionsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Window size option
        Label windowSizeLabel = new Label("Window Size", skin);
        Slider windowSizeSlider = new Slider(800, 1920, 100, false, skin);
        windowSizeSlider.setValue(Gdx.graphics.getWidth());

        // Volume option
        Label volumeLabel = new Label("Volume", skin);
        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setValue(50); // Example initial value

        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(event -> {
            game.setScreen(new MenuScreen(game));
            return true;
        });

        table.add(windowSizeLabel).colspan(2).center();
        table.row().pad(10, 0, 10, 0);
        table.add(windowSizeSlider).colspan(2).fillX().uniformX();
        table.row();
        table.add(volumeLabel).colspan(2).center();
        table.row().pad(10, 0, 10, 0);
        table.add(volumeSlider).colspan(2).fillX().uniformX();
        table.row();
        table.add(backButton).colspan(2).center();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
    }
}
