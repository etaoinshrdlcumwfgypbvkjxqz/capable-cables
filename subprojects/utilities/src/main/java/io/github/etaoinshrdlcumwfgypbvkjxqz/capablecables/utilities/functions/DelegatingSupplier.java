package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;

import java.util.function.Supplier;

public class DelegatingSupplier<T>
		extends AbstractDelegatingObject<Supplier<T>>
		implements ICompatibilitySupplier<T> {
	public DelegatingSupplier(Supplier<T> delegated) { super(delegated); }

	@Override
	public T get() { return getDelegate().get(); }
}