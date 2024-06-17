package me.tooshort.advmatcher.lib.matchers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

public class BoolMatcher extends Matcher {
    private final String key;
    private final Boolean value;

    @Override
    public String toString() {
        return String.format("BoolMatcher[key=%s, value=%b]", key, value);
    }

    public BoolMatcher(String key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    public static final MapCodec<BoolMatcher> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(m -> m.key),
                    Codec.BOOL.fieldOf("value").forGetter(m -> m.value)
            ).apply(instance, BoolMatcher::new)
    );

    @Override
    public MapCodec<? extends Matcher> getCodec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nullable NbtElement element) {
        if (!(element instanceof NbtCompound compound)) return false;
        return compound.getBoolean(key) == value;
    }
}
