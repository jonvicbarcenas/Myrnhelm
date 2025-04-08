package com.dainsleif.hartebeest.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.utils.CollisionDetector;

public class PlayerSwitcher {
    private Player currentPlayer;
    private PlayerMyron myronPlayer;
    private PlayerMeley meleyPlayer;

    public PlayerSwitcher(World world, CollisionDetector collisionDetector) {
        // Initialize both player types
        myronPlayer = new PlayerMyron(world, collisionDetector);
        meleyPlayer = new PlayerMeley(world, collisionDetector);

        // Set Myron as the default player
        currentPlayer = myronPlayer;
    }

    public void update(float deltaTime, KeyHandler keyHandler) {
        // Check for key presses to switch characters
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            switchToMyron();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            switchToMeley();
        }

        currentPlayer.update(deltaTime, keyHandler);
    }

    private void switchToMyron() {
        if (currentPlayer != myronPlayer && !currentPlayer.isDead()) {
            Vector2 position = currentPlayer.getPosition();

            disableCurrentPlayerBody();

            myronPlayer.setPosition(position.x, position.y);
            myronPlayer.enableBody();

            currentPlayer = myronPlayer;
        }
    }

    private void switchToMeley() {
        if (currentPlayer != meleyPlayer && !currentPlayer.isDead()) {
            Vector2 position = currentPlayer.getPosition();

            disableCurrentPlayerBody();

            meleyPlayer.setPosition(position.x, position.y);
            meleyPlayer.enableBody();

            currentPlayer = meleyPlayer;
        }
    }

    private void disableCurrentPlayerBody() {
        if (currentPlayer != null) {
            currentPlayer.disableBody();
        }
    }

    // Delegate methods to current player for easier integration
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public float getX() {
        return currentPlayer.getX();
    }

    public float getY() {
        return currentPlayer.getY();
    }

    public boolean isDead() {
        return currentPlayer.isDead();
    }

    public void setDead(boolean dead) {
        currentPlayer.setDead(dead);
    }

    public void setPosition(float x, float y) {
        currentPlayer.setPosition(x, y);
    }

    public void draw(Batch batch, float parentAlpha) {
        currentPlayer.draw(batch, parentAlpha);
    }

    public int getHealth() {
        if (currentPlayer instanceof PlayerMyron) {
            return PlayerMyron.getHealth();
        } else {
            return PlayerMeley.getHealth();
        }
    }

    public void setHealth(int health) {
        if (currentPlayer instanceof PlayerMyron) {
            ((PlayerMyron)currentPlayer).setHealth(health);
        } else {
            ((PlayerMeley)currentPlayer).setHealth(health);
        }
    }

    public void setPlayerPosition(Vector2 vector2) {
        if (currentPlayer instanceof PlayerMyron) {
            ((PlayerMyron)currentPlayer).setPosition(vector2.x, vector2.y);
        } else {
            ((PlayerMeley)currentPlayer).setPosition(vector2.x, vector2.y);
        }
    }

}
