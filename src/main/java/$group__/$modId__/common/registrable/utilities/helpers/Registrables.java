package $group__.$modId__.common.registrable.utilities.helpers;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

@SuppressWarnings("SpellCheckingInspection")
public enum Registrables {
	/* MARK empty */;


	/* SECTION static classes */

	public enum Blocks {
		/* MARK empty */;


		/* SECTION static methods */

		public static Vec3d getPosition(RayTraceResult rtr) { return new Vec3d(rtr.getBlockPos()).add(rtr.hitVec); }


		public static boolean checkNoEntityCollision(IBlockState state, World world, BlockPos pos) {
			@Nullable AxisAlignedBB collision = state.getCollisionBoundingBox(world, pos);
			return collision == null || world.checkNoEntityCollision(collision.offset(pos));
		}
	}

	public enum NBTs {
		/* MARK empty */;


		/* SECTION static variables */

		public static final ImmutableMap<String, Integer> RAW_NBT_TYPE_LOOKUP;
		public static final ImmutableMap<String, Integer> NBT_TYPE_LOOKUP;


		/* SECTION static methods */

		static {
			Map<String, Integer> rawNbtTypeLookup = new HashMap<>(NBTBase.NBT_TYPES.length);
			Map<String, Integer> nbtTypeLookup = new HashMap<>(NBTBase.NBT_TYPES.length);

			for (String nbt : NBTBase.NBT_TYPES) {
				rawNbtTypeLookup.put(nbt, rawNbtTypeLookup.size());
				nbtTypeLookup.put("NBTTag" + WordUtils.capitalizeFully(nbt).replace("[]", "Array"), nbtTypeLookup.size());
			}

			RAW_NBT_TYPE_LOOKUP = ImmutableMap.copyOf(rawNbtTypeLookup);
			NBT_TYPE_LOOKUP = ImmutableMap.copyOf(nbtTypeLookup);
		}

		@SuppressWarnings("UnusedReturnValue")
		public static boolean setTagIfNotEmpty(NBTTagCompound p, String k, NBTTagCompound v) {
			if (v.getSize() == 0) return false;
			p.setTag(k, v);
			return true;
		}

		@SuppressWarnings("UnusedReturnValue")
		public static <C> boolean setChildIfNotNull(NBTTagCompound p, String k, @Nullable C v, TriConsumer<NBTTagCompound, String, C> c) {
			if (v == null) return false;
			c.accept(p, k, v);
			return true;
		}

		public static <C> Optional<C> readChildIfHasKey(@Nullable NBTTagCompound p, String key, Class<C> type, BiFunction<NBTTagCompound, String, C> f) {
			if (p != null && p.hasKey(key, lookupNBTTypeNonnull(type))) return Optional.of(f.apply(p, key));
			return Optional.empty();
		}

		public static int lookupNBTTypeNonnull(Class<?> clazz) { return lookupNBTType(clazz).orElseThrow(() -> rejectArguments(clazz)); }

		public static Optional<Integer> lookupNBTType(Class<?> clazz) {
			@Nullable Integer ret = NBT_TYPE_LOOKUP.get(clazz.getSimpleName());
			if (ret == null) ret = RAW_NBT_TYPE_LOOKUP.get(clazz.getSimpleName().toUpperCase(Locale.ROOT));
			return Optional.ofNullable(ret);
		}


		/* SECTION static initializer */

		public static Optional<NBTTagCompound> returnTagIfNotEmpty(NBTTagCompound p) { return p.getSize() == 0 ? Optional.empty() : Optional.of(p); }
	}
}
