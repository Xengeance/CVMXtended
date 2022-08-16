package com.xengeance.cvmxtended;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

//@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class Config {
	
	public static class ServerConfig {
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> LOADER_BREAK_COMPATIBLE_BLOCKS;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> FUEL_TYPE_CONVERSION_VALUES;
	
		public ServerConfig (ForgeConfigSpec.Builder builder) {
			builder.push("Fuel Types");
			this.FUEL_TYPE_CONVERSION_VALUES = builder
					.comment("Fuel Types & Fuelium Conversion Ratios")
					.defineList("fueltypeconversionvalues", Lists.newArrayList(), o -> o instanceof String);
			builder.pop();
			builder.push("Vehicle Configs");
			builder.push("Loader");
			this.LOADER_BREAK_COMPATIBLE_BLOCKS = builder
					.comment("List of Blocks that can be broken by the Loader's bucket, as defined by their Forge Tag(s)")
					.defineList("loaderBreakCompaibleBlocks", Lists.newArrayList("forge:dirt"), o -> o instanceof String);
			builder.pop();
			
		}
	}
	public static class ClientConfig {

		public final BooleanValue ENABLE_DEBUG_RENDERER;
		
		public ClientConfig (ForgeConfigSpec.Builder builder) {
			builder.push("Debug");

			this.ENABLE_DEBUG_RENDERER = builder.push("Enable Debug Renderer")
					.comment("Enables or disables the mod debug renderer")
					.define("enabledebugrenderer", false);
			
			builder.pop();
		}
	}
	
	public static  ForgeConfigSpec CLIENT_SPEC;
	public static  ForgeConfigSpec SERVER_SPEC;
	public static final ClientConfig CLIENT;
	static
	{
		final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT = specPair.getLeft();
		CLIENT_SPEC = specPair.getRight();
	}
	public static final ServerConfig SERVER;
	static
	{
		final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		SERVER = specPair.getLeft();
		SERVER_SPEC = specPair.getRight();
	}
}
