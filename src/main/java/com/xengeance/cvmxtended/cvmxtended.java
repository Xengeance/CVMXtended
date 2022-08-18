package com.xengeance.cvmxtended;

import net.minecraftforge.common.MinecraftForge;
//forge/minecraft/java references
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//crayfish references
import com.mrcrayfish.vehicle.proxy.Proxy;
import com.mrcrayfish.vehicle.proxy.ServerProxy;
//mod references
import com.xengeance.cvmxtended.network.*;
import com.xengeance.cvmxtended.proxy.ClientProxy;
import com.xengeance.cvmxtended.init.ModVehicleProperties;
import com.xengeance.cvmxtended.client.ClientEvents;
import com.xengeance.cvmxtended.init.ModEntities;
import com.xengeance.cvmxtended.init.ModRecipeSerializers;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cvmxtended")
public class cvmxtended
{
    // Directly reference a log4j logger.

    @SuppressWarnings("deprecation")
	public static final Proxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    /*public static final ItemGroup CREATIVE_TAB = new ItemGroup("tabVehicle")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.WOOD_SMALL_ENGINE.get());
        }

	    @Override
	    public void fill(NonNullList<ItemStack> items)
	    {
            //super.fill(items);

            // Fill all jerry cans to their capacity
            /*items.forEach(stack ->
            {
                if(stack.getItem() instanceof JerryCanItem)
                {
                    JerryCanItem jerryCan = (JerryCanItem) stack.getItem();
                    jerryCan.fill(stack, jerryCan.getCapacity(stack));
                }
                else if(stack.getItem() instanceof SprayCanItem)
                {
                    SprayCanItem sprayCan = (SprayCanItem) stack.getItem();
                    sprayCan.refill(stack);
                }
            });*/

        /*public void RegisterCrates()
        {
            // Adds vehicle creates the the creative inventory
            BlockVehicleCrate.REGISTERED_CRATES.forEach(resourceLocation ->
            {
                CompoundNBT blockEntityTag = new CompoundNBT();
                blockEntityTag.putString("Vehicle", resourceLocation.toString());
                blockEntityTag.putInt("EngineTier", EngineTier.WOOD.ordinal());
                blockEntityTag.putInt("WheelType", WheelType.STANDARD.ordinal());
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.put("BlockEntityTag", blockEntityTag);
                ItemStack stack = new ItemStack(ModBlocks.VEHICLE_CRATE.get());
                stack.setTag(itemTag);
                VehicleMod.items.add(stack);
            });
        }
    };*/
    		
    public cvmxtended() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);
        
        //TODO: Implement config settings
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        eventBus.register(Config.class);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        
        
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        ModVehicleProperties.register();
        cvmxtended.LOGGER.log(Level.INFO, "COMMON Setup Complete!");
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        PROXY.setupClient();
        cvmxtended.LOGGER.log(Level.INFO, "CLIENT Setup Complete!");
    }

}
