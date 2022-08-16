package com.xengeance.cvmxtended.util;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.xengeance.cvmxtended.Config;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.network.PacketHandler;
import com.xengeance.cvmxtended.network.message.client.CSubmitScanVectorBlockData;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class BlockUtil {

	//Checks block agianst a list of forge tags and breaks it if it matches and then returns true.
	public static boolean BreakBlockWithTag(World worldIn, BlockPos pos, String query, @Nullable Entity source, @Nullable Logger logger) {
		Set<ResourceLocation> tags = worldIn.getBlockState(pos).getBlock().getTags();
		for (ResourceLocation rl : tags){
			if(query.equalsIgnoreCase(rl.toString())) {

				if(source != null && source instanceof PlayerEntity)
					worldIn.getBlockState(pos).getBlock().
						onBlockHarvested(worldIn, pos, worldIn.getBlockState(pos), (PlayerEntity) source);
				
				worldIn.destroyBlock(pos, true, source);
				return true;
			}
		}
		return false;
	}
	
	//Plots a point along a vector at fixed intervals directly in front of the vehicle to determine what blocks are to
	//be affected by it's front-mounted implement (i.e. the Loader's bucket)
	//also returns the list of block for use by the excavate method
	public static List<BlockPos> scanBlocks(int radius, List<BlockPos> blockScanCache, @Nonnull LoaderEntity vehicle) {
		BlockPos blockPos;
		if (blockScanCache == null)
			blockScanCache = Lists.newArrayList();
		
		List<BlockPos> dBScanCache = Lists.newArrayList();


		//this check prevents the recalculating of the slope offset if the heading (Yrot) has remain unchanged
		//since we can reuse the cached slope offset vector in the entity. Likely minimal performance gain
		//in most cases, but integrative processes tend to compound exponentially so may as well.
		Vector2f slopeOffsetVector = MathUtil.GetVectorOffset(1, vehicle.rotationYaw);
		
		for (int x = -radius; x < radius; x++) {
			for (int y = 0; y < 2; y++){ 
				double x0 = vehicle.getBucketPos().x + (slopeOffsetVector.x * (float)x);
				double z0 = vehicle.getBucketPos().z + (slopeOffsetVector.y * (float)x);
				Vector3d v = new Vector3d(x0, vehicle.getPosY() + vehicle.getBucketHeightOffset() + y, z0);
				blockPos = new BlockPos(v);
				Block b = vehicle.world.getBlockState(blockPos).getBlock();
				
				//dbScanCache will contain all possible blockPos to be used by the debug renderer
				dBScanCache.add(blockPos);
				//TODO: add check for filtering out fluid blocks
				
				//blockScanCache is selectively filtered to only contain valid positions to execute faster 
				//during block breaking and is used solely on the server thread.
				if(b.getBlock() != Blocks.AIR)
					blockScanCache.add(blockPos);
			}
		}
		//use the contents of dBScanCache to sync to client entities so they have an updated instance of the vehicle's
		//debug rendering block cache
		if(!dBScanCache.isEmpty()) {
			vehicle.setBlockScanCache(dBScanCache);
			PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
					new CSubmitScanVectorBlockData(dBScanCache, vehicle.getEntityId()));
		}
		
		return blockScanCache;
	}

	public static void BreakBlocks(int radius, List<BlockPos> blockScanCache, @Nonnull LoaderEntity vehicle) {
		
		//run a block scan to populate the blockPos we want to run excavation on
		List<BlockPos> cache = scanBlocks(radius, blockScanCache, vehicle);
		
		for(BlockPos checkPos : cache){

			Block block = vehicle.world.getBlockState(checkPos).getBlock();
			
			if(block != Blocks.AIR) 
			for (String tagString : Config.SERVER.LOADER_BREAK_COMPATIBLE_BLOCKS.get()) {
				String compareString = block.getRegistryName().toString();

				//performs an either-or check of the tag and resource location of the block
				//based on whitelist values in the server-side config
				if (tagString.equalsIgnoreCase(compareString)) {

					vehicle.world.destroyBlock(checkPos, true, vehicle);
				} else 
				{
					//TODO:Seperate config data so that strings for resource location
					//and forge tags are seperate data structures
					BlockUtil.BreakBlockWithTag(vehicle.world, checkPos, tagString, vehicle, cvmxtended.LOGGER);
				}
			}
		}
	}
}
