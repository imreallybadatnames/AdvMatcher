package me.tooshort.advmatcher.lib;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class Matcher {
    public static final MapCodec<Matcher> CODEC = MatcherRegistries.MATCHERS.getCodec().dispatchMap(Matcher::getCodec, Function.identity());

    // dummy method to initialize CODEC before matchers use it
    // probably won't do anything else for the near future
    public static void init() {}

    public abstract MapCodec<? extends Matcher> getCodec();

    // Element may be nullable (especially if not working on root element)
    // Must return false on error.
    public abstract boolean matches(@Nullable final NbtElement element);
}
