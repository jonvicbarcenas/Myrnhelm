package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum EnemyState implements State<EnemyGoblinGdxAi> {
    IDLE,

    WALK_LEFT,
    WALK_RIGHT,
    WALK_UP,
    WALK_DOWN,

    ATTACKING,
    ATTACKING_SPIN,

    DEAD;

    @Override
    public void enter(EnemyGoblinGdxAi enemyGoblinGdxAi) {

    }

    @Override
    public void update(EnemyGoblinGdxAi enemyGoblinGdxAi) {

    }

    @Override
    public void exit(EnemyGoblinGdxAi enemyGoblinGdxAi) {

    }

    @Override
    public boolean onMessage(EnemyGoblinGdxAi enemyGoblinGdxAi, Telegram telegram) {
        return false;
    }
}
