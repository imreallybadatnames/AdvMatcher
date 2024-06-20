package me.tooshort.advmatcher.lib;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.AdvMatcher;
import me.tooshort.advmatcher.lib.matchers.nbt.*;
import me.tooshort.advmatcher.lib.matchers.nbt.keyed.BoolMatcher;
import me.tooshort.advmatcher.lib.matchers.nbt.meta.DerefMatcher;
import me.tooshort.advmatcher.lib.matchers.nbt.meta.MultiNBTMatcher;
import me.tooshort.advmatcher.lib.matchers.nbt.value.ByteValueMatcher;
import me.tooshort.advmatcher.lib.matchers.nbt.meta.IsPresentMatcher;
import me.tooshort.advmatcher.lib.matchers.stack.ItemStackMatcher;
import me.tooshort.advmatcher.lib.matchers.stack.meta.MultiStackMatcher;
import me.tooshort.advmatcher.lib.matchers.stack.util.HasEnchantmentMatcher;
import net.minecraft.registry.Registry;

// Initialization happens here.
public class Matchers {
    static {
        NBTMatcher.init();
        ItemStackMatcher.init();
    }

    public static void registerBuiltins() {
        registerNBTBuiltins();
        registerStackBuiltins();
    }

    public static void registerNBTBuiltins() {
        registerNBT("boolean", BoolMatcher.CODEC);
        registerNBT("byte_value", ByteValueMatcher.CODEC);
        registerNBT("multi", MultiNBTMatcher.CODEC);
        registerNBT("deref", DerefMatcher.CODEC);
        registerNBT("value_present", IsPresentMatcher.CODEC);
    }

    public static void registerStackBuiltins() {
        registerStack("multi", MultiStackMatcher.CODEC);
        registerStack("has_enchantment", HasEnchantmentMatcher.CODEC);
    }

    private static void registerNBT(String name, MapCodec<? extends NBTMatcher> codec) {
        Registry.register(MatcherRegistries.NBT_MATCHERS, AdvMatcher.id(name), codec);
    }

    private static void registerStack(String name, MapCodec<? extends ItemStackMatcher> codec) {
        Registry.register(MatcherRegistries.STACK_MATCHERS, AdvMatcher.id(name), codec);
    }
}
