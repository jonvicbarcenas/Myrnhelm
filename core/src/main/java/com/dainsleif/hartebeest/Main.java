package com.dainsleif.hartebeest;

import com.badlogic.gdx.Game;
import com.dainsleif.hartebeest.helpers.test;
import com.dainsleif.hartebeest.screens.ScreenExample;
import com.dainsleif.hartebeest.world.Gameworld1;

public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new ScreenExample());
    }
}
