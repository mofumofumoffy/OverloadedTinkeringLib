package moffy.overloaded_tinkering_lib;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Config {
    public static ForgeConfigSpec.ConfigValue<Boolean> USE_SHADER;

    public static void registerConfig(FMLJavaModLoadingContext context){
        final ForgeConfigSpec.Builder CLIENT = new ForgeConfigSpec.Builder();

        CLIENT.comment("Client Settings").push("client");
        USE_SHADER = CLIENT.comment("Rendering with shaders for some tools/armors").define("useShader", true);

        context.registerConfig(ModConfig.Type.CLIENT, CLIENT.build());
    }
}
