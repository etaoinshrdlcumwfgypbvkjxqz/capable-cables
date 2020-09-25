package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IBindingField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.fields.IObservableField;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;

public class BindingField<T> implements IBindingField<T> {
	@Nullable
	protected final INamespacePrefixedString bindingKey;
	protected final IObservableField<T> field;

	public BindingField(@Nullable INamespacePrefixedString bindingKey, IObservableField<T> field) {
		this.bindingKey = bindingKey;
		this.field = field;
	}

	@Override
	public IObservableField<T> getField() { return field; }

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }
}