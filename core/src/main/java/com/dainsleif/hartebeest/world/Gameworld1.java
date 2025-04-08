package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.dainsleif.hartebeest.enemies.GoblinSpawner;
import com.dainsleif.hartebeest.helpers.CursorStyle;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.npc.AnkarosTheNPC;
import com.dainsleif.hartebeest.npc.NPC;
import com.dainsleif.hartebeest.npc.NpcHandler;
import com.dainsleif.hartebeest.players.PlayerMyron;
import com.dainsleif.hartebeest.players.PlayerSwitcher;
import com.dainsleif.hartebeest.quests.Quest;
import com.dainsleif.hartebeest.quests.QuestHandler;
import com.dainsleif.hartebeest.screens.*;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.Arrays;

public class Gameworld1 implements Screen {
    TiledMap map;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    // Stages
    FpsStage fpsStage;
    DeathStage deathStage;
    PlayerStatStage playerStatStage;
    DialogueStage dialogueStage;
    CursorStyle cursorStyle;

    // Camera
    OrthographicCamera camera;

    NpcHandler npcHandler;
    PlayerSwitcher playerSwitcher;
    KeyHandler keyHandler;
    QuestHandler questHandler;

    // Enemy
    private final float mapWidth;
    private final float mapHeight;
    private AssetManager assetManager;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private CollisionDetector collisionDetector;

    ///-------------- sum variables for Class Usage ---------------///
    boolean playerDamageApplied = false;
    private boolean transitioning = false;

    private Screen nextScreen = null;
    private final TransitionMapHandler transitionHandler;
    private final GoblinSpawner goblinSpawner;
    private final GoblinHealthBarStage goblinHealthBarStage;

    public Gameworld1() {
        System.out.println("Width: " + GameInfo.WIDTH + " Height: " + GameInfo.HEIGHT);

        // Initialize AssetManager
        assetManager = new AssetManager();

        // Queue assets for loading
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load("MAPS/Forrest1.tmx", TiledMap.class);

        assetManager.finishLoading();

        // Load map
        map = assetManager.get("MAPS/Forrest1.tmx", TiledMap.class);
        spriteBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);


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

        collisionDetector = new CollisionDetector(world, map, Arrays.asList(3, 4, 5, 6, 10, 11) , "blocked");

        // Create player
        keyHandler = new KeyHandler(camera);
        Gdx.input.setInputProcessor(keyHandler);
        playerSwitcher = new PlayerSwitcher(world, collisionDetector);
        playerSwitcher.setPosition(118, 32);

        transitionHandler = new TransitionMapHandler(map, "transitions", 15, "StartAreaMap");


        // Create quest handler
        questHandler = new QuestHandler();

        // Create goblin
        goblinSpawner = new GoblinSpawner(world, collisionDetector, playerSwitcher);
        goblinSpawner.spawnGoblins(new Vector2(356, 381), 15, 30f);
        goblinHealthBarStage = new GoblinHealthBarStage(goblinSpawner);
        goblinSpawner.setQuestHandler(questHandler);
        playerSwitcher.getCurrentPlayer().setGoblinSpawner(goblinSpawner);

        npcHandler = new NpcHandler(world, new Vector2(122, 105));

        // Connect questHandler to NPCs
        for (NPC npc : npcHandler.getNpcs()) {
            if (npc instanceof AnkarosTheNPC) {
                ((AnkarosTheNPC) npc).setQuestHandler(questHandler);
            }
        }

