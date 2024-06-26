package me.tooshort.advmatcher.lib.matchers.nbt.meta;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.nbt.NBTMatcher;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DerefMatcher extends NBTMatcher {
    private final NbtPathArgumentType.NbtPath path;
    private final NBTMatcher subMatcher;

    public static final MapCodec<DerefMatcher> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    NbtPathArgumentType.NbtPath.CODEC.fieldOf("path").forGetter(m -> m.path),
                    NBTMatcher.CODEC.fieldOf("matcher").forGetter(m -> m.subMatcher)
            ).apply(instance, DerefMatcher::new)
    );

    public DerefMatcher(NbtPathArgumentType.NbtPath path, NBTMatcher subMatcher) {
        this.path = path;
        this.subMatcher = subMatcher;
    }

    @Override
    public MapCodec<? extends NBTMatcher> getCodec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nullable NbtElement element, @NotNull MatchContext ctx) {
        List<NbtElement> list = List.of();

        try {
            list = path.get(element);
        } catch (Exception ignored) {}
        // Multiple-element lists will need a more complex handling mechanism.
        if(list.size() > 1) return false;

        return subMatcher.matches(list.getFirst(), ctx);
    }

    @Override
    public String toString() {
        return String.format("DerefMatcher[path=%s, matcher=%s]", path.getString(), subMatcher.toString());
    }
}
