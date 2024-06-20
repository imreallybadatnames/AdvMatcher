package me.tooshort.advmatcher.lib.matchers.stack.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.stack.ItemStackMatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MultiStackMatcher extends ItemStackMatcher {
    public enum Mode implements StringIdentifiable {
        OR("or"),
        AND("and");

        public final String name;

        public static final Codec<MultiStackMatcher.Mode> CODEC = StringIdentifiable.createCodec(MultiStackMatcher.Mode::values);

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    private final MultiStackMatcher.Mode mode;
    private final List<ItemStackMatcher> subMatchers;

    public static final MapCodec<MultiStackMatcher> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MultiStackMatcher.Mode.CODEC.fieldOf("mode").forGetter(m -> m.mode),
                    ItemStackMatcher.CODEC.codec().listOf(1, Integer.MAX_VALUE).fieldOf("matchers").forGetter(m -> m.subMatchers)
            ).apply(instance, MultiStackMatcher::new)
    );

    public MapCodec<MultiStackMatcher> getCodec() {
        return CODEC;
    }

    public MultiStackMatcher(MultiStackMatcher.Mode mode, ItemStackMatcher... subMatchers) {
        this(mode, Arrays.asList(subMatchers));
    }

    public MultiStackMatcher(MultiStackMatcher.Mode mode, List<ItemStackMatcher> subMatchers) {
        this.mode = mode;
        this.subMatchers = subMatchers;
    }

    public MultiStackMatcher.Mode mode() {
        return mode;
    }

    public List<ItemStackMatcher> matchers() {
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
        return String.format("MultiStackMatcher[mode=%s, matchers=%s]", mode, matchersAsString());
    }

    @Override
    public boolean matches(ItemStack stack, @NotNull MatchContext ctx) {
        return switch (mode) {
            case OR: for (var matcher : subMatchers) if (matcher.matches(stack, ctx)) yield true; yield false;
            case AND: for (var matcher : subMatchers) if (!matcher.matches(stack, ctx)) yield false; yield true;
        };
    }
}