        // Create stagesa
        fpsStage = new FpsStage();
        playerStatStage = new PlayerStatStage();
        deathStage = new DeathStage();
        dialogueStage = new DialogueStage();
        MusicGameSingleton.getInstance().play();


    }

    @Override
    public void show() {}

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        npcHandler.update(v, playerSwitcher.getCurrentPlayer());

        if (transitioning) {
            return;
        }

        // Update player position
        playerSwitcher.update(Gdx.graphics.getDeltaTime(), keyHandler);
        keyHandler.update(Gdx.graphics.getDeltaTime());
        GameInfo.setPlayerX(playerSwitcher.getX());
        GameInfo.setPlayerY(playerSwitcher.getY());

        // Check for transitions
        checkTransitions();
        dialogueStage.update(v);


        // If transition was triggered, execute it and return
        if (transitioning) {
            executeTransition();
            return;
        }

        // Update camera
        camera.position.set(playerSwitcher.getX(), playerSwitcher.getY(), 0);

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
        tiledMapRenderer.render(new int[]{0, 1, 2, 3, 4,5,6,7,8,9,11,12,13});
        // Render player with camera
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        if (!playerSwitcher.isDead()) {
            playerSwitcher.draw(spriteBatch, 1);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            goblinSpawner.checkPlayerAttack();
        }else {
            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                playerDamageApplied = false;
            }
        }

        goblinSpawner.update(Gdx.graphics.getDeltaTime());
        goblinSpawner.draw(spriteBatch, Gdx.graphics.getDeltaTime());
        if(!playerSwitcher.isDead()){
            goblinSpawner.checkDamageToPlayer();
        }

        npcHandler.draw(spriteBatch);



        handleInput();
        if (dialogueStage.isDialogueVisible()) {
            dialogueStage.render(spriteBatch, camera);
        }


        spriteBatch.end();

        if (PlayerMyron.getHealth() <= 0 && !playerSwitcher.isDead()) {
            playerSwitcher.setDead(true);

        }

        tiledMapRenderer.render(new int[]{8,10});

        if (playerSwitcher.isDead()) {
            deathStage.update(v);
            deathStage.draw();


            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                playerSwitcher.setDead(false);
                playerSwitcher.setHealth(1500);
                playerSwitcher.setPosition(118, 32);
            }
            return;
        }


        // Update the health bars
        goblinHealthBarStage.update(Gdx.graphics.getDeltaTime());
        goblinHealthBarStage.render(camera);



        if(GameInfo.getShowDebugging()){
            debugRenderer.render(world, camera.combined);
        }

        // Draw Stages
        fpsStage.update(v);
        fpsStage.draw();
        playerStatStage.draw();
        playerStatStage.update(v);

        world.step(1 / 60f, 6, 2);
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Vector2 playerPos = playerSwitcher.getCurrentPlayer().getPosition();

            for (NPC npc : npcHandler.getNpcs()) {
                if (npc.isPlayerInRange(playerPos)) {
                    npc.interact();

                    String dialogueText = "";


                    if (npc instanceof AnkarosTheNPC) {
                        AnkarosTheNPC ankaros = (AnkarosTheNPC) npc;
                        String questName = ankaros.getAssignedQuestName();
                        int questId = questHandler.getQuestIdByName(questName);
                        Quest quest = questHandler.getQuestById(questId);

                        if (quest != null) {
                            if (quest.status.equals("not_started") || quest.status.equals("in_progress")) {
                                dialogueText = quest.name + ": " + quest.description;

                                if (quest.status.equals("in_progress")) {
                                    dialogueText += "\n" + questHandler.getQuestProgressText(questId);
                                }
                            } else if (quest.status.equals("completed")) {
                                dialogueText = "Thank you for completing the " + quest.name + "!";
                                ankaros.stopFollowing();
                            }
                        } else {
                            dialogueText = "Greetings, traveler.";
                        }

                        dialogueStage.showDialogue(npc, dialogueText);
                    }

                    break;
                }
            }
        }
    }

    // Replace checkTransitions() method with:
    private void checkTransitions() {
        float playerX = playerSwitcher.getX();
        float playerY = playerSwitcher.getY();

        Screen targetScreen = transitionHandler.checkTransitions(playerX, playerY);
        if (targetScreen != null) {
            nextScreen = targetScreen;
            transitioning = true;
        }
    }

    // Replace executeTransition() method with:
    private void executeTransition() {
        transitionHandler.executeTransition(this, nextScreen, MusicGameSingleton.getInstance().getBackgroundMusic());
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
        goblinSpawner.dispose();
        goblinHealthBarStage.dispose();
        deathStage.dispose();
        tiledMapRenderer.dispose();
        map.dispose();
    }

}
