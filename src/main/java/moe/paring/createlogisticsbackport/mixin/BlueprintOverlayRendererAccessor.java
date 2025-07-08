package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.content.equipment.blueprint.BlueprintOverlayRenderer;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BlueprintOverlayRenderer.class)
public interface BlueprintOverlayRendererAccessor {
    @Accessor("active")
    static boolean getActive() {
        throw new AssertionError();
    }

    @Accessor("active")
    static void setActive(boolean active) {
        throw new AssertionError();
    }

    @Accessor("empty")
    static boolean getEmpty() {
        throw new AssertionError();
    }

    @Accessor("empty")
    static void setEmpty(boolean empty) {
        throw new AssertionError();
    }

    @Accessor("noOutput")
    static boolean getNoOutput() {
        throw new AssertionError();
    }

    @Accessor("noOutput")
    static void setNoOutput(boolean value) {
        throw new AssertionError();
    }

    @Accessor("ingredients")
    static List<Pair<ItemStack, Boolean>> getIngredients() {
        throw new AssertionError();
    }
}
