package net.irisshaders.iris.pipeline.programs;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.iris.gl.blending.AlphaTest;
import net.irisshaders.iris.gl.blending.AlphaTests;
import net.irisshaders.iris.gl.state.FogMode;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import net.irisshaders.iris.vertices.IrisVertexFormats;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public record ShaderKey(String name, ProgramId program, AlphaTest alphaTest, VertexFormat vertexFormat, FogMode fogMode, LightingModel lightingModel) {
	public static final List<ShaderKey> SHADERS = new ArrayList<>();
	public static final List<ShaderKey> SHADOWS = new ArrayList<>();
	public static List<ShaderKey> VALUES = null;
	// if you auto-format this and destroy all the manual indentation, I'll steal your kneecaps

	public static ShaderKey BASIC = register("BASIC", ProgramId.Basic, AlphaTests.OFF, DefaultVertexFormat.POSITION, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey BASIC_COLOR = register("BASIC_COLOR", ProgramId.Basic, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey TEXTURED = register("TEXTURED", ProgramId.Textured, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_TEX, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey TEXTURED_COLOR = register("TEXTURED_COLOR", ProgramId.Textured, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SKY_BASIC = register("SKY_BASIC", ProgramId.SkyBasic, AlphaTests.OFF, DefaultVertexFormat.POSITION, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey SKY_BASIC_COLOR = register("SKY_BASIC_COLOR", ProgramId.SkyBasic, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SKY_TEXTURED = register("SKY_TEXTURED", ProgramId.SkyTextured, AlphaTests.OFF, DefaultVertexFormat.POSITION_TEX, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SKY_TEXTURED_COLOR = register("SKY_TEXTURED_COLOR", ProgramId.SkyTextured, AlphaTests.OFF, DefaultVertexFormat.POSITION_TEX_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey CLOUDS = register("CLOUDS", ProgramId.Clouds, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey CLOUDS_SODIUM = register("CLOUDS_SODIUM", ProgramId.Clouds, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.CLOUDS, FogMode.PER_FRAGMENT, LightingModel.LIGHTMAP);
	public static ShaderKey TERRAIN_SOLID = register("TERRAIN_SOLID", ProgramId.TerrainSolid, AlphaTests.OFF, IrisVertexFormats.TERRAIN, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TERRAIN_CUTOUT = register("TERRAIN_CUTOUT", ProgramId.TerrainCutout, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.TERRAIN, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TERRAIN_TRANSLUCENT = register("TERRAIN_TRANSLUCENT", ProgramId.Water, AlphaTests.OFF, IrisVertexFormats.TERRAIN, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey MOVING_BLOCK = register("MOVING_BLOCK", ProgramId.Block, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.TERRAIN, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey ENTITIES_ALPHA = register("ENTITIES_ALPHA", ProgramId.Entities, AlphaTests.VERTEX_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey ENTITIES_SOLID = register("ENTITIES_SOLID", ProgramId.Entities, AlphaTests.OFF, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey ENTITIES_SOLID_DIFFUSE = register("ENTITIES_SOLID_DIFFUSE", ProgramId.Entities, AlphaTests.OFF, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey ENTITIES_SOLID_BRIGHT = register("ENTITIES_SOLID_BRIGHT", ProgramId.Entities, AlphaTests.OFF, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey ENTITIES_CUTOUT = register("ENTITIES_CUTOUT", ProgramId.Entities, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey ENTITIES_CUTOUT_DIFFUSE = register("ENTITIES_CUTOUT_DIFFUSE", ProgramId.Entities, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey ENTITIES_TRANSLUCENT = register("ENTITIES_TRANSLUCENT", ProgramId.EntitiesTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey ENTITIES_EYES = register("ENTITIES_EYES", ProgramId.SpiderEyes, AlphaTests.NON_ZERO_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey ENTITIES_EYES_TRANS = register("ENTITIES_EYES_TRANS", ProgramId.SpiderEyes, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey HAND_CUTOUT = register("HAND_CUTOUT", ProgramId.Hand, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey HAND_CUTOUT_BRIGHT = register("HAND_CUTOUT_BRIGHT", ProgramId.Hand, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey HAND_CUTOUT_DIFFUSE = register("HAND_CUTOUT_DIFFUSE", ProgramId.Hand, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey HAND_TEXT = register("HAND_TEXT", ProgramId.Hand, AlphaTests.NON_ZERO_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey HAND_TEXT_INTENSITY = register("HAND_TEXT_INTENSITY", ProgramId.Hand, AlphaTests.NON_ZERO_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey HAND_TRANSLUCENT = register("HAND_TRANSLUCENT", ProgramId.HandWater, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey HAND_WATER_BRIGHT = register("HAND_WATER_BRIGHT", ProgramId.HandWater, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey HAND_WATER_DIFFUSE = register("HAND_WATER_DIFFUSE", ProgramId.HandWater, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey LIGHTNING = register("LIGHTNING", ProgramId.Entities, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey LEASH = register("LEASH", ProgramId.Basic, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TEXT_BG = register("TEXT_BG", ProgramId.EntitiesTrans, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey PARTICLES = register("PARTICLES", ProgramId.Particles, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.PARTICLE, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey PARTICLES_TRANS = register("PARTICLES_TRANS", ProgramId.ParticlesTrans, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.PARTICLE, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey WEATHER = register("WEATHER", ProgramId.Weather, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.PARTICLE, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey CRUMBLING = register("CRUMBLING", ProgramId.DamagedBlock, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.TERRAIN, FogMode.OFF, LightingModel.FULLBRIGHT);
	public static ShaderKey TEXT = register("TEXT", ProgramId.EntitiesTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TEXT_INTENSITY = register("TEXT_INTENSITY", ProgramId.EntitiesTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TEXT_BE = register("TEXT_BE", ProgramId.BlockTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey TEXT_INTENSITY_BE = register("TEXT_INTENSITY_BE", ProgramId.BlockTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.GLYPH, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey BLOCK_ENTITY = register("BLOCK_ENTITY", ProgramId.Block, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey BLOCK_ENTITY_BRIGHT = register("BLOCK_ENTITY_BRIGHT", ProgramId.Block, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.FULLBRIGHT);
	public static ShaderKey BLOCK_ENTITY_DIFFUSE = register("BLOCK_ENTITY_DIFFUSE", ProgramId.Block, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey BE_TRANSLUCENT = register("BE_TRANSLUCENT", ProgramId.BlockTrans, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.PER_VERTEX, LightingModel.DIFFUSE_LM);
	public static ShaderKey BEACON = register("BEACON", ProgramId.BeaconBeam, AlphaTests.OFF, DefaultVertexFormat.BLOCK, FogMode.PER_FRAGMENT, LightingModel.FULLBRIGHT);
	public static ShaderKey GLINT = register("GLINT", ProgramId.ArmorGlint, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_TEX, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);
	public static ShaderKey LINES = register("LINES", ProgramId.Line, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR_NORMAL, FogMode.PER_VERTEX, LightingModel.LIGHTMAP);

	// Note: These must be at the very end (NewWorldRenderingPipeline implementation details)
	public static ShaderKey SHADOW_TERRAIN_CUTOUT = registerShadow("SHADOW_TERRAIN_CUTOUT", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.TERRAIN, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_ENTITIES_CUTOUT = registerShadow("SHADOW_ENTITIES_CUTOUT", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, IrisVertexFormats.ENTITY, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_BEACON_BEAM = registerShadow("SHADOW_BEACON_BEAM", ProgramId.Shadow, AlphaTests.OFF, DefaultVertexFormat.BLOCK, FogMode.OFF, LightingModel.FULLBRIGHT);
	public static ShaderKey SHADOW_BASIC = registerShadow("SHADOW_BASIC", ProgramId.Shadow, AlphaTests.OFF, DefaultVertexFormat.POSITION, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_BASIC_COLOR = registerShadow("SHADOW_BASIC_COLOR", ProgramId.Shadow, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_TEX = registerShadow("SHADOW_TEX", ProgramId.Shadow, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_TEX, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_TEX_COLOR = registerShadow("SHADOW_TEX_COLOR", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_CLOUDS = registerShadow("SHADOW_CLOUDS", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_LINES = registerShadow("SHADOW_LINES", ProgramId.Shadow, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR_NORMAL, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_LEASH = registerShadow("SHADOW_LEASH", ProgramId.Shadow, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_LIGHTNING = registerShadow("SHADOW_LIGHTNING", ProgramId.Shadow, AlphaTests.OFF, DefaultVertexFormat.POSITION_COLOR, FogMode.OFF, LightingModel.FULLBRIGHT);
	public static ShaderKey SHADOW_PARTICLES = registerShadow("SHADOW_PARTICLES", ProgramId.Shadow, AlphaTests.ONE_TENTH_ALPHA, DefaultVertexFormat.PARTICLE, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_TEXT = registerShadow("SHADOW_TEXT", ProgramId.Shadow, AlphaTests.NON_ZERO_ALPHA, IrisVertexFormats.GLYPH, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_TEXT_BG = registerShadow("SHADOW_TEXT_BG", ProgramId.Shadow, AlphaTests.NON_ZERO_ALPHA, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, FogMode.OFF, LightingModel.LIGHTMAP);
	public static ShaderKey SHADOW_TEXT_INTENSITY = registerShadow("SHADOW_TEXT_INTENSITY", ProgramId.Shadow, AlphaTests.NON_ZERO_ALPHA, IrisVertexFormats.GLYPH, FogMode.OFF, LightingModel.LIGHTMAP);

	public static ShaderKey register(String name, ProgramId program, AlphaTest alphaTest, VertexFormat vertexFormat, FogMode fogMode, LightingModel lightingModel) {
		ShaderKey key = new ShaderKey(name, program, alphaTest, vertexFormat, fogMode, lightingModel);
		SHADERS.add(key);
		return key;
	}

	public static ShaderKey registerShadow(String name, ProgramId program, AlphaTest alphaTest, VertexFormat vertexFormat, FogMode fogMode, LightingModel lightingModel) {
		ShaderKey key = new ShaderKey(name, program, alphaTest, vertexFormat, fogMode, lightingModel);
		SHADOWS.add(key);
		return key;
	}

	public static List<ShaderKey> getAll() {
		VALUES = new ArrayList<>(SHADERS);
		VALUES.addAll(SHADOWS);
		return VALUES;
	}

	public ProgramId getProgram() {
		return program;
	}

	public AlphaTest getAlphaTest() {
		return alphaTest;
	}

	public VertexFormat getVertexFormat() {
		return vertexFormat;
	}

	public FogMode getFogMode() {
		return fogMode;
	}

	public boolean isIntensity() {
		return this == TEXT_INTENSITY || this == TEXT_INTENSITY_BE || this == SHADOW_TEXT_INTENSITY;
	}

	public String getName() {
		return name.toLowerCase(Locale.ROOT);
	}

	public boolean isShadow() {
		return this.getProgram() == ProgramId.Shadow;
	}

	public boolean hasDiffuseLighting() {
		return lightingModel == LightingModel.DIFFUSE || lightingModel == LightingModel.DIFFUSE_LM;
	}

	public boolean shouldIgnoreLightmap() {
		return lightingModel == LightingModel.FULLBRIGHT || lightingModel == LightingModel.DIFFUSE;
	}

	public boolean isGlint() {
		return this == GLINT;
	}

	public boolean isText() {
		return this.name().contains("TEXT");
	}

	public enum LightingModel {
		FULLBRIGHT,
		LIGHTMAP,
		DIFFUSE,
		DIFFUSE_LM
	}
}
