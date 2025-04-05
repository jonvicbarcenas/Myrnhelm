package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DeathStage extends Stage {
    private final Label label;
    private final BitmapFont font;

    public DeathStage() {
        super(new ScreenViewport());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toriko.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200; // Set the desired font size
        parameter.borderWidth = 6;
        parameter.borderColor = Color.DARK_GRAY;
        font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.RED;


        // Create FPS label
        label = new Label("You\nDied", labelStyle);


        Table table = new Table();
        table.setFillParent(true);
        table.add(label).width(250).expand().fill().center();
        addActor(table);
    }

    public void update(float delta) {
        act(delta);
    }

    public void dispose() {
        super.dispose();
    }

}
