package $group__.$modId__.utilities.constructs.interfaces.basic;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Set;

import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Reflections.Unsafe.forName;
import static $group__.$modId__.utilities.helpers.Reflections.getMethodNameDescriptor;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

public interface IAnnotationProcessor<A extends Annotation> {
	/* SECTION methods */

	Class<A> annotationType();

	void process(ASMDataTable asm);


	/* SECTION static variables */

	LoadingCache<String, Reflections> REFLECTIONS_CACHE = CacheBuilder.newBuilder().softValues().build(CacheLoader.from(t -> {
		Reflections r = new Reflections(t);
		r.expandSuperTypes();
		return r;
	}));


	/* SECTION static methods */

	static String getMessage(IAnnotationProcessor<?> processor, @Nullable String msg) { return "Process annotation '" + processor.annotationType() + "'" + (msg == null || msg.isEmpty() ? "" : ": " + msg); }


	static <T, A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsidered(IAnnotationProcessor<A> processor, Class<T> sub, Method m) {
		Class<A> aClass = processor.annotationType();
		A[] r = castUnchecked(Array.newInstance(aClass, 0));

		Class<? super T> sc = sub;
		String mName = m.getName();
		Class<?>[] mArgs = m.getParameterTypes();
		do { try { r = sc.getDeclaredMethod(mName, mArgs).getDeclaredAnnotationsByType(aClass); } catch (NoSuchMethodException ignored) {  /* MARK empty */ }
		} while (r.length == 0 && (sc = sc.getSuperclass()) != null);

		return r;
	}

	static <T, A extends Annotation> A[] getEffectiveAnnotationsIfInheritingConsideredNonEmpty(IAnnotationProcessor<A> processor, Class<T> sub, Method subM) {
		A[] r = getEffectiveAnnotationsIfInheritingConsidered(processor, sub, subM);
		if (r.length == 0) r = castUnchecked(Array.newInstance(r.getClass().getComponentType(), 1));
		return r;
	}


	/* SECTION static classes */

	interface IClass<A extends Annotation> extends IAnnotationProcessor<A> {
		/* SECTION methods */

		void processClass(Result result);

		/** {@inheritDoc} */
		@Override
		default void process(ASMDataTable asm) {
			Set<ASMDataTable.ASMData> thisAsm = asm.getAll(annotationType().getName());

			thisAsm.forEach(t -> processClass(new Result(asm, thisAsm, t, forName(t.getClassName(), false, getClass().getClassLoader()))));
		}


		/* SECTION static classes */

		class Result implements IStruct {
			/* SECTION variables */

			public final ASMDataTable asm;
			public final Set<ASMDataTable.ASMData> thisAsm;
			public final ASMDataTable.ASMData currentAsm;
			public final Class<?> clazz;


			/* SECTION constructors */

			protected Result(ASMDataTable asm, Set<ASMDataTable.ASMData> thisAsm, ASMDataTable.ASMData currentAsm, Class<?> clazz) {
				this.asm = asm;
				this.thisAsm = thisAsm;
				this.currentAsm = currentAsm;
				this.clazz = clazz;
			}

			protected Result(Result c) { this(c.asm, c.thisAsm, c.currentAsm, c.clazz); }
		}


		interface IElement<A extends Annotation, AE extends AnnotatedElement> extends IClass<A> {
			/* SECTION methods */

			AE findElement(IClass.Result result);

			void processElement(Result<A, AE> result);

			/** {@inheritDoc} */
			@Override
			default void processClass(IClass.Result result) {
				AE ae = findElement(result);
				processElement(new Result<>(result, ae, ae.getDeclaredAnnotationsByType(annotationType())));
			}


			/* SECTION static classes */

			class Result<A extends Annotation, AE extends AnnotatedElement> extends IClass.Result {
				/* SECTION variables */

				public final AE element;
				public final A[] annotations;


				/* SECTION constructors */

				protected Result(IClass.Result result, AE element, A[] annotations) {
					super(result);
					this.element = element;
					this.annotations = annotations;
				}

				protected Result(Result<? extends A, ? extends AE> c) { this(c, c.element, c.annotations); }
			}


			interface IMethod<A extends Annotation> extends IElement<A, Method> {
				/* SECTION methods */

				void processMethod(Result<A> result);

				/** {@inheritDoc} */
				@Override
				default Method findElement(IClass.Result result) {
					String mName = result.currentAsm.getObjectName();
					Method r = null;
					for (Method m : result.clazz.getDeclaredMethods()) {
						if (mName.equals(getMethodNameDescriptor(m))) {
							r = m;
							break;
						}
					}
					if (r == null) throw rejectArguments(new NoSuchMethodException(getMessage(this, "No method name '" + mName + "' in class '" + result.clazz.toGenericString() + "'")), result.thisAsm);
					return r;
				}

				/** {@inheritDoc} */
				@Override
				default void processElement(IElement.Result<A, Method> result) {
					processMethod(new Result<>(result));
				}


				/* SECTION static classes */

				class Result<A extends Annotation> extends IElement.Result<A, Method> {
					/* SECTION constructors */

					protected Result(IElement.Result<? extends A, Method> result) { super(result); }

					protected Result(Result<? extends A> c) { this((IElement.Result<? extends A, Method>) c); }
				}
			}
		}
	}
}