package com.xengeance.cvmxtended.init;

import com.mrcrayfish.vehicle.common.*;
import com.mrcrayfish.vehicle.common.entity.*;
import com.mrcrayfish.vehicle.entity.*;
import com.mrcrayfish.vehicle.client.render.*;

import net.minecraft.util.math.vector.Vector3d;

public class ModVehicleProperties {

    public static void register()
    {
        VehicleProperties properties;
        
        /* BoxTruck */
        properties = new VehicleProperties();
        properties.setAxleOffset(-1.0F);
        properties.setWheelOffset(5.4F);
        properties.setBodyPosition(new PartPosition(1.9));
        properties.setFuelPortPosition(new PartPosition(-9.0, 0.25, 2.5, 0, -90, 0, 0.18));
        //properties.setKeyPortPosition(new PartPosition(0, 7, 6.2, -67.5, 0, 0, 0.5));
        properties.setHeldOffset(new Vector3d(0.0, 3.5, 0.0));
        properties.setDisplayPosition(new PartPosition(0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F, 0.65F));
        properties.addWheel(Wheel.Side.LEFT, Wheel.Position.FRONT, 8.0F, 16.0F, 2F, true, true);
        properties.addWheel(Wheel.Side.RIGHT, Wheel.Position.FRONT, 8.0F, 16.0F, 2F, true, true);
        properties.addWheel(Wheel.Side.LEFT, Wheel.Position.REAR, 8.0F, -12.0F, 2F, true, true);
        properties.addWheel(Wheel.Side.RIGHT, Wheel.Position.REAR, 8.0F, -12.0F, 2F, true, true);
        properties.setFrontAxelVec(0, 16);
        properties.setRearAxelVec(0, -12);
        properties.addSeat(new Seat(new Vector3d(-3.75, 5.65, 10.5), true));
        properties.addSeat(new Seat(new Vector3d(3.75, 5.65, 10.5), false));
        VehicleProperties.setProperties(ModEntities.BoxTruck.get(), properties);
        

        /* Semi Truck */
        properties = new VehicleProperties();
        properties.setAxleOffset(-1.0F);
        properties.setWheelOffset(4.4F);
        properties.setBodyPosition(new PartPosition(2.6));
        properties.setFuelPortPosition(new PartPosition(-5.25, 0.1, 2, 0, -90, 0, 0.12));
        properties.setTowBarPosition(new Vector3d(0.0f, 1f, -6f));
        //properties.setKeyPortPosition(new PartPosition(0, 7, 6.2, -67.5, 0, 0, 0.5));
        properties.setHeldOffset(new Vector3d(0.0, 3.5, 0.0));
        properties.setDisplayPosition(new PartPosition(0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F, 0.55F));
        properties.addWheel(Wheel.Side.LEFT, Wheel.Position.FRONT, 5.0F, 18.0F, 1.5F, true, true);
        properties.addWheel(Wheel.Side.RIGHT, Wheel.Position.FRONT, 5.0F, 18.0F, 1.5F, true, true);
        //mid axle wheels
        properties.addWheel(Wheel.Side.LEFT, Wheel.Position.REAR, 5.0F, -7.0F, 1.5F, true, true);
        properties.addWheel(Wheel.Side.RIGHT, Wheel.Position.REAR, 5.0F, -7.0F, 1.5F, true, true);
        //rear axle wheels
        properties.addWheel(Wheel.Side.LEFT, Wheel.Position.REAR, 5.0F, -16.0F, 1.5F, true, true);
        properties.addWheel(Wheel.Side.RIGHT, Wheel.Position.REAR, 5.0F, -16.0F, 1.5F, true, true);
        properties.setFrontAxelVec(0, 5);
        properties.setRearAxelVec(0, -11.5);
        properties.addSeat(new Seat(new Vector3d(-3.75, 5.65, 10.5), true));
        properties.addSeat(new Seat(new Vector3d(3.75, 5.65, 10.5), false));
        VehicleProperties.setProperties(ModEntities.SemiTruck.get(), properties);
    }
}
