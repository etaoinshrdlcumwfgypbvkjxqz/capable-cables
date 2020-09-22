package $group__.ui.structures.shapes.descriptors;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.ImmutableList;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import static $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor.StaticHolder.checkIsBeingModified;

public abstract class AbstractShapeDescriptor<S extends Shape>
		implements IShapeDescriptor<S> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException {
		if (isBeingModified())
			throw new ConcurrentModificationException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("this", this).addKeyValue("action", action)
							.addMessages(() -> getResourceBundle().getString("modify.concurrent"))
							.build()
			);
		setBeingModified(true);
		boolean ret = modify0(action);
		setBeingModified(false);
		return ret;
	}

	protected final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected boolean beingModified = false;

	protected AbstractShapeDescriptor() {}

	@Override
	public List<IShapeConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	protected boolean modify0(Supplier<? extends Boolean> action) {
		boolean ret = action.get();
		if (ret) {
			Rectangle2D bounds = getShapeOutput().getBounds2D();
			StaticHolder.constrain(bounds, getConstraints());
			bound(bounds);
		}
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

	@Override
	public boolean isBeingModified() { return beingModified; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean bound(Rectangle2D bounds)
			throws IllegalStateException {
		checkIsBeingModified(this);
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		checkIsBeingModified(this);
		return false;
	}
}
