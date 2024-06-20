package me.tooshort.advmatcher.lib.matchers;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Matcher<T> {
    public abstract MapCodec<? extends Matcher<T>> getCodec();

    // Object may be nullable. Original object must not be mutated by matchers.
    // Errors are interpreted as mismatches.
    // Note that specific errors (such as those indicating potentially malformed matchers) may be log-worthy.
    public abstract boolean matches(@Nullable final T object, @NotNull MatchContext ctx);
}
