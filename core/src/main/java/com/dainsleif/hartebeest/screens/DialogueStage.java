package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.dainsleif.hartebeest.npc.NPC;

public class DialogueStage {
    private final BitmapFont font;
    private final BitmapFont nameFont;
    private final ShapeRenderer shapeRenderer;
    private final GlyphLayout glyphLayout;
    private boolean isVisible = false;
    private String currentText = "";
    private String npcName = "";
    private NPC currentNpc;
    private float boxWidth = 180f;
    private float boxHeight = 60f;
    private final float PADDING = 5f;
    private final float NAME_HEIGHT = 20f;
    private float displayTime = 0f;
    private float maxDisplayTime = 5f;

    public DialogueStage() {
        // Initialize dialogue font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monogram.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        // Initialize name font
        parameter.size = 12;
        parameter.color = Color.YELLOW;
        nameFont = generator.generateFont(parameter);
        generator.dispose();

        // Initialize shape renderer for dialogue box
        shapeRenderer = new ShapeRenderer();

        // Initialize glyph layout for text sizing
        glyphLayout = new GlyphLayout();
    }

    public void showDialogue(NPC npc, String text) {
        currentNpc = npc;
        npcName = npc.getName();
        currentText = text;
        isVisible = true;
        displayTime = 0f;

        // Calculate box width based on text length (with upper/lower limits)
        boxWidth = Math.max(180f, Math.min(300f, text.length() * 5f));

        // Calculate text height with wrapping
        glyphLayout.setText(font, text, Color.WHITE, boxWidth - PADDING * 2, Align.left, true);
        float textHeight = glyphLayout.height;

        // Calculate box height based on text height plus padding and name area
        boxHeight = textHeight + NAME_HEIGHT + PADDING * 3;
    }

    public void hideDialogue() {
        isVisible = false;
        currentNpc = null;
    }

    public boolean isDialogueVisible() {
        return isVisible;
    }

    public void update(float delta) {
        if (isVisible) {
            displayTime += delta;
            if (displayTime > maxDisplayTime) {
                hideDialogue();
            }
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (!isVisible || currentNpc == null) return;

        // Get NPC position
        Vector2 npcPos = currentNpc.getPosition();

        // Position dialogue box above NPC
        float boxX = npcPos.x - boxWidth / 2;
        float boxY = npcPos.y + 40;

        Matrix4 batchMatrix = batch.getProjectionMatrix().cpy();
        batch.end();

        // Draw dialogue box background
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        // Draw box border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        // Restore batch and draw text
        batch.setProjectionMatrix(batchMatrix);
        batch.begin();

        // Draw NPC name
        nameFont.draw(batch, npcName, boxX + PADDING, boxY + boxHeight - PADDING);

        // Draw dialogue text
        font.draw(batch, currentText, boxX + PADDING,
            boxY + boxHeight - NAME_HEIGHT - PADDING,
            boxWidth - PADDING * 2, Align.left, true);
    }

    public void dispose() {
        font.dispose();
        nameFont.dispose();
        shapeRenderer.dispose();
    }
}
