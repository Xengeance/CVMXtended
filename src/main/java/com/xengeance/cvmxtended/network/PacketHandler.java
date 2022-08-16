package com.xengeance.cvmxtended.network;


import com.mrcrayfish.vehicle.network.message.IMessage;
import com.xengeance.cvmxtended.Reference;
import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.network.message.client.CSubmitScanVectorBlockData;
import com.xengeance.cvmxtended.network.message.client.CSyncImplement;
import com.xengeance.cvmxtended.network.message.server.SDumpCargoAction;
import com.xengeance.cvmxtended.network.message.server.SImplementKeybindAction;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler 
{

    public static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel instance;// = com.mrcrayfish.vehicle.network.PacketHandler.instance;
    private static int nextId = 0;
    
    public static void register()
    {
    	
    	instance = NetworkRegistry.newSimpleChannel(
			    new ResourceLocation(Reference.MOD_ID, "play"),
			    () -> PROTOCOL_VERSION,
			    PROTOCOL_VERSION::equals,
			    PROTOCOL_VERSION::equals);
    		
    	register(SDumpCargoAction.class, new SDumpCargoAction());
    	register(SImplementKeybindAction.class, new SImplementKeybindAction());
    	register(CSyncImplement.class, new CSyncImplement());
    	register(CSubmitScanVectorBlockData.class, new CSubmitScanVectorBlockData());
    }
    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
    	/*instance.messageBuilder(clazz.class,  nextId++, NetworkDirection.PLAY_TO_SERVER)
    		.encoder(clazz::encode).
    		decoder(clazz::new)
    		.consumer(clazz::handle).add();*/
    	instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}
