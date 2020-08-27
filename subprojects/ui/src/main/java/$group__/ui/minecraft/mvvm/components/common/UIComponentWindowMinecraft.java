package $group__.ui.minecraft.mvvm.components.common;

import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.rendering.IUIComponentRendererContainer;
import $group__.ui.core.parsers.binding.UIProperty;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.parsers.components.UIRendererConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.minecraft.core.mvvm.views.components.IUIComponentMinecraft;
import $group__.ui.minecraft.core.mvvm.views.components.rendering.IUIComponentRendererMinecraft;
import $group__.ui.minecraft.mvvm.components.rendering.UIComponentRendererMinecraft;
import $group__.ui.mvvm.views.components.common.UIComponentWindow;
import $group__.ui.mvvm.views.components.rendering.UIComponentRendererContainer;
import $group__.ui.utilities.BindingUtilities;
import $group__.ui.utilities.minecraft.DrawingUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class UIComponentWindowMinecraft
		extends UIComponentWindow
		implements IUIComponentMinecraft {
	protected final IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> rendererContainer =
			new UIComponentRendererContainer<>(new DefaultRenderer<>(ImmutableMap.of(), UIComponentWindowMinecraft.class));

	@UIComponentConstructor(type = UIComponentConstructor.ConstructorType.MAPPING__SHAPE_DESCRIPTOR)
	public UIComponentWindowMinecraft(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, IShapeDescriptor<RectangularShape> shapeDescriptor) { super(mapping, shapeDescriptor); }

	@Override
	public Optional<? extends IUIComponentRendererMinecraft<?>> getRenderer() { return getRendererContainer().getRenderer(); }

	@Override
	public void setRenderer(@Nullable IUIComponentRendererMinecraft<?> renderer) {
		IUIComponentRendererContainer.setRendererImpl(this, renderer,
				(s, r) -> s.getRendererContainer().setRenderer(r));
	}

	protected IUIComponentRendererContainer<IUIComponentRendererMinecraft<?>> getRendererContainer() { return rendererContainer; }

	public static class DefaultRenderer<C extends UIComponentWindowMinecraft>
			extends UIComponentRendererMinecraft<C> {
		public static final String PROPERTY_COLOR_BACKGROUND = INamespacePrefixedString.DEFAULT_PREFIX + "window.colors.background";
		public static final String PROPERTY_COLOR_BORDER = INamespacePrefixedString.DEFAULT_PREFIX + "window.colors.border";

		public static final INamespacePrefixedString PROPERTY_COLOR_BACKGROUND_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BACKGROUND);
		public static final INamespacePrefixedString PROPERTY_COLOR_BORDER_LOCATION = new NamespacePrefixedString(PROPERTY_COLOR_BORDER);

		@UIProperty(PROPERTY_COLOR_BACKGROUND)
		protected final IBindingField<Color> colorBackground;
		@UIProperty(PROPERTY_COLOR_BORDER)
		protected final IBindingField<Color> colorBorder;

		@UIRendererConstructor(type = UIRendererConstructor.ConstructorType.MAPPING__CONTAINER_CLASS)
		public DefaultRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mapping, Class<C> containerClass) {
			super(mapping, containerClass);

			this.colorBackground = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_BACKGROUND_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.BLACK);
			this.colorBorder = IHasBinding.createBindingField(Color.class,
					this.mapping.get(PROPERTY_COLOR_BORDER_LOCATION), BindingUtilities.Deserializers::deserializeColor, Color.WHITE);
		}

		@Override
		public void render(C container, IAffineTransformStack stack, Point2D cursorPosition, double partialTicks, boolean pre) {
			AffineTransform transform = stack.getDelegated().peek();
			if (pre) {
				Shape transformed = transform.createTransformedShape(container.getShapeDescriptor().getShapeOutput());
				getColorBackground().getValue().ifPresent(c ->
						DrawingUtilities.drawShape(transformed, true, c, 0));
				getColorBorder().getValue().ifPresent(c ->
						DrawingUtilities.drawShape(transformed, true, c, 0));
			}
		}

		public IBindingField<Color> getColorBackground() { return colorBackground; }

		public IBindingField<Color> getColorBorder() { return colorBorder; }
	}
}
