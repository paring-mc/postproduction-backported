package moe.paring.createlogisticsbackport;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import moe.paring.createlogisticsbackport.config.ExtraConfigs;
import moe.paring.createlogisticsbackport.content.logistics.packager.AllInventoryIdentifiers;
import moe.paring.createlogisticsbackport.content.logistics.packager.AllUnpackingHandlers;
import moe.paring.createlogisticsbackport.content.logistics.packagerLink.GlobalLogisticsManager;
import moe.paring.createlogisticsbackport.content.trains.schedule.destination.ExtraSchedules;
import moe.paring.createlogisticsbackport.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateLogisticsBackport.MODID)
public class CreateLogisticsBackport {
    public static volatile boolean INITIALIZED = false;

    // Define mod id in a common place for everything to reference
    public static final String MODID = "postproduction_backported";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final GlobalLogisticsManager LOGISTICS = new GlobalLogisticsManager();

    //    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Create.ID);
    public static final CreateRegistrate REGISTRATE = Create.REGISTRATE;

    public CreateLogisticsBackport() {
        try {
            ModLoadingContext modLoadingContext = ModLoadingContext.get();

            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

            ExtraSoundEvents.prepare();
            ExtraItems.register();
            ExtraBlocks.register();
            ExtraPaletteBlocks.register();
            ExtraMenuTypes.register();
            ExtraEntityTypes.register();
            ExtraBlockEntityTypes.register();
            ExtraSpriteShifts.register();
            ExtraPackets.registerPackets();
            ExtraConfigs.register(modLoadingContext);
            ExtraItemTags.init();
            ExtraBlockTags.init();
            ExtraSchedules.init();
            ExtraRecipeTypes.register(modEventBus);
            ExtraParticleTypes.register(modEventBus);

            modEventBus.addListener(CreateLogisticsBackport::init);
            modEventBus.addListener(ExtraEntityTypes::registerEntityAttributes);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateLogisticsBackportClient.onInit(modEventBus, forgeEventBus));
        } finally {
            INITIALIZED = true;
        }
    }

    public static void init(final FMLCommonSetupEvent event) {
        AllUnpackingHandlers.registerDefaults();
        AllInventoryIdentifiers.registerDefaults();
    }
}
