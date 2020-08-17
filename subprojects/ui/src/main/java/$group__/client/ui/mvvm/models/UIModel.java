package $group__.client.ui.mvvm.models;

import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.models.IUIModel;
import $group__.utilities.MapUtilities;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;

public class UIModel
		implements IUIModel {
	protected final ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIModel>> extensions = MapUtilities.getMapMakerSingleThreaded().initialCapacity(INITIAL_CAPACITY_SMALL).makeMap();

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIModel>> addExtension(IUIExtension<ResourceLocation, ? super IUIModel> extension) {
		IExtension.RegExtension.checkExtensionRegistered(extension);
		return IExtensionContainer.addExtension(this, getExtensions(), extension.getType().getKey(), extension);
	}

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIModel>> removeExtension(ResourceLocation key) { return IExtensionContainer.removeExtension(getExtensions(), key); }

	@Override
	public Optional<IUIExtension<ResourceLocation, ? super IUIModel>> getExtension(ResourceLocation key) { return Optional.ofNullable(getExtensions().get(key)); }

	@Override
	public Map<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIModel>> getExtensionsView() { return ImmutableMap.copyOf(getExtensions()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<ResourceLocation, IUIExtension<ResourceLocation, ? super IUIModel>> getExtensions() { return extensions; }
}
