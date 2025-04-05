package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dainsleif.hartebeest.helpers.GameInfo;
import com.dainsleif.hartebeest.helpers.KeyHandler;
import com.dainsleif.hartebeest.world.Gameworld1;


public class StoryScreen implements Screen {
    private SpriteBatch batch;
    StoryStage stanza;
    private boolean isFlag = false;
    KeyHandler keyHandler;

    private Rectangle playBounds;
    private Rectangle bgBounds;

    private Texture playTexture;
    private Texture playClickedTexture;
    private Texture backgroundTexture;

    private TextureRegion play;
    private TextureRegion playClicked;
    private TextureRegion background;

    private boolean isPlayClicked = false;

    private boolean isTouched = false;

    @Override
    public void show() {
        batch = new SpriteBatch();

        playTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/play.png"));
        playClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/playClicked.png"));
        backgroundTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/bg1.png"));

        play = new TextureRegion(playTexture);
        playClicked = new TextureRegion(playClickedTexture);
        background = new TextureRegion(backgroundTexture);

        float scaleFactor = 3.0f; // Adjust as needed

        // Start Button Bounds
        float playScaledWidth = play.getRegionWidth() * scaleFactor;
        float playScaledHeight = play.getRegionHeight() * scaleFactor;

        playBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - playScaledWidth) / 2) + 500,
            (Gdx.graphics.getHeight() / 2) - 350,
            playScaledWidth,
            playScaledHeight
        );

        float bgScaleFactor = 16.5f;

        float bgScaledWidth = play.getRegionWidth() * bgScaleFactor;
        float bgScaledHeight = play.getRegionHeight() * bgScaleFactor;

        bgBounds = new Rectangle(
            (Gdx.graphics.getWidth() - bgScaledWidth) / 2,
            (Gdx.graphics.getHeight() / 2) - 300,
            bgScaledWidth,
            bgScaledHeight
        );

        stanza = new StoryStage();
        keyHandler = new KeyHandler();

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        batch.begin();

        batch.draw(background, bgBounds.x, bgBounds.y, bgBounds.width, bgBounds.height);

        batch.end();


        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!isFlag) {
                isFlag = true;
                System.out.println("Enter pressed");
                System.out.println("Space pressed");
                stanza.next();
            }
        }else if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            if (!isFlag) {
                isFlag = true;
                System.out.println("Esc pressed");
                System.out.println(stanza.getStoryIndex() + " " + stanza.getStoryLength());
                stanza.storyEnd();
                stanza.next();
            }
        } else {
            isFlag = false;
        }

        if (stanza.getStoryIndex() == stanza.getStoryLength()) {
            batch.begin();
            TextureRegion playTexture = isPlayClicked ? playClicked : play;

            batch.draw(playTexture, playBounds.x, playBounds.y, playBounds.width, playBounds.height);

            batch.end();

            if (Gdx.input.isTouched()) {
                if (!isTouched) {
                    isTouched = true;
                    Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                    if (playBounds.contains(touchPos)) {
                        isPlayClicked = true;
                        System.out.println("play clicked!.");
                    }
                }
            } else {
                if(isPlayClicked) {
                    isPlayClicked = false;
                    System.out.println("play clicked!.");
                }
                isTouched = false;
            }
        }

        stanza.update(v); // Update the stage
        stanza.draw(); // Draw the stage
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stanza.dispose();
    }
}
