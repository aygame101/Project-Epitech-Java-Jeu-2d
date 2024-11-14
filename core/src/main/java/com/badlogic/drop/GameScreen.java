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

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;


public class GameScreen implements Screen {

    private final Main game;             // Référence au jeu principal
    private TiledMap map;                    // Carte TiledMap
    private OrthogonalTiledMapRenderer mapRenderer;  // Renderer pour afficher la carte
    private OrthographicCamera camera;       // Caméra pour suivre la carte
    private Viewport viewport;               // Viewport pour ajuster la taille d'affichage

    private static final int TILE_SIZE = 16;   // Taille en pixels d'une tuile

    private Rectangle currentRoomBounds;

    public GameScreen(Main game) {
        this.game = game;

        // Chargement de la carte à partir du fichier TMX
        map = new TmxMapLoader().load("Game_Map_complete.tmx");  // Charge la carte
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Initialise la caméra
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        // Charge la salle de départ (exemple avec une salle aux coordonnées 0, 0)
        moveToRoom(0, 0);
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

    public void moveToRoom(int roomX, int roomY) {
        // Récupère la salle par ses coordonnées et charge ses dimensions
        currentRoomBounds = getRoomBounds(roomX, roomY);

        // Ajuste la taille de la caméra pour correspondre à la taille de la salle
        camera.viewportWidth = currentRoomBounds.width;
        camera.viewportHeight = currentRoomBounds.height;

        // Centre la caméra au milieu de la salle
        camera.position.set(
            currentRoomBounds.x + currentRoomBounds.width / 2,
            currentRoomBounds.y + currentRoomBounds.height / 2,
            0
        );
    }

    private Rectangle getRoomBounds(int roomX, int roomY) {
        // Accède aux propriétés d'objet de la salle actuelle dans le fichier `.tmx`
        // Ici, on cherche un objet dans le calque "Rooms" avec les propriétés `roomX` et `roomY`

        MapLayer roomsLayer = map.getLayers().get("Rooms"); // Assurez-vous que le calque est nommé "Rooms"

        for (MapObject object : roomsLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject roomObject = (RectangleMapObject) object;

                // Vérifie les propriétés `roomX` et `roomY`
                if ((int) roomObject.getProperties().get("roomX") == roomX &&
                    (int) roomObject.getProperties().get("roomY") == roomY) {

                    // Retourne les limites de la salle actuelle
                    return roomObject.getRectangle();
                }
            }
        }

        // Valeur par défaut si la salle n’est pas trouvée (peut être remplacée par un message d'erreur)
        return new Rectangle(0, 0, 640, 480); // Par exemple, une salle de 640x480 pixels
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

