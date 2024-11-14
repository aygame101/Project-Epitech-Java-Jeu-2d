package com.badlogic.drop;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {
    private final Main game;      // Référence à la classe principale
    private Stage stage;              // Scène pour gérer les éléments de l'interface
    private Skin skin;                // Skin pour les boutons et autres widgets

    public MenuScreen(Main game) {
        this.game = game;

        // Crée une nouvelle scène avec un viewport pour le menu
        stage = new Stage(new ScreenViewport());

        // Charge une skin pour les boutons (par exemple, "uiskin.json" dans les assets)
        skin = new Skin(Gdx.files.internal("MenuSkin.json"));

        // Ajoute les boutons au menu
        createMenu();
    }

    private void createMenu() {
        // Table pour organiser les boutons verticalement
        Table table = new Table();
        table.setFillParent(true);  // Table occupe toute la scène
        stage.addActor(table);

        // Bouton "Play"
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.changeScreen(Main.ScreenType.GAME); // Lance le jeu
            }
        });

        // Bouton "Options" (à personnaliser avec vos options)
        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Ajouter les actions pour accéder aux options
            }
        });

        // Bouton "Quitter"
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();  // Ferme l'application
            }
        });

        // Ajouter les boutons dans la table (centrés verticalement)
        table.add(playButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);  // Espacement entre les boutons
        table.add(optionsButton).fillX().uniformX();
        table.row();
        table.add(quitButton).fillX().uniformX();
    }

    @Override
    public void show() {
        // Définit le stage comme processeur d'entrée
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Efface l'écran
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dessine la scène
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {

    }
}
