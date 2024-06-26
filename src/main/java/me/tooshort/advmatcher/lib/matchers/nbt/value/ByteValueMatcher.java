package me.tooshort.advmatcher.lib.matchers.nbt.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.nbt.NBTMatcher;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ByteValueMatcher extends NBTMatcher {
    private final byte value;

    @Override
    public String toString() {
        return String.format("ByteValueMatcher[value=%d]", value);
    }

    public ByteValueMatcher(byte value) {
        this.value = value;
    }

    public static final MapCodec<ByteValueMatcher> CODEC = Codec.BYTE.fieldOf("value").xmap(ByteValueMatcher::new, m -> m.value);

    @Override
    public MapCodec<? extends NBTMatcher> getCodec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nullable NbtElement element, @NotNull MatchContext ctx) {
        if (!(element instanceof NbtByte nbtByte)) return false;
        return nbtByte.byteValue() == value;
    }
}
