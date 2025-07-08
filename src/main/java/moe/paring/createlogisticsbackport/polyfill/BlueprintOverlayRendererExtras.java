package moe.paring.createlogisticsbackport.polyfill;

import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import moe.paring.createlogisticsbackport.content.logistics.packager.InventorySummary;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.BlueprintOverlayShopContext;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.ShoppingListItem;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockEntity;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import moe.paring.createlogisticsbackport.mixin.BlueprintOverlayRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class BlueprintOverlayRendererExtras {
    static BlueprintOverlayShopContext shopContext;
    static List<ItemStack> results = new ArrayList<>();

    public static void displayClothShop(TableClothBlockEntity dce, int alreadyPurchased, ShoppingListItem.ShoppingList list) {
        if (BlueprintOverlayRendererAccessor.getActive())
            return;

        prepareCustomOverlay();
        BlueprintOverlayRendererAccessor.setNoOutput(false);

        shopContext = new BlueprintOverlayShopContext(false, dce.getStockLevelForTrade(list), alreadyPurchased);

        BlueprintOverlayRendererAccessor.getIngredients().add(Pair.of(dce.getPaymentItem()
                        .copyWithCount(dce.getPaymentAmount()),
                !dce.getPaymentItem()
                        .isEmpty() && shopContext.stockLevel() > shopContext.purchases()));
        for (BigItemStack entry : dce.requestData.encodedRequest.stacks())
            results.add(entry.stack.copyWithCount(entry.count));
    }

    public static void displayChainRequirements(Item chainItem, int count, boolean fulfilled) {
        if (BlueprintOverlayRendererAccessor.getActive())
            return;
        prepareCustomOverlay();

        int chains = count;
        while (chains > 0) {
            BlueprintOverlayRendererAccessor.getIngredients().add(Pair.of(new ItemStack(chainItem, Math.min(64, chains)), fulfilled));
            chains -= 64;
        }
    }

    private static void prepareCustomOverlay() {
        BlueprintOverlayRendererAccessor.setActive(true);
        BlueprintOverlayRendererAccessor.setEmpty(false);
        BlueprintOverlayRendererAccessor.setNoOutput(true);
        BlueprintOverlayRendererAccessor.getIngredients().clear();
        results.clear();
        shopContext = null;
    }

    private static boolean canAfford(Player player, BigItemStack entry) {
        int itemsPresent = 0;
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack item = player.getInventory()
                    .getItem(i);
            if (item.isEmpty() || !ItemHandlerHelper.canItemStacksStack(item, entry.stack))
                continue;
            itemsPresent += item.getCount();
        }
        return itemsPresent >= entry.count;
    }

    public static void displayShoppingList(Couple<InventorySummary> bakedList) {
        if (BlueprintOverlayRendererAccessor.getActive() || bakedList == null)
            return;
        Minecraft mc = Minecraft.getInstance();
        prepareCustomOverlay();
        BlueprintOverlayRendererAccessor.setNoOutput(false);

        shopContext = new BlueprintOverlayShopContext(true, 1, 0);

        for (BigItemStack entry : bakedList.getSecond()
                .getStacksByCount()) {
            BlueprintOverlayRendererAccessor.getIngredients().add(Pair.of(entry.stack.copyWithCount(entry.count), canAfford(mc.player, entry)));
        }

        for (BigItemStack entry : bakedList.getFirst()
                .getStacksByCount())
            results.add(entry.stack.copyWithCount(entry.count));
    }

    // TODO: renderOverlay
}
