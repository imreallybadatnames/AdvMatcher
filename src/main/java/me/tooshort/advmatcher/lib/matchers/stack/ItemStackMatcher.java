package me.tooshort.advmatcher.lib.matchers.stack;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.lib.MatcherRegistries;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.Matcher;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class ItemStackMatcher extends Matcher<ItemStack> {
    public static final MapCodec<ItemStackMatcher> CODEC = MatcherRegistries.STACK_MATCHERS.getCodec().dispatchMap(ItemStackMatcher::getCodec, Function.identity());

    // dummy method to initialize CODEC before matchers use it
    // probably won't do anything else for the near future
    public static void init() {}

    @Override
    public abstract MapCodec<? extends ItemStackMatcher> getCodec();

    @Override
    public abstract boolean matches(@Nullable final ItemStack stack, @NotNull MatchContext ctx);
}
