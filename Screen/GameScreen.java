package com.badlogic.drop.Screen;

import com.badlogic.drop.Main;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {
    private final Main game;

    public GameScreen(Main game) {
        this.game = game;
        // Initialiser la logique du jeu, charger la carte, créer le joueur, etc.
    }

    @Override
    public void show() {
        // Préparer l'écran (par exemple, initialiser les éléments graphiques)
    }

    @Override
    public void render(float delta) {
        // Nettoie l’écran
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Met à jour la logique de jeu, affiche le joueur, la carte, etc.
        game.batch.begin();
        // Exemples de rendu : afficher des éléments de jeu ici
        game.font.draw(game.batch, "Platformer 2D", 10, 20);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) { /* Gérer le redimensionnement ici */ }

    @Override
    public void pause() { /* Gérer la pause ici */ }

    @Override
    public void resume() { /* Gérer la reprise ici */ }

    @Override
    public void hide() { /* Actions lors de la transition */ }

    @Override
    public void dispose() {
        // Libérer les ressources spécifiques à cet écran
    }
}

