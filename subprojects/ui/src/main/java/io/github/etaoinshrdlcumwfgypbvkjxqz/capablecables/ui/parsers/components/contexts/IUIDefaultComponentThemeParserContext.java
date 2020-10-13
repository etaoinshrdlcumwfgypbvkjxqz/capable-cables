package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming.UILambdaTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;

public interface IUIDefaultComponentThemeParserContext
		extends IUIAbstractDefaultComponentParserContext<NonnullBiConsumer<? super IUIDefaultComponentThemeParserContext, ?>> {
	UILambdaTheme.Builder getBuilder();
}
