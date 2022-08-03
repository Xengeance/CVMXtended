package com.xengeance.cvmxtended.proxy;

import com.mrcrayfish.vehicle.client.render.*;
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

import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.client.render.vehicle.*;
import com.xengeance.cvmxtended.entity.BoxTruckEntity;
import com.xengeance.cvmxtended.entity.SemiTruckEntity;
import com.xengeance.cvmxtended.init.ModEntities;

import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements Proxy {
	
	public void setupClient(){
		this.registerEntityRenders();
        this.registerRayTraceConstructors();
	}
	
	public void registerEntityRenders() 
	{
		/* Register Vehicles */
        this.registerVehicleRender(ModEntities.BoxTruck.get(), new RenderLandVehicleWrapper<BoxTruckEntity, RenderBoxTruck>(new RenderBoxTruck()));
        this.registerVehicleRender(ModEntities.SemiTruck.get(), new RenderLandVehicleWrapper<SemiTruckEntity, RenderSemiTruck>(new RenderSemiTruck()));
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
    }
}
