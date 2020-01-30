package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.variables.Constants;

import javax.annotation.Nullable;
import java.util.Map;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxed;
import static java.util.Objects.requireNonNull;

public enum Miscellaneous {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T> T markUnused(Class<T> t) {
		@Nullable T r = null;
		for (Map.Entry<Class<?>, Object> entry : Constants.PRIMITIVE_DATA_TYPES.entrySet()) {
			if (entry.getKey().isAssignableFrom(t)) {
				r = castUncheckedUnboxed(entry.getValue());
				break;
			}
		}
		return requireNonNull(r);
	}

	@SuppressWarnings("SameReturnValue")
	@Nullable
	public static <T> T markUnused() { return null; }


	public static Object newLockObject() { return new Object(); }


	/**
	 * Function K of SKI combinator calculus.
	 *
	 * @param x constant
	 * @param y any
	 * @param <T> type of parameter {@code x}
	 * @return parameter {@code x}
	 */
	public static <T> T K(T x, @SuppressWarnings("unused") Object... y) { return x; }
}
