package com.xengeance.cvmxtended.proxy;

import com.mrcrayfish.vehicle.client.render.*;


import java.util.concurrent.CompletableFuture;

import org.lwjgl.glfw.GLFW;

import com.mrcrayfish.vehicle.client.EntityRayTracer;
import com.mrcrayfish.vehicle.client.SpecialModels;
import com.mrcrayfish.vehicle.client.render.tileentity.FluidExtractorRenderer;
import com.mrcrayfish.vehicle.client.render.tileentity.FuelDrumRenderer;
import com.mrcrayfish.vehicle.client.render.tileentity.GasPumpRenderer;
import com.mrcrayfish.vehicle.client.render.tileentity.GasPumpTankRenderer;
import com.mrcrayfish.vehicle.client.render.tileentity.VehicleCrateRenderer;
import com.mrcrayfish.vehicle.client.render.vehicle.*;
import com.mrcrayfish.vehicle.entity.VehicleEntity;
import com.mrcrayfish.vehicle.proxy.Proxy;
import com.mrcrayfish.vehicle.util.FluidUtils;
import com.xengeance.cvmxtended.client.ClientEvents;
import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.client.render.vehicle.*;
import com.xengeance.cvmxtended.entity.BoxTruckEntity;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.entity.SemiTruckEntity;
import com.xengeance.cvmxtended.init.ModEntities;
import com.xengeance.cvmxtended.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy implements Proxy {
	
    public static final KeyBinding KEY_ACTIVATE = new KeyBinding("key.ctmxtended.activate", GLFW.GLFW_KEY_INSERT, "key.categories.ctmxtended");
    public static final KeyBinding KEY_RAISE_IMPLEMENT = new KeyBinding("key.ctmxtended.raise_implement", GLFW.GLFW_KEY_HOME, "key.categories.ctmxtended");
    public static final KeyBinding KEY_LOWER_IMPLEMENT = new KeyBinding("key.ctmxtended.lower_implement", GLFW.GLFW_KEY_END, "key.categories.ctmxtended");
	
    
	public void setupClient(){
		this.registerEntityRenders();
        this.registerRayTraceConstructors();
		this.registerKeyBindings();
		
		IResourceManager rm = Minecraft.getInstance().getResourceManager();
        if(rm instanceof IReloadableResourceManager)
        {
            ((IReloadableResourceManager) rm).addReloadListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) 
            		-> CompletableFuture.runAsync(() -> 
            			{
			                FluidUtils.clearCacheFluidColor();
			                EntityRayTracer.instance().clearDataForReregistration();
			            },
            		backgroundExecutor).thenCompose(stage::markCompleteAwaitingOthers).whenComplete((aVoid, throwable) -> SpecialModelsDefs.clearModelCache()));
        }
        

        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        
	}
	
	public void registerEntityRenders() 
	{
		/* Register Vehicles */
        this.registerVehicleRender(ModEntities.BoxTruck.get(), new RenderLandVehicleWrapper<BoxTruckEntity, RenderBoxTruck>(new RenderBoxTruck()));
        this.registerVehicleRender(ModEntities.SemiTruck.get(), new RenderLandVehicleWrapper<SemiTruckEntity, RenderSemiTruck>(new RenderSemiTruck()));     
        this.registerVehicleRender(ModEntities.Loader.get(), new RenderLandVehicleWrapper<LoaderEntity, RenderLoader>(new RenderLoader()));
        
    }

    private void registerKeyBindings()
    {
        ClientRegistry.registerKeyBinding(KEY_ACTIVATE);
        ClientRegistry.registerKeyBinding(KEY_RAISE_IMPLEMENT);
        ClientRegistry.registerKeyBinding(KEY_LOWER_IMPLEMENT);
    }

    private <T extends VehicleEntity & EntityRayTracer.IEntityRayTraceable, R extends AbstractRenderVehicle<T>> void registerVehicleRender(EntityType<T> type, RenderVehicleWrapper<T, R> wrapper)
    {
        RenderingRegistry.registerEntityRenderingHandler(type, manager -> new RenderEntityVehicle<>(manager, wrapper));
        VehicleRenderRegistry.registerRenderWrapper(type, wrapper);
    }
    
    private void registerRayTraceConstructors()
    {

        /* Box Truck */
        EntityRayTracer.instance().registerTransforms(ModEntities.BoxTruck.get(), (tracer, transforms, parts) ->
        {
            EntityRayTracer.createTransformListForPart(SpecialModelsDefs.BOX_TRUCK_BODY, parts, transforms);
            EntityRayTracer.createTransformListForPart(SpecialModelsDefs.CARGO_DOOR, parts, transforms);
            EntityRayTracer.createTransformListForPart(SpecialModels.GO_KART_STEERING_WHEEL, parts, transforms,
                EntityRayTracer.MatrixTransformation.createTranslation(0.24F, 0.32F, 0.87F),
                EntityRayTracer.MatrixTransformation.createRotation(Axis.POSITIVE_X, -45F),
                EntityRayTracer.MatrixTransformation.createScale(0.65F));
            EntityRayTracer.createFuelPartTransforms(ModEntities.BoxTruck.get(), SpecialModels.FUEL_DOOR_CLOSED, parts, transforms);
            //EntityRayTracer.createKeyPortTransforms(XModEntities.BoxTruck.get(), parts, transforms);
        });
        
        /* Semi Truck */
        EntityRayTracer.instance().registerTransforms(ModEntities.SemiTruck.get(), (tracer, transforms, parts) ->
        {
            EntityRayTracer.createTransformListForPart(SpecialModelsDefs.SEMI_TRUCK_BODY, parts, transforms);
            EntityRayTracer.createTransformListForPart(SpecialModels.GO_KART_STEERING_WHEEL, parts, transforms,
                EntityRayTracer.MatrixTransformation.createTranslation(0.24F, 0.32F, 0.87F),
                EntityRayTracer.MatrixTransformation.createRotation(Axis.POSITIVE_X, -45F),
                EntityRayTracer.MatrixTransformation.createScale(0.65F));
            EntityRayTracer.createFuelPartTransforms(ModEntities.SemiTruck.get(), SpecialModels.FUEL_DOOR_CLOSED, parts, transforms);
            //EntityRayTracer.createKeyPortTransforms(XModEntities.BoxTruck.get(), parts, transforms);
        });
        
        /* Loader */
        EntityRayTracer.instance().registerTransforms(ModEntities.Loader.get(), (tracer, transforms, parts) ->
        {
            EntityRayTracer.createTransformListForPart(SpecialModelsDefs.LOADER_BODY, parts, transforms);
            EntityRayTracer.createTransformListForPart(SpecialModels.GO_KART_STEERING_WHEEL, parts, transforms,
                EntityRayTracer.MatrixTransformation.createTranslation(0.24F, 0.32F, 0.87F),
                EntityRayTracer.MatrixTransformation.createRotation(Axis.POSITIVE_X, -45F),
                EntityRayTracer.MatrixTransformation.createScale(0.65F));
            EntityRayTracer.createFuelPartTransforms(ModEntities.Loader.get(), SpecialModels.FUEL_DOOR_CLOSED, parts, transforms);
            //EntityRayTracer.createKeyPortTransforms(XModEntities.BoxTruck.get(), parts, transforms);
        });
    }
}
