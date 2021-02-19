package dev.eeasee.gui_hanger.config;

import dev.eeasee.gui_hanger.sprites.SpriteManager;
import dev.eeasee.gui_hanger.sprites.renderer.BaseSprite;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.math.Matrix4f;

import java.io.*;
import java.util.Properties;

public class Configs {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "GUIHanger.properties");

    public static boolean sendScreenToServerIfSupported = true;
    public static boolean localLoopIfServerNotSupported = true;
    public static boolean displayInClientWorld = true;
    public static float hangedGUIScale = 0.5f;

    public static void onScaleChanged(float newValue) {
        hangedGUIScale = newValue;
        BaseSprite.SCALE_NUM = Configs.hangedGUIScale * 2.5f / 256.0f;
        BaseSprite.SCALE_MATRIX = Matrix4f.scale(BaseSprite.SCALE_NUM, BaseSprite.SCALE_NUM, BaseSprite.SCALE_NUM);
        for (BaseSprite sprite : SpriteManager.ACTIVE_SPRITES.values()) {
            sprite.setChanged();
        }
    }

    private static void read() {
        Properties properties = new Properties();
        FileInputStream configInputStream;
        try {
            configInputStream = new FileInputStream(CONFIG_FILE);
        } catch (FileNotFoundException e) {
            try {
                CONFIG_FILE.createNewFile();
                write();
                configInputStream = new FileInputStream(CONFIG_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
        try {
            properties.load(configInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendScreenToServerIfSupported = Boolean.parseBoolean(properties.getProperty("sendScreenToServerIfSupported"));
        localLoopIfServerNotSupported = Boolean.parseBoolean(properties.getProperty("localLoopIfServerNotSupported"));
        displayInClientWorld = Boolean.parseBoolean(properties.getProperty("displayInClientWorld"));
        hangedGUIScale = Float.parseFloat(properties.getProperty("hangedGUIScale"));
    }

    static void write() {
        Properties properties = new Properties();
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(CONFIG_FILE);
        } catch (FileNotFoundException e) {
            try {
                CONFIG_FILE.createNewFile();
                outputStream = new FileOutputStream(CONFIG_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
        try {
            properties.setProperty("sendScreenToServerIfSupported", Boolean.toString(sendScreenToServerIfSupported));
            properties.setProperty("localLoopIfServerNotSupported", Boolean.toString(localLoopIfServerNotSupported));
            properties.setProperty("displayInClientWorld", Boolean.toString(displayInClientWorld));
            properties.setProperty("hangedGUIScale", Float.toString(hangedGUIScale));
            properties.store(outputStream, "Configs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        read();
    }


}
