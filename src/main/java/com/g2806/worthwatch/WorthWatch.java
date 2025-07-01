package com.g2806.worthwatch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import java.text.NumberFormat;
import java.util.Locale;

public class WorthWatch implements ModInitializer {

	private static final int EMERALD_BLOCK_PRICE = 864;
	private static final int DIAMOND_BLOCK_PRICE = 432;
	private static final int GOLD_BLOCK_PRICE = 216;

	private static KeyBinding configKey;

	@Override
	public void onInitialize() {
		configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.worthwatch.config",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				"category.worthwatch.keys"
		));

		HudRenderCallback.EVENT.register(this::renderHud);
	}

	private void renderHud(DrawContext context, RenderTickCounter tickCounter) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (client.player == null || client.options.hudHidden) {
			return;
		}

		handleKeybinding(client);

		WorthWatchConfig config = WorthWatchConfig.getInstance();
		if (!config.showHud) { // Check HUD visibility
			return;
		}

		TextRenderer textRenderer = client.textRenderer;
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);

		int emeraldBlocks = countItemInInventory(client, Items.EMERALD_BLOCK);
		int diamondBlocks = countItemInInventory(client, Items.DIAMOND_BLOCK);
		int goldBlocks = countItemInInventory(client, Items.GOLD_BLOCK);

		int emeraldValue = emeraldBlocks * EMERALD_BLOCK_PRICE;
		int diamondValue = diamondBlocks * DIAMOND_BLOCK_PRICE;
		int goldValue = goldBlocks * GOLD_BLOCK_PRICE;
		int totalValue = emeraldValue + diamondValue + goldValue;

		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();
		int x, y;

		int maxWidth = 0;
		if (config.showTitle) {
			maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal("Worth Watch").formatted(Formatting.YELLOW, Formatting.BOLD)));
		}
		maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(emeraldBlocks + "x $" + formatter.format(emeraldValue)).formatted(Formatting.GREEN)));
		maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(diamondBlocks + "x $" + formatter.format(diamondValue)).formatted(Formatting.AQUA)));
		maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(goldBlocks + "x $" + formatter.format(goldValue)).formatted(Formatting.GOLD)));
		if (config.showTotal) {
			maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal("Total: $" + formatter.format(totalValue)).formatted(Formatting.WHITE, Formatting.BOLD)));
		}
		maxWidth += 22;

		switch (config.position) {
			case TOP_LEFT:
				x = 10;
				y = 10;
				break;
			case TOP_RIGHT:
				x = screenWidth - maxWidth - 10;
				y = 10;
				break;
			case BOTTOM_LEFT:
				x = 10;
				y = screenHeight - 80;
				break;
			case BOTTOM_RIGHT:
				x = screenWidth - maxWidth - 10;
				y = screenHeight - 80;
				break;
			default:
				x = screenWidth - maxWidth - 10;
				y = 10;
		}

		int lineHeight = 16;
		int currentY = y;

		if (config.showTitle) {
			Text title = Text.literal("Worth Watch").formatted(Formatting.YELLOW, Formatting.BOLD);
			context.drawText(textRenderer, title, x, currentY, 0xFFFFFF, true);
			currentY += lineHeight + 2;
		}

		if (emeraldBlocks > 0) {
			context.drawItem(new ItemStack(Items.EMERALD_BLOCK), x, currentY - 1);
			Text emeraldText = Text.literal(emeraldBlocks + "x $" + formatter.format(emeraldValue))
					.formatted(Formatting.GREEN);
			context.drawText(textRenderer, emeraldText, x + 22, currentY + 4, 0xFFFFFF, true);
		} else {
			context.drawItem(new ItemStack(Items.EMERALD_BLOCK), x, currentY - 1);
			Text emeraldText = Text.literal("0x $0").formatted(Formatting.GRAY);
			context.drawText(textRenderer, emeraldText, x + 22, currentY + 4, 0xFFFFFF, true);
		}
		currentY += lineHeight;

		if (diamondBlocks > 0) {
			context.drawItem(new ItemStack(Items.DIAMOND_BLOCK), x, currentY - 1);
			Text diamondText = Text.literal(diamondBlocks + "x $" + formatter.format(diamondValue))
					.formatted(Formatting.AQUA);
			context.drawText(textRenderer, diamondText, x + 22, currentY + 4, 0xFFFFFF, true);
		} else {
			context.drawItem(new ItemStack(Items.DIAMOND_BLOCK), x, currentY - 1);
			Text diamondText = Text.literal("0x $0").formatted(Formatting.GRAY);
			context.drawText(textRenderer, diamondText, x + 22, currentY + 4, 0xFFFFFF, true);
		}
		currentY += lineHeight;

		if (goldBlocks > 0) {
			context.drawItem(new ItemStack(Items.GOLD_BLOCK), x, currentY - 1);
			Text goldText = Text.literal(goldBlocks + "x $" + formatter.format(goldValue))
					.formatted(Formatting.GOLD);
			context.drawText(textRenderer, goldText, x + 22, currentY + 4, 0xFFFFFF, true);
		} else {
			context.drawItem(new ItemStack(Items.GOLD_BLOCK), x, currentY - 1);
			Text goldText = Text.literal("0x $0").formatted(Formatting.GRAY);
			context.drawText(textRenderer, goldText, x + 22, currentY + 4, 0xFFFFFF, true);
		}
		currentY += lineHeight;

		if (config.showTotal) {
			currentY += 2;
			Text totalText = Text.literal("Total: $" + formatter.format(totalValue))
					.formatted(Formatting.WHITE, Formatting.BOLD);
			context.drawText(textRenderer, totalText, x, currentY, 0xFFFF00, true);
		}
	}

	private void handleKeybinding(MinecraftClient client) {
		while (configKey.wasPressed()) {
			client.setScreen(new WorthWatchConfigScreen(client.currentScreen));
		}
	}

	private int countItemInInventory(MinecraftClient client, net.minecraft.item.Item item) {
		if (client.player == null) return 0;

		int count = 0;
		for (int i = 0; i < client.player.getInventory().size(); i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (stack.getItem() == item) {
				count += stack.getCount();
			}
		}
		return count;
	}
}