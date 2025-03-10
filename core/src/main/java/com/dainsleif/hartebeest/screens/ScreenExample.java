package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.dainsleif.hartebeest.helpers.GameInfo;

public class ScreenExample implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private Rectangle startButtonBounds;
    private Rectangle optionsButtonBounds;
    private Rectangle exitButtonBounds;
    private String startButtonText = "Start Game";
    private String optionsButtonText = "Options";
    private String exitButtonText = "Exit";
    Texture background;

    private boolean isTouched = false;

    public ScreenExample() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        layout = new GlyphLayout();

        //background image
        background = new Texture("Screen/MenuScreen/StartMenu.gif");

        // Start Button
        layout.setText(font, startButtonText, font.getColor(), 0, Align.center, false);
        startButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() - layout.height) / 2 + 50,
            layout.width,
            layout.height
        );

        // Options Button
        layout.setText(font, optionsButtonText, font.getColor(), 0, Align.center, false);
        optionsButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() - layout.height) / 2,
            layout.width,
            layout.height
        );

        // Exit Button
        layout.setText(font, exitButtonText, font.getColor(), 0, Align.center, false);
        exitButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() - layout.height) / 2 - 50,
            layout.width,
            layout.height
        );
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        font.draw(batch, startButtonText, startButtonBounds.x, startButtonBounds.y + startButtonBounds.height);
        font.draw(batch, optionsButtonText, optionsButtonBounds.x, optionsButtonBounds.y + optionsButtonBounds.height);
        font.draw(batch, exitButtonText, exitButtonBounds.x, exitButtonBounds.y + exitButtonBounds.height);
        batch.end();

        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true;
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (startButtonBounds.contains(touchPos)) {
                    // Handle Start button click
                    System.out.println("Start Button clicked!");
                } else if (optionsButtonBounds.contains(touchPos)) {
                    // Handle Options button click
                    System.out.println("Options Button clicked!");
                } else if (exitButtonBounds.contains(touchPos)) {
                    // Handle Exit button click
                    System.out.println("Exit Button clicked!");
                }
            }
        } else {
            isTouched = false;
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
