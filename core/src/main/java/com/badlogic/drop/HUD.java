package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class HUD implements Disposable {
    private final Viewport viewport;
    private final Stage stage;
    private int coinCount;

    private final Label coinLabel;

    public HUD(SpriteBatch spriteBatch) {
        this.coinCount = 0;
        this.viewport = new FitViewport(1366, 768, new OrthographicCamera());
        this.stage = new Stage(viewport, spriteBatch);

        // Vérifier si le fichier de police existe
        FileHandle fontFile = Gdx.files.internal("font.ttf");

        if (!fontFile.exists()) {
            throw new RuntimeException("Font file not found: font.ttf");
        }

        // Modification de police si besoin
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);

        coinLabel = new Label(String.format("Coins: %d", coinCount), labelStyle);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(coinLabel).expandX().padTop(10);

        stage.addActor(table);
    }
    public int getCoinCount() {
        return coinCount;
    }
    public void addCoin() {
        coinCount++;
        coinLabel.setText(String.format("Coins: %d", coinCount));
        Gdx.app.log("HUD", "Coin count increased to: " + coinCount);
    }

    public void draw() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
