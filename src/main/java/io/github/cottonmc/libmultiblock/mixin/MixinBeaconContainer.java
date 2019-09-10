package io.github.cottonmc.libmultiblock.mixin;

import io.github.cottonmc.libmultiblock.impl.BeaconInventory;
import net.minecraft.container.BeaconContainer;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.inventory.BasicInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import javax.annotation.Nullable;

@Mixin(BeaconContainer.class)
public abstract class MixinBeaconContainer extends Container {

	protected MixinBeaconContainer(@Nullable ContainerType<?> type, int syncId) {
		super(type, syncId);
	}

	@ModifyConstant(method = "<init>(ILnet/minecraft/inventory/Inventory;Lnet/minecraft/container/PropertyDelegate;Lnet/minecraft/container/BlockContext;)V")
	private BasicInventory injectNewInv(BasicInventory original) {
		return new BeaconInventory(1);
	}

}
