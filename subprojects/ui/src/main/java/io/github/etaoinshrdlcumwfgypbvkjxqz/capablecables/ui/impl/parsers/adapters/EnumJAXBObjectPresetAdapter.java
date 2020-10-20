package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

public enum EnumJAXBObjectPresetAdapter
		implements Map.Entry<Class<?>, Registry.RegistryObject<IDuplexFunction<?, ?>>> {
	;

	private final Class<?> key;
	private final Registry.RegistryObject<IDuplexFunction<?, ?>> value;

	<L, V extends IDuplexFunction<L, ?> & Serializable> EnumJAXBObjectPresetAdapter(Class<L> key, V value) {
		this.key = key;
		this.value = CastUtilities.castUnchecked(JAXBAdapterRegistries.Object.getInstance().registerSafe(key, value));
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Nonnull
	@Override
	public Class<?> getKey() { return key; }

	@Nonnull
	@Override
	public Registry.RegistryObject<IDuplexFunction<?, ?>> getValue() { return value; }

	@Nullable
	@Override
	public Registry.RegistryObject<IDuplexFunction<?, ?>> setValue(Registry.RegistryObject<IDuplexFunction<?, ?>> value)
			throws UnsupportedOperationException { throw new UnsupportedOperationException(); }
}