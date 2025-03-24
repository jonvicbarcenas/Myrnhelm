package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import com.dainsleif.hartebeest.helpers.TileScan;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.HashMap;
import java.util.Map;

public class Goblin extends Enemy {
    private static final String SPRITE_PATH = "sprite/Enemy/Goblin/RegularGobu.png";
    private static final String JSON_PATH = "sprite/Enemy/Goblin/RegularGobu.json";

    private final Vector2 spawnPosition;
    private final CollisionDetector collisionDetector;
    private static final float WIDTH = 48f;
    private static final float HEIGHT = 48f;


    private Map<String, Animation<TextureRegion>> animations;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;
    private EnemyState currentState;
    private String currentDirection = "D"; // D, L, U, R


    private Body body;
    private TileScan tileScan;
    private int collisionLayerIndex = 3;

    public Goblin(Vector2 position, CollisionDetector collisionDetector, World world, TileScan tileScan) {
        super("Goblin", 100, 1, 10f, position, new Rectangle(0, 0, WIDTH, HEIGHT));

        this.spawnPosition = new Vector2(position);
        this.collisionDetector = collisionDetector;
        this.tileScan = tileScan;

        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = world.createBody(bodyDef);

        // Create hitbox shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4, 4, new Vector2(0, -6), 0); // Collision box size and offset

        // Create fixture with definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f; // Slight bounce

        // Add fixture to body
        body.createFixture(fixtureDef);
        body.setFixedRotation(true); // Prevent rotation

        // User data for collision identification
        body.setUserData(this);

        // Dispose shape after creating fixture
        shape.dispose();

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
        animations.put("idleU", new Animation<>(0.35f, loader.getFrames("idleU")));
        animations.put("idleR", new Animation<>(0.35f, loader.getFrames("idleR")));

        animations.put("walkD", new Animation<>(0.12f, loader.getFrames("walkD")));
        animations.put("walkL", new Animation<>(0.12f, loader.getFrames("walkL")));
        animations.put("walkU", new Animation<>(0.12f, loader.getFrames("walkU")));
        animations.put("walkR", new Animation<>(0.12f, loader.getFrames("walkR")));

        animations.put("spearAtkD", new Animation<>(0.20f, loader.getFrames("spearAtkD")));
        animations.put("spearAtkL", new Animation<>(0.20f, loader.getFrames("spearAtkL")));
        animations.put("spearAtkU", new Animation<>(0.20f, loader.getFrames("spearAtkU")));
        animations.put("spearAtkR", new Animation<>(0.20f, loader.getFrames("spearAtkR")));

        animations.put("ouchD", new Animation<>(0.15f, loader.getFrames("ouchD")));
        animations.put("ouchL", new Animation<>(0.15f, loader.getFrames("ouchL")));
        animations.put("ouchU", new Animation<>(0.15f, loader.getFrames("ouchU")));
        animations.put("ouchR", new Animation<>(0.15f, loader.getFrames("ouchR")));

        animations.put("spinny", new Animation<>(0.15f, loader.getFrames("spinny")));

        // Set initial frame
        currentFrame = animations.get("idleD").getKeyFrame(0);
    }

    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();

        // Update position from Box2D body
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        // Apply movement based on state
        Vector2 velocity = new Vector2(0, 0);

        switch (currentState) {
            case WALK_LEFT:
                velocity.x = -speed;
                currentDirection = "L";
                break;
            case WALK_RIGHT:
                velocity.x = speed;
                currentDirection = "R";
                break;
            case WALK_UP:
                velocity.y = speed;
                currentDirection = "U";
                break;
            case WALK_DOWN:
                velocity.y = -speed;
                currentDirection = "D";
                break;
            case ATTACKING:
            case ATTACKING_SPIN:
            case IDLE:
            case DEAD:
                velocity.set(0, 0);
                break;
        }

        // Apply velocity to body
        body.setLinearVelocity(velocity);

        // Update hitbox position
        hitbox.setPosition(position.x - WIDTH/2, position.y - HEIGHT/2);
    }

    public void draw(SpriteBatch batch, float delta) {
        update(); // Update position and state
        currentFrame = getFrameForCurrentState();

        // Get the width and height of the current frame
        float width = currentFrame.getRegionWidth();
        float height = currentFrame.getRegionHeight();

        // Draw the sprite centered at the goblin's position
        batch.draw(currentFrame, position.x - width/2, position.y - height/2);
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
            case ATTACKING_SPIN:
                animKey = "spinny";
                break;
            case DEAD:
                animKey = "ouch" + currentDirection;
                Animation<TextureRegion> deathAnim = animations.get(animKey);
                return deathAnim != null ? deathAnim.getKeyFrame(stateTime, false) :
                    animations.get("idleD").getKeyFrame(0);
            default:
                animKey = "idle" + currentDirection;
                break;
        }

        // Add null check before calling getKeyFrame
        Animation<TextureRegion> anim = animations.get(animKey);
        if (anim == null) {
            // Fallback to a default animation if the requested one doesn't exist
            System.out.println("Warning: Animation not found: " + animKey);
            return animations.get("idleD").getKeyFrame(0);
        }

        return anim.getKeyFrame(stateTime, looping);
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

    public boolean isAttackFinishedBasedOnAnimation() {
        return animations.get("spearAtk" + currentDirection).isAnimationFinished(stateTime);
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    public void dispose() {
        spriteBatch.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setDirection(String direction) {
        this.currentDirection = direction;
    }

    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }

    public Map<String, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public float getStateTime() {
        return stateTime;
    }

    public String getCurrentDirection() {
        return currentDirection;
    }
}
