package xaeroplus.mixin.client.mc;

import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface AccessorWorldRenderer {
    @Accessor(value = "frustum")
    Frustum getFrustum();

    @Accessor(value = "chunks")
    BuiltChunkStorage getChunks();
}
