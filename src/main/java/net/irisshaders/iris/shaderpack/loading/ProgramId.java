package net.irisshaders.iris.shaderpack.loading;

import net.irisshaders.MekShaders;
import net.irisshaders.iris.gl.blending.BlendMode;
import net.irisshaders.iris.gl.blending.BlendModeFunction;
import net.irisshaders.iris.gl.blending.BlendModeOverride;
import net.irisshaders.iris.shaderpack.programs.ProgramSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record ProgramId(ProgramGroup group, String name, ProgramId fallback, BlendModeOverride defaultBlendOverride, ProgramSourceLocator locator) {
	private static final List<ProgramId> VALUES = new ArrayList<>();

	public static ProgramId Shadow = register(ProgramGroup.Shadow, "", ProgramSet::getShadow);
	public static ProgramId ShadowSolid = register(ProgramGroup.Shadow, "solid", Shadow, ProgramSourceLocator.EMPTY);
	public static ProgramId ShadowCutout = register(ProgramGroup.Shadow, "cutout", Shadow, ProgramSourceLocator.EMPTY);

	public static ProgramId Basic = register(ProgramGroup.Gbuffers, "basic", ProgramSet::getGbuffersBasic);
	public static ProgramId Line = register(ProgramGroup.Gbuffers, "line", Basic, set -> set.gbuffersLine.requireValid());

	public static ProgramId Textured = register(ProgramGroup.Gbuffers, "textured", Basic, ProgramSet::getGbuffersTextured);
	public static ProgramId TexturedLit = register(ProgramGroup.Gbuffers, "textured_lit", Textured, ProgramSet::getGbuffersTexturedLit);
	public static ProgramId SkyBasic = register(ProgramGroup.Gbuffers, "skybasic", Basic, ProgramSet::getGbuffersSkyBasic);
	public static ProgramId SkyTextured = register(ProgramGroup.Gbuffers, "skytextured", Textured, ProgramSet::getGbuffersSkyTextured);
	public static ProgramId Clouds = register(ProgramGroup.Gbuffers, "clouds", Textured, ProgramSet::getGbuffersClouds);

	public static ProgramId Terrain = register(ProgramGroup.Gbuffers, "terrain", TexturedLit, ProgramSet::getGbuffersTerrain);
	public static ProgramId TerrainSolid = register(ProgramGroup.Gbuffers, "terrain_solid", Terrain, ProgramSet::getGbuffersTerrainSolid);
	public static ProgramId TerrainCutout = register(ProgramGroup.Gbuffers, "terrain_cutout", Terrain, ProgramSet::getGbuffersTerrainCutout);
	public static ProgramId DamagedBlock = register(ProgramGroup.Gbuffers, "damagedblock", Terrain, ProgramSet::getGbuffersDamagedBlock);

	public static ProgramId Block = register(ProgramGroup.Gbuffers, "block", Terrain, ProgramSet::getGbuffersBlock);
	public static ProgramId BlockTrans = register(ProgramGroup.Gbuffers, "block_translucent", Block, ProgramSet::getGbuffersBlockTrans);
	public static ProgramId BeaconBeam = register(ProgramGroup.Gbuffers, "beaconbeam", Textured, ProgramSet::getGbuffersBeaconBeam);
	public static ProgramId Item = register(ProgramGroup.Gbuffers, "item", TexturedLit, ProgramSourceLocator.EMPTY);

	public static ProgramId Entities = register(ProgramGroup.Gbuffers, "entities", TexturedLit, ProgramSet::getGbuffersEntities);
	public static ProgramId EntitiesTrans = register(ProgramGroup.Gbuffers, "entities_translucent", Entities, ProgramSet::getGbuffersEntitiesTrans);
	public static ProgramId Particles = register(ProgramGroup.Gbuffers, "particles", TexturedLit, ProgramSet::getGbuffersParticles);
	public static ProgramId ParticlesTrans = register(ProgramGroup.Gbuffers, "particles_translucent", Particles, ProgramSet::getGbuffersParticlesTrans);
	public static ProgramId EntitiesGlowing = register(ProgramGroup.Gbuffers, "entities_glowing", Entities, ProgramSet::getGbuffersEntitiesGlowing);
	public static ProgramId ArmorGlint = register(ProgramGroup.Gbuffers, "armor_glint", Textured, ProgramSet::getGbuffersGlint);
	public static ProgramId SpiderEyes = register(ProgramGroup.Gbuffers, "spidereyes", Textured,
		new BlendModeOverride(new BlendMode(BlendModeFunction.SRC_ALPHA.getGlId(), BlendModeFunction.ONE.getGlId(), BlendModeFunction.ZERO.getGlId(), BlendModeFunction.ONE.getGlId()))
			, ProgramSet::getGbuffersEntityEyes);

	public static ProgramId Hand = register(ProgramGroup.Gbuffers, "hand", TexturedLit, ProgramSet::getGbuffersHand);
	public static ProgramId Weather = register(ProgramGroup.Gbuffers, "weather", TexturedLit, ProgramSet::getGbuffersWeather);
	public static ProgramId Water = register(ProgramGroup.Gbuffers, "water", Terrain, ProgramSet::getGbuffersWater);
	public static ProgramId HandWater = register(ProgramGroup.Gbuffers, "hand_water", Hand, ProgramSet::getGbuffersHandWater);
	public static ProgramId DhTerrain = register(ProgramGroup.Dh, "terrain", TexturedLit, ProgramSet::getDhTerrain);
	public static ProgramId DhGeneric = register(ProgramGroup.Dh, "generic", DhTerrain, ProgramSourceLocator.EMPTY);
	public static ProgramId DhWater = register(ProgramGroup.Dh, "water", DhTerrain,ProgramSet::getDhWater);
	public static ProgramId DhShadow = register(ProgramGroup.Dh, "shadow", ProgramSet::getDhShadow);

	public static ProgramId Final = register(ProgramGroup.Final, "", ProgramSet::getCompositeFinal);

	static {
		MekShaders.init();
	}

	public static ProgramId register(ProgramGroup group, String name, ProgramSourceLocator locator) {
		return register(group, name, null, null, locator);
	}

	public static ProgramId register(ProgramGroup group, String name, ProgramId fallback, ProgramSourceLocator locator) {
		return register(group, name, fallback, null, locator);
	}

	public static ProgramId register(ProgramGroup group, String name, ProgramId fallback, BlendModeOverride defaultBlendOverride, ProgramSourceLocator locator) {
		ProgramId id = new ProgramId(group, name, fallback, defaultBlendOverride, locator);
		VALUES.add(id);
		return id;
	}

	public static List<ProgramId> getValues() {
		return VALUES;
	}

	public ProgramGroup getGroup() {
		return group;
	}

	public String getSourceName() {
		return name.isEmpty() ? group.getBaseName() : group.getBaseName() + "_" + name;
	}

	public Optional<ProgramId> getFallback() {
		return Optional.ofNullable(fallback);
	}

	public BlendModeOverride getBlendModeOverride() {
		return defaultBlendOverride;
	}
}
