package com.dainsleif.hartebeest.helpers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class TileScan {
    private World world;
    private TiledMap map;
    private float tileWidth;
    private float tileHeight;

    public TileScan(World world, TiledMap map) {
        this.world = world;
        this.map = map;
        this.tileWidth = map.getProperties().get("tilewidth", Integer.class).floatValue();
        this.tileHeight = map.getProperties().get("tileheight", Integer.class).floatValue();
    }

    /**
     * Performs a raycast from player position in a specified direction
     *
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @param direction Direction of raycast (in degrees, 0 is right, 90 is up)
     * @param distance Distance of the raycast
     * @param layerIndex Index of the layer to check
     * @return The ID of the first tile hit, or -1 if no tile was hit
     */
    public int raycastForTileId(float playerX, float playerY, float direction, float distance, int layerIndex) {
        // Convert direction to radians
        float rads = (float) Math.toRadians(direction);

        // Calculate end point
        Vector2 start = new Vector2(playerX, playerY);
        Vector2 end = new Vector2(
            playerX + (float) Math.cos(rads) * distance,
            playerY + (float) Math.sin(rads) * distance
        );

        final int[] tileId = {-1};

        RayCastCallback callback = (fixture, point, normal, fraction) -> {
            // Convert world coordinates to tile coordinates
            int tileX = (int) (point.x / tileWidth);
            int tileY = (int) (point.y / tileHeight);

            // Get the specific layer
            if (map.getLayers().size() <= layerIndex) return 1;

            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

            if (cell != null && cell.getTile() != null) {
                int globalTileId = cell.getTile().getId() & 0x1FFFFFFF; // Clear flip flags

                // Determine the tileset this tile belongs to
                TiledMapTileSet tileset = null;
                for (TiledMapTileSet set : map.getTileSets()) {
                    int firstGid = (int) set.getProperties().get("firstgid");
                    if (globalTileId >= firstGid) {
                        tileset = set;
                    } else {
                        break;
                    }
                }

                if (tileset != null) {
                    int firstGid = (int) tileset.getProperties().get("firstgid");
                    tileId[0] = globalTileId - firstGid;
                }
                return 0; // Stop the raycast
            }
            return 1; // Continue the raycast
        };

        world.rayCast(callback, start, end);
        return tileId[0];
    }

    public int getTileIdAtPlayer(float playerX, float playerY, int layerIndex) {
        // Convert world coordinates to tile coordinates
        int tileX = (int) (playerX / tileWidth);
        int tileY = (int) (playerY / tileHeight);

        // Get the specific layer
        if (map.getLayers().size() <= layerIndex) return -1;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

        if (cell != null && cell.getTile() != null) {
            int globalTileId = cell.getTile().getId() & 0x1FFFFFFF; // Clear flip flags

            // Determine the tileset this tile belongs to
            TiledMapTileSet tileset = null;
            for (TiledMapTileSet set : map.getTileSets()) {
                int firstGid = (int) set.getProperties().get("firstgid");
                if (globalTileId >= firstGid) {
                    tileset = set;
                } else {
                    break;
                }
            }

            if (tileset != null) {
                int firstGid = (int) tileset.getProperties().get("firstgid");
                return globalTileId - firstGid;
            }
        }
        return -1;
    }

    public boolean isTileBlocked(float worldX, float worldY, int layerIndex) {
        // Convert world coordinates to tile coordinates
        int tileX = (int) (worldX / tileWidth);
        int tileY = (int) (worldY / tileHeight);

        // Get the specific layer
        if (map.getLayers().size() <= layerIndex) return false;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
        TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

        if (cell != null && cell.getTile() != null) {
            // Check if the tile has a "blocked" property
            return cell.getTile().getProperties().containsKey("blocked");
        }
        return false;
    }


}
