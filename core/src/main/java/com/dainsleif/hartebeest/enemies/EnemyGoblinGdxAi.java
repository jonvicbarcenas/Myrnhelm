package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dainsleif.hartebeest.players.Player;

public class EnemyGoblinGdxAi {
    // Owning enemy
    private final Enemy enemy;
    private final Goblin goblin;

    // Target player
    private final Player player;

    // State machine
    private final StateMachine<EnemyGoblinGdxAi, EnemyState> stateMachine;

    // AI behavior parameters
    private static final float tile = 5;
    private static final float VISION_RANGE = 16 * tile; // 16 tiles distance
    private static final float PATROL_CHANGE_TIME = 2f; // seconds between patrol direction changes
    private float patrolTimer = 0f;

    // Tracking state
    private boolean activePursuit = false;
    private long lastAttackTime = 0;
    private static final long ATTACK_INITIATE_TIME = 2000; // milliseconds between attacks
    private boolean isInAttackCooldown = false;
    private boolean isAttackAnimationPlaying = false;
    private long attackCooldownEndTime = 0;

    // Additional parameters
    private static final float RETURN_TO_SPAWN_DISTANCE = 250f; // Distance to trigger return
    private static final float SPAWN_ARRIVAL_THRESHOLD = 5f; // Distance considered "at spawn"

    // In EnemyGdxAi.java, add this constant near your other constants
    private static final float ATTACK_RANGE = 28f; // About 2 tiles distance

    private int comboMoveCounter = 0;

    public EnemyGoblinGdxAi(Goblin goblin, Player player) {
        this.enemy = goblin;
        this.goblin = goblin;
        this.player = player;
        this.stateMachine = new DefaultStateMachine<>(this, EnemyState.IDLE);
    }

    public void update(float delta) {
        // Update state machine
        stateMachine.update();

        // Check if player is in vision range
        float distanceToPlayer = distanceToPlayer();

        if (distanceToPlayer <= ATTACK_RANGE) {
            // Player is in attack range - perform attack animation
            attackPlayer(ATTACK_RANGE);
        } else if (distanceToPlayer <= VISION_RANGE) {
            // Player spotted but not in attack range - chase them
            activePursuit = true;
            chasePlayer();
        } else if (distanceToPlayer > RETURN_TO_SPAWN_DISTANCE) {
            // Player too far away - return to spawn
            activePursuit = false;
            returnToSpawn();
        } else {
            // Player not in range but not too far - patrol randomly
            activePursuit = false;
            patrol(delta);
        }
    }

    public boolean isActivePursuit() {
        return activePursuit;
    }

    public void attackPlayer(float attackRange) {
        long currentTime = TimeUtils.millis();

       // If we're waiting for an attack animation to finish
        if (isAttackAnimationPlaying) {
            // Check if the attack state was interrupted (goblin is no longer in an attack state)
            if (goblin.getCurrentState() != EnemyState.ATTACKING &&
                goblin.getCurrentState() != EnemyState.ATTACKING_SPIN) {
                // Attack was interrupted, reset flags
                isAttackAnimationPlaying = false;
                // No need to start cooldown since attack was interrupted
            }
            else if ((goblin.getCurrentState() == EnemyState.ATTACKING && goblin.isAttackFinishedBasedOnAnimation()) ||
                (goblin.getCurrentState() == EnemyState.ATTACKING_SPIN && goblin.isAttackFinishedBasedOnAnimation())) {
                // Animation finished normally, now start cooldown
                isAttackAnimationPlaying = false;
                isInAttackCooldown = true;
                attackCooldownEndTime = TimeUtils.millis() + 2000; // 2 seconds cooldown
                return;
            }
            else {
                // Animation still playing, don't do anything else
                return;
            }
        }

        // Check if we're in cooldown period
        if (isInAttackCooldown) {
            if (currentTime < attackCooldownEndTime) {
                // Still in cooldown, set to IDLE
                goblin.setState(EnemyState.IDLE);
                return;
            } else {
                // Cooldown finished
                isInAttackCooldown = false;
            }
        }

        if (distanceToPlayer() <= attackRange) {
            // Get positions
            Vector2 playerPos = new Vector2(player.getX(), player.getY());
            Vector2 enemyPos = enemy.getPosition();

            // Determine if we're already aligned with player on an axis
            boolean alignedX = Math.abs(playerPos.x - enemyPos.x) < 10f;
            boolean alignedY = Math.abs(playerPos.y - enemyPos.y) < 10f;

            if (!alignedX && !alignedY) {
                // Not aligned on either axis - move to align first
                chasePlayer();
                return;
            }

            // Regular attack cooldown check
            if (currentTime - lastAttackTime < ATTACK_INITIATE_TIME) {
                goblin.setState(EnemyState.IDLE);
                return;
            }

            // Face player before attacking
            facePlayer();

            // Start new attack sequence
            lastAttackTime = currentTime;
            isAttackAnimationPlaying = true; // Set flag that we're playing an attack animation

            // Check if it's time for a spin attack
            if (comboMoveCounter >= 4) {
                // Perform spin attack
                goblin.setState(EnemyState.ATTACKING_SPIN);
                comboMoveCounter = 0; // Reset counter after special attack
            } else {
                // Perform regular attack
                goblin.setState(EnemyState.ATTACKING);
                comboMoveCounter++;
            }
        }
    }

