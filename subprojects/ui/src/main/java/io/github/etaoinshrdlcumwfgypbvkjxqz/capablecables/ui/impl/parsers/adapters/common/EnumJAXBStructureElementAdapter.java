package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Tuple2Type;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import org.jetbrains.annotations.NonNls;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBStructureElementAdapter {
	TUPLE_2(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTuple2), CastUtilities.castUnchecked(ITuple2.class)),
			new JAXBFunctionalElementAdapter<Tuple2Type, ITuple2<?, ?>>(
					(context, left) -> {
						Tuple2Type left1 = left.getValue();
						Object rightLeft = left1.getLeft();
						Object rightRight = left1.getRight();
						return ImmutableTuple2.of(IJAXBAdapterRegistry.adaptFromJAXB(context, rightLeft),
								IJAXBAdapterRegistry.adaptFromJAXB(context, rightRight));
					},
					(context, right) -> {
						Object rightLeft = right.getLeft();
						Object rightRight = right.getRight();
						return ObjectFactories.getDefaultUIObjectFactory().createTuple2(
								Tuple2Type.of(IJAXBAdapterRegistry.adaptToJAXB(context, rightLeft),
										IJAXBAdapterRegistry.adaptToJAXB(context, rightRight))
						);
					}
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBStructureElementAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBStructureElementAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBStructureElementAdapter::getKey)
					.put("value", EnumJAXBStructureElementAdapter::getValue)
					.build();
	private final ITuple2<? extends QName, ? extends Class<?>> key;
	private final IJAXBElementAdapter<?, ?> value;

	<L, R, V extends IJAXBElementAdapter<L, R>> EnumJAXBStructureElementAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	public static void registerAll(IJAXBAdapterRegistry registry) {
		Arrays.stream(values()).unordered()
				.forEach(adapter -> adapter.register(registry));
	}

	@SuppressWarnings("deprecation")
	public void register(IJAXBAdapterRegistry registry) {
		registry.getElementRegistry().register(getKey(), getValue()); // COMMENT use deprecated, checked offers no benefits
	}

	public ITuple2<? extends QName, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBElementAdapter<?, ?> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBStructureElementAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}