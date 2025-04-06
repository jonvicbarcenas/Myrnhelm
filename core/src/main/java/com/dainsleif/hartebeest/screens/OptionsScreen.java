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
    private Animation<TextureRegion> animation;
    private float stateTime;

    private Rectangle volTextBounds;
    private Rectangle plusVolBounds;
    private Rectangle minVolBounds;
    private Rectangle backBounds;

    private Texture volTextTexture;
    private Texture plusVolTexture;
    private Texture minVolTexture;
    private Texture backTexture;
    private Texture plusVolClickedTexture;
    private Texture minVolClickedTexture;
    private Texture backClickedTexture;


    private TextureRegion volText;
    private TextureRegion plusVol;
    private TextureRegion minVol;
    private TextureRegion back;
    private TextureRegion plusVolClicked;
    private TextureRegion minVolClicked;
    private TextureRegion backClicked;


    private boolean isPlusVolClicked = false;
    private boolean isMinVolClicked = false;
    private boolean isBackClicked = false;

    private boolean isTouched = false;
    Music backgroundMusic;

    public OptionsScreen(Music backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();


        // Load sprite sheet and animation frames
        SpriteSheetLoaderJson spriteSheetLoader = new SpriteSheetLoaderJson("Screen/MenuScreen/MenuBG.png", "Screen/MenuScreen/MenuBG.json");
        TextureRegion[] frames = spriteSheetLoader.getFrames();
        System.out.println("Frames loaded: " + frames.length);

        AnimationLoader animationLoader = new AnimationLoader(frames, .1f);
        animation = animationLoader.getAnimation();
        batch = new SpriteBatch();
        stateTime = 0.2f;

        volTextTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/Music.png"));
        minVolTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/minus.png"));
        plusVolTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/plus.png"));
        backTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/back.png"));
        minVolClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/minusClicked.png"));
        plusVolClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/plusClicked.png"));
        backClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/backClicked.png"));

        volText = new TextureRegion(volTextTexture);
        minVol = new TextureRegion(minVolTexture);
        plusVol = new TextureRegion(plusVolTexture);
        back = new TextureRegion(backTexture);
        plusVolClicked = new TextureRegion(plusVolClickedTexture);
        minVolClicked = new TextureRegion(minVolClickedTexture);
        backClicked = new TextureRegion(backClickedTexture);

        float scaleFactor = 3.0f;

        float backScaledWidth = back.getRegionWidth() * scaleFactor;
        float backScaledHeight = back.getRegionHeight() * scaleFactor;

        backBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - backScaledWidth) / 2) - 500,
            (Gdx.graphics.getHeight() / 2) + 250,
            backScaledWidth,
            backScaledHeight
        );

        float volTextScaledWidth = volText.getRegionWidth() * scaleFactor;
        float volTextScaledHeight = volText.getRegionHeight() * scaleFactor;

        volTextBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - volTextScaledWidth) / 2) - 275,
            (Gdx.graphics.getHeight() / 2) + 125,
            volTextScaledWidth,
            volTextScaledHeight
        );

        float minVolScaledWidth = minVol.getRegionWidth() * scaleFactor;
        float minVolScaledHeight = minVol.getRegionHeight() * scaleFactor;

        minVolBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - minVolScaledWidth) / 2) + 150,
            (Gdx.graphics.getHeight() / 2) + 100,
            minVolScaledWidth,
            minVolScaledHeight
        );

        float plusVolScaledWidth = plusVol.getRegionWidth() * scaleFactor;
        float plusVolScaledHeight = plusVol.getRegionHeight() * scaleFactor;

        plusVolBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - plusVolScaledWidth) / 2) + 350,
            (Gdx.graphics.getHeight() / 2) + 100,
            plusVolScaledWidth,
            plusVolScaledHeight
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

        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth() + 50, Gdx.graphics.getHeight());

        TextureRegion backBtnTexture = isBackClicked ? backClicked : back;
        TextureRegion minusTexture = isMinVolClicked ? minVolClicked : minVol;
        TextureRegion plusTexture = isPlusVolClicked ? plusVolClicked : plusVol;

        batch.draw(backBtnTexture, backBounds.x, backBounds.y, backBounds.width, backBounds.height);
        batch.draw(volTextTexture, volTextBounds.x, volTextBounds.y, volTextBounds.width, volTextBounds.height);
        batch.draw(minusTexture, minVolBounds.x, minVolBounds.y, minVolBounds.width, minVolBounds.height);
        batch.draw(plusTexture, plusVolBounds.x, plusVolBounds.y, plusVolBounds.width, plusVolBounds.height);

        batch.end();

        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true;
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (minVolBounds.contains(touchPos)) {
                    isMinVolClicked = true;
                    float newVolume = Math.max(0.0f, GameInfo.getMusicVolume() - 0.1f); // Clamp to minimum 0.0
                    GameInfo.setMusicVolume(newVolume);
                    backgroundMusic.setVolume(newVolume);
                    System.out.println("Volume decreased: " + newVolume);

                } else if (plusVolBounds.contains(touchPos)) {
                    isPlusVolClicked = true;
                    float newVolume = Math.min(1.0f, GameInfo.getMusicVolume() + 0.1f); // Clamp to maximum 1.0
                    GameInfo.setMusicVolume(newVolume);
                    backgroundMusic.setVolume(newVolume);
                    System.out.println("Volume increased: " + newVolume);

                } else if (backBounds.contains(touchPos)) {
                    isBackClicked = true;
                }
            }
        } else {
            if (isMinVolClicked) {
                isMinVolClicked = false;
            }
            if (isPlusVolClicked) {
                isPlusVolClicked = false;
            }
            if (isBackClicked){
                isBackClicked = false;
                ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
            }
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
        backTexture.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }
}//
