package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import jakarta.xml.bind.JAXBElement;

import java.util.Map;
import java.util.Optional;

public interface IUIAbstractDefaultComponentParserContext<H> {
	@Immutable Map<String, Class<?>> getAliasesView();

	@Immutable Map<Class<?>, H> getHandlersView();

	enum StaticHolder {
		;

		public static <H> Optional<H> findHandler(IUIAbstractDefaultComponentParserContext<? extends H> self,
		                                          Object any) {
			boolean element = any instanceof JAXBElement;
			return Optional.ofNullable(self.getHandlersView()
					.get(element ? ((JAXBElement<?>) any).getDeclaredType() : any.getClass()));
		}
	}
}
