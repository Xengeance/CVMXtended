package com.xengeance.cvmxtended.client;

import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import com.xengeance.cvmxtended.cvmxtended;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.network.PacketHandler;
import com.xengeance.cvmxtended.network.PacketEnums.ACTIVATE_TYPE;
import com.xengeance.cvmxtended.network.message.server.SImplementKeybindAction;
import com.xengeance.cvmxtended.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

public class ClientEvents {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
		
    	if(ClientProxy.KEY_ACTIVATE.isKeyDown()) 
    	{
    		handleActivateKeybind();
    	}
    	
    	if(ClientProxy.KEY_RAISE_IMPLEMENT.isKeyDown()) 
    	{
    		handleImplementKeybind(ACTIVATE_TYPE.RAISE);
    	}
    	else if (ClientProxy.KEY_LOWER_IMPLEMENT.isKeyDown()) 
    	{
    		handleImplementKeybind(ACTIVATE_TYPE.LOWER);
    	}
    }

	private void handleActivateKeybind() {
		PlayerEntity player = Minecraft.getInstance().player;
		if(player != null || player.getRidingEntity() instanceof LoaderEntity) {
			LoaderEntity loader = (LoaderEntity) player.getRidingEntity();
				loader.doActivate();
		}
		else 
			return;
	}
    
    private void handleImplementKeybind(ACTIVATE_TYPE type) {
    	PlayerEntity player = Minecraft.getInstance().player;
		
		if(player == null || player.getRidingEntity() == null || !(player.getRidingEntity() instanceof LoaderEntity) || type == null) {
			return;
		}
		LoaderEntity loader = (LoaderEntity) player.getRidingEntity();

		//send a packet all listening clients to also execute the implement event 
		if(Minecraft.getInstance().world.isRemote)
			PacketHandler.instance.sendToServer(new SImplementKeybindAction(player.getEntityId(), loader.getEntityId(), type));	
		
    }
    
}
