package com.dainsleif.hartebeest.quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestHandler {
    private List<Quest> quests;
    private Map<Integer, Quest> questsById;
    private Map<String, Integer> killCounts;

    public QuestHandler() {
        quests = new ArrayList<>();
        questsById = new HashMap<>();
        killCounts = new HashMap<>();
        loadQuests();
    }

    private void loadQuests() {
        try {
            JsonReader jsonReader = new JsonReader();
            JsonValue root = jsonReader.parse(Gdx.files.internal("quests/Quest.json"));
            JsonValue questsArray = root.get("quests");

            for (JsonValue questJson : questsArray) {
                Quest quest = new Quest();
                quest.id = questJson.getInt("id");
                quest.name = questJson.getString("name");
                quest.description = questJson.getString("description");
                quest.status = questJson.getString("status");

                // Parse objectives
                JsonValue objectivesArray = questJson.get("objectives");
                quest.objectives = new ArrayList<>();

                for (JsonValue objJson : objectivesArray) {
                    QuestObjective objective = new QuestObjective();
                    objective.type = objJson.getString("type");
                    objective.target = objJson.getString("target");
                    objective.quantity = objJson.getInt("quantity");
                    objective.progress = 0;
                    quest.objectives.add(objective);

                    // Initialize kill counts for this target
                    if (objective.type.equals("kill")) {
                        killCounts.put(objective.target, 0);
                    }
                }

                // Parse rewards
                JsonValue rewardsJson = questJson.get("rewards");
                quest.rewards = new QuestRewards();
                quest.rewards.experience = rewardsJson.getInt("experience");
                quest.rewards.health = rewardsJson.getInt("health");

                quest.rewards.items = new ArrayList<>();
                JsonValue itemsArray = rewardsJson.get("items");
                for (JsonValue itemJson : itemsArray) {
                    QuestRewardItem item = new QuestRewardItem();
                    item.id = itemJson.getInt("id");
                    item.name = itemJson.getString("name");
                    item.quantity = itemJson.getInt("quantity");
                    quest.rewards.items.add(item);
                }

                quests.add(quest);
                questsById.put(quest.id, quest);
            }
        } catch (Exception e) {
            System.err.println("Error loading quests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startQuest(int questId) {
        Quest quest = questsById.get(questId);
        if (quest != null && quest.status.equals("not_started")) {
            quest.status = "in_progress";
            System.out.println("Quest started: " + quest.name);
        }
    }

    public void recordEnemyKill(String enemyType) {
        int currentKills = killCounts.getOrDefault(enemyType, 0);
        killCounts.put(enemyType, currentKills + 1);
        System.out.println("### QUEST SYSTEM: Killed " + enemyType + ": " + killCounts.get(enemyType) + " ###");

        // Log status of any active quests related to this enemy type
        for (Quest activeQuest : getAllActiveQuests()) {
            for (QuestObjective objective : activeQuest.objectives) {
                if (objective.type.equals("kill") && objective.target.equals(enemyType)) {
                    System.out.println("Quest current status: " + activeQuest.status);
                    break;
                }
            }
        }

        // Update all quests with this enemy type
        for (Quest quest : quests) {
            if (quest.status.equals("in_progress")) {
                boolean allComplete = true;

                for (QuestObjective objective : quest.objectives) {
                    if (objective.type.equals("kill") && objective.target.equals(enemyType)) {
                        objective.progress = killCounts.get(enemyType);

                        if (objective.progress < objective.quantity) {
                            allComplete = false;
                        }
                    } else if (objective.progress < objective.quantity) {
                        allComplete = false;
                    }
                }

                if (allComplete) {
                    completeQuest(quest);
                }
            }
        }
    }
    public Quest getQuestByName(String questName) {
        for (Quest quest : quests) {
            if (quest.name.equals(questName)) {
                return quest;
            }
        }
        return null;
    }

    public int getQuestIdByName(String questName) {
        Quest quest = getQuestByName(questName);
        return quest != null ? quest.id : -1;
    }

    public List<Quest> getAllActiveQuests() {
        List<Quest> activeQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.status.equals("in_progress")) {
                activeQuests.add(quest);
            }
        }
        return activeQuests;
    }

    public void completeQuest(Quest quest) {
        quest.status = "completed";
        System.out.println("Quest completed: " + quest.name);

        // Find any NPCs associated with this quest and update them
        // This would normally use an event system
    }

    public Quest getQuestById(int questId) {
        return questsById.get(questId);
    }

    public boolean isQuestCompleted(int questId) {
        Quest quest = questsById.get(questId);
        return quest != null && quest.status.equals("completed");
    }

    public String getQuestProgressText(int questId) {
        Quest quest = questsById.get(questId);
        if (quest == null) return "";

        StringBuilder sb = new StringBuilder();
        for (QuestObjective objective : quest.objectives) {
            if (objective.type.equals("kill")) {
                sb.append("Kill ").append(objective.target).append(": ")
                    .append(objective.progress).append("/").append(objective.quantity);
            }
        }
        return sb.toString();
    }

    public void saveQuestProgress() {
        // Code to save quest progress would go here

    }
}
