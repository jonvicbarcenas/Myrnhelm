package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.dainsleif.hartebeest.helpers.AnimationLoader;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;

public class ScreenExample implements Screen {
    private SpriteBatch batch;
    private Animation<TextureRegion> animation;
    private float stateTime;

    private BitmapFont font;
    private GlyphLayout layout;
    private Rectangle startButtonBounds;
    private Rectangle optionsButtonBounds;
    private Rectangle exitButtonBounds;
    private String startButtonText = "Start Game";
    private String optionsButtonText = "Options";
    private String exitButtonText = "Exit";

    private boolean isTouched = false;

    Music backgroundMusic;

    @Override
    public void show() {
        font = new BitmapFont();
        layout = new GlyphLayout();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/dawn_winery_MenuBGM.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(GameInfo.getMusicVolume());
        backgroundMusic.play();

        SpriteSheetLoaderJson spriteSheetLoader = new SpriteSheetLoaderJson("Screen/MenuScreen/frieren.png", "Screen/MenuScreen/frieren.json");
        TextureRegion[] frames = spriteSheetLoader.getFrames();
        System.out.println("Frames loaded: " + frames.length);

        AnimationLoader animationLoader = new AnimationLoader(frames, .1f);
        animation = animationLoader.getAnimation();
        batch = new SpriteBatch();
        stateTime = 0.2f;

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
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);


        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
