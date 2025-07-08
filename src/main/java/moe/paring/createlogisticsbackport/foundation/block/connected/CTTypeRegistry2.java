package moe.paring.createlogisticsbackport.foundation.block.connected;

import com.simibubi.create.foundation.block.connected.CTType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CTTypeRegistry2 {
	private static final Map<ResourceLocation, CTType2> TYPES = new HashMap<>();

	public static void register(CTType2 type) {
		ResourceLocation id = type.getId();
		if (TYPES.containsKey(id))
			throw new IllegalArgumentException("Tried to override CTType registration for id '" + id + "'. This is not supported!");
		TYPES.put(id, type);
	}

	@Nullable
	public static CTType2 get(ResourceLocation id) {
		return TYPES.get(id);
	}
}
