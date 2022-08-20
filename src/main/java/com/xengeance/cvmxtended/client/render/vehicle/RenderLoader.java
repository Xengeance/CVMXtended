package com.xengeance.cvmxtended.client.render.vehicle;

import java.util.Vector;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.vehicle.client.SpecialModels;
import com.mrcrayfish.vehicle.client.render.AbstractRenderVehicle;
import com.mrcrayfish.vehicle.client.render.Axis;
import com.mrcrayfish.vehicle.util.RenderUtil;
import com.xengeance.cvmxtended.client.SpecialModelsDefs;
import com.xengeance.cvmxtended.entity.LoaderEntity;
import com.xengeance.cvmxtended.util.MathUtil;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderLoader extends AbstractRenderVehicle<LoaderEntity>
{
	//pivot offsets for the articulated implement parts
	private static Vector3f bucketOffset = new Vector3f(MathUtil.PixelToBlockCoords(0, -3.25, 16));
	private static Vector3f armOffset = new Vector3f(MathUtil.PixelToBlockCoords(0, 5.75, 4.5));
	
	//TODO: Use this to lerp to instead of snapping the rotation
	private static double angleInterval = 30;
	
    @Override
    protected boolean shouldRenderFuelLid()
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(LoaderEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, float partialTicks, int light)
    {
        //Body
        this.renderDamagedPart(entity, SpecialModelsDefs.LOADER_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //render lift arm
        matrixStack.push();
	    	double opp = entity.getBucketHeightOffset();
	    	double hyp = new Vector3d(armOffset).distanceTo(new Vector3d(bucketOffset));
	    	double armElevation = opp * angleInterval; //Math.asin(opp / hyp);
	    	
	    	matrixStack.translate(armOffset.getX(), armOffset.getY(), armOffset.getZ());
			matrixStack.rotate(Axis.POSITIVE_X.rotationDegrees(-(float)armElevation));
	        
	    	
	        this.renderDamagedPart(entity, SpecialModelsDefs.LOADER_LIFT_ARM.getModel(), matrixStack, renderTypeBuffer, light);
		        //Render the handles bars
        matrixStack.pop();
        
        //render bucket
        /*matrixStack.push();
        	Vector2f p = MathUtil.FindPointOnCircle(hyp, armElevation);
        	double pY = p.y;
        	double pZ = p.x;
        	Vector3f offset = bucketOffset.copy();
        	//offset.sub(armOffset);
	    	//matrixStack.translate(armOffset.getX(), armOffset.getY(), armOffset.getZ());
	    	matrixStack.translate(offset.getX(), offset.getY(), offset.getZ());
        	//matrixStack.translate(0, pY, pZ);
			//matrixStack.rotate(Axis.POSITIVE_X.rotationDegrees(-(float)armElevation));
        	//matrixStack.translate(0, Math.sin(armElevation) * hyp, Math.tan(armElevation) * hyp);
        	this.renderDamagedPart(entity, SpecialModelsDefs.LOADER_BUCKET.getModel(), matrixStack, renderTypeBuffer, light);
        matrixStack.pop();*/
        
	    matrixStack.push();
	            // Positions the steering wheel in the correct position
	        matrixStack.translate(0, 0.33, 0.0);
	        matrixStack.rotate(Axis.POSITIVE_X.rotationDegrees(-45F));
	        //matrixStack.translate(0, -0.02, 0);
	        matrixStack.scale(0.45F, 0.45F, 0.45F);
	
	        // Rotates the steering wheel based on the wheel angle
	        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
	        float wheelAngleNormal = wheelAngle / 45F;
	        float turnRotation = wheelAngleNormal * 25F;
	        matrixStack.rotate(Axis.POSITIVE_Y.rotationDegrees(turnRotation));
	
	        RenderUtil.renderColoredModel(SpecialModels.GO_KART_STEERING_WHEEL.getModel(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.NO_OVERLAY);

	        
        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(LoaderEntity entity, PlayerEntity player, PlayerModel<AbstractClientPlayerEntity> model, float partialTicks)
    {
        model.bipedRightArm.rotationPointZ = 0f;
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
