package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    private AssetManager assetManager;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private TileScan tileScan;
    private CollisionDetector collisionDetector;

    public Gameworld1() {
        System.out.println("Width: " + GameInfo.WIDTH + " Height: " + GameInfo.HEIGHT);


        // Initialize AssetManager
        assetManager = new AssetManager();

        // Queue assets for loading
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load("MAPS/Village.tmx", TiledMap.class);
        assetManager.load("Music/16bitRpgBGMUSIC.mp3", Music.class);

        assetManager.finishLoading();

        // Load map
        map = assetManager.get("MAPS/Village.tmx", TiledMap.class);
        spriteBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        // Load and play background music
        backgroundMusic = assetManager.get("Music/16bitRpgBGMUSIC.mp3", Music.class);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(GameInfo.getMusicVolume());
        backgroundMusic.play();

        // Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 400);
        camera.zoom = 1.0f;
        camera.update();
        viewport = new FitViewport(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, camera);


        // Create Box2D world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        collisionDetector = new CollisionDetector(world, map, List.of(6));

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

        if (Gdx.input.isKeyPressed(Input.Keys.valueOf(","))) {
            zoomIn();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("."))) {
            zoomOut();
        }

        // Update player position
        player.update(Gdx.graphics.getDeltaTime(), keyHandler);
        GameInfo.setPlayerX(player.getX());
        GameInfo.setPlayerY(player.getY());

        // Update camera
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Render map

        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.setView(camera.combined, camera.position.x - camera.viewportWidth, camera.position.y - camera.viewportHeight, camera.viewportWidth * 2, camera.viewportHeight * 2);
        tiledMapRenderer.render(new int[]{0, 1, 2, 3, 4,5,6,7,9,10,11,12,13,14,15,16,17});

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
        spriteBatch.dispose();
        world.dispose();
        debugRenderer.dispose();
        assetManager.dispose();
    }

    public void zoomIn() {
        camera.zoom -= 0.01f; // Decrease zoom level
        if (camera.zoom < 0.5f) {
            camera.zoom = 0.5f; // Minimum zoom level
        }
        camera.update();
    }

    public void zoomOut() {
        camera.zoom += 0.01f; // Increase zoom level
        if (camera.zoom > 2.0f) {
            camera.zoom = 2.0f; // Maximum zoom level
        }
        camera.update();
    }
}
