package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import java.util.HashMap;
import java.util.Map;

public class Goblin extends Enemy {
    private static final String SPRITE_PATH = "sprite/Enemy/Goblin/RegularGobu.png";
    private static final String JSON_PATH = "sprite/Enemy/Goblin/RegularGobu.json";

    private Map<String, Animation<TextureRegion>> animations;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private EnemyState currentState;
    private String currentDirection = "D"; // D, L, U, R

    public Goblin(Vector2 position) {
        super("Goblin", 100, 10, 1.5f, position, new Rectangle(0, 0, 48, 48));

        spriteBatch = new SpriteBatch();
        currentState = EnemyState.IDLE;
        loadAnimations();
    }


    private void loadAnimations() {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(SPRITE_PATH, JSON_PATH);
        animations = new HashMap<>();

        // Load animations based on JSON file tags
        animations.put("idleD", new Animation<>(0.35f, loader.getFrames("idleD")));
        animations.put("idleL", new Animation<>(0.35f, loader.getFrames("idleL")));
        animations.put("idleU", new Animation<>(0.35f, loader.getFrames("IdleU"))); // Note the capitalization from JSON
        animations.put("idleR", new Animation<>(0.35f, loader.getFrames("idleR")));

        animations.put("walkD", new Animation<>(0.12f, loader.getFrames("walkD")));
        animations.put("walkL", new Animation<>(0.12f, loader.getFrames("walkL")));
        animations.put("walkU", new Animation<>(0.12f, loader.getFrames("walkU")));
        animations.put("walkR", new Animation<>(0.12f, loader.getFrames("walkR")));

        animations.put("spearAtkD", new Animation<>(0.1f, loader.getFrames("spearAtkD")));
        animations.put("spearAtkL", new Animation<>(0.1f, loader.getFrames("spearAtkL")));
        animations.put("spearAtkU", new Animation<>(0.1f, loader.getFrames("spearAtkU")));
        animations.put("spearAtkR", new Animation<>(0.1f, loader.getFrames("spearAtkR")));

        animations.put("ouchD", new Animation<>(0.15f, loader.getFrames("ouchD")));
        animations.put("ouchL", new Animation<>(0.15f, loader.getFrames("ouchL")));
        animations.put("ouchU", new Animation<>(0.15f, loader.getFrames("ouchU")));
        animations.put("ouchR", new Animation<>(0.15f, loader.getFrames("ouchR")));

        // Set initial frame
        currentFrame = animations.get("idleD").getKeyFrame(0);
    }

    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();

        // Update position based on state
        switch (currentState) {
            case WALK_LEFT:
                position.x -= speed * Gdx.graphics.getDeltaTime();
                currentDirection = "L";
                break;
            case WALK_RIGHT:
                position.x += speed * Gdx.graphics.getDeltaTime();
                currentDirection = "R";
                break;
            case WALK_UP:
                position.y += speed * Gdx.graphics.getDeltaTime();
                currentDirection = "U";
                break;
            case WALK_DOWN:
                position.y -= speed * Gdx.graphics.getDeltaTime();
                currentDirection = "D";
                break;
            default:
                // No movement for other states
                break;
        }

        // Update hitbox position
        hitbox.setPosition(position);
    }

    public void draw(SpriteBatch batch, float delta) {
        update(); // Update position and state
        currentFrame = getFrameForCurrentState();
        batch.draw(currentFrame, position.x, position.y);
    }

    private TextureRegion getFrameForCurrentState() {
        boolean looping = currentState != EnemyState.DEAD;
        String animKey;

        switch (currentState) {
            case IDLE:
                animKey = "idle" + currentDirection;
                break;
            case WALK_LEFT:
                animKey = "walkL";
                break;
            case WALK_RIGHT:
                animKey = "walkR";
                break;
            case WALK_UP:
                animKey = "walkU";
                break;
            case WALK_DOWN:
                animKey = "walkD";
                break;
            case ATTACKING:
                animKey = "spearAtk" + currentDirection;
                break;
            case DEAD:
                animKey = "ouch" + currentDirection;
                return animations.get(animKey).getKeyFrame(stateTime, false);
            default:
                animKey = "idle" + currentDirection;
                break;
        }

        // Special case for IdleU which has capital I in the JSON
        if (animKey.equals("idleU")) {
            animKey = "IdleU";
        }

        return animations.get(animKey).getKeyFrame(stateTime, looping);
    }

    @Override
    protected void die() {
        super.die();
        currentState = EnemyState.DEAD;
        stateTime = 0; // Reset state time to start death animation from beginning
    }

    public void setState(EnemyState state) {
        if (currentState != state) {
            stateTime = 0; // Reset animation timer when changing states
            currentState = state;
        }
    }

    public EnemyState getCurrentState() {
        return currentState;
    }


    public void dispose() {
        spriteBatch.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }
}
