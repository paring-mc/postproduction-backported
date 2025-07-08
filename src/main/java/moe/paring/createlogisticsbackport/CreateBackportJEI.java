package moe.paring.createlogisticsbackport;

import com.simibubi.create.compat.jei.GhostIngredientHandler;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import moe.paring.createlogisticsbackport.compat.jei.StockKeeperTransferHandler;
import moe.paring.createlogisticsbackport.content.logistics.factoryBoard.FactoryPanelSetItemScreen;
import moe.paring.createlogisticsbackport.content.logistics.filter.AbstractFilterScreen2;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
public class CreateBackportJEI implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CreateLogisticsBackport.MODID, "jei_plugin");

    public static IJeiRuntime runtime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addUniversalRecipeTransferHandler(new StockKeeperTransferHandler(registration.getJeiHelpers()));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(AbstractFilterScreen2.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(FactoryPanelSetItemScreen.class, new GhostIngredientHandler());
    }
}
