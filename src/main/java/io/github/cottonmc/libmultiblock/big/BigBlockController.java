package io.github.cottonmc.libmultiblock.big;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * The core block within a construct like a door or bed, which acts as one block that takes up multiple blockspaces.
 */
public abstract class BigBlockController extends Block {

	public BigBlockController(Settings settings) {
		super(settings);
	}

	/**
	 * @return The vectors from the controller to all of its companions, when facing north. Will be automatically rotated for all directions.
	 */
	public abstract Vec3i[] getCompanionOffsets();

	/**
	 * @param world The world the big block is in.
	 * @param controllerPos The position of the controller.
	 * @param controllerState The state of the controller.
	 * @param offsetToCompanion The vector from this controller to the companion, ignoring rotation (always for when the big block is facing north).
	 * @return The block state to put down.
	 */
	public abstract BlockState getCompanionState(World world, BlockPos controllerPos, BlockState controllerState, Vec3i offsetToCompanion);

	/**
	 * @return The property used to define this block's direction, or null.
	 */
	@Nullable
	abstract EnumProperty<Direction> getFacingProperty();

	/**
	 * @return The object defining properties for placed companions.
	 */
	public abstract BigBlockCompanion getCompanion();

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
		super.onBlockAdded(state, world, pos, newState, boolean_1);
		Direction dir = getFacingProperty() == null? Direction.NORTH : state.get(getFacingProperty());
		for (Vec3i vec : getCompanionOffsets()) {
			Vec3i offset = getRotatedVector(vec, dir);
			Vec3i opposite = new Vec3i(offset.getX() * -1, offset.getY() * -1, offset.getZ() * -1);
			BlockPos toAdd = pos.add(offset);
			world.setBlockState(toAdd, getCompanionState(world, pos, state, vec));
			getCompanion().configure(world, pos, state, opposite);
		}
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
		super.onBlockRemoved(state, world, pos, newState, boolean_1);
		Direction dir = getFacingProperty() == null? Direction.NORTH : state.get(getFacingProperty());
		for (Vec3i vec : getCompanionOffsets()) {
			Vec3i offset = getRotatedVector(vec, dir);
			BlockPos toRemove = pos.add(offset);
			world.breakBlock(toRemove, false);
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, ViewableWorld world, BlockPos pos) {
		Direction dir = getFacingProperty() == null? Direction.NORTH : state.get(getFacingProperty());
		for (Vec3i vec : getCompanionOffsets()) {
			Vec3i offset = getRotatedVector(vec, dir);
			BlockPos companion = pos.add(offset);
			if (!world.getBlockState(companion).getMaterial().isReplaceable()) return false;
		}
		return true;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	public static Vec3i getRotatedVector(Vec3i vec, Direction dir) {
		if (dir == Direction.NORTH) return vec;
		if (dir.getAxis() == Direction.Axis.Y) {
			//rotate around the X axis
			int y = vec.getY();
			int z = vec.getZ();
			int angle = dir == Direction.UP? 90 : 270;

			angle *= Math.PI/180.0; //Convert amount to radians

			double theta = Math.atan2(y, z);
			double r = Math.sqrt(z*z+y*y);

			z = (int)(r * Math.cos(theta-angle));
			y = (int)(r * Math.sin(theta-angle));

			return new Vec3i(vec.getX(), y, z);
		}
		else {
			//rotate around the Y axis
			int x = vec.getX();
			int z = vec.getZ();
			int angle = dir == Direction.EAST? 90 : dir == Direction.SOUTH? 180 : 270;

			angle *= Math.PI/180.0; //Convert amount to radians

			double theta = Math.atan2(x, z);
			double r = Math.sqrt(z*z+x*x);

			z = (int)(r * Math.cos(theta-angle));
			x = (int)(r * Math.sin(theta-angle));

			return new Vec3i(x, vec.getY(), z);
		}
	}
}