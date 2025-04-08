package com.dainsleif.hartebeest.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

public abstract class NPC extends Actor {
    protected Map<String, Animation<TextureRegion>> animations;
    protected TextureRegion currentFrame;
    protected float stateTime;
    protected Body body;
    protected String currentDirection = "front";
    protected final float WIDTH = 32;
    protected final float HEIGHT = 32;
    protected String name;
    protected float interactionRadius = 50f;

    // New fields for following behavior
    protected boolean isFollowing = false;
    protected float followDistance = 100f;
    protected float stopDistance = 30f;
    protected float movementSpeed = 50f;

    public NPC(World world, String texturePath, String jsonPath, float x, float y) {
        animations = new HashMap<>();
        stateTime = 0f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Change to DynamicBody to allow movement
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true; // Prevent rotation
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH/5, HEIGHT/5, new Vector2(0, -HEIGHT/6), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        body.createFixture(fixtureDef);
        shape.dispose();

        setWidth(WIDTH);
        setHeight(HEIGHT);

        loadAnimations(texturePath, jsonPath);
    }

    protected abstract void loadAnimations(String texturePath, String jsonPath);

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (animations.containsKey(currentDirection)) {
            currentFrame = animations.get(currentDirection).getKeyFrame(stateTime, true);
        }
    }

    // New method for following behavior
    public void updateFollowing(float deltaTime, Vector2 playerPos) {
        Vector2 npcPos = getPosition();
        float distance = npcPos.dst(playerPos);

        // Check if NPC should follow
        if (distance <= followDistance && distance > stopDistance && isFollowing) {
            // Calculate direction to player
            Vector2 direction = new Vector2(playerPos).sub(npcPos).nor();

            // Set velocity toward player
            body.setLinearVelocity(direction.x * movementSpeed, direction.y * movementSpeed);

            // Update animation direction
            updateDirectionFromVelocity(body.getLinearVelocity());
        } else {
            // Stop moving if too close or too far
            body.setLinearVelocity(0, 0);
        }
    }

    protected void updateDirectionFromVelocity(Vector2 velocity) {
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            currentDirection = velocity.x > 0 ? "right" : "left";
        } else if (Math.abs(velocity.y) > 0.1f) {
            currentDirection = velocity.y > 0 ? "back" : "front";
        }
    }

    public void startFollowing() {
        isFollowing = true;
    }

    public void stopFollowing() {
        isFollowing = false;
        body.setLinearVelocity(0, 0);
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (currentFrame != null) {
            batch.draw(currentFrame, body.getPosition().x - WIDTH/2,
                body.getPosition().y - HEIGHT/2, WIDTH, HEIGHT);
        }
    }

    public String getDialogueText() {
        return "...";
    }

    public String getName() {
        return name;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public boolean isPlayerInRange(Vector2 playerPosition) {
        return getPosition().dst(playerPosition) <= interactionRadius;
    }

    public abstract void interact();
}
