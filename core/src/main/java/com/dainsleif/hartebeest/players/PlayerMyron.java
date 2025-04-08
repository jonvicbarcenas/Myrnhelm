package com.dainsleif.hartebeest.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.enemies.Goblin;
import com.dainsleif.hartebeest.enemies.GoblinSpawner;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.HashMap;
import java.util.Map;

public class PlayerMyron extends Player {
    private static final String TEXTURE_PATH = "sprite/player/Myron.png";
    private static final String JSON_PATH = "sprite/player/Myron.json";

    private static int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private int damage;
    private int level;
    private int experience;
    private boolean isDead;

    private Animation<TextureRegion> strahlAnimation;
    private boolean isUsingStrahl = false;
    private float strahlStateTime = 0f;
    private static final float STRAHL_DURATION = 1.0f;

    GoblinSpawner goblinSpawner;

    public PlayerMyron(World world, CollisionDetector collisionDetector) {
        super(world, TEXTURE_PATH, JSON_PATH, collisionDetector);
        initializeStats();
        initializeAnimations();
    }

    @Override
    protected void initializeAnimations() {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(TEXTURE_PATH, JSON_PATH);

        Map<String, Animation<TextureRegion>> animations = new HashMap<>();
        animations.put("up", new Animation<>(0.1f, loader.getFrames("walkTop")));
        animations.put("down", new Animation<>(0.1f, loader.getFrames("walkDown")));
        animations.put("left", new Animation<>(0.1f, loader.getFrames("walkLeft")));
        animations.put("right", new Animation<>(0.1f, loader.getFrames("walkRight")));
        animations.put("atk_up", new Animation<>(0.1f, loader.getFrames("atkTop")));
        animations.put("atk_down", new Animation<>(0.1f, loader.getFrames("atkDown")));
        animations.put("atk_left", new Animation<>(0.1f, loader.getFrames("atkLeft")));
        animations.put("atk_right", new Animation<>(0.1f, loader.getFrames("atkRight")));
        animations.put("Strahl", new Animation<>(0.1f, loader.getFrames("Strahl")));

        SpriteSheetLoaderJson strahlLoader = new SpriteSheetLoaderJson("sprite/player/Strahl_Effects.png", "sprite/player/Strahl_Effects.json");
        animations.put("Strahl_Effetcs", new Animation<>(0.1f, strahlLoader.getFrames()));

        setAnimations(animations);
        setCurrentFrame(animations.get("down").getKeyFrame(0));
        setStateTime(0f);
    }

    private void initializeStats() {
        this.maxHealth = 1500;
        this.health = maxHealth;
        this.maxMana = 50;
        this.mana = maxMana;
        this.damage = 10;
        this.level = 1;
        this.experience = 0;
    }

    public void setGoblinSpawner(GoblinSpawner goblinSpawner) {
        this.goblinSpawner = goblinSpawner;
    }

    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Myron took " + amount + " damage!");
        if (health <= 0) {
            health = 0;
            die();
        }
    }

    public void useSkill1() {
        if (!isUsingStrahl) {
            isUsingStrahl = true;
            strahlStateTime = 0f;
            setCurrentFrame(getAnimations().get("Strahl").getKeyFrame(0));
            System.out.println("Using Strahl skill!");

            // Apply the Strahl effect
            applyStrahlEffect();
        }
    }

    private void applyStrahlEffect() {
        float knockbackDistance = 100f;
        int strahlDamage = 10;
        Vector2 playerPos = getPosition();

        for (Goblin goblin : goblinSpawner.getGoblins()) {
            Vector2 goblinPos = goblin.getPosition();
            float distance = playerPos.dst(goblinPos);

            if (distance <= knockbackDistance) {
                Vector2 knockbackDirection = new Vector2(goblinPos).sub(playerPos).nor();
                Vector2 knockbackVector = knockbackDirection.scl(knockbackDistance);

                // Apply the impulse to the center of the goblin's body
                goblin.getBody().applyLinearImpulse(knockbackVector, goblin.getBody().getWorldCenter(), true);

                goblin.takeDamage(strahlDamage);
            }
        }
    }
    private void showStrahlEffect(Batch batch) {
        if (strahlAnimation == null) {
            strahlAnimation = getAnimations().get("Strahl_Effetcs");
        }
        float delta = Gdx.graphics.getDeltaTime();
        strahlStateTime += delta;
        TextureRegion currentFrame = strahlAnimation.getKeyFrame(strahlStateTime, false);

        float effectX = (getX() - (this.getWidth()/2 ))  + (getWidth() - currentFrame.getRegionWidth()) / 2;
        float effectY = (getY()- (this.getHeight()/2 )) + (getHeight() - currentFrame.getRegionHeight()) / 2;
        batch.draw(currentFrame, effectX, effectY);
    }


    public void updateSkills(float delta) {
        if (isUsingStrahl) {
            strahlStateTime += delta;
            Animation<TextureRegion> strahlAnim = getAnimations().get("Strahl");
            setCurrentFrame(strahlAnim.getKeyFrame(strahlStateTime, false));

            if (strahlStateTime >= STRAHL_DURATION) {
                isUsingStrahl = false;
                strahlStateTime = 0f;
                String currentDirection = getDirection().toLowerCase();
                if (currentDirection.equals("r")) currentDirection = "right";
                else if (currentDirection.equals("l")) currentDirection = "left";
                else if (currentDirection.equals("u")) currentDirection = "up";
                else if (currentDirection.equals("d")) currentDirection = "down";
                setCurrentFrame(getAnimations().get(currentDirection).getKeyFrame(0));
            }
        }
    }


    public void useSkill2() {}

    public void useUltimate() {}

    public void update(float deltaTime, KeyHandler keyHandler) {
        if (!isUsingStrahl) {
            super.update(deltaTime, keyHandler);
        } else {
            updateSkills(deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            useSkill1();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isUsingStrahl) {
            showStrahlEffect(batch);
        }
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public boolean useMana(int amount) {
        if (mana >= amount) {
            mana -= amount;
            return true;
        }
        return false;
    }

    public void restoreMana(int amount) {
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void gainExperience(int amount) {
        experience += amount;
        int experienceNeeded = level * 100;
        if (experience >= experienceNeeded) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        maxHealth += 10;
        health = maxHealth;
        maxMana += 5;
        mana = maxMana;
        damage += 2;
        experience = 0;
        System.out.println("Myron leveled up to level " + level + "!");
    }

    private void die() {
        System.out.println("Myron has been defeated!");
    }

    public static int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getDamage() { return damage; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { isDead = dead; }

    public void setHealth(int i) {
        health = i;
    }
}
