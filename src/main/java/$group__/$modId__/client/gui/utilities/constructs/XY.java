package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString;
import $group__.$modId__.utilities.helpers.Primitives.Numbers;
import com.google.common.collect.Streams;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class XY<N extends Number, T extends XY<N, T>> implements IStructureCloneable<T>, IOperable<T, XY<N, ?>> {
	/* SECTION variables */
	
	protected N x;
	protected N y;
	
	
	/* SECTION constructors */

	public XY(N x, N y) {
		this.x = x;
		this.y = y;
	}
	
	public XY(XY<? extends N, ?> copy) { this(copy.getX(), copy.getY()); }
	
	
	/* SECTION getters & setters */
	
	public N getX() { return x; }
	
	public void setX(N x) { this.x = x; }
	
	public N getY() { return y; }

	public void setY(N y) { this.y = y; }
	
	
	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T negate() {
		T r = clone();
		r.x = Numbers.negate(r.getX());
		r.y = Numbers.negate(r.getY());
		return r;
	}


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sum(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.sum(r.getX(), extractXs(o));
		r.y = Numbers.sum(r.getY(), extractYs(o));
		return r;
	}

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sumX(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.sum(r.getX(), extractXs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumX(XY<N, ?>... o) { return sumX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sumY(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.y = Numbers.sum(r.getY(), extractYs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumY(XY<N, ?>... o) { return sumY(Arrays.asList(o)); }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T max(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.max(r.getX(), extractXs(o));
		r.y = Numbers.max(r.getY(), extractYs(o));
		return r;
	}

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T maxX(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.max(r.getX(), extractXs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxX(XY<N, ?>... o) { return maxX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T maxY(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.y = Numbers.max(r.getY(), extractYs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxY(XY<N, ?>... o) { return maxY(Arrays.asList(o)); }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T min(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.min(r.getX(), extractXs(o));
		r.y = Numbers.min(r.getY(), extractYs(o));
		return r;
	}

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T minX(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.x = Numbers.min(getX(), extractXs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minX(XY<N, ?>... o) { return minX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T minY(Iterable<? extends XY<N, ?>> o) {
		T r = clone();
		r.y = Numbers.min(r.getY(), extractYs(o));
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minY(XY<N, ?>... o) { return minY(Arrays.asList(o)); }


	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return IStrictToString.getToStringString(this, super.toString(),
			new Object[]{"x", getX()},
			new Object[]{"y", getY()}); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getX(), getY()); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getX().equals(t.getX()),
			t -> getY().equals(t.getY())); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
		r.x = tryCloneNonnull(x);
		r.y = tryCloneNonnull(y);
		return r;
	}

	
	/* SECTION static methods */

	@SuppressWarnings("UnstableApiUsage")
	public static <N extends Number> List<N> extractXs(Iterable<? extends XY<N, ?>> o) { return Streams.stream(o).map(XY::getX).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractXs(XY<N, ?>... o) { return extractYs(Arrays.asList(o)); }


	@SuppressWarnings("UnstableApiUsage")
	public static <N extends Number> List<N> extractYs(Iterable<? extends XY<N, ?>> o) { return Streams.stream(o).map(XY::getY).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractYs(XY<N, ?>... o) { return extractYs(Arrays.asList(o)); }

	
	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends XY<N, T> {
		/* SECTION constructors */

		public Immutable(N x, N y) { super(tryToImmutableNonnull(x), tryToImmutableNonnull(y)); }

		public Immutable(XY<N, ?> copy) { this(copy.getX(), copy.getY()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setX(N x) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setY(N y) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}