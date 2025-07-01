package com.g2806.worthwatch;

import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorthWatchConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "worthwatch.json");

    public enum Position {
        TOP_LEFT("Top Left"),
        TOP_RIGHT("Top Right"),
        BOTTOM_LEFT("Bottom Left"),
        BOTTOM_RIGHT("Bottom Right");

        private final String displayName;

        Position(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Position position = Position.TOP_RIGHT;
    public boolean showTitle = true;
    public boolean showTotal = true;
    public boolean showHud = true; // New field for HUD visibility

    private static WorthWatchConfig instance;

    public static WorthWatchConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static WorthWatchConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                WorthWatchConfig config = GSON.fromJson(reader, WorthWatchConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException e) {
                System.err.println("Error loading WorthWatch config: " + e.getMessage());
            }
        }

        WorthWatchConfig defaultConfig = new WorthWatchConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Error saving WorthWatch config: " + e.getMessage());
        }
    }

    public void cyclePosition() {
        Position[] positions = Position.values();
        int currentIndex = position.ordinal();
        position = positions[(currentIndex + 1) % positions.length];
        save();
    }

    public void toggleTitle() {
        showTitle = !showTitle;
        save();
    }

    public void toggleTotal() {
        showTotal = !showTotal;
        save();
    }

    public void toggleHud() { // New method for toggling HUD visibility
        showHud = !showHud;
        save();
    }
}