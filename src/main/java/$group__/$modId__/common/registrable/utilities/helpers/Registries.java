package $group__.$modId__.common.registrable.utilities.helpers;

import net.minecraft.util.ResourceLocation;

public enum Registries {
	/* MARK empty */;


	/* SECTION static methods */

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(ResourceLocation name) { return name.getResourceDomain() + '.' + name.getResourcePath(); }

	@SuppressWarnings("SpellCheckingInspection")
	public static String getNamespacedUnlocalizedNameForRegistry(String modId, String name) { return modId + '.' + name; }
}
