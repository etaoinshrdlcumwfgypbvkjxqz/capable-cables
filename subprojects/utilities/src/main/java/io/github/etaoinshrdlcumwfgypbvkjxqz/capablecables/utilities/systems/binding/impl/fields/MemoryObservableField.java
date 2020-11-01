package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

public class MemoryObservableField<T>
		extends AbstractObservableField<T> {
	private T value;

	public MemoryObservableField(Class<T> type, T initialValue) {
		super(type);
		this.value = initialValue;
	}

	@Override
	public void setValue(T value) {
		if (!getValue().equals(value)) {
			this.value = value;
			getNotifierSubject().onNext(value);
		}
	}

	@Override
	public T getValue() { return value; }
}
