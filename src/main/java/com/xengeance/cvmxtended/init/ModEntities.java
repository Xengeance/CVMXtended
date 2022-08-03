package com.xengeance.cvmxtended.init;

import com.mrcrayfish.vehicle.entity.*;
import com.mrcrayfish.vehicle.entity.trailer.FertilizerTrailerEntity;
import com.mrcrayfish.vehicle.entity.trailer.FluidTrailerEntity;
import com.mrcrayfish.vehicle.entity.trailer.SeederTrailerEntity;
import com.mrcrayfish.vehicle.entity.trailer.StorageTrailerEntity;
import com.mrcrayfish.vehicle.entity.trailer.VehicleEntityTrailer;
import com.mrcrayfish.vehicle.entity.vehicle.*;
import com.mrcrayfish.vehicle.util.EntityUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.xengeance.cvmxtended.Reference;
import com.xengeance.cvmxtended.entity.*;
//import com.xengeance.cvmxtended.init.util.ModEntitiesRegister;

import java.util.function.BiFunction;

public class ModEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<BoxTruckEntity>> BoxTruck = registerVehicle("box_truck", BoxTruckEntity::new, 2.0F, 1.0F);
    public static final RegistryObject<EntityType<SemiTruckEntity>> SemiTruck = registerVehicle("semi_truck", SemiTruckEntity::new, 2.0F, 1.0F);
    
    private static <T extends Entity> RegistryObject<EntityType<T>> registerVehicle(String id, BiFunction<EntityType<T>, World, T> function, float width, float height)
    {
    	//NOTE: Register crates seperately, otherwise the crate resides in CVM namespace
        EntityType<T> type = EntityUtil.buildVehicleType(new ResourceLocation(Reference.MOD_ID, id), function, width, height, false);
        return ModEntities.ENTITY_TYPES.register(id, () -> type);
    }
}
