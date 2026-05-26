package moffy.overloaded_tinkering_lib.event;

import moffy.overloaded_tinkering_lib.capability.AdvancedToolCapabilities;
import moffy.overloaded_tinkering_lib.client.model.MaterialOverrideModel;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverloadedTinkerEventHandlers {

    @SubscribeEvent
    public void registerCapability(RegisterCapabilitiesEvent event){
        AdvancedToolCapabilities.registerAdvancedCapabilities(event);
    }

    @SubscribeEvent
    public void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("mat_override_obj", MaterialOverrideModel.LOADER);
    }
}
