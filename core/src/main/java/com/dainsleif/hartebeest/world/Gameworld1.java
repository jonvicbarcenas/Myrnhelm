package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.dainsleif.hartebeest.enemies.EnemyGoblinGdxAi;
import com.dainsleif.hartebeest.enemies.Goblin;
import com.dainsleif.hartebeest.helpers.CursorStyle;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.players.PlayerMyron;
import com.dainsleif.hartebeest.screens.FpsStage;
import com.dainsleif.hartebeest.screens.PlayerStatStage;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.Arrays;

public class Gameworld1 implements Screen {
    TiledMap map;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Music backgroundMusic;
    FpsStage fpsStage;
    PlayerStatStage playerStatStage;
    CursorStyle cursorStyle;

    // Camera
    OrthographicCamera camera;

    private PlayerMyron player;
    KeyHandler keyHandler;

    // Enemy
    Goblin goblin;

    private final float mapWidth;
    private final float mapHeight;
    private AssetManager assetManager;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private CollisionDetector collisionDetector;
    private EnemyGoblinGdxAi goblinAi;

    ///-------------- sum variables for Class Usage ---------------///
    boolean isFlag = false;
    boolean isDamageApplied = false;
    boolean playerDamageApplied = false;

    public Gameworld1() {
        System.out.println("Width: " + GameInfo.WIDTH + " Height: " + GameInfo.HEIGHT);

        // Initialize AssetManager
        assetManager = new AssetManager();

        // Queue assets for loading
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load("MAPS/Forrest1.tmx", TiledMap.class);
        assetManager.load("Music/16bitRpgBGMUSIC.mp3", Music.class);

        assetManager.finishLoading();

        // Load map
        map = assetManager.get("MAPS/Forrest1.tmx", TiledMap.class);
        spriteBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        // Load and play background music
        backgroundMusic = assetManager.get("Music/16bitRpgBGMUSIC.mp3", Music.class);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(GameInfo.getMusicVolume());
        backgroundMusic.play();

        // Set cursor style
        cursorStyle = new CursorStyle();
        cursorStyle.changeCursorToHand();

        // Camera setup
        mapWidth = map.getProperties().get("width", Integer.class) *
            map.getProperties().get("tilewidth", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class) *
            map.getProperties().get("tileheight", Integer.class);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        camera.zoom = 1.0f;
        camera.update();
        viewport = new FitViewport(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT, camera);


        // Create Box2D world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        collisionDetector = new CollisionDetector(world, map, Arrays.asList(3, 4, 5, 6, 11));

        // Create player
        keyHandler = new KeyHandler(camera);
        Gdx.input.setInputProcessor(keyHandler);
        player = new PlayerMyron(world, collisionDetector);


        //create enemy and AI for enemy goblin
        goblin = new Goblin(new Vector2(505, 339), collisionDetector, world);
        goblinAi = new EnemyGoblinGdxAi(goblin, player);


        // Create stages
        fpsStage = new FpsStage();
        playerStatStage = new PlayerStatStage();

        System.out.println("Goblin Die Animation Time: " + goblin.getDieAnimationTime());
    }

    @Override
    public void show() {}

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        if (Gdx.input.isKeyPressed(Input.Keys.valueOf(","))) {
            keyHandler.zoomIn();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("."))) {
            keyHandler.zoomOut();
        }

        // Update player position
        player.update(Gdx.graphics.getDeltaTime(), keyHandler);
        GameInfo.setPlayerX(player.getX());
        GameInfo.setPlayerY(player.getY());

        // Update camera
        camera.position.set(player.getX(), player.getY(), 0);

        // Constrain camera to map boundaries
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        // Clamp camera position
        camera.position.x = Math.min(Math.max(camera.position.x, effectiveViewportWidth/2),
            mapWidth - effectiveViewportWidth/2);
        camera.position.y = Math.min(Math.max(camera.position.y, effectiveViewportHeight/2),
            mapHeight - effectiveViewportHeight/2);

        camera.update();

        // Render map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(new int[]{0, 1, 2, 3, 4,5,6,7,9,11,12,13});
        // Render player with camera
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        player.draw(spriteBatch, 1);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.playerAttack(goblin, player);
        } else {
            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                playerDamageApplied = false;
            }
        }


        // Render enemy and state
        if (goblin != null) {
            // Check if goblin should be removed
            if (goblin.isShouldRemove()) {
                // Remove from physics world
                world.destroyBody(goblin.getBody());

                // Dispose resources
                goblin.dispose();

                // Set to null to indicate it's been removed
                goblin = null;
                goblinAi = null;
            } else {
                // Only update and draw if the goblin shouldn't be removed
                goblin.update();
                goblin.draw(spriteBatch, Gdx.graphics.getDeltaTime());
                goblin.goblinDamage(player);

                // Update enemy AI
                if (goblinAi != null) {
                    goblinAi.update(Gdx.graphics.getDeltaTime());
                }
            }
        }

        spriteBatch.end();

        tiledMapRenderer.render(new int[]{8, 10, 14});

        if(GameInfo.getShowBlockedTiles()){
            debugRenderer.render(world, camera.combined);
        }

        // Draw Stages
        fpsStage.update(v);
        fpsStage.draw();
        playerStatStage.draw();
        playerStatStage.update(v);

        world.step(1 / 60f, 6, 2);

    }




    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        fpsStage.dispose();
        playerStatStage.dispose();
        backgroundMusic.dispose();
        goblin.dispose();
    }
}
