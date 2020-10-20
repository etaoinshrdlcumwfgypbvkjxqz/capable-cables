package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.builders;

import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class AbstractShapeDescriptorBuilder<S extends Shape>
		extends IHasGenericClass.Impl<S>
		implements IShapeDescriptorBuilder<S> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private final AffineTransform transform = new AffineTransform();
	private final Rectangle2D bounds = IShapeDescriptor.StaticHolder.getShapePlaceholder();
	private final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private final ConcurrentMap<String, Consumer<?>> properties =
			MapBuilderUtilities.newMapMakerSingleThreaded().makeMap();

	protected AbstractShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public AbstractShapeDescriptorBuilder<S> setProperty(@NonNls CharSequence key, @Nullable Object value) throws IllegalArgumentException {
		@Nullable Consumer<?> c = getProperties().get(key.toString());
		if (c == null)
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("key", key).addKeyValue("value", value)
							.addMessages(() -> getResourceBundle().getString("property.key.missing"))
							.build()
			);

		c.accept(CastUtilities.castUncheckedNullable(value)); // COMMENT ClassCastException may be thrown
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> transformConcatenate(AffineTransform transform) {
		getTransform().concatenate(transform);
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> transformPreConcatenate(AffineTransform transform) {
		getTransform().preConcatenate(transform);
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setWidth(double width) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) -> {
			assert rx != null;
			assert ry != null;
			assert rh != null;
			getBounds().setFrame(rx, ry, width, rh);
		});
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setHeight(double height) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) -> {
			assert rx != null;
			assert ry != null;
			assert rw != null;
			getBounds().setFrame(rx, ry, rw, height);
		});
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setX(double x) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) -> {
			assert ry != null;
			assert rw != null;
			assert rh != null;
			getBounds().setFrame(x, ry, rw, rh);
		});
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> setY(double y) {
		UIObjectUtilities.acceptRectangularShape(getBounds(), (rx, ry, rw, rh) -> {
			assert rx != null;
			assert rw != null;
			assert rh != null;
			getBounds().setFrame(rx, y, rw, rh);
		});
		return this;
	}

	@Override
	public AbstractShapeDescriptorBuilder<S> constrain(Iterable<? extends IShapeConstraint> constraints) {
		Iterables.addAll(getConstraints(), constraints);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	protected Rectangle2D getBounds() { return bounds; }

	protected AffineTransform getTransform() { return transform; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Consumer<?>> getProperties() { return properties; }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}