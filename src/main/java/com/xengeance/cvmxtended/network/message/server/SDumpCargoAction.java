package com.xengeance.cvmxtended.network.message.server;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.mrcrayfish.vehicle.common.inventory.StorageInventory;
import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.util.InventoryUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/*
 * Utility packet to tell the server the client want's to drop all the inventory on their vehicle
 */
public class SDumpCargoAction implements IMessage<SDumpCargoAction>{

	private int entityId;
	
	public SDumpCargoAction() {}
	
	public SDumpCargoAction(int id) {
		entityId = id;
	}
	
	@Override
	public SDumpCargoAction decode(PacketBuffer buffer) {
		int eid = buffer.readInt();
		
		SDumpCargoAction msg = new SDumpCargoAction(eid);
		return msg;
	}
	
	@Override
	public void encode(SDumpCargoAction message, PacketBuffer buffer) {
		buffer.writeInt(entityId);
	}

	@Override
	public void handle(SDumpCargoAction message, Supplier<Context> supplier) {
		supplier.get().enqueueWork(() -> {
			
			Entity e = (LoaderEntity) supplier.get().getSender().getRidingEntity();
			
			if(e instanceof LoaderEntity) {
				LoaderEntity entity = (LoaderEntity) e;
				
				StorageInventory inventory = entity.getInventory();
				Vector3d bucketPos = entity.getBucketPos();
				InventoryUtil.dropInventoryItems(e.world, bucketPos, inventory);
			}
		});
	}

}
