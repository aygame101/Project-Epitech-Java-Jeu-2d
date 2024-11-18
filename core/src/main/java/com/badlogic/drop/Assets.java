package com.badlogic.drop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public static AssetManager manager = new AssetManager();

    public static void load() {
        manager.load("assets/skins/uiskin.json", Skin.class);
        manager.load("assets/skins/default.fnt", BitmapFont.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}
