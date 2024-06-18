package me.tooshort.advmatcher.lib;

import com.mojang.serialization.MapCodec;
import me.tooshort.advmatcher.AdvMatcher;
import me.tooshort.advmatcher.lib.matchers.*;
import net.minecraft.registry.Registry;

// Initialization happens here.
public class Matchers {
    static {
        Matcher.init();
    }

    public static void registerBuiltins() {
        register("boolean", BoolMatcher.CODEC);
        register("byte_value", ByteValueMatcher.CODEC);
        register("multi", MultiMatcher.CODEC);
        register("deref", DerefMatcher.CODEC);
        register("value_present", IsPresentMatcher.CODEC);
    }

    private static void register(String name, MapCodec<? extends Matcher> codec) {
        Registry.register(MatcherRegistries.MATCHERS, AdvMatcher.id(name), codec);
    }
}
