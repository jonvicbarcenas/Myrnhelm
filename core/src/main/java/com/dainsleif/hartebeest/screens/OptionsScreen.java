package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dainsleif.hartebeest.helpers.AnimationLoader;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.SpriteSheetLoaderJson;

public class OptionsScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private Animation<TextureRegion> animation;
    private float stateTime;

    private Rectangle volTextBounds;
    private Rectangle plusVolBounds;
    private Rectangle minVolBounds;

    private Texture volTextTexture;
    private Texture plusVolTexture;
    private Texture minVolTexture;

    private TextureRegion volText;
    private TextureRegion plusVol;
    private TextureRegion minVol;

    private float scale = 3.0f;

    private boolean isTouched = false;
    Music backgroundMusic;

    @Override
    public void show() {
        // Initialize any necessary components
        batch = new SpriteBatch();

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

        volTextTexture = new Texture(Gdx.files.internal("Music.png"));
        minVolTexture = new Texture(Gdx.files.internal("minus.png"));
        plusVolTexture = new Texture(Gdx.files.internal("plus.png"));

//        volText = new TextureRegion(volTextTexture);
//        minVol = new TextureRegion(minVolTexture);
//        plusVol = new TextureRegion(plusVolTexture);

        volTextBounds = new Rectangle(
            250, // Left margin
            Gdx.graphics.getHeight() - (volTextTexture.getHeight() * scale) - 200, // Position from top
            volTextTexture.getWidth() * scale, // Scaled width
            volTextTexture.getHeight() * scale // Scaled height
        );

        minVolBounds = new Rectangle(
            volTextBounds.x + volTextBounds.width + 230,
            volTextBounds.y + (volTextBounds.height / 2) - (minVolTexture.getHeight() * scale / 2),
            minVolTexture.getWidth() * scale,
            minVolTexture.getHeight() * scale
        );

        plusVolBounds = new Rectangle(
            minVolBounds.x + minVolBounds.width + 100,
            volTextBounds.y + (volTextBounds.height / 2) - (plusVolTexture.getHeight() * scale / 2),
            plusVolTexture.getWidth() * scale,
            plusVolTexture.getHeight() * scale
        );

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.begin();

        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(volTextTexture, volTextBounds.x, volTextBounds.y, volTextBounds.width, volTextBounds.height);
        batch.draw(minVolTexture, minVolBounds.x, minVolBounds.y, minVolBounds.width, minVolBounds.height);
        batch.draw(plusVolTexture, plusVolBounds.x, plusVolBounds.y, plusVolBounds.width, plusVolBounds.height);

        batch.end();

        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true;
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (minVolBounds.contains(touchPos)) {
                    float newVolume = Math.max(0.0f, GameInfo.getMusicVolume() - 0.1f); // Clamp to minimum 0.0
                    GameInfo.setMusicVolume(newVolume);
                    backgroundMusic.setVolume(newVolume);
                    System.out.println("Volume decreased: " + newVolume);

                } else if (plusVolBounds.contains(touchPos)) {
                    float newVolume = Math.min(1.0f, GameInfo.getMusicVolume() + 0.1f); // Clamp to maximum 1.0
                    GameInfo.setMusicVolume(newVolume);
                    backgroundMusic.setVolume(newVolume);
                    System.out.println("Volume increased: " + newVolume);

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
        // Dispose resources properly
        batch.dispose();
        volTextTexture.dispose();
        plusVolTexture.dispose();
        minVolTexture.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }
}
