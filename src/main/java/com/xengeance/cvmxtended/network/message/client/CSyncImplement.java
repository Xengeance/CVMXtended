package com.xengeance.cvmxtended.network.message.client;

import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.network.PacketHandler;
import com.xengeance.cvmxtended.network.PacketEnums.ACTIVATE_TYPE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/*
 * Response packet of SImplementKeybindAction that syncs implement values and invokes update events
 *  of the entity for the client
 */
public class CSyncImplement implements IMessage<CSyncImplement>{
	private int playerId;
	private int vehicleId;
	private ACTIVATE_TYPE activationType;
	private int heightOffset;
	
	
	public CSyncImplement() {}
	
	public CSyncImplement(int controllingPassenger, int vId, ACTIVATE_TYPE activation, int height){
		playerId = controllingPassenger;
		activationType = activation;
		vehicleId = vId;
		heightOffset = height;
	}
	
	@Override
	public CSyncImplement decode(PacketBuffer buffer) {
		int id = buffer.readInt();
		int vehID = buffer.readInt();
		ACTIVATE_TYPE activation = buffer.readEnumValue(ACTIVATE_TYPE.class);
		int h = buffer.readInt();
		return new CSyncImplement(id, vehID, activation, h);
	}

	@Override
	public void encode(CSyncImplement message, PacketBuffer buffer) {
		
		buffer.writeInt(message.playerId);
		buffer.writeInt(message.vehicleId);
		buffer.writeEnumValue(message.activationType);
		buffer.writeInt(message.heightOffset);
		
	}
	
	@Override
	public void handle(CSyncImplement message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() ->{
			cvmxtended.LOGGER.log(Level.INFO, "Processing sync request for Implement.");
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(player != null) {
				World world  = player.world;
				LoaderEntity loader = (LoaderEntity) world.getEntityByID(message.vehicleId);
				if(loader != null && loader instanceof LoaderEntity) {
					boolean validTypeInvoked = false;
					if(message.activationType == ACTIVATE_TYPE.RAISE) {
						loader.doImplementRaise();
						validTypeInvoked = true;
					}else if (message.activationType == ACTIVATE_TYPE.LOWER) {
						loader.doImplementLower();
						validTypeInvoked = true;
					}
					//send sync packet to clients
					if(validTypeInvoked && message.playerId == player.getEntityId())
						NotifySourceControllingPassenger(player, loader, message);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	
	private void NotifySourceControllingPassenger(ClientPlayerEntity e, LoaderEntity loader, CSyncImplement message) {
		if(e != null) {
			if(loader.getControllingPassenger() == Minecraft.getInstance().player)
			e.sendMessage(new StringTextComponent(I18n.format(message.activationType == ACTIVATE_TYPE.LOWER 
					? "text.cvmxtended.chat.lower_implement" : "text.cvmxtended.chat.raise_implement", 
					message.heightOffset)), Util.DUMMY_UUID);
		}
	
	}
}
