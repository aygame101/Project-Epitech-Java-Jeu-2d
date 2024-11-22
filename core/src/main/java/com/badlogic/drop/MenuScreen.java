package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {
    private Stage stage;
    private Main game;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;

    public MenuScreen(Main game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Initialisation du niveau
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Chargement du background
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Titre du jeu + modif taille
        Label titleLabel = new Label("Kwa-FEUR", skin, "default");

        float scaleX = 2f; // Modifier cette valeur pour ajuster la largeur
        float scaleY = 3f; // Modifier cette valeur pour ajuster la hauteur
        titleLabel.setFontScale(scaleX, scaleY);

        // Création des bouttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        // Ecoute de click
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.startGame();
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.showOptions();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Création du menu
        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).center().padBottom(20);
        table.row();
        table.add(playButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(optionsButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();

        stage.addActor(table);
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
