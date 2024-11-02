package net.irisshaders.iris.pipeline.programs;

import net.minecraft.client.renderer.ShaderInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A specialized map mapping {@link ShaderKey} to {@link ShaderInstance}.
 * Avoids much of the complexity / overhead of an EnumMap while ultimately
 * fulfilling the same function.
 */
public class ShaderMap {
	private final List<ShaderInstance> shaders;

	public ShaderMap(Function<ShaderKey, ShaderInstance> factory) {
		List<ShaderKey> ids = ShaderKey.getAll();

		this.shaders = new ArrayList<>();

        for (ShaderKey id : ids) {
            this.shaders.add(factory.apply(id));
        }
	}

	public ShaderInstance getShader(ShaderKey id) {
		return shaders.get(ShaderKey.getAll().indexOf(id));
	}
}
