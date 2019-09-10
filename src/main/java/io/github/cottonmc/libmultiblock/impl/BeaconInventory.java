package io.github.cottonmc.libmultiblock.impl;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BeaconInventory extends BasicInventory {
	public static final Tag<Item> BEACON_INGREDIENT = TagRegistry.item(new Identifier("libmultiblock", "beacon_ingredients"));

	public BeaconInventory(int size) {
		super(size);
	}

	public boolean isValidInvStack(int slot, ItemStack stack) {
		return BEACON_INGREDIENT.contains(stack.getItem());
	}

	public int getInvMaxStackAmount() {
		return 1;
	}
}
