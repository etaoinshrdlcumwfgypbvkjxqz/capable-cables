package $group__.client.gui.components.roots;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.backgrounds.GuiBackground;
import $group__.client.gui.components.common.GuiWindow;
import $group__.client.gui.structures.ShapeDescriptor;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiRootWindows<S extends ShapeDescriptor<? extends Rectangle2D>, D extends GuiRoot.Data<?, C>, C extends Container> extends GuiRoot<S, D, C> {
	protected final List<GuiWindow<?, ?>> windows = new ArrayList<>(INITIAL_CAPACITY_SMALL);

	public GuiRootWindows(ITextComponent title, Function<? super Rectangle2D, ? extends S> shape, D data) { super(title, shape, data); }

	@Override
	public void reshape(GuiComponent<?, ?> invoker, Consumer<? super S> transformer) {
		super.reshape(invoker, transformer);
		getWindows().forEach(w -> w.reshape(invoker));
	}

	protected List<GuiWindow<?, ?>> getWindows() { return windows; }

	protected void add(@Nullable GuiBackground<?, ?, ?> background, GuiWindow<?, ?>... windows) {
		if (background != null) super.add(background);
		super.add(windows);
		getWindows().addAll(ImmutableList.copyOf(windows));
	}

	public void add(GuiWindow<?, ?>... windows) {
		add(null, windows);
	}

	@Override
	public void remove(GuiComponent<?, ?>... components) {
		super.remove(components);
		List<GuiComponent<?, ?>> cl = Arrays.asList(components);
		getWindows().removeIf(cl::contains);
	}

	@Override
	@Deprecated
	public void add(GuiComponent<?, ?>... components) {
		@Nullable GuiBackground<?, ?, ?> background = null;
		GuiWindow<?, ?>[] windows = new GuiWindow[components.length];
		int i = 0;
		for (GuiComponent<?, ?> component : components) {
			if (component instanceof GuiBackground && background == null)
				background = (GuiBackground<?, ?, ?>) component;
			else if (component instanceof GuiWindow)
				windows[i++] = (GuiWindow<?, ?>) component;
			else
				throw BecauseOf.illegalArgument("components", Arrays.toString(components), "component", component);
		}
		if (background == null)
			add(windows);
		else
			add(background, Arrays.copyOf(windows, windows.length - 1));
	}

	public ImmutableList<GuiWindow<?, ?>> getWindowsView() { return ImmutableList.copyOf(getWindows()); }
}