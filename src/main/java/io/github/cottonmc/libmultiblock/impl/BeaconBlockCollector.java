package io.github.cottonmc.libmultiblock.impl;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;

public interface BeaconBlockCollector {
	Object2IntMap<Block> getBeaconBlocks();
}
