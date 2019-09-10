package io.github.cottonmc.libmultiblock.mixin;

import io.github.cottonmc.libmultiblock.impl.BeaconInventory;
import net.minecraft.container.BeaconContainer;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconContainer.SlotPayment.class)
public abstract class MixinBeaconContainerSlotPayment extends Slot {

	public MixinBeaconContainerSlotPayment(Inventory inv, int slotNum, int x, int y) {
		super(inv, slotNum, x, y);
	}

	@Inject(method = "canInsert", at = @At("HEAD"))
	private void taggableInsertSlot(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (BeaconInventory.BEACON_INGREDIENT.contains(stack.getItem())) cir.setReturnValue(true);
	}
}
