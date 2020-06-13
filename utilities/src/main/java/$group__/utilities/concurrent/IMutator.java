package $group__.utilities.concurrent;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxed;
import static java.util.Objects.requireNonNull;

public interface IMutator {
	@Nullable
	static <T> T trySet(IMutator mutator, @Nullable T target, boolean initialize) {
		AtomicReference<T> t = new AtomicReference<>(target == null ? null : castUncheckedUnboxed(getDefaultValue(target.getClass())));
		mutator.trySet(t::set, target, initialize);
		return t.get();
	}

	static <T> T trySetNonnull(IMutator mutator, T target, boolean initialize) {
		return requireNonNull(trySet(mutator,
				target, initialize));
	}

	<T> boolean trySet(Consumer<T> setter, @Nullable T target, boolean initialize);

	<T> T mutate(Supplier<T> action, boolean initialize) throws UnsupportedOperationException;

	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default <T> boolean trySet(Consumer<T> setter, @Nullable T target) { return trySet(setter, target, false); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default <T> T mutate(Supplier<T> action) throws UnsupportedOperationException { return mutate(action, false); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default void mutate(Runnable action, boolean initialize) throws UnsupportedOperationException {
		mutate(() -> {
			action.run();
			return null;
		}, initialize);
	}

	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default void mutate(Runnable action) throws UnsupportedOperationException { mutate(action, false); }
}
