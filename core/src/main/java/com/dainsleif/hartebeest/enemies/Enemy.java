package com.dainsleif.hartebeest.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    private final String mobName;
    protected int maxHealth;
    protected int health;
    protected int damage;
    protected float speed;
    protected float stateTime;
    protected Vector2 position;
    protected Rectangle hitbox;


    public Enemy(String mobName,int health, int damage, float speed, Vector2 position, Rectangle hitbox) {
        this.mobName = mobName;
        this.maxHealth = health;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.position = position;
        this.hitbox = new Rectangle(position.x, position.y, hitbox.width, hitbox.height);
    }

    public void update() {
    }
    public void render() {
    }


    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) die();
    }

    protected void die() {
        System.out.println("Enemy :" + mobName + " has died");
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public String getMobName(){
        return mobName;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

}
