package moe.paring.createlogisticsbackport.polyfill;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import net.minecraft.client.gui.GuiGraphics;

public class NewIconButton extends IconButton {
    public boolean green;
    public NewIconButton(int x, int y, ScreenElement icon) {
        super(x, y, icon);
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;

            Object button = !active ? ExtraGuiTextures.BUTTON_DISABLED
                    : isHovered && AllKeys.isMouseButtonDown(0) ? AllGuiTextures.BUTTON_DOWN
                    : isHovered ? AllGuiTextures.BUTTON_HOVER
                    : green ? ExtraGuiTextures.BUTTON_GREEN : AllGuiTextures.BUTTON;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawBgUnified(graphics, button);
            icon.render(graphics, getX() + 1, getY() + 1);
        }
    }

    protected void drawBgUnified(GuiGraphics graphics, Object button) {
        if (button instanceof AllGuiTextures t) {
            graphics.blit(t.location, getX(), getY(), t.startX, t.startY, t.width, t.height);
            return;
        }
        if (button instanceof ExtraGuiTextures t) {
            graphics.blit(t.location, getX(), getY(), t.getStartX(), t.getStartY(), t.getWidth(), t.getHeight());
            return;
        }
        throw new IllegalArgumentException();
    }
}
