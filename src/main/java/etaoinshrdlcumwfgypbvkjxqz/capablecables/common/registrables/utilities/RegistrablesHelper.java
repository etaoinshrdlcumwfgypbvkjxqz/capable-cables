package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("SpellCheckingInspection")
@Immutable
public enum RegistrablesHelper {
    ;
    public enum BlockHelper {
        ;
        public static boolean checkNoEntityCollision(IBlockState state, World world, BlockPos pos) {
            AxisAlignedBB collision = state.getCollisionBoundingBox(world, pos);
            return collision == null || world.checkNoEntityCollision(collision.offset(pos));
        }
    }

    public enum ItemHelper {
        ;
        public static boolean areItemStacksCompletelyEqual(ItemStack a, ItemStack b) { return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b); }

        public static int getSlotFor(IInventory inventory, ItemStack stack) {
            int size = inventory.getSizeInventory();
            for (int i = 0; i < size; i++) {
                ItemStack cur = inventory.getStackInSlot(i);
                if (areItemStacksCompletelyEqual(stack, cur)) return i;
            }
            return -1;
        }
    }

    public enum PositionHelper {
        ;
        public static Vec3d getPosition(RayTraceResult rtr) { return new Vec3d(rtr.getBlockPos()).add(rtr.hitVec); }
    }

    public enum EnumHandHelper {
        ;
        @Nullable
        public static EnumHand getHandHoldingItem(ItemStack stack, Entity entity) {
            return entity instanceof EntityPlayer ? ((EntityPlayer)entity).inventory.hasItemStack(stack) ? stack == ((EntityPlayer)entity).inventory.getCurrentItem() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND : null : null;
        }
    }

    public enum NBTHelper {
        ;
        public static final Map<String, Integer> RAW_NBT_TYPE_LOOKUP;
        public static final Map<String, Integer> NBT_TYPE_LOOKUP;
        static {
            List<String> nbtTypes = Arrays.asList(NBTBase.NBT_TYPES);

            Map<String, Integer> rawNbtTypeLookup = new HashMap<>(NBTBase.NBT_TYPES.length);
            nbtTypes.forEach(t -> rawNbtTypeLookup.put(t, rawNbtTypeLookup.size()));
            RAW_NBT_TYPE_LOOKUP = ImmutableMap.copyOf(rawNbtTypeLookup);

            Map<String, Integer> nbtTypeLookup = new HashMap<>(NBTBase.NBT_TYPES.length);
            nbtTypes.forEach(t -> nbtTypeLookup.put("NBTTag" + WordUtils.capitalizeFully(t).replace("[]", "Array"), nbtTypeLookup.size()));
            NBT_TYPE_LOOKUP = ImmutableMap.copyOf(nbtTypeLookup);
        }
        @SuppressWarnings("ConstantConditions")
        public static int lookupNBTType(Class<?> clazz) {
            return lookupNBTType(clazz, false);
        }
        @Nullable
        public static Integer lookupNBTType(Class<?> clazz, boolean nullable) {
            Integer ret = NBT_TYPE_LOOKUP.get(clazz.getSimpleName());
            if (ret == null) ret = RAW_NBT_TYPE_LOOKUP.get(clazz.getSimpleName().toUpperCase(Locale.ROOT));
            return nullable ? ret : requireNonNull(ret);
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

        @Nullable
        public static <C> C readChildIfHasKey(@Nullable NBTTagCompound p, String key, Class<C> type, BiFunction<NBTTagCompound, String, C> f) {
            if (p != null && p.hasKey(key, lookupNBTType(type))) return f.apply(p, key);
            return null;
        }

        @Nullable
        public static NBTTagCompound returnTagIfNotEmpty(NBTTagCompound p) { return p.getSize() == 0 ? null : p; }
    }
}