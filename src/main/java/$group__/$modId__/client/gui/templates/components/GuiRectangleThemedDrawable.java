package $group__.$modId__.client.gui.templates.components;

import $group__.$modId__.client.gui.utilities.constructs.IDrawable;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Colors;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.awt.*;

import static $group__.$modId__.client.gui.utilities.constructs.IThemed.tryCastThemedTo;
import static $group__.$modId__.client.gui.utilities.helpers.Guis.popMatrix;
import static $group__.$modId__.client.gui.utilities.helpers.Guis.pushMatrix;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class GuiRectangleThemedDrawable<N extends Number, TH extends IThemed.ITheme<TH>, T extends GuiRectangleThemedDrawable<N, TH, T>> extends GuiRectangleThemed<N, TH, T> {
	/* SECTION variables */

	@SuppressWarnings("NotNullFieldNotInitialized")
	protected IDrawable<N, ?> drawable;


	/* SECTION constructors */

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, Color color, TH theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) {
		super(rect, color, theme);
		setDrawable(this, drawable);
	}

	public GuiRectangleThemedDrawable(Rectangle<N, ?> rect, TH theme, /* REMARKS mutable */ IDrawable<N, ?> drawable) { this(rect, Color.WHITE, theme, drawable); }

	public GuiRectangleThemedDrawable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme(), copy.getDrawable()); }


	/* SECTION getters & setters */

	public IDrawable<N, ?> getDrawable() { return drawable; }

	public void setDrawable(IDrawable<N, ?> drawable) { setDrawable(this, drawable); }

	/** {@inheritDoc} */
	@Override
	public void setTheme(TH theme) {
		IThemed<TH> t;
		if ((t = tryCastThemedTo(drawable)) != null) t.setTheme(theme);
		super.setTheme(theme);
	}


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public void draw(Minecraft client) {
		pushMatrix();
		super.draw(client);
		getDrawable().draw(client);
		popMatrix();
	}


	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"drawable", getDrawable()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getDrawable(), getTheme()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDrawable().equals(t.getDrawable())); }

	/** {@inheritDoc} */
	@Override
	public T clone() {
		T r = super.clone();
		r.drawable = drawable.clone();
		return r;
	}


	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/* SECTION static methods */

	protected static <N extends Number, T extends ITheme<T>> void setDrawable(GuiRectangleThemedDrawable<N, T, ?> t, IDrawable<N, ?> d) {
		if (d.isImmutable()) throw rejectArguments(d);
		t.drawable = d;
		t.setTheme(t.getTheme());
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, TH extends ITheme<TH>, T extends Immutable<N, TH, T>> extends GuiRectangleThemedDrawable<N, TH, T> {
		/* SECTION constructors */

		public Immutable(Rectangle<N, ?> rect, Color color, TH theme, IDrawable<N, ?> drawable) { super(rect.toImmutable(), tryToImmutableNonnull(color), tryToImmutableNonnull(theme), drawable.toImmutable()); }

		public Immutable(Rectangle<N, ?> rect, TH theme, IDrawable<N, ?> drawable) { this(rect, Colors.COLORLESS, theme, drawable); }

		public Immutable(GuiRectangleThemedDrawable<N, TH, ?> copy) { this(copy.getRect(), copy.getColor(), copy.getTheme(), copy.getDrawable()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setRect(Rectangle<N, ?> rect) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setColor(Color color) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTheme(TH theme) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setDrawable(IDrawable<N, ?> drawable) { throw rejectUnsupportedOperation(); }


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
