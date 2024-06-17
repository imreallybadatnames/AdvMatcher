package me.tooshort.advmatcher.lib.matchers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.List;

public class MultiMatcher extends Matcher {
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
    private final List<Matcher> subMatchers;

    public static final MapCodec<MultiMatcher> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Mode.CODEC.fieldOf("mode").forGetter(m -> m.mode),
                    Matcher.CODEC.codec().listOf(1, Integer.MAX_VALUE).fieldOf("matchers").forGetter(m -> m.subMatchers)
            ).apply(instance, MultiMatcher::new)
    );

    public MapCodec<MultiMatcher> getCodec() {
        return CODEC;
    }

    public MultiMatcher(Mode mode, Matcher... subMatchers) {
        this(mode, Arrays.asList(subMatchers));
    }

    public MultiMatcher(Mode mode, List<Matcher> subMatchers) {
        this.mode = mode;
        this.subMatchers = subMatchers;
    }

    public Mode mode() {
        return mode;
    }

    public List<Matcher> matchers() {
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
        return String.format("MultiMatcher[mode=%s, matchers=%s]", mode, matchersAsString());
    }

    @Override
    public boolean matches(NbtElement element) {
            return switch (mode) {
                case OR: for (var matcher : subMatchers) if (matcher.matches(element)) yield true; yield false;
                case AND: for (var matcher : subMatchers) if (!matcher.matches(element)) yield false; yield true;
            };
    }
}
