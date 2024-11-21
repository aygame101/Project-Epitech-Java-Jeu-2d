package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class HUD implements Disposable {
    private final Viewport viewport;
    private final SpriteBatch spriteBatch;
    private int coinCount;
    private final BitmapFont font;
    private final Label coinLabel;
    private final Table table;

    public HUD(SpriteBatch spriteBatch) {
        this.coinCount = 0;
        this.spriteBatch = spriteBatch;
        this.viewport = new FitViewport(1366, 768, new OrthographicCamera());

        // Vérifier si le fichier de police existe
        FileHandle fontFile = Gdx.files.internal("font.ttf");

        if (!fontFile.exists()) {
            throw new RuntimeException("Font file not found: font.ttf");
        }

        // Custom font if needed
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);

        coinLabel = new Label(String.format("Coins: %d", coinCount), labelStyle);

        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(coinLabel).expandX().padTop(10);
    }

    public void addCoin() {
        coinCount++;
        coinLabel.setText(String.format("Coins: %d", coinCount));
    }

    public void draw() {
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin(); // Commence un nouveau dessin

        // Dessine tous les éléments du tableau
        table.draw(spriteBatch, 1);

        spriteBatch.end(); // Termine le dessin
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
