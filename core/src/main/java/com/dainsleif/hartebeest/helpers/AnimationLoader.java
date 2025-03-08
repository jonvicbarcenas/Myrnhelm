package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationLoader {
    private Animation<TextureRegion> animation;

    public AnimationLoader(TextureRegion[] frames, float frameDuration) {
        animation = new Animation<>(frameDuration, frames);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
}
