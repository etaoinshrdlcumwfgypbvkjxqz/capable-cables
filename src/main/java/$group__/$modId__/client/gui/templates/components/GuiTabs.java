package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IDrawableThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString;
import $group__.$modId__.utilities.helpers.Casts;
import $group__.$modId__.utilities.helpers.Throwables;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectIndexOutOfBounds;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static com.google.common.collect.ImmutableSet.of;

@SideOnly(Side.CLIENT)
public class GuiTabs<N extends Number, E extends GuiTabs.ITab<N, ?>, T extends GuiTabs<N, E, T>> extends GuiGroup<N, E, T> {
	/* SECTION variables */

	protected int open;


	/* SECTION constructors */

	public GuiTabs(List<? extends E> tabs, int open) {
		super(tabs);
		setOpen(this, open);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public GuiTabs(int open, E... tabs) { this(Arrays.asList(tabs), open); }

	public GuiTabs(GuiTabs<N, ? extends E, ?> copy) { this(copy.getTabs(), copy.getOpen()); }


	/* SECTION getters & setters */

	public List<E> getTabs() { return (List<E>) getElements(); }

	public void setTabs(List<? extends E> tabs, int open) {
		setElements(tabs);
		setOpen(this, open);
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final void setTabs(int open, E... tabs) { setTabs(Arrays.asList(tabs), open); }

	public int getOpen() { return open; }

	public void setOpen(int open) { setOpen(this, open); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() {
		return getToStringString(this, super.toString(),
				new Object[]{"open", getOpen()});
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getOpen()); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) {
		return isEquals(this, o, super.equals(o),
				t -> getOpen() == t.getOpen());
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r = super.clone();
		r.open = tryCloneUnboxedNonnull(open);
		return r;
	}


	/* SECTION static methods */

	protected static void setOpen(GuiTabs<?, ?, ?> t, int open) {
		int bound = t.getElements().size();
		if (bound <= open) throw rejectIndexOutOfBounds(bound, open);
		t.open = open;
		open = t.getOpen();

		int i = 0;
		for (ITab<?, ?> tab : t.getElements()) { tab.setOpen(i++ == open); }
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, E extends ITab<N, ?>, T extends Immutable<N, E, T>> extends GuiTabs<N, E, T> {
		/* SECTION constructors */

		public Immutable(List<? extends E> tabs, int open) { super(tryToImmutableUnboxedNonnull(tabs), tryToImmutableUnboxedNonnull(open)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(int open, E... tabs) { this(Arrays.asList(tabs), open); }

		public Immutable(GuiTabs<N, ? extends E, ?> copy) { this(copy.getTabs(), copy.getOpen()); }


		/* SECTION getters & setters */

		@Override
		public void setElements(Collection<? extends E> elements) { throw rejectUnsupportedOperation(); }

		@Override
		public void setTabs(List<? extends E> tabs, int open) { throw rejectUnsupportedOperation(); }

		@Override
		public void setOpen(int open) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}


	public interface ITab<N extends Number, T extends ITab<N, T>> extends IDrawable<N, T> {
		/* SECTION methods */

		boolean isOpen();

		void setOpen(boolean open);


		@Override
		String toString();

		@Override
		int hashCode();

		@Override
		boolean equals(Object o);

		@Override
		T clone();


		/* SECTION static classes */

		class Impl<N extends Number, T extends Impl<N, T>> implements ITab<N, T> {
			/* SECTION variables */

			protected IDrawable<N, ?> access;
			protected IDrawable<N, ?> content;
			protected boolean open;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) {
				this.access = access;
				this.content = content;
				this.open = open;
			}

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

			public Impl(Impl<N, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.isOpen()); }


			/* SECTION getters & setters */

			public IDrawable<N, ?> getAccess() { return access; }

			public void setAccess(IDrawable<N, T> access) { this.access = access; }

			public IDrawable<N, ?> getContent() { return content; }

			public void setContent(IDrawable<N, T> content) { this.content = content; }


			@Override
			public boolean isOpen() { return open; }

			@Override
			public void setOpen(boolean open) { this.open = open; }


			/* SECTION methods */

			@SuppressWarnings("EmptyMethod")
			protected void merge() { /* MARK empty */ }


			@Override
			public Optional<Rectangle<N, ?>> spec() {
				Optional<? extends Rectangle<N, ?>> aSpec = getAccess().spec(),
						cSpec = getContent().spec();
				if (isOpen() && cSpec.isPresent())
					return aSpec.map(t -> t.min().min(of(cSpec.get().min()))).<Optional<Rectangle<N, ?>>>map(min -> Optional.of(new Rectangle<>(min, aSpec.get().max().max(of(cSpec.get().max())).sum(of(min.negate()))))).orElseGet(() -> castUncheckedUnboxedNonnull(cSpec));
				else
					return castUncheckedUnboxedNonnull(aSpec);
			}


			@Override
			public void draw(Minecraft client) {
				getAccess().draw(client);
				if (isOpen()) {
					getContent().draw(client);
					merge();
				}
			}


			@Override
			public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }


			@Override
			@OverridingStatus(group = GROUP, when = When.MAYBE)
			public String toString() {
				return getToStringString(this, super.toString(),
						new Object[]{"access", getAccess()},
						new Object[]{"content", getContent()},
						new Object[]{"open", isOpen()});
			}

			@Override
			@OverridingStatus(group = GROUP, when = When.MAYBE)
			public int hashCode() { return getHashCode(this, super.hashCode(), getContent(), getAccess(), isOpen()); }

			@Override
			@OverridingStatus(group = GROUP, when = When.MAYBE)
			public boolean equals(Object o) {
				return isEquals(this, o, super.equals(o),
						t -> getAccess().equals(t.getAccess()),
						t -> getContent().equals(t.getContent()),
						t -> isOpen() == t.isOpen());
			}

			@Override
			@OverridingStatus(group = GROUP, when = When.MAYBE)
			public T clone() {
				T r;
				try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
					throw Throwables.unexpected(e);
				}
				r.access = access.clone();
				r.content = content.clone();
				return r;
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Impl<N, T> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, boolean open) { super(access.toImmutable(), content.toImmutable(), tryToImmutableUnboxedNonnull(open)); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content) { this(access, content, false); }

				public Immutable(Impl<N, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.isOpen()); }


				/* SECTION getters & setters */

				@Override
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				@Override
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final boolean isImmutable() { return true; }
			}
		}
	}


	public interface ITabThemed<N extends Number, TH extends IThemed.ITheme<TH>, T extends ITabThemed<N, TH, T>> extends ITab<N, T>, IDrawableThemed<N, TH, T> {
		/* SECTION methods */

		@Override
		String toString();

		@Override
		int hashCode();

		@Override
		boolean equals(Object o);

		@Override
		T clone();


		/* SECTION static classes */

		class Impl<N extends Number, TH extends ITheme<TH>, T extends Impl<N, TH, T>> extends ITab.Impl<N, T> implements ITabThemed<N, TH, T> {
			/* SECTION variables */

			@SuppressWarnings("NotNullFieldNotInitialized")
			protected TH theme;


			/* SECTION constructors */

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) {
				super(access, content, open);
				setTheme(this, theme);
			}

			public Impl(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access, content, theme, false); }

			public Impl(ITabThemed.Impl<N, TH, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.getTheme(), copy.isOpen()); }


			/* SECTION getters & setters */

			@Override
			public TH getTheme() { return theme; }

			@Override
			public void setTheme(TH theme) { setTheme(this, theme); }


			/* SECTION methods */

			@Override
			public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


			@Override
			public String toString() {
				return IStrictToString.getToStringString(this, super.toString(),
						new Object[]{"theme", getTheme()});
			}

			@Override
			public int hashCode() { return getHashCode(this, super.hashCode(), getTheme()); }

			@Override
			public boolean equals(Object o) {
				return isEquals(this, o, super.equals(o),
						t -> getTheme().equals(t.getTheme()));
			}

			@Override
			public T clone() {
				T r = super.clone();
				r.theme = tryCloneUnboxedNonnull(theme);
				return r;
			}


			/* SECTION static methods */

			protected static <T extends ITheme<T>> void setTheme(ITabThemed.Impl<?, T, ?> t, T theme) {
				t.theme = theme;

				T theme1 = t.getTheme();
				Casts.<IThemed<T>>castChecked(t.getAccess(), castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t1 -> t1.setTheme(theme));
				Casts.<IThemed<T>>castChecked(t.getContent(), castUncheckedUnboxedNonnull(IThemed.class)).ifPresent(t1 -> t1.setTheme(theme));
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends ITabThemed.Impl<N, TH, T> {
				/* SECTION constructors */

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme, boolean open) { super(access.toImmutable(), content.toImmutable(), tryToImmutableUnboxedNonnull(theme), tryToImmutableUnboxedNonnull(open)); }

				public Immutable(IDrawable<N, ?> access, IDrawable<N, ?> content, TH theme) { this(access.toImmutable(), content.toImmutable(), theme, false); }

				public Immutable(ITabThemed.Impl<N, TH, ?> copy) { this(copy.getAccess(), copy.getContent(), copy.getTheme()); }


				/* SECTION getters & setters */

				@Override
				public void setOpen(boolean open) { throw rejectUnsupportedOperation(); }

				@Override
				public void setContent(IDrawable<N, T> content) { throw rejectUnsupportedOperation(); }

				@Override
				public void setAccess(IDrawable<N, T> access) { throw rejectUnsupportedOperation(); }

				@Override
				public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }


				/* SECTION methods */

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final boolean isImmutable() { return true; }
			}
		}
	}
}
