package com.xengeance.cvmxtended.util;

import net.minecraft.util.math.vector.Vector3d;

public class MathUtil {
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
		double x = value.getX() * 16f;
		double y = value.getY() * 16f;
		double z = value.getZ() * 16f;
		Vector3d result = new Vector3d(x,y,z);
		return result;
	}
	
	public static Vector3d PixelToBlockCoords(Vector3d value) {
		double x = value.getX() / 16f;
		double y = value.getY() / 16f;
		double z = value.getZ() / 16f;
		Vector3d result = new Vector3d(x,y,z);
		return result;
	}

	public static Vector3d BlockBenchToPixelCoords(float x, float y, float z) {
		return BlockBenchToPixelCoords(new Vector3d(x,y,z));
	}
	public static Vector3d BlockBenchToPixelCoords(Vector3d value) {
		Vector3d result = new Vector3d(value.getX() - 8, value.getY(), value.getZ());
		return result;
	}

	public static Vector3d BlockBenchToBlockCoords(float x, float y, float z) {
		return BlockBenchToBlockCoords(new Vector3d(x,y,z));
	}
	public static Vector3d BlockBenchToBlockCoords(Vector3d value) {
		Vector3d result = PixelToBlockCoords(BlockBenchToPixelCoords(value));
		return result;
	}
}
