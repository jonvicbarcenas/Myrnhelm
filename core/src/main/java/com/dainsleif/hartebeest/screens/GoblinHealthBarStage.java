package com.dainsleif.hartebeest.screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dainsleif.hartebeest.enemies.Goblin;
import com.dainsleif.hartebeest.enemies.GoblinSpawner;

import java.util.HashMap;
import java.util.Map;

public class GoblinHealthBarStage implements Disposable {
    private final Stage stage;
    private final GoblinSpawner goblinSpawner;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer; // Added for simple drawing
    private final Map<Goblin, Float> targetHealthPercentages = new HashMap<>();
    private final Map<Goblin, Float> displayedHealthPercentages = new HashMap<>();

    private static final float BAR_WIDTH = 25f;
    private static final float BAR_HEIGHT = 4f;
    private static final float BAR_OFFSET_Y = 20f;
    private static final float HEALTH_BAR_ANIMATION_SPEED = 3.0f;

    public GoblinHealthBarStage(GoblinSpawner goblinSpawner) {
        this.goblinSpawner = goblinSpawner;
        this.stage = new Stage(new ScreenViewport());
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void update() {
        // Update target health percentages for all goblins
        for (Goblin goblin : goblinSpawner.getGoblins()) {
            float healthPercentage = (float)goblin.getHealth() / goblin.getMaxHealth();
            targetHealthPercentages.put(goblin, healthPercentage);

            // Initialize display percentage if not present
            if (!displayedHealthPercentages.containsKey(goblin)) {
                displayedHealthPercentages.put(goblin, healthPercentage);
            }
        }

        // Remove entries for dead goblins
        targetHealthPercentages.keySet().removeIf(goblin ->
            !goblinSpawner.getGoblins().contains(goblin));
        displayedHealthPercentages.keySet().removeIf(goblin ->
            !goblinSpawner.getGoblins().contains(goblin));
    }

    public void update(float deltaTime) {
        update();

        // Smoothly interpolate health bar values
        for (Goblin goblin : goblinSpawner.getGoblins()) {
            float target = targetHealthPercentages.get(goblin);
            float current = displayedHealthPercentages.get(goblin);

            if (Math.abs(current - target) > 0.001f) {
                float newValue = MathUtils.lerp(current, target, deltaTime * HEALTH_BAR_ANIMATION_SPEED);
                displayedHealthPercentages.put(goblin, newValue);
            }
        }
    }

    public void render(OrthographicCamera camera) {
        // Use ShapeRenderer for drawing health bars
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw background bars
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Goblin goblin : goblinSpawner.getGoblins()) {
            float x = goblin.getX() - BAR_WIDTH / 2;
            float y = goblin.getY() + BAR_OFFSET_Y;

            // Background (black)
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2);

            // Background (gray)
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x, y, BAR_WIDTH, BAR_HEIGHT);

            // Health bar (red)
            float healthPercentage = displayedHealthPercentages.getOrDefault(goblin, 1f);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(x, y, BAR_WIDTH * healthPercentage, BAR_HEIGHT);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}
