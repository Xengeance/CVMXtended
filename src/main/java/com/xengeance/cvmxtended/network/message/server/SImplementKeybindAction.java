package com.xengeance.cvmxtended.network.message.server;

import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.network.PacketHandler;
import com.xengeance.cvmxtended.network.PacketEnums.ACTIVATE_TYPE;
import com.xengeance.cvmxtended.network.message.client.CSyncImplement;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/*
 * Packet that is sent to the server by a client when one of the implement keybind has been activated by a client 
 */
public class SImplementKeybindAction implements IMessage<SImplementKeybindAction>{
	private int playerId; //Packet sender (usually the driver
	private int vehicleId; //vehicle to run the handler for
	private ACTIVATE_TYPE activationType; //corresponds to the desired action of the relevant keybind
	
	
	public SImplementKeybindAction() {}
	
	public SImplementKeybindAction(int controllingPassenger, int vId, ACTIVATE_TYPE activation){
		playerId = controllingPassenger;
		activationType = activation;
		vehicleId = vId;
	}
	
	@Override
	public SImplementKeybindAction decode(PacketBuffer buffer) {
		int id = buffer.readInt();
		int vehID = buffer.readInt();
		ACTIVATE_TYPE activation = buffer.readEnumValue(ACTIVATE_TYPE.class);
		return new SImplementKeybindAction(id, vehID, activation);
	}

	@Override
	public void encode(SImplementKeybindAction message, PacketBuffer buffer) {
		
		buffer.writeInt(message.playerId);
		buffer.writeInt(message.vehicleId);
		buffer.writeEnumValue(message.activationType);
		
	}
	
	@Override
	public void handle(SImplementKeybindAction message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() ->{
			MinecraftServer server  = ServerLifecycleHooks.getCurrentServer();
			ServerPlayerEntity player = ctx.get().getSender();
			if(player != null) {
				World world  = player.world;
				LoaderEntity loader = (LoaderEntity) world.getEntityByID(message.vehicleId);
				if(loader != null) {
					boolean validTypeInvoked = false;
					if(message.activationType == ACTIVATE_TYPE.RAISE) {
						loader.setBucketHeightOffset(loader.getBucketHeightOffset() + 1);
						validTypeInvoked = true;
					}else if (message.activationType == ACTIVATE_TYPE.LOWER) {
						loader.setBucketHeightOffset(loader.getBucketHeightOffset() - 1);
						validTypeInvoked = true;
					}
					//send sync packet to clients
					if(validTypeInvoked)
						PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> loader), 
								new CSyncImplement(player.getEntityId(), loader.getEntityId(), message.activationType, loader.getBucketHeightOffset()));
				}else
					cvmxtended.LOGGER.log(Level.WARN, "Implement Update Request rejected: loader with ID {} returned as null", message.vehicleId);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
