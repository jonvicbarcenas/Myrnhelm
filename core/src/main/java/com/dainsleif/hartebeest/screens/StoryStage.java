package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StoryStage extends Stage {
    private final Label label;
    private final BitmapFont font;

    private int storyIndex = 0;

    private String[] story = {
        "Once upon a time, in a land far, far away...\n Myron, a wandering warrior seeking to\n master his skills, arrives at Alderwynds.\n" +
            " Alderwynds lively town known for its rich \nculture and thriving guild.\n\n\n\n" +
            " Excited, he prepares to register with the \nwarrior’s guild, hoping to prove himself.\n" +
            "However, one night, a terrible calamity \nbefalls the town. Undead creatures rise, \nled by a dark sorcerer." +
            "Chaos erupts as \ncivilians flee, buildings burn, and despair \nconsumes Alderwynds.\n\n\n\n" +
            "Amid the destruction, an NPC guide, a wise \nfigure who welcomed Myron upon arrival" +
            ",\n urges him to fight back and uncover \nthe truth behind the attack.\n\n\n\n\n\n",

            " But as Myron delves deeper into the \nconflict, " +
        "he discovers that the fall of \nAlderwynds is not just the work of the \nundead" +
        ", a much darker force has been at \nplay long before his arrival.\n\n\n\n" +

        " The three bosses he faces were once \nprotectors of the town" +
        "but something \nturned them into villains." +
        "The truth? \nThe NPC guide who once aided him is the \ntrue mastermind " +
        "manipulating events from \nthe shadows " +
        "to render Alderwynds unlivable \nfor his own mysterious purpose. " +
        "Now, Myron\n and his allies must face the horrors of the\n past, " +
        "defeat the corrupted bosses and \nconfront the NPC guide in a final reckoning \nthat will determine the town’s fate.\n\n\n\n\n\n\n\n"
    };

    public StoryStage() {
        super(new ScreenViewport());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toriko.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30; // Set the desired font size
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        generator.dispose();


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;


        // Create FPS label
        label = new Label(story[0], labelStyle);


        Table table = new Table();
        table.setFillParent(true);
        table.add(label).width(450).expand().fill().center();
        storyIndex = 1;

        addActor(table);
    }

    public void update(float delta) {
        act(delta);
    }

    public void dispose() {
        super.dispose();
    }

    public void next() {
        if (storyIndex < story.length) {
            label.setText(story[storyIndex]);
            storyIndex++;
        }else if (storyIndex == story.length && storyIndex < story.length+1){
            label.setText(story[storyIndex-1]);
            storyIndex++;
        }
    }

    public void storyEnd() {
        storyIndex = story.length-1;
    }

    public int getStoryIndex(){
        return storyIndex;
    }

    public int getStoryLength(){
        return story.length;
    }


}
