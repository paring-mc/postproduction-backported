package moe.paring.createlogisticsbackport.content.equipment.blueprint;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.blueprint.BlueprintEntity;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.trains.track.TrackPlacement;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import moe.paring.createlogisticsbackport.content.logistics.BigItemStack;
import moe.paring.createlogisticsbackport.content.logistics.packager.InventorySummary;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.BlueprintOverlayShopContext;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.ShoppingListItem;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.TableClothBlockEntity;
import moe.paring.createlogisticsbackport.polyfill.ItemHelperExtra;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import moe.paring.createlogisticsbackport.registry.ExtraItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class BlueprintOverlayRenderer2 {

    public static final IGuiOverlay OVERLAY = BlueprintOverlayRenderer2::renderOverlay;

    static BlueprintOverlayShopContext shopContext;

    static boolean active;
    static boolean empty;
    static boolean noOutput;

    static List<Pair<ItemStack, Boolean>> ingredients = new ArrayList<>();

    static Map<ItemStack, ItemStack[]> cachedRenderedFilters = new IdentityHashMap<>();
    static List<ItemStack> results = new ArrayList<>();
    static boolean resultCraftable = false;

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();

        active = false;
        noOutput = false;
        shopContext = null;

        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        HitResult mouseOver = mc.hitResult;
        if (mouseOver == null)
            return;
        if (mouseOver.getType() != HitResult.Type.ENTITY)
            return;

        EntityHitResult entityRay = (EntityHitResult) mouseOver;
        if (!(entityRay.getEntity() instanceof BlueprintEntity blueprintEntity))
            return;

        active = true;
    }

    public static void displayTrackRequirements(TrackPlacement.PlacementInfo info, ItemStack pavementItem) {
        if (active)
            return;
        prepareCustomOverlay();

        int tracks = info.requiredTracks;
        while (tracks > 0) {
            ingredients.add(
                    Pair.of(new ItemStack(info.trackMaterial.getBlock(), Math.min(64, tracks)), info.hasRequiredTracks));
            tracks -= 64;
        }

        int pavement = info.requiredPavement;
        while (pavement > 0) {
            ingredients.add(Pair.of(ItemHandlerHelper.copyStackWithSize(pavementItem, Math.min(64, pavement)),
                    info.hasRequiredPavement));
            pavement -= 64;
        }
    }

    public static void displayChainRequirements(Item chainItem, int count, boolean fulfilled) {
        if (active)
            return;
        prepareCustomOverlay();

        int chains = count;
        while (chains > 0) {
            ingredients.add(Pair.of(new ItemStack(chainItem, Math.min(64, chains)), fulfilled));
            chains -= 64;
        }
    }

    public static void displayClothShop(TableClothBlockEntity dce, int alreadyPurchased, ShoppingListItem.ShoppingList list) {
        if (active)
            return;
        prepareCustomOverlay();
        noOutput = false;

        shopContext = new BlueprintOverlayShopContext(false, dce.getStockLevelForTrade(list), alreadyPurchased);

            ingredients.add(Pair.of(dce.getPaymentItem()
                        .copyWithCount(dce.getPaymentAmount()),
                !dce.getPaymentItem()
                        .isEmpty() && shopContext.stockLevel() > shopContext.purchases()));
        for (BigItemStack entry : dce.requestData.encodedRequest.stacks())
            results.add(entry.stack.copyWithCount(entry.count));
    }

    public static void displayShoppingList(Couple<InventorySummary> bakedList) {
        if (active || bakedList == null)
            return;
        Minecraft mc = Minecraft.getInstance();
        prepareCustomOverlay();
        noOutput = false;

        shopContext = new BlueprintOverlayShopContext(true, 1, 0);

        for (BigItemStack entry : bakedList.getSecond()
                .getStacksByCount()) {
            ingredients.add(Pair.of(entry.stack.copyWithCount(entry.count), canAfford(mc.player, entry)));
        }

        for (BigItemStack entry : bakedList.getFirst()
                .getStacksByCount())
            results.add(entry.stack.copyWithCount(entry.count));
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

    private static void prepareCustomOverlay() {
        active = true;
        empty = false;
        noOutput = true;
        ingredients.clear();
        results.clear();
        shopContext = null;
    }

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen != null)
            return;

        if (!active || empty)
            return;

        boolean invalidShop = shopContext != null && (ingredients.isEmpty() || ingredients.get(0)
                .getFirst()
                .isEmpty() || shopContext.stockLevel() == 0);

        int w = 21 * ingredients.size();

        if (!noOutput) {
            w += 21 * results.size();
            w += 30;
        }

        int x = (width - w) / 2;
        int y = (int) (height - 100);

        if (shopContext != null) {
            TooltipRenderUtil.renderTooltipBackground(graphics, x - 2, y + 1, w + 4, 19, 0, 0x55_000000, 0x55_000000, 0,
                    0);

            ExtraGuiTextures.TRADE_OVERLAY.render(graphics, width / 2 - 48, y - 19);
            if (shopContext.purchases() > 0) {
                graphics.renderItem(ExtraItems.SHOPPING_LIST.asStack(), width / 2 + 20, y - 20);
                graphics.drawString(mc.font, Component.literal("x" + shopContext.purchases()), width / 2 + 20 + 16,
                        y - 20 + 4, 0xff_eeeeee, true);
            }
        }

        // Ingredients
        for (Pair<ItemStack, Boolean> pair : ingredients) {
            RenderSystem.enableBlend();
            (pair.getSecond() ? AllGuiTextures.HOTSLOT_ACTIVE : AllGuiTextures.HOTSLOT).render(graphics, x, y);
            ItemStack itemStack = pair.getFirst();
            String count = shopContext != null && !shopContext.checkout() || pair.getSecond() ? null
                    : ChatFormatting.GOLD.toString() + itemStack.getCount();
            drawItemStack(graphics, mc, x, y, itemStack, count);
            x += 21;
        }

        if (noOutput)
            return;

        // Arrow
        x += 5;
        RenderSystem.enableBlend();
        if (invalidShop)
            ExtraGuiTextures.HOTSLOT_ARROW_BAD.render(graphics, x, y + 4);
        else
            AllGuiTextures.HOTSLOT_ARROW.render(graphics, x, y + 4);
        x += 25;

        // Outputs
        if (results.isEmpty()) {
            AllGuiTextures.HOTSLOT.render(graphics, x, y);
            GuiGameElement.of(Items.BARRIER)
                    .at(x + 3, y + 3)
                    .render(graphics);
        } else {
            for (ItemStack result : results) {
                AllGuiTextures slot = resultCraftable ? AllGuiTextures.HOTSLOT_SUPER_ACTIVE : AllGuiTextures.HOTSLOT;
                if (!invalidShop && shopContext != null && shopContext.stockLevel() > shopContext.purchases())
                    slot = AllGuiTextures.HOTSLOT_ACTIVE;
                slot.render(graphics, resultCraftable ? x - 1 : x, resultCraftable ? y - 1 : y);
                drawItemStack(graphics, mc, x, y, result, null);
                x += 21;
            }
        }

        if (shopContext != null && !shopContext.checkout()) {
            int cycle = 0;
            for (boolean count : Iterate.trueAndFalse)
                for (int i = 0; i < results.size(); i++) {
                    ItemStack result = results.get(i);
                    List<Component> tooltipLines = result.getTooltipLines(mc.player, TooltipFlag.NORMAL);
                    if (tooltipLines.size() <= 1)
                        continue;
                    if (count) {
                        cycle++;
                        continue;
                    }
                    if ((gui.getGuiTicks() / 40) % cycle != i)
                        continue;
                    graphics.renderComponentTooltip(gui.getFont(), tooltipLines, mc.getWindow()
                                    .getGuiScaledWidth(),
                            mc.getWindow()
                                    .getGuiScaledHeight());
                }
        }

        RenderSystem.disableBlend();
    }

    public static void drawItemStack(GuiGraphics graphics, Minecraft mc, int x, int y, ItemStack itemStack,
                                     String count) {
        if (itemStack.getItem() instanceof FilterItem) {
            int step = AnimationTickHolder.getTicks(mc.level) / 10;
            ItemStack[] itemsMatchingFilter = getItemsMatchingFilter(itemStack);
            if (itemsMatchingFilter.length > 0)
                itemStack = itemsMatchingFilter[step % itemsMatchingFilter.length];
        }

        GuiGameElement.of(itemStack)
                .at(x + 3, y + 3)
                .render(graphics);
        graphics.renderItemDecorations(mc.font, itemStack, x + 3, y + 3, count);
    }

    private static ItemStack[] getItemsMatchingFilter(ItemStack filter) {
        return cachedRenderedFilters.computeIfAbsent(filter, itemStack -> {
            CompoundTag tag = itemStack.getOrCreateTag();

            if (AllItems.FILTER.isIn(itemStack) && !tag.getBoolean("Blacklist")) {
                ItemStackHandler filterItems = FilterItem.getFilterItems(itemStack);
                return ItemHelperExtra.getNonEmptyStacks(filterItems).toArray(ItemStack[]::new);
            }

//            if (AllItems.ATTRIBUTE_FILTER.isIn(itemStack)) {
//                AttributeFilterMenu.WhitelistMode whitelistMode = AttributeFilterMenu.WhitelistMode.values()[tag.getInt("WhitelistMode")];
//                ListTag attributes = tag.getList("MatchedAttributes", net.minecraft.nbt.Tag.TAG_COMPOUND);
//                if (whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ && attributes.size() == 1) {
//                    ItemAttribute fromNBT = ItemAttribute.fromNBT((CompoundTag) attributes.get(0));
//                    if (fromNBT instanceof InTagAttribute inTag) {
//                        ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();
//                        if (tagManager.isKnownTagName(inTag.tag)) {
//                            ITag<Item> taggedItems = tagManager.getTag(inTag.tag);
//                            if (!taggedItems.isEmpty()) {
//                                ItemStack[] stacks = new ItemStack[taggedItems.size()];
//                                int i = 0;
//                                for (Item item : taggedItems) {
//                                    stacks[i] = new ItemStack(item);
//                                    i++;
//                                }
//                                return stacks;
//                            }
//                        }
//                    }
//                }
//            }

            return new ItemStack[0];
        });
    }

}

