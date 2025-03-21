package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FpsStage extends Stage {
    private final Label fpsLabel;
    private final BitmapFont font;
    private float updateInterval = 0.5f;
    private float timeSinceUpdate = 0;

    public FpsStage() {
        super(new ScreenViewport());

        // Create font and label style
        font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Create FPS label
        fpsLabel = new Label("FPS: 0", labelStyle);

        // Position at top left with padding
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(10);
        table.add(fpsLabel);

        addActor(table);
    }

    public void update(float delta) {
        timeSinceUpdate += delta;

        if (timeSinceUpdate >= updateInterval) {
            fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            timeSinceUpdate = 0;
        }

        act(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
