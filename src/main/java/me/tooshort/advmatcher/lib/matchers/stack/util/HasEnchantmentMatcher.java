package me.tooshort.advmatcher.lib.matchers.stack.util;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.AdvMatcher;
import me.tooshort.advmatcher.lib.matchers.MatchContext;
import me.tooshort.advmatcher.lib.matchers.stack.ItemStackMatcher;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HasEnchantmentMatcher extends ItemStackMatcher {
    private final Identifier checkFor;
    private RegistryEntry<Enchantment> entry;
    private boolean warned = false;

    public HasEnchantmentMatcher(Identifier checkFor) {
        this.checkFor = checkFor;
    }

    public static final MapCodec<HasEnchantmentMatcher> CODEC = Identifier.CODEC.fieldOf("id").xmap(HasEnchantmentMatcher::new, m -> m.checkFor);

    @Override
    public MapCodec<HasEnchantmentMatcher> getCodec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return String.format("HasEnchantmentMatcher[id=%s, entry=%s]", checkFor, entry);
    }

    private void warnOnce(String msg) {
        if (!warned) AdvMatcher.LOGGER.warn("[{}] {}", this, msg);
        warned = true;
    }

    private boolean initializeEntry(@NotNull MatchContext ctx) {
        if (entry != null) return true;
        var manager = ctx.getRegistryManager();
        if (manager == null) {
            warnOnce("Missing registry manager");
            return false;
        }

        var registry = ctx.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        if (registry == null) {
            warnOnce("Missing enchantment registry");
            return false;
        }

        var newEntry = registry.getEntry(checkFor).orElse(null);
        if (newEntry == null) {
            warnOnce(String.format("Missing entry for ID %s", checkFor));
            return false;
        }

        this.entry = newEntry;
        return true;
    }

    @Override
    public boolean matches(@Nullable ItemStack stack, @NotNull MatchContext ctx) {
        if (stack == null) return false;
        if (!initializeEntry(ctx)) return false;
        return EnchantmentHelper.getLevel(entry, stack) > 0;
    }
}
