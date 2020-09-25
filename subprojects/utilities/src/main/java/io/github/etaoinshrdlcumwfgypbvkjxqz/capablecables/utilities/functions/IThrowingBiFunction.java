package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThrowableUtilities;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IThrowingBiFunction<T, U, R, TH extends Throwable> {
	@SuppressWarnings("RedundantThrows")
	static <T, U, R, TH extends Throwable> BiFunction<T, U, R> execute(IThrowingBiFunction<T, U, R, TH> lambda)
			throws TH {
		return (t, u) -> {
			try {
				return lambda.apply(t, u);
			} catch (Throwable th) {
				throw ThrowableUtilities.propagateUnverified(th);
			}
		};
	}

	R apply(T t, U u) throws TH;
}