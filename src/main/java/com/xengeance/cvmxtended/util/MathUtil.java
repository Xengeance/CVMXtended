package com.xengeance.cvmxtended.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public class MathUtil {
	
	/*
	 * Convenience functions for converting between MC world-based position and blockbench model position systems
	 */
	
	public static float BlockToPixelCoords(float value) {
		return value / 16f;
	}
	
	public static float PixelToBlockCoords(float value) {
		return value * 16f;
	}

	public static double BlockToPixelCoords(double value) {
		return value / 16f;
	}
	
	public static double PixelToBlockCoords(double value) {
		return value * 16f;
	}

	public static Vector3d BlockToPixelCoords(float x, float y, float z) {
		float rx = x * 16f;
		float ry = y * 16f;
		float rz = z * 16f;
		Vector3d result = new Vector3d(rx,ry,rz);
		return result;
	}
	
	public static Vector3d PixelToBlockCoords(float x, float y, float z) {
		float rx = x / 16f;
		float ry = y / 16f;
		float rz = z / 16f;
		Vector3d result = new Vector3d(rx,ry,rz);
		return result;
	}

	public static Vector3d BlockToPixelCoords(double x, double y, double z) {
		double rx = x * 16f;
		double ry = y * 16f;
		double rz = z * 16f;
		Vector3d result = new Vector3d(rx,ry,rz);
		return result;
	}
	
	public static Vector3d PixelToBlockCoords(double x, double y, double z) {
		double rx = x / 16f;
		double ry = y / 16f;
		double rz = z / 16f;
		Vector3d result = new Vector3d(rx,ry,rz);
		return result;
	}

	public static Vector3d BlockToPixelCoords(Vector3d value) {
		double x = value.x * 16f;
		double y = value.y * 16f;
		double z = value.z * 16f;
		Vector3d result = new Vector3d(x,y,z);
		return result;
	}
	
	public static Vector3d PixelToBlockCoords(Vector3d value) {
		double x = value.x * 16f;
		double y = value.y * 16f;
		double z = value.z * 16f;
		Vector3d result = new Vector3d(x,y,z);
		return result;
	}

	public static Vector3d BlockBenchToPixelCoords(float x, float y, float z) {
		return BlockBenchToPixelCoords(new Vector3d(x,y,z));
	}
	public static Vector3d BlockBenchToPixelCoords(Vector3d value) {
		Vector3d result = new Vector3d(value.x - 8, value.y, value.z);
		return result;
	}

	public static Vector3d BlockBenchToBlockCoords(float x, float y, float z) {
		return BlockBenchToBlockCoords(new Vector3d(x,y,z));
	}
	public static Vector3d BlockBenchToBlockCoords(Vector3d value) {
		Vector3d result = PixelToBlockCoords(BlockBenchToPixelCoords(value));
		return result;
	}
	
	//Constructs and returns a co-planar vector of length distance in the direction of axisAngle degrees around the up-axis
	public static Vector2f GetVectorOffset(float distance, float axisAngle) {
		double x = (distance * Math.cos((float) (ClampAngle(axisAngle) * Math.PI / 180.0D)));;
		double z = (distance * Math.sin((float) (ClampAngle(axisAngle) * Math.PI / 180.0D)));;
		Vector2f result = new Vector2f((float)x, (float)z);
		return result;
	}

	//Converts any angle to it's Range[0, 360] degree equivalent
	public static double ClampAngle(double angle) {
		if(angle > 360)
			while(angle > 360)
				angle -= 360;
		if(angle < 0)
			while (angle < 0)
				angle += 360;
		
		return angle;
	}
	
	public static Vector2f FindPointOnCircle(float radius, float angle) {
		return FindPointOnCircle((double) radius, (double) angle);
	}
	

	
	public static Vector2f FindPointOnCircle(double radius, double angle) {
		double x = Math.cos(angle) * radius;
		double y = Math.sin(angle) * radius;
		return new Vector2f((float)x, (float)y);
	}
}
