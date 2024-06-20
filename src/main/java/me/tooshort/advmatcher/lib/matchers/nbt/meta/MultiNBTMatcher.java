package me.tooshort.advmatcher.lib.matchers.nbt.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.nbt.NBTMatcher;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MultiNBTMatcher extends NBTMatcher {
    public enum Mode implements StringIdentifiable {
        OR("or"),
        AND("and");

        public final String name;

        public static final Codec<Mode> CODEC = StringIdentifiable.createCodec(Mode::values);

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    private final Mode mode;
    private final List<NBTMatcher> subMatchers;

    public static final MapCodec<MultiNBTMatcher> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Mode.CODEC.fieldOf("mode").forGetter(m -> m.mode),
                    NBTMatcher.CODEC.codec().listOf(1, Integer.MAX_VALUE).fieldOf("matchers").forGetter(m -> m.subMatchers)
            ).apply(instance, MultiNBTMatcher::new)
    );

    public MapCodec<MultiNBTMatcher> getCodec() {
        return CODEC;
    }

    public MultiNBTMatcher(Mode mode, NBTMatcher... subMatchers) {
        this(mode, Arrays.asList(subMatchers));
    }

    public MultiNBTMatcher(Mode mode, List<NBTMatcher> subMatchers) {
        this.mode = mode;
        this.subMatchers = subMatchers;
    }

    public Mode mode() {
        return mode;
    }

    public List<NBTMatcher> matchers() {
        return subMatchers;
    }

    private String matchersAsString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < subMatchers.size() - 1; i++) {
            s.append(subMatchers.get(i));
            s.append(", ");
        }
        s.append(subMatchers.getLast());
        s.append("]");
        return s.toString();
    }

    @Override
    public String toString() {
        return String.format("MultiNBTMatcher[mode=%s, matchers=%s]", mode, matchersAsString());
    }

    @Override
    public boolean matches(NbtElement element, @NotNull MatchContext ctx) {
            return switch (mode) {
                case OR: for (var matcher : subMatchers) if (matcher.matches(element, ctx)) yield true; yield false;
                case AND: for (var matcher : subMatchers) if (!matcher.matches(element, ctx)) yield false; yield true;
            };
    }
}
