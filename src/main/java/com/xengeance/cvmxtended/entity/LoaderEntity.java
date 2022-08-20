package com.xengeance.cvmxtended.entity;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.vehicle.client.EntityRayTracer;
import com.mrcrayfish.vehicle.common.inventory.IStorage;
import com.mrcrayfish.vehicle.common.inventory.StorageInventory;
import com.mrcrayfish.vehicle.entity.EngineType;
import com.mrcrayfish.vehicle.entity.LandVehicleEntity;
import com.mrcrayfish.vehicle.init.ModSounds;
import com.mrcrayfish.vehicle.network.message.MessageOpenStorage;
import com.xengeance.cvmxtended.Config;
import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.network.PacketHandler;
import com.xengeance.cvmxtended.network.message.client.CSubmitScanVectorBlockData;
import com.xengeance.cvmxtended.network.message.client.CSyncImplement;
import com.xengeance.cvmxtended.network.message.server.SDumpCargoAction;
import com.xengeance.cvmxtended.util.BlockUtil;
import com.xengeance.cvmxtended.util.InventoryUtil;
import com.xengeance.cvmxtended.util.MathUtil;
import com.xengeance.cvmxtended.*;
import com.xengeance.cvmxtended.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class LoaderEntity extends LandVehicleEntity implements IStorage {

	private static final int pickUpScanCooldown = 10;
	private int inventoryTimer;
	private int blockScanTimer;
	private List<BlockPos> blockScanCache;
	private Vector2f slopeOffsetVector;
	private double prevScanY;
	private List<BlockPos> debugBlockScanCache;
	private int bucketHeightOffset; // the Y offset of the bucket relative to the loader's blockPosition
	private StorageInventory inventory; // inventory for the loader bucket
	
	

	//TODO: Consider integrating these into VehicleProperties
	// this is the offset of the bucket in block length from the center of the
	// vehicle
	static final int offsetDist = 3;
	// the width in blocks of the bucket from the center outwards
	static final int bucketRadius = 2;
	
	@OnlyIn(Dist.CLIENT)
	public void SetDBBlockCache(@Nonnull List<BlockPos> cache) {
		if(!cache.isEmpty())
			debugBlockScanCache = cache;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void setBlockScanCache(@Nonnull List<BlockPos> cache) {
		blockScanCache = cache;
	}
	
	public List<BlockPos> getDebugScanCache() {
		if(debugBlockScanCache == null) {

			//instantiate the block cache if it is null for some reason
			List<BlockPos> result = Lists.newArrayList();
			return  result;
		}
		return Lists.newArrayList(debugBlockScanCache);
	}
	
	
	
	public int getBucketHeightOffset() {return bucketHeightOffset;}
	public void setBucketHeightOffset(int value){
		//set the value
		bucketHeightOffset = value;
		
		//then clamp it
		if(bucketHeightOffset > 3)
			bucketHeightOffset = 3;
		else if (bucketHeightOffset < -1)
			bucketHeightOffset = -1;
	}


	public LoaderEntity(EntityType<? extends LoaderEntity> type, World worldIn) {
		super(type, worldIn);
		this.setMaxSpeed(4);
		this.setTurnSensitivity(3);
		this.initInventory();
		
		debugBlockScanCache = Lists.newArrayList();
		inventoryTimer = blockScanTimer = 0;
	}

	public void doActivate() {
		Vector3d bucket = getBucketPos();
		PacketHandler.instance.sendToServer(new SDumpCargoAction(this.getEntityId()));
	}
	
	public void doImplementRaise() {
		if (bucketHeightOffset == 3)
			return;
		
		//this.setBucketHeightOffset(bucketHeightOffset - 1);

		Minecraft.getInstance().player.playSound(SoundEvents.BLOCK_PISTON_EXTEND, 1, 1);
	}
	
	public void doImplementLower() {
		if (bucketHeightOffset == -1)
			return;

		//this.setBucketHeightOffset(bucketHeightOffset + 1);

		Minecraft.getInstance().player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 1, 1);
	}

	public Vector3d getBucketPos() {
		Vector3d bucketPos = this.getPositionVec().add(this.getLookVec().scale(offsetDist)); // world Position of the bucket
		return bucketPos;
	}


    @Override
    public boolean canBeColored()
    {
        return true;
    }
	@Override
	public boolean canTowTrailer() {
		return false;
	}
	
	@Override
	public boolean canMountTrailer() {
		return false;
	}
	
	@Override
	public void updateVehicle() {
		super.updateVehicle();

		inventoryTimer++;
		blockScanTimer++;

		World world = this.world;
		if (!this.world.isRemote && this.getControllingPassenger() != null) {
				
			//timer field is used to prevent the excavation code from executing every tick 
			if (blockScanTimer >= 3) {
				// reset the scan timer
				blockScanTimer = 0;
				
				//only break blocks when being driver and only on server thread
				BlockUtil.BreakBlocks(LoaderEntity.bucketRadius, blockScanCache, this);
			}
	
	
			Vector3d min = new Vector3d(-bucketRadius, 0, -bucketRadius);
			Vector3d max = new Vector3d(bucketRadius, 1, bucketRadius);
			AxisAlignedBB axisAligned = new AxisAlignedBB(getBucketPos().add(min), getBucketPos().add(max));
	
			// inventory time is used like blockScanTimer, but controls how often to scan for block entities
			// in front of the loader bucket
			if ( inventoryTimer >= pickUpScanCooldown) {
				// reset the scan timer
				inventoryTimer = 0;
	
				List<ItemEntity> items = world.getEntitiesWithinAABB(EntityType.ITEM, axisAligned,
						item -> (item.ticksExisted > 1) && (item.getThrowerId() == this.entityUniqueID | !item.cannotPickup()) && !item.getItem().isEmpty()
								&& !item.getPersistentData().contains("PreventRemoteMovement"));
				if (items != null && !items.isEmpty()) {
	
					// TODO: If performance ever becomes a significant issue due to this. Look into batching
					// iterations across multiple ticks
					items.forEach(item -> {
						this.addItemToLocalStorage(item);
					});
	
					inventory.markDirty();
				}
			}
		}
	}

	private void addItemToLocalStorage(ItemEntity item) {
		ItemStack stack = item.getItem();
		if (stack.isEmpty())
			return;

		// only attempt to pickup an items we can carry
		if (inventory != null && inventory.func_233541_b_(stack)) {
			// TODO: Add some dust/rubble particles
			this.playSound(SoundEvents.BLOCK_GRAVEL_HIT, 1, 1);

			// set the original ItemEntity to only hold the leftover stack size
			// if there was not enough space to contain the entire stack in inventory
			// or to empty if the stack was fully added/merged
			item.setItem(inventory.addItem(stack));
		}
	}

	@Override
	public EngineType getEngineType() {
		return EngineType.LARGE_MOTOR;
	}

	@Override
	public SoundEvent getMovingSound() {
		return ModSounds.TRACTOR_ENGINE_MONO.get();
	}

	@Override
	public SoundEvent getRidingSound() {
		return ModSounds.TRACTOR_ENGINE_STEREO.get();
	}

	@Override
	public float getMinEnginePitch() {
		return 0.8F;
	}

	@Override
	public float getMaxEnginePitch() {
		return 1.6F;
	}

	@Override
	public StorageInventory getInventory() {
		return inventory;
	}

	@Override
	public void openInventory(PlayerEntity player) {
		this.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, 0.9F);
	}

	@Override
	public ITextComponent getStorageName() {
		return this.getDisplayName();
	}

	@Override
	protected void onVehicleDestroyed(LivingEntity entity) {
		super.onVehicleDestroyed(entity);
		if (this.inventory != null) {
			InventoryUtil.dropInventoryItems(this.world, this.getPositionVec(), this.inventory);
		}
	}
	

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawInteractionBoxes(Tessellator tessellator, BufferBuilder buffer) {
		// NOTE: I figured it out Cray, take my code! -> LoaderEntity#onRender()
		// TODO figure this out
		// RenderGlobal.drawSelectionBoundingBox(CHEST_BOX.getBox(), 0, 1, 0, 0.4F);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("Inventory", Constants.NBT.TAG_LIST)) {
			this.initInventory();
			com.mrcrayfish.vehicle.util.InventoryUtil.readInventoryToNBT(compound, "Inventory", inventory);
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.inventory != null) {
			com.mrcrayfish.vehicle.util.InventoryUtil.writeInventoryToNBT(compound, "Inventory", inventory);
		}
	}

	private void initInventory() {
		StorageInventory original = this.inventory;
		this.inventory = new StorageInventory(this, 9);
		// Copies the inventory if it exists already over to the new instance
		if (original != null) {
			for (int i = 0; i < original.getSizeInventory(); i++) {
				ItemStack stack = original.getStackInSlot(i);
				if (!stack.isEmpty()) {
					this.inventory.setInventorySlotContents(i, stack.copy());
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean processHit(EntityRayTracer.RayTraceResultRotated result, boolean rightClick) {
		Entity driver = this.getControllingPassenger();
		PlayerEntity player = Minecraft.getInstance().player;
		if (rightClick) {
			//open the inventory when right clicking while riding vehicle.
			//TODO: may also damage vehicle with certain held items (like Tetra shield spikes) needs testing.
			if (result.getPartHit().getModel() == SpecialModelsDefs.LOADER_BODY && driver == player) {
				com.mrcrayfish.vehicle.network.PacketHandler.instance
						.sendToServer(new MessageOpenStorage(this.getEntityId()));
				Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
				return true;
			}
		}
		return super.processHit(result, rightClick);
	}

}
