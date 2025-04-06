package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DeathStage extends Stage {
    private final Label deathLabel;
    private final Label retryLabel;
    private final BitmapFont font;
    private final BitmapFont smallFont;
    private final ShapeRenderer shapeRenderer;

    // Animation parameters
    private float animationTime = 0f;
    private float animationDuration = 1.5f;
    private float backgroundAlpha = 0f;
    private float textAlpha = 0f;
    private float textScale = 0.5f;
    private float retryTextAlpha = 0f;

    public DeathStage() {
        super(new ScreenViewport());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toriko.ttf"));

        // Main font for "You Died"
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        parameter.borderWidth = 6;
        parameter.borderColor = Color.DARK_GRAY;
        font = generator.generateFont(parameter);

        // Smaller font for "Press Enter to retry"
        parameter.size = 50;
        parameter.borderWidth = 2;
        smallFont = generator.generateFont(parameter);

        generator.dispose();

        shapeRenderer = new ShapeRenderer();

        // Death label setup
        Label.LabelStyle deathLabelStyle = new Label.LabelStyle();
        deathLabelStyle.font = font;
        deathLabelStyle.fontColor = new Color(1, 0, 0, 0);
        deathLabel = new Label("You\nDied", deathLabelStyle);

        // Retry label setup
        Label.LabelStyle retryLabelStyle = new Label.LabelStyle();
        retryLabelStyle.font = smallFont;
        retryLabelStyle.fontColor = new Color(1, 1, 1, 0);
        retryLabel = new Label("Press Enter to retry", retryLabelStyle);

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.add(deathLabel).width(250).expand().fill().center().row();
        table.add(retryLabel).padTop(50).center();
        addActor(table);
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, backgroundAlpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        deathLabel.getStyle().fontColor = new Color(1, 0, 0, textAlpha);
        deathLabel.setFontScale(textScale);

        retryLabel.getStyle().fontColor = new Color(1, 1, 1, retryTextAlpha);

        super.draw();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void update(float delta) {
        animationTime = Math.min(animationTime + delta, animationDuration + 1.0f);
        float progress = Math.min(animationTime / animationDuration, 1.0f);
        float easedProgress = Interpolation.pow3Out.apply(progress);

        backgroundAlpha = easedProgress * 0.65f;
        textAlpha = Interpolation.pow2Out.apply(0, 1, progress);
        textScale = Interpolation.swingOut.apply(0.5f, 1.0f, progress);

        // Only show retry text after main animation completes
        if (animationTime > animationDuration) {
            float retryProgress = Math.min((animationTime - animationDuration) / 0.5f, 1.0f);
            retryTextAlpha = Interpolation.fade.apply(retryProgress);
        }

        act(delta);
    }

    public void reset() {
        animationTime = 0f;
        backgroundAlpha = 0f;
        textAlpha = 0f;
        textScale = 0.5f;
        retryTextAlpha = 0f;
    }

    @Override
    public void dispose() {
        font.dispose();
        smallFont.dispose();
        shapeRenderer.dispose();
        super.dispose();
    }
}
