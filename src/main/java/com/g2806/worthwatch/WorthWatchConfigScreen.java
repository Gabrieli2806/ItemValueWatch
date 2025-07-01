package com.g2806.worthwatch;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WorthWatchConfigScreen extends Screen {
    private final Screen parent;
    private WorthWatchConfig config;
    private ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public WorthWatchConfigScreen(Screen parent) {
        super(Text.literal("Worth Watch Config"));
        this.parent = parent;
        this.config = WorthWatchConfig.getInstance();
    }

    @Override
    protected void init() {
        this.layout.addHeader(Text.literal("Worth Watch Configuration").formatted(Formatting.BOLD), this.textRenderer);

        DirectionalLayoutWidget content = this.layout.addBody(DirectionalLayoutWidget.vertical());
        content.spacing(8);

        content.add(ButtonWidget.builder(
                        Text.literal("Position: " + config.position.getDisplayName()),
                        button -> {
                            config.cyclePosition();
                            button.setMessage(Text.literal("Position: " + config.position.getDisplayName()));
                        })
                .dimensions(0, 0, 200, 20)
                .build());

        content.add(ButtonWidget.builder(
                        Text.literal("Show Title: " + (config.showTitle ? "ON" : "OFF")),
                        button -> {
                            config.toggleTitle();
                            button.setMessage(Text.literal("Show Title: " + (config.showTitle ? "ON" : "OFF")));
                        })
                .dimensions(0, 0, 200, 20)
                .build());

        content.add(ButtonWidget.builder(
                        Text.literal("Show Total: " + (config.showTotal ? "ON" : "OFF")),
                        button -> {
                            config.toggleTotal();
                            button.setMessage(Text.literal("Show Total: " + (config.showTotal ? "ON" : "OFF")));
                        })
                .dimensions(0, 0, 200, 20)
                .build());

        content.add(ButtonWidget.builder(
                        Text.literal("Show HUD: " + (config.showHud ? "ON" : "OFF")),
                        button -> {
                            config.toggleHud();
                            button.setMessage(Text.literal("Show HUD: " + (config.showHud ? "ON" : "OFF")));
                        })
                .dimensions(0, 0, 200, 20)
                .build());

        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
                .dimensions(0, 0, 200, 20)
                .build());

        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        SimplePositioningWidget.setPos(this.layout, 0, 0, this.width, this.height, 0.5f, 0.25f);
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

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Configure your Worth Watch display settings").formatted(Formatting.GRAY),
                this.width / 2,
                this.height / 2 + 60,
                0xFFFFFF);
    }
}