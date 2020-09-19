package $group__.utilities;

import $group__.utilities.compile.EnumBuildType;
import org.jetbrains.annotations.NonNls;

public enum UtilitiesConstants {
	;

	@NonNls
	public static final String MODULE_NAME = "${moduleName}";
	@NonNls
	public static final String BUILD_TYPE_STRING = "${buildType}";
	public static final EnumBuildType BUILD_TYPE = EnumBuildType.valueOfSafe(BUILD_TYPE_STRING);
}
