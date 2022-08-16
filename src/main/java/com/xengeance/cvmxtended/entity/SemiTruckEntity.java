package com.xengeance.cvmxtended.entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableList;
import com.mrcrayfish.vehicle.client.EntityRayTracer;
import com.mrcrayfish.vehicle.common.inventory.IStorage;
import com.mrcrayfish.vehicle.common.inventory.StorageInventory;
import com.mrcrayfish.vehicle.entity.EngineType;
import com.mrcrayfish.vehicle.entity.LandVehicleEntity;
import com.mrcrayfish.vehicle.entity.PoweredVehicleEntity.FuelPortType;
import com.mrcrayfish.vehicle.init.ModSounds;
import com.mrcrayfish.vehicle.item.SprayCanItem;
import com.mrcrayfish.vehicle.network.PacketHandler;
import com.mrcrayfish.vehicle.network.message.MessageAttachTrailer;
import com.mrcrayfish.vehicle.network.message.MessageOpenStorage;
import com.mrcrayfish.vehicle.util.InventoryUtil;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.util.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkHooks;
public class SemiTruckEntity extends LandVehicleEntity
{
	
	
    public SemiTruckEntity(EntityType<? extends SemiTruckEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.setMaxSpeed(6);
        this.setTurnSensitivity(3);
        
        //cvmxtended.LOGGER.log(Level.INFO, this.getType().getRegistryName().toString());     
    }

/*
    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand)
    {
        ItemStack heldItem = player.getHeldItem(hand);
        if((heldItem.isEmpty() || !(heldItem.getItem() instanceof SprayCanItem)) && player instanceof ServerPlayerEntity)
        {
            NetworkHooks.openGui((ServerPlayerEntity) player, this.getInventory(), buffer -> buffer.writeVarInt(this.getEntityId()));
            return ActionResultType.SUCCESS;
        }
        return super.processInitialInteract(player, hand);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInteractionBoxes(Tessellator tessellator, BufferBuilder buffer)
    {
        //TODO figure this out
        //RenderGlobal.drawSelectionBoundingBox(CHEST_BOX.getBox(), 0, 1, 0, 0.4F);
    }
    
    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.TRACTOR_ENGINE_MONO.get();
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.TRACTOR_ENGINE_STEREO.get();
    }

    @Override
    public float getMinEnginePitch()
    {
        return 0.8F;
    }

    @Override
    public float getMaxEnginePitch()
    {
        return 1.6F;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.LARGE_MOTOR;
    }

    @Override
    public FuelPortType getFuelPortType()
    {
        return FuelPortType.DEFAULT;
    }

    @Override
    public boolean shouldRenderEngine()
    {
        return false;
    }

    @Override
    public boolean shouldShowEngineSmoke()
    {
        return true;
    }

    @Override
    public Vector3d getEngineSmokePosition()
    {
    	float scale = 2.6f;
        return MathUtil.PixelToBlockCoords(4.65f * scale, 22.95f * scale, 3.5f * scale);
    }

    @Override
    public boolean canTowTrailer()
    {
        return true;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }
    
    @Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
    }
    

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
	}
}
