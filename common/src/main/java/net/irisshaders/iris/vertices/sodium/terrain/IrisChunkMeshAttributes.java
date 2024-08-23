package net.irisshaders.iris.vertices.sodium.terrain;


import org.embeddedt.embeddium.impl.gl.attribute.GlVertexAttributeFormat;
import org.embeddedt.embeddium.impl.render.chunk.vertex.format.ChunkMeshAttribute;

public class IrisChunkMeshAttributes {
	public static final ChunkMeshAttribute MID_TEX_COORD = new ChunkMeshAttribute("midTexCoord", GlVertexAttributeFormat.UNSIGNED_SHORT, 2, false, false);
	public static final ChunkMeshAttribute TANGENT = new ChunkMeshAttribute("TANGENT", GlVertexAttributeFormat.BYTE, 4, true, false);
	public static final ChunkMeshAttribute NORMAL = new ChunkMeshAttribute("NORMAL", GlVertexAttributeFormat.BYTE, 3, true, false);
	public static final ChunkMeshAttribute BLOCK_ID = new ChunkMeshAttribute("BLOCK_ID", GlVertexAttributeFormat.SHORT, 2, false, false);
	public static final ChunkMeshAttribute MID_BLOCK = new ChunkMeshAttribute("MID_BLOCK", GlVertexAttributeFormat.BYTE, 4, false, false);
}
