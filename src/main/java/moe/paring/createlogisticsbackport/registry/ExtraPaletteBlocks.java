package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.WindowBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.WindowGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import moe.paring.createlogisticsbackport.CreateLogisticsBackport;
import moe.paring.createlogisticsbackport.content.decoration.palettes.WeatheredIronWindowCTBehaviour;
import moe.paring.createlogisticsbackport.content.decoration.palettes.WeatheredIronWindowPaneCTBehaviour;
import moe.paring.createlogisticsbackport.foundation.data.CreateRegistrate2;
import moe.paring.createlogisticsbackport.foundation.data.WindowGen2;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.Tags;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static moe.paring.createlogisticsbackport.foundation.data.WindowGen2.*;

public class ExtraPaletteBlocks {
    public static final BlockEntry<WindowBlock>
            CHERRY_WINDOW = woodenWindowBlock(WoodType.CHERRY, Blocks.CHERRY_PLANKS),
            BAMBOO_WINDOW = woodenWindowBlock(WoodType.BAMBOO, Blocks.BAMBOO_PLANKS),
            WEATHERED_IRON_WINDOW = WindowGen2
                    .randomisedWindowBlock("weathered_iron_window", ExtraBlocks.WEATHERED_IRON_BLOCK,
                            () -> RenderType::translucent, true, () -> MapColor.TERRACOTTA_LIGHT_GRAY)
                    .onRegister(CreateRegistrate2.connectedTextures(WeatheredIronWindowCTBehaviour::new))
                    .register();
    public static final BlockEntry<ConnectedGlassPaneBlock>
            CHERRY_WINDOW_PANE = woodenWindowPane(WoodType.CHERRY, CHERRY_WINDOW),
            BAMBOO_WINDOW_PANE = woodenWindowPane(WoodType.BAMBOO, BAMBOO_WINDOW),
            WEATHERED_IRON_WINDOW_PANE = customWindowPane("weathered_iron_window", WEATHERED_IRON_WINDOW, null, () -> RenderType::translucent)
                    .onRegister(CreateRegistrate2.connectedTextures(WeatheredIronWindowPaneCTBehaviour::new))
                    .register();

    public static void register() {
    }
}
