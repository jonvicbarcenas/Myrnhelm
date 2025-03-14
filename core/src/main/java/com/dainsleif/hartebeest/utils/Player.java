package com.dainsleif.hartebeest.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import com.dainsleif.hartebeest.movements.BasicMovements;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {
    private Map<String, Animation<TextureRegion>> animations;
    private TextureRegion currentFrame;
    private float stateTime;
    private Body body;
    private BasicMovements movements;
    private final float WIDTH = 48;
    private final float HEIGHT = 64;
    private String currentDirection = "down";
    private final CollisionDetector collisionDetector;

    public Player(World world, String texturePath, String jsonPath, CollisionDetector collisionDetector) {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(texturePath, jsonPath);

        // Load animations for all directions
        animations = new HashMap<>();
        animations.put("up", new Animation<>(0.1f, loader.getFrames("up")));
        animations.put("down", new Animation<>(0.1f, loader.getFrames("down")));
        animations.put("left", new Animation<>(0.1f, loader.getFrames("left")));
        animations.put("right", new Animation<>(0.1f, loader.getFrames("right")));

        // Optional: Add diagonal animations if they exist
        if (loader.getFrames("left_up").length > 0) {
            animations.put("left_up", new Animation<>(0.1f, loader.getFrames("left_up")));
        }
        if (loader.getFrames("right_up").length > 0) {
            animations.put("right_up", new Animation<>(0.1f, loader.getFrames("right_up")));
        }

        // Set initial frame
        currentFrame = animations.get("down").getKeyFrame(0);
        stateTime = 0f;

        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(GameInfo.getPlayerX(), GameInfo.getPlayerY());
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(4, 4, new Vector2(0, -6), 0); // Collision box size (32x32 pixels)
        body.createFixture(shape, 0); // Density is 0, so it's a static body 1.0f physics enabled
        shape.dispose();

        movements = new BasicMovements();

        setWidth(WIDTH);
        setHeight(HEIGHT);

        this.collisionDetector = collisionDetector;
    }

    public void setPosition(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        float oldX = getX();
        float oldY = getY();

        movements.update(deltaTime, keyHandler, body);

        float newX = getX();
        float newY = getY();

        if (!collisionDetector.canMoveTo(oldX, oldY, WIDTH, HEIGHT, newX, newY)) {
            body.setTransform(oldX, oldY, body.getAngle());
        }

        // Always update stateTime to keep animation flowing
        stateTime += deltaTime;

        String direction = movements.getCurrentDirection();
        if (direction != null && !direction.isEmpty()) {
            currentDirection = direction;
        }

        Animation<TextureRegion> currentAnimation = animations.get(currentDirection);

        if (currentAnimation != null) {
            if (movements.isMoving()) {
                // Loop the animation by setting the second parameter to true
                currentFrame = currentAnimation.getKeyFrame(stateTime, true);
            } else {
                // When not moving, display a static frame (first frame)
                // but don't reset stateTime to prevent jerky animation restart
                currentFrame = currentAnimation.getKeyFrame(0);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (currentFrame != null) {
            batch.draw(currentFrame, getX() - WIDTH/2, getY() - HEIGHT/2, WIDTH, HEIGHT);
        }
    }
}
