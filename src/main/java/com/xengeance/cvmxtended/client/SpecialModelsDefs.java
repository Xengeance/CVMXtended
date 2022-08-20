package com.xengeance.cvmxtended.client;

import com.mrcrayfish.vehicle.client.ISpecialModel;
import com.xengeance.cvmxtended.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModelsDefs implements ISpecialModel{
	
	BOX_TRUCK_BODY("box_truck_body"),
	CARGO_DOOR("cargo_door"),
	
	SEMI_TRUCK_BODY("semi_truck_body"),
	
	LOADER_BODY("loader_body"),
	LOADER_LIFT_ARM("loader_lift_arm"),
	LOADER_BUCKET("loader_bucket");

    // Add spray can lid
    /**
     * The location of an item model in the [MOD_ID]/models/vehicle/[NAME] folder
     */
    private ResourceLocation modelLocation;

    /**
     * Determines if the model should be loaded as a special model
     */
    private boolean specialModel;

    /**
     * Cached model
     */
    @OnlyIn(Dist.CLIENT)
    private IBakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModelsDefs(String modelName)
    {
        this(new ResourceLocation(Reference.MOD_ID, "vehicle/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param resource name of the model file
     */
    SpecialModelsDefs(ResourceLocation resource, boolean specialModel)
    {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if(model == Minecraft.getInstance().getModelManager().getMissingModel())
            {
                return model;
            }
            this.cachedModel = model;
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event)
    {
        for(SpecialModelsDefs model : values())
        {
            if(model.specialModel)
            {
                ModelLoader.addSpecialModel(model.modelLocation);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearModelCache()
    {
        for(SpecialModelsDefs model : values())
        {
            model.cachedModel = null;
        }
    }
}
