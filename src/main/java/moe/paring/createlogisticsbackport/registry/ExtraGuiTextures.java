package moe.paring.createlogisticsbackport.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public enum ExtraGuiTextures implements ScreenElement {
    POSTBOX_HEADER("frogport_and_mailbox", 0, 23, 214, 24),
    FROGPORT_HEADER("frogport_and_mailbox", 0, 0, 214, 17),
    FROGPORT_SLOT("frogport_and_mailbox", 26, 55, 18, 18),
    FROGPORT_EDIT_NAME("frogport_and_mailbox", 230, 3, 13, 13),
    FROGPORT_BG("frogport_and_mailbox", 0, 47, 220, 82),

    FACTORY_GAUGE_RECIPE("factory_gauge", 32, 0, 192, 96),
    FACTORY_GAUGE_RESTOCK("factory_gauge", 32, 112, 192, 40),
    FACTORY_GAUGE_BOTTOM("factory_gauge", 32, 176, 200, 64),

    REDSTONE_REQUESTER("requester", 16, 16, 232, 120),
    BUTTON_GREEN("widgets", 72, 0, 18, 18),
    BUTTON_DISABLED("widgets", 90, 0, 18, 18),

    STOCK_KEEPER_CATEGORY("stock_keeper_categories", 32, 32, 192, 20),
    STOCK_KEEPER_CATEGORY_SAYS("stock_keeper_categories", 238, 86, 14, 20),
    STOCK_KEEPER_CATEGORY_HEADER("stock_keeper_categories", 32, 0, 192, 18),
    STOCK_KEEPER_CATEGORY_EDIT("stock_keeper_categories", 32, 208, 192, 38),
    STOCK_KEEPER_CATEGORY_FOOTER("stock_keeper_categories", 32, 79, 200, 33),
    STOCK_KEEPER_CATEGORY_NEW("stock_keeper_categories", 38, 127, 27, 18),
    STOCK_KEEPER_CATEGORY_ENTRY("stock_keeper_categories", 38, 159, 171, 18),
    STOCK_KEEPER_CATEGORY_UP("stock_keeper_categories", 211, 160, 8, 8),
    STOCK_KEEPER_CATEGORY_DOWN("stock_keeper_categories", 211, 169, 8, 8),

    STOCK_KEEPER_REQUEST_HEADER("stock_keeper", 0, 0, 256, 36),
    STOCK_KEEPER_REQUEST_BODY("stock_keeper", 0, 48, 256, 20),
    STOCK_KEEPER_REQUEST_FOOTER("stock_keeper", 0, 80, 256, 80),
    STOCK_KEEPER_REQUEST_SEARCH("stock_keeper", 57, 17, 142, 18),
    STOCK_KEEPER_REQUEST_SAYS("stock_keeper", 4, 163, 8, 16),
    STOCK_KEEPER_REQUEST_LOCKED("stock_keeper", 16, 176, 15, 16),
    STOCK_KEEPER_REQUEST_UNLOCKED("stock_keeper", 32, 176, 15, 16),
    STOCK_KEEPER_REQUEST_SLOT("stock_keeper", 32, 200, 18, 18),
    STOCK_KEEPER_REQUEST_BLUEPRINT_LEFT("stock_keeper", 28, 220, 10, 25),
    STOCK_KEEPER_REQUEST_BLUEPRINT_MIDDLE("stock_keeper", 38, 220, 4, 25),
    STOCK_KEEPER_REQUEST_BLUEPRINT_RIGHT("stock_keeper", 42, 220, 10, 25),
    STOCK_KEEPER_REQUEST_SEND_HOVER("stock_keeper", 55, 200, 80, 20),
    STOCK_KEEPER_REQUEST_SCROLL_TOP("stock_keeper", 219, 192, 5, 4),
    STOCK_KEEPER_REQUEST_SCROLL_PAD("stock_keeper", 219, 196, 5, 1),
    STOCK_KEEPER_REQUEST_SCROLL_MID("stock_keeper", 219, 197, 5, 9),
    STOCK_KEEPER_REQUEST_SCROLL_BOT("stock_keeper", 219, 207, 5, 5),
    STOCK_KEEPER_REQUEST_BANNER_L("stock_keeper", 64, 228, 8, 16),
    STOCK_KEEPER_REQUEST_BANNER_M("stock_keeper", 73, 228, 1, 16),
    STOCK_KEEPER_REQUEST_BANNER_R("stock_keeper", 75, 228, 8, 16),
    STOCK_KEEPER_REQUEST_BG("stock_keeper", 37, 48, 182, 20),
    STOCK_KEEPER_CATEGORY_HIDDEN("stock_keeper", 143, 176, 8, 8),
    STOCK_KEEPER_CATEGORY_SHOWN("stock_keeper", 151, 176, 8, 8),
    NUMBERS("stock_keeper", 48, 176, 5, 8);
    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;

    ExtraGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    ExtraGuiTextures(String location, int startX, int startY, int width, int height) {
        this(Create.ID, location, startX, startY, width, height);
    }

    ExtraGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @NotNull ResourceLocation getLocation() {
        return location;
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, getLocation());
    }
}
