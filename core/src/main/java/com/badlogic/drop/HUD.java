package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
    //Long jump
    private static int NLongJump;
    public static int LJitem = 0;
    //les warp
    public static int WarpItems = 0;
    private static int NWarp;
    //les message afficher
    private final Label coinLabel;
    private static Label longJumpLabel = null;
    private static Label warpLabel = null;

    public HUD(SpriteBatch spriteBatch) {
        this.coinCount = 0;
        this.NLongJump = 3;
        this.NWarp = 1;
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

        Label.LabelStyle coinlabelStyle = new Label.LabelStyle(font, Color.GOLD);
        Label.LabelStyle NLongJumplabelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label.LabelStyle NWarplabelStyle = new Label.LabelStyle(font, Color.FIREBRICK);

        coinLabel = new Label(String.format("Coins: %d", coinCount), coinlabelStyle);
        if (LJitem == 0){
            longJumpLabel = new Label(String.format(""), NLongJumplabelStyle);
        }
        if (WarpItems == 0){
            warpLabel = new Label(String.format(""), NWarplabelStyle);
        }
        //"Press 'space' and 'Q' or 'D' to Warp on a direction"

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.add(coinLabel).expandY().padLeft(75).padTop(-675);
        table.add(longJumpLabel).expandY().padLeft(-90).padTop(-600);
        table.add(warpLabel).expandY().padLeft(100).padTop(-600);

        stage.addActor(table);
    }

    //coins
    public int getCoinCount() {
        return coinCount;
    }
    public void addCoin() {
        coinCount++;
        coinLabel.setText(String.format("Coins: %d", coinCount));
        Gdx.app.log("HUD", "Coin count increased to: " + coinCount);
    }
    //fin coins

    //Long jump
    public static int getNLongJump() {return NLongJump;}
    public static void addNLongJump() {
        NLongJump--;
        longJumpLabel.setText(String.format("(Press 'K' to deactivate) Long Jump left: %d", NLongJump));

    }
    public static void ResLongJump(){
        NLongJump = 3;
        if(!Player.GotLongJump()){
            longJumpLabel.setText(String.format("Press L to activate the long jump"));
        } else {
            longJumpLabel.setText(String.format("(Press 'K' to deactivate) Long Jump left: %d", NLongJump));
        }
    }
    public static void set_NJ_view(){
        longJumpLabel.setText(String.format("(Press 'K' to deactivate) Long Jump left: %d", NLongJump));
    }
    public static void set_NoJ_view(){
        longJumpLabel.setText(String.format("Press 'L' to activate the long jump"));
    }
    //fin Long Jump

    //Warp
    public static int getNWarp() {return NWarp;}
    public static void addNWarp() {
        NWarp--;
        warpLabel.setText(String.format("Press 'space' to Warp on a direction, warp left: %d", NWarp));
    }
    public static void ResWarp(){
        NWarp=1;
        warpLabel.setText(String.format("Press 'space' to Warp on a direction, warp left: %d", NWarp));
    }

    public void draw() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
