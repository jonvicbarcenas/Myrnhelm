package com.dainsleif.hartebeest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    TiledMap map;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    Texture char1Texture;
    SpriteBatch spriteBatch;
    Sprite char1Sprite;
    FitViewport viewport;

    @Override
    public void create() {
        // Prepare your application here.
        map = new TmxMapLoader().load("Map.tmx");
        char1Texture = new Texture("avatar.png");
        spriteBatch = new SpriteBatch();
        char1Sprite = new Sprite(char1Texture);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 800);
        camera.update();
        viewport = new FitViewport(800, 800, camera);

    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        // Draw your application here.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        viewport.getCamera().translate(0,0,0);
        viewport.update(Gdx.graphics.getWidth() + 500, Gdx.graphics.getHeight() + 500);
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();
        spriteBatch.begin();
        char1Sprite.draw(spriteBatch);
        spriteBatch.end();

    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
