package com.dainsleif.hartebeest.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    private TiledMap map;
    private List<TiledMapTileLayer> collisionLayers;
    private float tileWidth;
    private float tileHeight;
    private String blockingProperty;
    private World world;

    public CollisionDetector(World world, TiledMap map, List<Integer> collisionLayerIndices, String blockingProperty) {
        this.world = world;
        this.map = map;
        this.collisionLayers = new ArrayList<>();
        for (int index : collisionLayerIndices) {
            this.collisionLayers.add((TiledMapTileLayer) map.getLayers().get(index));
        }
        this.tileWidth = map.getProperties().get("tilewidth", Integer.class).floatValue();
        this.tileHeight = map.getProperties().get("tileheight", Integer.class).floatValue();
        this.blockingProperty = blockingProperty;

        createCollisionBodies();
    }

    public CollisionDetector(World world, TiledMap map, List<Integer> collisionLayerIndices) {
        this(world, map, collisionLayerIndices, "blocked");
    }

    private void createCollisionBodies() {
        for (TiledMapTileLayer layer : collisionLayers) {
            for (int x = 0; x < layer.getWidth(); x++) {
                for (int y = 0; y < layer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null && cell.getTile() != null &&
                        cell.getTile().getProperties().containsKey(blockingProperty)) {

                        BodyDef bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(
                            (x * tileWidth) + (tileWidth / 2),
                            (y * tileHeight) + (tileHeight / 2)
                        );

                        Body body = world.createBody(bodyDef);

                        PolygonShape shape = new PolygonShape();
                        shape.setAsBox(tileWidth / 2, tileHeight / 2);

                        FixtureDef fixtureDef = new FixtureDef();
                        fixtureDef.shape = shape;
                        fixtureDef.density = 1.0f;
                        fixtureDef.friction = 0.4f;
                        fixtureDef.restitution = 0.0f;

                        body.createFixture(fixtureDef);
                        shape.dispose();
                    }
                }
            }
        }
    }

    private boolean isCellBlocked(float worldX, float worldY) {
        int tileX = (int) (worldX / tileWidth);
        int tileY = (int) (worldY / tileHeight);

        for (TiledMapTileLayer layer : collisionLayers) {
            TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);
            if (cell != null && cell.getTile() != null &&
                cell.getTile().getProperties().containsKey(blockingProperty)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesRight(float x, float y, float width, float height) {
        float stepSize = tileHeight / 2;
        for (float step = 0; step < height; step += stepSize) {
            if (isCellBlocked(x + width, y + step)) {
                return true;
            }
        }
        return isCellBlocked(x + width, y + height);
    }

    public boolean collidesLeft(float x, float y, float width, float height) {
        float stepSize = tileHeight / 2;
        for (float step = 0; step < height; step += stepSize) {
            if (isCellBlocked(x, y + step)) {
                return true;
            }
        }
        return isCellBlocked(x, y + height);
    }

    public boolean collidesTop(float x, float y, float width, float height) {
        float stepSize = tileWidth / 2;
        for (float step = 0; step < width; step += stepSize) {
            if (isCellBlocked(x + step, y + height)) {
                return true;
            }
        }
        return isCellBlocked(x + width, y + height);
    }

    public boolean collidesBottom(float x, float y, float width, float height) {
        float stepSize = tileWidth / 2;
        for (float step = 0; step < width; step += stepSize) {
            if (isCellBlocked(x + step, y)) {
                return true;
            }
        }
        return isCellBlocked(x + width, y);
    }

    public boolean canMoveTo(float x, float y, float width, float height, float newX, float newY) {
        if (newX > x && collidesRight(x, y, width, height)) {
            return false;
        } else if (newX < x && collidesLeft(x, y, width, height)) {
            return false;
        }

        if (newY > y && collidesTop(x, y, width, height)) {
            return false;
        } else if (newY < y && collidesBottom(x, y, width, height)) {
            return false;
        }

        return true;
    }

    public void setCollisionLayers(List<Integer> layerIndices) {
        this.collisionLayers.clear();
        for (int index : layerIndices) {
            this.collisionLayers.add((TiledMapTileLayer) map.getLayers().get(index));
        }
    }

    public List<Integer> getCollisionLayerIndices() {
        List<Integer> indices = new ArrayList<>();
        for (TiledMapTileLayer layer : collisionLayers) {
            indices.add(map.getLayers().getIndex(layer));
        }
        return indices;
    }

    public boolean isBlocked(float x, float y) {
        return isCellBlocked(x, y);
    }
}
