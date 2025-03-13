package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.world.Gameworld1;


public class ScreenExample1 implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private Rectangle startButtonBounds;
    private Rectangle optionsButtonBounds;
    private Rectangle exitButtonBounds;
    private Rectangle gameNameBounds;
    private String startButtonText = "Start Game";
    private String optionsButtonText = "Options";
    private String exitButtonText = "Exit";
    private String gameNameText = "Myrnhelm";
    Texture background;
    private boolean isTouched = false;

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Background image
        background = new Texture("Screen/MenuScreen/bg.gif");



        // Set font scale for buttons
        font.getData().setScale(1);

        layout.setText(font, gameNameText);
        gameNameBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() / 2) + 200,
            layout.width,
            layout.height
        );

        // Start Button
        layout.setText(font, startButtonText);
        startButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() / 2) + 50,
            layout.width,
            layout.height
        );

        // Options Button
        layout.setText(font, optionsButtonText);
        optionsButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() / 2),
            layout.width,
            layout.height
        );

        // Exit Button
        layout.setText(font, exitButtonText);
        exitButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() / 2) - 50,
            layout.width,
            layout.height
        );



    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);

        // Draw the game title at the top center
        font.getData().setScale(4); // Ensure correct font size for title
        layout.setText(font, gameNameText);
        font.draw(batch, gameNameText, (Gdx.graphics.getWidth() - layout.width) / 2, gameNameBounds.y + layout.height);

        // Reset font size for buttons
        font.getData().setScale(1);


        layout.setText(font, startButtonText);
        font.draw(batch, startButtonText, (Gdx.graphics.getWidth() - layout.width) / 2, startButtonBounds.y + layout.height);

        layout.setText(font, optionsButtonText);
        font.draw(batch, optionsButtonText, (Gdx.graphics.getWidth() - layout.width) / 2, optionsButtonBounds.y + layout.height);

        layout.setText(font, exitButtonText);
        font.draw(batch, exitButtonText, (Gdx.graphics.getWidth() - layout.width) / 2, exitButtonBounds.y + layout.height);

        batch.end();

        // Handle button clicks
        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true;
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (startButtonBounds.contains(touchPos)) {
                    System.out.println("Start Button clicked!");
                } else if (optionsButtonBounds.contains(touchPos)) {
                    System.out.println("Options Button clicked!");
                } else if (exitButtonBounds.contains(touchPos)) {
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
