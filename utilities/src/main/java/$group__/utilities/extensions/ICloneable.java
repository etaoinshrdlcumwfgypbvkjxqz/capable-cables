package $group__.utilities.extensions;

import $group__.annotations.OverridingStatus;
import $group__.annotations.runtime.ExternalCloneMethod;
import $group__.annotations.runtime.processors.ExternalCloneMethodProcessor;
import $group__.annotations.runtime.processors.IProcessorRuntime;
import $group__.utilities.Constants;
import $group__.utilities.helpers.Assertions;
import $group__.utilities.helpers.Casts;
import $group__.utilities.helpers.specific.Loggers;
import $group__.utilities.helpers.specific.MapsExtension;
import $group__.utilities.helpers.specific.Optionals;
import $group__.utilities.helpers.specific.Throwables;
import com.google.common.annotations.Beta;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_4;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.Concurrency.MULTI_THREAD_THREAD_COUNT;
import static $group__.utilities.helpers.Dynamics.IMPL_LOOKUP;
import static $group__.utilities.helpers.Dynamics.Reflections.Bulk.mapFields;
import static $group__.utilities.helpers.Dynamics.getSuperclassesAndInterfaces;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Collections.newSetFromMap;

public interface ICloneable<T> extends Cloneable {
	/* SECTION static variables */

	Logger LOGGER = LogManager.getLogger(ICloneable.class);

	@Nullable
	MethodHandle DEFAULT_METHOD = Throwables.tryCall(() -> IMPL_LOOKUP.findVirtual(Object.class, "clone",
			methodType(Object.class)), LOGGER).orElseGet(() -> {
		Throwables.consumeIfCaughtThrowable(t -> LOGGER.warn(() -> Loggers.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(Loggers.EnumMessages.INVOCATION_UNABLE_TO_FIND_METHOD_HANDLE.makeMessage("clone method", Object.class, "clone", methodType(Object.class)), t)));
		return null;
	});
	@Nullable
	ExternalCloneMethod DEFAULT_ANNOTATION = DEFAULT_METHOD == null ? null : new ExternalCloneMethod() {
		/* SECTION methods */

		@Override
		public Class<?>[] value() { return new Class<?>[0]; }

		@Override
		public boolean allowExtends() { return false; }


		@Override
		public Class<ExternalCloneMethod> annotationType() { return ExternalCloneMethod.class; }
	};

