package io.github.cottonmc.libmultiblock.mixin;

import io.github.cottonmc.libmultiblock.impl.BeaconBlockCollector;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity extends BlockEntity implements BeaconBlockCollector {
	private Object2IntMap<Block> beaconBlocks = new Object2IntArrayMap<>();
	private static final Tag<Block> BEACON_BASE = TagRegistry.block(new Identifier("libmultiblock", "beacon_base_usable"));

	public MixinBeaconBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Inject(method = "updateLevel", at = @At("HEAD"))
	public void clearBlockMap(int x, int y, int z, CallbackInfo ci) {
		beaconBlocks.clear();
	}

	@ModifyArg(method = "updateLevel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
	public Block getNewBlock(Block original) {
		if (BEACON_BASE.contains(original)) {
			int currentCount = beaconBlocks.getOrDefault(original, 0);
			beaconBlocks.put(original, currentCount + 1);
			return Blocks.IRON_BLOCK;
		}
		return original;
	}

	@Override
	public Object2IntMap<Block> getBeaconBlocks() {
		return beaconBlocks;
	}
}
