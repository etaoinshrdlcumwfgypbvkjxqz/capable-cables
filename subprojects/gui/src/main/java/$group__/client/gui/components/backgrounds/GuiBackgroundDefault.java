package $group__.client.gui.components.backgrounds;

import $group__.client.gui.utilities.Backgrounds;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;

@OnlyIn(Dist.CLIENT)
public class GuiBackgroundDefault extends GuiBackground {
	@Override
	public void renderBackground(MatrixStack matrix, Screen screen, Point2D mouse, float partialTicks) { Backgrounds.renderBackground(screen.getMinecraft(), screen.width, screen.height); }
}
