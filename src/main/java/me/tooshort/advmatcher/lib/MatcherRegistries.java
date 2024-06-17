package me.tooshort.advmatcher.lib;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.AdvMatcher;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MatcherRegistries {
    public static final RegistryKey<Registry<MapCodec<? extends Matcher>>> MATCHERS_KEY = RegistryKey.ofRegistry(AdvMatcher.id("matchers"));
    public static final Registry<MapCodec<? extends Matcher>> MATCHERS = FabricRegistryBuilder.createSimple(MATCHERS_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister();
}
