package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.players.PlayerMyron;
import com.dainsleif.hartebeest.utils.CollisionDetector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GoblinSpawner {
    private final World world;
    private final CollisionDetector collisionDetector;
    private final PlayerMyron player;

    private List<Goblin> goblins = new ArrayList<>();
    private List<EnemyGoblinGdxAi> goblinAIs = new ArrayList<>();

    public GoblinSpawner(World world, CollisionDetector collisionDetector, PlayerMyron player) {
        this.world = world;
        this.collisionDetector = collisionDetector;
        this.player = player;
    }

    public void spawnGoblins(Vector2 center, int count, float radius) {
        for (int i = 0; i < count; i++) {
            float angle = MathUtils.random(0, MathUtils.PI2);
            float distance = MathUtils.random(0, radius);

            float x = center.x + distance * MathUtils.cos(angle);
            float y = center.y + distance * MathUtils.sin(angle);

            Vector2 position = new Vector2(x, y);

            Goblin goblin = new Goblin(position, collisionDetector, world);
            EnemyGoblinGdxAi goblinAI = new EnemyGoblinGdxAi(goblin, player);

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
            } else {
                goblin.update();
                ai.update(delta);
            }
        }
    }

    public void draw(SpriteBatch batch, float delta) {
        for (Goblin goblin : goblins) {
            goblin.draw(batch, delta);
        }
    }

    public void checkDamageToPlayer() {
        for (Goblin goblin : goblins) {
            goblin.goblinDamage(player);
        }
    }

    public List<Goblin> getGoblins() {
        return goblins;
    }

    public void checkPlayerAttack() {
        for (Goblin goblin : goblins) {
            player.playerAttack(goblin, player);
        }
    }

    public void dispose() {
        for (Goblin goblin : goblins) {
            goblin.dispose();
        }
        goblins.clear();
        goblinAIs.clear();
    }
}
