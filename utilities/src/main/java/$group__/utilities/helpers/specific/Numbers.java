package $group__.utilities.helpers.specific;

import $group__.traits.IOperable;
import $group__.utilities.helpers.Casts;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;

import static $group__.utilities.helpers.Casts.*;
import static $group__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.utilities.helpers.specific.Patterns.TWO_MINUS_SIGNS_PATTERN;
import static java.lang.Double.doubleToLongBits;
import static java.util.Arrays.asList;

public enum Numbers {
	/* MARK empty */;


	/* SECTION static variables */

	public static final ImmutableMap<Class<? extends Number>, Function<Number, ? extends Number>> PRIMITIVE_TYPE_FROM_NUMBER_MAP = new ImmutableMap.Builder<Class<? extends Number>, Function<Number, ? extends Number>>()
			.put(Integer.class, Number::intValue)
			.put(Float.class, Number::floatValue)
			.put(Double.class, Number::doubleValue)
			.put(Long.class, Number::longValue)
			.put(Byte.class, Number::byteValue)
			.put(Short.class, Number::shortValue).build();
	public static final ImmutableMap<Class<? extends Number>, Function<String, ? extends Number>> NUMBER_FROM_STRING_MAP = new ImmutableMap.Builder<Class<? extends Number>, Function<String, ? extends Number>>()
			.put(Integer.class, Integer::valueOf)
			.put(Float.class, Float::valueOf)
			.put(Double.class, Double::valueOf)
			.put(Long.class, Long::valueOf)
			.put(Byte.class, Byte::valueOf)
			.put(Short.class, Short::valueOf).build();
	public static final ImmutableMap<Class<? extends Number>, BiFunction<? extends Number, ? extends Number, ? extends Number>> SUM_MAP = new ImmutableMap.Builder<Class<? extends Number>, BiFunction<? extends Number, ? extends Number, ? extends Number>>()
			.put(Integer.class, (BiFunction<Integer, Integer, Integer>) Integer::sum)
			.put(Float.class, (BiFunction<Float, Float, Float>) Float::sum)
			.put(Double.class, (BiFunction<Double, Double, Double>) Double::sum)
			.put(Long.class, (BiFunction<Long, Long, Long>) Long::sum)
			.put(Byte.class, (Byte a, Byte b) -> (byte) (a + b))
			.put(Short.class, (Short a, Short b) -> (short) (a + b)).build();


	/* SECTION static methods */

	@SuppressWarnings("MagicNumber")
	public static boolean isNegative(Number value) {
		double vd = value.doubleValue();
		return (doubleToLongBits(vd) & 0b1000000000000000000000000000000000000000000000000000000000000000L) != 0L && !Double.isNaN(vd);
	}


	public static <N> Optional<N> negate(@Nullable N n, @Nullable Logger logger) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class), logger).map(IOperable::negate).flatMap(Casts::castUnchecked);
		else if (n instanceof Number)
			return Optional.ofNullable(NUMBER_FROM_STRING_MAP.get(n.getClass())).map(f -> f.apply(TWO_MINUS_SIGNS_PATTERN.matcher(("-" + n)).replaceAll(Matcher.quoteReplacement("")))).flatMap(Casts::castUnchecked);
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> sum(Logger logger, N... a) { return sum(asList(a), logger); }

	public static <N> Optional<N> sum(Iterable<? extends N> it, @Nullable Logger logger) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return sum(l.get(0), l.subList(1, l.size()), logger);
		}
	}

	public static <N> Optional<N> sum(@Nullable N n, Iterable<? extends N> it, @Nullable Logger logger) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class), logger).flatMap(t -> castChecked(t.sum(it), castUncheckedUnboxedNonnull(t.getClass()), logger));
		else if (n instanceof Number) {
			Class<? extends N> clazz = castUncheckedUnboxedNonnull(n.getClass());
			@Nullable BiFunction<? extends Number, ? extends Number, ? extends Number> m = SUM_MAP.get(clazz);
			@Nullable Function<Number, ? extends Number> ef = PRIMITIVE_TYPE_FROM_NUMBER_MAP.get(clazz);
			if (m == null || ef == null) return Optional.empty();

			Iterator<? extends Number> itr = castUncheckedUnboxedNonnull(it.iterator());
			while (itr.hasNext() && n != null) {
				N nf = n;
				n = unboxOptional(tryCall(() -> m.apply(castUncheckedUnboxedNonnull(nf), castUncheckedUnboxedNonnull(ef.apply(itr.next()))), logger).flatMap(Casts::castUnchecked));
			}
			return Optional.ofNullable(n);
		}
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> max(@Nullable Logger logger, N... a) { return max(asList(a), logger); }

	public static <N> Optional<N> max(Iterable<? extends N> it, @Nullable Logger logger) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return max(l.get(0), l.subList(1, l.size()), logger);
		}
	}

	public static <N> Optional<N> max(@Nullable N n, Iterable<? extends N> it, @Nullable Logger logger) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class), logger).flatMap(t -> castChecked(t.max(it), castUncheckedUnboxedNonnull(t.getClass()), logger));
		else if (n instanceof Comparable<?>) {
			@Nullable Comparable<N> t = castCheckedUnboxed(n, castUncheckedUnboxedNonnull(Comparable.class), logger);
			Iterator<? extends N> itr = it.iterator();
			while (itr.hasNext() && t != null) {
				N t1 = itr.next();
				if (Comparables.lessThan(t, t1))
					t = castCheckedUnboxed(t1, castUncheckedUnboxedNonnull(Comparable.class), logger);
			}
			return castChecked(t, castUncheckedUnboxedNonnull(n.getClass()), logger);
		}
		return Optional.empty();
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N> Optional<N> min(@Nullable Logger logger, N... a) { return min(asList(a), logger); }

	public static <N> Optional<N> min(Iterable<? extends N> it, @Nullable Logger logger) {
		List<? extends N> l = Lists.newArrayList(it);
		switch (l.size()) {
			case 0:
				return Optional.empty();
			case 1:
				return Optional.ofNullable(l.get(0));
			default:
				return min(l.get(0), l.subList(1, l.size()), logger);
		}
	}

	public static <N> Optional<N> min(@Nullable N n, Iterable<? extends N> it, @Nullable Logger logger) {
		if (n instanceof IOperable<?, ?>)
			return Casts.<IOperable<?, N>>castChecked(n, castUncheckedUnboxedNonnull(IOperable.class), logger).flatMap(t -> castChecked(t.min(it), castUncheckedUnboxedNonnull(t.getClass()), logger));
		else if (n instanceof Comparable<?>) {
			@Nullable Comparable<N> t = castCheckedUnboxed(n, castUncheckedUnboxedNonnull(Comparable.class), logger);
			Iterator<? extends N> itr = it.iterator();
			while (itr.hasNext() && t != null) {
				N t1 = itr.next();
				if (Comparables.greaterThan(t, t1))
					t = castCheckedUnboxed(t1, castUncheckedUnboxedNonnull(Comparable.class), logger);
			}
			return castChecked(t, castUncheckedUnboxedNonnull(n.getClass()), logger);
		}
		return Optional.empty();
	}
}
