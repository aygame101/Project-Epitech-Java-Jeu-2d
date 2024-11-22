package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class ControlsScreen implements Screen {
    private Stage stage;
    private Main game;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;

    public ControlsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Chargement du background
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Création du titre du menu
        Label titleLabel = new Label("Options", skin, "default");
        titleLabel.setFontScale(2); // Optionally scale the font

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Menu des commandes
        Label controlsLabel = new Label("Controls", skin);
        Label moveLabel = new Label("Move: WASD / Arrow Keys", skin);
        Label jumpLabel = new Label("Jump: Space", skin);

        // Boutton retour
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionsScreen(game));
            }
        });

        table.add(titleLabel).center().padBottom(20);
        table.row();
        table.add(controlsLabel).colspan(2).center();
        table.row().pad(10, 0, 10, 0);
        table.add(moveLabel).colspan(2).center();
        table.row();
        table.add(jumpLabel).colspan(2).center();
        table.row().pad(10, 0, 10, 0);
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
        skin.dispose();
        backgroundTexture.dispose();
    }
}
