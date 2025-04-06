package com.dainsleif.hartebeest.quests;

import java.util.List;

public class Quest {
    public int id;
    public String name;
    public String description;
    public List<QuestObjective> objectives;
    public QuestRewards rewards;
    public String status; // not_started, in_progress, completed
}

class QuestObjective {
    public String type;  // kill, collect, talk, etc.
    public String target;
    public int quantity;
    public int progress;
}

class QuestRewards {
    public int experience;
    public int health;
    public List<QuestRewardItem> items;
}

class QuestRewardItem {
    public int id;
    public String name;
    public int quantity;
}
