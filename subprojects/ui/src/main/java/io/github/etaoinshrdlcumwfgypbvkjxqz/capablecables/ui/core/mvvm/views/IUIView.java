package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructure;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Optional;

/**
 * Operations not thread-safe.
 */
public interface IUIView<S extends Shape>
		extends IUISubInfrastructure, IUIReshapeExplicitly<S>, IHasBinding, IExtensionContainer<INamespacePrefixedString> {
	IUIEventTarget getTargetAtPoint(Point2D point);

	Optional<? extends IUIEventTarget> changeFocus(@Nullable IUIEventTarget currentFocus, boolean next);
}