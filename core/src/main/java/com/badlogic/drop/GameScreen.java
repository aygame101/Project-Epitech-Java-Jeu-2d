package com.badlogic.drop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private final Main game;             // Référence au jeu principal
    private TiledMap map;                    // Carte TiledMap
    private OrthogonalTiledMapRenderer mapRenderer;  // Renderer pour afficher la carte
    private OrthographicCamera camera;       // Caméra pour suivre la carte
    private Viewport viewport;               // Viewport pour ajuster la taille d'affichage

    public GameScreen(Main game) {
        this.game = game;

        // Chargement de la carte à partir du fichier TMX
        map = new TmxMapLoader().load("Test_Base_map.tmx"); // Assurez-vous que le fichier est dans le dossier assets
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f); // Facteur d'échelle pour les tuiles de 32x32 pixels

        // Initialisation de la caméra
        camera = new OrthographicCamera();
        viewport = new FitViewport(16, 9, camera);  // Dimensions ajustées pour une résolution de 16:9
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); // Centrer la caméra
    }

    @Override
    public void show() {
        // Préparer l'écran (par exemple, initialiser les éléments graphiques)
    }

    @Override
    public void render(float delta) {
        // Effacer l'écran pour éviter les artefacts de rendu
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mise à jour de la caméra et du renderer
        camera.update();
        mapRenderer.setView(camera);

        // Dessiner la carte
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
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
        map.dispose();
        mapRenderer.dispose();
    }
}

