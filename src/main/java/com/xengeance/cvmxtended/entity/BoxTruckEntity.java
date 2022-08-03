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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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

/**
 * Author: MrCrayfish
 */
public class BoxTruckEntity extends LandVehicleEntity implements IStorage
{
	//private static final EntityRayTracer.RayTracePart CARGO_DOOR = new EntityRayTracer.RayTracePart(createScaledBoundingBox(-6 * 0.0625, 4.2 * 0.0625, 9 * 0.0625, 6 * 0.0625, 8.3 * 0.0625F, 17 * 0.0625, 1.1));
	private static final EntityRayTracer.RayTracePart CARGO_DOOR = new EntityRayTracer.RayTracePart(new AxisAlignedBB(-0.5, 0.3125, -2.4375, 0.5, 1.375, -2.5375));
	@SuppressWarnings("deprecation")
	private static final Map<EntityRayTracer.RayTracePart, EntityRayTracer.TriangleRayTraceList> interactionBoxMapStatic = DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> {
        Map<EntityRayTracer.RayTracePart, EntityRayTracer.TriangleRayTraceList> map = new HashMap<>();
        map.put(CARGO_DOOR, EntityRayTracer.boxToTriangles(CARGO_DOOR.getBox(), null));
        //map.put(CHEST_BOX, EntityRayTracer.boxToTriangles(CHEST_BOX.getBox(), null));
        return map;
    });
	
    private StorageInventory inventory;
	
    public BoxTruckEntity(EntityType<? extends BoxTruckEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.setMaxSpeed(6);
        this.setTurnSensitivity(3);
        this.initInventory();
        
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
        return new Vector3d(-0.5312, 1.1406, -2.5);
    }

    @Override
    public boolean canTowTrailer()
    {
        return false;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }

    @Override
    public boolean canBeColored()
    {
        return false;
    }

	@Override
	public StorageInventory getInventory() {
        return this.inventory;
	}
	

    @Override
    @OnlyIn(Dist.CLIENT)
    public Map<EntityRayTracer.RayTracePart, EntityRayTracer.TriangleRayTraceList> getStaticInteractionBoxMap()
    {
        return interactionBoxMapStatic;
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        this.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, 0.9F);
    }

    @Override
    public ITextComponent getStorageName()
    {
        return this.getDisplayName();
    }
    


    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        if(compound.contains("Inventory", Constants.NBT.TAG_LIST))
        {
            this.initInventory();
            InventoryUtil.readInventoryToNBT(compound, "Inventory", inventory);
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        if(this.inventory != null)
        {
            InventoryUtil.writeInventoryToNBT(compound, "Inventory", inventory);
        }
    }

    private void initInventory()
    {
        StorageInventory original = this.inventory;
        this.inventory = new StorageInventory(this, 54);
        // Copies the inventory if it exists already over to the new instance
        if(original != null)
        {
            for(int i = 0; i < original.getSizeInventory(); i++)
            {
                ItemStack stack = original.getStackInSlot(i);
                if(!stack.isEmpty())
                {
                    this.inventory.setInventorySlotContents(i, stack.copy());
                }
            }
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean processHit(EntityRayTracer.RayTraceResultRotated result, boolean rightClick)
    {
        if(rightClick)
        {
            if(result.getPartHit().getModel() == SpecialModelsDefs.CARGO_DOOR)
            {
                PacketHandler.instance.sendToServer(new MessageOpenStorage(this.getEntityId()));
                Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
                return true;
            }
        }
        return super.processHit(result, rightClick);
    }
    /*
    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public List<EntityRayTracer.RayTracePart> getApplicableInteractionBoxes()
    {
        return ImmutableList.of(CARGO_DOOR);
    }*/
}
