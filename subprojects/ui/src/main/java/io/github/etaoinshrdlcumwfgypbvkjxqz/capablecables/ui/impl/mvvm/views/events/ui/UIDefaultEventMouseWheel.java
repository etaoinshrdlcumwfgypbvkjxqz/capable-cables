package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.events.ui;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventMouseWheel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IMouseButtonClickData;

import javax.annotation.Nullable;

public class UIDefaultEventMouseWheel extends UIDefaultEventMouse implements IUIEventMouseWheel {
	private final double delta;

	public UIDefaultEventMouseWheel(INamespacePrefixedString type, boolean canBubble, boolean cancelable, IUIViewContext viewContext, IUIEventTarget target, IMouseButtonClickData data, @Nullable IUIEventTarget relatedTarget, double delta) {
		super(type, canBubble, cancelable, viewContext, target, data, relatedTarget);
		this.delta = delta;
	}

	@Override
	public double getDelta() { return delta; }
}