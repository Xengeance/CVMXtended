package com.xengeance.cvmxtended.network.message.client;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/*
 *  Packet that transfers the generated BlockPos array to cache in the target vehicle for debug rendering
 */
public class CSubmitScanVectorBlockData implements IMessage<CSubmitScanVectorBlockData>{
		private List<BlockPos> blocksPosCache;
		private int vehicleId;
		
		
		public CSubmitScanVectorBlockData() {}
		
		public CSubmitScanVectorBlockData(List<BlockPos> cache, int vId){
			blocksPosCache = cache;
			vehicleId = vId;
		}
		
		@Override
		public CSubmitScanVectorBlockData decode(PacketBuffer buffer) {
			int vehID = buffer.readInt();
			int size = buffer.readInt();
			List<BlockPos> blocks = Lists.newArrayList();
			for(int i = 0; i < size; i++) {
				blocks.add(buffer.readBlockPos());
			}
			return new CSubmitScanVectorBlockData(blocks, vehID);
		}

		@Override
		public void encode(CSubmitScanVectorBlockData message, PacketBuffer buffer) {
			
			buffer.writeInt(message.vehicleId);
			buffer.writeInt(message.blocksPosCache.size());
			message.blocksPosCache.forEach(block -> {
				buffer.writeBlockPos(block);
			});
		}
		
		@Override
		public void handle(CSubmitScanVectorBlockData message, Supplier<Context> ctx) {
			ctx.get().enqueueWork(() ->{
				
				World world  = Minecraft.getInstance().player.world;
				Entity e  = world.getEntityByID(message.vehicleId);
				if(e != null && e instanceof LoaderEntity) {
					LoaderEntity loader  = (LoaderEntity) e;
					loader.SetDBBlockCache(message.blocksPosCache);
				}
			});
			ctx.get().setPacketHandled(true);
		}
}
