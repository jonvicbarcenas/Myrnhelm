package com.dainsleif.hartebeest.helpers;

public class VariableTimeAnimation<T> {
    private final T[] keyFrames;
    private final float[] frameDurations;
    private final float totalDuration;

    public VariableTimeAnimation(T[] keyFrames, float[] frameDurations) {
        this.keyFrames = keyFrames;
        this.frameDurations = frameDurations;

        float total = 0;
        for (float duration : frameDurations) {
            total += duration;
        }
        this.totalDuration = total;
    }

    public T getKeyFrame(float stateTime, boolean looping) {
        if (keyFrames.length == 0) return null;

        if (looping) {
            stateTime = stateTime % totalDuration;
        } else if (stateTime >= totalDuration) {
            return keyFrames[keyFrames.length - 1];
        }

        float currentTime = 0;
        for (int i = 0; i < keyFrames.length; i++) {
            currentTime += frameDurations[i];
            if (stateTime < currentTime) {
                return keyFrames[i];
            }
        }

        return keyFrames[keyFrames.length - 1];
    }
}
