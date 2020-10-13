package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.theming.IUITheme;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class UIArrayThemeStack
		extends UIAbstractThemeStack {
	private final Deque<IUITheme> data;

	public UIArrayThemeStack(Consumer<? super IUITheme> applier, int initialCapacity) {
		super(applier);
		this.data = new ArrayDeque<>(initialCapacity);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Deque<IUITheme> getData() { return data; }
}