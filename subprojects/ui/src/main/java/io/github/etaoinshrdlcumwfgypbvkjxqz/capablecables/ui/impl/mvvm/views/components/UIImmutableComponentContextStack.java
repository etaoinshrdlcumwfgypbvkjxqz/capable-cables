package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

public final class UIImmutableComponentContextStack
		implements IUIComponentContextStack {
	private final IPath<IUIComponent> path;
	private final IAffineTransformStack transformStack;

	public UIImmutableComponentContextStack(IPath<IUIComponent> path, IAffineTransformStack transformStack) {
		assert transformStack.size() - path.size() == 1;
		this.path = path.copy();
		this.transformStack = transformStack.copy();
	}

	@Override
	public IPath<IUIComponent> getPathRef() { return getPath(); }

	protected IPath<IUIComponent> getPath() { return path; }

	@Override
	public IAffineTransformStack getTransformStackRef() { return getTransformStack(); }

	protected IAffineTransformStack getTransformStack() { return transformStack; }

	@Override
	public IUIComponentContextStack copy() { return new UIImmutableComponentContextStack(getPath(), getTransformStack()); }

	@Override
	public void close() {
		int size = getPath().size();
		getPath().parentPath(size);
		IAffineTransformStack.popNTimes(getTransformStack(), size);
	}
}