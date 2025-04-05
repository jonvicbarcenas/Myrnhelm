package com.dainsleif.hartebeest;

import com.badlogic.gdx.Game;
import com.dainsleif.hartebeest.screens.MenuScreen;
import com.dainsleif.hartebeest.screens.StoryScreen;
import com.dainsleif.hartebeest.screens.TesterScreen;
import com.dainsleif.hartebeest.world.Gameworld1;

public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new Gameworld1());
    }
}
