package com.xengeance.cvmxtended.client.render.vehicle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.vehicle.client.SpecialModels;
import com.mrcrayfish.vehicle.client.render.AbstractRenderVehicle;
import com.mrcrayfish.vehicle.client.render.Axis;
import com.mrcrayfish.vehicle.util.RenderUtil;
import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.entity.BoxTruckEntity;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderBoxTruck extends AbstractRenderVehicle<BoxTruckEntity>
{
    @Override
    protected boolean shouldRenderFuelLid()
    {
        return true;
    }

    @Override
    public void render(BoxTruckEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks, int light)
    {
        //Body
        this.renderDamagedPart(entity, SpecialModelsDefs.BOX_TRUCK_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        this.renderDamagedPart(entity, SpecialModelsDefs.CARGO_DOOR.getModel(), matrixStack, renderTypeBuffer, light);

        //Render the handles bars
        matrixStack.push();
            // Positions the steering wheel in the correct position
        matrixStack.translate(0.24, 0.33, 0.87);
        matrixStack.rotate(Axis.POSITIVE_X.rotationDegrees(-45F));
        //matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.65F, 0.65F, 0.65F);

        // Rotates the steering wheel based on the wheel angle
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.rotate(Axis.POSITIVE_Y.rotationDegrees(turnRotation));

        RenderUtil.renderColoredModel(SpecialModels.GO_KART_STEERING_WHEEL.getModel(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.NO_OVERLAY);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(BoxTruckEntity entity, PlayerEntity player, PlayerModel<AbstractClientPlayerEntity> model, float partialTicks)
    {
        model.bipedRightArm.rotateAngleZ = 0f;
        model.bipedLeftArm.rotateAngleZ = 0F;
        model.bipedLeftLeg.rotateAngleX = (float) Math.toRadians(-80F);
        model.bipedRightLeg.rotateAngleX = (float) Math.toRadians(-80F);

	    if(entity.getControllingPassenger() == player)
	    {
	        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
	        float wheelAngleNormal = wheelAngle / 45F;
	        float turnRotation = wheelAngleNormal * 6F;
	        model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-65F - turnRotation);
	        model.bipedRightArm.rotateAngleY = (float) Math.toRadians(-7F);
	        model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-65F + turnRotation);
	        model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(7F);
	    }
    }
}
