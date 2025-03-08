package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class test implements Screen {
    private Animation<TextureRegion> animation;
    private SpriteBatch spriteBatch;
    private float stateTime;

    @Override
    public void show() {
        SpriteSheetLoaderJson spriteSheetLoader = new SpriteSheetLoaderJson("sprite/bunny/Running.png", "sprite/bunny/Running.json");
        TextureRegion[] frames = spriteSheetLoader.getFrames();
        AnimationLoader animationLoader = new AnimationLoader(frames, 0.1f);
        animation = animationLoader.getAnimation();
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        // Render the current frame (example)
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);
        spriteBatch.end();
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

    }
}
