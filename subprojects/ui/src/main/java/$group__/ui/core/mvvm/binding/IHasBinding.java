package $group__.ui.core.mvvm.binding;

import $group__.ui.core.mvvm.structures.IUIPropertyMappingValue;
import $group__.ui.mvvm.binding.BindingField;
import $group__.ui.mvvm.binding.ObservableField;
import $group__.ui.utilities.BindingUtilities;
import io.reactivex.rxjava3.core.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IHasBinding {
	Logger LOGGER = LogManager.getLogger();

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super Node, ? extends Optional<? extends T>> deserializer, T defaultValue) {
		return IHasBinding.<T>createBindingField(clazz, mapping, deserializer, () -> defaultValue);
	}

	static <T> IBindingField<T> createBindingField(Class<T> clazz,
	                                               @Nullable IUIPropertyMappingValue mapping,
	                                               Function<? super Node, ? extends Optional<? extends T>> deserializer, Supplier<T> defaultValue) {
		return new BindingField<>(
				Optional.ofNullable(mapping)
						.flatMap(IUIPropertyMappingValue::getBindingKey)
						.orElse(null),
				new ObservableField<>(clazz,
						Optional.ofNullable(mapping)
								.flatMap(IUIPropertyMappingValue::getDefaultValue)
								.<T>flatMap(n -> deserializer.apply(n).map(Function.identity()))
								.orElseGet(defaultValue)));
	}

	// TODO need some changes
	default Iterable<IBindingField<?>> getBindingFields() { return BindingUtilities.getBindingFields(this); }

	default Iterable<IBindingMethod<?>> getBindingMethods() { return BindingUtilities.getBindingMethods(this); }

	default Consumer<Supplier<? extends Observer<? super IBinderAction>>> getBinderSubscriber() {
		return s -> BindingUtilities.getHasBindingsVariables(this).forEach(v -> v.getBinderSubscriber().accept(s));
	}
}
