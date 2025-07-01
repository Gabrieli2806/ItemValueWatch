package com.g2806.worthwatch;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WorthWatchSimpleConfig extends Screen {
    private final Screen parent;
    private WorthWatchConfig config;

    public WorthWatchSimpleConfig(Screen parent) {
        super(Text.literal("Worth Watch Config"));
        this.parent = parent;
        this.config = WorthWatchConfig.getInstance();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 60;

        // Title
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Worth Watch Configuration").formatted(Formatting.BOLD),
                        button -> {})
                .dimensions(centerX - 100, startY - 30, 200, 20)
                .build()).active = false;

        // Position button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Position: " + config.position.getDisplayName()),
                        button -> {
                            config.cyclePosition();
                            button.setMessage(Text.literal("Position: " + config.position.getDisplayName()));
                        })
                .dimensions(centerX - 100, startY, 200, 20)
                .build());

        // Title toggle button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Show Title: " + (config.showTitle ? "ON" : "OFF")),
                        button -> {
                            config.toggleTitle();
                            button.setMessage(Text.literal("Show Title: " + (config.showTitle ? "ON" : "OFF")));
                            updateButtonText(button, "Show Title: " + (config.showTitle ? "ON" : "OFF"));
                        })
                .dimensions(centerX - 100, startY + 30, 200, 20)
                .build());

        // Total toggle button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Show Total: " + (config.showTotal ? "ON" : "OFF")),
                        button -> {
                            config.toggleTotal();
                            button.setMessage(Text.literal("Show Total: " + (config.showTotal ? "ON" : "OFF")));
                            updateButtonText(button, "Show Total: " + (config.showTotal ? "ON" : "OFF"));
                        })
                .dimensions(centerX - 100, startY + 60, 200, 20)
                .build());

        // Done button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> this.close())
                .dimensions(centerX - 50, startY + 100, 100, 20)
                .build());
    }

    private void updateButtonText(ButtonWidget button, String text) {
        button.setMessage(Text.literal(text));
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        // Draw instructions
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Configure your Worth Watch display settings").formatted(Formatting.GRAY),
                this.width / 2,
                this.height / 2 + 80,
                0xFFFFFF);

        // Draw current preview
        String previewText = String.format("Preview: %s | Title: %s | Total: %s",
                config.position.getDisplayName(),
                config.showTitle ? "ON" : "OFF",
                config.showTotal ? "ON" : "OFF");

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal(previewText).formatted(Formatting.YELLOW),
                this.width / 2,
                this.height / 2 + 100,
                0xFFFFFF);
    }
}