    private void facePlayer() {
        Vector2 playerPos = new Vector2(player.getX(), player.getY());
        Vector2 enemyPos = enemy.getPosition();

        // Determine direction to player
        float dx = playerPos.x - enemyPos.x;
        float dy = playerPos.y - enemyPos.y;

        // Choose the dominant direction (up/down vs left/right)
        if (Math.abs(dx) > Math.abs(dy)) {
            // Face horizontally
            if (dx > 0) {
                goblin.setDirection("R");
            } else {
                goblin.setDirection("L");
            }
        } else {
            // Face vertically
            if (dy > 0) {
                goblin.setDirection("U");
            } else {
                goblin.setDirection("D");
            }
        }
    }

    private float distanceToPlayer() {
        Vector2 playerPos = player.getPosition();
        Vector2 enemyPos = enemy.getPosition();
        return playerPos.dst(enemyPos);
    }

    private void chasePlayer() {
        Vector2 playerPos = new Vector2(player.getX(), player.getY());
        Vector2 enemyPos = enemy.getPosition();

        // Determine distance to player on each axis
        float dx = playerPos.x - enemyPos.x;
        float dy = playerPos.y - enemyPos.y;

        // Define a threshold for considering "aligned" on an axis
        float alignmentThreshold = 5f;

        // Always align horizontally first, then vertically
        if (Math.abs(dx) > alignmentThreshold) {
            // Move horizontally first
            if (dx > 0) {
                goblin.setState(EnemyState.WALK_RIGHT);
            } else {
                goblin.setState(EnemyState.WALK_LEFT);
            }
        } else if (Math.abs(dy) > alignmentThreshold) {
            // Once horizontal alignment is achieved, move vertically
            if (dy > 0) {
                goblin.setState(EnemyState.WALK_UP);
            } else {
                goblin.setState(EnemyState.WALK_DOWN);
            }
        } else {
            // If we're close enough on both axes, go idle
            goblin.setState(EnemyState.IDLE);
        }
    }

    private void patrol(float delta) {
        patrolTimer += delta;

        // Change patrol direction periodically
        if (patrolTimer >= PATROL_CHANGE_TIME) {
            patrolTimer = 0;

            // Choose a random direction
            int direction = (int)(Math.random() * 5); // 0-4

            switch (direction) {
                case 0:
                    goblin.setState(EnemyState.IDLE);
                    break;
                case 1:
                    goblin.setState(EnemyState.WALK_LEFT);
                    break;
                case 2:
                    goblin.setState(EnemyState.WALK_RIGHT);
                    break;
                case 3:
                    goblin.setState(EnemyState.WALK_UP);
                    break;
                case 4:
                    goblin.setState(EnemyState.WALK_DOWN);
                    break;
            }
        }
    }

    private float distanceToSpawn() {
        Vector2 enemyPos = enemy.getPosition();
        Vector2 spawnPos = goblin.getSpawnPosition();
        return enemyPos.dst(spawnPos);
    }

    private void returnToSpawn() {
        Vector2 enemyPos = enemy.getPosition();
        Vector2 spawnPos = goblin.getSpawnPosition();

        // If we're already close to spawn, just go idle
        if (enemyPos.dst(spawnPos) < SPAWN_ARRIVAL_THRESHOLD) {
            goblin.setState(EnemyState.IDLE);
            return;
        }

        // Determine direction to spawn point
        float dx = spawnPos.x - enemyPos.x;
        float dy = spawnPos.y - enemyPos.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            // Move horizontally
            if (dx > 0) {
                goblin.setState(EnemyState.WALK_RIGHT);
            } else {
                goblin.setState(EnemyState.WALK_LEFT);
            }
        } else {
            // Move vertically
            if (dy > 0) {
                goblin.setState(EnemyState.WALK_UP);
            } else {
                goblin.setState(EnemyState.WALK_DOWN);
            }
        }
    }

    // Additional methods for state handling
    public Enemy getEnemy() {
        return enemy;
    }

    public Player getPlayer() {
        return player;
    }
}
