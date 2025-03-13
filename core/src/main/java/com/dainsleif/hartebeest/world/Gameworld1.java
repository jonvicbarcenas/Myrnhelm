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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.utils.Player;

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

    public Gameworld1() {
        System.out.println("Width: " + GameInfo.WIDTH + " Height: " + GameInfo.HEIGHT);

        // Load map
        map = new TmxMapLoader().load("Fan-Tasy/newMap.tmx");
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

        // Create player
        player = new Player("sprite/walk/walk.atlas", GameInfo.getPlayerX(), GameInfo.getPlayerY());
        keyHandler = new KeyHandler();


    }

    @Override
    public void show() {}

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        player.update(Gdx.graphics.getDeltaTime(), keyHandler);

        // Get map dimensions
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);

        // Calculate camera position
        float cameraX = Math.max(camera.viewportWidth / 2, Math.min(player.getX() + player.getWidth() / 2, mapWidth - camera.viewportWidth / 2));
        float cameraY = Math.max(camera.viewportHeight / 2, Math.min(player.getY() + player.getHeight() / 2, mapHeight - camera.viewportHeight / 2));

        // Update camera
        camera.position.set(cameraX, cameraY, 0);
        camera.update();

        // Render map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(new int[]{0, 1, 2, 3, 4,5,7,8});

        // Render player with camera
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        player.draw(spriteBatch);
        spriteBatch.end();

        tiledMapRenderer.render(new int[]{6});
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
    }
}
