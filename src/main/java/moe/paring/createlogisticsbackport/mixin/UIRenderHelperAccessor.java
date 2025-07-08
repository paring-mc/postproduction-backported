package moe.paring.createlogisticsbackport.mixin;

import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.utility.Color;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UIRenderHelper.class)
public interface UIRenderHelperAccessor {
    @Invoker("drawTexturedQuad")
    static void invokeDrawTexturedQuad(Matrix4f m, Color c, int left, int right, int top, int bot, int z, float u1, float u2, float v1, float v2) {
        throw new AssertionError();
    }
}
