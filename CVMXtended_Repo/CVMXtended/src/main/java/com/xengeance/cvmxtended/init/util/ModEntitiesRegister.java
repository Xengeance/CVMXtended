package com.xengeance.cvmxtended.init.util;

import java.util.function.BiFunction;

import com.mrcrayfish.vehicle.util.EntityUtil;

import com.xengeance.cvmxtended.Reference;
import com.xengeance.cvmxtended.init.ModEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModEntitiesRegister {
	//TODO: Find a way to return MOD_ID of mods that extend this class instead or implement some kind of resource-pack solution
	/*
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);
	
	private static <T extends Entity> RegistryObject<EntityType<T>> registerVehicle(String id, BiFunction<EntityType<T>, World, T> function, float width, float height)
    {
        EntityType<T> type = EntityUtil.buildVehicleType(new ResourceLocation(Reference.MOD_ID, id), function, width, height);
        return XModEntities.ENTITY_TYPES.register(id, () -> type);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerDependent(String modId, String id, BiFunction<EntityType<T>, World, T> function, float width, float height, boolean registerCrate)
    {
        if(ModList.get().isLoaded(modId))
        {
            EntityType<T> type = EntityUtil.buildVehicleType(new ResourceLocation(Reference.MOD_ID, id), function, width, height, registerCrate);
            return XModEntities.ENTITY_TYPES.register(id, () -> type);
        }
        return null;
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String id, BiFunction<EntityType<T>, World, T> function, float width, float height)
    {
        EntityType<T> type = EntityType.Builder.create(function::apply, EntityClassification.MISC).size(width, height).setTrackingRange(256).setUpdateInterval(1).disableSummoning().immuneToFire().setShouldReceiveVelocityUpdates(true).build(id);
        return XModEntities.ENTITY_TYPES.register(id, () -> type);
    }*/
}
