package moe.paring.createlogisticsbackport.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ExtraRenderTypes extends RenderStateShard {
    private static final Function<ResourceLocation, RenderType> CHAIN = Util.memoize((p_234330_) -> RenderType.create("chain_conveyor_chain", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false,
            true, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER)
                    .setTextureState(new TextureStateShard(p_234330_, false, true))
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(false)));

    public static RenderType chain(ResourceLocation pLocation) {
        return CHAIN.apply(pLocation);
    }


    public ExtraRenderTypes() {
        super(null, null, null);
    }
}
