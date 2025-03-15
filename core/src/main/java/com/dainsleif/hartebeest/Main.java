package com.dainsleif.hartebeest;

import com.badlogic.gdx.Game;
import com.dainsleif.hartebeest.screens.OptionsScreen;
import com.dainsleif.hartebeest.screens.ScreenExample;
import com.dainsleif.hartebeest.screens.ScreenExample1;
import com.dainsleif.hartebeest.world.Gameworld1;

public class Main extends Game {

    @Override
    public void create() {
        this.setScreen(new ScreenExample1());
    }


}
