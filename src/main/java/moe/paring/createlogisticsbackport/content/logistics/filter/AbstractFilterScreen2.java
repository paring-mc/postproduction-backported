package moe.paring.createlogisticsbackport.content.logistics.filter;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.logistics.filter.AbstractFilterMenu;
import com.simibubi.create.content.logistics.filter.FilterScreenPacket;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;
import moe.paring.createlogisticsbackport.polyfill.NewIconButton;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import moe.paring.createlogisticsbackport.registry.ExtraItems;
import moe.paring.createlogisticsbackport.registry.ExtraPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;
import java.util.List;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public abstract class AbstractFilterScreen2<F extends AbstractFilterMenu> extends AbstractSimiContainerScreen<F> {

    protected ExtraGuiTextures background;
    private List<Rect2i> extraAreas = Collections.emptyList();

    private IconButton resetButton;
    private IconButton confirmButton;

    protected AbstractFilterScreen2(F menu, Inventory inv, Component title, ExtraGuiTextures background) {
        super(menu, inv, title);
        this.background = background;
    }

    @Override
    protected void init() {
        setWindowSize(Math.max(background.getWidth(), PLAYER_INVENTORY.width),
                background.getHeight() + 4 + PLAYER_INVENTORY.height);
        super.init();

        int x = leftPos;
        int y = topPos;

        resetButton = new IconButton(x + background.getWidth() - 62, y + background.getHeight() - 24, AllIcons.I_TRASH);
        resetButton.withCallback(() -> {
            menu.clearContents();
            contentsCleared();
            menu.sendClearPacket();
        });
        confirmButton = new IconButton(x + background.getWidth() - 33, y + background.getHeight() - 24, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });

        addRenderableWidget(resetButton);
        addRenderableWidget(confirmButton);

        extraAreas = ImmutableList.of(new Rect2i(x + background.getWidth(), y + background.getHeight() - 40, 80, 48));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = getLeftOfCentered(PLAYER_INVENTORY.width);
        int invY = topPos + background.getHeight() + 4;
        renderPlayerInventory(graphics, invX, invY);

        int x = leftPos;
        int y = topPos;

        background.render(graphics, x, y);
        graphics.drawString(font, title, x + (background.getWidth() - 8) / 2 - font.width(title) / 2, y + 4,
                ExtraItems.PACKAGE_FILTER.isIn(menu.contentHolder) ? 0x3D3C48
                        : AllItems.FILTER.isIn(menu.contentHolder) ? 0x303030 : 0x592424,
                false);

        GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(x + background.getWidth() + 8, y + background.getHeight() - 52, -200)
                .scale(4)
                .render(graphics);
    }

    @Override
    protected void containerTick() {
        if (!menu.player.getMainHandItem()
                .equals(menu.contentHolder, false))
            menu.player.closeContainer();

        super.containerTick();

        handleTooltips();
        handleIndicators();
    }

    protected void handleTooltips() {
        List<NewIconButton> tooltipButtons = getTooltipButtons();

        for (NewIconButton button : tooltipButtons) {
            if (!button.getToolTip()
                    .isEmpty()) {
                button.setToolTip(button.getToolTip()
                        .get(0));
                button.getToolTip()
                        .add(TooltipHelper.holdShift(Palette.YELLOW, hasShiftDown()));
            }
        }

        if (hasShiftDown()) {
            List<MutableComponent> tooltipDescriptions = getTooltipDescriptions();
            for (int i = 0; i < tooltipButtons.size(); i++)
                fillToolTip(tooltipButtons.get(i), tooltipDescriptions.get(i));
        }
    }

    public void handleIndicators() {
        for (var button : getTooltipButtons())
            button.green = !isButtonEnabled(button);
    }

    protected abstract boolean isButtonEnabled(IconButton button);

    protected List<NewIconButton> getTooltipButtons() {
        return Collections.emptyList();
    }

    protected List<MutableComponent> getTooltipDescriptions() {
        return Collections.emptyList();
    }

    private void fillToolTip(IconButton button, Component tooltip) {
        if (!button.isHoveredOrFocused())
            return;
        List<Component> tip = button.getToolTip();
        tip.addAll(TooltipHelper.cutTextComponent(tooltip, Palette.ALL_GRAY));
    }

    protected void contentsCleared() {}

    protected void sendOptionUpdate(FilterScreenPacket2.Option option) {
        ExtraPackets.getChannel()
                .sendToServer(new FilterScreenPacket2(option));
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

}

