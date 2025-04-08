package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.audio.Sound;


public class CreditStage extends Stage {
    private final Label titleLabel;
    private final Label ankarosDialogueLabel;
    private final Label[] developerLabels;
    private final BitmapFont titleFont;
    private final BitmapFont nameFont;
    private final ShapeRenderer shapeRenderer;

    private final Sound evilLaughSound;

    private float animationTime = 0f;
    private float animationDuration = 3.0f;
    private float backgroundAlpha = 0f;
    private float textAlpha = 0f;

    public CreditStage() {
        super(new ScreenViewport());

        shapeRenderer = new ShapeRenderer();

        // Load and play evil laugh sound
        evilLaughSound = Gdx.audio.newSound(Gdx.files.internal("Music/evil_laugh_02.ogg"));

        // Create fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toriko.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.GRAY;
        parameter.color = Color.WHITE;
        titleFont = generator.generateFont(parameter);

        // Developer names font
        parameter.size = 30;
        parameter.borderWidth = 1;
        parameter.color = Color.WHITE;
        nameFont = generator.generateFont(parameter);

        generator.dispose();

        // Create styles
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = new Color(1, 1, 1, 0);

        Label.LabelStyle nameStyle = new Label.LabelStyle();
        nameStyle.font = nameFont;
        nameStyle.fontColor = new Color(1, 1, 1, 0);

        // Create labels
        titleLabel = new Label("Is it the end or ??", titleStyle);

        Label.LabelStyle dialogueStyle = new Label.LabelStyle();
        dialogueStyle.font = titleFont;
        dialogueStyle.fontColor = new Color(106/255f, 30/255f, 116/255f, 1);

        ankarosDialogueLabel = new Label("Ankaros: HA HA HA HA!", dialogueStyle);

        String[] developers = {
            "Jon Vic Barcenas",
            "Elijah Thomas Rellon",
            "Joseph James Banico",
            "Myron Deandre Alia",
            "Ma. Melessa Cabasag"
        };

        developerLabels = new Label[developers.length];
        for (int i = 0; i < developers.length; i++) {
            developerLabels[i] = new Label(developers[i], nameStyle);
        }

        // Create layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(titleLabel).padBottom(50).row();
        table.add(ankarosDialogueLabel).padBottom(55).row();

        for (Label label : developerLabels) {
            table.add(label).padBottom(20).row();
        }

        addActor(table);
    }

    public void update(float delta) {
        // Update animation
        if (animationTime < animationDuration) {
            animationTime += delta;

            // Calculate alpha values based on animation time
            backgroundAlpha = Math.min(1.0f, animationTime / (animationDuration * 0.3f));
            textAlpha = Math.min(1.0f, (animationTime - animationDuration * 0.2f) / (animationDuration * 0.5f));

            // Update label colors
            titleLabel.getStyle().fontColor.a = textAlpha;

            ankarosDialogueLabel.getStyle().fontColor.a = textAlpha;

            for (Label label : developerLabels) {
                label.getStyle().fontColor.a = textAlpha;
            }
        }

        act(delta);
    }

    public void playEvilLaugh() {
        if (evilLaughSound != null) {
            evilLaughSound.play();
        }
    }

    @Override
    public void draw() {
        // Draw background
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, backgroundAlpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Draw actors
        super.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        titleFont.dispose();
        nameFont.dispose();
        shapeRenderer.dispose();
    }
}
