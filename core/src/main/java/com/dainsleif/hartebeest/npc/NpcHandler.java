package com.dainsleif.hartebeest.npc;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.players.Player;

import java.util.ArrayList;
import java.util.List;

public class NpcHandler {
    private List<NPC> npcs;

    public NpcHandler(World world, Vector2 position) {
        npcs = new ArrayList<>();
        // Initialize NPCs in specific locations
        npcs.add(new AnkarosTheNPC(world, position.x, position.y));
        // Add more NPCs as needed
    }

    public void update(float deltaTime, Player player) {
        Vector2 playerPos = player.getPosition();

        for (NPC npc : npcs) {
            npc.update(deltaTime);

            // Update following behavior
            if (npc.isFollowing()) {
                npc.updateFollowing(deltaTime, playerPos);
            }

            // Make NPCs face the player when nearby
            if (npc.isPlayerInRange(playerPos)) {
                if (npc instanceof AnkarosTheNPC) {
                    ((AnkarosTheNPC)npc).facePlayer(playerPos.x, playerPos.y);
                }
            }
        }
    }

    public void draw(Batch batch) {
        for (NPC npc : npcs) {
            npc.draw(batch, 1f);
        }
    }

    public NPC getNpcAtPosition(float x, float y, float radius) {
        Vector2 position = new Vector2(x, y);
        for (NPC npc : npcs) {
            if (npc.getPosition().dst(position) <= radius) {
                return npc;
            }
        }
        return null;
    }

    public void checkPlayerInteraction(Player player, float interactKey) {
        // This would be called when player presses an interaction key
        for (NPC npc : npcs) {
            if (npc.isPlayerInRange(player.getPosition())) {
                npc.interact();

                // Toggle following state when interacting
                if (npc.isFollowing()) {
                    npc.stopFollowing();
                } else {
                    npc.startFollowing();
                }

                break;
            }
        }
    }

    public NPC[] getNpcs() {
        return npcs.toArray(new NPC[0]);
    }
}
