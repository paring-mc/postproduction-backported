package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.RegisterEvent;

public class ExtraSoundEvents {
    public static final AllSoundEvents.SoundEntry
            PACKAGER = create("packager").subtitle("Packager packages")
            .playExisting(SoundEvents.SHULKER_OPEN, 0.125f, 0.75f)
            .category(SoundSource.BLOCKS)
            .build(),
            STOCK_LINK = create("stock_link").subtitle("Stock link reacts")
                    .category(SoundSource.BLOCKS)
                    .build(),

    STOCK_TICKER_REQUEST = create("stock_ticker_request").subtitle("Stock ticker requests")
            .category(SoundSource.BLOCKS)
            .build(),

    CONFIRM_2 = create("confirm_2").subtitle("Affirmative ding")
            .category(SoundSource.PLAYERS)
            .build(),

    STOCK_TICKER_TRADE = create("stock_ticker_trade").subtitle("Stock ticker goes 'ka-ching!'")
            .category(SoundSource.BLOCKS)
            .build(),

    CARDBOARD_SWORD = create("cardboard_bonk").subtitle("Resonant bonk")
            .category(SoundSource.PLAYERS)
            .build(),

    PACKAGE_POP = create("package_pop").subtitle("Package breaks")
            .playExisting(SoundEvents.CHISELED_BOOKSHELF_BREAK, .75f, 1f)
            .playExisting(SoundEvents.WOOL_BREAK, .25f, 1.15f)
            .category(SoundSource.BLOCKS)
            .build(),

    FROGPORT_DEPOSIT = create("frogport_deposit").subtitle("Frogport places package")
            .playExisting(SoundEvents.FROG_TONGUE, 1f, 1f)
            .category(SoundSource.BLOCKS)
            .build(),

    FROGPORT_OPEN = create("frogport_open").subtitle("Frogport opens")
            .playExisting(SoundEvents.WARDEN_TENDRIL_CLICKS, 1f, 2f)
            .category(SoundSource.BLOCKS)
            .build(),

    FROGPORT_CLOSE = create("frogport_close").subtitle("Frogport shuts")
            .category(SoundSource.BLOCKS)
            .build(),

    FROGPORT_CATCH = create("frogport_catch").subtitle("Frogport catches package")
            .addVariant("frogport_catch_1")
            .addVariant("frogport_catch_2")
            .addVariant("frogport_catch_3")
            .category(SoundSource.BLOCKS)
            .build();

    public static void prepare() {
//        AllSoundEvents.prepare();
//        for (AllSoundEvents.SoundEntry entry : ALL.values())
//            entry.prepare();
    }

    private static AllSoundEvents.SoundEntryBuilder create(String name) {
        return create(Create.asResource(name));
    }

    public static AllSoundEvents.SoundEntryBuilder create(ResourceLocation id) {
        return new AllSoundEvents.SoundEntryBuilder(id);
    }

    public static void register(RegisterEvent event) {
        AllSoundEvents.register(event);
//        event.register(Registries.SOUND_EVENT, helper -> {
//            for (AllSoundEvents.SoundEntry entry : ALL.values())
//                entry.register(helper);
//        });
    }
}
