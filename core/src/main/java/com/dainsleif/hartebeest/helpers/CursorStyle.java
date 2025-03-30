package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class CursorStyle {

    public void changeCursorToHand() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("Cursor/cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }

    public void changeCursorToSystemHand() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
    }
}
