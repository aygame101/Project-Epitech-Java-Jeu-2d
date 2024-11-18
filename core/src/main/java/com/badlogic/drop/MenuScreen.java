package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {
    private Stage stage;
    private Main game;

    public MenuScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(event -> {
            game.setScreen(new GameScreen(game));
            return true;
        });

        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(event -> {
            game.setScreen(new OptionsScreen(game));
            return true;
        });

        TextButton controlsButton = new TextButton("Controls", skin);
        controlsButton.addListener(event -> {
            game.setScreen(new ControlsScreen(game));
            return true;
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(event -> {
            Gdx.app.exit();
            return true;
        });

        table.add(playButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(optionsButton).fillX().uniformX();
        table.row();
        table.add(controlsButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
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
