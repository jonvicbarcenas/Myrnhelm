package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.npc.AnkarosTheNPC;
import com.dainsleif.hartebeest.players.Player;
import com.dainsleif.hartebeest.players.PlayerMyron;
import com.dainsleif.hartebeest.players.PlayerSwitcher;
import com.dainsleif.hartebeest.quests.Quest;
import com.dainsleif.hartebeest.quests.QuestHandler;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GoblinSpawner {
    private final World world;
    private final CollisionDetector collisionDetector;
    private final PlayerSwitcher playerSwitcher;

    private List<Goblin> goblins = new ArrayList<>();
    private List<EnemyGoblinGdxAi> goblinAIs = new ArrayList<>();

    private QuestHandler questHandler;

    public GoblinSpawner(World world, CollisionDetector collisionDetector, PlayerSwitcher playerSwitcher) {
        this.world = world;
        this.collisionDetector = collisionDetector;
        this.playerSwitcher = playerSwitcher;
    }

    public void spawnGoblins(Vector2 center, int count, float radius) {
        for (int i = 0; i < count; i++) {
            float angle = MathUtils.random(0, MathUtils.PI2);
            float distance = MathUtils.random(0, radius);

            float x = center.x + distance * MathUtils.cos(angle);
            float y = center.y + distance * MathUtils.sin(angle);

            Vector2 position = new Vector2(x, y);

            Goblin goblin = new Goblin(position, collisionDetector, world);
            EnemyGoblinGdxAi goblinAI = new EnemyGoblinGdxAi(goblin, playerSwitcher.getCurrentPlayer());

            goblins.add(goblin);
            goblinAIs.add(goblinAI);
        }
    }

    public void update(float delta) {
        Iterator<Goblin> goblinIterator = goblins.iterator();
        Iterator<EnemyGoblinGdxAi> aiIterator = goblinAIs.iterator();

        while (goblinIterator.hasNext() && aiIterator.hasNext()) {
            Goblin goblin = goblinIterator.next();
            EnemyGoblinGdxAi ai = aiIterator.next();

            if (goblin.isShouldRemove()) {
                world.destroyBody(goblin.getBody());
                goblin.dispose();
                goblinIterator.remove();
                aiIterator.remove();
                continue;
            }

            if (goblin.getHealth() <= 0 && goblin.getCurrentState() != EnemyState.DEAD) {
                goblin.setState(EnemyState.DEAD);
                goblin.update(); // Start the death animation
                ai.update(delta);
                continue;
            }

            // If player is dead, set goblin to idle state
            if (playerSwitcher.isDead() && goblin.getCurrentState() != EnemyState.DEAD) {
                goblin.setState(EnemyState.IDLE);
                goblin.update();
                continue;
            }

            goblin.update();
            ai.update(delta);
        }
    }

    public void draw(SpriteBatch batch, float delta) {
        for (Goblin goblin : goblins) {
            goblin.draw(batch, delta);
        }
    }

    public void checkDamageToPlayer() {
        Player currentPlayer = playerSwitcher.getCurrentPlayer();
        if (currentPlayer instanceof PlayerMyron) {
            PlayerMyron myronPlayer = (PlayerMyron) currentPlayer;
            for (Goblin goblin : goblins) {
                goblin.goblinDamage(myronPlayer);
            }
        }
    }

    public List<Goblin> getGoblins() {
        return goblins;
    }

    public void checkPlayerAttack() {
        Player currentPlayer = playerSwitcher.getCurrentPlayer();
        if (currentPlayer instanceof PlayerMyron) {
            PlayerMyron myronPlayer = (PlayerMyron) currentPlayer;
            myronPlayer.playerAttack();

            for (Iterator<Goblin> iterator = goblins.iterator(); iterator.hasNext();) {
                Goblin goblin = iterator.next();

                int healthBefore = goblin.getHealth();

                boolean wasKilled = myronPlayer.playerAttack(goblin, myronPlayer);

                // Log attack detailz
                System.out.println("Goblin health before: " + healthBefore +
                    ", after: " + goblin.getHealth() +
                    ", wasKilled: " + wasKilled);

// In checkPlayerAttack method in GoblinSpawner.java
                if (healthBefore > 0 && goblin.getHealth() <= 0 && questHandler != null) {
                    System.out.println("### GOBLIN KILLED! ###");
                    questHandler.recordEnemyKill("goblin");

                    // Get the quest dynamically from any NPC that might have assigned it
                    // This assumes NPCs are accessible through a method like npcHandler.getNpcByName("Ankaros")
                    // You'll need to adapt this to your actual object structure
                    for (Quest quest : questHandler.getAllActiveQuests()) {
                        if (quest.name.contains("Goblin")) {
                            System.out.println("Quest status: " + quest.status +
                                ", Progress: " + questHandler.getQuestProgressText(quest.id));
                        }
                    }
                }
            }
        }
    }

    public void dispose() {
        for (Goblin goblin : goblins) {
            goblin.dispose();
        }
        goblins.clear();
        goblinAIs.clear();
    }

    public void setQuestHandler(QuestHandler questHandler) {
        this.questHandler = questHandler;
    }

}
