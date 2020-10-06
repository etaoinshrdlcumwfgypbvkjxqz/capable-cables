package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import jakarta.xml.bind.JAXBElement;

import java.util.Map;
import java.util.Optional;

public interface IParserContext {
	IUIComponentParser.EnumHandlerType getHandlingType();

	@Immutable
	Map<String, Class<?>> getAliasesView();

	@Immutable
	Map<IUIComponentParser.EnumHandlerType, ? extends Map<Class<?>, IConsumer3<? super IParserContext, ?, ?, ?>>> getHandlersView();

	enum StaticHolder {
		;

		public static Optional<? extends IConsumer3<? super IParserContext, ?, ?, ?>> findHandler(IParserContext self,
		                                                                                          Object any) {
			boolean element = any instanceof JAXBElement;
			return Optional.ofNullable(self.getHandlersView().get(self.getHandlingType().getVariant(element)))
					.map(map -> map.get(element ? ((JAXBElement<?>) any).getDeclaredType() : any.getClass()));
		}
	}
}
