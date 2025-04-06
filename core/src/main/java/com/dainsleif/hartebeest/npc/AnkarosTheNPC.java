package com.dainsleif.hartebeest.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;
import com.dainsleif.hartebeest.quests.Quest;
import com.dainsleif.hartebeest.quests.QuestHandler;

public class AnkarosTheNPC extends NPC {
    private static final String TEXTURE_PATH = "sprite/npc/npc.png";
    private static final String JSON_PATH = "sprite/npc/npc.json";
    private boolean hasQuest = true;
    private boolean questCompleted = false;

    public static final int GOBLIN_QUEST_ID = 1;
    private QuestHandler questHandler;

    public AnkarosTheNPC(World world, float x, float y) {
        super(world, TEXTURE_PATH, JSON_PATH, x, y);
        this.name = "Ankaros";
        this.interactionRadius = 60f; // Slightly larger interaction radius
    }

    @Override
    protected void loadAnimations(String texturePath, String jsonPath) {
        SpriteSheetLoaderJson loader = new SpriteSheetLoaderJson(texturePath, jsonPath);

        // Load animations for each direction from the sprite sheet
        TextureRegion[] frontFrames = loader.getFrames("front");
        TextureRegion[] leftFrames = loader.getFrames("left");
        TextureRegion[] rightFrames = loader.getFrames("right");
        TextureRegion[] backFrames = loader.getFrames("back");

        // Create animations with frame duration of 0.15 seconds
        animations.put("front", new Animation<>(0.15f, frontFrames));
        animations.put("left", new Animation<>(0.15f, leftFrames));
        animations.put("right", new Animation<>(0.15f, rightFrames));
        animations.put("back", new Animation<>(0.15f, backFrames));

        // Set default frame
        currentFrame = frontFrames[0];
    }


    @Override
    public void interact() {
        if (questHandler == null) {
            return;
        }

        Quest quest = questHandler.getQuestById(GOBLIN_QUEST_ID);

        if (quest == null) {
            System.out.println(getName() + ": Greetings, traveler.");
            return;
        }

        if (quest.status.equals("not_started")) {
            questHandler.startQuest(GOBLIN_QUEST_ID);
            System.out.println(getName() + ": Hello traveler! I need your help with something important.");
        } else if (quest.status.equals("in_progress")) {
            String progress = questHandler.getQuestProgressText(GOBLIN_QUEST_ID);
            System.out.println(getName() + ": How is the hunt going? " + progress);
        } else if (quest.status.equals("completed")) {
            System.out.println(getName() + ": Thank you for your help, brave adventurer!");
            // Give rewards here
        }
    }


    public String getName() {
        return name;
    }

    // Quest-related methods
    public boolean hasQuest() {
        return hasQuest && !questCompleted;
    }

    public void completeQuest() {
        questCompleted = true;
        System.out.println("Quest completed!"+ getName() + " is grateful.");
        // Reward logic would go here
    }

    public void setQuestHandler(QuestHandler questHandler) {
        this.questHandler = questHandler;
    }

    public void facePlayer(float playerX, float playerY) {
        Vector2 npcPos = getPosition();

        float dx = playerX - npcPos.x;
        float dy = playerY - npcPos.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = (dx > 0) ? "right" : "left";
        } else {
            currentDirection = (dy > 0) ? "back" : "front";
        }
    }

    public String getDialogueText() {
        if (hasQuest && !questCompleted) {
            return "Hello traveler! I need your help with something important.";
        } else if (questCompleted) {
            return "Thank you for your help, brave adventurer!";
        } else {
            return "Greetings, traveler. What a fine day it is.";
        }
    }
}
