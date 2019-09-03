package io.github.cottonmc.libmultiblock.big;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * An interface for companion blocks of a big block.
 */
public interface BigBlockCompanion {
	/**
	 * @param world The world the big block is in.
	 * @param controllerPos The position of the controller.
	 * @param controllerState The state of the controller.
	 * @param offsetToController The vector from this companion to the controller.
	 * @return The block state to put down.
	 */
	BlockState getCompanionState(World world, BlockPos controllerPos, BlockState controllerState, Vec3i offsetToController);

	/**
	 * Give the newly-set-up companion block information about itself, so that it can save it if needed.
	 * @param world The world the big block is in.
	 * @param controllerPos The position of the controller.
	 * @param controllerState The state of the controller.
	 * @param offsetToController The vector from this companion to the controller.
	 */
	void configure(World world, BlockPos controllerPos, BlockState controllerState, Vec3i offsetToController);

	/**
	 * @param world The world the big block is in.
	 * @param pos The position of this companion.
	 * @param state The state of this companion.
	 * @return The position of the controller this companion belongs to.
	 */
	BlockPos getControllerPosition(World world, BlockPos pos, BlockState state);
}
