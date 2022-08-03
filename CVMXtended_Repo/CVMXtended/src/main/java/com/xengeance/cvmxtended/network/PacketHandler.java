package com.xengeance.cvmxtended.network;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;

import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.Reference;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.network.message.MessageCraftVehicle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler 
{

    public static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel instance;// = com.mrcrayfish.vehicle.network.PacketHandler.instance;
    private static int nextId = 0;
    
    public static void register()
    {
    	//instance = com.mrcrayfish.vehicle.network.PacketHandler.instance;
    	
    	instance = NetworkRegistry.ChannelBuilder
			    .named(new ResourceLocation(Reference.MOD_ID, "play"))
			    .networkProtocolVersion(() -> PROTOCOL_VERSION)
			    .clientAcceptedVersions(PROTOCOL_VERSION::equals)
			    .serverAcceptedVersions(PROTOCOL_VERSION::equals)
			    .simpleChannel();
    		
    	register(MessageCraftVehicle.class, new MessageCraftVehicle());
    }
    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
    	instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