	ConcurrentMap<ExternalCloneMethod, MethodHandle> EXTERNAL_METHOD_MAP = MapsExtension.MAP_MAKER_MULTI_THREAD_WEAK_KEYS.makeMap();
	ConcurrentMap<Class<?>, ExternalCloneMethod> EXTERNAL_ANNOTATIONS_MAP = MapsExtension.MAP_MAKER_MULTI_THREAD.makeMap();
	LoadingCache<Class<?>, ExternalCloneMethod> EXTERNAL_ANNOTATIONS_CACHE =
			CacheBuilder.newBuilder().initialCapacity(INITIAL_CAPACITY_4).expireAfterAccess(MapsExtension.CACHE_EXPIRATION_ACCESS_DURATION, MapsExtension.CACHE_EXPIRATION_ACCESS_TIME_UNIT).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).build(new CacheLoader<Class<?>, ExternalCloneMethod>() {
				/* SECTION methods */

				@Override
				public ExternalCloneMethod load(Class<?> key) throws NoSuchMethodException {
					@Nullable ExternalCloneMethod r = EXTERNAL_ANNOTATIONS_MAP.get(key);
					if (r != null) return r;

					List<Map.Entry<Class<?>, ExternalCloneMethod>> l =
							EXTERNAL_ANNOTATIONS_MAP.entrySet().stream().filter(t -> t.getValue().allowExtends() && t.getKey().isAssignableFrom(key)).collect(Collectors.toList());
					sss:
					for (ImmutableSet<Class<?>> ss : getSuperclassesAndInterfaces(key))
						for (Map.Entry<Class<?>, ExternalCloneMethod> e : l)
							if (ss.contains(e.getKey())) {
								r = e.getValue();
								break sss;
							}

					if (r != null) {
						ExternalCloneMethod rf = r;
						LOGGER.debug(() -> FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Clone method '{}' with annotation '{}' auto-registered for class '{}'", EXTERNAL_METHOD_MAP.get(rf), rf,
								key.toGenericString()));
					} else
						throw Throwables.throw_(new NoSuchMethodException("No clone method for class '" + key.toGenericString() + '\''));

					return r;
				}
			});

	Set<Class<?>> BROKEN_CLASS_SET = newSetFromMap(MapsExtension.MAP_MAKER_MULTI_THREAD_WEAK_KEYS.makeMap());


	/* SECTION static methods */

	/**
	 * Attempts to clone the given parameter {@code o}.
	 * <p>
	 * The given object (hence {@code o}) will attempt to clone itself through, in
	 * order:
	 * <ol>
	 *     <li>If {@code o} is {@code null}, return {@code null}.</li>
	 *     <li>If {@code o} is {@code instanceof} this interface, return the invocation
	 *     result of {@link #clone} on {@code o}.</li>
	 *     <li>If {@link ExternalCloneMethod} is not
	 *     {@linkplain IProcessorRuntime#isProcessed() processed}, return
	 *     {@code o}.</li>
	 *     <li>Otherwise, if {@link #BROKEN_CLASS_SET}
	 *     {@link Set#contains contains} the {@link Class} of {@code o}, return
	 *     {@code o}.</li>
	 *     <li>Attempts to get the {@linkplain MethodHandle clone method} from
	 *     {@link #EXTERNAL_METHOD_MAP}, and if
	 *     a {@link Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_CLONE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Else, the {@code MethodHandle} is invoked and if a
	 *     {@code Throwable} is caught, put the {@code Class} of {@code o} into
	 *     {@code BROKEN_CLONE_CLASSES} and {@code return} {@code o}.</li>
	 *     <li>Lastly, {@code return} the result of the invoked {@code MethodHandle}.</li>
	 * </ol>
	 * The result is always wrapped in an {@link Optional} before {@code return}ing.
	 *
	 * @param <T>    type of parameter {@code o}
	 * @param o      object that will attempt to clone itself
	 * @param logger {@link Logger} used for logging
	 *
	 * @return a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}, wrapped in an {@link Optional}
	 *
	 * @see ExternalCloneMethod for the cloning system
	 * @see #tryCloneUnboxed unboxed version
	 * @see #tryCloneUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	static <T> Optional<T> tryClone(@Nullable T o, @Nullable Logger logger) {
		if (o == null) return Optional.empty();
		else if (o instanceof ICloneable<?>) return Casts.<ICloneable<T>>castUnchecked(o).map(ICloneable::copy);

		Class<?> oc = o.getClass();
		if (ExternalCloneMethodProcessor.INSTANCE.isProcessed()) {
			if (BROKEN_CLASS_SET.contains(oc)) return Optional.of(o);

			MethodHandle m;
			try {
				m = Assertions.assertNonnull(EXTERNAL_METHOD_MAP.get(EXTERNAL_ANNOTATIONS_CACHE.get(oc)));
			} catch (ExecutionException e) {
				Throwables.setCaughtThrowableStatic(e, logger);
				if (logger != null)
					logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Unable to clone '{}' object of class '{}' as no clone method is obtained, will not attempt to clone again", o, oc.toGenericString()), e));
				BROKEN_CLASS_SET.add(oc);
				return Optional.of(o);
			}

			Optional<T> r = tryCall(() -> {
				try {
					return m.invoke(o);
				} catch (Throwable t) {
					throw throwThrowable(t);
				}
			}, logger).flatMap(Casts::castUnchecked);
			Throwables.consumeIfCaughtThrowable(t -> {
				if (logger != null)
					logger.warn(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Clone method '{}' failed for object '{}' of class '{}', will NOT attempt to clone again", m, o, oc.toGenericString()), t));
				BROKEN_CLASS_SET.add(oc);
			});
			return r;
		} else {
			if (logger != null)
				logger.debug(() -> SUFFIX_WITH_THROWABLE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Unable to clone object '{}' of class '{}' as clone method annotation is NOT yet processed, will attempt to clone again", o, oc.toGenericString()), newThrowable()));
			return Optional.of(o);
		}
	}

	/**
	 * See {@link #tryClone}.
	 * <p>
	 * Useful when you want to get the unwrapped result object.
	 *
	 * @param <T>    type of parameter {@code o}
	 * @param o      object that will attempt to clone itself
	 * @param logger {@link Logger} used for logging
	 *
	 * @return a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}, nullable
	 *
	 * @see #tryClone overloaded version
	 * @see #tryCloneUnboxedNonnull unboxed nonnull version
	 * @since 0.0.1.0
	 */
	@Nullable
	static <T> T tryCloneUnboxed(@Nullable T o, @Nullable Logger logger) { return Optionals.unboxOptional(tryClone(o, logger)); }

	/**
	 * See {@link #tryClone}.
	 * <p>
	 * Useful when you want to get the unwrapped result object, and know the returned
	 * object is nonnull as the parameter {@code o} is nonnull.
	 *
	 * @param <T>    type of parameter {@code o}
	 * @param o      object that will attempt to clone itself
	 * @param logger {@link Logger} used for logging
	 *
	 * @return a clone of parameter {@code o} if it successful, else parameter
	 * {@code o}
	 *
	 * @see #tryClone overloaded version
	 * @see #tryCloneUnboxed unboxed version
	 * @since 0.0.1.0
	 */
	static <T> T tryCloneUnboxedNonnull(T o, @Nullable Logger logger) { return tryClone(o, logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }


	static <T> T clone(Callable<?> clone, @Nullable Logger logger) {
		T cloned = Throwables.tryCall(clone::call, logger).flatMap(Casts::<T>castUnchecked).orElseThrow(Throwables::unexpected);
		mapFields(castUncheckedUnboxedNonnull(cloned.getClass()), cloned, cloned, o -> tryCloneUnboxed(o, logger),
				true, logger);
		return cloned;
	}


	/* SECTION methods */

	/**
	 * Alias for {@link #clone}.
	 * <p>
	 * Prefer this method over {@code clone}, as invoking {@code clone} directly may
	 * incur {@link NoSuchMethodException}, and {@link ClassNotFoundException} if used
	 * as a method reference.
	 *
	 * @return cloned object of this object
	 *
	 * @implSpec consider this method as {@code final}
	 * @see #clone aliased version
	 * @since 0.0.1.0
	 */
	@OverridingStatus(group = Constants.PACKAGE, when = When.NEVER)
	default T copy() { return clone(); }

	/**
	 * {@inheritDoc}
	 * <p>
	 * Prefer {@link #copy} over this method, as invoking {@code clone} directly may
	 * incur {@link NoSuchMethodException}, and {@link ClassNotFoundException} if used
	 * as a method reference.
	 *
	 * @return cloned object of this object
	 *
	 * @implSpec must override this method even though {@code Object} overrides it as
	 * {@link ExternalCloneMethodProcessor} will check it.
	 * @see #copy safe version
	 * @since 0.0.1.0
	 */
	@Beta
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = Constants.PACKAGE, when = When.ALWAYS)
	T clone();
}
