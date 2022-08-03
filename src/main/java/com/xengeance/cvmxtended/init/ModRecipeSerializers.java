package com.xengeance.cvmxtended.init;

import com.mrcrayfish.vehicle.crafting.VehicleRecipeSerializer;
import com.xengeance.cvmxtended.Reference;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {
	
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);
    
    public static final RegistryObject<VehicleRecipeSerializer> CRAFTING = RECIPE_SERIALIZERS.register("crafting", VehicleRecipeSerializer::new);

}
