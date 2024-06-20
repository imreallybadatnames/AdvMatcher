package me.tooshort.advmatcher.lib;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.AdvMatcher;
import me.tooshort.advmatcher.lib.matchers.Matcher;
import me.tooshort.advmatcher.lib.matchers.nbt.NBTMatcher;
import me.tooshort.advmatcher.lib.matchers.stack.ItemStackMatcher;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MatcherRegistries {
    public static final RegistryKey<Registry<MapCodec<? extends NBTMatcher>>> NBT_MATCHERS_KEY = createKey("nbt_matchers");
    public static final Registry<MapCodec<? extends NBTMatcher>> NBT_MATCHERS = createRegistry(NBT_MATCHERS_KEY);

    public static final RegistryKey<Registry<MapCodec<? extends ItemStackMatcher>>> STACK_MATCHERS_KEY = createKey("stack_matchers");
    public static final Registry<MapCodec<? extends ItemStackMatcher>> STACK_MATCHERS = createRegistry(STACK_MATCHERS_KEY);
    
    private static <E, T extends Matcher<E>> RegistryKey<Registry<MapCodec<? extends T>>> createKey(String name) {
        return RegistryKey.ofRegistry(AdvMatcher.id(name));
    }

    private static <E, T extends Matcher<E>> Registry<MapCodec<? extends T>> createRegistry(RegistryKey<Registry<MapCodec<? extends T>>> key) {
        return FabricRegistryBuilder.createSimple(key).attribute(RegistryAttribute.MODDED).buildAndRegister();
    }
}
