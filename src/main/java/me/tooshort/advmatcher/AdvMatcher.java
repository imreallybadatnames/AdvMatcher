package me.tooshort.advmatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.JsonOps;
import me.tooshort.advmatcher.lib.Matcher;
import me.tooshort.advmatcher.lib.Matchers;
import me.tooshort.advmatcher.lib.matchers.ByteValueMatcher;
import me.tooshort.advmatcher.lib.matchers.DerefMatcher;
import me.tooshort.advmatcher.lib.matchers.IsPresentMatcher;
import me.tooshort.advmatcher.lib.matchers.MultiMatcher;
import net.fabricmc.api.ModInitializer;

import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Comparator;
import java.util.function.BiFunction;

// TODO: Make this entire project less of a mess, and give it an actual use.
public class AdvMatcher implements ModInitializer {
	public static final String MODID = "advmatcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	@Override
	public void onInitialize() {
		Matchers.init();

		// Temporary testing code, ephemeral even.
		// TODO: move this into a separate test unit and create more test cases
        try {
			LOGGER.info("AdvMatcher test");
			MultiMatcher testMatcher = new MultiMatcher(MultiMatcher.Mode.AND,
                    new DerefMatcher(NbtPathArgumentType.NbtPath.parse("one.two.three"), new ByteValueMatcher((byte) 1)),
					new DerefMatcher(NbtPathArgumentType.NbtPath.parse("four.five.six"), new ByteValueMatcher((byte) 0)),
					new DerefMatcher(NbtPathArgumentType.NbtPath.parse("Inventory[{Slot:0b}]"), new IsPresentMatcher())
            );
			// toString format test
			LOGGER.info("Matcher: {}", testMatcher);

			// JSON serialization test
			JsonElement testJson = Matcher.CODEC.codec().encodeStart(JsonOps.INSTANCE, testMatcher).getOrThrow();

			StringWriter stringWriter = new StringWriter();
			JsonWriter jsonWriter = GSON.newJsonWriter(stringWriter);
			JsonHelper.writeSorted(jsonWriter, testJson, Comparator.naturalOrder());

			LOGGER.info("JSON: \n{}", stringWriter);

			// JSON deserialization test
			Matcher recreatedMatcher = Matcher.CODEC.codec().parse(JsonOps.INSTANCE, testJson).getOrThrow();
			LOGGER.info("Recreated matcher: {}", recreatedMatcher);

			// Match test
			final NbtCompound matchAgainst = new NbtCompound();

			final BiFunction<String, NbtElement, NbtCompound> create = (key, elem) -> {
				final NbtCompound compound = new NbtCompound();
				compound.put(key, elem);
				return compound;
			};

			matchAgainst.put("one", create.apply("two", create.apply("three", NbtByte.of((byte) 1))));
			matchAgainst.put("four", create.apply("five", create.apply("six", NbtByte.of((byte) 0))));

			final NbtCompound advancedPathTest = new NbtCompound();
			advancedPathTest.put("Slot", NbtByte.of((byte) 0));
			final NbtList fakeInv = new NbtList(); fakeInv.add(advancedPathTest);
			matchAgainst.put("Inventory", fakeInv);
			LOGGER.info("Match NBT: {}", matchAgainst);

			LOGGER.info("Original matcher result: {}", testMatcher.matches(matchAgainst));
			LOGGER.info("Recreated matcher result: {}", recreatedMatcher.matches(matchAgainst));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	public static Identifier id(String name) {
		return Identifier.of(MODID, name);
	}
}