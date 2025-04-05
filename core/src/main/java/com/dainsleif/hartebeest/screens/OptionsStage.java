package com.dainsleif.hartebeest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionsStage extends Stage {
    private final Label pausedLabel;
    private final Label resumeLabel;
    private final Label quitLabel;
    private final Label volLabel;
    private final BitmapFont pausedFont;
    private final BitmapFont resumeFont;
    private final BitmapFont quitFont;

    private Texture plusVolTexture;
    private Texture plusVolClickedTexture;
    private Texture minVolTexture;
    private Texture minVolClickedTexture;
    private TextureRegion plusVol;
    private TextureRegion plusVolClicked;
    private TextureRegion minVol;
    private TextureRegion minVolClicked;
    private Rectangle plusVolBounds;
    private Rectangle minVolBounds;

    private boolean isPlusVolClicked = false;
    private boolean isMinVolClicked = false;
    private boolean isTouched = false;

    public OptionsStage() {
        super(new ScreenViewport());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Toriko.ttf"));

        // Font for "Paused"
        FreeTypeFontGenerator.FreeTypeFontParameter pausedParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pausedParameter.size = 100; // Set the desired font size for "Paused"
        pausedParameter.borderWidth = 6;
        pausedParameter.borderColor = Color.DARK_GRAY;
        pausedFont = generator.generateFont(pausedParameter);

        // Font for "Resume" and "Quit Game"
        FreeTypeFontGenerator.FreeTypeFontParameter resumeQuitParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        resumeQuitParameter.size = 50; // Set the desired font size for "Resume" and "Quit Game"
        resumeQuitParameter.borderWidth = 3;
        resumeQuitParameter.borderColor = Color.DARK_GRAY;
        resumeFont = generator.generateFont(resumeQuitParameter);
        quitFont = generator.generateFont(resumeQuitParameter);

        generator.dispose();

        Label.LabelStyle pausedLabelStyle = new Label.LabelStyle();
        pausedLabelStyle.font = pausedFont;
        pausedLabelStyle.fontColor = Color.WHITE;

        Label.LabelStyle resumeQuitLabelStyle = new Label.LabelStyle();
        resumeQuitLabelStyle.font = resumeFont;
        resumeQuitLabelStyle.fontColor = Color.WHITE;

        // Create label with "Paused" text
        pausedLabel = new Label("Paused", pausedLabelStyle);

        // Create clickable labels for "Resume" and "Quit Game"
        resumeLabel = new Label("Resume", resumeQuitLabelStyle);
        quitLabel = new Label("Quit Game", resumeQuitLabelStyle);

        volLabel = new Label("Volume", resumeQuitLabelStyle);

        // Add listeners to labels
        resumeLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("OptionsStage", "Resume clicked");
                System.out.println("Resume clicked");
            }
        });

        quitLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("OptionsStage", "Quit clicked");
                System.out.println("Quit clicked");
                Gdx.app.exit();
            }
        });

        // Load textures
        plusVolTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/plus.png"));
        plusVolClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/plusClicked.png"));
        minVolTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/minus.png"));
        minVolClickedTexture = new Texture(Gdx.files.internal("sprite/MenuSprite/minusClicked.png"));
        plusVol = new TextureRegion(plusVolTexture);
        plusVolClicked = new TextureRegion(plusVolClickedTexture);
        minVol = new TextureRegion(minVolTexture);
        minVolClicked = new TextureRegion(minVolClickedTexture);

        // Define bounds for the textures
        float scaleFactor = 2.0f;
        float plusVolScaledWidth = plusVol.getRegionWidth() * scaleFactor;
        float plusVolScaledHeight = plusVol.getRegionHeight() * scaleFactor;
        float minVolScaledWidth = minVol.getRegionWidth() * scaleFactor;
        float minVolScaledHeight = minVol.getRegionHeight() * scaleFactor;

        plusVolBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - plusVolScaledWidth) / 2) + 100,
            (Gdx.graphics.getHeight() / 2) - 75,
            plusVolScaledWidth,
            plusVolScaledHeight
        );

        minVolBounds = new Rectangle(
            ((Gdx.graphics.getWidth() - minVolScaledWidth) / 2) - 100,
            (Gdx.graphics.getHeight() / 2) - 75,
            minVolScaledWidth,
            minVolScaledHeight
        );

        Table table = new Table();
        table.setFillParent(true);
        table.top(); // Align the table to the top
        table.add(pausedLabel).expandX().padTop(20).center(); // Center the label horizontally and add padding at the top
        table.row();
        table.add(resumeLabel).padTop(50).center(); // Add resume label with padding
        table.row();
        table.add(quitLabel).padTop(20).center(); // Add quit label with padding
        table.row();
        table.add(volLabel).padTop(10).center(); // Add quit label with padding
        addActor(table);

        // Set the input processor to this stage
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void draw() {
        super.draw();
        getBatch().begin();
        getBatch().draw(isPlusVolClicked ? plusVolClicked : plusVol, plusVolBounds.x, plusVolBounds.y, plusVolBounds.width, plusVolBounds.height);
        getBatch().draw(isMinVolClicked ? minVolClicked : minVol, minVolBounds.x, minVolBounds.y, minVolBounds.width, minVolBounds.height);
        getBatch().end();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Gdx.input.isTouched()) {
            if (!isTouched) {
                isTouched = true;
                Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (minVolBounds.contains(touchPos)) {
                    isMinVolClicked = true;
                } else if (plusVolBounds.contains(touchPos)) {
                    isPlusVolClicked = true;
                }
            }
        } else {
            if (isMinVolClicked) {
                isMinVolClicked = false;
            }
            if (isPlusVolClicked) {
                isPlusVolClicked = false;
            }
            isTouched = false;
        }
    }

    public void update(float delta) {
        act(delta);
    }

    public void dispose() {
        super.dispose();
        pausedFont.dispose();
        resumeFont.dispose();
        quitFont.dispose();
        plusVolTexture.dispose();
        plusVolClickedTexture.dispose();
        minVolTexture.dispose();
        minVolClickedTexture.dispose();
    }
}
