@Override
public void show() {
    map = new TmxMapLoader().load("Game_Map_complete.tmx");
    mapRenderer = new OrthogonalTiledMapRenderer(map);
    camera = new OrthographicCamera();
    viewport = new FitViewport(800, 600, camera);

    // Initialiser à la première room
    roomsLayer = map.getLayers().get("Rooms 1");
    moveToRoom(0, 0); // Déplacer vers la première room (0, 0)
}