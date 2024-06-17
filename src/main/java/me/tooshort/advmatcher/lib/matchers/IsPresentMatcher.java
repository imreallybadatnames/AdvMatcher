package me.tooshort.advmatcher.lib.matchers;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.lib.Matcher;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IsPresentMatcher extends Matcher {
    public static MapCodec<IsPresentMatcher> CODEC = MapCodec.unit(IsPresentMatcher::new);
    @Override
    public MapCodec<? extends Matcher> getCodec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nullable NbtElement element) {
        return !Objects.isNull(element);
    }

    @Override
    public String toString() {
        return "IsPresentMatcher";
    }
}
