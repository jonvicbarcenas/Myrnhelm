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
        int frameCount = frames.size;
        TextureRegion[] regions = new TextureRegion[frameCount];

        for (int i = 0; i < frameCount; i++) {
            JsonValue frame = frames.get(i);
            JsonValue frameData = frame.get("frame");
            int x = frameData.getInt("x");
            int y = frameData.getInt("y");
            int width = frameData.getInt("w");
            int height = frameData.getInt("h");

            regions[i] = new TextureRegion(texture, x, y, width, height);
        }

        return regions;
    }


    public TextureRegion[] getFrames(String direction) {
        JsonValue meta = json.get("meta");
        JsonValue frameTags = meta.get("frameTags");

        int fromIndex = -1;
        int toIndex = -1;

        // Find the matching direction tag
        for (JsonValue tag : frameTags) {
            if (tag.getString("name").equals(direction)) {
                fromIndex = tag.getInt("from");
                toIndex = tag.getInt("to");
                break;
            }
        }

        // If direction not found, return empty array
        if (fromIndex == -1 || toIndex == -1) {
            return new TextureRegion[0];
        }

        // Create array for the frames in this direction
        int frameCount = toIndex - fromIndex + 1;
        TextureRegion[] regions = new TextureRegion[frameCount];

        // Get all frames
        JsonValue frames = json.get("frames");

        // Extract only the frames for the specified direction
        for (int i = 0; i < frameCount; i++) {
            JsonValue frame = frames.get(fromIndex + i);
            JsonValue frameData = frame.get("frame");
            int x = frameData.getInt("x");
            int y = frameData.getInt("y");
            int width = frameData.getInt("w");
            int height = frameData.getInt("h");

            regions[i] = new TextureRegion(texture, x, y, width, height);
        }

        return regions;
    }
}
