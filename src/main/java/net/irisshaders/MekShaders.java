package net.irisshaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.gl.blending.AlphaTests;
import net.irisshaders.iris.gl.state.FogMode;
import net.irisshaders.iris.pipeline.ShaderRenderingPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.pipeline.programs.ShaderKey;
import net.irisshaders.iris.shaderpack.loading.ProgramGroup;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import net.irisshaders.iris.shaderpack.programs.ProgramSet;
import net.irisshaders.iris.shaderpack.programs.ProgramSource;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import net.irisshaders.iris.vertices.IrisVertexFormats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.stream.Collectors;

public class MekShaders {

    public static void init() {

    }

    //public static ProgramId MEK_FLAME = ProgramId.register(ProgramGroup.Gbuffers, "mek_flame", ProgramId.SpiderEyes, set -> getSource(set, "mek_flame"));
    public static ProgramId MEK_SUIT = ProgramId.register(ProgramGroup.Gbuffers, "mekasuit", ProgramId.EntitiesTrans, set -> getSource(set, "mekasuit"));

    //public static ShaderKey MEK_FLAME_KEY = ShaderKey.register("MEK_FLAME", MEK_FLAME, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR, FogMode.PER_VERTEX, ShaderKey.LightingModel.LIGHTMAP);
    //public static ShaderKey MEK_FLAME_SHADOW_KEY = ShaderKey.registerShadow("MEK_FLAME_SHADOW", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR, FogMode.OFF, ShaderKey.LightingModel.LIGHTMAP);
    public static ShaderKey MEK_SUIT_KEY = ShaderKey.register("MEK_SUIT", MEK_SUIT, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, ShaderKey.LightingModel.DIFFUSE_LM);
    public static ShaderKey MEK_SUIT_SHADOW_KEY = ShaderKey.registerShadow("MEK_SUIT_SHADOW", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.OFF, ShaderKey.LightingModel.LIGHTMAP);

    public static class MekType extends RenderType {

        public static final RenderType MEKASUIT = RenderType.create("mekasuit", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 131_072, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(MekShaders::getMekasuitShader))
                        .setTextureState(BLOCK_SHEET)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true)
        );

        //Ignored
        public MekType(String string, VertexFormat arg, VertexFormat.Mode arg2, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
            super(string, arg, arg2, i, bl, bl2, runnable, runnable2);
        }
    }


    private static ShaderInstance getMekasuitShader() {
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();

        if (pipeline instanceof ShaderRenderingPipeline) {
            return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? MEK_SUIT_SHADOW_KEY : MEK_SUIT_KEY);
        }

        return GameRenderer.getRendertypeEntityCutoutShader();
    }

    public static Optional<ProgramSource> getSource(ProgramSet programSet, String program) {
        try {
            var vertexOpt = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("iris", "shaders/" + program + ".vsh"));
            String vertexSource = null;
            if (vertexOpt.isPresent()) {
                vertexSource = vertexOpt.get().openAsReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }

            var geometryOpt = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("iris", "shaders/" + program + ".gsh"));
            String geometrySource = null;
            if (geometryOpt.isPresent()) {
                geometrySource = geometryOpt.get().openAsReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }

            var tessControlOpt = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("iris", "shaders/" + program + ".tcs"));
            String tessControlSource = null;
            if (tessControlOpt.isPresent()) {
                tessControlSource = tessControlOpt.get().openAsReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }

            var tessEvalOpt = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("iris", "shaders/" + program + ".tes"));
            String tessEvalSource = null;
            if (tessEvalOpt.isPresent()) {
                tessEvalSource = tessEvalOpt.get().openAsReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
            var fragmentOpt = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("iris", "shaders/" + program + ".fsh"));
            String fragmentSource = null;
            if (fragmentOpt.isPresent()) {
                fragmentSource = fragmentOpt.get().openAsReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
            ProgramSource source = new ProgramSource(program, vertexSource, geometrySource, tessControlSource, tessEvalSource, fragmentSource, programSet, programSet.shaderProperties, null);
            return source.requireValid();
        }catch (Exception ignored) {

        }
        return Optional.empty();
    }
}
