package $group__.ui.mvvm.models;

import $group__.ui.core.mvvm.models.IUIModel;
import $group__.ui.mvvm.extensions.UIExtensionRegistry;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.extensions.core.IExtension;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIModel
		implements IUIModel {
	protected final ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> extensions = MapUtilities.newMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();

	@Override
	@Deprecated
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> addExtension(IExtension<? extends INamespacePrefixedString, ?> extension) {
		UIExtensionRegistry.getInstance().checkExtensionRegistered(extension);
		return StaticHolder.addExtensionImpl(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> removeExtension(INamespacePrefixedString key) { return StaticHolder.removeExtensionImpl(getExtensions(), key); }

	@Override
	public Optional<? extends IExtension<? extends INamespacePrefixedString, ?>> getExtension(INamespacePrefixedString key) { return IExtensionContainer.StaticHolder.getExtensionImpl(getExtensions(), key); }

	@Override
	public Map<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<INamespacePrefixedString, IExtension<? extends INamespacePrefixedString, ?>> getExtensions() { return extensions; }
}
