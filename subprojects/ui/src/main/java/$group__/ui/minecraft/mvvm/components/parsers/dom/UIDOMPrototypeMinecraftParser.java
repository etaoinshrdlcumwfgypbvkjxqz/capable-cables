package $group__.ui.minecraft.mvvm.components.parsers.dom;

import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.parsers.components.UIDOMPrototypeParser;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDOMPrototypeMinecraftParser<T extends IUIComponentManager<?>>
		extends UIDOMPrototypeParser<T> {
	public UIDOMPrototypeMinecraftParser(Class<T> managerClass) { super(managerClass); }

	public static <T extends UIDOMPrototypeMinecraftParser<?>> T makeParserMinecraft(T parser) {
		// TODO add something here
		return parser;
	}
}
