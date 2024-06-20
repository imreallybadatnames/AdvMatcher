package me.tooshort.advmatcher.lib.matchers.nbt.meta;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.nbt.NBTMatcher;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IsPresentMatcher extends NBTMatcher {
    public static MapCodec<IsPresentMatcher> CODEC = MapCodec.unit(IsPresentMatcher::new);
    @Override
    public MapCodec<? extends NBTMatcher> getCodec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nullable NbtElement element, @NotNull MatchContext ctx) {
        return !Objects.isNull(element);
    }

    @Override
    public String toString() {
        return "IsPresentMatcher";
    }
}
