package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.UIProperty;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.IUIRendererArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.construction.UIRendererConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.AutoCloseableGraphics2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.rendering.UIDefaultRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBindingKey;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.BindingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.ImmutableBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;
import org.jetbrains.annotations.NonNls;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressThisEscapedWarning;

public class UIShapeComponent
		extends UIDefaultComponent {
	private final IUIRendererContainerContainer<IUIComponentRenderer<?>> rendererContainerContainer;

	@UIComponentConstructor
	public UIShapeComponent(IUIComponentArguments arguments) {
		super(arguments);

		this.rendererContainerContainer =
				UIDefaultRendererContainerContainer.ofDefault(arguments.getRendererName().orElse(null), suppressThisEscapedWarning(() -> this),
						CastUtilities.castUnchecked(DefaultRenderer.class));
	}

	@Override
	protected IUIRendererContainerContainer<IUIComponentRenderer<?>> getRendererContainerContainer() {
		return rendererContainerContainer;
	}

	public static class DefaultRenderer<C extends UIShapeComponent>
			extends UIDefaultComponentRenderer<C> {
		public static final @NonNls String PROPERTY_FILLED_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.shape.color.filled";
		public static final @NonNls String PROPERTY_BORDER_COLOR = IHasBindingKey.StaticHolder.DEFAULT_PREFIX + "property.shape.color.border";
		private static final IIdentifier PROPERTY_FILLED_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyFilledColor());
		private static final IIdentifier PROPERTY_BORDER_COLOR_IDENTIFIER = ImmutableIdentifier.ofInterning(getPropertyBorderColor());

		@UIProperty(PROPERTY_FILLED_COLOR)
		private final IBindingField<Color> filledColor; // TODO Color to Paint
		@UIProperty(PROPERTY_BORDER_COLOR)
		private final IBindingField<Color> borderColor; // TODO Color to Paint

		@UIRendererConstructor
		public DefaultRenderer(IUIRendererArguments arguments) {
			super(arguments);

			Map<IIdentifier, IUIPropertyMappingValue> mappings = arguments.getMappingsView();
			this.filledColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.BLACK), mappings.get(getPropertyFilledColorIdentifier()));
			this.borderColor = IUIPropertyMappingValue.createBindingField(Color.class, ConstantValue.of(Color.WHITE), mappings.get(getPropertyBorderColorIdentifier()));
		}

		public static IIdentifier getPropertyFilledColorIdentifier() {
			return PROPERTY_FILLED_COLOR_IDENTIFIER;
		}

		public static IIdentifier getPropertyBorderColorIdentifier() {
			return PROPERTY_BORDER_COLOR_IDENTIFIER;
		}

		public static String getPropertyBorderColor() {
			return PROPERTY_BORDER_COLOR;
		}

		public static String getPropertyFilledColor() {
			return PROPERTY_FILLED_COLOR;
		}

		@Override
		public void render(IUIComponentContext context, EnumRenderStage stage) {
			super.render(context, stage);
			getContainer().ifPresent(container -> {
				if (stage == EnumRenderStage.PRE_CHILDREN) {
					Shape relativeShape = IUIComponent.getShape(container);
					try (AutoCloseableGraphics2D graphics = AutoCloseableGraphics2D.of(context.createGraphics())) {
						graphics.setPaint(getFilledColor().getValue());
						graphics.fill(relativeShape);
						graphics.setPaint(getBorderColor().getValue());
						graphics.draw(relativeShape);
					}
				}
			});
		}

		protected IBindingField<Color> getFilledColor() {
			return filledColor;
		}

		protected IBindingField<Color> getBorderColor() {
			return borderColor;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
			super.initializeBindings(binderObserverSupplier);
			BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
					ImmutableBinderAction.bind(getFilledColor(), getBorderColor()));
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public void cleanupBindings() {
			getBinderObserverSupplierHolder().getValue().ifPresent(binderObserverSupplier ->
					BindingUtilities.actOnBinderObserverSupplier(binderObserverSupplier, () ->
							ImmutableBinderAction.unbind(getFilledColor(), getBorderColor()))
			);
			super.cleanupBindings();
		}
	}
}
