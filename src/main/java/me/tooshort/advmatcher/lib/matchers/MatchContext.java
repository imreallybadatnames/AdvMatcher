package me.tooshort.advmatcher.lib.matchers;

import net.minecraft.registry.DynamicRegistryManager;
import org.jetbrains.annotations.Nullable;

/*
 * (Mostly) immutable generic context object.
 * Contains fields important for matchers.
 * May be expanded to type-specific contexts in the future.
 */
public class MatchContext {
    // Important for dynamic registries.
    // Matchers are free to use predefined registries unless otherwise specified.
    private final @Nullable DynamicRegistryManager registryManager;

    public MatchContext(@Nullable DynamicRegistryManager manager) {
        this.registryManager = manager;
    }

    public static MatchContext of(@Nullable DynamicRegistryManager manager) {
        return new MatchContext(manager);
    }

    public @Nullable DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
}
