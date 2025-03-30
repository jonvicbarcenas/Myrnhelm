package com.dainsleif.hartebeest.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.dainsleif.hartebeest.enemies.Enemy;
import com.dainsleif.hartebeest.enemies.Goblin;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import com.dainsleif.hartebeest.movements.BasicMovements;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {
    private Map<String, Animation<TextureRegion>> animations;
    private TextureRegion currentFrame;
    private float stateTime;
    private Body body;
    private BasicMovements movements;
    private final float WIDTH = 64;
    private final float HEIGHT = 64;
    private String currentDirection = "down";
    private final CollisionDetector collisionDetector;

    private boolean isAttacking = false;
    private float attackTimer = 0;
    private static final float ATTACK_DURATION = 0.8f;

    // Track player attack state
    private boolean playerDamageApplied = false;
    private float playerAttackRange = 40f;
    private int playerAttackDamage = 10;
    private float knockbackForce = 5f;

    public Player(World world, String texturePath, String jsonPath, CollisionDetector collisionDetector) {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(texturePath, jsonPath);

        animations = new HashMap<>();
        animations.put("up", new Animation<>(0.1f, loader.getFrames("walkTop")));
        animations.put("down", new Animation<>(0.1f, loader.getFrames("walkDown")));
        animations.put("left", new Animation<>(0.1f, loader.getFrames("walkLeft")));
        animations.put("right", new Animation<>(0.1f, loader.getFrames("walkRight")));
        animations.put("atk_up", new Animation<>(0.1f, loader.getFrames("atkTop")));
        animations.put("atk_down", new Animation<>(0.1f, loader.getFrames("atkDown")));
        animations.put("atk_left", new Animation<>(0.1f, loader.getFrames("atkLeft")));
        animations.put("atk_right", new Animation<>(0.1f, loader.getFrames("atkRight")));

        currentFrame = animations.get("down").getKeyFrame(0);
        stateTime = 0f;

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

    public void playerAttack(Goblin goblin, Player player) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            playerDamageApplied = false;
        }

        if (!isAttacking) {
            isAttacking = true;
            attackTimer = 0;
            stateTime = 0;

            body.setLinearVelocity(0, 0);
        }

        if(goblin == null){
            return;
        }

        // Get positions
        Vector2 playerPos = player.getPosition();
        Vector2 goblinPos = goblin.getPosition();
        float distance = playerPos.dst(goblinPos);

        // Only process during attack and when damage hasn't been applied yet
        if (distance <= playerAttackRange && !playerDamageApplied && isGoblinInAttackDirection(playerPos, goblinPos)) {
            Vector2 knockbackDirection = new Vector2(goblinPos).sub(playerPos).nor();
            goblin.takeDamage(playerAttackDamage);
            System.out.println("Player hit goblin! -" + playerAttackDamage + " damage");

            Body goblinBody = goblin.getBody();
            goblinBody.applyLinearImpulse(
                knockbackDirection.scl(knockbackForce),
                goblinBody.getWorldCenter(),
                true
            );
            playerDamageApplied = true;
        }
    }

    public String getDirection() {
        switch (currentDirection) {
            case "right": return "R";
            case "left": return "L";
            case "up": return "U";
            case "down": return "D";
            default: return "D";
        }
    }

    private boolean isGoblinInAttackDirection(Vector2 playerPos, Vector2 goblinPos) {
        // Calculate direction vector from player to goblin
        Vector2 directionToGoblin = new Vector2(goblinPos).sub(playerPos).nor();

        // Get player's facing direction as a normalized vector
        Vector2 playerFacingDirection = new Vector2(0, 0);

        // Set direction vector based on player's current facing direction
        String direction = getDirection(); // Assuming you have a getDirection() method
        if (direction.equals("R")) playerFacingDirection.set(1, 0);
        else if (direction.equals("L")) playerFacingDirection.set(-1, 0);
        else if (direction.equals("U")) playerFacingDirection.set(0, 1);
        else if (direction.equals("D")) playerFacingDirection.set(0, -1);

        // Calculate dot product (determines if goblin is in front of player)
        float dotProduct = directionToGoblin.dot(playerFacingDirection);

        // Goblin is in attack direction if dot product is positive (within ~90 degrees of facing direction)
        return dotProduct > 0;
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

        // Only allow movement when not attacking
        if (!isAttacking) {
            movements.update(deltaTime, keyHandler, body);
        }

        float newX = getX();
        float newY = getY();

        if (!collisionDetector.canMoveTo(oldX, oldY, WIDTH, HEIGHT, newX, newY)) {
            body.setTransform(oldX, oldY, body.getAngle());
        }

        // Update attack state
        if (isAttacking) {
            attackTimer += deltaTime;
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0;
            }
        }

        // Always update stateTime to keep animation flowing
        stateTime += deltaTime;

        String direction = movements.getCurrentDirection();
        if (direction != null && !direction.isEmpty() && !isAttacking) {
            currentDirection = direction;
        }

        // Select the appropriate animation
        String animationKey;
        if (isAttacking) {
            animationKey = "atk_" + currentDirection;
        } else {
            animationKey = currentDirection;
        }

        Animation<TextureRegion> currentAnimation = animations.get(animationKey);

        if (currentAnimation != null) {
            if (isAttacking || movements.isMoving()) {
                // For attack animations, don't loop (false)
                currentFrame = currentAnimation.getKeyFrame(stateTime, !isAttacking);
            } else {
                // When not moving, display a static frame
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

    public Vector2 getPosition() {
       return body.getPosition();
    }
}
