package me.tooshort.advmatcher.lib.matchers.nbt;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.lib.MatcherRegistries;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.Matcher;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class NBTMatcher extends Matcher<NbtElement> {
    public static final MapCodec<NBTMatcher> CODEC = MatcherRegistries.NBT_MATCHERS.getCodec().dispatchMap(NBTMatcher::getCodec, Function.identity());

    // dummy method to initialize CODEC before matchers use it
    // probably won't do anything else for the near future
    public static void init() {}

    @Override
    public abstract MapCodec<? extends NBTMatcher> getCodec();

    @Override
    public abstract boolean matches(@Nullable final NbtElement element, @NotNull MatchContext ctx);
}
