package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dainsleif.hartebeest.players.PlayerMyron;

public class PlayerStatStage extends Stage {

    private final Label healthLabel;
    private final Label manaLabel;

    private final BitmapFont font;
    private float updateInterval = 0.1f;
    private float timeSinceUpdate = 0;


    public PlayerStatStage() {
        super(new ScreenViewport());

        // Create font and label style
        font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Create health label
        healthLabel = new Label("Health: 0", labelStyle);
        manaLabel = new Label("Mana: 0", labelStyle);


        // Position at top left with padding
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(10).padTop(50);
        table.add(healthLabel);
        addActor(table);
    }

    public void update(float delta) {
        timeSinceUpdate += delta;

        if (timeSinceUpdate >= updateInterval) {
            healthLabel.setText("Health: " + PlayerMyron.getHealth());
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
