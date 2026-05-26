package moffy.overloaded_tinkering_lib.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class AdvancedToolCapabilities {
    public static final Capability<ToolEnchantmentCapability> TOOL_ENCHANTMENT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerAdvancedCapabilities(RegisterCapabilitiesEvent event){
        event.register(ToolEnchantmentCapability.class);
    }
}

