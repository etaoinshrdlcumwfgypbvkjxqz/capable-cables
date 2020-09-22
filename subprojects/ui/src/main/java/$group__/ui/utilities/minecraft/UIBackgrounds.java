package $group__.ui.utilities.minecraft;

import $group__.ui.events.bus.UIEventBusEntryPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public enum UIBackgrounds {
	;

	public static void notifyBackgroundDrawn(Screen screen) { UIEventBusEntryPoint.getEventBus().onNext(new GuiScreenEvent.BackgroundDrawnEvent(screen)); }

	/**
	 * @see Screen#renderBackground()
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height) {
		UIScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground();
	}

	/**
	 * @see Screen#renderBackground(int)
	 */
	public static void renderBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		UIScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderBackground(blitOffset);
	}

	/**
	 * @see Screen#renderDirtBackground(int)
	 */
	public static void renderDirtBackgroundAndNotify(@Nullable Minecraft client, int width, int height, int blitOffset) {
		UIScreenUtility.getInstance()
				.setClient_(client)
				.setWidth_(width)
				.setHeight_(height)
				.renderDirtBackground(blitOffset);
	}
}
