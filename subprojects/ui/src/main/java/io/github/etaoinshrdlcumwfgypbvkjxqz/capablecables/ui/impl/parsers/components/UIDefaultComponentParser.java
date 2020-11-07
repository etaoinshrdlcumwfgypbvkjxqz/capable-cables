package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import com.google.common.collect.*;
import com.google.common.reflect.TypeToken;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventComponentType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.types.EnumUIEventDOMType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserUncheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.UIViewComponentConstructor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.binding.UIImmutablePropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBImmutableAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.LegacyJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.IUIAbstractDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.UIImmutableDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers.UIDefaultDefaultComponentParserAnchorHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers.UIDefaultDefaultComponentParserExtensionHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers.UIDefaultDefaultComponentParserRendererContainerHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.ShapeDescriptorBuilderFactoryRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.AnnotationUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ITypeCapture;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.awt.geom.AffineTransform;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;

public class UIDefaultComponentParser<T extends IUIViewComponent<?, ?>>
		extends UIAbstractDefaultComponentParser<T, ComponentUI, ComponentUI, IUIDefaultComponentParserContext>
		implements ITypeCapture {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final @Immutable Map<IUIEventType, Function<@Nonnull Component, @Nonnull Optional<String>>> EVENT_TYPE_FUNCTION_MAP = ImmutableMap.<IUIEventType, Function<Component, Optional<String>>>builder()
			.put(EnumUIEventDOMType.LOAD, Component::getLoad)
			.put(EnumUIEventDOMType.UNLOAD, Component::getUnload)
			.put(EnumUIEventDOMType.ABORT, Component::getAbort)
			.put(EnumUIEventDOMType.ERROR, Component::getError)
			.put(EnumUIEventDOMType.SELECT, Component::getSelect)
			.put(EnumUIEventDOMType.FOCUS_OUT_POST, Component::getBlur)
			.put(EnumUIEventDOMType.FOCUS_IN_POST, Component::getFocus)
			.put(EnumUIEventDOMType.FOCUS_IN_PRE, Component::getFocusin)
			.put(EnumUIEventDOMType.FOCUS_OUT_PRE, Component::getFocusout)
			.put(EnumUIEventDOMType.CLICK_AUXILIARY, Component::getAuxclick)
			.put(EnumUIEventDOMType.CLICK, Component::getClick)
			.put(EnumUIEventDOMType.CLICK_DOUBLE, Component::getDblclick)
			.put(EnumUIEventDOMType.MOUSE_DOWN, Component::getMousedown)
			.put(EnumUIEventDOMType.MOUSE_ENTER, Component::getMouseenter)
			.put(EnumUIEventDOMType.MOUSE_LEAVE, Component::getMouseleave)
			.put(EnumUIEventDOMType.MOUSE_MOVE, Component::getMousemove)
			.put(EnumUIEventDOMType.MOUSE_LEAVE_SELF, Component::getMouseout)
			.put(EnumUIEventDOMType.MOUSE_ENTER_SELF, Component::getMouseover)
			.put(EnumUIEventDOMType.MOUSE_UP, Component::getMouseup)
			.put(EnumUIEventDOMType.WHEEL, Component::getWheel)
			.put(EnumUIEventDOMType.INPUT_PRE, Component::getBeforeinput)
			.put(EnumUIEventDOMType.INPUT_POST, Component::getInput)
			.put(EnumUIEventDOMType.KEY_DOWN, Component::getKeydown)
			.put(EnumUIEventDOMType.KEY_UP, Component::getKeyup)
			.put(EnumUIEventDOMType.COMPOSITION_START, Component::getCompositionstart)
			.put(EnumUIEventDOMType.COMPOSITION_UPDATE, Component::getCompositionupdate)
			.put(EnumUIEventDOMType.COMPOSITION_END, Component::getCompositionend)
			.put(EnumUIEventComponentType.CHAR_TYPED, Component::getCharTyped)
			.build();
	@SuppressWarnings("UnstableApiUsage")
	private final TypeToken<T> typeToken;

	@SuppressWarnings("UnstableApiUsage")
	public UIDefaultComponentParser(Class<T> type) {
		this.typeToken = TypeToken.of(type);
	}

	public static <T extends UIDefaultComponentParser<?>> T makeParserStandard(T instance) {
		instance.addObjectHandler(Anchor.class, new UIDefaultDefaultComponentParserAnchorHandler());
		instance.addObjectHandler(RendererContainer.class, new UIDefaultDefaultComponentParserRendererContainerHandler());
		instance.addObjectHandler(Extension.class, new UIDefaultDefaultComponentParserExtensionHandler());
		return instance;
	}

	@SuppressWarnings("UnstableApiUsage")
	public static Map<INamespacePrefixedString, IUIPropertyMappingValue> createMappings(Iterable<? extends Property> properties) {
		return Streams.stream(properties).unordered()
				.map(p -> Maps.immutableEntry(ImmutableNamespacePrefixedString.of(p.getKey()),
						new UIImmutablePropertyMappingValue(p.getAny()
								.map(any -> IJAXBAdapterRegistry.adaptFromJAXB(JAXBImmutableAdapterContext.of(LegacyJAXBAdapterRegistry.getRegistry()), any))
								.orElse(null),
								p.getBindingKey()
										.map(ImmutableNamespacePrefixedString::of)
										.orElse(null))))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static IShapeDescriptorBuilder<?> createShapeDescriptorBuilder(IUIDefaultComponentParserContext context, Shape shape) {
		AffineTransform transform = shape.getAffineTransform()
				.map(transform1 -> IJAXBAdapterRegistry.adaptFromJAXB(JAXBImmutableAdapterContext.of(LegacyJAXBAdapterRegistry.getRegistry()), transform1))
				.map(AffineTransform.class::cast)
				.orElseGet(AffineTransform::new);

		IShapeDescriptorBuilder<?> sdb = ShapeDescriptorBuilderFactoryRegistry.getDefaultFactory()
				.createBuilder(
						CastUtilities.castUnchecked(AssertionUtilities.assertNonnull(context.getAliasesView().get(shape.getClazz()))) // COMMENT should not throw
				);

		shape.getProperty().stream().unordered()
				.forEach(p -> {
					assert !p.getBindingKey().isPresent();
					sdb.setProperty(p.getKey(), p.getAny()
							.map(any -> IJAXBAdapterRegistry.adaptFromJAXB(JAXBImmutableAdapterContext.of(LegacyJAXBAdapterRegistry.getRegistry()), any))
							.orElse(null));
				});

		return sdb.setX(shape.getX()).setY(shape.getY()).setWidth(shape.getWidth()).setHeight(shape.getHeight())
				.transformConcatenate(transform)
				.constrain(shape.getConstraint().stream()
						.map(c -> new ShapeConstraint(
								c.getMinX().orElse(null),
								c.getMinY().orElse(null),
								c.getMaxX().orElse(null),
								c.getMaxY().orElse(null),
								c.getMinWidth().orElse(null),
								c.getMinHeight().orElse(null),
								c.getMaxWidth().orElse(null),
								c.getMaxHeight().orElse(null)
						))
						.collect(ImmutableList.toImmutableList()));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public TypeToken<? extends T> getTypeToken() { return typeToken; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@SuppressWarnings("RedundantThrows")
	@Override
	protected Iterable<? extends Using> getRawAliases(ComponentUI resource)
			throws UIParserCheckedException, UIParserUncheckedException { return resource.getUsing(); }

	public static IUIViewComponent<?, ?> createView(IUIDefaultComponentParserContext context, View view)
			throws Throwable {
		Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(view.getProperty());
		UIViewComponentConstructor.IArguments argument = new UIViewComponentConstructor.ImmutableArguments(mappings);

		Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(view.getClazz()));
		Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIViewComponentConstructor.class, Arrays.asList(clazz.getDeclaredConstructors()));
		MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);

		return (IUIViewComponent<?, ?>) constructorHandle.invoke(argument);
	}

	public static IUIComponent createComponent(IUIDefaultComponentParserContext context, Component component)
			throws Throwable {
		return TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, component,
				IThrowingFunction.executeNow(node -> {
					assert node != null;
					Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings = createMappings(node.getProperty());
					// COMMENT attributes
					{
						Map<INamespacePrefixedString, IUIPropertyMappingValue> attributes = new HashMap<>(getEventTypeFunctionMap().size());

						getEventTypeFunctionMap()
								.forEach((key, value) -> {
									assert key != null;
									assert value != null;
									attributes.put(key.getEventType(),
											new UIImmutablePropertyMappingValue(null,
													AssertionUtilities.assertNonnull(value.apply(node))
															.map(IUIEventType.StaticHolder.getDefaultPrefix()::concat)
															.map(ImmutableNamespacePrefixedString::of)
															.orElse(null)));
								});

						mappings = MapUtilities.concatMaps(mappings, attributes);
					}
					IShapeDescriptorBuilder<?> shapeDescriptorBuilder = createShapeDescriptorBuilder(context, node.getShape());
					UIComponentConstructor.IArguments argument = new UIComponentConstructor.ImmutableArguments(node.getName(),
							mappings,
							shapeDescriptorBuilder.build());

					Class<?> clazz = AssertionUtilities.assertNonnull(context.getAliasesView().get(node.getClazz()));
					Constructor<?> constructor = AnnotationUtilities.getElementAnnotatedWith(UIComponentConstructor.class,
							Arrays.asList(clazz.getDeclaredConstructors()));
					MethodHandle constructorHandle = InvokeUtilities.getImplLookup().unreflectConstructor(constructor);

					return (IUIComponent) constructorHandle.invoke(argument);
				}),
				Component::getComponent,
				(p, c) -> {
					if (!Iterables.isEmpty(c)) {
						if (!(p instanceof IUIComponentContainer))
							throw new IllegalArgumentException(
									new LogMessageBuilder()
											.addMarkers(UIMarkers.getInstance()::getMarkerParser)
											.addKeyValue("p", p).addKeyValue("c", c)
											.addMessages(() -> getResourceBundle().getString("construct.component.container.instance_of.fail"))
											.build()
							);
						((IUIComponentContainer) p).addChildren(c);
					}
					return p;
				},
				n -> {
					throw new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(UIMarkers.getInstance()::getMarkerParser)
									.addKeyValue("n", n).addKeyValue("component", component)
									.addMessages(() -> getResourceBundle().getString("construct.view.tree.cyclic"))
									.build()
					);
				})
				.orElseThrow(AssertionError::new);
	}

	@SuppressWarnings({"RedundantThrows", "UnstableApiUsage"})
	@Override
	public ComponentUI parse1(ComponentUI resource)
			throws Throwable {
		Class<?> viewClass = AssertionUtilities.assertNonnull(getAliases().get(resource.getView().getClazz()));
		if (!getTypeToken().getRawType().isAssignableFrom(viewClass))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerParser)
							.addKeyValue("viewClass", viewClass).addKeyValue("this::getGenericClass", this::getTypeToken)
							.addMessages(() -> getResourceBundle().getString("construct.view.instance_of.fail"))
							.build()
			);

		return resource;
	}

	public static @Immutable Map<IUIEventType, Function<@Nonnull Component, @Nonnull Optional<String>>> getEventTypeFunctionMap() { return EVENT_TYPE_FUNCTION_MAP; }

	@Override
	protected T construct0(ComponentUI parsed)
			throws Throwable {
		// COMMENT raw
		View rawView = parsed.getView();
		Component rawComponent = parsed.getComponent();

		// COMMENT create hierarchy
		@SuppressWarnings("unchecked") T view = (T) createView(new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), null, null), rawView); // COMMENT should be checked
		IUIDefaultComponentParserContext viewContext = new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), view, view);
		view.setManager(
				CastUtilities.castUnchecked(createComponent(viewContext, rawComponent)) // COMMENT may throw
		);

		// COMMENT configure view
		Iterables.concat(
				rawView.getExtension(),
				rawView.getAnyContainer()
						.map(AnyContainer::getAny)
						.orElseGet(ImmutableList::of))
				.forEach(any -> {
							assert any != null;
							IUIAbstractDefaultComponentParserContext.findHandler(viewContext, any)
									.ifPresent(handler -> handler.accept(
											viewContext,
											CastUtilities.castUnchecked(any) // COMMENT should not throw
									));
						}
				);

		// COMMENT configure components
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, rawComponent,
				node -> {
					assert node != null;
					IUIComponent component = IUIView.getNamedTrackers(view).get(IUIComponent.class, node.getName())
							.orElseThrow(AssertionError::new);
					IUIDefaultComponentParserContext componentContext = new UIImmutableDefaultComponentParserContext(getAliases(), getHandlers(), view, component);
					Iterables.concat(
							node.getAnchor(),
							ImmutableSet.of(node.getRendererContainer()),
							node.getExtension(),
							node.getAnyContainer()
									.map(AnyContainer::getAny)
									.orElseGet(ImmutableList::of))
							.forEach(any -> {
								assert any != null;
								IUIAbstractDefaultComponentParserContext.findHandler(componentContext, any)
										.ifPresent(handler -> handler.accept(
												componentContext,
												CastUtilities.castUnchecked(any) // COMMENT should not throw
										));
							});
					return node;
				},
				Component::getComponent,
				null,
				node -> {
					throw new IllegalArgumentException(
							new LogMessageBuilder()
									.addMarkers(UIMarkers.getInstance()::getMarkerParser)
									.addKeyValue("node", node).addKeyValue("rawComponent", rawComponent)
									.addMessages(() -> getResourceBundle().getString("construct.view.tree.cyclic"))
									.build()
					);
				});
		return view;
	}
}
