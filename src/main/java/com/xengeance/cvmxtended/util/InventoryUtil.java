package com.xengeance.cvmxtended.util;


import java.util.Random;


import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class InventoryUtil {
	private static final Random RANDOM = new Random();
	
	//takes each item of an inventory, spawns a ItemEnity of it on the ground at pos and then voids the inventory slot it was in
	public static void dropInventoryItems(World worldIn, Vector3d pos, Inventory inventory)
    {
        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if(!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, pos, itemstack, 60, false);
                //set the stack in the inventory to be empty since we dropped it
                itemstack.setCount(0);
            }
        }
        
        //trigger broadcasting the change to any listeners
        inventory.markDirty();
    }

	//Convenience overrides for spawnItemStack() with various extra spawn values
	public static void spawnItemStack(World worldIn, Vector3d pos, ItemStack stack) {
		spawnItemStack(worldIn, pos.x, pos.y, pos.z, stack, -1, false);
	}
	
	public static void spawnItemStack(World worldIn, Vector3d pos, ItemStack stack, int pickUpDelay) {
		spawnItemStack(worldIn, pos.x, pos.y, pos.z, stack, pickUpDelay, false);
	}
	
	public static void spawnItemStack(World worldIn, Vector3d pos, ItemStack stack, boolean splitStack) {
		spawnItemStack(worldIn, pos.x, pos.y, pos.z, stack, -1, splitStack);
	}
	
	public static void spawnItemStack(World worldIn, Vector3d pos, ItemStack stack, int pickUpDelay, boolean splitStack) {
		//set the pickupdelay to be equivalent to setDefaultPickUpDelay() if the passed value is less than 0
		if (pickUpDelay < 0)
			pickUpDelay = 10;
		spawnItemStack(worldIn, pos.x, pos.y, pos.z, stack, pickUpDelay, splitStack);
	}
	
	
	//spawns new ItemEntity(s) in the world
	//Note: the spawned ItemStack is a clone of the original, handle results appropriately
    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack, int pickUpDelay, boolean splitStack)
    {
    	ItemStack stack1 = stack.copy();
    	
        if(splitStack)
        	//split the stack into smaller spawned portions
	        while(!stack1.isEmpty())
	        {
	        	ItemEntity entity = ItemEntityBuilder(worldIn, x, y, z, stack1.split(RANDOM.nextInt(21) + 10), pickUpDelay);
		        
	            entity.setMotion(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.05D + 0.2D, RANDOM.nextGaussian() * 0.05D);
	            
	            worldIn.addEntity(entity);
	        }
        else
        {
        	//spawn the entire stack, This will consume the passed in stack all at once
        	ItemEntity entity = ItemEntityBuilder(worldIn, x, y, z, stack1.copy(), pickUpDelay);
        	entity.setMotion(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.05D + 0.2D, RANDOM.nextGaussian() * 0.05D);

            worldIn.addEntity(entity);
        }
    }
    
    //Custom item spawning function. Instantiates and returns a new ItemEnity with some random position vairation and optional
    //custom pickup delay
    public static ItemEntity ItemEntityBuilder(World worldIn, double x, double y, double z, ItemStack stack, int pickUpDelay) {

    	float offsetX = -0.25F + RANDOM.nextFloat() * 0.5F;
        float offsetY = RANDOM.nextFloat() * 0.8F;
        float offsetZ = -0.25F + RANDOM.nextFloat() * 0.5F;
    	ItemEntity entity = new ItemEntity(worldIn, x + offsetX, y + offsetY, z + offsetZ, stack);
        	
        entity.setMotion(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.05D + 0.2D, RANDOM.nextGaussian() * 0.05D);
        if(pickUpDelay > 10)
        	entity.setPickupDelay(pickUpDelay);
        else
            entity.setDefaultPickupDelay();

        return entity;
    }
}
