package net.irisshaders.iris.compat.embeddium.mixin.vertex_format;

import org.embeddedt.embeddium.impl.render.chunk.vertex.format.ChunkMeshAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMeshAttribute.class)
public interface ChunkMeshAttributeAccessor {
	@Invoker(value = "<init>")
	static ChunkMeshAttribute createChunkMeshAttribute(String name, int ordinal) {
		throw new AssertionError();
	}
}
