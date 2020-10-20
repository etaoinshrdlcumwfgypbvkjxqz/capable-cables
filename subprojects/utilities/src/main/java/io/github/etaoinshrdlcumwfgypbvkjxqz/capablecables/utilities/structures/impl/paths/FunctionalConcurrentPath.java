package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class FunctionalConcurrentPath<T>
		extends AbstractConcurrentPath<T> {
	private final List<T> data;
	private final Function<? super Iterable<? extends T>, ? extends List<T>> generator;

	public FunctionalConcurrentPath(Iterable<? extends T> data, Function<? super Iterable<? extends T>, ? extends List<T>> generator) {
		this.generator = generator;
		this.data = AssertionUtilities.assertNonnull(generator.apply(data));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }

	protected Function<? super Iterable<? extends T>, ? extends List<T>> getGenerator() { return generator; }

	@Override
	public FunctionalConcurrentPath<T> copy() {
		Lock readLock = getLock().readLock();
		readLock.lock();
		FunctionalConcurrentPath<T> ret = new FunctionalConcurrentPath<>(getData(), getGenerator());
		readLock.unlock();
		return ret;
	}
}