package com.dainsleif.hartebeest.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Handles map transitions between screens
 */
public class TransitionMapHandler {
    private TiledMap map;
    private String transitionLayerName;
    private int fallbackLayerIndex;
    private String targetMap;

    /**
     * Creates a transition handler for the specified map
     * @param map The TiledMap to handle transitions for
     * @param transitionLayerName Name of the transitions layer
     * @param fallbackLayerIndex Layer index to use if name isn't found
     */
    public TransitionMapHandler(TiledMap map, String transitionLayerName, int fallbackLayerIndex, String targetMap) {
        this.map = map;
        this.transitionLayerName = transitionLayerName;
        this.fallbackLayerIndex = fallbackLayerIndex;
        this.targetMap = targetMap;
    }

    /**
     * Checks if player position triggers a transition
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @return The screen to transition to, or null if no transition
     */
    public Screen checkTransitions(float playerX, float playerY) {
        MapLayer transitionLayer = map.getLayers().get(transitionLayerName);
        if (transitionLayer == null) {
            transitionLayer = map.getLayers().get(fallbackLayerIndex);
        }

        for (MapObject object : transitionLayer.getObjects()) {
            Rectangle rect = null;
            if (object instanceof RectangleMapObject) {
                rect = ((RectangleMapObject) object).getRectangle();
            } else if (object instanceof com.badlogic.gdx.maps.objects.PolygonMapObject) {
                com.badlogic.gdx.maps.objects.PolygonMapObject polyObj =
                    (com.badlogic.gdx.maps.objects.PolygonMapObject) object;
                rect = polyObj.getPolygon().getBoundingRectangle();
            }

            if (rect != null && rect.contains(playerX, playerY)) {
                System.out.println("TRANSITION TRIGGERED! Target: " + targetMap);

                // Create the appropriate screen based on target
                // This can be expanded based on different target values
                if (targetMap.equals("Gameworld1")) {
                    return new Gameworld1();
                } else if (targetMap.equals("StartAreaMap")) {
                    //teleport first player to the start area
                    StartAreaMap startAreaMap = new StartAreaMap();
                    startAreaMap.playerSwitcher.setPlayerPosition(new Vector2(86, 578));
                    return startAreaMap;
                }
            }
        }
        return null;
    }

    /**
     * Executes the transition to a new screen
     * @param currentScreen Current screen that will be disposed
     * @param nextScreen Screen to transition to
     * @param backgroundMusic Music to stop before transition
     */
    public void executeTransition(Screen currentScreen, Screen nextScreen, Music backgroundMusic) {


        // Schedule the screen change
        final Screen screenToTransition = nextScreen;
        Gdx.app.postRunnable(() -> {
            ((Game)Gdx.app.getApplicationListener()).setScreen(screenToTransition);
        });

        // Now it's safe to dispose the current screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
