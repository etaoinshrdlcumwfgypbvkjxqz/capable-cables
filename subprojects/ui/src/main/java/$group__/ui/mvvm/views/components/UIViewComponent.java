package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.binding.IBinderAction;
import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.core.mvvm.structures.IAffineTransformStack;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.mvvm.views.UIView;
import $group__.utilities.CastUtilities;
import $group__.utilities.TreeUtilities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.reactivex.rxjava3.core.Observer;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UIViewComponent<S extends Shape, M extends IUIComponentManager<S>>
		extends UIView<S>
		implements IUIViewComponent<S, M> {
	protected final M manager;

	public UIViewComponent(M manager) { this.manager = manager; }

	@Override
	public IUIEventTarget getTargetAtPoint(Point2D point) { return getManager().getPathResolver().resolvePath(point, true).getPathEnd(); }

	@Override
	public M getManager() { return manager; }

	@Override
	public IAffineTransformStack getCleanTransformStack() { return getManager().getCleanTransformStack(); }

	@Override
	public Optional<IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next) { return getManager().changeFocus(currentFocus, next); }

	@Override
	public Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() {
		return s -> {
			super.getBinderSubscriber().accept(s);
			getManager().getChildrenFlatView().forEach(c -> c.getBinderSubscriber().accept(s));
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<IBindingField<?>> getBindingFields() {
		return Iterables.concat(Lists.asList(
				super.getBindingFields(),
				(Iterable<IBindingField<?>>[]) // COMMENT should be safe
						getManager().getChildrenFlatView().stream().unordered()
								.map(IHasBinding::getBindingFields)
								.toArray(Iterable[]::new)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<IBindingMethod<?>> getBindingMethods() {
		return Iterables.concat(Lists.asList(
				super.getBindingMethods(),
				(Iterable<IBindingMethod<?>>[]) // COMMENT should be safe
						getManager().getChildrenFlatView().stream().unordered()
								.map(IHasBinding::getBindingMethods)
								.toArray(Iterable[]::new)));
	}

	@Override
	public boolean reshape(Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action) throws ConcurrentModificationException { return getManager().reshape(action); }

	@Override
	public void initialize() {
		IAffineTransformStack stack = getCleanTransformStack();
		TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
				Function.identity(),
				c -> {
					c.initialize(stack);
					stack.push();
					return CastUtilities.castChecked(IUIComponentContainer.class, c)
							.<Iterable<IUIComponent>>map(cp -> {
								cp.transformChildren(stack);
								return cp.getChildrenView();
							})
							.orElseGet(ImmutableSet::of);
				},
				(p, c) -> {
					stack.getDelegated().pop();
					return p;
				},
				r -> { throw new InternalError(); });
	}

	@Override
	public void removed() {
		IAffineTransformStack stack = getCleanTransformStack();
		TreeUtilities.<IUIComponent, IUIComponent>visitNodesDepthFirst(getManager(),
				Function.identity(),
				c -> {
					c.removed(stack);
					stack.push();
					return CastUtilities.castChecked(IUIComponentContainer.class, c)
							.<Iterable<IUIComponent>>map(cp -> {
								cp.transformChildren(stack);
								return cp.getChildrenView();
							})
							.orElseGet(ImmutableSet::of);
				},
				(p, c) -> {
					stack.getDelegated().pop();
					return p;
				},
				r -> { throw new InternalError(); });
	}
}
