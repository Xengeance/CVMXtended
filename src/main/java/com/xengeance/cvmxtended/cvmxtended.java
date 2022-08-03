package com.xengeance.cvmxtended;

//forge/minecraft/java references
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//crayfish references
import com.mrcrayfish.vehicle.proxy.Proxy;
import com.mrcrayfish.vehicle.proxy.ServerProxy;
//mod references
import com.xengeance.cvmxtended.network.*;
import com.xengeance.cvmxtended.proxy.ClientProxy;
import com.xengeance.cvmxtended.init.ModVehicleProperties;
import com.xengeance.cvmxtended.init.ModEntities;
import com.xengeance.cvmxtended.init.ModRecipeSerializers;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cvmxtended")
public class cvmxtended
{
    // Directly reference a log4j logger.

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
        //ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        //MinecraftForge.EVENT_BUS.register(new CommonEvents());
        
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        //CustomDataSerializers.register();
        //HeldVehicleDataHandler.register();
        ModVehicleProperties.register();
        //ItemLookup.init();
        //ModDataKeys.register();
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        PROXY.setupClient();
    }

}
