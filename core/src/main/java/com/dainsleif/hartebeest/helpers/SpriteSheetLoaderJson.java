package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class SpriteSheetLoaderJson {
    private Texture texture;
    private JsonValue json;

    public SpriteSheetLoaderJson(String texturePath, String jsonPath) {
        texture = new Texture(Gdx.files.internal(texturePath));
        FileHandle file = Gdx.files.internal(jsonPath);
        JsonReader jsonReader = new JsonReader();
        json = jsonReader.parse(file);
    }

    public TextureRegion[] getFrames() {
        JsonValue frames = json.get("frames");
        TextureRegion[] regions = new TextureRegion[frames.size];
        int index = 0;

        for (JsonValue frame : frames) {
            int x = frame.get("frame").getInt("x");
            int y = frame.get("frame").getInt("y");
            int w = frame.get("frame").getInt("w");
            int h = frame.get("frame").getInt("h");
            regions[index++] = new TextureRegion(texture, x, y, w, h);
        }

        return regions;
    }
}
