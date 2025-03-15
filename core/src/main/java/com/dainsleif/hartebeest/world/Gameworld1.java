package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.TileScan;
import com.dainsleif.hartebeest.utils.CollisionDetector;
import com.dainsleif.hartebeest.utils.Player;

import java.util.List;

public class Gameworld1 implements Screen {
    TiledMap map;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Music backgroundMusic;

    // Camera
    OrthographicCamera camera;

    Player player;
    KeyHandler keyHandler;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private TileScan tileScan;
    private CollisionDetector collisionDetector;

    public Gameworld1() {
        System.out.println("Width: " + GameInfo.WIDTH + " Height: " + GameInfo.HEIGHT);

        // Load map
        map = new TmxMapLoader().load("MAPS/Village.tmx");
        spriteBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        // Load and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/16bitRpgBGMUSIC.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(GameInfo.getMusicVolume());
        backgroundMusic.play();

        // Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 400);
        camera.update();
        viewport = new FitViewport(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, camera);


        // Create Box2D world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        collisionDetector = new CollisionDetector(world, map, List.of());

        // Create player
        keyHandler = new KeyHandler();
        Gdx.input.setInputProcessor(keyHandler);
        player = new Player(world, "sprite/player/walk.png", "sprite/player/walk.json", collisionDetector);


        tileScan = new TileScan(world, map);

    }

    @Override
    public void show() {}

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Update player position
        player.update(Gdx.graphics.getDeltaTime(), keyHandler);
        GameInfo.setPlayerX(player.getX());
        GameInfo.setPlayerY(player.getY());

        // Update camera
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Render map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(new int[]{0, 1, 2, 3, 4,5,6,7,9,10,11});

        // Render player with camera
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        player.draw(spriteBatch, 1);
        spriteBatch.end();
        if(GameInfo.getShowBlockedTiles()){
            debugRenderer.render(world, camera.combined);
        }
        world.step(1 / 60f, 6, 2);

        tiledMapRenderer.render(new int[]{8});

//        int currentTileId = tileScan.getTileIdAtPlayer(player.getX(), player.getY(), 8);
//        if (currentTileId != -1) {
//            System.out.println("Player is standing on tile ID: " + currentTileId);
//
//            if(!tileScan.isTileBlocked(GameInfo.getPlayerX(), GameInfo.getPlayerY(), 8)) {
//                System.out.println("Tile is blocked");
//            }
//        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        tiledMapRenderer.dispose();
        spriteBatch.dispose();
        backgroundMusic.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}
