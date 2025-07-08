package moe.paring.createlogisticsbackport;

import com.simibubi.create.foundation.utility.Color;
import moe.paring.createlogisticsbackport.mixin.UIRenderHelperAccessor;
import moe.paring.createlogisticsbackport.registry.ExtraGuiTextures;
import net.minecraft.client.gui.GuiGraphics;

public class UIRenderHelperExtra {
    public static void drawStretched(GuiGraphics graphics, int left, int top, int w, int h, int z, ExtraGuiTextures tex) {
        tex.bind();
        UIRenderHelperAccessor.invokeDrawTexturedQuad(graphics.pose().last()
                        .pose(), Color.WHITE, left, left + w, top, top + h, z, tex.getStartX() / 256f, (tex.getStartX() + tex.getWidth()) / 256f,
                tex.getStartY() / 256f, (tex.getStartY() + tex.getHeight()) / 256f);
    }
}
